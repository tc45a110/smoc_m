package com.smoc.cloud.admin.sso.filter;

import com.smoc.cloud.admin.oauth2.service.OauthTokenService;
import com.smoc.cloud.admin.sso.token.TokenAuthenticationToken;
import com.smoc.cloud.common.auth.entity.Token;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by fp295 on 2018/6/16.
 * 二维码登陆：
 * post: /qrLogin?qrCode=token
 * 生成二维码，建立 websocket - 后台
 * 用户在移动端扫码，回调事件到后台
 * 后台通过websocket通知前台登陆
 * 前台post:/qrLogin, qrCode为生成二维码时后台生成的key
 */
@Slf4j
public class TokenLoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired
    private OauthTokenService oauthTokenService;

    private static final String SPRING_SECURITY_RESTFUL_TOKEN_CODE_KEY = "token";

    private static final String SPRING_SECURITY_RESTFUL_LOGIN_URL = "/tokenLogin";

    public TokenLoginAuthenticationFilter() {
        super(new AntPathRequestMatcher(SPRING_SECURITY_RESTFUL_LOGIN_URL, "GET"));
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        if (postOnly && !request.getMethod().equals("POST")) {
//            throw new AuthenticationServiceException(
//                    "Authentication method not supported: " + request.getMethod());
//        }

        AbstractAuthenticationToken authRequest;
        String principal;
        String credentials;

        // token
        principal = obtainParameter(request, SPRING_SECURITY_RESTFUL_TOKEN_CODE_KEY);
        credentials = null;
        principal = principal.trim();

        //获取缓存的token
        ResponseData<Token> tokenData = oauthTokenService.getSSOToken(principal);
        if (!ResponseCode.SUCCESS.getCode().equals(tokenData.getCode()) || null == tokenData.getData().getToken()) {
            throw new AuthenticationServiceException(
                    "Authentication Parameter invalid");
        }

        request.getSession().setAttribute("token", tokenData.getData().getToken());


        authRequest = new TokenAuthenticationToken(tokenData.getData().getToken(), credentials);

        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    private void setDetails(HttpServletRequest request,
                            AbstractAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    private String obtainParameter(HttpServletRequest request, String parameter) {
        String result = request.getParameter(parameter);
        return result == null ? "" : result;
    }
}
