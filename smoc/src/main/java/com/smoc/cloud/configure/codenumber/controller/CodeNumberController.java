package com.smoc.cloud.configure.codenumber.controller;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.validator.CodeNumberInfoValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.configure.codenumber.service.CodeNumberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

/**
 * 码号接口
 **/
@Slf4j
@RestController
@RequestMapping("configure/code/number")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class CodeNumberController {

    @Autowired
    private CodeNumberService codeNumberService;

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public PageList<CodeNumberInfoValidator> page(@RequestBody PageParams<CodeNumberInfoValidator> pageParams) {

        return codeNumberService.page(pageParams);
    }

    /**
     * 添加、修改
     *
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody CodeNumberInfoValidator codeNumberInfoValidator, @PathVariable String op) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(codeNumberInfoValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(codeNumberInfoValidator));
        }

        //保存操作
        ResponseData data = codeNumberService.save(codeNumberInfoValidator, op);

        return data;
    }

}
