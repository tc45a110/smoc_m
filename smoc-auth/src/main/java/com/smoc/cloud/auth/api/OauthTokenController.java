package com.smoc.cloud.auth.api;

import com.smoc.cloud.auth.data.provider.entity.BaseRole;
import com.smoc.cloud.auth.data.provider.service.BaseRoleService;
import com.smoc.cloud.auth.data.provider.service.BaseUserService;
import com.smoc.cloud.auth.security.configuation.MpmAuthorizationServerConfiguation;
import com.smoc.cloud.auth.security.service.BaseUserDetailService;
import com.smoc.cloud.auth.security.service.MpmTokenService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * oauth2权限接口
 * 2019/4/5 10:42
 **/
@Slf4j
@RestController
@RequestMapping("/oauth")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class OauthTokenController {

    @Autowired
    private MpmTokenService mpmTokenService;

    @Autowired
    private BaseUserService baseUserService;

    @Autowired
    private BaseRoleService baseRoleService;

    @Autowired
    private BaseUserDetailService baseUserDetailService;

    @Autowired
    private MpmAuthorizationServerConfiguation mpmAuthorizationServerConfiguation;


    /**
     * 根据token获取授权用户、角色
     *
     * @param token
     * @return 用户信息
     */
    @PreAuthorize("hasAuthority('SUPER-ROLE')")
    @RequestMapping(value = "/getUser/{token}", method = RequestMethod.GET)
    public SecurityUser getUserByToken(@PathVariable String token) {

        //log.info("/getUser:{}", token);
        ResponseData<SecurityUser> data = mpmTokenService.getUserByToken(token);
        return data.getData();
    }

    /**
     * 根据用户名获取授权用户、角色
     *
     * @param userName
     * @return 用户信息
     */
    @PreAuthorize("hasAuthority('SUPER-ROLE')")
    @RequestMapping(value = "/getUserByUserName/{userName}", method = RequestMethod.GET)
    public SecurityUser getUserByUserName(@PathVariable String userName) {

        ResponseData<SecurityUser> data = baseUserService.findUserByUserName(userName);
        if (data.getData() != null) {

            //加载用户角色信息
            ResponseData<List<BaseRole>> baseRoleListResponseData = baseRoleService.getRoleByUserId(data.getData().getId());
            if (!ResponseCode.SUCCESS.getCode().equals(baseRoleListResponseData.getCode()) || baseRoleListResponseData.getData() == null) {
                log.info(" {}：{}", userName, ResponseCode.USER_UNAUTH.getMessage());
                throw new UsernameNotFoundException(ResponseCode.USER_UNAUTH.getMessage());
            }
            //log.info("加载登录用户角色............");

            List<String> grantedAuthorities = new ArrayList<>();
            baseRoleListResponseData.getData().forEach(baseRole -> {
                // 存储用户、角色信息到GrantedAuthority，并放到GrantedAuthority列表
                GrantedAuthority authority = new SimpleGrantedAuthority(baseRole.getRoleCode());
                grantedAuthorities.add(baseRole.getRoleCode());

            });
            data.getData().setAuthorities(grantedAuthorities);

        }

        return data.getData();
    }

    /**
     * 根据用户名获取授权用户、角色
     *
     * @param ca
     * @return 用户信息
     */
    @PreAuthorize("hasAuthority('SUPER-ROLE')")
    @RequestMapping(value = "/getUserByCA/{ca}", method = RequestMethod.GET)
    public SecurityUser getUserByCA(@PathVariable String ca) {

        ResponseData<SecurityUser> data = baseUserService.findUserByCa(ca);
        if (data.getData() != null) {

            //加载用户角色信息
            ResponseData<List<BaseRole>> baseRoleListResponseData = baseRoleService.getRoleByUserId(data.getData().getId());
            if (!ResponseCode.SUCCESS.getCode().equals(baseRoleListResponseData.getCode()) || baseRoleListResponseData.getData() == null) {
                log.info(" {}：{}", ca, ResponseCode.USER_UNAUTH.getMessage());
                throw new UsernameNotFoundException(ResponseCode.USER_UNAUTH.getMessage());
            }
            //log.info("加载登录用户角色............");

            List<String> grantedAuthorities = new ArrayList<>();
            baseRoleListResponseData.getData().forEach(baseRole -> {
                // 存储用户、角色信息到GrantedAuthority，并放到GrantedAuthority列表
                GrantedAuthority authority = new SimpleGrantedAuthority(baseRole.getRoleCode());
                grantedAuthorities.add(baseRole.getRoleCode());

            });
            data.getData().setAuthorities(grantedAuthorities);

            UserDetails userDetails = baseUserDetailService.loadUserByUsername(data.getData().getUserName());
            String clientId = "176C2F4344C24F399FAE56DEE77B987C";
            ClientDetailsService clientDetailsService = mpmAuthorizationServerConfiguation.clientDetails();
            ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);
            DefaultTokenServices defaultTokenServices = mpmAuthorizationServerConfiguation.defaultTokenServices();
            TokenRequest tokenRequest = new TokenRequest(new HashMap<>(), clientId, clientDetails.getScope(), "meip");
            OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);

            Authentication userAuth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, userAuth);
            OAuth2AccessToken oAuth2AccessToken = defaultTokenServices.createAccessToken(oAuth2Authentication);
            //log.info(new Gson().toJson(oAuth2AccessToken.getValue()));
            data.getData().setDepartment(oAuth2AccessToken.getValue());

        }



        return data.getData();
    }


}
