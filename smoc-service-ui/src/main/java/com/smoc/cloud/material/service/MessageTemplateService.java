package com.smoc.cloud.material.service;

import com.smoc.cloud.admin.security.remote.service.FlowApproveService;
import com.smoc.cloud.common.auth.validator.FlowApproveValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.template.AccountTemplateInfoValidator;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.material.remote.MessageTemplateFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.util.Date;


/**
 * 模板管理服务
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MessageTemplateService {

    @Autowired
    private MessageTemplateFeignClient messageTemplateFeignClient;

    @Autowired
    private FlowApproveService flowApproveService;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<AccountTemplateInfoValidator>> page(PageParams<AccountTemplateInfoValidator> pageParams) {
        try {
            ResponseData<PageList<AccountTemplateInfoValidator>> pageList = this.messageTemplateFeignClient.page(pageParams);
            return pageList;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    public ResponseData<AccountTemplateInfoValidator> findById(String id) {
        try {
            ResponseData<AccountTemplateInfoValidator> data = this.messageTemplateFeignClient.findById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 保存、修改数据
     * op 是类型 表示了保存或修改
     */
    public ResponseData save(AccountTemplateInfoValidator accountTemplateInfoValidator, String op,String userId) {

        try {

            ResponseData data = this.messageTemplateFeignClient.save(accountTemplateInfoValidator, op);

            //组织审核信息
            if("add".equals(op)) {
                FlowApproveValidator flowApproveValidator = new FlowApproveValidator();
                flowApproveValidator.setId(UUID.uuid32());
                flowApproveValidator.setApproveId(accountTemplateInfoValidator.getTemplateId());
                flowApproveValidator.setApproveType("TEMPLATE_INFO");
                flowApproveValidator.setSubmitTime(new Date());
                flowApproveValidator.setBusiUrl(accountTemplateInfoValidator.getCreatedBy());
                flowApproveValidator.setUserId(userId);
                flowApproveValidator.setApproveAdvice("新建");
                flowApproveValidator.setApproveStatus(4);
                flowApproveValidator.setFlowStatus(0);
                flowApproveValidator.setUserApproveId("");
                flowApproveService.saveFlowApprove(flowApproveValidator,"add");
            }
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据id删除系统数据
     */
    public ResponseData deleteById(String id) {
        try {
            ResponseData data = this.messageTemplateFeignClient.deleteById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
