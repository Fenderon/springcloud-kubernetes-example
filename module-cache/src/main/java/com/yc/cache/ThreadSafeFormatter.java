package com.yc.cache;

import java.text.SimpleDateFormat;

/**
 * Description TODO
 *
 * @version 1.0 create at 2020/2/28
 * @auther yangchuan
 */
public class ThreadSafeFormatter {

    public static ThreadLocal<SimpleDateFormat> dateFormatTheadLocal =
            new ThreadLocal<SimpleDateFormat>(){

                @Override
                protected SimpleDateFormat initialValue() {
                    return new SimpleDateFormat("mm:ss");
                }

                @Override
                public SimpleDateFormat get() {
                    return super.get();
                }
            };
}
