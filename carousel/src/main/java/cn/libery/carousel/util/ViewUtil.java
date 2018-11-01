package cn.libery.carousel.util;

import android.view.View;
import android.view.ViewGroup;

/**
 * @author shizhiqiang on 2018/10/31.
 * @description
 */
public class ViewUtil {
    public static void removeViewFromParent(View view) {
        if (view == null) {
            return;
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
    }
}
