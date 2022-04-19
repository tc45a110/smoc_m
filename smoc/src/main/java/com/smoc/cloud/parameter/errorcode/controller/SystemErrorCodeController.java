package com.smoc.cloud.parameter.errorcode.controller;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.parameter.SystemErrorCodeValidator;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.parameter.errorcode.service.SystemErrorCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;


/**
 * 错误码管理接口
 * 2019/4/16 20:26
 **/
@Slf4j
@RestController
@RequestMapping("/errorcode")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class SystemErrorCodeController {

    @Autowired
    private SystemErrorCodeService systemErrorCodeService;

    /**
     * 根据群id查询通讯录
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public PageList<SystemErrorCodeValidator> page(@RequestBody PageParams<SystemErrorCodeValidator> pageParams) {

        return systemErrorCodeService.page(pageParams);
    }

    /**
     * 根据ID 查询
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
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR);
        }

        return systemErrorCodeService.findById(id);
    }

    /**
     * 添加、修改
     *
     * @param systemErrorCodeValidator
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody SystemErrorCodeValidator systemErrorCodeValidator, @PathVariable String op) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(systemErrorCodeValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(systemErrorCodeValidator));
        }

        return systemErrorCodeService.save(systemErrorCodeValidator, op);
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

        return systemErrorCodeService.deleteById(id);
    }

    /**
     * 批量保存
     *
     * @param systemErrorCodeValidator
     * @return
     */
    @RequestMapping(value = "/bathSave", method = RequestMethod.POST)
    public void bathSave(@RequestBody SystemErrorCodeValidator systemErrorCodeValidator) {

        systemErrorCodeService.bathSave(systemErrorCodeValidator);
    }

}
