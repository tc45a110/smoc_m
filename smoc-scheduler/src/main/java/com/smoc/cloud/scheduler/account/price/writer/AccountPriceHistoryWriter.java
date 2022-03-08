package com.smoc.cloud.scheduler.account.price.writer;

import com.smoc.cloud.common.smoc.customer.validator.AccountFinanceInfoValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 业务账号价格历史 批处理
 */
@Slf4j
@Component
public class AccountPriceHistoryWriter implements ItemWriter<AccountFinanceInfoValidator> {


    @Override
    public void write(List<? extends AccountFinanceInfoValidator> list) throws Exception {
        //channelPriceHistoryService.saveHistory(list);
    }
}
