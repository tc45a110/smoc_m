package com.smoc.cloud.configure.channel.controller;

import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigRepairRuleValidator;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.configure.channel.service.ConfigRepairRuleService;
import com.smoc.cloud.sign.service.SignRegisterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;


/**
 * 失败补发规则接口
 **/
@Slf4j
@RestController
@RequestMapping("configure/repair/rule")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class ConfigRepairRuleController {

    @Autowired
    private SignRegisterService signRegisterService;

    @Autowired
    private ConfigRepairRuleService configRepairRuleService;

    /**
     * 根据id获取信息
     *
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

        ResponseData data = configRepairRuleService.findById(id);
        return data;
    }

    /**
     * 保存补发规则
     *
     * @param configRepairRuleValidator
     * @param op
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody ConfigRepairRuleValidator configRepairRuleValidator, @PathVariable String op) {

        //保存操作
        ResponseData data = configRepairRuleService.save(configRepairRuleValidator, op);
        //生成签名报备
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            signRegisterService.generateSignRegisterByAccount(configRepairRuleValidator.getBusinessId());
        }

        return data;
    }

    /**
     * 根据业务ID查询
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/findByBusinessId/{id}", method = RequestMethod.GET)
    public ResponseData findByBusinessId(@PathVariable String id) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(validator));
        }

        ResponseData data = configRepairRuleService.findByBusinessId(id);
        return data;
    }
}
