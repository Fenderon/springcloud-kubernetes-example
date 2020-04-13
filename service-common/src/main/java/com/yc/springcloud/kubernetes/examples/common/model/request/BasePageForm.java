package com.yc.springcloud.kubernetes.examples.common.model.request;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;

/**
 * 分页表单基类
 *
 * @author yangchuan
 * @version 1.0 create at 2020/2/21
 */
@Getter
@Setter
public class BasePageForm {

    /**
     * 每页最大数量
     */
    public static final int MAX_PAGE_SIZE = 1000;

    /**
     * 默认每页大小
     */
    public static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * 默认排序字段：创建时间
     */
    private static final String ORDER_FILED = "createTime";

    @ApiModelProperty(hidden = true)
    private long total = 0L;

    @ApiModelProperty(value = "页码(默认1)，从1开始....")
    private Integer current;

    /**
     * 每页数据大小
     */
    @ApiModelProperty(value = "每页条数(默认10)")
    @Max(value = MAX_PAGE_SIZE, message = "数据条数不能超过一万条")
    private Integer pageSize;

    public BasePageForm() {
        current = 1;
        pageSize = 10;
    }

    public BasePageForm(Integer current, Integer pageSize) {
        this.current = current;
        this.pageSize = pageSize;
    }

    /**
     * 返回是否还有下一页
     *
     * @return 总数 > 页数 * 页面大小
     */
    public boolean hasNextPage() {
        return this.getTotal() > (this.getCurrent() + 1) * this.getPageSize();
    }

    /**
     * 下一页
     *
     * @return 页数
     */
    public Integer nextPage() {
        this.setCurrent(this.getCurrent() + 1);
        return this.getCurrent();
    }

    /**
     * 获取当前页数
     *
     * @return
     */
    public Integer getCurrent() {
        if (this.current == null || this.current < 1) {
            current = 1;
        }
        return this.current;
    }

    /**
     * 设置分页
     *
     * @param current
     */
    public void setCurrent(Integer current) {
        if (current == null || current < 1) {
            //抛出异常，以免造成程序排错困呐
            throw new RuntimeException("page must be != null and >= 1 ");
        }
        this.current = current;
    }

    /**
     * 获取当前页面大小
     *
     * @return
     */
    public Integer getPageSize() {
        if (this.pageSize == null || this.pageSize < 1 || this.pageSize > MAX_PAGE_SIZE) {
            this.pageSize = DEFAULT_PAGE_SIZE;
        }
        return this.pageSize;
    }

    /**
     * 设置页面大小
     *
     * @param pageSize
     */
    public void setPageSize(Integer pageSize) {
        if (pageSize == null || pageSize < 1 || pageSize > MAX_PAGE_SIZE) {
            //抛出异常，以免造成程序排错困呐
            throw new RuntimeException("page size must > 0 and < " + MAX_PAGE_SIZE);
        }
        this.pageSize = pageSize;
    }


    /**
     * 转换为Mybatis-plus的分页
     */
    public IPage queryMybatisPage() {
        return new Page(current, pageSize);
    }
}
