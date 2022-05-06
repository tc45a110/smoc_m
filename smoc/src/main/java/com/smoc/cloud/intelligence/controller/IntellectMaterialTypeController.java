package com.smoc.cloud.intelligence.controller;


import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.intelligence.IntellectMaterialTypeValidator;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.intelligence.entity.IntellectMaterialType;
import com.smoc.cloud.intelligence.service.IntellectMaterialTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

/**
 * 智能短信素材分类
 */
@Slf4j
@RestController
@RequestMapping("intel/resource/type")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class IntellectMaterialTypeController {

    @Autowired
    private IntellectMaterialTypeService intellectMaterialTypeService;

    /**
     * 查询素材分类
     *
     * @return
     */
    @RequestMapping(value = "/getMaterialType/{parentId}", method = RequestMethod.GET)
    public ResponseData<List<IntellectMaterialType>> findIntellectMaterialTypeByParentIdAndStatus(@PathVariable String parentId) {

        return intellectMaterialTypeService.findIntellectMaterialTypeByParentIdAndStatus(parentId);

    }

    /**
     * 添加、修改
     *
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody IntellectMaterialTypeValidator intellectMaterialTypeValidator, @PathVariable String op) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(intellectMaterialTypeValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(intellectMaterialTypeValidator));
        }

        //保存操作
        ResponseData data = intellectMaterialTypeService.save(intellectMaterialTypeValidator, op);

        return data;
    }

    /**
     * 根据id获取信息
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
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(validator));
        }

        ResponseData data = intellectMaterialTypeService.findById(id);
        return data;
    }

    /**
     * 注销或启用
     *
     * @param id
     * @param status
     * @return
     */
    @RequestMapping(value = "/cancel/{id}/{status}", method = RequestMethod.GET)
    public ResponseData cancel(@PathVariable String id, @PathVariable String status) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(validator));
        }

        ResponseData data = intellectMaterialTypeService.cancel(id, status);
        return data;
    }


}
