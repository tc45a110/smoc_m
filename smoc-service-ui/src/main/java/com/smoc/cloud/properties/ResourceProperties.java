package com.smoc.cloud.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 资源配置属性文件
 **/
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "com.manpuman.cloud.smoc.message-resource")
public class ResourceProperties {

    //资源文件访问路径
    private String resourceUrl;

    //彩信资源文件根目录
    private String resourceFileRootPath;

    //彩信资源格式
    private String[] resourceAllowFormat;

    //彩信资源文件大小限制
    private Integer resourceFileSizeLimit;

    //彩信帧默认停留时间（秒）
    private Integer stayTimes;

}
