package cn.libery.hippolyta.worker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import androidx.work.Worker;
import androidx.work.WorkerParameters;
import cn.libery.hippolyta.db.Db;
import cn.libery.hippolyta.model.Hephaestus;

/**
 * @author shizhiqiang on 2018/11/7.
 * @description
 */
public class UploadWorker extends Worker {

    List<Hephaestus> list = new ArrayList<>();

    public UploadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Db.queryHephaestus().subscribe(new Subscriber<List<Hephaestus>>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(List<Hephaestus> hephaestuses) {
                list = hephaestuses;
                for (Hephaestus h : list) {
                    Log.e("next", h.toString());
                }
                //upload
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        });
        return Result.success();
    }

}
