package com.smoc.cloud.template.controller;

import com.google.gson.Gson;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;

import com.smoc.cloud.common.smoc.template.AccountTemplateInfoValidator;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.template.service.AccountTemplateInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

/**
 * 模板管理
 */
@Slf4j
@RestController
@RequestMapping("account/template")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class AccountTemplateInfoController {

    @Autowired
    private AccountTemplateInfoService accountTemplateInfoService;


    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ResponseData<PageList<AccountTemplateInfoValidator>> page(@RequestBody PageParams<AccountTemplateInfoValidator> pageParams) {

        return accountTemplateInfoService.page(pageParams);
    }

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public ResponseData<AccountTemplateInfoValidator> findById(@PathVariable String id) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(validator));
        }

        ResponseData data = accountTemplateInfoService.findById(id);
        return data;
    }

    /**
     * 添加、修改
     *
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody AccountTemplateInfoValidator accountTemplateInfoValidator, @PathVariable String op) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(accountTemplateInfoValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(accountTemplateInfoValidator));
        }
        log.info("[accountTemplateInfoValidator]:{}", new Gson().toJson(accountTemplateInfoValidator));
        //保存操作
        ResponseData data = accountTemplateInfoService.save(accountTemplateInfoValidator, op);
        //更新模版到缓存
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            if ("CMPP".equals(accountTemplateInfoValidator.getTemplateAgreementType())) {
                //CMPP固定模版
                if ("1".equals(accountTemplateInfoValidator.getTemplateFlag())) {
                    this.accountTemplateInfoService.loadFixedTemplate(accountTemplateInfoValidator.getBusinessAccount());
                }
                //变量模版，匹配后继续过滤
                if ("2".equals(accountTemplateInfoValidator.getTemplateFlag()) && "FILTER".equals(accountTemplateInfoValidator.getIsFilter())) {
                    this.accountTemplateInfoService.loadFilterVariableTemplate(accountTemplateInfoValidator.getBusinessAccount());
                }
                //变量模版，匹配后不继续过滤
                if ("2".equals(accountTemplateInfoValidator.getTemplateFlag()) && "NO_FILTER".equals(accountTemplateInfoValidator.getIsFilter())) {
                    this.accountTemplateInfoService.loadNoFilterVariableTemplate(accountTemplateInfoValidator.getBusinessAccount());
                }
                //CMPP签名模版
                if ("3".equals(accountTemplateInfoValidator.getTemplateFlag())) {
                    this.accountTemplateInfoService.loadCMPPSignTemplate(accountTemplateInfoValidator.getBusinessAccount());
                }
            }
        }

        return data;
    }

    /**
     * 注销模板
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/cancelTemplate/{id}/{templateStatus}", method = RequestMethod.GET)
    public ResponseData cancelTemplate(@PathVariable String id, @PathVariable String templateStatus) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(validator));
        }

        ResponseData data = accountTemplateInfoService.cancelTemplate(id, templateStatus);

        ResponseData<AccountTemplateInfoValidator> entity = accountTemplateInfoService.findById(id);
        AccountTemplateInfoValidator accountTemplateInfoValidator = entity.getData();
        log.info("[cancelTemplate]:{}", new Gson().toJson(accountTemplateInfoValidator));
        //更新模版缓存
        if (null != accountTemplateInfoValidator) {
            if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
                if ("CMPP".equals(accountTemplateInfoValidator.getTemplateAgreementType())) {
                    //CMPP固定模版
                    if ("1".equals(accountTemplateInfoValidator.getTemplateFlag())) {
                        this.accountTemplateInfoService.loadFixedTemplate(accountTemplateInfoValidator.getBusinessAccount());
                    }
                    //变量模版，匹配后继续过滤
                    if ("2".equals(accountTemplateInfoValidator.getTemplateFlag()) && "FILTER".equals(accountTemplateInfoValidator.getIsFilter())) {
                        this.accountTemplateInfoService.loadFilterVariableTemplate(accountTemplateInfoValidator.getBusinessAccount());
                    }
                    //变量模版，匹配后不继续过滤
                    if ("2".equals(accountTemplateInfoValidator.getTemplateFlag()) && "NO_FILTER".equals(accountTemplateInfoValidator.getIsFilter())) {
                        this.accountTemplateInfoService.loadNoFilterVariableTemplate(accountTemplateInfoValidator.getBusinessAccount());
                    }
                    //CMPP签名模版
                    if ("3".equals(accountTemplateInfoValidator.getTemplateFlag())) {
                        this.accountTemplateInfoService.loadCMPPSignTemplate(accountTemplateInfoValidator.getBusinessAccount());
                    }
                }

                //HTTP、WEB模版通过审核
                if (("WEB".equals(accountTemplateInfoValidator.getTemplateAgreementType()) || "HTTP".equals(accountTemplateInfoValidator.getTemplateAgreementType())) && !"1".equals(accountTemplateInfoValidator.getTemplateStatus())) {
                    //固定模版
                    if ("1".equals(accountTemplateInfoValidator.getTemplateFlag())) {
                        this.accountTemplateInfoService.loadHttpWebTemplate();
                    }
                    //变量模版
                    if ("2".equals(accountTemplateInfoValidator.getTemplateFlag())) {
                        this.accountTemplateInfoService.loadHttpWebVariableTemplates();
                    }
                }
            }
        }
        return data;
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
        ResponseData<AccountTemplateInfoValidator> entity = accountTemplateInfoService.findById(id);
        AccountTemplateInfoValidator accountTemplateInfoValidator = entity.getData();
        ResponseData data = accountTemplateInfoService.deleteById(id);
        //更新模版缓存
        if (null != accountTemplateInfoValidator) {
            if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
                //CMPP固定模版
                if ("1".equals(accountTemplateInfoValidator.getTemplateFlag())) {
                    this.accountTemplateInfoService.loadFixedTemplate(accountTemplateInfoValidator.getBusinessAccount());
                }
                //变量模版，匹配后继续过滤
                if ("2".equals(accountTemplateInfoValidator.getTemplateFlag()) && "FILTER".equals(accountTemplateInfoValidator.getIsFilter())) {
                    this.accountTemplateInfoService.loadFilterVariableTemplate(accountTemplateInfoValidator.getBusinessAccount());
                }
                //变量模版，匹配后不继续过滤
                if ("2".equals(accountTemplateInfoValidator.getTemplateFlag()) && "NO_FILTER".equals(accountTemplateInfoValidator.getIsFilter())) {
                    this.accountTemplateInfoService.loadNoFilterVariableTemplate(accountTemplateInfoValidator.getBusinessAccount());
                }
                //CMPP签名模版
                if ("3".equals(accountTemplateInfoValidator.getTemplateFlag())) {
                    this.accountTemplateInfoService.loadCMPPSignTemplate(accountTemplateInfoValidator.getBusinessAccount());
                }
            }
        }
        return data;
    }
}
