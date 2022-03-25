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

    //资源文件根目录
    private String resourceFileRootPath;

    //资源格式
    private String[] resourceAllowFormat;

    //资源文件大小限制
    private Integer resourceFileSizeLimit;

    //帧默认停留时间（秒）
    private Integer stayTimes;

}
