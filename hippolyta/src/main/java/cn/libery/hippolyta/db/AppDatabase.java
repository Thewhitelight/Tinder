package cn.libery.hippolyta.db;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import cn.libery.hippolyta.model.Hephaestus;
import cn.libery.hippolyta.model.Os;

/**
 * @author shizhiqiang on 2018/11/5.
 * @description
 */
@Database(entities = {Os.class, Hephaestus.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract OsDao osDao();

    public abstract HephaestusDao HephaestusDao();
}
