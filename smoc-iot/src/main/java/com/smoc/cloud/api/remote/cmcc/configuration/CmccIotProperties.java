package com.smoc.cloud.api.remote.cmcc.configuration;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 移动物联网卡接口对接属性文件
 */
@Slf4j
@Setter
@Getter
@Service
public class CmccIotProperties {

    private String url = "https://api.iot.10086.cn";

    private String appId = "C5010551AIOT2022081911060939437";

    private String password = "Zr0*XCTFoDoq";


}
