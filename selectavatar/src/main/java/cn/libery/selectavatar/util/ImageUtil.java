package cn.libery.selectavatar.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author shizhiqiang on 2018/7/31.
 * @description
 */
public class ImageUtil {

    private static final String SDCARD_MNT = "/mnt/sdcard";
    public static String SD_PATH = Environment.getExternalStorageDirectory() + File.separator;

    public static String getAbsImagePath(Activity context, Uri uri) {
        if (context == null || uri == null) {
            return null;
        }
        String path = getAbsolutePathFromNoStandardUri(uri);
        // 如果是标准Uri
        if (TextUtils.isEmpty(path)) {
            path = getAbsoluteImagePath(context, uri);
        }
        return path;
    }

    /**
     * 判断当前Url是否标准的content://样式，如果不是，则返回绝对路径
     */
    public static String getAbsolutePathFromNoStandardUri(Uri uri) {
        if (uri == null) {
            return null;
        }
        String filePath = null;
        String uriString = uri.toString();
        uriString = Uri.decode(uriString);

        String pre1 = "file://" + SD_PATH + File.separator;
        String pre2 = "file://" + SDCARD_MNT + File.separator;

        if (uriString.startsWith(pre1)) {
            filePath = SD_PATH + File.separator + uriString.substring(pre1.length());
        } else if (uriString.startsWith(pre2)) {
            filePath = SD_PATH + File.separator + uriString.substring(pre2.length());
        }
        return filePath;
    }

    /**
     * 通过uri获取文件的绝对路径
     */
    @SuppressWarnings("deprecation")
    public static String getAbsoluteImagePath(Activity context, Uri uri) {
        if (context == null || uri == null) {
            return null;
        }
        String imagePath = "";
        String[] data = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri,
                data,
                null,
                null,
                null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                imagePath = cursor.getString(columnIndex);
            }
            cursor.close();
        }
        return imagePath;
    }

    /**
     * 获取文件扩展名
     */
    public static String getFileFormat(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return "";
        }
        int point = fileName.lastIndexOf('.');
        return fileName.substring(point + 1);
    }

    /**
     * 获取图片信息
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                default:
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 图片旋转
     */
    public static Bitmap rotateImageView(int angle, Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        if (bitmap != null) {
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } else {
            return null;
        }
    }

    public static void compressHeadPhoto(String path) {
        try {
            Bitmap bm = BitmapFactory.decodeFile(path);
            bm = rotateImageView(readPictureDegree(path), bm);
            if (bm != null) {
                bm.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(new File(path)));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
        }
    }

    public static void generateFile(String filePath) {
        File saveDir = new File(filePath);
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
    }

}
