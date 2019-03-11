package cn.libery.camera

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.*
import android.hardware.Camera
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.SurfaceHolder
import android.widget.Toast
import cn.libery.camera.camera.CameraManager
import kotlinx.android.synthetic.main.activity_take_photo.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


/**
 * @author shizhiqiang on 2019/2/24.
 * @description
 */
class CameraActivity : AppCompatActivity() {

    private lateinit var mSurfaceHolder: SurfaceHolder
    private var height = 1
    private var isTakeFront: Boolean = false
    private var canCrop: Boolean = false
    private lateinit var cameraManager: CameraManager
    private val permissionCamera = 1
    private var maxWidth = 1280

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take_photo)
        cameraManager = CameraManager(this)
        initData()
    }

    private fun initData() {
        initSurfaceView()
        isTakeFront = intent.getBooleanExtra("isTakeFront", false)
        canCrop = intent.getBooleanExtra("canCrop", false)
        maxWidth = intent.getIntExtra("maxWidth", 1280)
        camera_container.post { height = camera_container.height }
        camera_finish.setOnClickListener { onBackPressed() }
        if (isTakeFront) {
            card_image.setImageResource(R.drawable.bg_take_photo_card_front)
        } else {
            card_image.setImageResource(R.drawable.bg_take_photo_card_back)
        }
        take_photo.setOnClickListener {
            takePhoto()
        }
    }

    private fun takePhoto() {
        val camera = cameraManager.camera
        camera.setOneShotPreviewCallback { data, _ ->
            val bitmap = generateBitmap(camera, data)
            if (bitmap == null) {
                Toast.makeText(this@CameraActivity, "拍照失败", Toast.LENGTH_SHORT).show()
            } else {
                cameraResult(bitmap)
            }
        }
    }

    private fun cameraResult(bitmap: Bitmap) {
        var resource = bitmap
        if (canCrop) {
            resource = cropImage(resource)
        }
        val path = saveBitmap(resource)
        if (path.isNotEmpty()) {
            Log.e("photo saved in path:", path)
        } else {
            Toast.makeText(this@CameraActivity, "照片保存失败", Toast.LENGTH_SHORT).show()
        }
        val intent = Intent()
        intent.putExtra("imagePath", path)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun generateBitmap(camera: Camera, data: ByteArray): Bitmap? {
        val size = camera.parameters.previewSize
        val w = size.width
        val h = size.height
        val image = YuvImage(data, ImageFormat.NV21,
                w, h, null)
        val os = ByteArrayOutputStream()
        if (!image.compressToJpeg(Rect(0, 0, w, h), 100, os)) {
            return null
        }
        val tmp = os.toByteArray()
        return BitmapFactory.decodeByteArray(tmp, 0, tmp.size, BitmapFactory.Options())
    }

    private fun saveBitmap(resource: Bitmap): String {
        val picturePathName = cacheDir.absolutePath + File.separator + System.currentTimeMillis() + ".jpg"
        val file = File(picturePathName)
        var result = ""
        var isSuccess = false
        try {
            val fos = FileOutputStream(file)
            isSuccess = resource.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
            result = file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return if (isSuccess) result else ""
    }

    private fun cropImage(resource: Bitmap): Bitmap {
        val ratio = resource.width / (height * 1.0)
        val pictureWidth = dp2px(450) * ratio
        val pictureHeight = dp2px(282) * ratio
        val x = (resource.width - pictureWidth) / 2
        val y = (resource.height - pictureHeight) / 2
        val bitmap: Bitmap
        if (x < 0 || y < 0) {
            bitmap = resource
        } else {
            bitmap = Bitmap.createBitmap(resource, x.toInt(), y.toInt(), pictureWidth.toInt(), pictureHeight.toInt())
            if (pictureWidth > 1280) {
                val originRatio = (pictureWidth / (pictureHeight * 1.0)).toFloat()
                val matrix = Matrix()
                matrix.postScale(originRatio, originRatio)
                return Bitmap.createScaledBitmap(bitmap, 1280, (1280 / originRatio).toInt(), true)
            }
        }
        return bitmap
    }

    private fun dp2px(dp: Int): Int {
        val density = Resources.getSystem().displayMetrics.density
        return (dp * density).toInt()
    }

    private fun initSurfaceView() {
        mSurfaceHolder = camera_surface.holder
        mSurfaceHolder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {

            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
            }

            override fun surfaceCreated(holder: SurfaceHolder?) {
                checkCameraPermission()
            }
        })

    }

    private fun openCamera() {
        cameraManager.openDriver(mSurfaceHolder)
        cameraManager.startPreview()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraManager.stopPreview()
        cameraManager.closeDriver()
    }

    //region checkPermission
    private fun checkCameraPermission() {
        val camera = Manifest.permission.CAMERA
        if (ContextCompat.checkSelfPermission(this, camera) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, arrayOf(camera), permissionCamera)
        } else {
            openCamera()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && requestCode == permissionCamera) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                AlertDialog.Builder(this)
                        .setTitle("请求拍照权限")
                        .setMessage("需要获取相机权限才能使用相机功能")
                        .setPositiveButton("同意") { _, _ ->
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            intent.data = Uri.parse("package:$packageName")
                            this@CameraActivity.startActivity(intent)
                        }
                        .setNegativeButton("拒绝", null)
                        .show()
            }
        }
    }
    //endregion

}