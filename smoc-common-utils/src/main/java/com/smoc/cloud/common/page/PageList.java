package com.smoc.cloud.common.page;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装分页对象
 * 2019/5/6 11:45
 **/
public class PageList<T> {

    //分页参数
    private PageParams<T>  pageParams = new PageParams();

    //页面数据
    private List<T> list = new ArrayList();

    public List<T> getList() {
        if (list == null) {
            list = new ArrayList();
        }
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public void setPageParams(PageParams<T> pageParams) {
        this.pageParams = pageParams;
    }

    public PageParams<T> getPageParams() {
        return pageParams;
    }
}
