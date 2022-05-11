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
}
