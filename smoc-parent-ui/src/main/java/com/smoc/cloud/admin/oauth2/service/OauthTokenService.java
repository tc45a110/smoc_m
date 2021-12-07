package com.smoc.cloud.admin.oauth2.service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.entity.Token;
import com.smoc.cloud.common.auth.qo.Dict;
import com.smoc.cloud.common.auth.qo.DictType;
import com.smoc.cloud.common.auth.qo.Nodes;
import com.smoc.cloud.common.auth.qo.RoleMenus;
import com.smoc.cloud.common.auth.validator.SystemValidator;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * OAuth2 token操作
 * 2019/4/14 13:31
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class OauthTokenService {

    @Autowired
    private OAuth2TokenUtils oAuth2TokenUtils;

    /**
     * 获取token
     *
     * @param userName
     * @param password
     * @return
     */
    public ResponseData<OAuth2AccessToken> getAccessToken(String userName, String password) {

        try {
            Map<String,Object> resultMap = oAuth2TokenUtils.getAccessToken(userName, password);
            if(!StringUtils.isEmpty(resultMap.get("error"))){
                ResponseCode responseCode = (ResponseCode) resultMap.get("error");
                return ResponseDataUtil.buildError(responseCode);
            }

            OAuth2AccessToken oAuth2AccessToken = (OAuth2AccessToken)resultMap.get("token");
            if (null == oAuth2AccessToken) {
                return ResponseDataUtil.buildError(ResponseCode.USER_LOGIN_FAILURE);
            }
            return ResponseDataUtil.buildSuccess(oAuth2AccessToken);
        } catch (Exception e) {
            return ResponseDataUtil.buildError(ResponseCode.OAUTH2_EXCEPTION_GET_TOKEN, e);
        }

    }


    /**
     * 刷新token
     *
     * @param refreshToken
     * @return
     */
    public ResponseData<OAuth2AccessToken> refreshAccessToken(String refreshToken) {

        try {
            OAuth2AccessToken oAuth2AccessToken = oAuth2TokenUtils.refreshAccessToken(refreshToken);
            return ResponseDataUtil.buildSuccess(oAuth2AccessToken);
        } catch (Exception e) {
            return ResponseDataUtil.buildError(ResponseCode.OAUTH2_EXCEPTION_GET_TOKEN, e);
        }

    }

    /**
     * 刷新token
     *
     * @param token
     * @return
     */
    public ResponseData<OAuth2Authentication> checkAccessToken(String token) {

        try {
            OAuth2Authentication oAuth2Authentication = oAuth2TokenUtils.checkAccessToken(token);
            return ResponseDataUtil.buildSuccess(oAuth2Authentication);
        } catch (Exception e) {
            return ResponseDataUtil.buildError(ResponseCode.TOKEN_INVALID, e);
        }

    }

    /**
     * 加载系统角色资源
     *
     * @return
     */
    public RoleMenus[] loadRoleMenus() {

        RoleMenus[] roleMenus = null;
        try {
            String url = oAuth2TokenUtils.getAccessTokenUrl();
            ResponseEntity<RoleMenus[]> responseEntity = oAuth2TokenUtils.getOAuth2RestTemplate().getForEntity(url, RoleMenus[].class);
            if (null != responseEntity.getBody()) {
                roleMenus = responseEntity.getBody();
            }
            log.info("[系统启动][数据初始化]数据:启动加载安全访问鉴权数据数据-角色-资源数据{}条",  roleMenus.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return roleMenus;
    }

    /**
     * 加载系统字典
     *
     * @return
     */
    public Map<String, DictType> loadSysDict(String system) {

        Map<String, DictType> result = null;
        try {
            String url = oAuth2TokenUtils.getAuthHost() + "/sysDict/getDict/" + system;
            JSONObject jsonObject = oAuth2TokenUtils.getOAuth2RestTemplate().getForObject(url, JSONObject.class);
            result = JSONObject.parseObject(jsonObject.toJSONString(), new TypeReference<Map<String, DictType>>() {
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 加载系统信息
     *
     * @return
     */
    public Map<String, SystemValidator> getSystemList() {

        Map<String, SystemValidator> result = null;
        try {
            String url = oAuth2TokenUtils.getAuthHost() + "/system/getSystemList";
            JSONObject jsonObject = oAuth2TokenUtils.getOAuth2RestTemplate().getForObject(url, JSONObject.class);
            result = JSONObject.parseObject(jsonObject.toJSONString(), new TypeReference<Map<String, SystemValidator>>() {
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public ResponseData<SecurityUser> getUser(String token) {

        SecurityUser user = null;
        try {
            String url = oAuth2TokenUtils.getAuthHost() + "/oauth/getUser/" + token;
            ResponseEntity<SecurityUser> responseEntity = oAuth2TokenUtils.getOAuth2RestTemplate().getForEntity(url, SecurityUser.class);
            if (null != responseEntity.getBody()) {
                user = responseEntity.getBody();
            }
            return ResponseDataUtil.buildSuccess(user);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDataUtil.buildError(e.getMessage());
        }

    }

    public ResponseData<SecurityUser> getUserByUserName(String userName) {

        SecurityUser user = null;
        try {
            String url = oAuth2TokenUtils.getAuthHost() + "/oauth/getUserByUserName/" + userName;
            ResponseEntity<SecurityUser> responseEntity = oAuth2TokenUtils.getOAuth2RestTemplate().getForEntity(url, SecurityUser.class);
            if (null != responseEntity.getBody()) {
                user = responseEntity.getBody();
            }
            return ResponseDataUtil.buildSuccess(user);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDataUtil.buildError(e.getMessage());
        }

    }

    public ResponseData<SecurityUser> getUserByCA(String ca) {

        SecurityUser user = null;
        try {
            String url = oAuth2TokenUtils.getAuthHost() + "/oauth/getUserByCA/" + ca;
            ResponseEntity<SecurityUser> responseEntity = oAuth2TokenUtils.getOAuth2RestTemplate().getForEntity(url, SecurityUser.class);
            if (null != responseEntity.getBody()) {
                user = responseEntity.getBody();
            }
            return ResponseDataUtil.buildSuccess(user);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDataUtil.buildError(e.getMessage());
        }

    }

    public ResponseData<Token> getSSOToken(String key){

        ResponseData<Token> token = null;
        try {
            String url = oAuth2TokenUtils.getAuthHost() + "/getSSOToken/" + key;
            JSONObject jsonObject = oAuth2TokenUtils.getOAuth2RestTemplate().getForObject(url, JSONObject.class);
            token = JSONObject.parseObject(jsonObject.toJSONString(), new TypeReference<ResponseData<Token>>() {
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }

    public ResponseData<Nodes[]> getUserMenus(String projectName, String userId) {

        Nodes[] nodes = null;
        try {
            String url = oAuth2TokenUtils.getAuthHost() + "/menus/getUserMenus/" + projectName + "/" + userId;
            ResponseEntity<Nodes[]> responseEntity = oAuth2TokenUtils.getOAuth2RestTemplate().getForEntity(url, Nodes[].class);
            if (null != responseEntity.getBody()) {
                nodes = responseEntity.getBody();
            }
            return ResponseDataUtil.buildSuccess(nodes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDataUtil.buildSuccess(e.getMessage());
        }


    }


}
