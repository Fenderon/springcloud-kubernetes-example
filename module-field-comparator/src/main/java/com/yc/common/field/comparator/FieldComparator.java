package com.yc.common.field.comparator;

import java.util.HashMap;
import java.util.Map;

/**
 * 属性比较器
 *
 * @author yangchuan
 * @version 1.0 create at 2020/3/21
 */
public interface FieldComparator<T extends FieldMapDefiner> {

    /**
     * 比较两个对象的属性值差异，
     *
     * <p>
     * key:   目标对象的field
     * value: 原对象的值
     * </p>
     *
     * @param origin          原对象
     * @param target          目标对象
     * @return 返回以目标对象属性名称作为key，原对象属性值作为value的map
     */
    Map<String, Object> compare(Object origin, Object target) throws IllegalAccessException;


    /**
     * 比较两个对象的属性值差异，
     *
     * <p>
     * key:   目标对象的field
     * value: 原对象的值
     * </p>
     *
     * @param origin          原对象
     * @param target          目标对象
     * @param fieldMapDefiner 选择的字段映射器
     * @return 返回以目标对象属性名称作为key，原对象属性值作为value的map
     */
    Map<String, Object> compare(Object origin, Object target, Class<T> fieldMapDefiner) throws IllegalAccessException;

    /**
     * 比较两个对象的字段差异
     *
     * @param origin          原对象
     * @param target          目标对象
     * @param fieldMapDefiner 选择的字段映射器
     * @param reverseFlag     以哪个对象为准，false 以 origin为准，true 以 target为准
     * @return
     */
    Map<String, Object> compare(Object origin, Object target, Class<T> fieldMapDefiner, boolean reverseFlag) throws IllegalAccessException;

    /**
     * 比较两个对象的字段差异
     *
     * @param origin          原对象
     * @param target          目标对象
     * @param fieldMapDefiner 选择的字段映射器对象
     * @param reverseFlag     以哪个对象为准，false 以 origin为准，true 以 target为准
     * @return
     */
    Map<String, Object> compare(Object origin, Object target, T fieldMapDefiner, boolean reverseFlag) throws IllegalAccessException;


    /**
     * 打印比较的数据
     *
     * @param origin          原对象
     * @param target          目标对象
     * @param fieldMapDefiner 选择的字段映射器
     */
    void printData(Object origin, Object target, Class<T> fieldMapDefiner);

    /**
     * 同步数据，从原数据到目标数据 (reverseFlag = false)
     *
     * @param origin          原数据
     * @param target          目标数据
     * @param fieldMapDefiner 属性映射器
     */
    void syncData(Object origin, Object target, Class<T> fieldMapDefiner) throws IllegalAccessException;

    /**
     * 同步数据，默认从原数据到目标数据
     *
     * @param origin          原数据
     * @param target          目标数据
     * @param fieldMapDefiner 属性映射器
     * @param reverseFlag     是否反转
     */
    void syncData(Object origin, Object target, Class<T> fieldMapDefiner, boolean reverseFlag) throws IllegalAccessException;
}
