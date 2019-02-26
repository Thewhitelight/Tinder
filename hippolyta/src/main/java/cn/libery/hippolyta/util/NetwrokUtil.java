package cn.libery.hippolyta.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author shizhiqiang on 2018/11/7.
 * @description
 */
public class NetwrokUtil {

    /**
     * 获取当前的网络连接类型
     */
    public static String getNetWorkType(Context context) {
        ConnectivityManager connMgr =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }
        if (networkInfo == null) {
            return "无网络";
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            if ("cmnet".equalsIgnoreCase(networkInfo.getExtraInfo())) {
                return "NET";
            } else {
                return "WAP";
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            return "WIFI";
        }
        return "无网络";
    }
}
