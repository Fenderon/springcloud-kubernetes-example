package com.yc.springcloud.kubernetes.examples.common.model.response;

import com.yc.springcloud.kubernetes.examples.common.code.ErrorCodes;
import com.yc.springcloud.kubernetes.examples.common.exception.SystemApiException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Result Response 对象
 *
 * @author yangchuan
 * @version 1.0 create at 2020/2/21
 */
@ApiModel("结果返回对象")
@ToString
@Getter
@Setter
public class ResultResp<T> extends BaseResult implements Serializable {

    private static final long serialVersionUID = 5590514460991209531L;

    @ApiModelProperty("结果数据")
    private T data;

    public ResultResp(T data) {
        this.data = data;
    }

    /**
     * private构造器
     *
     * @param data 数据
     * @param code 编码
     * @param desc 描述
     */
    private ResultResp(T data, String code, String desc) {
        this.data = data;
        this.setCode(code);
        this.setMsg(desc);
    }

    /**
     * 返回一个成功的结果对象
     *
     * @param data 数据
     * @return 结果对象
     */
    public static <T> ResultResp success(T data) {
        return new ResultResp<T>(data, ErrorCodes.OK.getCode(), ErrorCodes.OK.getDescription());
    }

    /**
     * 返回一个空数据的结果对象
     *
     * @return
     */
    public static <T> ResultResp success() {
        return new ResultResp<T>(null, ErrorCodes.OK.getCode(),
                ErrorCodes.OK.getDescription());
    }

    /**
     * 返回一个有数据 和 信息的结果对象
     *
     * @param data    数据
     * @param message 额外信息
     * @return
     */
    public static <T> ResultResp success(T data, String message) {
        ResultResp res = new ResultResp(data);
        return new ResultResp<T>(data, ErrorCodes.OK.getCode(), message);
    }

    public static <T> ResultResp error(ErrorCodes codes) {
        return error(null, codes);
    }

    public static <T> ResultResp error(T data, ErrorCodes codes) {
        return new ResultResp<T>(data, codes.getCode(), codes.getDescription());
    }

    /**
     * 检查是否请求成功
     *
     * @return 如果请求成功，返回data，否则抛出异常
     */
    public T checkThrow() {
        if (this.checkSuccess()) {
            return data;
        }
        throw new SystemApiException(this);
    }

    /**
     * 检查是否请求成功
     *
     * @return 如果请求成功，返回data，否则返回null
     */
    public T TryCheckThrow() {
        if (this.checkSuccess()) {
            return data;
        }
        return null;
    }

}
