package com.smoc.cloud.customer.service;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseInvoiceInfoValidator;
import com.smoc.cloud.customer.entity.EnterpriseBasicInfo;
import com.smoc.cloud.customer.entity.EnterpriseInvoiceInfo;
import com.smoc.cloud.customer.repository.EnterpriseInvoiceRepository;
import com.smoc.cloud.customer.repository.EnterpriseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * 企业发票信息管理
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class EnterpriseInvoiceService {

    @Resource
    private EnterpriseInvoiceRepository enterpriseInvoiceRepository;

    @Resource
    private EnterpriseRepository enterpriseRepository;

    /**
     * 查询列表
     *
     * @param enterpriseInvoiceInfoValidator
     * @return
     */
    public ResponseData<List<EnterpriseInvoiceInfoValidator>> page(EnterpriseInvoiceInfoValidator enterpriseInvoiceInfoValidator) {

        List<EnterpriseInvoiceInfoValidator> list = enterpriseInvoiceRepository.page(enterpriseInvoiceInfoValidator);
        return ResponseDataUtil.buildSuccess(list);
    }

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    public ResponseData findById(String id) {
        Optional<EnterpriseInvoiceInfo> data = enterpriseInvoiceRepository.findById(id);

        //使用了 Optional 的判断方式，判断优雅
        return ResponseDataUtil.buildSuccess(data.orElse(null));
    }

    /**
     * 保存或修改
     *
     * @param enterpriseInvoiceInfoValidator
     * @param op  操作类型 为add、edit
     * @return
     */
    @Transactional
    public ResponseData<EnterpriseInvoiceInfo> save(EnterpriseInvoiceInfoValidator enterpriseInvoiceInfoValidator, String op) {

        Iterable<EnterpriseInvoiceInfo> data = enterpriseInvoiceRepository.findByEnterpriseIdAndInvoiceTypeAndInvoiceTitle(enterpriseInvoiceInfoValidator.getEnterpriseId(),enterpriseInvoiceInfoValidator.getInvoiceType(),enterpriseInvoiceInfoValidator.getInvoiceTitle());

        EnterpriseInvoiceInfo entity = new EnterpriseInvoiceInfo();
        BeanUtils.copyProperties(enterpriseInvoiceInfoValidator, entity);

        //add查重
        if (data != null && data.iterator().hasNext() && "add".equals(op)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
        }
        //edit查重
        else if (data != null && data.iterator().hasNext() && "edit".equals(op)) {
            boolean status = false;
            Iterator iter = data.iterator();
            while (iter.hasNext()) {
                EnterpriseInvoiceInfo info = (EnterpriseInvoiceInfo) iter.next();
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
            EnterpriseInvoiceInfo info = enterpriseInvoiceRepository.findById(entity.getId()).get();
            entity.setCreatedBy(info.getCreatedBy());
            entity.setCreatedTime(info.getCreatedTime());
            entity.setInvoiceStatus(info.getInvoiceStatus());
        }

        if ("add".equals(op)) {
            //更新进度
            Optional<EnterpriseBasicInfo> optional = enterpriseRepository.findById(entity.getEnterpriseId());
            if (optional.isPresent()) {
                EnterpriseBasicInfo enterpriseBasicInfo = optional.get();
                StringBuffer process = new StringBuffer(enterpriseBasicInfo.getEnterpriseProcess());
                process = process.replace(3, 4, "1");
                enterpriseBasicInfo.setEnterpriseProcess(process.toString());
                enterpriseRepository.save(enterpriseBasicInfo);
            }
        }

        //记录日志
        log.info("[企业接入][企业发票信息][{}]数据:{}",op, JSON.toJSONString(entity));
        enterpriseInvoiceRepository.saveAndFlush(entity);

        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 根据ID 删除数据
     *
     * @param id
     * @return
     */
    @Transactional
    public ResponseData<EnterpriseInvoiceInfo> deleteById(String id) {

        EnterpriseInvoiceInfo data = enterpriseInvoiceRepository.findById(id).get();
        //记录日志
        log.info("[企业接入][企业发票信息][delete]数据:{}",JSON.toJSONString(data));
        enterpriseInvoiceRepository.deleteById(id);

        List<EnterpriseInvoiceInfo> list = enterpriseInvoiceRepository.findByEnterpriseId(data.getEnterpriseId());
        if(StringUtils.isEmpty(list) || list.size()<=0){
            //更新进度
            Optional<EnterpriseBasicInfo> optional = enterpriseRepository.findById(data.getEnterpriseId());
            if (optional.isPresent()) {
                EnterpriseBasicInfo enterpriseBasicInfo = optional.get();
                StringBuffer process = new StringBuffer(enterpriseBasicInfo.getEnterpriseProcess());
                process = process.replace(3, 4, "0");
                enterpriseBasicInfo.setEnterpriseProcess(process.toString());
                enterpriseRepository.save(enterpriseBasicInfo);
            }
        }

        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 根据类型和企业id查询数据
     * @param enterpriseId
     * @param type
     * @return
     */
    public ResponseData findByEnterpriseIdAndInvoiceType(String enterpriseId, String type) {

        EnterpriseInvoiceInfo data = enterpriseInvoiceRepository.findByEnterpriseIdAndInvoiceType(enterpriseId,type);

        //使用了 Optional 的判断方式，判断优雅
        return ResponseDataUtil.buildSuccess(data);
    }
}
