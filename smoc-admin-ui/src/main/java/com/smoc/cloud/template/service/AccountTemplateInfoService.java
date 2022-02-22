package com.smoc.cloud.template.service;

import com.smoc.cloud.admin.security.remote.service.FlowApproveService;
import com.smoc.cloud.common.auth.validator.FlowApproveValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.template.AccountTemplateInfoValidator;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.template.remote.AccountTemplateInfoFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


/**
 * 模板管理
 */
@Slf4j
@Service
public class AccountTemplateInfoService {

    @Autowired
    private FlowApproveService flowApproveService;

    @Autowired
    private AccountTemplateInfoFeignClient accountTemplateInfoFeignClient;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<AccountTemplateInfoValidator>> page(PageParams<AccountTemplateInfoValidator> pageParams) {

        try {

            ResponseData<PageList<AccountTemplateInfoValidator>> page = accountTemplateInfoFeignClient.page(pageParams);
            return page;
        } catch (Exception e) {
            return ResponseDataUtil.buildError(e.getMessage());
        }

    }

    /**
     * 注销
     *
     * @param id
     * @return
     */
    public ResponseData cancelTemplate(String id,String templateStatus) {
        try {
            ResponseData data = this.accountTemplateInfoFeignClient.cancelTemplate(id,templateStatus);
            return data;
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
            ResponseData<AccountTemplateInfoValidator> data = this.accountTemplateInfoFeignClient.findById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 保存、修改数据
     * op 是类型 表示了保存或修改
     *
     */
    public ResponseData save(AccountTemplateInfoValidator accountTemplateInfoValidator, String op,String userId) {

        try {
            ResponseData data = this.accountTemplateInfoFeignClient.save(accountTemplateInfoValidator, op);

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
                flowApproveValidator.setApproveStatus(0);
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
}
