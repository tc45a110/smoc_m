package com.smoc.cloud.auth.api;

import com.smoc.cloud.auth.data.provider.entity.SystemExtendBusinessParam;
import com.smoc.cloud.auth.data.provider.service.SystemExtendBusinessParamService;
import com.smoc.cloud.common.auth.validator.SystemExtendBusinessParamValidator;
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

@Slf4j
@RestController
@RequestMapping("param")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class SystemExtendBusinessParamController {

    @Autowired
    private SystemExtendBusinessParamService systemExtendBusinessParamService;

    /**
     * 按业务类型查询系统扩展参数
     *
     * @param businessType
     * @return
     */
    @RequestMapping(value = "/list/{businessType}", method = RequestMethod.GET)
    public ResponseData list(@PathVariable String businessType) {
        return systemExtendBusinessParamService.findSystemExtendBusinessParamByBusinessTypeAndParamStatus(businessType, "1");
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

        return systemExtendBusinessParamService.findById(id);
    }

    /**
     * 保存
     *
     * @param systemExtendBusinessParamValidator
     * @param op                                 保存类型 指的是add 或 edit
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody SystemExtendBusinessParamValidator systemExtendBusinessParamValidator, @PathVariable String op) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(systemExtendBusinessParamValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(systemExtendBusinessParamValidator));
        }

        //转SystemExtendBusinessParam存放对象
        SystemExtendBusinessParam systemExtendBusinessParam = new SystemExtendBusinessParam();
        BeanUtils.copyProperties(systemExtendBusinessParamValidator, systemExtendBusinessParam);
        return systemExtendBusinessParamService.save(systemExtendBusinessParam, op);
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

        return systemExtendBusinessParamService.deleteById(id);
    }
}
