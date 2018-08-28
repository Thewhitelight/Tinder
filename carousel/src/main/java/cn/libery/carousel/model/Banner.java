package cn.libery.carousel.model;

import android.support.annotation.IntDef;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author shizhiqiang on 2018/8/22.
 * @description
 */
public class Banner {
    private String url;
    private int type;
    private View view;

    public Banner(View view) {
        this.type = CUSTOM;
        this.view = view;
    }

    public Banner(int type, String url) {
        this.url = url;
        this.type = type;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static final int IMAGE = 0;
    public static final int VIDEO = 1;
    public static final int CUSTOM = 2;

    @IntDef({IMAGE, VIDEO, CUSTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface BannerType {
    }


}
