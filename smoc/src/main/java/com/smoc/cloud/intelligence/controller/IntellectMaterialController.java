package com.smoc.cloud.intelligence.controller;

import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.intelligence.IntellectMaterialValidator;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.intelligence.entity.IntellectMaterial;
import com.smoc.cloud.intelligence.service.IntellectMaterialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

/**
 * 智能短信素材
 */
@Slf4j
@RestController
@RequestMapping("intel/material")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class IntellectMaterialController {

    @Autowired
    private IntellectMaterialService intellectMaterialService;

    /**
     * 查询素材
     *
     * @return
     */
    @RequestMapping(value = "/getMaterial/{materialTypeId}", method = RequestMethod.GET)
    public ResponseData<List<IntellectMaterial>> getMaterial(@PathVariable String materialTypeId) {

        return intellectMaterialService.findIntellectMaterialByMaterialTypeId(materialTypeId);

    }

    /**
     * 添加、修改
     *
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody IntellectMaterialValidator intellectMaterialValidator, @PathVariable String op) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(intellectMaterialValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(intellectMaterialValidator));
        }

        //保存操作
        ResponseData data = intellectMaterialService.save(intellectMaterialValidator, op);

        return data;
    }

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public ResponseData<IntellectMaterialValidator> findById(@PathVariable String id) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(validator));
        }

        ResponseData data = intellectMaterialService.findById(id);
        return data;
    }

}
