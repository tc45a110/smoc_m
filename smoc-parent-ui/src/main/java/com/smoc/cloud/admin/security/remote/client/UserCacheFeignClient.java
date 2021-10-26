package com.smoc.cloud.admin.security.remote.client;

import com.smoc.cloud.common.auth.entity.Token;
import com.smoc.cloud.common.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 用户缓存远程服务接口
 * 2019/4/23 16:28
 **/
@FeignClient(name = "smoc-auth", path = "/auth")
public interface UserCacheFeignClient {


    /**
     * 清除用户缓存  缓存信息包括 用户信息、角色信息、菜单信息
     *
     * @return
     */
    @RequestMapping(value = "/clearUserCache/{projectName}/{userId}/{userName}", method = RequestMethod.GET)
    ResponseData clearUserCache(@PathVariable(value = "projectName") String projectName, @PathVariable(value = "userId") String userId, @PathVariable(value = "userName") String userName) throws Exception;

    /**
     * 缓存sso 的token
     * @param key
     * @param token
     * @return
     */
    @RequestMapping(value = "/cacheSSOToekn/{key}/{token}", method = RequestMethod.GET)
    ResponseData cacheSSOToekn(@PathVariable(value = "key") String key, @PathVariable(value = "token") String token);

    /**
     * 获取sso token  并使token 失效
     * @param key
     * @return
     */
    @RequestMapping(value = "/getSSOToken/{key}", method = RequestMethod.GET)
    ResponseData<Token> getSSOToken(@PathVariable String key);

}
