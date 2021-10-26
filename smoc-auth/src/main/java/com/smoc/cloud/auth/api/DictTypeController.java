package com.smoc.cloud.auth.api;

import com.smoc.cloud.auth.data.provider.entity.BaseCommDictType;
import com.smoc.cloud.auth.data.provider.service.BaseCommDictTypeService;
import com.smoc.cloud.common.auth.qo.Nodes;
import com.smoc.cloud.common.auth.validator.DictTypeValidator;
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
@RequestMapping("dictType")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class DictTypeController {

    @Autowired
    private BaseCommDictTypeService baseCommDictTypeService;

    /**
     * 查询
     *
     * @param
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseData<List<BaseCommDictType>> list() {
        return baseCommDictTypeService.list();
    }

    /**
     * 查询
     *
     * @param
     */
    @RequestMapping(value = "/getDictTree", method = RequestMethod.GET)
    public List<Nodes> getDictTree() {
        return baseCommDictTypeService.getDictTree();
    }

    /**
     * 查询
     *
     * @param
     */
    @RequestMapping(value = "/getDictTree/{projectName}", method = RequestMethod.GET)
    public List<Nodes> getDictTree(@PathVariable String projectName) {
        return baseCommDictTypeService.getDictTree(projectName);
    }

    /**
     * 根据ID 查询
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public ResponseData<BaseCommDictType> findById(@PathVariable String id) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR);
        }

        return baseCommDictTypeService.findById(id);
    }

    /**
     * 添加、修改
     *
     * @param dictTypeValidator
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody DictTypeValidator dictTypeValidator, @PathVariable String op) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(dictTypeValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(dictTypeValidator));
        }

        //转BaseUser存放对象
        BaseCommDictType baseCommDictType = new BaseCommDictType();
        BeanUtils.copyProperties(dictTypeValidator, baseCommDictType);

        return baseCommDictTypeService.save(baseCommDictType, op);
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

        return baseCommDictTypeService.deleteById(id);
    }

}
