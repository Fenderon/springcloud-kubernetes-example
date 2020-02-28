package com.yc.cache;

/**
 * 缓存管理
 *
 * @version 1.0 create at 2020/2/28
 * @auther yangchuan
 */
public interface CacheManager<V> {

    void put(CacheKey key, V value);

    V putIfAbsent(CacheKey key, V value);

    V get(CacheKey key);

    void put(String key, V value);

    V putIfAbsent(String key, V value);

    V get(String key);

    void remove(CacheKey arg);
}
