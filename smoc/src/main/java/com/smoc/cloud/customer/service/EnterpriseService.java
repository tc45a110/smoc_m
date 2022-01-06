package com.smoc.cloud.customer.service;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBasicInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.customer.entity.EnterpriseBasicInfo;
import com.smoc.cloud.customer.repository.EnterpriseRepository;
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
 * 企业接入管理
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class EnterpriseService {

    @Resource
    private EnterpriseRepository enterpriseRepository;

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    public PageList<EnterpriseBasicInfoValidator> page(PageParams<EnterpriseBasicInfoValidator> pageParams) {
        return enterpriseRepository.page(pageParams);
    }

    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    public ResponseData findById(String id) {
        Optional<EnterpriseBasicInfo> data = enterpriseRepository.findById(id);

        if(!data.isPresent()){
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }

        EnterpriseBasicInfo entity = data.get();
        EnterpriseBasicInfoValidator enterpriseBasicInfoValidator = new EnterpriseBasicInfoValidator();
        BeanUtils.copyProperties(entity, enterpriseBasicInfoValidator);

        //转换日期
        enterpriseBasicInfoValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(entity.getCreatedTime()));

        return ResponseDataUtil.buildSuccess(enterpriseBasicInfoValidator);
    }

    /**
     * 保存或修改
     *
     * @param enterpriseBasicInfoValidator
     * @param op     操作类型 为add、edit
     * @return
     */
    @Transactional
    public ResponseData<EnterpriseBasicInfo> save(EnterpriseBasicInfoValidator enterpriseBasicInfoValidator, String op) {

        Iterable<EnterpriseBasicInfo> data = enterpriseRepository.findByEnterpriseNameAndEnterpriseParentId(enterpriseBasicInfoValidator.getEnterpriseName(),enterpriseBasicInfoValidator.getEnterpriseParentId());

        EnterpriseBasicInfo entity = new EnterpriseBasicInfo();
        BeanUtils.copyProperties(enterpriseBasicInfoValidator, entity);

        //add查重
        if (data != null && data.iterator().hasNext() && "add".equals(op)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
        }
        //edit查重
        else if (data != null && data.iterator().hasNext() && "edit".equals(op)) {
            boolean status = false;
            Iterator iter = data.iterator();
            while (iter.hasNext()) {
                EnterpriseBasicInfo info = (EnterpriseBasicInfo) iter.next();
                if (!entity.getEnterpriseId().equals(info.getEnterpriseId())) {
                    status = true;
                    break;
                }
            }
            if (status) {
                return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
            }
        }

        //转换日期格式
        entity.setCreatedTime(DateTimeUtils.getDateTimeFormat(enterpriseBasicInfoValidator.getCreatedTime()));

        //op 不为 edit 或 add
        if (!("edit".equals(op) || "add".equals(op))) {
            return ResponseDataUtil.buildError();
        }

        //记录日志
        log.info("[企业接入][企业开户信息][{}]数据:{}",op,JSON.toJSONString(entity));
        enterpriseRepository.saveAndFlush(entity);
        return ResponseDataUtil.buildSuccess();
    }



}
