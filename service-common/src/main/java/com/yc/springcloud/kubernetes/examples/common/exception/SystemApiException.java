package com.yc.springcloud.kubernetes.examples.common.exception;

import com.yc.springcloud.kubernetes.examples.common.code.ErrorCodes;
import com.yc.springcloud.kubernetes.examples.common.model.response.BaseResult;

/**
 * 系统API异常
 *
 * @author yangchuan
 * @version 1.0 create at 2020/2/21
 */
public class SystemApiException extends SystemBaseException {

    private static final long serialVersionUID = 3382083383947095812L;

    public SystemApiException() {
    }

    public SystemApiException(String message) {
        super(message);
    }

    public SystemApiException(ErrorCodes res) {
        super(res);
    }

    public SystemApiException(BaseResult res) {
        super(res);
    }

    public SystemApiException(String code, String message) {
        super(code, message);
    }
}
