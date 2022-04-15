package com.smoc.cloud.configure.channel.controller;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelAccountInfoQo;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelBasicInfoQo;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelInterfaceInfoQo;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelBasicInfoValidator;
import com.smoc.cloud.common.smoc.customer.qo.AccountStatisticComplaintData;
import com.smoc.cloud.common.smoc.customer.qo.AccountStatisticSendData;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.configure.channel.service.ChannelInterfaceService;
import com.smoc.cloud.configure.channel.service.ChannelPriceService;
import com.smoc.cloud.configure.channel.service.ChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;


/**
 * 通道接口
 **/
@Slf4j
@RestController
@RequestMapping("configure/channel")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class ChannelController {

    @Autowired
    private ChannelService channelService;

    @Autowired
    private ChannelInterfaceService channelInterfaceService;

    @Autowired
    private ChannelPriceService channelPriceService;

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public PageList<ChannelBasicInfoQo> page(@RequestBody PageParams<ChannelBasicInfoQo> pageParams) {

        return channelService.page(pageParams);
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

        ResponseData data = channelService.findById(id);
        return data;
    }

    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/findChannelById/{id}", method = RequestMethod.GET)
    public ResponseData findChannelById(@PathVariable String id) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(validator));
        }

        ResponseData data = channelService.findChannelById(id);
        return data;
    }

    /**
     * 添加、修改
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody ChannelBasicInfoValidator channelBasicInfoValidator, @PathVariable String op) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(channelBasicInfoValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(channelBasicInfoValidator));
        }

        //保存操作
        ResponseData data = channelService.save(channelBasicInfoValidator, op);

        return data;
    }

    /**
     * 通道按维度统计发送量
     * @param statisticSendData
     * @return
     */
    @RequestMapping(value = "/statisticChannelSendNumber", method = RequestMethod.POST)
    public ResponseData<List<AccountStatisticSendData>> statisticChannelSendNumber(@RequestBody AccountStatisticSendData statisticSendData) {

        ResponseData<List<AccountStatisticSendData>> data = channelService.statisticChannelSendNumber(statisticSendData);
        return data;
    }

    /**
     *  通道投诉率统计
     * @param statisticComplaintData
     * @return
     */
    @RequestMapping(value = "/statisticComplaintMonth", method = RequestMethod.POST)
    public ResponseData<List<AccountStatisticComplaintData>> statisticComplaintMonth(@RequestBody AccountStatisticComplaintData statisticComplaintData) {

        ResponseData<List<AccountStatisticComplaintData>> data = channelService.statisticComplaintMonth(statisticComplaintData);
        return data;
    }

    /**
     * 通道账号使用明细
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/channelAccountList", method = RequestMethod.POST)
    public ResponseData<PageList<ChannelAccountInfoQo>> channelAccountList(@RequestBody PageParams<ChannelAccountInfoQo> pageParams){

        return channelService.channelAccountList(pageParams);
    }

    /**
     * 通道接口参数查询
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/channelInterfacePage", method = RequestMethod.POST)
    public ResponseData<PageList<ChannelInterfaceInfoQo>> channelInterfacePage(@RequestBody PageParams<ChannelInterfaceInfoQo> pageParams){

        return channelService.channelInterfacePage(pageParams);
    }
}
