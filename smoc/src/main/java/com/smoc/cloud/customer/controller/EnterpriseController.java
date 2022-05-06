package com.smoc.cloud.customer.controller;

import com.smoc.cloud.common.auth.qo.Nodes;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBasicInfoValidator;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.customer.service.EnterpriseService;
import com.smoc.cloud.utils.RandomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

/**
 * 企业接入接口
 **/
@Slf4j
@RestController
@RequestMapping("enterprise")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class EnterpriseController {

    @Autowired
    private EnterpriseService enterpriseService;

    @Autowired
    private RandomService randomService;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public PageList<EnterpriseBasicInfoValidator> page(@RequestBody PageParams<EnterpriseBasicInfoValidator> pageParams) {

        return enterpriseService.page(pageParams);
    }

    /**
     * 根据id获取信息
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
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(validator));
        }

        ResponseData data = enterpriseService.findById(id);
        return data;
    }

    /**
     * 添加、修改
     *
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody EnterpriseBasicInfoValidator enterpriseBasicInfoValidator, @PathVariable String op) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(enterpriseBasicInfoValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(enterpriseBasicInfoValidator));
        }

        //保存操作
        ResponseData data = enterpriseService.save(enterpriseBasicInfoValidator, op);

        return data;
    }

    /**
     * 注销、启用企业业务
     *
     * @param id
     * @param status
     * @return
     */
    @RequestMapping(value = "/forbiddenEnterprise/{id}/{status}", method = RequestMethod.GET)
    public ResponseData forbiddenEnterprise(@PathVariable String id, @PathVariable String status) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(validator));
        }

        ResponseData data = enterpriseService.forbiddenEnterprise(id, status);
        return data;
    }

    /**
     * 生成企业标识
     *
     * @return
     */
    @RequestMapping(value = "/createEnterpriseFlag", method = RequestMethod.GET)
    public ResponseData<String> createEnterpriseFlag() {

        String enterpriseFlag = randomService.getRandomStr(3);
        return ResponseDataUtil.buildSuccess(enterpriseFlag);
    }

    @RequestMapping(value = "/findByAccountBusinessType/{businessType}", method = RequestMethod.GET)
    public ResponseData<List<Nodes>> findByAccountBusinessType(@PathVariable String businessType) {

        return enterpriseService.findByAccountBusinessType(businessType);
    }
}
