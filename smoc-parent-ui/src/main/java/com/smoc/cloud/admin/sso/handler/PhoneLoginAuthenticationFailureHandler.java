package com.smoc.cloud.admin.sso.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录失败处理
 * <p>
 * Description:登录失败处理，配合MpmUsernamePasswordAuthenticationFilter
 * </p>
 * 
 * @author Administrator
 *
 */
@Slf4j
public class PhoneLoginAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {



	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {

		/**
		 * 定义登录错误返回连接，并携带错误类型
		 */
		String authenticationFailureUrl = "/login/"+exception.getMessage();
		super.setDefaultFailureUrl(authenticationFailureUrl);
		super.onAuthenticationFailure(request, response, exception);

	}

}