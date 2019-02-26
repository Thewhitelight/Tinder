package cn.libery.hippolyta.util;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author shizhiqiang on 2018/11/7.
 * @description
 */
public class TimeUtil {

    public static String yyyyMMddHHmmss(long date) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf1.format(date);
    }

}
