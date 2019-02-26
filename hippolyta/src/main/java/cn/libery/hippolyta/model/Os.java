package cn.libery.hippolyta.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * @author shizhiqiang on 2018/11/5.
 * @description
 */
@Entity(tableName = "os")
public class Os {

    @PrimaryKey(autoGenerate = true)
    public long id;

    /**
     * 系统版本号
     */
    @ColumnInfo(name = "os_version")
    public String osVersion;

    /**
     * 手机厂商
     */
    @ColumnInfo(name = "os_brand")
    public String osBrand;

    /**
     * 手机型号
     */
    @ColumnInfo(name = "os_model")
    public String osModel;
}
