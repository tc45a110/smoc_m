package com.smoc.cloud.scheduler.channel.price.service;

import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.scheduler.channel.price.service.model.ChannelPriceModel;
import com.smoc.cloud.scheduler.common.repository.BatchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ChannelPriceHistoryService {

    @Autowired
    private BatchRepository batchRepository;

    public void saveHistory(List<? extends ChannelPriceModel> list) {

        try {
            //组织执行的sql
            List<String> sql = new ArrayList<>();
            for (ChannelPriceModel model : list) {
                //days 存的是今天与上次上次生成记录相差的天数
                if (null != model.getDays() && model.getDays() > 0) {
                    int days = model.getDays();
                    for (int i = 0; i < days; i++) {

                        //价格日期
                        String dataDate = DateTimeUtils.checkOption(model.getBatchDate(), i + 1);

                        //如果存在则进行修改操作
                        StringBuffer updateSqlBuffer = new StringBuffer("update smoc.config_channel_price_history set CHANNEL_PRICE =" + model.getChannelPrice() + ",SOURCE_ID='" + model.getId() + "' where CHANNEL_ID='" + model.getChannelId() + "' and AREA_CODE='" + model.getAreaCode() + "' and PRICE_DATE='" + dataDate + "' ");

                        //进行插入，并判断是否存在
                        StringBuffer insertSqlBuffer = new StringBuffer("insert into smoc.config_channel_price_history(ID,SOURCE_ID,CHANNEL_ID,PRICE_STYLE,AREA_CODE,CHANNEL_PRICE,PRICE_DATE,CREATE_TIME) ");
                        insertSqlBuffer.append(" select '" + UUID.uuid32() + "',ID,CHANNEL_ID,PRICE_STYLE,AREA_CODE,CHANNEL_PRICE,'" + model.getPriceData() + "' PRICE_DATE ,now() CREATE_TIME from smoc.config_channel_price where ID ='" + model.getId() + "' ");
                        insertSqlBuffer.append(" and NOT EXISTS(select * from smoc.config_channel_price_history t where t.CHANNEL_ID='" + model.getChannelId() + "' and t.AREA_CODE='" + model.getAreaCode() + "' and t.PRICE_DATE='" + dataDate + "' )");

                        //修改原来数据的批处理日期
                        String updateSql = "update smoc.config_channel_price set BATCH_DATE = now() where ID ='" + model.getId() + "'";

                        sql.add(updateSqlBuffer.toString());
                        sql.add(insertSqlBuffer.toString());
                        sql.add(updateSql);
                    }
                }
            }
            //log.info("[通道价格批处理]:{}", sql.size());
            batchRepository.batchSave(sql);
        } catch (Exception e) {
            log.error("[通道价格批处理]:{}", e.getMessage());
        }

    }

    public void updateOrSaveChannelPrice(List<? extends ChannelPriceModel> list) {

        if (null == list || list.size() < 1) {
            return;
        }

        try {
            List<String> sql = new ArrayList<>();
            for (ChannelPriceModel model : list) {

                //如果存在则进行修改操作
                StringBuffer updateSqlBuffer = new StringBuffer("update smoc.config_channel_price_history set CHANNEL_PRICE =" + model.getChannelPrice() + ",SOURCE_ID='" + model.getId() + "' where CHANNEL_ID='" + model.getChannelId() + "' and AREA_CODE='" + model.getAreaCode() + "' and PRICE_DATE='" + model.getPriceData() + "' ");

                //如果不存在则进行 insert
                StringBuffer insertSqlBuffer = new StringBuffer("insert into smoc.config_channel_price_history(ID,SOURCE_ID,CHANNEL_ID,PRICE_STYLE,AREA_CODE,CHANNEL_PRICE,PRICE_DATE,CREATE_TIME) ");
                insertSqlBuffer.append(" select '" + UUID.uuid32() + "',ID,CHANNEL_ID,PRICE_STYLE,AREA_CODE,CHANNEL_PRICE,'" + model.getPriceData() + "' PRICE_DATE ,now() CREATE_TIME from smoc.config_channel_price where ID ='" + model.getId() + "' ");
                insertSqlBuffer.append(" and NOT EXISTS(select * from smoc.config_channel_price_history t where t.CHANNEL_ID='" + model.getChannelId() + "' and t.AREA_CODE='" + model.getAreaCode() + "' and t.PRICE_DATE='" + model.getPriceData() + "' )");

                //修改原来数据的批处理日期
                String updateSql = "update smoc.config_channel_price set BATCH_DATE = now() where ID ='" + model.getId() + "'";

                sql.add(updateSqlBuffer.toString());
                sql.add(insertSqlBuffer.toString());
                sql.add(updateSql);
            }

            //log.info("[通道价格批处理 新数据]:{}", sql.size());
            batchRepository.batchSave(sql);
        } catch (Exception e) {
            log.error("[通道价格批处理]:{}", e.getMessage());
        }

    }


}
