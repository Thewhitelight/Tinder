package cn.libery.hippolyta.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * @author shizhiqiang on 2018/11/6.
 * @description 赫菲斯托斯 匠神
 */
@Entity(tableName = "hephaestus")
public class Hephaestus {

    @PrimaryKey(autoGenerate = true)
    public long id;

    /**
     * 级别
     */
    @ColumnInfo(name = "level")
    public String level;

    /**
     * 全类名
     */
    @ColumnInfo(name = "class_name")
    public String className;

    /**
     * 方法名
     */
    @ColumnInfo(name = "method_name")
    public String methodName;

    /**
     * 行号
     */
    @ColumnInfo(name = "line_number")
    public int lineNumber;

    /**
     * 线程Id
     */
    @ColumnInfo(name = "thread_id")
    public long threadId;

    /**
     * 日志标记
     */
    @ColumnInfo(name = "tag")
    public String tag;

    /**
     * 日志信息
     */
    @ColumnInfo(name = "msg")
    public String msg;

    /**
     * 时间
     */
    @ColumnInfo(name = "time")
    public String time;

    /**
     * app版本号
     */
    @ColumnInfo(name = "app_version")
    public String appVersion;

    /**
     * 网络类型
     */
    @ColumnInfo(name = "network_type")
    public String networkType;

    /**
     * 用户Id
     */
    @ColumnInfo(name = "userId")
    public String userId;


    @Override
    public String toString() {
        return "Hephaestus{" +
                "id=" + id +
                ", level='" + level + '\'' +
                ", className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", lineNumber=" + lineNumber +
                ", threadId=" + threadId +
                ", tag='" + tag + '\'' +
                ", msg='" + msg + '\'' +
                ", time='" + time + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", networkType='" + networkType + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
