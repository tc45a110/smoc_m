package com.smoc.cloud.filter.controller;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.filter.ExcelModel;
import com.smoc.cloud.common.smoc.filter.FilterBlackListValidator;
import com.smoc.cloud.common.smoc.filter.FilterWhiteListValidator;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.filter.entity.FilterBlackList;
import com.smoc.cloud.filter.service.BlackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

/**
 * 黑名单管理接口
 * 2019/4/16 20:26
 **/
@Slf4j
@RestController
@RequestMapping("/filter/black")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class BlackController {

    @Autowired
    private BlackService blackService;

    /**
     * 根据群id查询通讯录
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public PageList<FilterBlackListValidator> page(@RequestBody PageParams<FilterBlackListValidator> pageParams) {

        return blackService.page(pageParams);
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

        return blackService.findById(id);
    }

    /**
     * 添加、修改
     *
     * @param filterBlackListValidator
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody FilterBlackListValidator filterBlackListValidator, @PathVariable String op) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(filterBlackListValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(filterBlackListValidator));
        }

        ResponseData responseData = blackService.save(filterBlackListValidator, op);
        //更新黑名单过滤器
        if (ResponseCode.SUCCESS.getCode().equals(responseData.getCode())) {

            if ("SYSTEM".equals(filterBlackListValidator.getEnterpriseId())) {
                //加载黑名单到黑名单过滤器
                this.blackService.loadBlackList();
            }

            if ("INDUSTRY".equals(filterBlackListValidator.getEnterpriseId())) {
                //加载行业黑名单
                this.blackService.loadIndustryBlackList();
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
        ResponseData<FilterBlackList> model = blackService.findById(id);
        ResponseData responseData = blackService.deleteById(id);
        //更新黑名单过滤器
        if (ResponseCode.SUCCESS.getCode().equals(responseData.getCode())) {
            //删除行业黑名单
            if ("INDUSTRY".equals(model.getData().getEnterpriseId())) {
                this.blackService.deleteIndustryBlackList(model.getData().getGroupId(), model.getData().getMobile());
            }
        }
        return responseData;
    }

    /**
     * 批量保存
     *
     * @param filterBlackListValidator
     * @return
     */
    @RequestMapping(value = "/bathSave", method = RequestMethod.POST)
    public void bathSave(@RequestBody FilterBlackListValidator filterBlackListValidator) {

        blackService.bathSave(filterBlackListValidator);
    }

    /**
     * 查询导出数据
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/excelModel", method = RequestMethod.POST)
    public ResponseData<List<ExcelModel>> excelModel(@RequestBody PageParams<FilterBlackListValidator> pageParams) {

        return blackService.excelModel(pageParams);
    }
}
