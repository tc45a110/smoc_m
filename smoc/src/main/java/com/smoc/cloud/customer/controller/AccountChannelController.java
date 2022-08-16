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
import com.smoc.cloud.sign.service.SignRegisterService;
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
@RequestMapping("account")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class AccountChannelController {

    @Autowired
    private SignRegisterService signRegisterService;

    @Autowired
    private AccountChannelService accountChannelService;

    /**
     * 查询账号的配置通道
     * @param accountChannelInfoQo
     * @return
     */
    @RequestMapping(value = "/channel/findAccountChannelConfig", method = RequestMethod.POST)
    public ResponseData<Map<String, AccountChannelInfoQo>> findAccountChannelConfig(@RequestBody AccountChannelInfoQo accountChannelInfoQo) {

        return accountChannelService.findAccountChannelConfig(accountChannelInfoQo);
    }

    /**
     * 检索通道列表
     * @param channelBasicInfoQo
     * @return
     */
    @RequestMapping(value = "/channel/findChannelList", method = RequestMethod.POST)
    public ResponseData<List<ChannelBasicInfoQo>> findChannelList(@RequestBody ChannelBasicInfoQo channelBasicInfoQo) {

        return accountChannelService.findChannelList(channelBasicInfoQo);
    }

    /**
     * 添加、修改
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    @RequestMapping(value = "/channel/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody AccountChannelInfoValidator accountChannelInfoValidator, @PathVariable String op) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(accountChannelInfoValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(accountChannelInfoValidator));
        }

        //保存操作
        ResponseData data = accountChannelService.save(accountChannelInfoValidator, op);

        //生成签名报备
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            signRegisterService.generateSignRegisterByAccount(accountChannelInfoValidator.getAccountId());
        }
        return data;
    }

    /**
     * 查询账号下运营商是否配置过通道
     * @return
     */
    @RequestMapping(value = "/channel/findByAccountIdAndCarrier/{accountId}/{carrier}", method = RequestMethod.GET)
    public ResponseData findByAccountIdAndCarrier(@PathVariable String accountId,@PathVariable String carrier) {

        return accountChannelService.findByAccountIdAndCarrier(accountId,carrier);
    }

    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/channel/findById/{id}", method = RequestMethod.GET)
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
    @RequestMapping(value = "/channel/deleteById/{id}", method = RequestMethod.GET)
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
    @RequestMapping(value = "/channelGroup/findChannelGroupList", method = RequestMethod.POST)
    public ResponseData<List<ChannelGroupInfoValidator>> findChannelGroupList(@RequestBody ChannelGroupInfoValidator channelGroupInfoValidator) {

        return accountChannelService.findChannelGroupList(channelGroupInfoValidator);
    }

    /**
     * 查询已配置通道组数据
     * @param accountId
     * @param carrier
     * @param channelGroupId
     * @return
     */
    @RequestMapping(value = "/channelGroup/findByAccountIdAndCarrierAndChannelGroupId/{accountId}/{carrier}/{channelGroupId}", method = RequestMethod.GET)
    public ResponseData findByAccountIdAndCarrierAndChannelGroupId(@PathVariable String accountId,@PathVariable String carrier,@PathVariable String channelGroupId) {

        return accountChannelService.findByAccountIdAndCarrierAndChannelGroupId(accountId,carrier,channelGroupId);
    }

    /**
     * 移除已配置通道组
     * @param accountId
     * @param carrier
     * @param channelGroupId
     * @return
     */
    @RequestMapping(value = "/channelGroup/deleteChannelGroup/{accountId}/{carrier}/{channelGroupId}", method = RequestMethod.GET)
    public ResponseData deleteById(@PathVariable String accountId,@PathVariable String carrier,@PathVariable String channelGroupId) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(accountId);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(validator));
        }

        ResponseData data = accountChannelService.deleteChannelGroup(accountId,carrier,channelGroupId);
        return data;
    }

    /**
     * 业务账号通道明细
     * @param accountChannelInfoValidator
     * @return
     */
    @RequestMapping(value = "/channel/channelDetail", method = RequestMethod.POST)
    public ResponseData<List<AccountChannelInfoValidator>> channelDetail(@RequestBody AccountChannelInfoValidator accountChannelInfoValidator) {

        return accountChannelService.channelDetail(accountChannelInfoValidator);
    }

    /**
     * 通过channelId 查询 该通道的业务账号引用情况
     * @param channelId
     * @return
     */
    @RequestMapping(value = "/channel/channelDetail/{channelId}", method = RequestMethod.POST)
    public ResponseData<List<AccountChannelInfoValidator>> channelDetail(@PathVariable String channelId) {

        return accountChannelService.channelDetail(channelId);
    }

}
