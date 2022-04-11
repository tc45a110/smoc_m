package com.smoc.cloud.admin.security.handler;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.oauth2.service.OauthTokenService;
import com.smoc.cloud.admin.security.properties.SystemProperties;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.admin.security.sms.MessageConfig;
import com.smoc.cloud.admin.security.sms.SmsUtils;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.validator.SystemUserLogValidator;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.util.StringUtils;

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
public class MpmAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private SystemProperties systemProperties;

    private MessageConfig messageConfig;

    private OauthTokenService oauthTokenService;

    public MpmAuthenticationSuccessHandler(OauthTokenService oauthTokenService, SystemProperties systemProperties, MessageConfig messageConfig) {
        this.oauthTokenService = oauthTokenService;
        this.systemProperties = systemProperties;
        this.messageConfig = messageConfig;

    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        // 认证成功后，获取用户信息并添加到session中
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        ResponseData<SecurityUser> user = oauthTokenService.getUser(userDetails.getUsername());
        //记录日志
        log.info("[用户登录][系统安全][{}]数据:{}",user.getData().getUserName(), systemProperties.getSystemMarking());
        SecurityUser u = user.getData();
        request.getSession().setAttribute("user", u);

        //添加web登录记录
        saveSysWebLogin(systemProperties,u);

        if(StringUtils.isEmpty(systemProperties.getMainUrl())) {
            super.setDefaultTargetUrl("/main");
        }else{
            super.setDefaultTargetUrl(systemProperties.getMainUrl());
        }
        super.onAuthenticationSuccess(request, response, authentication);

    }

    /**
     * 保存web登录记录
     * @param systemProperties
     * @param user
     */
    private void saveSysWebLogin(SystemProperties systemProperties,SecurityUser user) {
        if("smoc-service".equals(systemProperties.getSystemMarking())){
            SystemUserLogValidator systemUserLogValidator = new SystemUserLogValidator();
            systemUserLogValidator.setId(UUID.uuid32());
            systemUserLogValidator.setUserId(user.getUserName());
            systemUserLogValidator.setModule("WEB-LOGIN");
            systemUserLogValidator.setModuleId(user.getOrganization());
            systemUserLogValidator.setOperationType(systemProperties.getSystemMarking());
            systemUserLogValidator.setSimpleIntroduce("WEB登录：自服务系统");
            systemUserLogValidator.setLogData(JSON.toJSONString(user));
            systemUserLogValidator.setCreatedTime(DateTimeUtils.getNowDateTime());
            oauthTokenService.postSystemUserLog(systemUserLogValidator);
        }
    }

}
