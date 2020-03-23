package com.yc.common.field.comparator.fielddefiner;

import com.yc.common.field.comparator.FieldDefiner;
import com.yc.common.field.comparator.FieldMapDefiner;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 原对象
 *
 * @author yangchuan
 * @version 1.0 create at 2020/3/23
 */
@FieldDefiner(type = "OriginToTargetFieldDefiner")
public class OriginToTargetFieldDefiner implements FieldMapDefiner {

    @Override
    public Class<?> getOriginClass() {
        return Origin.class;
    }

    @Override
    public Class<?> getTargetClass() {
        return Target.class;
    }

    @Override
    public Map<String, String> columnDef() {
        Map<String,String> map = new HashMap<>();
        map.put("origin1","target1");
        map.put("origin2","target2");
        return map;
    }

    @Data
    public static class Origin{
        private String origin1;
        private String origin2;
    }

    @Data
    public static class Target{
        private String target1;
        private String target2;
    }
}
