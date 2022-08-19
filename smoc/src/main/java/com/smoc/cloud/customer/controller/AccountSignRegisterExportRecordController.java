package com.smoc.cloud.customer.controller;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.AccountSignRegisterExportRecordValidator;
import com.smoc.cloud.customer.service.AccountSignRegisterExportRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

@Slf4j
@RestController
@RequestMapping("sign/register/file/record")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class AccountSignRegisterExportRecordController {

    @Autowired
    private AccountSignRegisterExportRecordService accountSignRegisterExportRecordService;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ResponseData<PageList<AccountSignRegisterExportRecordValidator>> page(@RequestBody PageParams pageParams) {

        return accountSignRegisterExportRecordService.page(pageParams);
    }

    @RequestMapping(value = "/findByRegisterOrderNo/{registerOrderNo}", method = RequestMethod.GET)
    public ResponseData<AccountSignRegisterExportRecordValidator>  findByRegisterOrderNo(@PathVariable  String registerOrderNo){
        return accountSignRegisterExportRecordService.findByRegisterOrderNo(registerOrderNo);
    }
}
