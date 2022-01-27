package com.smoc.cloud.identification.controller;

import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.identification.validator.IdentificationRequestDataValidator;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.identification.service.IdentificationRequestDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

/**
 * 认证请求原数据
 */
@Slf4j
@RestController
@RequestMapping("identification/data")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class IdentificationRequestDataController {

    @Autowired
    private IdentificationRequestDataService identificationRequestDataService;


    /**
     * 根据id获取信息
     *
     * @param orderNo
     * @return
     */
    @RequestMapping(value = "/findByOrderNo/{orderNo}", method = RequestMethod.GET)
    public ResponseData<IdentificationRequestDataValidator> findByOrderNo(@PathVariable String orderNo) {


        ResponseData data = identificationRequestDataService.findByOrderNo(orderNo);
        return data;
    }

    /**
     * 添加、修改
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public void save(@RequestBody IdentificationRequestDataValidator identificationRequestDataValidator) {

        identificationRequestDataService.save(identificationRequestDataValidator);

    }
}
