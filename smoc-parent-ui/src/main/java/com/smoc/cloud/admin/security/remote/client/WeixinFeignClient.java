package com.smoc.cloud.admin.security.remote.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 微信公众号 token 服务器缓存
 */
@FeignClient(name = "smoc-auth", path = "/auth")
public interface WeixinFeignClient {

    /**
     * 获取微信access_token
     *
     * @return
     */
    @RequestMapping(value = "/weixin/getWeixinAccessToken", method = RequestMethod.GET)
    String getWeixinAccessToken() throws Exception;

    /**
     * 获取微信jsapi_ticket
     *
     * @return
     */
    @RequestMapping(value = "/weixin/getWeixinJsapiTicket", method = RequestMethod.GET)
    String getWeixinJsapiTicket() throws Exception;


    /**
     * 保存微信access_token
     *
     * @return
     */
    @RequestMapping(value = "/weixin/saveWeixinAccessToken/{accessToken}", method = RequestMethod.GET)
    void saveWeixinAccessToken(@PathVariable(value = "accessToken") String accessToken) throws Exception;

    /**
     * 保存微信jsapi_ticket
     *
     * @return
     */
    @RequestMapping(value = "/weixin/saveWeixinJsapiTicket/{jsapiTicket}", method = RequestMethod.GET)
    void saveWeixinJsapiTicket(@PathVariable(value = "jsapiTicket") String jsapiTicket) throws Exception;
}
