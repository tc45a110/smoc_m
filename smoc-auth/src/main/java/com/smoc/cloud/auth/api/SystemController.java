package com.smoc.cloud.auth.api;

import com.smoc.cloud.auth.data.provider.entity.BaseSystem;
import com.smoc.cloud.auth.data.provider.service.BaseSystemService;
import com.smoc.cloud.common.auth.validator.SystemValidator;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

/**
 * 系统接口
 * 2019/4/16 20:26
 **/
@Slf4j
@RestController
@RequestMapping("system")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class SystemController {

    @Autowired
    private BaseSystemService baseSystemService;

    /**
     * 获取所有系统
     *
     * @param
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseData list() {
        return baseSystemService.list();
    }

    /**
     * 根据ID 查询
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public ResponseData findById(@PathVariable String id, @RequestHeader("Authorization") String Authorization) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR);
        }

        return baseSystemService.findById(id);
    }

    /**
     * 保存系统
     *
     * @param systemValidator
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody SystemValidator systemValidator, @PathVariable String op) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(systemValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(systemValidator));
        }

        //转BaseUser存放对象
        BaseSystem system = new BaseSystem();
        BeanUtils.copyProperties(systemValidator, system);
        return baseSystemService.save(system, op);
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

        return baseSystemService.deleteById(id);
    }


}
