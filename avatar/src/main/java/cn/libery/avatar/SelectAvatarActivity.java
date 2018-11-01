package cn.libery.avatar;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

import cn.libery.avatar.util.AppUtil;
import cn.libery.avatar.util.ImageUtil;

import static cn.libery.avatar.Avatar.CAMERA;
import static cn.libery.avatar.Avatar.GALLERY;

/**
 * @author shizhiqiang on 2018/7/31.
 * @description 选择头像
 */
public class SelectAvatarActivity extends Activity {

    private static final int REQUEST_CODE_GET_IMAGE_CAMERA = 1000;
    private static final int REQUEST_CODE_GET_IMAGE_CROP = 1001;
    private static final int REQUEST_CODE_GET_IMAGE_SDCARD = 1002;

    public static final int CAMERA_PERMISSION = 1003;
    public static final int GALLERY_PERMISSION = 1004;

    private int cameraPermission;
    private int writePermission;
    private int readPermission;

    private boolean hasCrop;
    private String packageName;
    private String imgPath;
    private File mAvatarFile;
    private Uri mOrigUri;
    private int height = 500;
    private int width = 500;
    private int aspectX = 1, aspectY = 1;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        writePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        readPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }
        packageName = intent.getStringExtra(Avatar.PACKAGE_NAME);
        String fileDir = intent.getStringExtra("img_dir");
        int selectMode = intent.getIntExtra("select_mode", 0);
        hasCrop = intent.getBooleanExtra("has_crop", false);
        height = intent.getIntExtra("height", 500);
        width = intent.getIntExtra("width", 500);
        aspectX = intent.getIntExtra("aspectX", 1);
        aspectY = intent.getIntExtra("aspectY", 1);

        if (TextUtils.isEmpty(fileDir)) {
            fileDir = packageName;
        }
        imgPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + fileDir + File.separator;

        if (selectMode == CAMERA) {
            startCamera();
        } else if (selectMode == GALLERY) {
            startGallery();
        } else {
            avatarDialog();
        }
    }

    private void avatarDialog() {
        String[] items = {"拍照", "相册选取"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectAvatar(which);
            }
        });
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setCancelable(false);
        alertDialog = builder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }

    private void selectAvatar(int witch) {
        switch (witch) {
            // 拍照
            case 0:
                startCamera();
                break;
            // 相册
            case 1:
                startGallery();
                break;
            default:
        }
    }

    private void startCamera() {
        if (cameraPermission == PackageManager.PERMISSION_DENIED ||
                writePermission == PackageManager.PERMISSION_DENIED ||
                readPermission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    CAMERA_PERMISSION);
        } else {
            takePhoto();
        }
    }

    private void startGallery() {
        if (writePermission == PackageManager.PERMISSION_DENIED ||
                readPermission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    GALLERY_PERMISSION);
        } else {
            pickGallery();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION:
                if (grantResults.length > 2 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    takePhoto();
                } else {
                    requirePermission();
                }
                break;
            case GALLERY_PERMISSION:
                if (grantResults.length > 1 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickGallery();
                } else {
                    requirePermission();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void requirePermission() {
        new AlertDialog.Builder(this)
                .setMessage("需要权限,是否去授权？")
                .setNegativeButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        AppUtil.openAppInfo(SelectAvatarActivity.this, packageName);
                        finish();
                    }
                })
                .setPositiveButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        finish();
                    }
                })
                .setCancelable(false)
                .show();
    }


    private void takePhoto() {
        // 判断是否挂载了SD卡
        String savePath = "";
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            savePath = imgPath + "Camera/";
            ImageUtil.generateFile(savePath);
        }
        // 没有挂载SD卡，无法保存文件
        if (TextUtils.isEmpty(savePath)) {
            Toast.makeText(this, "无法保存照片，请检查SD卡是否挂载", Toast.LENGTH_SHORT).show();
            return;
        }
        String timestamp = AppUtil.yyyyMMddHHmmss(System.currentTimeMillis());
        String cameraPath = "camera_" + timestamp + ".jpg";
        File cameraFile = new File(savePath, cameraPath);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mOrigUri = Uri.fromFile(cameraFile);
        } else {
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, cameraFile.getAbsolutePath());
            mOrigUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mOrigUri);
        startActivityForResult(intent, REQUEST_CODE_GET_IMAGE_CAMERA);
    }

    private void pickGallery() {
        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        } else {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        }
        startActivityForResult(intent, REQUEST_CODE_GET_IMAGE_SDCARD);
    }

    /**
     * 拍照后裁剪
     *
     * @param uri 原始图片
     */
    private void startActionCrop(Uri uri) {
        new ClipImageTask().execute(uri);
    }

    /**
     * 裁剪头像的绝对路径
     */
    public Uri getTempUri(Uri uri) {
        if (uri == null) {
            return null;
        }
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "无法保存照片，请检查SD卡是否挂载", Toast.LENGTH_SHORT).show();
            return null;
        }
        String filePath = imgPath + "Avatar/";
        ImageUtil.generateFile(filePath);
        String thePath = ImageUtil.getAbsImagePath(this, uri);
        if (uri == mOrigUri) {
            ImageUtil.compressHeadPhoto(thePath);
        }
        String ext = ImageUtil.getFileFormat(thePath);
        ext = TextUtils.isEmpty(ext) ? "jpg" : ext;
        // 照片命名
        String timeStamp = AppUtil.yyyyMMddHHmmss(System.currentTimeMillis());
        String cropFileName = "crop_" + timeStamp + "." + ext;
        mAvatarFile = new File(filePath, cropFileName);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            uri = Uri.fromFile(mAvatarFile);
        } else {
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, mAvatarFile.getAbsolutePath());
            uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        }
        return uri;
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            switch (requestCode) {
                // 拍照后裁剪
                case REQUEST_CODE_GET_IMAGE_CAMERA: {
                    String path = ImageUtil.getAbsImagePath(this, mOrigUri);
                    try {
                        deleteImageFile(path);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
                case REQUEST_CODE_GET_IMAGE_CROP:
                    if (mAvatarFile != null) {
                        try {
                            deleteImageFile(mAvatarFile);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                default:
            }
            finish();
            return;
        }
        switch (requestCode) {
            // 拍照后裁剪
            case REQUEST_CODE_GET_IMAGE_CAMERA:
                if (hasCrop) {
                    startActionCrop(mOrigUri);
                } else {
                    String path = ImageUtil.getAbsImagePath(this, mOrigUri);
                    ProgressImageTask task = new ProgressImageTask();
                    task.execute(path);
                }
                break;
            // 选照片后裁剪
            case REQUEST_CODE_GET_IMAGE_SDCARD:
                Uri uri = data == null ? null : data.getData();
                if (uri == null) {
                    finish();
                    return;
                }
                if (hasCrop) {
                    startActionCrop(uri);
                } else {
                    String path = ImageUtil.getAbsImagePath(this, uri);
                    Avatar.getInstance().setImageFile(new File(path));
                    finish();
                }
                break;
            case REQUEST_CODE_GET_IMAGE_CROP:
                Avatar.getInstance().setImageFile(mAvatarFile);
                finish();
                break;
            default:
        }
    }

    private void deleteImageFile(File file) {
        if (file.delete()) {
            Log.e(file.getAbsolutePath(), " 文件已被删除！");
        } else {
            Log.e(file.getAbsolutePath(), "文件删除失败！");
        }
    }

    private void deleteImageFile(String filePath) {
        File file = new File(filePath);
        deleteImageFile(file);
    }

    public class ProgressImageTask extends AsyncTask<String, Void, Void> {

        ProgressDialog dialog;
        private String path;

        @Override
        protected Void doInBackground(String... strings) {
            path = strings[0];
            path = ImageUtil.compressHeadPhoto(path);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(SelectAvatarActivity.this);
            dialog.setMessage("请稍候...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            if (!TextUtils.isEmpty(path)) {
                Avatar.getInstance().setImageFile(new File(path));
            }
            finish();
        }
    }


    public class ClipImageTask extends AsyncTask<Uri, Void, Void> {

        ProgressDialog dialog;
        Intent intent;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(SelectAvatarActivity.this);
            dialog.setMessage("请稍候...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Uri... uris) {
            Uri uri = uris[0];
            intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri, "image/*");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri(uri));
            intent.putExtra("crop", "true");
            String huawei = "HUAWEI";
            if (android.os.Build.MODEL.contains(huawei) || Build.MANUFACTURER.equals(huawei)) {
                intent.putExtra("aspectX", 9998);
                intent.putExtra("aspectY", 9999);
            } else {
                intent.putExtra("aspectX", aspectX);
                intent.putExtra("aspectY", aspectY);
            }
            // 输出图片大小
            intent.putExtra("outputX", height);
            intent.putExtra("outputY", width);
            // 去黑边
            intent.putExtra("scale", true);
            // 去黑边
            intent.putExtra("scaleUpIfNeeded", true);
            // 是否将数据保留在Bitmap中返回
            intent.putExtra("return-data", false);
            // 圆形裁剪区域
            intent.putExtra("circleCrop", false);
            // 不检测人脸
            intent.putExtra("noFaceDetection", false);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            startActivityForResult(intent, REQUEST_CODE_GET_IMAGE_CROP);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
        }
    }

}
