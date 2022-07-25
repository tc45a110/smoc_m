package com.smoc.cloud.iot.packages.controller;


import com.smoc.cloud.common.iot.validator.IotFlowCardsPrimaryInfoValidator;
import com.smoc.cloud.common.iot.validator.IotPackageInfoValidator;
import com.smoc.cloud.common.iot.validator.PackageCards;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.iot.packages.service.IotPackageInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("iot/package")
public class IotPackageInfoController {

    @Autowired
    private IotPackageInfoService iotPackageInfoService;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ResponseData<PageList<IotPackageInfoValidator>> page(@RequestBody PageParams<IotPackageInfoValidator> pageParams) {
        ResponseData<PageList<IotPackageInfoValidator>> data = iotPackageInfoService.page(pageParams);
        return data;
    }

    /**
     * 根据套餐id查询，套餐配置的信息，及未配置的 物联网卡信息
     *
     * @param PackageId
     * @return
     */
    @RequestMapping(value = "/list/{packageId}", method = RequestMethod.GET)
    public ResponseData<List<IotFlowCardsPrimaryInfoValidator>> list(@PathVariable String PackageId) {
        ResponseData<List<IotFlowCardsPrimaryInfoValidator>> data = iotPackageInfoService.list(PackageId);
        return data;
    }

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public ResponseData<IotPackageInfoValidator> findById(@PathVariable String id) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(validator));
        }

        ResponseData<IotPackageInfoValidator> data = iotPackageInfoService.findById(id);
        return data;
    }

    /**
     * 添加、修改
     *
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody IotPackageInfoValidator iotPackageInfoValidator, @PathVariable String op) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(iotPackageInfoValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(iotPackageInfoValidator));
        }

        //保存操作
        ResponseData data = iotPackageInfoService.save(iotPackageInfoValidator, op);

        return data;
    }

    /**
     * 添加、修改
     *
     * @return
     */
    @RequestMapping(value = "/config/save", method = RequestMethod.POST)
    public ResponseData saveConfig(@RequestBody PackageCards packageCards) {

        //保存操作
        ResponseData data = iotPackageInfoService.saveConfig(packageCards);

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

        ResponseData data = iotPackageInfoService.forbidden(id);
        return data;
    }
}
