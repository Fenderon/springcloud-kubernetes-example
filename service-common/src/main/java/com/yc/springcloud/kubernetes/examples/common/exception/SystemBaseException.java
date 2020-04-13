package com.yc.springcloud.kubernetes.examples.common.exception;

import com.yc.springcloud.kubernetes.examples.common.code.ErrorCodes;
import com.yc.springcloud.kubernetes.examples.common.model.response.BaseResult;
import lombok.Getter;

/**
 * 系统常见异常
 *
 * @author yangchuan
 * @version 1.0 create at 2020/2/21
 */
public class SystemBaseException extends RuntimeException {

    private static final long serialVersionUID = 3382083383947095812L;

    @Getter
    private String code;

    public SystemBaseException() {

    }

    public SystemBaseException(String message) {
        super(message);
    }

    public SystemBaseException(ErrorCodes res) {
        super(res.getDescription());
        this.code = res.getCode();
    }

    public SystemBaseException(BaseResult res) {
        super(res.getMsg());
        this.code = res.getCode();
    }

    public SystemBaseException(String code, String message) {
        super(message);
        this.code = code;
    }
}
