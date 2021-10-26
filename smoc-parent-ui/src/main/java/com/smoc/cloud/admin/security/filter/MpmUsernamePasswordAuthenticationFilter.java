package com.smoc.cloud.admin.security.filter;

import com.smoc.cloud.admin.oauth2.service.OauthTokenService;
import com.smoc.cloud.admin.security.properties.SystemProperties;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.utils.RSAUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 登录过滤器 或是拦截器
 * <p>
 * Description:拦截器拦截特定（或是配置）登录的url、用户名、密码等，自己扩展了图形验证码
 * </p>
 */
@Slf4j
public class MpmUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    @Autowired
    private OauthTokenService oauthTokenService;

    @Autowired
    private SystemProperties systemProperties;

    // 扩展的图形验证码参数
    public static final String SPRING_SECURITY_FORM_VERIFYCODE_KEY = "verifyCode";

    private String verifyCodeParameter = SPRING_SECURITY_FORM_VERIFYCODE_KEY;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        // 强制手动判断是否为post提交
        if (!request.getMethod().equals("POST")) {
            log.error("非法提交登录，登录必须为POST提交");
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        String userName = obtainUsername(request);
        String password = obtainPassword(request);

        //万一前台生成publishKey是空的就不用加密，至少能让登录成功
        if(userName.length()>20){
            userName = RSAUtils.decryptBase64(userName.trim());
            password = RSAUtils.decryptBase64(password.trim());
        }

        if(log.isDebugEnabled()){
            log.debug("用户登录：{},{},{}",userName,password,"");
        }

        /**
         * 判定验证码
         */
        if(systemProperties.getEnableVerifyCode()) {
            String verifyCode = obtainVerifyCode(request);
            String vcode = (String) request.getSession().getAttribute("verifyCode");
            request.getSession().setAttribute("verifyCode", "");//让验证码失效
            if (StringUtils.isEmpty(verifyCode) || !vcode.toLowerCase().equals(verifyCode.toLowerCase().trim())) {
                throw new AuthenticationServiceException("verify");
            }
        }

        //查询密码错误次数（30分钟内）
        Integer sendCount = (Integer) redisTemplate.opsForValue().get("passwordCount:" +userName);
        if (null != sendCount && sendCount >= 5) {
            throw new AuthenticationServiceException("userIsLocked");
        }

        /**
         * 用户登录验证，并返回用户OAuth2AccessToken
         *
         */
        //log.info("登录请求..............");
        ResponseData<OAuth2AccessToken> oAuth2AccessToken = oauthTokenService.getAccessToken(userName, password);
        if (!ResponseCode.SUCCESS.getCode().equals(oAuth2AccessToken.getCode())) {
            log.info("[用户登录][系统安全][{}]数据:{}", userName, oAuth2AccessToken.getMessage());

            if(ResponseCode.USER_NOT_EXIST.getCode().equals(oAuth2AccessToken.getCode())){
                throw new AuthenticationServiceException("UserNotFound");
            }

            if(ResponseCode.USER_PASSWORD_NULL.getCode().equals(oAuth2AccessToken.getCode())){
                //记数 统计密码错误次数  30分钟内
                redisTemplate.opsForValue().increment("passwordCount:" +userName);
                Integer count = (Integer) redisTemplate.opsForValue().get("passwordCount:" +userName);
                if (count == 1) {
                    redisTemplate.expire("passwordCount:" +userName, 30 * 60, TimeUnit.SECONDS);
                    throw new AuthenticationServiceException("passwordInvalidOneTimes");
                }else if(count == 2){
                    throw new AuthenticationServiceException("passwordInvalidTwoTimes");
                }else if(count == 3){
                    throw new AuthenticationServiceException("passwordInvalidThreeTimes");
                }else if(count == 4){
                    throw new AuthenticationServiceException("passwordInvalidFourTimes");
                }else if(count == 5){
                    throw new AuthenticationServiceException("passwordInvalidFiveTimes");
                }
            }

            if(ResponseCode.USER_LOGIN_FAILURE.getCode().equals(oAuth2AccessToken.getCode())){
                throw new AuthenticationServiceException("TokenError");
            }

            throw new AuthenticationServiceException("failure");
        }else{
            if (null != sendCount && sendCount > 0) {
                redisTemplate.expire("passwordCount:" +userName, 1 * 60, TimeUnit.SECONDS);
            }
        }

        //查询新手引导次数 8640是一天
        /*Integer guideCount = (Integer) redisTemplate.opsForValue().get("guideCount:" +userName);
        if (StringUtils.isEmpty(guideCount) || guideCount < 5) {
            redisTemplate.opsForValue().increment("guideCount:" +userName);
            Integer count = (Integer) redisTemplate.opsForValue().get("guideCount:" +userName);
            if (count == 1) {
                redisTemplate.expire("guideCount:" +userName, 864000000, TimeUnit.SECONDS);
            }
            request.getSession().setAttribute("guideCount", "1");
        }*/

        request.getSession().setAttribute("token", oAuth2AccessToken.getData().getValue());


        Authentication userAuthentication = null;
        /**
         * 登录成功
         */
        try {
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                    oAuth2AccessToken.getData().getValue(), oAuth2AccessToken.getData().getValue());
            userAuthentication = authenticationManager.authenticate(authRequest);
        } catch (Exception e) {
            log.error("用户登录异常：{}", e.getMessage());
            e.printStackTrace();
            throw e;
        }

        return userAuthentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);

    }

    /**
     * 登录失败
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {

        super.unsuccessfulAuthentication(request, response, failed);
    }

    protected String obtainVerifyCode(HttpServletRequest request) {
        return request.getParameter(verifyCodeParameter);
    }

    public String getVerifyCodeParameter() {
        return verifyCodeParameter;
    }

    public void setVerifyCodeParameter(String verifyCodeParameter) {
        this.verifyCodeParameter = verifyCodeParameter;
    }

}
