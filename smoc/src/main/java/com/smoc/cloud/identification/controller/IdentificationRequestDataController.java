package com.smoc.cloud.identification.controller;

import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.identification.validator.IdentificationRequestDataValidator;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.identification.service.IdentificationRequestDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

/**
 * 认证请求原数据
 */
@Slf4j
@RestController
@RequestMapping("identification/data")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class IdentificationRequestDataController {

    @Autowired
    private IdentificationRequestDataService identificationRequestDataService;


    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public ResponseData<IdentificationRequestDataValidator> findById(@PathVariable String id) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(validator));
        }

        ResponseData data = identificationRequestDataService.findById(id);
        return data;
    }

    /**
     * 添加、修改
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public void save(@RequestBody IdentificationRequestDataValidator identificationRequestDataValidator) {

        identificationRequestDataService.save(identificationRequestDataValidator);

    }
}
