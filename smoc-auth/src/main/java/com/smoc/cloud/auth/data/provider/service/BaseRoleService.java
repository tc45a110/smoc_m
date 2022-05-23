package com.smoc.cloud.auth.data.provider.service;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.auth.data.provider.entity.BaseRole;
import com.smoc.cloud.auth.data.provider.entity.BaseUser;
import com.smoc.cloud.auth.data.provider.entity.BaseUserRole;
import com.smoc.cloud.auth.data.provider.repository.BaseRoleRepository;
import com.smoc.cloud.auth.data.provider.repository.BaseUserRepository;
import com.smoc.cloud.auth.data.provider.repository.BaseUserRoleRepository;
import com.smoc.cloud.common.constant.RedisConstant;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.qo.ServiceAuthInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author 武基慧
 * @Description //security授权
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BaseRoleService {

    @Resource
    private BaseRoleRepository baseRoleRepository;

    @Resource
    private BaseUserRoleRepository baseUserRoleRepository;

    @Resource
    private RedisTemplate<String, Map<String, Object>> redisTemplate;

    @Resource
    private RedisTemplate<String, BaseRole> roleRedisTemplate;

    @Resource
    private UserCacheService userCacheService;

    @Resource
    private BaseUserRepository baseUserRepository;

    /**
     * 根据用户查询角色  redis 优化查询
     *
     * @param userId
     * @return
     */
    public ResponseData<List<BaseRole>> getRoleByUserId(String userId) {

        List<BaseRole> roles = null;

        //判断是否存在key
        boolean hasKey = redisTemplate.hasKey(RedisConstant.AUTH_USER_ROLES + ":" + userId);
        if (hasKey) {
            //redis 查询
            roles = roleRedisTemplate.opsForList().range(RedisConstant.AUTH_USER_ROLES + ":" + userId, 0, -1);
        } else {
            //数据库查询
            roles = baseRoleRepository.findBaseRoleByUserId(userId);
            //数据存储到redis
            if (null != roles && roles.size() > 0) {
                for (BaseRole entity : roles) {
                    roleRedisTemplate.opsForList().rightPush(RedisConstant.AUTH_USER_ROLES + ":" + userId, entity);
                }
            }
        }

        return ResponseDataUtil.buildSuccess(roles);
    }

    /**
     * 查询角色数据
     *
     * @return
     */
    public ResponseData<List<BaseRole>> findAll() {
        List<BaseRole> data = baseRoleRepository.findAll();
        return ResponseDataUtil.buildSuccess(data);
    }

    public ResponseData<List<Map<String, Object>>> finsdRolesAndMenus(String systemId) {

        List<Map<String, Object>> menus = null;

        menus = redisTemplate.opsForList().range(RedisConstant.RROLE_MENUS, 0, -1);
        if (null == menus || menus.size() <= 0) {
            log.info("[角色菜单][查询]数据:数据库查询");
            menus = baseRoleRepository.findRolesAndMenus(systemId);
            for (Map<String, Object> entity : menus) {
                log.info((String) entity.get("ROLE_NAME"));
                log.info((String) entity.get("MODULE_NAME"));
                redisTemplate.opsForList().rightPush(RedisConstant.RROLE_MENUS, entity);
            }
        }
        return ResponseDataUtil.buildSuccess(menus);
    }

    /**
     * 根据ID 查询角色
     *
     * @return
     */
    public ResponseData<BaseRole> findById(String id) {

        Optional<BaseRole> data = baseRoleRepository.findById(id);

        //使用了 Optional 的判断方式，判断优雅
        return ResponseDataUtil.buildSuccess(data.orElse(null));
    }

    /**
     * 添加、修改
     *
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    @Transactional
    public ResponseData<BaseRole> save(BaseRole entity, String op) {

        Iterable<BaseRole> data = baseRoleRepository.findBaseRoleByRoleCodeOrAndRoleName(entity.getRoleCode(), entity.getRoleName());

        //add查重roleCode、roleName
        if (data != null && data.iterator().hasNext() && "add".equals(op)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
        }
        //edit查重roleCode、roleName
        else if (data != null && data.iterator().hasNext() && "edit".equals(op)) {
            boolean status = false;
            Iterator iter = data.iterator();
            while (iter.hasNext()) {
                BaseRole role = (BaseRole) iter.next();
                if (!entity.getId().equals(role.getId())) {
                    status = true;
                    break;
                }
            }
            if (status) {
                return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
            }
        }

        //op 不为 edit 或 add
        if (!("edit".equals(op) || "add".equals(op))) {
            return ResponseDataUtil.buildError();
        }

        //记录日志
        log.info("[角色菜单][{}]数据:{}",op,JSON.toJSONString(entity));
        baseRoleRepository.saveAndFlush(entity);
        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 根据ID 删除数据
     *
     * @param id
     * @return
     */
    @Transactional
    public ResponseData<BaseRole> deleteById(String id) {

        BaseRole data = baseRoleRepository.findById(id).get();
        //记录日志
        log.info("[角色菜单][delete]数据:{}",JSON.toJSONString(data));
        baseRoleRepository.deleteById(id);
        return ResponseDataUtil.buildSuccess();
    }

    public boolean exists(String id) {

        return baseRoleRepository.existsById(id);
    }

    /**
     * 根据用户id查询自服务平台角色
     * @param id
     * @return
     */
    public ResponseData<List<ServiceAuthInfo>> webLoginAuth(String id) {

        List<ServiceAuthInfo> authList = new ArrayList<>();

        //查询自服务平台角色
        List<BaseRole> list = baseRoleRepository.webLoginAuth();

        //查询用户角色
        List<BaseUserRole> roles = baseUserRoleRepository.findBaseUserRoleByUserId(id);

        if(!StringUtils.isEmpty(list) && list.size()>0){
            for(int i=0;i<list.size();i++){
                BaseRole role = list.get(i);
                ServiceAuthInfo info = new ServiceAuthInfo();
                info.setRoleIds(role.getId());
                info.setRoleName(role.getRoleName());
                info.setCheckedStatus(false);

                if(!StringUtils.isEmpty(roles) && roles.size()>0){
                    for(int a=0;a<roles.size();a++){
                        BaseUserRole b = roles.get(a);
                        if(role.getId().equals(b.getRoleId())){
                            info.setCheckedStatus(true);
                            break;
                        }
                    }
                }

                authList.add(info);
            }
        }

        return ResponseDataUtil.buildSuccess(authList);
    }

    @Transactional
    public ResponseData<List<ServiceAuthInfo>> webAuthSave(ServiceAuthInfo serviceAuthInfo) {
        //查询自服务平台角色
        List<BaseRole> list = baseRoleRepository.webLoginAuth();

        //先把自服务平台用户权限删除(不能删除其他系统权限)
        baseUserRoleRepository.batchDeleteByUserIdAndRoleId(serviceAuthInfo.getUserId(),list);

        //在把新添加的权限放进去
        baseUserRoleRepository.batchSaveWebAuth(serviceAuthInfo);

        Optional<BaseUser> data = baseUserRepository.findById(serviceAuthInfo.getUserId());
        String userName = "";
        if(data.isPresent()){
            userName = data.get().getUserName();
        }

        //清除缓存
        userCacheService.clearUserCache("smoc-service",serviceAuthInfo.getUserId(),userName);

        return ResponseDataUtil.buildSuccess();
    }
}
