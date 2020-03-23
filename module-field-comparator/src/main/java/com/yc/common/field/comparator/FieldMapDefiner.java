package com.yc.common.field.comparator;

import java.util.Map;

/**
 * 属性映射定义器
 *
 * @author yangchuan
 * @version 1.0 create at 2020/3/21
 */
public interface FieldMapDefiner {

    /**
     * 获取原对象类
     *
     * @return
     */
    Class<?> getOriginClass();

    /**
     * 获取目标对象类
     *
     * @return
     */
    Class<?> getTargetClass();

    /**
     * 比较的字段
     *
     * @return
     */
    Map<String, String> columnDef();
}
