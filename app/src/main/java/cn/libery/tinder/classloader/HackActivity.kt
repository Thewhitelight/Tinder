package cn.libery.tinder.classloader

import android.support.v7.app.AppCompatActivity

/**
 * @author shizhiqiang on 2018/11/19.
 * @description
 */
open class HackActivity : AppCompatActivity() {

    private lateinit var cachedClassLoaderWrapper: ClassLoader

    override fun getClassLoader(): ClassLoader {
        cachedClassLoaderWrapper = HackClassLoader(super.getClassLoader())
        return cachedClassLoaderWrapper
    }

}