package cn.libery.tinder

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import android.support.v7.widget.AppCompatTextView
import android.util.Log
import android.widget.TextView
import cn.libery.tinder.classloader.HackClassLoader
import cn.libery.tinder.widget.TestTextView
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
        HackClassLoader.addGlobalClassRouting(TextView::class.java, TestTextView::class.java)
        HackClassLoader.addGlobalClassRouting(AppCompatTextView::class.java, TestTextView::class.java)
        //Hippolyta.init(this, BuildConfig.DEBUG)
        LeakCanary.install(this)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(base)
        val isLoad = hookPackageClassLoader(base!!, HackClassLoader(classLoader))
        Log.e("Application", isLoad.toString())
    }

    private fun hookPackageClassLoader(context: Context, appClassLoaderNew: ClassLoader): Boolean {
        try {
            val packageInfoField = Class.forName("android.app.ContextImpl").getDeclaredField("mPackageInfo")
            packageInfoField.isAccessible = true
            val loadedApkObject = packageInfoField.get(context)
            val loadedApkClass = Class.forName("android.app.LoadedApk")
            val appClassLoaderField = loadedApkClass.getDeclaredField("mClassLoader")
            appClassLoaderField.isAccessible = true
            appClassLoaderField.set(loadedApkObject, appClassLoaderNew)
            return true
        } catch (ignored: Throwable) {
            Log.e("hookPackageClassLoader", ignored.toString())
        }
        return false
    }
}