package com.yc.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认-缓存管理器
 *
 * @version 1.0 create at 2020/2/28
 * @auther yangchuan
 */
public class DefaultCacheManager<V> implements CacheManager<V> {

    //并发安全
    private final Map cache = new ConcurrentHashMap<String,V>();


    @Override
    public void put(CacheKey key, V value) {
        cache.put(key.getKey(),value);
    }

    @Override
    public V putIfAbsent(CacheKey key, V value) {
        return (V) cache.putIfAbsent(key.getKey(),value);
    }

    @Override
    public V get(CacheKey key) {
        return (V) cache.get(key.getKey());
    }

    @Override
    public void put(String key, V value) {
        cache.put(key,value);
    }

    @Override
    public V putIfAbsent(String key, V value) {
        return (V) cache.putIfAbsent(key,value);
    }

    @Override
    public V get(String key) {
        return (V) cache.get(key);
    }

    @Override
    public void remove(CacheKey arg) {
        cache.remove(arg);
    }
}
