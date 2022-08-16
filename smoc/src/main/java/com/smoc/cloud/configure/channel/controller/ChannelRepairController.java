package com.smoc.cloud.configure.channel.controller;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelBasicInfoValidator;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelRepairRuleValidator;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelRepairValidator;
import com.smoc.cloud.common.smoc.customer.qo.AccountChannelRepairQo;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.configure.channel.service.ChannelRepairService;
import com.smoc.cloud.sign.service.SignRegisterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;


/**
 * 通道失败补发接口
 **/
@Slf4j
@RestController
@RequestMapping("configure/channel/repair")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class ChannelRepairController {

    @Autowired
    private SignRegisterService signRegisterService;

    @Autowired
    private ChannelRepairService channelRepairService;

    /**
     * 查询通道失败补发列表
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public PageList<ConfigChannelRepairValidator> page(@RequestBody PageParams<ConfigChannelRepairValidator> pageParams) {

        return channelRepairService.page(pageParams);
    }

    /**
     * 查询账号失败补发列表
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/accountChannelRepairPage", method = RequestMethod.POST)
    public PageList<AccountChannelRepairQo> accountChannelRepairPage(@RequestBody PageParams<AccountChannelRepairQo> pageParams) {

        return channelRepairService.accountChannelRepairPage(pageParams);
    }

    /**
     * 根据运营商、业务类型查询符合要求的备用通道
     * @param configChannelRepairValidator
     * @return
     */
    @RequestMapping(value = "/findSpareChannel", method = RequestMethod.POST)
    public ResponseData<List<ConfigChannelRepairValidator>> findSpareChannel(@RequestBody ConfigChannelRepairValidator configChannelRepairValidator) {

        return channelRepairService.findSpareChannel(configChannelRepairValidator);
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

        ResponseData data = channelRepairService.findById(id);
        return data;
    }

    /**
     * 保存补发通道
     * @param configChannelRepairRuleValidator
     * @param op
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody ConfigChannelRepairRuleValidator configChannelRepairRuleValidator, @PathVariable String op){

        //保存操作
        ResponseData data = channelRepairService.save(configChannelRepairRuleValidator, op);

        //生成签名报备
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            signRegisterService.generateSignRegisterByAccount(configChannelRepairRuleValidator.getBusinessId());
        }

        return data;
    }

    /**
     * 根据ID 删除
     *
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

        return channelRepairService.deleteById(id);
    }

    /**
     * 查询已经存在的备用通道
     * @param configChannelRepairRuleValidator
     * @return
     */
    @RequestMapping(value = "/findChannelRepairByChannelId", method = RequestMethod.POST)
    public ResponseData<List<ConfigChannelRepairRuleValidator>> findChannelRepairByChannelId(@RequestBody ConfigChannelRepairRuleValidator configChannelRepairRuleValidator) {

        return channelRepairService.findChannelRepairByChannelId(configChannelRepairRuleValidator);
    }

}
