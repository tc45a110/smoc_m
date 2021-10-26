package com.smoc.cloud.admin.security.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 授权失败处理
 * Description:根据授权失败返回的异常，决定错误类型决定异常访问提示页
 */
@Slf4j
public class MpmAccessDeniedExceptionHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {

        //log.info("accessDeniedException:{}",accessDeniedException.getMessage());
		RequestDispatcher dispatcher = request.getRequestDispatcher("/denied");
		dispatcher.forward(request, response);
	}

}
