package com.smoc.cloud.message.remote;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.message.MessageAccountValidator;
import com.smoc.cloud.common.smoc.message.MessageHttpsTaskInfoValidator;
import com.smoc.cloud.common.smoc.message.model.MessageTaskDetail;
import com.smoc.cloud.common.smoc.message.model.StatisticMessageSend;
import com.smoc.cloud.common.smoc.message.MessageWebTaskInfoValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


/**
 * 短信群发远程服务接口
 **/
@FeignClient(name = "smoc", path = "/smoc")
public interface MessageFeignClient {

    /**
     * 查询web列表
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/message/web/task/page", method = RequestMethod.POST)
    ResponseData<PageList<MessageWebTaskInfoValidator>> page(@RequestBody PageParams<MessageWebTaskInfoValidator> pageParams) throws Exception;

    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/message/web/task/findById/{id}", method = RequestMethod.GET)
    ResponseData<MessageWebTaskInfoValidator> findById(@PathVariable String id) throws Exception;

    /**
     * 保存、修改数据
     * op 是类型 表示了保存或修改
     */
    @RequestMapping(value = "/message/web/task/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody MessageWebTaskInfoValidator messageWebTaskInfoValidator, @PathVariable String op) throws Exception;

    /**
     * 根据id删除系统数据
     */
    @RequestMapping(value = "/message/web/task/deleteById/{id}", method = RequestMethod.GET)
    ResponseData deleteById(@PathVariable String id) throws Exception;

    /**
     * 短信发送
     * @param id
     * @return
     */
    @RequestMapping(value = "/message/web/task/sendMessageById/{id}", method = RequestMethod.GET)
    ResponseData sendMessageById(@PathVariable String id) throws Exception;

    /**
     * 查询企业发送量
     * @param messageAccountValidator
     * @return
     */
    @RequestMapping(value = "/message/web/task/statisticMessageSendCount", method = RequestMethod.POST)
    ResponseData<StatisticMessageSend> statisticMessageSendCount(@RequestBody MessageAccountValidator messageAccountValidator) throws Exception;

    /**
     * 统计短信提交发送量
     * @param messageWebTaskInfoValidator
     * @return
     */
    @RequestMapping(value = "/message/web/task/statisticSubmitMessageSendCount", method = RequestMethod.POST)
    ResponseData<StatisticMessageSend> statisticSubmitMessageSendCount(@RequestBody MessageWebTaskInfoValidator messageWebTaskInfoValidator);

    /**
     * 查询web短信明细列表
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/message/detail/web/webTaskDetailList", method = RequestMethod.POST)
    ResponseData<PageList<MessageTaskDetail>> webTaskDetailList(@RequestBody PageParams<MessageTaskDetail> pageParams);

    /**
     * 查询http短信明细列表
     * @param params
     * @return
     */
    @RequestMapping(value = "/message/detail/http/httpTaskDetailList", method = RequestMethod.POST)
    ResponseData<PageList<MessageTaskDetail>> httpTaskDetailList(@RequestBody PageParams<MessageTaskDetail> params);

    /**
     * 查询http列表
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/message/https/task/page", method = RequestMethod.POST)
    ResponseData<PageList<MessageHttpsTaskInfoValidator>> httpPage(@RequestBody PageParams<MessageHttpsTaskInfoValidator> pageParams) throws Exception;

    /**
     * 查询http任务
     * @param id
     * @return
     */
    @RequestMapping(value = "/message/https/task/findById/{id}", method = RequestMethod.GET)
    ResponseData<MessageHttpsTaskInfoValidator> findHttpTaskById(@PathVariable String id);
}
