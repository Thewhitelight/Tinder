package cn.libery.hippolyta;

import android.content.Context;
import android.support.annotation.IntDef;
import android.util.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import cn.libery.hippolyta.db.Db;
import cn.libery.hippolyta.model.Hephaestus;
import cn.libery.hippolyta.model.Os;
import cn.libery.hippolyta.util.NetwrokUtil;
import cn.libery.hippolyta.util.TimeUtil;
import cn.libery.hippolyta.worker.UploadWorker;
import io.reactivex.Flowable;

/**
 * @author shizhiqiang on 2018/11/2.
 * @description 希波吕忒
 */
public class Hippolyta {

    static final int VERBOSE = 0;
    static final int DEBUG = 1;
    static final int INFO = 2;
    static final int WARN = 3;
    static final int ERROR = 4;

    @IntDef({VERBOSE, DEBUG, INFO, WARN, ERROR})
    @interface Level {
    }

    private static Context sApplicationContext;
    public static boolean sIsDebug;
    private static final Hephaestus LOG = new Hephaestus();
    private static volatile Hippolyta hippolyta;

    static int x = 1;

    private Hippolyta() {
        PeriodicWorkRequest.Builder uploadWorkBuilder =
                new PeriodicWorkRequest.Builder(UploadWorker.class, 20, TimeUnit.MINUTES);
        PeriodicWorkRequest uploadWorkRequest = uploadWorkBuilder.build();
        WorkManager.getInstance().enqueue(uploadWorkRequest);
        x++;
        Log.e("计数", x + "");
    }

    public static Hippolyta init(Context context, boolean isDebug) {
        if (hippolyta == null) {
            synchronized (Hippolyta.class) {
                if (hippolyta == null) {
                    sApplicationContext = context.getApplicationContext();
                    sIsDebug = isDebug;
                    hippolyta = new Hippolyta();
                }
            }
        }
        return hippolyta;
    }

    public static Context getApplicationContext() {
        return sApplicationContext;
    }

    public static void v(String msg) {
        v("", msg);
    }

    public static void v(String tag, String msg) {
        log(VERBOSE, tag, msg);
    }

    public static void d(String msg) {
        d("", msg);
    }

    public static void d(String tag, String msg) {
        log(DEBUG, tag, msg);
    }

    public static void i(String msg) {
        i("", msg);
    }

    public static void i(String tag, String msg) {
        log(INFO, tag, msg);
    }

    public static void w(String msg) {
        w("", msg);
    }

    public static void w(String tag, String msg) {
        log(WARN, tag, msg);
    }

    public static void e(String msg) {
        e("", msg);
    }

    public static void e(String tag, String msg) {
        log(ERROR, tag, msg);
    }


    private static void log(@Level int level, String tag, String msg) {
        final StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[4];

        final String fullClassName = stackTraceElement.getClassName();
        final String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        final int lineNumber = stackTraceElement.getLineNumber();
        final String methodName = stackTraceElement.getMethodName();
        final long threadId = Thread.currentThread().getId();
        printLog(level, tag, msg, className, lineNumber, methodName, threadId);
        String levelName;
        switch (level) {
            case VERBOSE:
                levelName = "VERBOSE";
                break;
            case DEBUG:
                levelName = "DEBUG";
                break;
            case INFO:
                levelName = "INFO";
                break;
            case WARN:
                levelName = "WARN";
                break;
            case ERROR:
                levelName = "ERROR";
                break;
            default:
                levelName = "VERBOSE";
        }
        LOG.className = fullClassName;
        LOG.level = levelName;
        LOG.lineNumber = lineNumber;
        LOG.methodName = methodName;
        LOG.networkType = NetwrokUtil.getNetWorkType(sApplicationContext);
        LOG.threadId = threadId;
        LOG.time = TimeUtil.yyyyMMddHHmmss(System.currentTimeMillis());
        LOG.tag = tag;
        LOG.msg = msg;
        Db.insertHephaestus(LOG);
    }

    private static void printLog(@Level int level, String tag, String msg, String className, int lineNumber, String method, long threadId) {
        if (sIsDebug) {
            final StringBuilder builder = new StringBuilder()
                    .append("[")
                    .append(threadId)
                    .append("]")
                    .append(className)
                    .append("<")
                    .append(lineNumber)
                    .append(">");

            final StringBuilder msgBuilder = new StringBuilder();
            msgBuilder.append(method);
            msgBuilder.append("():");
            msgBuilder.append(tag);
            msgBuilder.append(" ");
            msgBuilder.append(msg);
            switch (level) {
                case VERBOSE:
                    Log.v(builder.toString(), msgBuilder.toString());
                    break;
                case DEBUG:
                    Log.d(builder.toString(), msgBuilder.toString());
                    break;
                case INFO:
                    Log.i(builder.toString(), msgBuilder.toString());
                    break;
                case WARN:
                    Log.w(builder.toString(), msgBuilder.toString());
                    break;
                case ERROR:
                    Log.e(builder.toString(), msgBuilder.toString());
                    break;
                default:
            }
        }
    }

    public static Flowable<List<Os>> queryOs() {
        return Db.queryOs();
    }

}
