package com.smoc.cloud.configure.number.controller;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.configuate.validator.SystemSegmentProvinceCityValidator;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.configure.number.service.SegmentProvinceCityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

/**
 *  省号码接口
 **/
@Slf4j
@RestController
@RequestMapping("configure/segment")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class SegmentProvinceCityController {

    @Autowired
    private SegmentProvinceCityService segmentProvinceCityService;

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public PageList<SystemSegmentProvinceCityValidator> page(@RequestBody PageParams<SystemSegmentProvinceCityValidator> pageParams) {

        return segmentProvinceCityService.page(pageParams);
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

        ResponseData data = segmentProvinceCityService.findById(id);
        return data;
    }

    /**
     * 添加、修改
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody SystemSegmentProvinceCityValidator systemSegmentProvinceCityValidator, @PathVariable String op) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(systemSegmentProvinceCityValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(systemSegmentProvinceCityValidator));
        }

        //保存操作
        ResponseData data = segmentProvinceCityService.save(systemSegmentProvinceCityValidator, op);

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

        return segmentProvinceCityService.deleteById(id);
    }

    /**
     * 批量保存
     *
     * @param systemSegmentProvinceCityValidator
     * @return
     */
    @RequestMapping(value = "/batchSave", method = RequestMethod.POST)
    public void batchSave(@RequestBody SystemSegmentProvinceCityValidator systemSegmentProvinceCityValidator) {

        segmentProvinceCityService.batchSave(systemSegmentProvinceCityValidator);
    }
}
