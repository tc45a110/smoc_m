package com.smoc.cloud.admin.security.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * spring security 属性配置
 * 2019/4/16 11:58
 **/
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "com.manpuman.cloud.security")
public class SystemProperties {


    //公司名称
    public String companyName;

    //系统名称
    public  String systemName;

    //本系统标示
    private String systemMarking;

    //spring security 登录Post path
    private String loginPostPath;

    //系统备用链接
    private String authUri;

    private Boolean ssl;

    //是否启用验证码
    private Boolean enableVerifyCode;

    //帮助电话
    private String helpMobile;

    private String mainUrl;



}
