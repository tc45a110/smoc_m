package com.smoc.cloud.customer.controller;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.qo.AccountContentRepairQo;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.ConfigContentRepairRuleValidator;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.customer.service.ConfigContentRepairService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;


/**
 * 账号内容失败补发接口
 **/
@Slf4j
@RestController
@RequestMapping("configure/content/repair")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class ConfigContentRepairController {

    @Autowired
    private ConfigContentRepairService configContentRepairService;

    /**
     * 查询通道失败补发列表
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public PageList<ConfigContentRepairRuleValidator> page(@RequestBody PageParams<ConfigContentRepairRuleValidator> pageParams) {

        return configContentRepairService.page(pageParams);
    }

    /**
     * 业务账号列表
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/accountList", method = RequestMethod.POST)
    public PageList<AccountContentRepairQo> accountList(@RequestBody PageParams<AccountContentRepairQo> pageParams) {

        return configContentRepairService.accountList(pageParams);
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

        ResponseData data = configContentRepairService.findById(id);
        return data;
    }

    /**
     * 添加、修改
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody ConfigContentRepairRuleValidator configContentRepairRuleValidator, @PathVariable String op) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(configContentRepairRuleValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(configContentRepairRuleValidator));
        }

        //保存操作
        ResponseData data = configContentRepairService.save(configContentRepairRuleValidator, op);

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

        return configContentRepairService.deleteById(id);
    }
}
