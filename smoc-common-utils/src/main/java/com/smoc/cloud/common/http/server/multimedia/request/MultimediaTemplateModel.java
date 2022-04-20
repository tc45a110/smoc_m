package com.smoc.cloud.common.http.server.multimedia.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 多媒体信息
 */
@Setter
@Getter
public class MultimediaTemplateModel {

    //主题
    @NotNull(message = "主题不能为空！")
    private String subject;

    //多媒体类型  音频、视频、图片、文本
    @NotNull(message = "多媒体类型不能为空！")
    @Pattern(regexp = "^(PIC|VIDEO|TXT|AUDIO){1}", message = "多媒体类型不符合规则！只支持PIC、VIDEO、TXT、AUDIO")
    private String mediaType;

    //文件类型 .png  .mp3
    @NotNull(message = "文件类型不能为空！")
    @Pattern(regexp = "^(jpg|jpeg|gif|png|midi|wav|amr|mp3|aac|3gp|mp4){1}", message = "文件类型不符合规则！只支持jpg,jpeg,gif,png,midi,wav,amr,mp3,aac,3gp,mp4")
    private String fileType;

    //停留时间
    @NotNull(message = "停留时间不能为空！")
    private Integer stayTimes;

    //帧文本
    private String frameTxt;

    //多媒体文件 Base64
    @NotNull(message = "多媒体文件不能为空！")
    private String  mediaFile;

}
