package com.smoc.cloud.iot.account.controller;


import com.smoc.cloud.common.iot.validator.AccountPackage;
import com.smoc.cloud.common.iot.validator.IotAccountPackageItemsValidator;
import com.smoc.cloud.common.iot.validator.IotPackageInfoValidator;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.iot.account.service.IotAccountPackageItemsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("iot/account/package")
public class IotAccountPackageItemsController {

    @Autowired
    private IotAccountPackageItemsService iotAccountPackageItemsService;
    
    /**
     * 查询列表
     *
     * @param account
     * @return
     */
    @RequestMapping(value = "/list/{account}", method = RequestMethod.GET)
    public ResponseData<List<IotPackageInfoValidator>> list(@PathVariable String account) {
        ResponseData<List<IotPackageInfoValidator>> data = iotAccountPackageItemsService.list(account);
        return data;
    }

    /**
     * 查询账号配置得套餐
     *
     * @param account
     * @return
     */
    @RequestMapping(value = "/listAccountPackages/{account}", method = RequestMethod.GET)
    public ResponseData<List<IotPackageInfoValidator>> listAccountPackages(@PathVariable String account) {
        ResponseData<List<IotPackageInfoValidator>> data = iotAccountPackageItemsService.listAccountPackages(account);
        return data;
    }

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public ResponseData<IotAccountPackageItemsValidator> findById(@PathVariable String id) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(validator));
        }

        ResponseData<IotAccountPackageItemsValidator> data = iotAccountPackageItemsService.findById(id);
        return data;
    }

    /**
     * 添加、修改
     *
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseData save(@RequestBody AccountPackage accountPackage) {
        //保存操作
        ResponseData data = iotAccountPackageItemsService.save(accountPackage);

        return data;
    }


}
