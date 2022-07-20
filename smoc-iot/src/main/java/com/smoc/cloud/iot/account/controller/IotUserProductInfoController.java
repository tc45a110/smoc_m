package com.smoc.cloud.iot.account.controller;

import com.smoc.cloud.common.iot.validator.AccountProduct;
import com.smoc.cloud.common.iot.validator.IotProductInfoValidator;
import com.smoc.cloud.common.iot.validator.IotUserProductInfoValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.iot.account.service.IotUserProductInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("iot/user/product")
public class IotUserProductInfoController {

    @Autowired
    private IotUserProductInfoService iotUserProductInfoService;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ResponseData<PageList<IotUserProductInfoValidator>> page(@RequestBody PageParams<IotUserProductInfoValidator> pageParams) {
        ResponseData<PageList<IotUserProductInfoValidator>> data = iotUserProductInfoService.page(pageParams);
        return data;
    }

    /**
     * 查询列表
     *
     * @param account
     * @return
     */
    @RequestMapping(value = "/list/{account}", method = RequestMethod.GET)
    public ResponseData<List<IotProductInfoValidator>> list(@PathVariable String account) {
        ResponseData<List<IotProductInfoValidator>> data = iotUserProductInfoService.list(account);
        return data;
    }

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public ResponseData<IotUserProductInfoValidator> findById(@PathVariable String id) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(validator));
        }

        ResponseData<IotUserProductInfoValidator> data = iotUserProductInfoService.findById(id);
        return data;
    }

    /**
     * 添加、修改
     *
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseData save(@RequestBody AccountProduct accountProduct) {
        //保存操作
        ResponseData data = iotUserProductInfoService.save(accountProduct);

        return data;
    }


}
