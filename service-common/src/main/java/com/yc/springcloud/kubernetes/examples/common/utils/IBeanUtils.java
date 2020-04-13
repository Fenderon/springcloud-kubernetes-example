package com.yc.springcloud.kubernetes.examples.common.utils;

import com.yc.springcloud.kubernetes.examples.common.code.ErrorCodes;
import com.yc.springcloud.kubernetes.examples.common.exception.SystemBaseException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;

/**
 * Bean 复制工具
 * .....
 * <p>
 * 整个项目都用此工具复制对象
 * </p>
 *
 * @author yangchuan
 * @version 1.0 create at 2020/2/21
 */
public class IBeanUtils {


    /**
     * 构建Target对象
     */
    private static <T> T buildTarget(Class<T> dest) throws IllegalAccessException, InstantiationException {
        return dest.newInstance();
    }

    /**
     * 复制对象 - 浅拷贝
     *
     * @param source 原对象
     * @param target 目标对象的Class类对象
     * @param <T>    目标对象的Class类对象
     * @return 新建的目标对象，如果source为空，则返回NULL
     */
    public static <T> T copyWithEmpty(Object source, Class<T> target) {
        return copyWithEmpty(source, target, "");
    }

    /**
     * 复制对象 - 浅拷贝
     *
     * @param source           原对象
     * @param target           目标对象的Class类对象
     * @param ignoreProperties 忽略字段
     * @param <T>              目标对象的Class类对象
     * @return 新建的目标对象
     */
    public static <T> T copyWithEmpty(Object source, Class<T> target, String... ignoreProperties) {
        try {
            T dest = buildTarget(target);
            copyWithNotEmpty(source, dest, ignoreProperties);
            return dest;
        } catch (IllegalAccessException | InstantiationException e) {
            throw new SystemBaseException(ErrorCodes.SYSTEM_BEAN_COPY_ERROR);
        }
    }

    /**
     * 复制Bean - 浅拷贝
     *
     * @param source 原对象
     * @param target 目标对象，不能为空
     * @return 返回目标对象
     * @throws BeansException 复制失败
     */
    public static Object copyWithNotEmpty(Object source, Object target) throws BeansException {
        BeanUtils.copyProperties(source, target);
        return target;
    }

    /**
     * 复制Bean - 浅拷贝
     *
     * @param source           原对象
     * @param target           目标对象，不能为空
     * @param ignoreProperties 忽略字段
     * @return 返回目标对象
     * @throws BeansException 复制失败
     */
    public static Object copyWithNotEmpty(Object source, Object target, String... ignoreProperties) throws BeansException {
        BeanUtils.copyProperties(source, target, ignoreProperties);
        return target;
    }
}
