package com.smoc.cloud.admin.sso.filter;

import com.smoc.cloud.admin.sso.token.PhoneAuthenticationToken;
import com.smoc.cloud.admin.sso.token.TokenAuthenticationToken;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by fp295 on 2018/6/16.
 * 自定义登陆filter，新增登陆方式：验证码、二维码扫码、账号密码；
 * 验证码登陆：
 * post: /login?type=phone&phone=13000000000&verifyCode=1000
 * 二维码登陆：
 * post: /login?type=qr&qrCode=token
 * 账号密码登陆：
 * post: /login?username=username&password=password
 * 此filter 为生成自定义的 MyAuthenticationToken
 */
public class MpmLoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public static final String SPRING_SECURITY_RESTFUL_TYPE_PHONE = "phone";
    public static final String SPRING_SECURITY_RESTFUL_TYPE_TOKEN = "token";
    public static final String SPRING_SECURITY_RESTFUL_TYPE_DEFAULT = "user";

    // 登陆类型：user:用户密码登陆；phone:手机验证码登陆；token:二维码扫码登陆
    private static final String SPRING_SECURITY_RESTFUL_TYPE_KEY = "type";
    // 登陆终端：1：移动端登陆，包括微信公众号、小程序等；0：PC后台登陆
    private static final String SPRING_SECURITY_RESTFUL_MOBILE_KEY = "mobile";

    private static final String SPRING_SECURITY_RESTFUL_USERNAME_KEY = "username";
    private static final String SPRING_SECURITY_RESTFUL_PASSWORD_KEY = "password";
    private static final String SPRING_SECURITY_RESTFUL_PHONE_KEY = "phone";
    private static final String SPRING_SECURITY_RESTFUL_VERIFY_CODE_KEY = "verifyCode";
    private static final String SPRING_SECURITY_RESTFUL_TOKEN_CODE_KEY = "token";

    private static final String SPRING_SECURITY_RESTFUL_LOGIN_URL = "/login";
    private boolean postOnly = true;

    /*public MyLoginAuthenticationFilter() {
        super(new AntPathRequestMatcher(SPRING_SECURITY_RESTFUL_LOGIN_URL, "POST"));
    }*/


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }

        String type = obtainParameter(request, SPRING_SECURITY_RESTFUL_TYPE_KEY);
        String mobile = obtainParameter(request, SPRING_SECURITY_RESTFUL_MOBILE_KEY);
        String token = obtainParameter(request, SPRING_SECURITY_RESTFUL_TOKEN_CODE_KEY);
        AbstractAuthenticationToken authRequest;
        String principal;
        //密码
        String credentials;

        // 手机验证码登陆
        if (SPRING_SECURITY_RESTFUL_TYPE_PHONE.equals(type)) {
            principal = obtainParameter(request, SPRING_SECURITY_RESTFUL_PHONE_KEY);
            credentials = obtainParameter(request, SPRING_SECURITY_RESTFUL_VERIFY_CODE_KEY);

            principal = principal.trim();
            authRequest = new PhoneAuthenticationToken(principal, credentials);
        }
        // token登陆
        else if (SPRING_SECURITY_RESTFUL_TYPE_TOKEN.equals(type)) {
            principal = obtainParameter(request, SPRING_SECURITY_RESTFUL_TOKEN_CODE_KEY);
            credentials = null;
            authRequest = new TokenAuthenticationToken(principal, credentials);
        }
        // 账号密码登陆
        else {
            principal = obtainParameter(request, SPRING_SECURITY_RESTFUL_USERNAME_KEY);
            credentials = obtainParameter(request, SPRING_SECURITY_RESTFUL_PASSWORD_KEY);

            principal = principal.trim();
            authRequest = new UsernamePasswordAuthenticationToken(principal, credentials);

        }


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
