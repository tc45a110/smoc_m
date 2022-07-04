package com.smoc.cloud.scheduler.account.price.processor;

import com.smoc.cloud.scheduler.account.price.service.model.AccountFutruePriceModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;


/**
 * 业务账号未来价格 批处理
 */
@Slf4j
@Component
public class AccountPriceFutureProcessor implements ItemProcessor<AccountFutruePriceModel, AccountFutruePriceModel> {
    @Override
    public AccountFutruePriceModel process(AccountFutruePriceModel accountFutruePriceModel) throws Exception {
        //log.info("[AccountPriceModel]:{}",new Gson().toJson(accountPriceModel));
        return accountFutruePriceModel;
    }
}
