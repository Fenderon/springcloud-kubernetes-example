package com.yc.cache;

/**
 * 缓存 请求示例对象
 *
 * @version 1.0 create at 2020/2/28
 * @auther yangchuan
 */
public class CacheReqeust implements CacheKey {

    @Override
    public String getKey() {
        return "test";
    }
}
