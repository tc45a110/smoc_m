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
import com.smoc.cloud.common.smoc.configuate.validator.ChannelPriceValidator;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelRepairRuleValidator;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelRepairValidator;
import com.smoc.cloud.common.smoc.customer.qo.AccountStatisticComplaintData;
import com.smoc.cloud.common.smoc.customer.qo.AccountStatisticSendData;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.configure.channel.service.ChannelInterfaceService;
import com.smoc.cloud.configure.channel.service.ChannelPriceService;
import com.smoc.cloud.configure.channel.service.ChannelRepairService;
import com.smoc.cloud.configure.channel.service.ChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Map;


/**
 * 通道失败补发接口
 **/
@Slf4j
@RestController
@RequestMapping("configure/channel/repair")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class ChannelRepairController {

    @Autowired
    private ChannelRepairService channelRepairService;

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public PageList<ConfigChannelRepairValidator> page(@RequestBody PageParams<ConfigChannelRepairValidator> pageParams) {

        return channelRepairService.page(pageParams);
    }

    /**
     * 根据运营商、业务类型、信息分类查询符合要求的备用通道
     * @param channelBasicInfoValidator
     * @return
     */
    @RequestMapping(value = "/findSpareChannel", method = RequestMethod.POST)
    public ResponseData<List<ConfigChannelRepairValidator>> findSpareChannel(@RequestBody ChannelBasicInfoValidator channelBasicInfoValidator) {

        return channelRepairService.findSpareChannel(channelBasicInfoValidator);
    }

    /**
     * 初始化备用通道
     * @param channelId
     * @return
     */
    @RequestMapping(value = "/editRepairRule/{channelId}", method = RequestMethod.GET)
    public ResponseData<Map<String, ConfigChannelRepairRuleValidator>> editRepairRule(@PathVariable String channelId) {

        return channelRepairService.editRepairRule(channelId);
    }

    /**
     * 保存补发通道
     * @param configChannelRepairValidator
     * @param op
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody ConfigChannelRepairValidator configChannelRepairValidator, @PathVariable String op){

        //保存操作
        ResponseData data = channelRepairService.batchSave(configChannelRepairValidator, op);

        return data;
    }
}
