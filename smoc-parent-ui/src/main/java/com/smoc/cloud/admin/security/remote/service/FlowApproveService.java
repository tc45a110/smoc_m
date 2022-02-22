package com.smoc.cloud.admin.security.remote.service;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.security.remote.client.FlowApproveFeignClient;
import com.smoc.cloud.common.auth.validator.FlowApproveValidator;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.utils.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 字典管理服务
 * 2019/4/23 16:28
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class FlowApproveService {

    @Autowired
    private FlowApproveFeignClient flowApproveFeignClient;


    /**
     * 根据id获取数据
     */
    public ResponseData<FlowApproveValidator> findById(String id) {
        try {
            ResponseData<FlowApproveValidator> data = this.flowApproveFeignClient.findById(id);
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
    public ResponseData save(FlowApproveValidator flowValidator, String op) {
        try {
            flowValidator.setId(UUID.uuid32());
            flowValidator.setSubmitTime(new Date());
            flowValidator.setApproveStatus(0);//审核中
            flowValidator.setFlowStatus(0);//流程进行中

            log.info("[业务审核][add][{}]数据::{}", JSON.toJSONString(flowValidator));

            ResponseData data = this.flowApproveFeignClient.save(flowValidator, op);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }


    /**
     * 根据id删除数据
     */
    public ResponseData deleteById(String id) {
        try {
            ResponseData data = this.flowApproveFeignClient.deleteById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 审核记录
     * @param flowApproveValidator
     * @return
     */
    public ResponseData<List<FlowApproveValidator>> checkRecord(FlowApproveValidator flowApproveValidator) {
        try {
            List<FlowApproveValidator> pageList = flowApproveFeignClient.checkRecord(flowApproveValidator);
            return ResponseDataUtil.buildSuccess(pageList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 审核记录
     * @param approveId
     * @return
     */
    public ResponseData<List<FlowApproveValidator>> checkRecord(String approveId) {
        try {
            List<FlowApproveValidator> pageList = flowApproveFeignClient.checkRecord(approveId);
            return ResponseDataUtil.buildSuccess(pageList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    public ResponseData saveFlowApprove(FlowApproveValidator flowValidator, String op) {
        try {
            flowValidator.setId(UUID.uuid32());
            flowValidator.setSubmitTime(new Date());

            log.info("[业务审核][add][{}]数据::{}", JSON.toJSONString(flowValidator));

            ResponseData data = this.flowApproveFeignClient.save(flowValidator, op);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
