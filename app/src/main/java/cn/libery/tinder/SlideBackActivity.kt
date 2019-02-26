package cn.libery.tinder

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.util.Log
import android.view.View
import cn.libery.slideback.SlideBack
import cn.libery.tinder.classloader.HackClassLoader
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import java.util.concurrent.TimeUnit

/**
 * @author shizhiqiang on 2018/9/30.
 * @description
 */
class SlideBackActivity : AppCompatActivity() {

    val ds = CompositeDisposable()

    private lateinit var cachedClassLoaderWrapper: ClassLoader

    override fun getClassLoader(): ClassLoader {
        Log.e("getClassLoader", "test")

        cachedClassLoaderWrapper = HackClassLoader(super.getClassLoader())
        return cachedClassLoaderWrapper
    }

    override fun onCreateView(name: String?, context: Context?, attrs: AttributeSet?): View? {
        return super.onCreateView(name, context, attrs)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SlideBack.Builder().init(R.layout.activity_slideback)
                .setOnlyLeftBack(true)
                .setBackEdgeWidth(60f)
                .build(this)
        Log.e("onCreate", "onCreate")

        Observable.just("1", "2", "3", "4", "5", "6", "7")
                .subscribeOn(Schedulers.io())
                .delay(2, TimeUnit.SECONDS)
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { s -> s + "x" }
                .flatMap { s -> ObservableSource<String> { observer -> observer.onNext(s + "t") } }
                .subscribe(object : Observer<String> {
                    override fun onSubscribe(d: Disposable) {
                        ds.addAll(d)
                    }

                    override fun onNext(s: String) {
                        Log.e("s", s)
                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onComplete() {

                    }
                })

        Flowable.just("1", "2", "3", "4", "5", "6", "7")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap { s -> Publisher<Int> { observer -> observer.onNext(s.toInt()) } }
                .map { x -> x * x }
                .subscribe(object : Subscriber<Int> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(s: Subscription?) {
                        s?.request(Long.MAX_VALUE)
                    }

                    override fun onNext(t: Int?) {
                        Log.e("flowable", t.toString())
                    }

                    override fun onError(t: Throwable?) {
                    }

                })
    }

    override fun onDestroy() {
        super.onDestroy()
        ds.clear()
    }

}
