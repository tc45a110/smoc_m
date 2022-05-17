package com.somc.cloud.gateway.configuration;

import com.smoc.cloud.common.gateway.utils.AESConstUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 配置文件
 */
@Slf4j
@Component
@ConfigurationProperties(prefix = "com.smoc.cloud.gateway.configuration.signature")
public class GatewayConfigurationProperties {

    // 签名、验签算法，支持md5_HMAC、sha256_HMAC、sha384_HMAC、sha512_HMAC
    private String signStyle;

    private String aesKey;

    private String aesIv;

    private String md5HmacKey;

    private String shaHmacKey;

    public String getSignStyle() {
        return signStyle;
    }

    public void setSignStyle(String signStyle) {

        this.signStyle = signStyle;
//        log.info("签名方式:{}",this.signStyle);
    }

    public String getAesKey() {
        return aesKey;
    }

    public void setAesKey(String aesKey) {

        this.aesKey = AESConstUtil.decrypt(aesKey);
//        log.info("aesKey:{}",this.aesKey);
    }

    public String getAesIv() {
        return aesIv;
    }

    public void setAesIv(String aesIv) {
        this.aesIv = AESConstUtil.decrypt(aesIv);
//        log.info("aesIv:{}",this.aesIv);
    }

    public String getMd5HmacKey() {
        return md5HmacKey;
    }

    public void setMd5HmacKey(String md5HmacKey) {
        this.md5HmacKey = AESConstUtil.decrypt(md5HmacKey);
//        log.info("md5HmacKey:{}",this.md5HmacKey);
    }

    public String getShaHmacKey() {
        return shaHmacKey;
    }

    public void setShaHmacKey(String shaHmacKey) {
        this.shaHmacKey = AESConstUtil.decrypt(shaHmacKey);
//        log.info("shaHmacKey:{}",this.shaHmacKey);
    }
}
