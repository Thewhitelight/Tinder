package cn.libery.tinder.classloader;

import android.util.Log;

import java.util.HashMap;

/**
 * @author shizhiqiang on 2018/11/19.
 * @description
 */
public class HackClassLoader extends ClassLoader {

    public HackClassLoader(ClassLoader parent) {
        super(parent);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        Log.e("HackClassLoader", name);
        if (instanceClassRouting.containsKey(name)) {
            return instanceClassRouting.get(name);
        }

        if (HackClassLoader.globalClassRouting.containsKey(name)) {
            return HackClassLoader.globalClassRouting.get(name);
        }
        return super.loadClass(name);
    }

    private static HashMap<String, Class> globalClassRouting = new HashMap<>();

    public static void addGlobalClassRouting(Class fromClass, Class toClass) {
        HackClassLoader.globalClassRouting.put(fromClass.getName(), toClass);
    }

    private HashMap<String, Class> instanceClassRouting = new HashMap<>();

    public void addClassRouting(Class fromClass, Class toClass) {
        instanceClassRouting.put(fromClass.getName(), toClass);
    }


    public HackClassLoader withClassRouting(Class fromClass, Class toClass) {
        instanceClassRouting.put(fromClass.getName(), toClass);
        return this;
    }

}
