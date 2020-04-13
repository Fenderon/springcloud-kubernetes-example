package com.yc.springcloud.kubernetes.examples.common.model.response;

import com.yc.springcloud.kubernetes.examples.common.code.ErrorCodes;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 返回结果对象基类
 *
 * @author yangchuan
 * @version 1.0 create at 2020/2/21
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BaseResult {

    @ApiModelProperty("状态码")
    private String code;

    @ApiModelProperty("信息")
    private String msg = "";

    public BaseResult(ErrorCodes codes) {
        this.code = codes.getCode();
        this.msg = codes.getDescription();
    }

    public boolean checkSuccess() {
        return code.equals(ErrorCodes.OK.getCode());
    }
}
