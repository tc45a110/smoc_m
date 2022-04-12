package com.smoc.cloud.filter.controller;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.filter.FilterSignFrequencyLimitValidator;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.filter.service.FilterSignFrequencyLimitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

@Slf4j
@RestController
@RequestMapping("/filter/limit")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class FilterSignFrequencyLimitController {

    @Autowired
    private FilterSignFrequencyLimitService filterSignFrequencyLimitService;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ResponseData<PageList<FilterSignFrequencyLimitValidator>> page(@RequestBody PageParams<FilterSignFrequencyLimitValidator> pageParams) {

        return filterSignFrequencyLimitService.page(pageParams);
    }

    /**
     * 根据ID 查询
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public ResponseData<FilterSignFrequencyLimitValidator> findById(@PathVariable String id) {


        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR);
        }

        return filterSignFrequencyLimitService.findById(id);
    }

    /**
     * 添加、修改
     *
     * @param filterSignFrequencyLimitValidator
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody FilterSignFrequencyLimitValidator filterSignFrequencyLimitValidator, @PathVariable String op) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(filterSignFrequencyLimitValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(filterSignFrequencyLimitValidator));
        }

        return filterSignFrequencyLimitService.save(filterSignFrequencyLimitValidator, op);
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

        return filterSignFrequencyLimitService.deleteById(id);
    }

}
