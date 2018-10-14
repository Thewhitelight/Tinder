package cn.libery.avatar;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.IntDef;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author shizhiqiang on 2018/7/31.
 * @description
 */
public class Avatar {

    public static final String PACKAGE_NAME = "package_name";
    private int selectedMode;
    private String imageDir;
    private ImageCallBack imageCallBack;
    private boolean hasCrop;
    private int width, height;
    private int aspectX, aspectY;

    private Avatar() {
    }

    public static Avatar getInstance() {
        return AvatarHolder.INSTANCE;
    }

    private static class AvatarHolder {
        static final Avatar INSTANCE = new Avatar();
    }

    public Avatar imageFile(ImageCallBack callBack) {
        imageCallBack = callBack;
        return this;
    }

    public Avatar setSelectMode(@SelectMode int mode) {
        this.selectedMode = mode;
        return this;
    }

    /**
     * 图片保存目录
     *
     * @param dir 目录名称 默认应用包名
     */
    public Avatar setImageFileDir(String dir) {
        this.imageDir = dir;
        return this;
    }

    /**
     * 是否进行剪裁
     *
     * @param hasCrop 是否剪裁 默认不剪裁
     */
    public Avatar setHasCrop(boolean hasCrop) {
        this.hasCrop = hasCrop;
        return this;
    }

    /**
     * 剪裁后输出图片尺寸
     *
     * @param width  剪裁后输出图片宽度
     * @param height 剪裁后输出图片高度
     */
    public Avatar setImageSize(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    /**
     * 剪裁框比例
     *
     * @param aspectX 剪裁框X
     * @param aspectY 剪裁框Y
     */
    public Avatar setAspectSize(int aspectX, int aspectY) {
        this.aspectX = aspectX;
        this.aspectY = aspectY;
        return this;
    }


    void setImageFile(File imageFile) {
        imageCallBack.getImageFile(imageFile);
    }

    public void build(Context context) {
        Intent intent = new Intent(context, SelectAvatarActivity.class);
        intent.putExtra(PACKAGE_NAME, context.getPackageName());
        intent.putExtra("select_mode", selectedMode);
        intent.putExtra("img_dir", imageDir);
        intent.putExtra("has_crop", hasCrop);
        intent.putExtra("width", width);
        intent.putExtra("height", height);
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        context.startActivity(intent);
    }

    /**
     * 需要在onDestroy调用 否则可能会造成内存泄漏
     */
    public void clear() {
        imageCallBack = null;
    }

    public interface ImageCallBack {
        /**
         * 获取剪裁后图片文件
         *
         * @param imageFile 图片文件
         */
        void getImageFile(File imageFile);

    }

    // region SelectMode

    public static final int ALL = 0;
    public static final int CAMERA = 1;
    public static final int GALLERY = 2;

    @IntDef({CAMERA, GALLERY, ALL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SelectMode {
    }

    //endregion

}
