package com.smoc.cloud.auth.api;


import com.smoc.cloud.auth.data.provider.service.WeixinTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信公众号 token 服务器缓存
 */
@Slf4j
@RestController
@RequestMapping("/weixin")
public class WeixinController {

    @Autowired
    private WeixinTokenService weixinTokenService;

    /**
     * 获取微信access_token
     *
     * @return
     */
    @RequestMapping(value = "/getWeixinAccessToken", method = RequestMethod.GET)
    public String getWeixinAccessToken() {
        return weixinTokenService.getWeixinAccessToken();
    }

    /**
     * 获取微信jsapi_ticket
     *
     * @return
     */
    @RequestMapping(value = "/getWeixinJsapiTicket", method = RequestMethod.GET)
    public String getWeixinJsapiTicket() {
        return weixinTokenService.getWeixinJsapiTicket();
    }

    /**
     * 保存微信access_token
     *
     * @return
     */
    @RequestMapping(value = "/saveWeixinAccessToken/{accessToken}", method = RequestMethod.GET)
    public void saveWeixinAccessToken(@PathVariable String accessToken) {
        weixinTokenService.saveWeixinAccessToken(accessToken);
    }

    /**
     * 保存微信jsapi_ticket
     *
     * @return
     */
    @RequestMapping(value = "/saveWeixinJsapiTicket/{jsapiTicket}", method = RequestMethod.GET)
    public void saveWeixinJsapiTicket(@PathVariable String jsapiTicket) {
        weixinTokenService.saveWeixinJsapiTicket(jsapiTicket);
    }


}
