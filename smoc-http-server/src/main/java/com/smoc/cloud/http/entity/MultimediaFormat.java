package com.smoc.cloud.http.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MultimediaFormat {

    //播放顺序
    private Integer index;

    //资源id
    private String resId;

    //资源大小 K
    private Integer resSize;

    //资源相对路径
    private String resUrl;

    //资源类型
    private String resType;

    //资源文件后嘴
    private String resPostfix;

    //停留时间
    private String stayTimes;

    //资源文本
    private String frameTxt;

}
