package com.smoc.cloud.auth.api;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.auth.security.service.MpmTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

/**
 * 用户登录
 */
@Slf4j
@RestController
@RequestMapping("/oauth")
public class LoginByUserNameController {

    @Autowired
    private TokenEndpoint tokenEndpoint;

    @Autowired
    private MpmTokenService mpmTokenService;

    @GetMapping("/token")
    public Object getAccessToken(Principal principal, @RequestParam Map<String, String> parameters, @RequestHeader Map<String, String> headers) throws HttpRequestMethodNotSupportedException {

        return tokenEndpoint.getAccessToken(principal, parameters).getBody();
    }

    @PostMapping("/token")
    public Object postAccessToken(Principal principal, @RequestParam Map<String, String> parameters, @RequestHeader Map<String, String> headers) throws HttpRequestMethodNotSupportedException {

        //log.info("[自定义登录][请求参数]数据:{}", JSON.toJSONString(parameters));
        Object object = tokenEndpoint.postAccessToken(principal, parameters).getBody();
        //log.info("[自定义登录][验证后参数]数据:{}", JSON.toJSONString(object));
        return object;
    }



}
