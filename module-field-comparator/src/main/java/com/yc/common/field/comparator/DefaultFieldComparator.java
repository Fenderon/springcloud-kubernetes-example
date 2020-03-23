package com.yc.common.field.comparator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 默认属性比较器
 *
 * @author yangchuan
 * @version 1.0 create at 2020/3/20
 */
@Slf4j
@Service
public class DefaultFieldComparator<T extends FieldMapDefiner> extends AbstractFieldComparator<T> {

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
    @Override
    protected Map<String, Object> doCompare(Object origin, Object target, Map<Field, Field> fieldMap, boolean reverseFlag) throws IllegalAccessException {

        HashMap<String, Object> res = new HashMap<>();
        Object originV = null;
        Object targetV = null;
        for (Map.Entry<Field, Field> entry : fieldMap.entrySet()) {
            originV = entry.getKey().get(origin);
            targetV = entry.getValue().get(target);
            if (Objects.equals(originV, targetV)) {
                continue;
            }
            if (reverseFlag == false) {
                //以origin为准
                res.put(entry.getValue().getName(),
                        originV);
            } else {
                //以target为准
                res.put(entry.getKey().getName(), targetV);
            }
        }
        return res;
    }
}
