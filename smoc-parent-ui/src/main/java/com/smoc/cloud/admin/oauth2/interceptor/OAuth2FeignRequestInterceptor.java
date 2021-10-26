package com.smoc.cloud.admin.oauth2.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * FeignClient拦截器  为FeignClient请求注入  Authorization Bearer token
 * 2019/4/13 11:43
 **/
@Slf4j
@Component
public class OAuth2FeignRequestInterceptor implements RequestInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private static final String BEARER_TOKEN_TYPE = "Bearer";

    /**
     * 为FeignClient调用时添加Authorization token
     */
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null != attributes) {
            RequestContextHolder.setRequestAttributes(attributes, true);
            HttpServletRequest request = attributes.getRequest();
            String token = (String) request.getSession().getAttribute("token");
            //log.info("请求注入token:" + token);
            if (!StringUtils.isEmpty(token)) {
                requestTemplate.header(AUTHORIZATION_HEADER, BEARER_TOKEN_TYPE + " " + token);
            }else{
                log.info("[系统安全][token拦截]数据:token为空");
            }
        } else {
            log.info("[系统安全][token拦截]数据:attributes为空");
        }

    }
}
