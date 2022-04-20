package com.smoc.cloud.http.properties;

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
@ConfigurationProperties(prefix = "com.smoc.cloud.http.server.message-resource")
public class ResourceProperties {

    //彩信资源文件根目录
    private String resourceFileRootPath;

    //彩信资源文件大小限制
    private Integer resourceFileSizeLimit;

}
