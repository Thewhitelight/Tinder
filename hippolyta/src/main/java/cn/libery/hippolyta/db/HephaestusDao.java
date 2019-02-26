package cn.libery.hippolyta.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import cn.libery.hippolyta.model.Hephaestus;

/**
 * @author shizhiqiang on 2018/11/5.
 * @description
 */
@Dao
public interface HephaestusDao {

    @Insert
    Long insertHephaestus(Hephaestus log);

    @Query("SELECT * FROM Hephaestus")
    List<Hephaestus> queryHephaestus();

    @Delete
    void deleteHephasetus(List<Hephaestus> hephaestuses);

}
