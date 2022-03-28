package com.smoc.cloud.scheduler.init;

import com.smoc.cloud.scheduler.account.price.service.model.AccountPriceModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;


/**
 * 业务账号价格历史 批处理
 */
@Slf4j
@Component
public class InitProcessor implements ItemProcessor<InitModel, InitModel> {
    @Override
    public InitModel process(InitModel initModel) throws Exception {
        //log.info("[AccountPriceModel]:{}",new Gson().toJson(accountPriceModel));
        return initModel;
    }
}
