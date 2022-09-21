package com.smoc.cloud.complaint.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.message.MessageComplaintInfoValidator;
import com.smoc.cloud.common.smoc.message.MessageDetailInfoValidator;
import com.smoc.cloud.complaint.remote.ComplaintFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 投诉管理服务
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ComplaintService {

    @Autowired
    private ComplaintFeignClient complaintFeignClient;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<MessageComplaintInfoValidator>> page(PageParams<MessageComplaintInfoValidator> pageParams) {
        try {
            PageList<MessageComplaintInfoValidator> pageList = this.complaintFeignClient.page(pageParams);
            return ResponseDataUtil.buildSuccess(pageList);
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
    public ResponseData<MessageComplaintInfoValidator> findById(String id) {
        try {
            ResponseData<MessageComplaintInfoValidator> data = this.complaintFeignClient.findById(id);
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
    public ResponseData save(MessageComplaintInfoValidator messageComplaintInfoValidator, String op) {

        try {

            ResponseData data = this.complaintFeignClient.save(messageComplaintInfoValidator, op);
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
            ResponseData data = this.complaintFeignClient.deleteById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 批量导入投诉
     * @param messageComplaintInfoValidator
     * @return
     */
    public ResponseData batchSave(MessageComplaintInfoValidator messageComplaintInfoValidator) {
        try {
            ResponseData data = this.complaintFeignClient.batchSave(messageComplaintInfoValidator);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据投诉手机号查询10天内的下发记录
     * @param detail
     * @return
     */
    public ResponseData<List<MessageDetailInfoValidator>> sendMessageList(MessageDetailInfoValidator detail) {
        try {
            ResponseData<List<MessageDetailInfoValidator>> data = this.complaintFeignClient.sendMessageList(detail);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
