package com.smoc.cloud.scheduler.account.price.writer;

import com.smoc.cloud.scheduler.account.price.service.AccountFinanceService;
import com.smoc.cloud.scheduler.account.price.service.model.AccountFutruePriceModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 业务账号未来价格 批处理
 */
@Slf4j
@Component
public class AccountPriceFutureWriter implements ItemWriter<AccountFutruePriceModel> {

    @Autowired
    private AccountFinanceService accountFinanceService;

    @Override
    public void write(List<? extends AccountFutruePriceModel> list) throws Exception {
        accountFinanceService.saveFutrue(list);
    }
}
