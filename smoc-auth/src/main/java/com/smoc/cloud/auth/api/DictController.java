package com.smoc.cloud.auth.api;

import com.smoc.cloud.auth.data.provider.entity.BaseCommDict;
import com.smoc.cloud.auth.data.provider.service.BaseCommDictService;
import com.smoc.cloud.common.auth.validator.DictValidator;
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

import java.util.List;

/**
 * 公用字典接口
 * 2019/5/21 17:16
 **/
@Slf4j
@RestController
@RequestMapping("dict")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class DictController {

    @Autowired
    private BaseCommDictService baseCommDictService;

    /**
     * 根据dictType查询
     *
     * @param
     */
    @RequestMapping(value = "/listByDictType/{typeId}/{dictType}", method = RequestMethod.GET)
    public ResponseData<List<BaseCommDict>> listByDictType(@PathVariable String typeId, @PathVariable String dictType) {
        return baseCommDictService.listByDictType(typeId,dictType);
    }

    /**
     * 根据ID 查询
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public ResponseData<BaseCommDict> findById(@PathVariable String id) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR);
        }

        return baseCommDictService.findById(id);
    }

    /**
     * 添加、修改
     *
     * @param dictValidator
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody DictValidator dictValidator, @PathVariable String op) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(dictValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(dictValidator));
        }

        //转BaseUser存放对象
        BaseCommDict baseCommDict = new BaseCommDict();
        BeanUtils.copyProperties(dictValidator, baseCommDict);

        return baseCommDictService.save(baseCommDict, op);
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

        return baseCommDictService.deleteById(id);
    }

}
