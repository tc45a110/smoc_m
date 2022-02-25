package com.smoc.cloud.customer.service;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseContractInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.SystemAttachmentValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.customer.entity.EnterpriseContractInfo;
import com.smoc.cloud.customer.entity.SystemAttachmentInfo;
import com.smoc.cloud.customer.repository.EnterpriseContractRepository;
import com.smoc.cloud.customer.repository.SystemAttachmentRepository;
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
 * EC合同管理管理
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class EnterpriseContractService {

    @Resource
    private EnterpriseContractRepository enterpriseContractRepository;

    @Resource
    private SystemAttachmentRepository systemAttachmentRepository;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    public PageList<EnterpriseContractInfoValidator> page(PageParams<EnterpriseContractInfoValidator> pageParams) {
        return enterpriseContractRepository.page(pageParams);
    }

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    public ResponseData findById(String id) {
        Optional<EnterpriseContractInfo> data = enterpriseContractRepository.findById(id);

        if (!data.isPresent()) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }

        EnterpriseContractInfo entity = data.get();
        EnterpriseContractInfoValidator enterpriseContractInfoValidator = new EnterpriseContractInfoValidator();
        BeanUtils.copyProperties(entity, enterpriseContractInfoValidator);

        //转换日期
        enterpriseContractInfoValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(entity.getCreatedTime()));

        return ResponseDataUtil.buildSuccess(enterpriseContractInfoValidator);
    }

    /**
     * 保存或修改
     *
     * @param enterpriseContractInfoValidator
     * @param op  操作类型 为add、edit
     * @return
     */
    @Transactional
    public ResponseData<EnterpriseContractInfo> save(EnterpriseContractInfoValidator enterpriseContractInfoValidator, String op) {

        List<EnterpriseContractInfo> data = enterpriseContractRepository.findByEnterpriseIdAndContractNoAndContractStatus(enterpriseContractInfoValidator.getEnterpriseId(),enterpriseContractInfoValidator.getContractNo(),"1");

        EnterpriseContractInfo entity = new EnterpriseContractInfo();
        BeanUtils.copyProperties(enterpriseContractInfoValidator, entity);

        //add查重
        if (data != null && data.iterator().hasNext() && "add".equals(op)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
        }
        //edit查重
        else if (data != null && data.iterator().hasNext() && "edit".equals(op)) {
            boolean status = false;
            Iterator iter = data.iterator();
            while (iter.hasNext()) {
                EnterpriseContractInfo info = (EnterpriseContractInfo) iter.next();
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
        entity.setCreatedTime(DateTimeUtils.getDateTimeFormat(enterpriseContractInfoValidator.getCreatedTime()));

        //op 不为 edit 或 add
        if (!("edit".equals(op) || "add".equals(op))) {
            return ResponseDataUtil.buildError();
        }

        //记录日志
        log.info("[EC合同管理][{}]数据:{}", op, JSON.toJSONString(entity));
        enterpriseContractRepository.saveAndFlush(entity);

        //保存附件
        List<SystemAttachmentValidator> filesList = enterpriseContractInfoValidator.getFilesList();
        if(!StringUtils.isEmpty(filesList) && filesList.size()>0){
            systemAttachmentRepository.batchSave(filesList);
        }

        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @Transactional
    public ResponseData<EnterpriseContractInfo> deleteById(String id) {

        EnterpriseContractInfo data = enterpriseContractRepository.findById(id).get();
        //记录日志
        log.info("[EC合同管理][delete]数据:{}",JSON.toJSONString(data));
        enterpriseContractRepository.updateStatusById(id);

        //查询有没有附件，有：置为无效
        List<SystemAttachmentInfo> list = systemAttachmentRepository.findByMoudleIdAndAttachmentStatus(data.getId(),"1");
        if(!StringUtils.isEmpty(list) && list.size()>0){
            systemAttachmentRepository.updateAttachmentStatusByMoudleId(data.getId());
        }


        return ResponseDataUtil.buildSuccess();
    }
}
