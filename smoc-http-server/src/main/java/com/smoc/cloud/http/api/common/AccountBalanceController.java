package com.smoc.cloud.http.api.common;


import com.google.gson.Gson;
import com.smoc.cloud.common.gateway.utils.ValidatorUtil;
import com.smoc.cloud.common.http.server.message.request.AccountBalanceRequestParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.http.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

/**
 * 查询账户余额
 */
@Slf4j
@RestController
@RequestMapping("account")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class AccountBalanceController {

    @Autowired
    private AccountService accountService;

    /**
     * 查询账户余额
     *
     * @param params
     * @return
     */

    @RequestMapping(value = "/getAccountBalance", method = RequestMethod.POST)
    public ResponseData<Map<String, String>> getAccountBalance(@RequestBody AccountBalanceRequestParams params) {

        log.info("[获取账户余额]：{}", new Gson().toJson(params));

        if (!ValidatorUtil.validate(params)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), ValidatorUtil.validateMessage(params));
        }

        return accountService.getAccountBalance(params);
    }


}
