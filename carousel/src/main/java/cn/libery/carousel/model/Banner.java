package cn.libery.carousel.model;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author shizhiqiang on 2018/8/22.
 * @description
 */
public class Banner {
    private String url;
    private int type;

    public Banner(String url, int type) {
        this.url = url;
        this.type = type;
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

    @IntDef({IMAGE, VIDEO})
    @Retention(RetentionPolicy.SOURCE)
    public @interface BannerType {
    }


}
