package cn.libery.avatar.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author shizhiqiang on 2018/8/1.
 * @description
 */
public class AppUtil {
    public static boolean openAppInfo(Context context, String packageName) {
        if (context != null && !TextUtils.isEmpty(packageName)) {
            Intent intent = new Intent();
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.parse("package:" + packageName));
            context.startActivity(intent);
            return true;
        }
        return false;
    }

    public static String yyyyMMddHHmmss(long date) {
        return new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(date);
    }
}
