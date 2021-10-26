package com.smoc.cloud.auth.data.provider.service;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.auth.data.provider.entity.BaseFlowApprove;
import com.smoc.cloud.auth.data.provider.repository.BaseFlowApproveRepository;
import com.smoc.cloud.common.auth.validator.FlowApproveValidator;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * 流程审核服务
 * 2019/5/21 16:32
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BaseFlowApproveService {

    @Resource
    private BaseFlowApproveRepository baseFlowApproveRepository;


    /**
     * 根据id 查询
     *
     * @param id
     * @return
     */
    public ResponseData<BaseFlowApprove> findById(String id) {

        Optional<BaseFlowApprove> data = baseFlowApproveRepository.findById(id);

        //使用了 Optional 的判断方式，判断优雅
        return ResponseDataUtil.buildSuccess(data.orElse(null));
    }

    /**
     * 保存或修改
     *
     * @param entity
     * @param op     操作类型 为add、edit
     * @return
     */
    @Transactional
    public ResponseData<BaseFlowApprove> save(BaseFlowApprove entity, String op) {

        //记录日志
        log.info("[业务审核管理][{}]数据:{}",op,JSON.toJSONString(entity));
        baseFlowApproveRepository.saveAndFlush(entity);
        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 根据id 删除
     *
     * @param id
     * @return
     */
    @Transactional
    public ResponseData<BaseFlowApprove> deleteById(String id) {

        BaseFlowApprove data = baseFlowApproveRepository.findById(id).get();

        //记录日志
        log.info("[业务审核管理][delete]数据:{}",JSON.toJSONString(data));
        baseFlowApproveRepository.deleteById(id);
        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 根据id判断对象是否存在
     *
     * @param id
     * @return
     */
    public boolean exists(String id) {
        return baseFlowApproveRepository.existsById(id);
    }

    /**
     * 审核记录
     * @param flowApproveValidator
     * @return
     */
    public List<FlowApproveValidator> checkRecord(FlowApproveValidator flowApproveValidator) {
        return baseFlowApproveRepository.checkRecord(flowApproveValidator);
    }
}
