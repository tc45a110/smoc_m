package com.smoc.cloud.admin.sso.filter;

import com.smoc.cloud.admin.sso.properties.SsoCofigurationProperties;
import com.smoc.cloud.admin.sso.token.PhoneAuthenticationToken;
import com.smoc.cloud.admin.sso.util.MD5;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by fp295 on 2018/6/16.
 * 验证码登陆：
 * post: /phoneLogin?phone=13000000000&verifyCode=1000
 */
@Slf4j
public class PhoneLoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired
    private SsoCofigurationProperties ssoCofigurationProperties;

    private static final String SPRING_SECURITY_RESTFUL_ECCODE_KEY = "ecCode";
    private static final String SPRING_SECURITY_RESTFUL_TOKEN_KEY = "token";
    private static final String SPRING_SECURITY_RESTFUL_CACODE_KEY = "caCode";
    private static final String SPRING_SECURITY_RESTFUL_MOBILE_KEY = "mobile";

    private static final String SPRING_SECURITY_RESTFUL_LOGIN_URL = "/sso";

    public PhoneLoginAuthenticationFilter() {
        super(new AntPathRequestMatcher(SPRING_SECURITY_RESTFUL_LOGIN_URL, "GET"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        AbstractAuthenticationToken authRequest;

        String credentials = null;

        // TOKEN登录
        String token = obtainParameter(request, SPRING_SECURITY_RESTFUL_TOKEN_KEY);
        String ecCode = obtainParameter(request, SPRING_SECURITY_RESTFUL_ECCODE_KEY);
        String caCode = obtainParameter(request, SPRING_SECURITY_RESTFUL_CACODE_KEY);
        String mobile = obtainParameter(request, SPRING_SECURITY_RESTFUL_MOBILE_KEY);

        log.info("[SSO][用户登录]eccode数据:{}", ecCode);
        log.info("[SSO][用户登录]caCode数据:{}", caCode);
        log.info("[SSO][用户登录]mobile数据:{}", mobile);
        log.info("[SSO][用户登录] token数据:{}", token);

        //token 验证
        if (StringUtils.isEmpty(token)) {
            throw new UsernameNotFoundException("TokenIsEmpty");
        }

        String newTokenString = ecCode+caCode+mobile;
        String newToken = MD5.mD5(newTokenString.getBytes());
        log.info("[SSO][用户登录]newToken数据:{}", newToken);
        //token 验证
        if (!token.equals(newToken)) {
            throw new UsernameNotFoundException("TokenError");
        }

        //caCode 验证
        if (StringUtils.isEmpty(caCode)) {
            throw new UsernameNotFoundException("TokenIsEmpty");
        }
        caCode = caCode.replace("CA:","");

        /*TokenResult result;

        try {

            String flag = MD5.mD5((newToken + ssoCofigurationProperties.getApiSecret()).getBytes());
            String url = ssoCofigurationProperties.getServerUrl() + flag;
            log.info("[SSO][TOKEN验证] url数据:{}", url);
            String validator = Okhttp3Utils.sendGet(url);
            log.info("[SSO][url返回数据]数据:{}", validator);
            result = new Gson().fromJson(validator, TokenResult.class);

        } catch (Exception e) {
            e.printStackTrace();
            throw new UsernameNotFoundException("TokenServerException");
        }

        if (null == result) {
            log.info("[SSO][用户登录]数据:url返回为空");
            throw new UsernameNotFoundException("TokenError");
        }

        if (!"200".equals(result.getResultCode())) {
            throw new UsernameNotFoundException(result.getResultCode());
        }*/

        //用户校验并登录
        authRequest = new PhoneAuthenticationToken(caCode, credentials);

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
