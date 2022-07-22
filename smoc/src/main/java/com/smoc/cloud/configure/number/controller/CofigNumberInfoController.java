package com.smoc.cloud.configure.number.controller;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigNumberInfoValidator;
import com.smoc.cloud.common.smoc.filter.FilterBlackListValidator;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.configure.number.service.ConfigNumberInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

/**
 * 携号转网接口
 **/
@Slf4j
@RestController
@RequestMapping("configure/number")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class CofigNumberInfoController {

    @Autowired
    private ConfigNumberInfoService configNumberInfoService;

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public PageList<ConfigNumberInfoValidator> page(@RequestBody PageParams<ConfigNumberInfoValidator> pageParams) {

        return configNumberInfoService.page(pageParams);
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

        ResponseData data = configNumberInfoService.findById(id);
        return data;
    }

    /**
     * 添加、修改
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody ConfigNumberInfoValidator configNumberInfoValidator, @PathVariable String op) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(configNumberInfoValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(configNumberInfoValidator));
        }

        //保存操作
        ResponseData data = configNumberInfoService.save(configNumberInfoValidator, op);

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

        return configNumberInfoService.deleteById(id);
    }

    /**
     * 批量保存
     *
     * @param configNumberInfoValidator
     * @return
     */
    @RequestMapping(value = "/batchSave", method = RequestMethod.POST)
    public void batchSave(@RequestBody ConfigNumberInfoValidator configNumberInfoValidator) {

        configNumberInfoService.batchSave(configNumberInfoValidator);
    }

    /**
     * 查询携号转网数据是否在redis库
     * @param numberCode
     * @return
     */
    @RequestMapping(value = "/findRedis/{numberCode}", method = RequestMethod.GET)
    public ResponseData<ConfigNumberInfoValidator> findRedis(@PathVariable String numberCode) {

        ResponseData<ConfigNumberInfoValidator> data = configNumberInfoService.findRedis(numberCode);
        return data;
    }
    /**
     * 根据ID 删除
     *
     * @param numberCode
     * @return
     */
    @RequestMapping(value = "/deleteRedis/{numberCode}", method = RequestMethod.GET)
    public ResponseData deleteRedis(@PathVariable String numberCode) {

        return configNumberInfoService.deleteRedis(numberCode);
    }

}
