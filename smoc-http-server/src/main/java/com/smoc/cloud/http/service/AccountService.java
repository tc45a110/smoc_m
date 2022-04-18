package com.smoc.cloud.http.service;


import com.smoc.cloud.common.http.server.message.request.AccountBalanceRequestParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.utils.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class AccountService {

    public ResponseData<Map<String, String>> getAccountBalance(AccountBalanceRequestParams params){
        Map<String,String> result = new HashMap<>();
        result.put("account",params.getAccount());
        result.put("balance","12358.12");
        result.put("time", DateTimeUtils.getDateFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
        return ResponseDataUtil.buildSuccess(result);
    }
}
