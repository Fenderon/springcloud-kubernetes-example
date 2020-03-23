package com.yc.common.field.comparator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 抽象层，封装抽象逻辑
 *
 * @author yangchuan
 * @version 1.0 create at 2020/3/21
 */
@Slf4j
public abstract class AbstractFieldComparator<T extends FieldMapDefiner> implements FieldComparator<T>, InitializingBean {

    @Autowired
    private List<FieldMapDefiner> fieldMapDefiners;

    /**
     * 具体比较方法
     *
     * @param origin      原对象
     * @param target      目标对象
     * @param fieldMap    属性映射
     * @param reverseFlag 是否反转
     * @return 返回以目标对象属性名称作为key，原对象属性值作为value的map，，反转为true，反之
     * @throws IllegalAccessException
     */
    protected abstract Map<String, Object> doCompare(Object origin, Object target, Map<Field, Field> fieldMap, boolean reverseFlag) throws IllegalAccessException;

    /**
     * 缓存的字段定义map
     */
    private Map<Class<T>, Map<Field, Field>> columnMap = new ConcurrentHashMap<>();

    /**
     * 反射获取Field属性对象
     *
     * @param definer
     * @return
     */
    private Map<Field, Field> reflectToGetField(FieldMapDefiner definer) {
        HashMap<Field, Field> res = new HashMap<>(definer.columnDef().size());
        try {
            //获取Field对象
            for (Map.Entry<String, String> entry : definer.columnDef().entrySet()) {
                res.put(definer.getOriginClass().getDeclaredField(entry.getKey()),
                        definer.getTargetClass().getDeclaredField(entry.getValue()));
            }
        } catch (NoSuchFieldException e) {
            log.error("字段比较器初始化字段自定失败...{}", definer, e);
            throw new FieldComparatorException("服务器异常，属性比较器器初始化字段自定失败");
        }


        for (Map.Entry<Field, Field> entry : res.entrySet()) {
            entry.getKey().setAccessible(true);
            entry.getValue().setAccessible(true);
        }
        return res;
    }

    /**
     * 初始属性定义器
     */
    private void loadFieldDefiner() {
        try {
            for (FieldMapDefiner f : fieldMapDefiners) {
                cacheFieldDefiner(f);
            }
        } catch (Exception e) {
            log.error("加载属性映射器失败...", e);
            System.exit(0);
        }
        log.info("加载完成属性映射器...");
    }

    /**
     * 缓存属性定义器
     *
     * @param fieldMapDefiner 属性定义器
     */
    private Map<Field, Field> cacheFieldDefiner(FieldMapDefiner fieldMapDefiner) {

        if (StringUtils.isEmpty(fieldMapDefiner.getClass().getSimpleName())) {
            //匿名类，不缓存
            return reflectToGetField(fieldMapDefiner);
        }

        if (columnMap.containsKey(fieldMapDefiner.getClass())) {
            return columnMap.get(fieldMapDefiner.getClass());
        }

        //线程安全
        return columnMap.putIfAbsent((Class<T>) fieldMapDefiner.getClass(), reflectToGetField(fieldMapDefiner));

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        loadFieldDefiner();
    }


    /**
     * 获取字段定义
     *
     * @param fieldMapDefiner
     * @return
     */
    private Map<Field, Field> getColunmDef(Class<T> fieldMapDefiner) {

        if (!columnMap.containsKey(fieldMapDefiner)) {
            log.error("字段映射器不存在：【{}】", fieldMapDefiner);
            throw new FieldComparatorException("服务器异常，字段映射器不存在，请联系管理员");
        }

        return columnMap.get(fieldMapDefiner);
    }

    @Override
    public Map<String, Object> compare(Object origin, Object target, Class<T> fieldMapDefiner, boolean reverseFlag) throws IllegalAccessException {
        return doCompare(origin, target, getColunmDef(fieldMapDefiner), reverseFlag);
    }

    @Override
    public Map<String, Object> compare(Object origin, Object target, T fieldMapDefiner, boolean reverseFlag) throws IllegalAccessException {
        return doCompare(origin, target, cacheFieldDefiner(fieldMapDefiner), reverseFlag);
    }


    /**
     * 打印数据
     *
     * @param origin
     * @param target
     */
    @Override
    public void printData(Object origin, Object target, Class<T> fieldMapDefiner) {
        try {
            for (Map.Entry<Field, Field> entry : getColunmDef(fieldMapDefiner).entrySet()) {
                log.info(
                        entry.getKey().getName() + " -- [origin]" + entry.getKey().get(origin) + " : [target]" +
                                entry.getValue().get(target)
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, Object> compare(Object origin, Object target, Class<T> fieldMapDefiner) throws IllegalAccessException {
        return compare(origin, target, fieldMapDefiner, false);
    }

    @Override
    public void syncData(Object origin, Object target, Class<T> fieldMapDefiner) throws IllegalAccessException {
        syncData(origin, target, fieldMapDefiner, false);
    }

    @Override
    public void syncData(Object origin, Object target, Class<T> fieldMapDefiner, boolean reverseFlag) throws IllegalAccessException {

        for (Map.Entry<Field, Field> entry : getColunmDef(fieldMapDefiner).entrySet()) {
            if (reverseFlag) {
                //反转，同步到origin
                entry.getKey().set(origin, entry.getValue().get(target));
            } else {
                //同步到target
                entry.getValue().set(target, entry.getKey().get(origin));
            }
        }
    }
}
