package com.smoc.cloud.http.message.template.service;


import com.google.gson.Gson;
import com.smoc.cloud.common.http.server.message.request.AccountBalanceRequestParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class AccountService {

    public ResponseData<Map<String, String>> getAccountBalance(AccountBalanceRequestParams params){
        Map<String,String> result = new HashMap<>();
        result.put("account",params.getAccount());
        result.put("balance","12358.12");
        return ResponseDataUtil.buildSuccess(result);
    }
}
