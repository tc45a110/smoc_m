package com.smoc.cloud.intellect.remote.request;

import lombok.Getter;
import lombok.Setter;

/**
 * 模板资源上传请求参数
 */
@Setter
@Getter
public class RequestTemplateResource {

    //资源类型 image：图片（图片不能超过 2M） video：视频（视频不能超过 10M）图片小图:限制 100K 以内 图片大图:限制 2M 图片格式支持:JPG、JPEG、PNG 视频格式支持:MP4）
    private String resourceType;

    //文件类型
    private String fileType;

    //文件 URL
    private String fileUrl;

    //多媒体资源文件流的 BASE64 编码字符 串。
    private String fileStream;

    //图片比例
    private String imageRate;

    //描述信息
    private String description;

    //业务 Id
    private String bizId;
}
