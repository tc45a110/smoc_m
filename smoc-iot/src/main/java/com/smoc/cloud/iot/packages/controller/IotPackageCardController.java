package com.smoc.cloud.iot.packages.controller;


import com.smoc.cloud.common.iot.validator.IotFlowCardsPrimaryInfoValidator;
import com.smoc.cloud.common.iot.validator.IotPackageCardValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.iot.packages.service.IotPackageCardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("iot/package/cards")
public class IotPackageCardController {

    @Autowired
    private IotPackageCardService iotPackageCardService;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ResponseData<PageList<IotPackageCardValidator>> page(@RequestBody PageParams<IotPackageCardValidator> pageParams) {
        ResponseData<PageList<IotPackageCardValidator>> data = iotPackageCardService.page(pageParams);
        return data;
    }

    /**
     * 根据套餐id，查询套餐绑定的物联网卡
     *
     * @param packageId
     * @return
     */
    @RequestMapping(value = "/listCardsByPackageId/{account}/{packageId}", method = RequestMethod.GET)
    public ResponseData<List<IotFlowCardsPrimaryInfoValidator>> listCardsByPackageId(@PathVariable String account,@PathVariable String packageId) {
        ResponseData<List<IotFlowCardsPrimaryInfoValidator>> data = iotPackageCardService.listCardsByPackageId(account,packageId);
        return data;
    }

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public ResponseData<IotPackageCardValidator> findById(@PathVariable String id) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(validator));
        }

        ResponseData<IotPackageCardValidator> data = iotPackageCardService.findById(id);
        return data;
    }

    /**
     * 添加、修改
     *
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseData save(@RequestBody List<IotPackageCardValidator> cards) {

        //保存操作
        ResponseData data = iotPackageCardService.save(cards);

        return data;
    }

}
