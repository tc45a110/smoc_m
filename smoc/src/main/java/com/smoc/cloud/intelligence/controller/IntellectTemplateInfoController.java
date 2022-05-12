package com.smoc.cloud.intelligence.controller;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.intelligence.IntellectMaterialValidator;
import com.smoc.cloud.common.smoc.intelligence.IntellectTemplateInfoValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.intelligence.service.IntellectTemplateInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 智能短信模版
 */
@Slf4j
@RestController
@RequestMapping("intel/template")
public class IntellectTemplateInfoController {

    @Autowired
    private IntellectTemplateInfoService intellectTemplateInfoService;

    /**
     * 查询模版
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public PageList<IntellectTemplateInfoValidator> page(@RequestBody PageParams<IntellectTemplateInfoValidator> pageParams) {

        return intellectTemplateInfoService.page(pageParams);

    }

    /**
     * 添加、修改
     *
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseData save(@RequestBody IntellectTemplateInfoValidator intellectTemplateInfoValidator) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(intellectTemplateInfoValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(intellectTemplateInfoValidator));
        }

        //保存操作
        ResponseData data = intellectTemplateInfoService.save(intellectTemplateInfoValidator);

        return data;
    }

    /**
     * 根据templateId 修改 tplId
     * @param templateId
     * @param tplId
     * @return
     */
    @RequestMapping(value = "/updateTplIdAndStatus/{templateId}/{tplId}/{status}", method = RequestMethod.GET)
    public ResponseData updateTplId(@PathVariable String templateId,@PathVariable String tplId,@PathVariable Integer status) {

        //保存操作
        ResponseData data = intellectTemplateInfoService.updateTplIdAndStatus(templateId,tplId,status);

        return data;
    }

    /**
     * templateId 修改 check status
     * @param status
     * @param templateId
     * @return
     */
    @RequestMapping(value = "/updateCheckStatus/{templateId}/{status}", method = RequestMethod.GET)
    public ResponseData updateCheckStatus(@PathVariable String templateId,@PathVariable Integer status) {

        //保存操作
        ResponseData data = intellectTemplateInfoService.updateCheckStatus(templateId,status);

        return data;
    }

    /**
     * tplId 修改 check status
     * @param status
     * @param tplId
     * @return
     */
    @RequestMapping(value = "/updateCheckStatusByTplId/{tplId}/{status}", method = RequestMethod.GET)
    public ResponseData updateCheckStatusByTplId(@PathVariable String tplId,@PathVariable Integer status) {

        //保存操作
        ResponseData data = intellectTemplateInfoService.updateCheckStatusByTplId(tplId,status);

        return data;
    }

    /**
     * templateId 修改  status
     * @param status
     * @param templateId
     * @return
     */
    @RequestMapping(value = "/updateStatusByTemplateId/{templateId}/{status}", method = RequestMethod.GET)
    public ResponseData updateStatusByTemplateId(@PathVariable String templateId,@PathVariable Integer status) {

        //保存操作
        ResponseData data = intellectTemplateInfoService.updateStatusByTemplateId(templateId,status);

        return data;
    }
}
