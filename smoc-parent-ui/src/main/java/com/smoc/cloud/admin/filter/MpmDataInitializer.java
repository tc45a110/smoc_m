package com.smoc.cloud.admin.filter;

import com.smoc.cloud.admin.oauth2.service.OauthTokenService;
import com.smoc.cloud.admin.security.properties.SystemProperties;
import com.smoc.cloud.common.auth.qo.Dict;
import com.smoc.cloud.common.auth.qo.DictType;
import com.smoc.cloud.common.auth.validator.SystemValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 初始化系统数据类
 * <p>
 * Description:项目启动时，初始化项目启动数据，把数据放到context中，增加使用时的效率
 * </p>
 *
 */
@Slf4j
public class MpmDataInitializer extends HttpServlet {

	private static final long serialVersionUID = -632259145658594384L;

	@Autowired
	private SystemProperties systemProperties;

	@Autowired
	private OauthTokenService oauthTokenService;


	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub

		super.init();
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub

		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());

		/**
		 * 加载系统字典数据
		 */
		log.info("[系统启动][数据初始化]数据:系统启动加载系统公用字典数据");
		Map<String, DictType> dict = oauthTokenService.loadSysDict(systemProperties.getSystemMarking());
		config.getServletContext().setAttribute("dict", dict);

		log.info("[系统启动][数据初始化]数据:系统启动加载系统SSO列表数据");
		Map<String, SystemValidator> sysMap = oauthTokenService.getSystemList();
		config.getServletContext().setAttribute("sysMap", sysMap);

		super.init(config);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	}

}
