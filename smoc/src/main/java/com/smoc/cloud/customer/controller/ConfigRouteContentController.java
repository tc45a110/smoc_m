package com.smoc.cloud.customer.controller;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.qo.AccountContentRepairQo;
import com.smoc.cloud.common.smoc.customer.validator.ConfigRouteContentRuleValidator;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.customer.service.ConfigRouteContentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;


/**
 * 账号内容路由接口
 **/
@Slf4j
@RestController
@RequestMapping("configure/content/repair")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class ConfigRouteContentController {

    @Autowired
    private ConfigRouteContentService configRouteContentService;

    /**
     * 查询通道失败补发列表
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public PageList<ConfigRouteContentRuleValidator> page(@RequestBody PageParams<ConfigRouteContentRuleValidator> pageParams) {

        return configRouteContentService.page(pageParams);
    }

    /**
     * 业务账号列表
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/accountList", method = RequestMethod.POST)
    public PageList<AccountContentRepairQo> accountList(@RequestBody PageParams<AccountContentRepairQo> pageParams) {

        return configRouteContentService.accountList(pageParams);
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

        ResponseData data = configRouteContentService.findById(id);
        return data;
    }

    /**
     * 添加、修改
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody ConfigRouteContentRuleValidator configContentRepairRuleValidator, @PathVariable String op) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(configContentRepairRuleValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(configContentRepairRuleValidator));
        }

        //保存操作
        ResponseData data = configRouteContentService.save(configContentRepairRuleValidator, op);

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

        return configRouteContentService.deleteById(id);
    }

    @RequestMapping(value = "/findContentRepair/{accountId}/{carrier}", method = RequestMethod.GET)
    public ResponseData findById(@PathVariable String accountId,@PathVariable String carrier) {

        ResponseData data = configRouteContentService.findContentRepair(accountId,carrier);
        return data;
    }
}
