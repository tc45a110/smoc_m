package com.smoc.cloud.admin.security.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 授权失败处理
 * <p>
 * Description:MpmAccessDecisionManager 抛出拒绝异常后 首先执行 LoginUrlAuthenticationEntry
 * 进行改造 当用户访问不需要登录的非法页面、或其他业务逻辑页面时 直接跳转到 非法访问页面或相应业务页面，而不是跳转到登录操作.
 * </p>
 */
@Slf4j
public class MpmAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

    public MpmAuthenticationEntryPoint(String loginFormUrl) {
        super(loginFormUrl);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        // TODO Auto-generated method stub
        super.commence(request, response, authException);

    }

}
