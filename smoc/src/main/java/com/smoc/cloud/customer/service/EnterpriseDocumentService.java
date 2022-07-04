package com.smoc.cloud.customer.service;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseDocumentInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.SystemAttachmentValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.customer.entity.EnterpriseContractInfo;
import com.smoc.cloud.customer.entity.EnterpriseDocumentInfo;
import com.smoc.cloud.customer.entity.SystemAttachmentInfo;
import com.smoc.cloud.customer.repository.EnterpriseDocumentRepository;
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
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * 签名资质管理
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class EnterpriseDocumentService {

    @Resource
    private EnterpriseDocumentRepository enterpriseDocumentRepository;

    @Resource
    private SystemAttachmentRepository systemAttachmentRepository;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    public PageList<EnterpriseDocumentInfoValidator> page(PageParams<EnterpriseDocumentInfoValidator> pageParams) {
        return enterpriseDocumentRepository.page(pageParams);
    }

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    public ResponseData findById(String id) {
        Optional<EnterpriseDocumentInfo> data = enterpriseDocumentRepository.findById(id);

        if (!data.isPresent()) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }

        EnterpriseDocumentInfo entity = data.get();
        EnterpriseDocumentInfoValidator enterpriseDocumentInfoValidator = new EnterpriseDocumentInfoValidator();
        BeanUtils.copyProperties(entity, enterpriseDocumentInfoValidator);

        //转换日期
        enterpriseDocumentInfoValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(entity.getCreatedTime()));

        return ResponseDataUtil.buildSuccess(enterpriseDocumentInfoValidator);
    }

    /**
     * 保存或修改
     *
     * @param enterpriseDocumentInfoValidator
     * @param op  操作类型 为add、edit
     * @return
     */
    @Transactional
    public ResponseData<EnterpriseDocumentInfo> save(EnterpriseDocumentInfoValidator enterpriseDocumentInfoValidator, String op) {

        List<EnterpriseDocumentInfo> data = enterpriseDocumentRepository.findByEnterpriseIdAndSignNameAndBusinessTypeAndDocStatus(enterpriseDocumentInfoValidator.getEnterpriseId(),enterpriseDocumentInfoValidator.getSignName(),enterpriseDocumentInfoValidator.getBusinessType(),"1");

        EnterpriseDocumentInfo entity = new EnterpriseDocumentInfo();
        BeanUtils.copyProperties(enterpriseDocumentInfoValidator, entity);

        //add查重
        if (data != null && data.iterator().hasNext() && "add".equals(op)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
        }
        //edit查重
        else if (data != null && data.iterator().hasNext() && "edit".equals(op)) {
            boolean status = false;
            Iterator iter = data.iterator();
            while (iter.hasNext()) {
                EnterpriseDocumentInfo info = (EnterpriseDocumentInfo) iter.next();
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
        entity.setCreatedTime(DateTimeUtils.getDateTimeFormat(enterpriseDocumentInfoValidator.getCreatedTime()));

        //op 不为 edit 或 add
        if (!("edit".equals(op) || "add".equals(op))) {
            return ResponseDataUtil.buildError();
        }

        //记录日志
        log.info("[签名资质管理][{}]数据:{}", op, JSON.toJSONString(entity));
        enterpriseDocumentRepository.saveAndFlush(entity);

        //保存附件
        List<SystemAttachmentValidator> filesList = enterpriseDocumentInfoValidator.getFilesList();
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
    public ResponseData<EnterpriseDocumentInfo> deleteById(String id) {

        EnterpriseDocumentInfo data = enterpriseDocumentRepository.findById(id).get();
        //记录日志
        log.info("[签名资质管理][delete]数据:{}",JSON.toJSONString(data));
        enterpriseDocumentRepository.updateStatusById(id,"0","");

        //查询有没有附件，有：置为无效
        List<SystemAttachmentInfo> list = systemAttachmentRepository.findByMoudleIdAndAttachmentStatus(data.getId(),"1");
        if(!StringUtils.isEmpty(list) && list.size()>0){
            systemAttachmentRepository.updateAttachmentStatusByMoudleId(data.getId());
        }


        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 审核
     * @param enterpriseDocumentInfoValidator
     * @return
     */
    @Transactional
    public ResponseData check(EnterpriseDocumentInfoValidator enterpriseDocumentInfoValidator) {

        //记录日志
        log.info("[签名资质审核][check]数据:{}",JSON.toJSONString(enterpriseDocumentInfoValidator));

        if("0".equals(enterpriseDocumentInfoValidator.getCheckStatus())){
            enterpriseDocumentInfoValidator.setDocStatus("3");
        }else{
            enterpriseDocumentInfoValidator.setDocStatus(enterpriseDocumentInfoValidator.getCheckStatus());
        }
        String checkDate = DateTimeUtils.getDateTimeFormat(new Date());
        enterpriseDocumentRepository.updateStatusById(enterpriseDocumentInfoValidator.getId(),enterpriseDocumentInfoValidator.getDocStatus(),checkDate);

        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 查询签名
     * @param enterpriseDocumentInfoValidator
     * @return
     */
    public ResponseData<List<EnterpriseDocumentInfoValidator>> findMessageSign(EnterpriseDocumentInfoValidator enterpriseDocumentInfoValidator) {
        List<EnterpriseDocumentInfoValidator> list = enterpriseDocumentRepository.findMessageSign(enterpriseDocumentInfoValidator);
        return ResponseDataUtil.buildSuccess(list);
    }
}
