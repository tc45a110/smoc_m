package com.smoc.cloud.admin.security.verifycode;

import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.nacos.client.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 生成图形验证码
 * <p>
 * Description:生成图形验证码，并把验证码存放到session中，key为verifyCode
 * </p>
 * 
 * @author wujihui
 *
 */
public class VerifyCodeInitializer extends HttpServlet {

	private static final long serialVersionUID = 7819070874852081945L;

	private static final Logger logger = LoggerFactory.getLogger(VerifyCodeInitializer.class);

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("image/jpeg");
		// 设置相应类型,告诉浏览器输出的内容为图片
		response.setHeader("Pragma", "No-cache");
		// 设置响应头信息，告诉浏览器不要缓存此内容
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expire", 0);

		try {
			// 生成随机验证码
			int charSize = 4;
			String verifyCode = RandomVerifyImgCodeUtil.generateVerifyCode(charSize);

			// 把验证码数字放到session里
			if (StringUtils.isNotBlank(verifyCode)) {
				// logger.info("图形验证码--" + verifyCode);
				request.getSession().setAttribute("verifyCode", verifyCode.toLowerCase());
			}

			// 生成图片规格w宽 h高
			int w = 100, h = 36;
			int type = new Random().nextInt(7);
			if (type == 0) {
				RandomVerifyImgCodeUtil.outputImage(w, h, response.getOutputStream(), verifyCode, "login");
			} else if (type == 1) {
				RandomVerifyImgCodeUtil.outputImage(w, h, response.getOutputStream(), verifyCode, "GIF");
			} else if (type == 2) {
				RandomVerifyImgCodeUtil.outputImage(w, h, response.getOutputStream(), verifyCode, "3D");
			} else if (type == 3) {
				RandomVerifyImgCodeUtil.outputImage(w, h, response.getOutputStream(), verifyCode, "GIF3D");
			} else if (type == 4) {
				RandomVerifyImgCodeUtil.outputImage(w, h, response.getOutputStream(), verifyCode, "mix2");
			} else if (type == 5) {
				RandomVerifyImgCodeUtil.outputImage(w, h, response.getOutputStream(), verifyCode, "mixGIF");
			} else if (type == 6) {
				RandomVerifyImgCodeUtil.outputImage(w, h, response.getOutputStream(), verifyCode, "coupons");
			} else {
				RandomVerifyImgCodeUtil.outputImage(w, h, response.getOutputStream(), verifyCode, "mixGIF");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
