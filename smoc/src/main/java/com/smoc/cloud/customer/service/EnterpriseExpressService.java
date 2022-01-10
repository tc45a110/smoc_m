package com.smoc.cloud.customer.service;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseExpressInfoValidator;
import com.smoc.cloud.customer.entity.EnterpriseBasicInfo;
import com.smoc.cloud.customer.entity.EnterpriseExpressInfo;
import com.smoc.cloud.customer.repository.EnterpriseExpressRepository;
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
import java.util.List;
import java.util.Optional;

/**
 * 企业邮寄信息管理
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class EnterpriseExpressService {

    @Resource
    private EnterpriseExpressRepository enterpriseExpressRepository;

    @Resource
    private EnterpriseRepository enterpriseRepository;

    /**
     * 查询列表
     *
     * @param enterpriseExpressInfoValidator
     * @return
     */
    public ResponseData<List<EnterpriseExpressInfoValidator>> page(EnterpriseExpressInfoValidator enterpriseExpressInfoValidator) {

        List<EnterpriseExpressInfoValidator> list = enterpriseExpressRepository.page(enterpriseExpressInfoValidator);
        return ResponseDataUtil.buildSuccess(list);
    }

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    public ResponseData findById(String id) {
        Optional<EnterpriseExpressInfo> data = enterpriseExpressRepository.findById(id);

        //使用了 Optional 的判断方式，判断优雅
        return ResponseDataUtil.buildSuccess(data.orElse(null));
    }

    /**
     * 保存或修改
     *
     * @param enterpriseExpressInfoValidator
     * @param op  操作类型 为add、edit
     * @return
     */
    @Transactional
    public ResponseData<EnterpriseExpressInfo> save(EnterpriseExpressInfoValidator enterpriseExpressInfoValidator, String op) {

        Iterable<EnterpriseExpressInfo> data = enterpriseExpressRepository.findByEnterpriseIdAndPostContactsAndPostPhoneAndPostAddress(enterpriseExpressInfoValidator.getEnterpriseId(), enterpriseExpressInfoValidator.getPostContacts(), enterpriseExpressInfoValidator.getPostPhone(), enterpriseExpressInfoValidator.getPostAddress());

        EnterpriseExpressInfo entity = new EnterpriseExpressInfo();
        BeanUtils.copyProperties(enterpriseExpressInfoValidator, entity);

        //add查重
        if (data != null && data.iterator().hasNext() && "add".equals(op)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
        }
        //edit查重
        else if (data != null && data.iterator().hasNext() && "edit".equals(op)) {
            boolean status = false;
            Iterator iter = data.iterator();
            while (iter.hasNext()) {
                EnterpriseExpressInfo info = (EnterpriseExpressInfo) iter.next();
                if (!entity.getId().equals(info.getId())) {
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

        if ("edit".equals(op)) {
            EnterpriseExpressInfo info = enterpriseExpressRepository.findById(entity.getId()).get();
            entity.setCreatedBy(info.getCreatedBy());
            entity.setCreatedTime(info.getCreatedTime());
            entity.setPostStatus(info.getPostStatus());
        }

        //记录日志
        log.info("[企业接入][企业邮寄信息][{}]数据:{}", op, JSON.toJSONString(entity));
        enterpriseExpressRepository.saveAndFlush(entity);

        if ("add".equals(op)) {
            //更新进度
            Optional<EnterpriseBasicInfo> optional = enterpriseRepository.findById(entity.getEnterpriseId());
            if (optional.isPresent()) {
                EnterpriseBasicInfo enterpriseBasicInfo = optional.get();
                StringBuffer process = new StringBuffer(enterpriseBasicInfo.getEnterpriseProcess());
                process = process.replace(2, 3, "1");
                enterpriseBasicInfo.setEnterpriseProcess(process.toString());
                enterpriseRepository.save(enterpriseBasicInfo);
            }
        }

        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 根据ID 删除数据
     *
     * @param id
     * @return
     */
    @Transactional
    public ResponseData<EnterpriseExpressInfo> deleteById(String id) {

        EnterpriseExpressInfo data = enterpriseExpressRepository.findById(id).get();
        //记录日志
        log.info("[企业接入][企业邮寄信息][delete]数据:{}", JSON.toJSONString(data));
        enterpriseExpressRepository.deleteById(id);
        return ResponseDataUtil.buildSuccess();
    }


}
