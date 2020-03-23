package com.yc.cache.example;

import com.yc.cache.CacheReqeust;

/**
 * Description TODO
 *
 * @author yangchuan
 * @version 1.0 create at 2020/3/23
 */
public class TestService {

    public Object get(CacheReqeust reqeust) throws InterruptedException {
        Thread.sleep(1000);
        return reqeust;
    }
}
