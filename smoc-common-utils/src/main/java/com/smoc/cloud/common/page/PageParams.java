package com.smoc.cloud.common.page;

import lombok.Getter;
import lombok.Setter;

/**
 * 分页参数传递
 * 2019/5/6 14:24
 **/
@Setter
@Getter
public class PageParams<T> {

    //当前页
    private Integer currentPage;

    //每页行数
    private Integer pageSize;

    //开始行
    private Integer startRow;

    //结束行
    private Integer endRow;

    //总行数
    private int totalRows;

    //总页数
    private int pages;

    //查询参数对象
    private T params;
}
