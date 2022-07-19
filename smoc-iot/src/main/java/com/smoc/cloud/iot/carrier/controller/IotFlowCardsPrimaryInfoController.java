package com.smoc.cloud.iot.carrier.controller;


import com.smoc.cloud.common.iot.validator.IotFlowCardsInfo;
import com.smoc.cloud.common.iot.validator.IotFlowCardsPrimaryInfoValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.iot.carrier.service.IotFlowCardsPrimaryInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("iot/carrier/cards")
public class IotFlowCardsPrimaryInfoController {

    @Autowired
    private IotFlowCardsPrimaryInfoService iotFlowCardsPrimaryInfoService;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ResponseData<PageList<IotFlowCardsPrimaryInfoValidator>> page(@RequestBody PageParams<IotFlowCardsPrimaryInfoValidator> pageParams) {
        ResponseData<PageList<IotFlowCardsPrimaryInfoValidator>> data = iotFlowCardsPrimaryInfoService.page(pageParams);
        return data;
    }

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public ResponseData<IotFlowCardsInfo>  findById(@PathVariable String id) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(validator));
        }

        ResponseData<IotFlowCardsInfo>  data = iotFlowCardsPrimaryInfoService.findById(id);
        return data;
    }

    /**
     * 添加、修改
     *
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody IotFlowCardsPrimaryInfoValidator iotFlowCardsPrimaryInfoValidator, @PathVariable String op) {

        //保存操作
        ResponseData data = iotFlowCardsPrimaryInfoService.save(iotFlowCardsPrimaryInfoValidator, op);

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

        ResponseData data = iotFlowCardsPrimaryInfoService.forbidden(id);
        return data;
    }
}
