package com.smoc.cloud.customer.service;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.customer.entity.EnterpriseInvoiceInfo;
import com.smoc.cloud.customer.entity.SystemAttachmentInfo;
import com.smoc.cloud.customer.repository.SystemAttachmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * 附件管理
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SystemAttachmentService {

    @Resource
    private SystemAttachmentRepository systemAttachmentRepository;


    /**
     *根据业务id查询附件
     *
     * @param id
     * @return
     */
    public ResponseData findByMoudleId(String id) {

        List<SystemAttachmentInfo> list = systemAttachmentRepository.findByMoudleIdAndAttachmentStatus(id,"1");

        return ResponseDataUtil.buildSuccess(list);
    }

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    public ResponseData findById(String id) {
        Optional<SystemAttachmentInfo> data = systemAttachmentRepository.findById(id);

        //使用了 Optional 的判断方式，判断优雅
        return ResponseDataUtil.buildSuccess(data.orElse(null));
    }

    /**
     * 根据ID 删除数据
     *
     * @param id
     * @return
     */
    @Transactional
    public ResponseData<EnterpriseInvoiceInfo> deleteById(String id) {

        SystemAttachmentInfo data = systemAttachmentRepository.findById(id).get();
        //记录日志
        log.info("[附件管理][delete]数据:{}",JSON.toJSONString(data));
        systemAttachmentRepository.deleteById(id);

        return ResponseDataUtil.buildSuccess();
    }
}
