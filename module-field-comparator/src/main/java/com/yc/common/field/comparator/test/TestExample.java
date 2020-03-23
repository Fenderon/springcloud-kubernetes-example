package com.yc.common.field.comparator.test;

import com.yc.common.field.comparator.FieldComparator;
import com.yc.common.field.comparator.FieldMapDefiner;
import com.yc.common.field.comparator.fielddefiner.OriginToTargetFieldDefiner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试类
 *
 * @author yangchuan
 * @version 1.0 create at 2020/3/23
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestExample {

    @Autowired
    private FieldComparator fieldComparator;

    @Test
    public void test() throws IllegalAccessException {

        OriginToTargetFieldDefiner.Origin origin = new OriginToTargetFieldDefiner.Origin();
        origin.setOrigin1("origin1");
        origin.setOrigin2("origin2");

        OriginToTargetFieldDefiner.Target target = new OriginToTargetFieldDefiner.Target();
        target.setTarget1("target1");
        target.setTarget2("target2");

        //比较原对象与目标对象差异，以目标对象为准
        Map compare = fieldComparator.compare(origin, target, OriginToTargetFieldDefiner.class);
        System.out.println("比较原对象与目标对象差异，以目标对象为准");
        System.out.println(compare);
        System.out.println();

        //反转，以原对象为准
        compare = fieldComparator.compare(origin, target, OriginToTargetFieldDefiner.class, true);
        System.out.println("反转，以原对象为准");
        System.out.println(compare);
        System.out.println();

        //自定义对象
        compare = fieldComparator.compare(origin, target, new FieldMapDefiner() {
            @Override
            public Class<?> getOriginClass() {
                return OriginToTargetFieldDefiner.Origin.class;
            }

            @Override
            public Class<?> getTargetClass() {
                return OriginToTargetFieldDefiner.Target.class;
            }

            @Override
            public Map<String, String> columnDef() {
                Map<String, String> map = new HashMap<>();
                map.put("origin1", "target2");
                map.put("origin2", "target1");
                return map;
            }
        }, true);
        System.out.println("自定义对象");
        System.out.println(compare);

    }
}
