package com.smoc.cloud.auth.data.provider.service;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.auth.common.utils.MpmEncryptPasswordEncoder;
import com.smoc.cloud.auth.data.provider.entity.BaseUser;
import com.smoc.cloud.auth.data.provider.entity.BaseUserExtends;
import com.smoc.cloud.auth.data.provider.entity.BaseUserRole;
import com.smoc.cloud.auth.data.provider.repository.BaseUserExtendsRepository;
import com.smoc.cloud.auth.data.provider.repository.BaseUserRepository;
import com.smoc.cloud.auth.data.provider.repository.BaseUserRoleRepository;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.qo.Users;
import com.smoc.cloud.common.auth.validator.BaseUserExtendsValidator;
import com.smoc.cloud.common.auth.validator.BaseUserValidator;
import com.smoc.cloud.common.auth.validator.UserValidator;
import com.smoc.cloud.common.constant.RedisConstant;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.utils.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * 完成用户管理模块
 * 在service层完成 业务 规则验证
 * 2019/3/29 14:29
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BaseUserService {

    @Resource
    private BaseUserRepository baseUserRepository;

    @Resource
    private BaseUserExtendsRepository baseUserExtendsRepository;

    @Resource
    private BaseUserRoleRepository baseUserRoleRepository;

    @Resource
    private RedisTemplate<String, SecurityUser> redisTemplate;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public PageList<Users> page(PageParams<Users> pageParams) {
        return baseUserRepository.page(pageParams);
    }

    /**
     * 根据用户名查询用户  加入redis 优化
     */
    public ResponseData<SecurityUser> findUserByUserName(String userName) {

        SecurityUser user = null;
        //判断是否存在key
        boolean hasKey = redisTemplate.hasKey(RedisConstant.AUTH_USERS_PREFIX + ":" + userName);
        if (hasKey) {
            //redis 查询
            user = redisTemplate.opsForValue().get(RedisConstant.AUTH_USERS_PREFIX + ":" + userName);
        } else {
            //数据库查询
            user = baseUserRepository.findSecurityUserByUserName(userName);
            if (null != user) {
                //把数据放到redis里
                redisTemplate.opsForValue().set(RedisConstant.AUTH_USERS_PREFIX + ":" + userName, user);
            }
        }

        return ResponseDataUtil.buildSuccess(user);
    }

    /**
     * 根据用户ca查询
     */
    public ResponseData<SecurityUser> findUserByCa(String ca) {

        SecurityUser  user = baseUserRepository.findSecurityUserByCa(ca);

        return ResponseDataUtil.buildSuccess(user);
    }



    /**
     * 根据组织id 查询用户
     * @param orgId
     * @return
     */
    public ResponseData<List<SecurityUser>> findSecurityUserByOrgId(String orgId){

        List<SecurityUser> users = baseUserRepository.findSecurityUserByOrgId(orgId);
        return ResponseDataUtil.buildSuccess(users);
    }

    /**
     * 根据手机查询用户
     */
    public ResponseData<UserValidator> findUserByPhone(String phone) {

        BaseUser user = baseUserRepository.findBaseUserByPhoneAndActive(phone, 1);
        if (null == user) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }

        UserValidator userValidator = new UserValidator();
        BaseUserValidator baseUserValidator = new BaseUserValidator();
        BeanUtils.copyProperties(user, baseUserValidator);
        userValidator.setBaseUserValidator(baseUserValidator);
        return ResponseDataUtil.buildSuccess(userValidator);
    }

    /**
     * 根据手机、用户名查询用户
     */
    public ResponseData<BaseUser> findUserByUserNameOrPhone(String userName, String phone) {

        BaseUser user = baseUserRepository.findBaseUserByUserNameAndActiveOrPhone(userName, 1, phone);
        if (null == user) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }

        return ResponseDataUtil.buildSuccess(user);
    }

    /**
     * 重置密码
     *
     * @param id
     * @param password
     */
    @Transactional
    public ResponseData<BaseUser> resetPassword(String id, String password) {

        //加密操作
        password = MpmEncryptPasswordEncoder.getPasswordEncoder().encode(password);

        //记录日志
        log.info("[用户管理][重置密码]数据:{}",id);
        baseUserRepository.resetPassword(id, password);
        BaseUser user = baseUserRepository.findById(id).get();

        //数据库查询
        SecurityUser sysUser = baseUserRepository.findSecurityUserByUserName(user.getUserName());
        if (null != sysUser) {
            //把数据放到redis里
            redisTemplate.opsForValue().set(RedisConstant.AUTH_USERS_PREFIX + ":" + user.getUserName(), sysUser);
        }


        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 根据ID 查询数据
     *
     * @param id
     * @return
     */
    public ResponseData<UserValidator> findById(String id) {

        //查询主表及扩展表数据
        UserValidator userValidator = new UserValidator();
        BaseUserValidator baseUserValidator = new BaseUserValidator();
        BaseUserExtendsValidator baseUserExtendsValidator = new BaseUserExtendsValidator();

        Optional<BaseUser> data = baseUserRepository.findById(id);
        if (data.isPresent()) {
            BaseUser user = data.get();
            user.setPassword(null);
            BaseUserExtends userExtends = baseUserExtendsRepository.findById(id).get();

            //对象copy
            BeanUtils.copyProperties(user, baseUserValidator);
            BeanUtils.copyProperties(userExtends, baseUserExtendsValidator);

            userValidator.setBaseUserValidator(baseUserValidator);
            userValidator.setBaseUserExtendsValidator(baseUserExtendsValidator);

            //查询角色
            String roleIds = "";
            List<BaseUserRole> roles = baseUserRoleRepository.findBaseUserRoleByUserId(user.getId());
            for (BaseUserRole role : roles) {
                if (StringUtils.isEmpty(roleIds)) {
                    roleIds = role.getRoleId();
                } else {
                    roleIds += "," + role.getRoleId();
                }
            }
            userValidator.setRoleIds(roleIds);
        }

        return ResponseDataUtil.buildSuccess(userValidator);
    }

    /**
     * 添加、修改
     *
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    @Transactional
    public ResponseData save(UserValidator userValidator, String op) {

        BaseUser user = new BaseUser();
        BaseUserExtends userExtends = new BaseUserExtends();
        BeanUtils.copyProperties(userValidator.getBaseUserValidator(), user);
        BeanUtils.copyProperties(userValidator.getBaseUserExtendsValidator(), userExtends);

        //编码转大写
        userExtends.setCode(userExtends.getCode().toUpperCase());
        userExtends.setParentCode(userExtends.getParentCode().toUpperCase());

        //查重 userName
        List<BaseUser> list = baseUserRepository.findBaseUserByUserNameAndActive(user.getUserName(), 1);

        //进行密码加密操作
        if ("add".equals(op)) {

            //查重 userName
            if (null != list && list.size() > 0) {
                return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
            }

            user.setPassword(MpmEncryptPasswordEncoder.getPasswordEncoder().encode(user.getPassword()));
        } else if ("edit".equals(op)) {

            //查重 userName
            boolean status = false;
            Iterator iter = list.iterator();
            while (iter.hasNext()) {
                BaseUser baseUser = (BaseUser) iter.next();
                if (!user.getId().equals(baseUser.getId())) {
                    status = true;
                    break;
                }
            }
            if (status) {
                return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
            }

            BaseUser u = baseUserRepository.findById(user.getId()).get();
            user.setPassword(u.getPassword());

        }

        //op 不为 edit 或 add
        if (!("edit".equals(op) || "add".equals(op))) {
            return ResponseDataUtil.buildError();
        }

        //记录日志
        log.info("[用户管理][{}]数据:{}",op,JSON.toJSONString(user));
        log.info("[用户管理][{}]数据:{}",op,JSON.toJSONString(userExtends));


        //保存或修改数据
        baseUserRepository.saveAndFlush(user);
        baseUserExtendsRepository.saveAndFlush(userExtends);

        //保存角色
        List<BaseUserRole> userRoles = new ArrayList<>();
        if (!StringUtils.isEmpty(userValidator.getRoleIds())) {
            String[] roles = userValidator.getRoleIds().split(",");
            for (int i = 0; i < roles.length; i++) {
                BaseUserRole baseUserRole = new BaseUserRole();
                baseUserRole.setId(UUID.uuid32());
                baseUserRole.setUserId(user.getId());
                baseUserRole.setRoleId(roles[i]);
                userRoles.add(baseUserRole);
            }
        }
        baseUserRoleRepository.deleteByUserId(user.getId());
        baseUserRoleRepository.batchSave(userRoles);

        //数据库查询
        SecurityUser sysUser = baseUserRepository.findSecurityUserByUserName(user.getUserName());
        if (null != sysUser) {
            //把数据放到redis里
            redisTemplate.opsForValue().set(RedisConstant.AUTH_USERS_PREFIX + ":" + user.getUserName(), sysUser);
        }


        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 根据ID 删除用户
     *
     * @param id
     * @return
     */
    @Transactional
    public ResponseData deleteById(String id) {

        BaseUser user = baseUserRepository.findById(id).get();
        BaseUserExtends userExtends = baseUserExtendsRepository.findById(id).get();
        //记录日志
        log.info("[用户管理][delete]数据:{}",JSON.toJSONString(user));
        log.info("[用户管理][delete]数据:{}",JSON.toJSONString(userExtends));
        baseUserRepository.deleteById(id);
        baseUserExtendsRepository.deleteById(id);
        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 禁用、启用用户
     *
     * @param id
     * @return
     */
    public ResponseData forbiddenUser(String id, Integer status) {
        //记录日志
        log.info("[用户管理][forbidden]数据:{}",id);
        baseUserRepository.forbiddenUser(id, status);
        return ResponseDataUtil.buildSuccess();
    }

    public boolean exists(String id) {
        return baseUserRepository.existsById(id);
    }

    public ResponseData<UserValidator> findByUserId(String id) {
        //查询主表及扩展表数据

        BaseUserExtends userExtends = baseUserExtendsRepository.findById(id).get();

        //上级
        BaseUserExtends extend = baseUserExtendsRepository.findByCode(userExtends.getParentCode());
        if(null == extend){
            UserValidator userValidator = new UserValidator();
            return ResponseDataUtil.buildSuccess(userValidator);
        }

        BaseUser user = baseUserRepository.findById(extend.getId()).get();

        //对象copy
        BaseUserValidator baseUserValidator = new BaseUserValidator();
        BaseUserExtendsValidator baseUserExtendsValidator = new BaseUserExtendsValidator();
        BeanUtils.copyProperties(user, baseUserValidator);
        BeanUtils.copyProperties(extend, baseUserExtendsValidator);

        UserValidator userValidator = new UserValidator();
        userValidator.setBaseUserValidator(baseUserValidator);
        userValidator.setBaseUserExtendsValidator(baseUserExtendsValidator);

        return ResponseDataUtil.buildSuccess(userValidator);
    }

    public ResponseData<UserValidator> findUserByCode(String teamCode) {
        BaseUserExtends user = baseUserExtendsRepository.findByCode(teamCode);
        if (null == user) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }

        UserValidator userValidator = new UserValidator();
        BaseUserExtendsValidator baseUserExtendsValidator = new BaseUserExtendsValidator();
        BeanUtils.copyProperties(user, baseUserExtendsValidator);
        userValidator.setBaseUserExtendsValidator(baseUserExtendsValidator);
        return ResponseDataUtil.buildSuccess(userValidator);
    }

    public BaseUser findBaseUserById(String id) {
        BaseUser user = baseUserRepository.findById(id).get();
        return user;
    }

    @Transactional
    public ResponseData closeUser(String id, String status) {

        BaseUser user = baseUserRepository.findById(id).get();

        baseUserRepository.updateUserActive(user.getId(),status);

        //redis里帐号删除，登录会重新查数据
        if("0".equals(status)){
            redisTemplate.delete(RedisConstant.AUTH_USERS_PREFIX + ":" + user.getUserName());
        }

        //记录日志
        log.info("[用户管理][forbidden]数据:{}",JSON.toJSONString(user));

        return ResponseDataUtil.buildSuccess();
    }

    public ResponseData batchForbiddenUser(List<SecurityUser> userList, String status) {

        if(!StringUtils.isEmpty(userList) && userList.size()>0){
            baseUserRepository.batchUpdateUserActive(userList,status);

            //redis里帐号删除，登录会重新查数据
            if("0".equals(status)){
                for(SecurityUser user:userList){
                    redisTemplate.delete(RedisConstant.AUTH_USERS_PREFIX + ":" + user.getUserName());
                }
            }
            //记录日志
            log.info("[用户管理][batchForbidden]数据:{}",JSON.toJSONString(userList));
        }

        return ResponseDataUtil.buildSuccess();
    }
}
