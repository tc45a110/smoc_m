package com.smoc.cloud.message.remote;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.message.MessageCodeValidator;
import com.smoc.cloud.common.smoc.message.MessageDetailInfoValidator;
import com.smoc.cloud.common.smoc.message.TableStoreMessageDetailInfoValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * 短信明细
 */
@FeignClient(name = "smoc", path = "/smoc")
public interface MessageDetailInfoFeignClient {

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/message/detail/page", method = RequestMethod.POST)
    ResponseData<PageList<MessageDetailInfoValidator>> page(@RequestBody PageParams<MessageDetailInfoValidator> pageParams) throws Exception;

    @RequestMapping(value = "/message/detail/tableStorePage", method = RequestMethod.POST)
    ResponseData<PageList<TableStoreMessageDetailInfoValidator>> tableStorePage(@RequestBody PageParams<TableStoreMessageDetailInfoValidator> params);

    /**
     * 根据企业实时查询发送总量
     * @param messageDetailInfoValidator
     * @return
     */
    @RequestMapping(value = "/message/detail/statisticEnterpriseSendMessage", method = RequestMethod.POST)
    ResponseData<Map<String, Object>> statisticEnterpriseSendMessage(@RequestBody MessageDetailInfoValidator messageDetailInfoValidator);

    /**
     * 消息状态码统计查询
     * @param params
     * @return
     */
    @RequestMapping(value = "/message/detail/code/messageCcodeStautsList", method = RequestMethod.POST)
    ResponseData<PageList<MessageCodeValidator>> messageCcodeStautsList(@RequestBody PageParams<MessageCodeValidator> params);

    /**
     * 通道消息明细查询
     * @param params
     * @return
     */
    @RequestMapping(value = "/message/detail/channel/messageChannelPage", method = RequestMethod.POST)
    ResponseData<PageList<MessageDetailInfoValidator>> messageChannelPage(@RequestBody PageParams<MessageDetailInfoValidator> params);

    /**
     * 统计提交给通道发送总量
     * @param messageDetailInfoValidator
     * @return
     */
    @RequestMapping(value = "/message/detail/channel/statisticChannelSendMessage", method = RequestMethod.POST)
    ResponseData<Map<String, Object>> statisticChannelSendMessage(@RequestBody MessageDetailInfoValidator messageDetailInfoValidator);
}
