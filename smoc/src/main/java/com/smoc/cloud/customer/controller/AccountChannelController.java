package com.smoc.cloud.customer.controller;

import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelBasicInfoQo;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelGroupInfoValidator;
import com.smoc.cloud.common.smoc.customer.qo.AccountChannelInfoQo;
import com.smoc.cloud.common.smoc.customer.validator.AccountChannelInfoValidator;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.customer.service.AccountChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Map;

/**
 * 账号通道配置接口信息
 **/
@Slf4j
@RestController
@RequestMapping("account/channel")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class AccountChannelController {

    @Autowired
    private AccountChannelService accountChannelService;

    /**
     * 查询账号的配置通道
     * @param accountChannelInfoQo
     * @return
     */
    @RequestMapping(value = "/findAccountChannelConfig", method = RequestMethod.POST)
    public ResponseData<Map<String, AccountChannelInfoQo>> findAccountChannelConfig(@RequestBody AccountChannelInfoQo accountChannelInfoQo) {

        return accountChannelService.findAccountChannelConfig(accountChannelInfoQo);
    }

    /**
     * 检索通道列表
     * @param channelBasicInfoQo
     * @return
     */
    @RequestMapping(value = "/findChannelList", method = RequestMethod.POST)
    public ResponseData<List<ChannelBasicInfoQo>> findChannelList(@RequestBody ChannelBasicInfoQo channelBasicInfoQo) {

        return accountChannelService.findChannelList(channelBasicInfoQo);
    }

    /**
     * 添加、修改
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody AccountChannelInfoValidator accountChannelInfoValidator, @PathVariable String op) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(accountChannelInfoValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(accountChannelInfoValidator));
        }

        //保存操作
        ResponseData data = accountChannelService.save(accountChannelInfoValidator, op);

        return data;
    }

    /**
     * 查询账号下运营商是否配置过通道
     * @return
     */
    @RequestMapping(value = "/findByAccountIdAndCarrier/{accountId}/{carrier}", method = RequestMethod.GET)
    public ResponseData findByAccountIdAndCarrier(@PathVariable String accountId,@PathVariable String carrier) {

        return accountChannelService.findByAccountIdAndCarrier(accountId,carrier);
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

        ResponseData data = accountChannelService.findById(id);
        return data;
    }

    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/deleteById/{id}", method = RequestMethod.GET)
    public ResponseData deleteById(@PathVariable String id) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(validator));
        }

        ResponseData data = accountChannelService.deleteById(id);
        return data;
    }

    /**
     * 检索通道组列表
     * @param channelGroupInfoValidator
     * @return
     */
    @RequestMapping(value = "/findChannelGroupList", method = RequestMethod.POST)
    public ResponseData<List<ChannelGroupInfoValidator>> findChannelGroupList(@RequestBody ChannelGroupInfoValidator channelGroupInfoValidator) {

        return accountChannelService.findChannelGroupList(channelGroupInfoValidator);
    }
}
