package cn.libery.hippolyta.db;

import android.arch.persistence.room.Room;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;

import cn.libery.hippolyta.Hippolyta;
import cn.libery.hippolyta.model.Hephaestus;
import cn.libery.hippolyta.model.Os;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * @author shizhiqiang on 2018/11/5.
 * @description
 */
public class Db {

    private static volatile Db db;
    private AppDatabase appDatabase;

    private Db() {
        appDatabase = Room.databaseBuilder(Hippolyta.getApplicationContext(), AppDatabase.class, "hippolyta")
                .build();
    }

    public static Db getInstance() {
        if (db == null) {
            synchronized (Db.class) {
                if (db == null) {
                    db = new Db();
                }
            }
        }
        return db;
    }

    public static void updateOs(final Os os) {
        db = getInstance();
        Flowable.just(os)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new Function<Os, Integer>() {
                    @Override
                    public Integer apply(Os os) {
                        //查询插入更新
                        return db.appDatabase.osDao().updateOs(os);
                    }
                })
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Integer lineNumber) {
                        if (Hippolyta.sIsDebug) {
                            android.util.Log.e("insert line", lineNumber + "");
                        }
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public static Flowable<List<Os>> queryOs() {
        db = getInstance();
        return Flowable.just("")
                .map(new Function<String, List<Os>>() {
                    @Override
                    public List<Os> apply(String s) {
                        return db.appDatabase.osDao().queryOss();
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }

    public static void insertHephaestus(Hephaestus log) {
        db = getInstance();
        Flowable.just(log)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new Function<Hephaestus, Long>() {
                    @Override
                    public Long apply(Hephaestus hephaestus) {
                        return db.appDatabase.HephaestusDao().insertHephaestus(hephaestus);
                    }
                }).subscribe(new Subscriber<Long>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Long rowId) {
                if (Hippolyta.sIsDebug) {
                    android.util.Log.e("insert rowId", String.valueOf(rowId));
                }
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        });

    }

    public static Flowable<List<Hephaestus>> queryHephaestus() {
        db = getInstance();
        return Flowable.just("")
                .map(new Function<String, List<Hephaestus>>() {
                    @Override
                    public List<Hephaestus> apply(String s) {
                        return db.appDatabase.HephaestusDao().queryHephaestus();
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }
}
