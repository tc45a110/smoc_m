package com.smoc.cloud.admin.configuration;


import org.springframework.cloud.netflix.ribbon.RibbonClient;

/**
 *  ribbon配置
 * 2019/5/11 00:03
 */
@RibbonClient(value="smoc-auth")
public class MpmAuthRibbonConfiguration {
}
