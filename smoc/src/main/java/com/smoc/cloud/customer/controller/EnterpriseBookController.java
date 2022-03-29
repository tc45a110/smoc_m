package com.smoc.cloud.customer.controller;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBookInfoValidator;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.customer.service.EnterpriseBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;


/**
 * 通讯录管理接口
 * 2019/4/16 20:26
 **/
@Slf4j
@RestController
@RequestMapping("/book")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class EnterpriseBookController {

    @Autowired
    private EnterpriseBookService enterpriseBookService;

    /**
     * 根据群id查询通讯录
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public PageList<EnterpriseBookInfoValidator> page(@RequestBody PageParams<EnterpriseBookInfoValidator> pageParams) {

        return enterpriseBookService.page(pageParams);
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

        return enterpriseBookService.findById(id);
    }

    /**
     * 添加、修改
     *
     * @param enterpriseBookInfoValidator
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody EnterpriseBookInfoValidator enterpriseBookInfoValidator, @PathVariable String op) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(enterpriseBookInfoValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(enterpriseBookInfoValidator));
        }

        return enterpriseBookService.save(enterpriseBookInfoValidator, op);
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

        return enterpriseBookService.deleteById(id);
    }

    /**
     * 批量保存
     *
     * @param enterpriseBookInfoValidator
     * @return
     */
    @RequestMapping(value = "/bathSave", method = RequestMethod.POST)
    public void bathSave(@RequestBody EnterpriseBookInfoValidator enterpriseBookInfoValidator) {

        enterpriseBookService.bathSave(enterpriseBookInfoValidator);
    }


}
