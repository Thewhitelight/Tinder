package cn.libery.tinder;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author shizhiqiang on 2018/11/15.
 * @description
 */
public class LruCache<K, V> extends LinkedHashMap<K, V> implements Map<K, V> {

    public LruCache() {
        super(10, 0.75f, true);
    }

    @Override
    protected boolean removeEldestEntry(Entry<K, V> eldest) {
        return size() > 20;
    }

}
