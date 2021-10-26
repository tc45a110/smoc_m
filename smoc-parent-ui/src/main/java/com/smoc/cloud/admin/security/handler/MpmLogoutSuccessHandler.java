package com.smoc.cloud.admin.security.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户退出
 * <p>
 * Description:用户安全退出后的处理，可以增加个性化功能，不过载系统中，用这个logout 必须用form提交，现在系统自定义了logout操作
 * </p>
 */
public class MpmLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		// TODO Auto-generated method stub

		/**
		 * 可以在此添加自己的注销业务处理逻辑
		 */
		super.onLogoutSuccess(request, response, authentication);
	}

}
