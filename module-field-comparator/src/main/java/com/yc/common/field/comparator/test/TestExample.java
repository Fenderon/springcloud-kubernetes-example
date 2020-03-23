package com.yc.common.field.comparator.test;

import com.yc.common.field.comparator.FieldComparator;
import com.yc.common.field.comparator.FieldMapDefiner;
import com.yc.common.field.comparator.fielddefiner.OriginToTargetFieldDefiner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
    public void testBeanCopy() throws IllegalAccessException {
        OriginToTargetFieldDefiner.Origin origin = new OriginToTargetFieldDefiner.Origin();
        origin.setOrigin1("origin1");
        origin.setOrigin2("origin2");
        origin.setCommon("origin_common");

        OriginToTargetFieldDefiner.Target target = new OriginToTargetFieldDefiner.Target();
        target.setTarget1("target1");
        target.setTarget2("target2");
        target.setCommon("target_common");

        Long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            BeanUtils.copyProperties(origin,target);
        }
        System.out.println(System.currentTimeMillis() - startTime);

        startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
//            fieldComparator.syncData(origin,target);
        }
        System.out.println(System.currentTimeMillis() - startTime);
    }
    @Test
    public void test() throws IllegalAccessException {

        OriginToTargetFieldDefiner.Origin origin = new OriginToTargetFieldDefiner.Origin();
        origin.setOrigin1("origin1");
        origin.setOrigin2("origin2");
        origin.setCommon("origin_common");

        OriginToTargetFieldDefiner.Target target = new OriginToTargetFieldDefiner.Target();
        target.setTarget1("target1");
        target.setTarget2("target2");
        target.setCommon("target_common");

        Map compare = null;

        //默认比较相同的字段
        System.out.println("默认比较相同的字段 -- 1");
        compare = fieldComparator.compare(origin, target);
        System.out.println(compare);

        //比较原对象与目标对象差异，以目标对象为准
        compare = fieldComparator.compare(origin, target, OriginToTargetFieldDefiner.class);
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
