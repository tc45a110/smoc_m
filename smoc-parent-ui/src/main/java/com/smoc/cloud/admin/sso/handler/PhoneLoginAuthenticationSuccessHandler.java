package com.smoc.cloud.admin.sso.handler;

import com.google.gson.Gson;
import com.smoc.cloud.admin.oauth2.service.OauthTokenService;
import com.smoc.cloud.admin.security.properties.SystemProperties;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.response.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户登录成功
 * <p>
 * Description:表示用户登录成功，可以根据需要，用户登录成功后加入个性化的业务逻辑；如初始化客户个性化数据等等
 * </p>
 */
@Slf4j
public class  PhoneLoginAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private SystemProperties systemProperties;

    private OauthTokenService oauthTokenService;

    public PhoneLoginAuthenticationSuccessHandler(OauthTokenService oauthTokenService, SystemProperties systemProperties) {
        this.oauthTokenService = oauthTokenService;
        this.systemProperties = systemProperties;

    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        // 认证成功后，获取用户信息并添加到session中
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        ResponseData<SecurityUser> user = oauthTokenService.getUserByCA(userDetails.getUsername());

        //记录日志
        log.info("[用户登录][系统安全][{}]数据:{}",user.getData().getUserName(), systemProperties.getSystemMarking());
        log.info("[用户登录][]数据:{}",new Gson().toJson(user.getData()));
        SecurityUser u = user.getData();
        request.getSession().setAttribute("user", u);
        request.getSession().setAttribute("token", u.getDepartment());
        super.setDefaultTargetUrl("/main");
        super.onAuthenticationSuccess(request, response, authentication);

    }

}
