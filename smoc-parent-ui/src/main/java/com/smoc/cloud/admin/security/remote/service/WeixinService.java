package com.smoc.cloud.admin.security.remote.service;


import com.smoc.cloud.admin.security.remote.client.WeixinFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

/**
 * 微信公众号 token 服务器缓存
 */
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class WeixinService {

    @Autowired
    private WeixinFeignClient weixinFeignClient;


    /**
     * 获取微信access_token
     *
     * @return
     */
    public String getWeixinAccessToken() {
        try {
            String data = this.weixinFeignClient.getWeixinAccessToken();
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return "";
        }
    }

    /**
     * 获取微信jsapi_ticket
     *
     * @return
     */
    public String getWeixinJsapiTicket() {
        try {
            String data = this.weixinFeignClient.getWeixinJsapiTicket();
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return "";
        }
    }

    /**
     * 保存微信access_token
     *
     * @return
     */
    public void saveWeixinAccessToken(String accessToken) {
        try {
            this.weixinFeignClient.saveWeixinAccessToken(accessToken);
        } catch (Exception e) {
            log.error(e.getMessage());

        }
    }

    /**
     * 保存微信jsapi_ticket
     *
     * @return
     */
    public void saveWeixinJsapiTicket(String jsapiTicket) {
        try {
            this.weixinFeignClient.saveWeixinJsapiTicket(jsapiTicket);
        } catch (Exception e) {
            log.error(e.getMessage());

        }
    }
}
