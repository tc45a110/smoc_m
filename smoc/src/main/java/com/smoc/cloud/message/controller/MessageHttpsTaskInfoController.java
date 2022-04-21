package com.smoc.cloud.message.controller;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.message.MessageHttpsTaskInfoValidator;
import com.smoc.cloud.common.smoc.message.model.StatisticMessageSendData;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.message.service.MessageHttpTaskInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * http任务单
 */
@Slf4j
@RestController
@RequestMapping("message/https/task")
public class MessageHttpsTaskInfoController {

    @Autowired
    private MessageHttpTaskInfoService messageHttpTaskInfoService;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ResponseData<PageList<MessageHttpsTaskInfoValidator>> page(@RequestBody PageParams<MessageHttpsTaskInfoValidator> pageParams) {

        return messageHttpTaskInfoService.page(pageParams);
    }


    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public ResponseData findById(@PathVariable String id) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(validator));
        }

        ResponseData data = messageHttpTaskInfoService.findById(id);
        return data;
    }

    /**
     * 自服务平台不同维度统计发送量
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/messageSendNumberList", method = RequestMethod.POST)
    public ResponseData<PageList<StatisticMessageSendData>> messageSendNumberList(@RequestBody PageParams<StatisticMessageSendData> pageParams) {

        return messageHttpTaskInfoService.messageSendNumberList(pageParams);
    }
}
