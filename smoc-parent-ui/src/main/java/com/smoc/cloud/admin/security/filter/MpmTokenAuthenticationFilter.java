package com.smoc.cloud.admin.security.filter;

import com.smoc.cloud.admin.oauth2.service.OauthTokenService;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录过滤器 或是拦截器
 * <p>
 * Description:拦截器拦截特定（或是配置）登录的url、用户名、密码等，自己扩展了图形验证码
 * </p>
 */
@Slf4j
public class MpmTokenAuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    @Autowired
    private OauthTokenService oauthTokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        // 强制手动判断是否为post提交
        if (!request.getMethod().equals("POST")) {
            log.error("[系统安全][系统安全]:非法提交登录，登录必须为POST提交");
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        String userName = obtainUsername(request);
        String password = obtainPassword(request);
        //String verifyCode = obtainVerifyCode(request).toLowerCase().trim();
        if (log.isDebugEnabled()) {
            log.debug("用户登录：{},{},{}", userName, password, "");
        }

//        /**
//         * 判定验证码
//         */
//        String vcode = (String) request.getSession().getAttribute("verificationCode");
//        request.getSession().setAttribute("verificationCode", "");//让验证码失效
//        if (StringUtils.isEmpty(vcode) || !vcode.equals(verifyCode)) {
//            throw new AuthenticationServiceException("verifyCodeError");
//        }

        /**
         * 用户登录验证，并返回用户OAuth2AccessToken
         *
         */
        //log.info("登录请求..............");
        ResponseData<OAuth2AccessToken> oAuth2AccessToken = oauthTokenService.getAccessToken(userName, password);
        if (!ResponseCode.SUCCESS.getCode().equals(oAuth2AccessToken.getCode())) {

            log.info("[用户登录][系统安全][{}]:{}", userName, oAuth2AccessToken.getMessage());
            throw new AuthenticationServiceException(oAuth2AccessToken.getMessage());
        }

        request.getSession().setAttribute("token", oAuth2AccessToken.getData().getValue());

        Authentication userAuthentication = null;

        /**
         * 登录成功
         */
        try {
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                    oAuth2AccessToken.getData().getValue(), oAuth2AccessToken.getData().getValue());
            userAuthentication = authenticationManager.authenticate(authRequest);
        } catch (Exception e) {
            log.error("[用户登录][系统安全] [{}]:{}", userName, e.getMessage());
            throw e;
        }

        return userAuthentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);

    }

    /**
     * 登录失败
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {

        super.unsuccessfulAuthentication(request, response, failed);
    }

}
