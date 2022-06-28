package com.smoc.cloud.tablestore.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 配置属性文件
 **/
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "com.manpuman.cloud.smoc.tablestore")
public class TableStoreProperties {

    private String endPoint;

    private String accessKeyId;

    private String accessKeySecret;

    private String instanceName;

}
