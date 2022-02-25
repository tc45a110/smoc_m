package com.smoc.cloud.customer.service;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseChainInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.customer.entity.EnterpriseChainInfo;
import com.smoc.cloud.customer.repository.EnterpriseChainRepository;
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
 * 签名合同链管理
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class EnterpriseChainService {

    @Resource
    private EnterpriseChainRepository enterpriseChainRepository;

    /**
     * 查询列表
     *
     * @param enterpriseChainInfoValidator
     * @return
     */
    public ResponseData<List<EnterpriseChainInfoValidator>> page(EnterpriseChainInfoValidator enterpriseChainInfoValidator) {
        List<EnterpriseChainInfoValidator> list = enterpriseChainRepository.page(enterpriseChainInfoValidator);
        return ResponseDataUtil.buildSuccess(list);
    }

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    public ResponseData findById(String id) {
        Optional<EnterpriseChainInfo> data = enterpriseChainRepository.findById(id);

        if (!data.isPresent()) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }

        EnterpriseChainInfo entity = data.get();
        EnterpriseChainInfoValidator enterpriseChainInfoValidator = new EnterpriseChainInfoValidator();
        BeanUtils.copyProperties(entity, enterpriseChainInfoValidator);

        //转换日期
        enterpriseChainInfoValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(entity.getCreatedTime()));

        return ResponseDataUtil.buildSuccess(enterpriseChainInfoValidator);
    }

    /**
     * 保存或修改
     *
     * @param enterpriseChainInfoValidator
     * @param op  操作类型 为add、edit
     * @return
     */
    @Transactional
    public ResponseData<EnterpriseChainInfo> save(EnterpriseChainInfoValidator enterpriseChainInfoValidator, String op) {

        List<EnterpriseChainInfo> data = enterpriseChainRepository.findByIdAndSignChainStatus(enterpriseChainInfoValidator.getId(),"1");

        EnterpriseChainInfo entity = new EnterpriseChainInfo();
        BeanUtils.copyProperties(enterpriseChainInfoValidator, entity);

        //add查重
        if (data != null && data.iterator().hasNext() && "add".equals(op)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
        }
        //edit查重
        else if (data != null && data.iterator().hasNext() && "edit".equals(op)) {
            boolean status = false;
            Iterator iter = data.iterator();
            while (iter.hasNext()) {
                EnterpriseChainInfo info = (EnterpriseChainInfo) iter.next();
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
        entity.setCreatedTime(DateTimeUtils.getDateTimeFormat(enterpriseChainInfoValidator.getCreatedTime()));

        //op 不为 edit 或 add
        if (!("edit".equals(op) || "add".equals(op))) {
            return ResponseDataUtil.buildError();
        }

        //记录日志
        log.info("[签名合同链管理][{}]数据:{}", op, JSON.toJSONString(entity));
        enterpriseChainRepository.saveAndFlush(entity);


        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @Transactional
    public ResponseData<EnterpriseChainInfo> deleteById(String id) {

        EnterpriseChainInfo data = enterpriseChainRepository.findById(id).get();
        //记录日志
        log.info("[签名合同链管理][delete]数据:{}",JSON.toJSONString(data));
        enterpriseChainRepository.updateStatusById(id);


        return ResponseDataUtil.buildSuccess();
    }
}
