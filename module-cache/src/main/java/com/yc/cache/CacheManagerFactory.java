package com.yc.cache;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 缓存管理器创建工厂
 *
 * @version 1.0 create at 2020/2/28
 * @auther yangchuan
 */
public class CacheManagerFactory {

    //并发安全
    private final static List<CacheManager> cacheManagerList = new CopyOnWriteArrayList<>();

    public static <V> DefaultCacheManager createDefaultCacheManager() {
        DefaultCacheManager< V> cacheManager = new DefaultCacheManager<>();
        cacheManagerList.add(cacheManager);
        return cacheManager;
    }
}
