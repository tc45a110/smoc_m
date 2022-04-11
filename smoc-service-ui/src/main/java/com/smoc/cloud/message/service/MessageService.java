package com.smoc.cloud.message.service;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.message.MessageAccountValidator;
import com.smoc.cloud.common.smoc.message.model.MessageTaskDetail;
import com.smoc.cloud.common.smoc.message.model.StatisticMessageSend;
import com.smoc.cloud.common.smoc.message.MessageWebTaskInfoValidator;
import com.smoc.cloud.message.remote.MessageFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MessageService {

    @Autowired
    private MessageFeignClient messageFeignClient;


    /**
     * 查询WEB列表
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<MessageWebTaskInfoValidator>> page(PageParams<MessageWebTaskInfoValidator> pageParams) {
        try {
            ResponseData<PageList<MessageWebTaskInfoValidator>> pageList = this.messageFeignClient.page(pageParams);
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
    public ResponseData<MessageWebTaskInfoValidator> findById(String id) {
        try {
            ResponseData<MessageWebTaskInfoValidator> data = this.messageFeignClient.findById(id);
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
    public ResponseData save(MessageWebTaskInfoValidator messageWebTaskInfoValidator, String op) {
        try {

            ResponseData data = this.messageFeignClient.save(messageWebTaskInfoValidator, op);
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
            ResponseData data = this.messageFeignClient.deleteById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 短信发送
     * @param id
     * @return
     */
    public ResponseData sendMessageById(String id) {
        try {
            ResponseData data = this.messageFeignClient.sendMessageById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 查询企业发送量
     * @param messageAccountValidator
     * @return
     */
    public ResponseData<StatisticMessageSend> statisticMessageSendCount(MessageAccountValidator messageAccountValidator) {
        try {
            ResponseData<StatisticMessageSend> data = this.messageFeignClient.statisticMessageSendCount(messageAccountValidator);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 统计短信提交发送量
     * @param messageWebTaskInfoValidator
     * @return
     */
    public ResponseData<StatisticMessageSend> statisticSubmitMessageSendCount(MessageWebTaskInfoValidator messageWebTaskInfoValidator) {
        try {
            ResponseData<StatisticMessageSend> data = this.messageFeignClient.statisticSubmitMessageSendCount(messageWebTaskInfoValidator);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 查询web短信明细列表
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<MessageTaskDetail>> webTaskDetailList(PageParams<MessageTaskDetail> pageParams) {
        try {
            ResponseData<PageList<MessageTaskDetail>> pageList = this.messageFeignClient.webTaskDetailList(pageParams);
            return pageList;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 查询http列表
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<MessageWebTaskInfoValidator>> httpPage(PageParams pageParams) {
        try {
            ResponseData<PageList<MessageWebTaskInfoValidator>> pageList = this.messageFeignClient.httpPage(pageParams);
            return pageList;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 查询http短信明细列表
     * @param params
     * @return
     */
    public ResponseData<PageList<MessageTaskDetail>> httpTaskDetailList(PageParams<MessageTaskDetail> params) {
        try {
            ResponseData<PageList<MessageTaskDetail>> pageList = this.messageFeignClient.httpTaskDetailList(params);
            return pageList;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
