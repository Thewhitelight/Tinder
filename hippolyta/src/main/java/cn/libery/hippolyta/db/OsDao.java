package cn.libery.hippolyta.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import cn.libery.hippolyta.model.Os;

/**
 * @author shizhiqiang on 2018/11/5.
 * @description
 */
@Dao
public interface OsDao {

    @Insert
    Long insertOs(Os os);

    @Query("SELECT * FROM Os")
    List<Os> queryOss();

    @Update
    int updateOs(Os os);
}
