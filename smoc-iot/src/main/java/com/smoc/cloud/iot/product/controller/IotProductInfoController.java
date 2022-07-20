package com.smoc.cloud.iot.product.controller;


import com.smoc.cloud.common.iot.validator.IotFlowCardsPrimaryInfoValidator;
import com.smoc.cloud.common.iot.validator.IotProductInfoValidator;
import com.smoc.cloud.common.iot.validator.ProductCards;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.iot.product.service.IotProductInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("iot/product")
public class IotProductInfoController {

    @Autowired
    private IotProductInfoService iotProductInfoService;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ResponseData<PageList<IotProductInfoValidator>> page(@RequestBody PageParams<IotProductInfoValidator> pageParams) {
        ResponseData<PageList<IotProductInfoValidator>> data = iotProductInfoService.page(pageParams);
        return data;
    }

    /**
     * 根据产品id查询，产品配置的信息，及未配置的 物联网卡信息
     *
     * @param productId
     * @return
     */
    @RequestMapping(value = "/list/{productId}", method = RequestMethod.GET)
    public ResponseData<List<IotFlowCardsPrimaryInfoValidator>> list(@PathVariable String productId) {
        ResponseData<List<IotFlowCardsPrimaryInfoValidator>> data = iotProductInfoService.list(productId);
        return data;
    }

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public ResponseData<IotProductInfoValidator> findById(@PathVariable String id) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(validator));
        }

        ResponseData<IotProductInfoValidator> data = iotProductInfoService.findById(id);
        return data;
    }

    /**
     * 添加、修改
     *
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody IotProductInfoValidator iotProductInfoValidator, @PathVariable String op) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(iotProductInfoValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(iotProductInfoValidator));
        }

        //保存操作
        ResponseData data = iotProductInfoService.save(iotProductInfoValidator, op);

        return data;
    }

    /**
     * 添加、修改
     *
     * @return
     */
    @RequestMapping(value = "/config/save", method = RequestMethod.POST)
    public ResponseData saveConfig(@RequestBody ProductCards productCards) {

        //保存操作
        ResponseData data = iotProductInfoService.saveConfig(productCards);

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

        ResponseData data = iotProductInfoService.forbidden(id);
        return data;
    }
}
