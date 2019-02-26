package cn.libery.tinder

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import cn.libery.avatar.Avatar
import cn.libery.badgeview.BadgeView
import cn.libery.camera.CameraActivity
import cn.libery.carousel.detail.ImageDetailActivity
import cn.libery.carousel.model.Banner
import cn.libery.carousel.model.Banner.IMAGE
import cn.libery.carousel.model.Banner.VIDEO
import cn.libery.slideback.SlideBack
import cn.libery.tinder.classloader.HackActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : HackActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SlideBack.Builder()
                .init(R.layout.activity_main)
                .build(this)
        val avatar = Avatar.getInstance()
        img.setOnClickListener {
            avatar
                    .setSelectMode(Avatar.ALL)
                    .setImageFileDir("Tinder")
                    .setHasCrop(false)
                    .setImageSize(500, 500)
                    .setAspectSize(1, 1)
                    .imageFile { imageFile ->
                        run {
                            Log.e("MainActivity-file:", imageFile.absolutePath)
                            img.setImageBitmap(BitmapFactory.decodeFile(imageFile.absolutePath))
                        }
                    }
                    .build(this)
        }
        lifecycle.addObserver(avatar)
        val bitmap = drawableToBitmap(img0.drawable)
        Log.e("img0:", (bitmap.byteCount / 1024 * 1.0 / 1024).toString())

        val bitmap2 = drawableToBitmap(img.drawable)
        Log.e("img2:", (bitmap2.byteCount / 1024 * 1.0 / 1024).toString())
        img.setImageBitmap(bitmap2)

        val badge = BadgeView(this)
        badge.setDisplacement(BadgeView.dp2px(50), BadgeView.dp2px(10))
        badge.bindView(img2_layout)

        val badge2 = BadgeView(this)
        badge2.setDisplacement(0, 0)
        badge2.setBackgroundResource(R.drawable.ic_badge_1)
        badge2.bindView(img2_layout)

        val badge3 = BadgeView(this)
        badge3.setDisplacement(50, 50)
        badge3.text = "66"
        badge3.setBackgroundResource(R.drawable.ic_badge_2)
        val v = badge3.bindView(img2_layout)
        image_id_card.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            intent.putExtra("isTakeFront", true)
            intent.putExtra("canCrop", true)
            intent.putExtra("maxWidth", 1080)
            startActivityForResult(intent, 1)
        }

        val banners = ArrayList<Banner>(6)
        banners.add(Banner(IMAGE, "http://wx1.sinaimg.cn/orj360/5d098bccly1fuihjws6ihj21yw2567wh.jpg"))
        banners.add(Banner(VIDEO, "http://jmtvideo.oss-cn-hangzhou.aliyuncs.com/DDIezgXLSxUZCOU.mp4"))
        banners.add(Banner(IMAGE, "https://upload-images.jianshu.io/upload_images/7078011-430b292666591af5.jpeg?imageMogr2/auto-orient/strip%7CimageView2/2/w/700"))
        banners.add(Banner(VIDEO, "http://img.soogif.com/HkkOHvJc0bSj10yemFqUfe3rcf8B628o.mp4"))
        banners.add(Banner(IMAGE, "http://n.sinaimg.cn/tech/transform/529/w284h245/20180822/t3QG-fzrwica1378454.gif"))
        banners.add(Banner(VIDEO, "http://f.us.sinaimg.cn/000XduoYlx07n2fcfO1G010402001PpW0k010.mp4?label=mp4_720p&template=1280x720.28&Expires=1534949695&ssig=bxZduauD09&KID=unistore,video"))

        banners.add(Banner(layoutInflater.inflate(R.layout.layout_item_text, null)))
        carousel.banners = banners
        carousel.delaySecond = 7

        val indicator = TextView(this)
        indicator.text = "1/" + banners.size
        indicator.gravity = Gravity.CENTER
        indicator.setBackgroundColor(Color.GRAY)
        val indicatorParams = FrameLayout.LayoutParams(100, 60)
        indicatorParams.gravity = (Gravity.BOTTOM or Gravity.LEFT)
        carousel.setIndicator(indicator, indicatorParams)
        carousel.setOnScrollPositionListener { position -> indicator.text = (position + 1).toString() + "/" + banners.size }
        carousel.setOnItemClickListener { position ->
            Toast.makeText(this, banners[position].url, Toast.LENGTH_SHORT).show()
            val intent = Intent(this, ImageDetailActivity::class.java)
            intent.putExtra("url", banners[position].url)
            startActivity(intent)
        }
        slide.setOnClickListener { startActivity(Intent(this, SlideBackActivity::class.java)) }
    }

    fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        val bitmap = Bitmap.createBitmap(60, 60, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
        drawable.draw(canvas)

        return bitmap
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            val bitmap = BitmapFactory.decodeFile(data.getStringExtra("imagePath"))
            Log.e("image size", "${bitmap.width} ${bitmap.height}")
            image_id_card.setImageBitmap(bitmap)
        }
    }
}
