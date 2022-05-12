package com.smoc.cloud.intelligence.controller;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.intelligence.IntellectShortUrlValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.intelligence.service.IntellectShortUrlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 智能短链
 */
@Slf4j
@RestController
@RequestMapping("intel/short/url")
public class IntellectShortUrlController {

    @Autowired
    private IntellectShortUrlService intellectShortUrlService;

    /**
     * 查询短链
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public PageList<IntellectShortUrlValidator> page(@RequestBody PageParams<IntellectShortUrlValidator> pageParams) {

        return intellectShortUrlService.page(pageParams);

    }

    /**
     * 添加、修改
     *
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseData save(@RequestBody IntellectShortUrlValidator intellectShortUrlValidator) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(intellectShortUrlValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(intellectShortUrlValidator));
        }

        //保存操作
        return intellectShortUrlService.save(intellectShortUrlValidator);

    }
}
