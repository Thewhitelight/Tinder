package cn.libery.tinder

import android.app.Application
import com.squareup.leakcanary.LeakCanary

/**
 * @author shizhiqiang on 2018/9/30.
 * @description
 */
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        LeakCanary.install(this)
    }
}