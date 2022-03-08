package com.smoc.cloud.scheduler.account.price.processor;

import com.google.gson.Gson;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelPriceValidator;
import com.smoc.cloud.common.smoc.customer.validator.AccountFinanceInfoValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;


/**
 * 业务账号价格历史 批处理
 */
@Slf4j
@Component
public class AccountPriceHistoryProcessor implements ItemProcessor<AccountFinanceInfoValidator, AccountFinanceInfoValidator> {
    @Override
    public AccountFinanceInfoValidator process(AccountFinanceInfoValidator accountFinanceInfoValidator) throws Exception {
        log.info("[AccountFinanceInfoValidator]:{}",new Gson().toJson(accountFinanceInfoValidator));
        return accountFinanceInfoValidator;
    }
}
