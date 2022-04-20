package com.smoc.cloud.http.service;


import com.google.gson.Gson;
import com.smoc.cloud.common.http.server.message.request.AccountBalanceRequestParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.http.entity.FinanceAccount;
import com.smoc.cloud.http.repository.FinanceAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class AccountService {

    @Resource
    private FinanceAccountRepository financeAccountRepository;

    @Autowired
    private SystemHttpApiRequestService systemHttpApiRequestService;

    /**
     * 获取账户余额
     * @param params
     * @return
     */
    public ResponseData<Map<String, String>> getAccountBalance(AccountBalanceRequestParams params) {

        //异步保存请求记录
        systemHttpApiRequestService.save(params.getOrderNo(), params.getAccount(), "getAccountBalance", new Gson().toJson(params));

        Optional<FinanceAccount> financeAccountOptional = financeAccountRepository.findById(params.getAccount());
        if (!financeAccountOptional.isPresent()) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR.getCode(), ResponseCode.PARAM_QUERY_ERROR.getMessage());
        }

        FinanceAccount financeAccount = financeAccountOptional.get();

        Map<String, String> result = new HashMap<>();
        result.put("account", params.getAccount());
        result.put("balance", financeAccount.getAccountUsableSum() + "");
        result.put("time", DateTimeUtils.getDateFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
        return ResponseDataUtil.buildSuccess(result);
    }
}
