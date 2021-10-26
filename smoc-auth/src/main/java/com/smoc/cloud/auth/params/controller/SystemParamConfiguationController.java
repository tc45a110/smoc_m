package com.smoc.cloud.auth.params.controller;

import com.smoc.cloud.auth.params.entity.BaseSystemParamConfiguation;
import com.smoc.cloud.auth.params.service.BaseSystemParamConfiguationService;
import com.smoc.cloud.common.auth.qo.ConfiguationParams;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

/**
 * 系统参数配置
 * 2020/5/30 15:06
 **/
@Slf4j
@RestController
@RequestMapping("parames")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class SystemParamConfiguationController {

    @Autowired
    private BaseSystemParamConfiguationService baseSystemParamConfiguationService;

    //分页查询
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public PageList<ConfiguationParams> page(@RequestBody PageParams<ConfiguationParams> pageParams) {
        return baseSystemParamConfiguationService.page(pageParams);
    }

    //查询
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseData<List<ConfiguationParams>> list(@RequestBody ConfiguationParams params) {
        return baseSystemParamConfiguationService.list(params);
    }


    /**
     * 根据ID 查询
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public ResponseData<BaseSystemParamConfiguation> findById(@PathVariable String id) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR);
        }

        return baseSystemParamConfiguationService.findById(id);
    }

    /**
     * 添加、修改
     *
     * @param paramsList
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody List<BaseSystemParamConfiguation> paramsList, @PathVariable String op) {

        return baseSystemParamConfiguationService.save(paramsList, op);
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

        return baseSystemParamConfiguationService.deleteById(id);
    }
}
