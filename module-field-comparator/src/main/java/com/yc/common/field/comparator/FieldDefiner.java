package com.yc.common.field.comparator;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 属性定义器
 *
 * @author yangchuan
 * @version 1.0 create at 2020/3/21
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface FieldDefiner {

    /**
     * @return
     */
    String type();
}
