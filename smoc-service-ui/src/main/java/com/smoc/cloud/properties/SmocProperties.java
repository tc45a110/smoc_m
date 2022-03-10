package com.smoc.cloud.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 配置属性文件
 * 2020/5/16 9:23
 **/
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "com.manpuman.cloud.smoc")
public class SmocProperties {

    //合同、资质文件路径
    private String enterpriseFilePath;

    //手机号文件根目录
    private String mobileFileRootPath;
}
