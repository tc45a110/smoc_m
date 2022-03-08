package com.smoc.cloud.scheduler.channel.price.service;

import com.smoc.cloud.common.smoc.configuate.validator.ChannelPriceValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.scheduler.channel.price.service.repository.ChannelPriceRepoitory;
import com.smoc.cloud.scheduler.excetion.repository.ExceptionMonitorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ChannelPriceHistoryService {

    @Autowired
    private ChannelPriceRepoitory channelPriceRepoitory;

    @Autowired
    private ExceptionMonitorRepository exceptionMonitorRepository;


    public void saveHistory(List<? extends ChannelPriceValidator> list) {

        try {
            //组织执行的sql
            List<String> sql = new ArrayList<>();
            for (ChannelPriceValidator channelPrice : list) {
                //CreatedBy 存的是今天与上次上次生成记录相差的天数
                if (!StringUtils.isEmpty(channelPrice.getCreatedBy()) && new Integer(channelPrice.getCreatedBy()) > 0) {
                    int days = new Integer(channelPrice.getCreatedBy());
                    for (int i = 0; i < days; i++) {
                        String dataDate = DateTimeUtils.checkOption(channelPrice.getLasttimeHistory(), i + 1);
                        StringBuffer sqlBuffer = new StringBuffer(" insert into smoc.config_channel_price_history(ID,CHANNEL_ID,PRICE_STYLE,AREA_CODE,CHANNEL_PRICE,PRICE_DATE,CREATE_TIME) ");
                        sqlBuffer.append(" select '" + UUID.uuid32() + "' ID,CHANNEL_ID,PRICE_STYLE,AREA_CODE,CHANNEL_PRICE,'" + dataDate + "' ,now() from smoc.config_channel_price where ID ='" + channelPrice.getId() + "' ");
                        String updateSql = "update smoc.config_channel_price set LASTTIME_HISTORY = now() where ID ='" + channelPrice.getId() + "'";
                        sql.add(sqlBuffer.toString());
                        sql.add(updateSql);
                    }
                }
            }
            channelPriceRepoitory.batchSavePriceHistory(sql);
        } catch (Exception e) {
            log.error("[通道价格]:{}",e.getMessage());
        }

    }


}
