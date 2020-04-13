package com.yc.springcloud.kubernetes.examples.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统通用错误码定义
 *
 * @author yangchuan
 * @version 1.0 create at 2020/2/21
 */
@AllArgsConstructor
@Getter
public enum ErrorCodes {
    OK("200", "success"),

    SYSTEM_BEAN_COPY_ERROR("10000", "BEAN COPY ERROR");

    /**
     * 错误码
     */
    private String code;

    /**
     * 描述
     */
    private String description;
}
