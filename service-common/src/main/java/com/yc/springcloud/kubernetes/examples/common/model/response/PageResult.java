package com.yc.springcloud.kubernetes.examples.common.model.response;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yc.springcloud.kubernetes.examples.common.code.ErrorCodes;
import com.yc.springcloud.kubernetes.examples.common.utils.IBeanUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 分页返回结果对象
 *
 * @author yangchuan
 * @version 1.0 create at 2020/2/21
 */
@Data
public class PageResult<T, S> extends BaseResult {

    @ApiModelProperty("分页数据对象")
    private PageData<T, S> pageData;

    public PageResult(String code, String msg, PageData<T, S> pageData) {
        super(code, msg);
        setPageData(pageData);
    }

    public PageResult(ErrorCodes codes, PageData<T, S> pageData) {
        super(codes);
        setPageData(pageData);
    }

    public PageResult(BaseResult res, PageData<T, S> pageData) {
        super(res.getCode(), res.getMsg());
        setPageData(pageData);
    }

    /**
     * 返回一个成功的分页结果对象
     *
     * @param data        分页数据
     * @param summeryData 汇总数据
     * @return 分页结果对象
     */
    public static <T, S> PageResult success(Page<T> data, S summeryData) {

        PageData<T, S> pageData = new PageData<>(data.getContent(),
                data.getTotalElements(), summeryData);

        return new PageResult<T, S>(ErrorCodes.OK, pageData);
    }


    /**
     * 返回一个成功的分页结果对象
     *
     * @param data 分页数据
     * @return 分页结果对象
     */
    public static <T, S> PageResult success(Page<T> data) {

        PageData pageData = new PageData<>(data.getContent(),
                data.getTotalElements(), null);

        return new PageResult<T, S>(ErrorCodes.OK, pageData);
    }

    /**
     * 返回一个成功的分页结果对象
     *
     * @param pageData 分页数据
     * @return 分页结果对象
     */
    public static <T, S> PageResult success(PageData<T, S> pageData) {
        return new PageResult<T, S>(ErrorCodes.OK, pageData);
    }

    /**
     * 返回一个成功的分页结果对象
     *
     * @param page     分页数据
     * @param resClass 目标对象类
     * @return 分页结果对象
     */
    public static <T, S> PageResult success(IPage page, Class<T> resClass) {

        List<T> collect = (List<T>) page.getRecords().stream().map(e -> {
            return IBeanUtils.copyWithEmpty(e, resClass);
        }).collect(Collectors.toList());

        PageData pageData = new PageData<>(collect,
                page.getTotal(), null);

        return new PageResult<T, S>(ErrorCodes.OK, pageData);
    }

    /**
     * 返回一个错误的分页结果对象
     *
     * @return 分页结果对象
     */
    public static <T, S> PageResult error(ErrorCodes codes) {
        return new PageResult<>(codes, null);
    }

    /**
     * 返回一个错误的分页结果对象
     *
     * @return 分页结果对象
     */
    public static <T, S> PageResult error(String code, String message) {
        return new PageResult<T, S>(new BaseResult(code, message), null);
    }

}
