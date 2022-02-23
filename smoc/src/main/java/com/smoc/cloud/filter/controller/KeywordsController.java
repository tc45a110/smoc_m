package com.smoc.cloud.filter.controller;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.filter.FilterKeyWordsInfoValidator;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.filter.service.KeywordsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;


/**
 * 关键词管理接口
 * 2019/4/16 20:26
 **/
@Slf4j
@RestController
@RequestMapping("/filter/keywords/system")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class KeywordsController {

    @Autowired
    private KeywordsService keywordsService;

    /**
     * 根据群id查询通讯录
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public PageList<FilterKeyWordsInfoValidator> page(@RequestBody PageParams<FilterKeyWordsInfoValidator> pageParams) {

        return keywordsService.page(pageParams);
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

        return keywordsService.findById(id);
    }

    /**
     * 添加、修改
     *
     * @param filterKeyWordsInfoValidator
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody FilterKeyWordsInfoValidator filterKeyWordsInfoValidator, @PathVariable String op) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(filterKeyWordsInfoValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(filterKeyWordsInfoValidator));
        }

        return keywordsService.save(filterKeyWordsInfoValidator, op);
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

        return keywordsService.deleteById(id);
    }

    /**
     * 批量保存
     *
     * @param filterKeyWordsInfoValidator
     * @return
     */
    @RequestMapping(value = "/bathSave/{op}", method = RequestMethod.POST)
    public void bathSave(@RequestBody FilterKeyWordsInfoValidator filterKeyWordsInfoValidator, @PathVariable String op) {

        keywordsService.bathSave(filterKeyWordsInfoValidator);
    }

    /**
     *  关键字导入
     * @param filterKeyWordsInfoValidator
     */
    @RequestMapping(value = "/expBatchSave", method = RequestMethod.POST)
    public void expBatchSave(@RequestBody FilterKeyWordsInfoValidator filterKeyWordsInfoValidator) {

        keywordsService.expBatchSave(filterKeyWordsInfoValidator);
    }
}
