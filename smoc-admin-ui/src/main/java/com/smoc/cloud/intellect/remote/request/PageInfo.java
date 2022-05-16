package com.smoc.cloud.intellect.remote.request;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PageInfo {

    //查询页数
    private Integer page;

    //页面条数
    private Integer size;

    //总条数
    private Integer total;
}
