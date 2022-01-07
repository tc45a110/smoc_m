package com.smoc.cloud.customer.service;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.auth.service.AuthorityService;
import com.smoc.cloud.common.auth.validator.BaseUserExtendsValidator;
import com.smoc.cloud.common.auth.validator.BaseUserValidator;
import com.smoc.cloud.common.auth.validator.UserValidator;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseWebAccountInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.customer.entity.EnterpriseBasicInfo;
import com.smoc.cloud.customer.entity.EnterpriseWebAccountInfo;
import com.smoc.cloud.customer.repository.EnterpriseRepository;
import com.smoc.cloud.customer.repository.EnterpriseWebRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.Optional;

/**
 * 企业WEB登录账号管理
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class EnterpriseWebService {

    @Resource
    private EnterpriseWebRepository enterpriseWebRepository;

    @Resource
    private EnterpriseRepository enterpriseRepository;

    @Resource
    private AuthorityService authorityService;


    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    public ResponseData findById(String id) {
        Optional<EnterpriseWebAccountInfo> data = enterpriseWebRepository.findById(id);

        if (!data.isPresent()) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }

        EnterpriseWebAccountInfo entity = data.get();
        EnterpriseWebAccountInfoValidator enterpriseWebAccountInfoValidator = new EnterpriseWebAccountInfoValidator();
        BeanUtils.copyProperties(entity, enterpriseWebAccountInfoValidator);

        return ResponseDataUtil.buildSuccess(enterpriseWebAccountInfoValidator);
    }

    /**
     * 保存或修改
     *
     * @param enterpriseWebAccountInfoValidator
     * @param op   操作类型 为add、edit
     * @return
     */
    @Transactional
    public ResponseData<EnterpriseWebAccountInfo> save(EnterpriseWebAccountInfoValidator enterpriseWebAccountInfoValidator, String op) {

        Iterable<EnterpriseWebAccountInfo> data = enterpriseWebRepository.findByWebLoginName(enterpriseWebAccountInfoValidator.getWebLoginName());

        EnterpriseWebAccountInfo entity = new EnterpriseWebAccountInfo();
        BeanUtils.copyProperties(enterpriseWebAccountInfoValidator, entity);

        //add查重
        if (data != null && data.iterator().hasNext() && "add".equals(op)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
        }
        //edit查重
        else if (data != null && data.iterator().hasNext() && "edit".equals(op)) {
            boolean status = false;
            Iterator iter = data.iterator();
            while (iter.hasNext()) {
                EnterpriseWebAccountInfo info = (EnterpriseWebAccountInfo) iter.next();
                if (!entity.getId().equals(info.getId())) {
                    status = true;
                    break;
                }
            }
            if (status) {
                return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
            }
        }

        //转换日期格式
        entity.setCreatedTime(DateTimeUtils.getDateTimeFormat(enterpriseWebAccountInfoValidator.getCreatedTime()));

        //op 不为 edit 或 add
        if (!("edit".equals(op) || "add".equals(op))) {
            return ResponseDataUtil.buildError();
        }

        //记录日志
        log.info("[企业接入][企业WEB登录账号][{}]数据:{}", op, JSON.toJSONString(entity));
        enterpriseWebRepository.saveAndFlush(entity);

        //生成用户数据（包含默认授权）
        if ("add".equals(op)) {
            saveUser(entity);
        }

        return ResponseDataUtil.buildSuccess();
    }

    private void saveUser(EnterpriseWebAccountInfo entity) {

        //查询企业
        Optional<EnterpriseBasicInfo> data = enterpriseRepository.findById(entity.getEnterpriseId());
        String corporation = "";
        String salerCode = "0";
        if (data.isPresent()) {
            corporation = data.get().getEnterpriseName();
            salerCode = data.get().getSaler();
        }

        //创建用户信息
        UserValidator user = new UserValidator();

        //设置角色
        user.setRoleIds("7da61ba9be8f4f6581c20f5a6d449b85");

        BaseUserValidator baseUser = new BaseUserValidator();
        String userId = UUID.uuid32();
        baseUser.setId(userId);
        baseUser.setUserName(entity.getWebLoginName());
        baseUser.setPassword(entity.getWebLoginPassword());
        baseUser.setPhone(entity.getWebLoginName());
        baseUser.setOrganization(entity.getEnterpriseId());
        baseUser.setCreateDate(DateTimeUtils.getNowDateTime());
        baseUser.setUpdateDate(DateTimeUtils.getNowDateTime());
        baseUser.setActive(1);
        user.setBaseUserValidator(baseUser);

        BaseUserExtendsValidator baseUserExtendsValidator = new BaseUserExtendsValidator();
        baseUserExtendsValidator.setId(userId);
        baseUserExtendsValidator.setRealName(entity.getWebLoginName());
        baseUserExtendsValidator.setCorporation(corporation);
        baseUserExtendsValidator.setCode("0");
        baseUserExtendsValidator.setParentCode(salerCode);
        baseUserExtendsValidator.setAdministrator(1);
        baseUserExtendsValidator.setWebChat("smoc-service");
        baseUserExtendsValidator.setType(4);
        user.setBaseUserExtendsValidator(baseUserExtendsValidator);

        //新建人员信息
        ResponseData responseUser = authorityService.saveUser(user, "add");
    }



}
