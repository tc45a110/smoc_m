package com.smoc.cloud.iot.carrier.controller;

import com.smoc.cloud.common.iot.validator.IotCarrierFlowPoolValidator;
import com.smoc.cloud.common.iot.validator.IotCarrierInfoValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.iot.carrier.service.IotCarrierFlowPoolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 运营商流量池
 */
@Slf4j
@RestController
@RequestMapping("iot/carrier/pool")
public class IotCarrierFlowPoolController {

    @Autowired
    private IotCarrierFlowPoolService iotCarrierFlowPoolService;
    
    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ResponseData<PageList<IotCarrierFlowPoolValidator>> page(@RequestBody PageParams<IotCarrierFlowPoolValidator> pageParams) {
        ResponseData<PageList<IotCarrierFlowPoolValidator>> data = iotCarrierFlowPoolService.page(pageParams);
        return data;
    }

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public ResponseData<IotCarrierFlowPoolValidator> findById(@PathVariable String id) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(validator));
        }

        ResponseData<IotCarrierFlowPoolValidator> data = iotCarrierFlowPoolService.findById(id);
        return data;
    }

    /**
     * 添加、修改
     *
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody IotCarrierFlowPoolValidator iotCarrierInfoValidator, @PathVariable String op) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(iotCarrierInfoValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(iotCarrierInfoValidator));
        }

        //保存操作
        ResponseData data = iotCarrierFlowPoolService.save(iotCarrierInfoValidator, op);

        return data;
    }

    /**
     * 注销
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/forbidden/{id}", method = RequestMethod.GET)
    public ResponseData forbidden(@PathVariable String id) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(validator));
        }

        ResponseData data = iotCarrierFlowPoolService.forbidden(id);
        return data;
    }
}
