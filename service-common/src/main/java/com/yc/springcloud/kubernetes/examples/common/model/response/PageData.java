package com.yc.springcloud.kubernetes.examples.common.model.response;

import com.yc.springcloud.kubernetes.examples.common.utils.IBeanUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 分页数据对象
 *
 * @author yangchuan
 * @version 1.0 create at 2020/2/21
 */
@Getter
@Setter
public class PageData<T, S> {

    @ApiModelProperty("数据列表")
    private List<T> data;

    @ApiModelProperty("数据总数量")
    private Long total;

    @ApiModelProperty("汇总数据")
    private S summeryData;

    public PageData() {
        this.total = 0L;
    }

    public PageData(List<T> data, Long total, S summeryData) {
        this.data = data;
        this.total = total;
        this.summeryData = summeryData;
    }

    public PageData(List<T> data, Long total) {
        this.data = data;
        this.total = total;
        this.summeryData = null;
    }

    /**
     * 转换结果
     *
     * @param data  数据
     * @param total 总数
     * @param dest  目标类
     * @return 分页对象
     */
    public static <T, S> PageData convert(List data, Long total, Class<T> dest) {
        return new PageData<T, S>(
                (List<T>) data.stream().map(e -> {
                    return IBeanUtils.copyWithEmpty(e, dest);
                }).collect(Collectors.toList()),
                total, null);
    }

    /**
     * 获取分页数据
     *
     * @return
     */
    public List<T> getData() {
        if (data == null) {
            data = Collections.emptyList();
        }
        return data;
    }
}
