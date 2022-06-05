package com.smoc.cloud.filter.controller;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.filter.ExcelModel;
import com.smoc.cloud.common.smoc.filter.FilterWhiteListValidator;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.filter.entity.FilterWhiteList;
import com.smoc.cloud.filter.service.WhiteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

/**
 * 白名单管理接口
 * 2019/4/16 20:26
 **/
@Slf4j
@RestController
@RequestMapping("/filter/white")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class WhiteController {

    @Autowired
    private WhiteService whiteService;

    /**
     * 根据群id查询通讯录
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public PageList<FilterWhiteListValidator> page(@RequestBody PageParams<FilterWhiteListValidator> pageParams) {

        return whiteService.page(pageParams);
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

        return whiteService.findById(id);
    }

    /**
     * 添加、修改
     *
     * @param filterWhiteListValidator
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody FilterWhiteListValidator filterWhiteListValidator, @PathVariable String op) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(filterWhiteListValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(filterWhiteListValidator));
        }
        ResponseData responseData = whiteService.save(filterWhiteListValidator, op);
        //加载系统到白名单过滤器
        if (ResponseCode.SUCCESS.getCode().equals(responseData.getCode())) {

            if ("SYSTEM".equals(filterWhiteListValidator.getEnterpriseId())) {
                //加载白名单到白名单过滤器
                this.whiteService.loadWhiteList();
            }

            if ("INDUSTRY".equals(filterWhiteListValidator.getEnterpriseId())) {
                //加载行业白名单
                this.whiteService.loadIndustryWhiteList();
            }
        }
        return responseData;
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
        ResponseData<FilterWhiteList> findById = whiteService.findById(id);

        ResponseData responseData = whiteService.deleteById(id);
        //把明白从白名单过滤器删除
        if (ResponseCode.SUCCESS.getCode().equals(responseData.getCode())) {
            if ("SYSTEM".equals(findById.getData().getEnterpriseId())) {
                //删除白名单到白名单过滤器
                this.whiteService.delWhiteList(findById.getData().getMobile());
            }

            if ("INDUSTRY".equals(findById.getData().getEnterpriseId())) {
                //删除行业白名单
                this.whiteService.deleteIndustryWhiteList(findById.getData().getGroupId(), findById.getData().getMobile());
            }

        }
        return responseData;
    }

    /**
     * 批量保存
     *
     * @param filterWhiteListValidator
     * @return
     */
    @RequestMapping(value = "/bathSave", method = RequestMethod.POST)
    public void bathSave(@RequestBody FilterWhiteListValidator filterWhiteListValidator) {

        whiteService.bathSave(filterWhiteListValidator);
    }

    /**
     * 查询导出数据
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/excelModel", method = RequestMethod.POST)
    public ResponseData<List<ExcelModel>> excelModel(@RequestBody PageParams<FilterWhiteListValidator> pageParams) {

        return whiteService.excelModel(pageParams);
    }
}
