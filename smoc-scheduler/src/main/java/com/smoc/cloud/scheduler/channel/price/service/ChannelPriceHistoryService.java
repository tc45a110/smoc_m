package com.smoc.cloud.scheduler.channel.price.service;

import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.scheduler.channel.price.service.model.ChannelPriceModel;
import com.smoc.cloud.scheduler.common.repository.BatchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ChannelPriceHistoryService {

    @Autowired
    private BatchRepository batchRepository;

    /**
     * 为已经跑过批处理得 价格生成历史数据
     *
     * @param list
     */
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
                        insertSqlBuffer.append(" select '" + UUID.uuid32() + "',ID,CHANNEL_ID,PRICE_STYLE,AREA_CODE,CHANNEL_PRICE,'" + dataDate + "' PRICE_DATE ,now() CREATE_TIME from smoc.config_channel_price where ID ='" + model.getId() + "' ");
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


    /**
     * 处理当天创建数据 或 创建后 没有跑过批处理数据  为这些数据生成价格历史数据
     *
     * @param list
     */
    public void updateOrSaveChannelPrice(List<? extends ChannelPriceModel> list) {

        if (null == list || list.size() < 1) {
            return;
        }

        try {
            List<String> sql = new ArrayList<>();
            for (ChannelPriceModel model : list) {
                log.info("[days]:{}", model.getDays());
                //如果是当天数据
                if (0 == model.getDays()) {
                    //如果存在则进行修改操作
                    StringBuffer updateSqlBuffer = new StringBuffer("update smoc.config_channel_price_history set CHANNEL_PRICE =" + model.getChannelPrice() + ",SOURCE_ID='" + model.getId() + "',UPDATED_TIME = now() where CHANNEL_ID='" + model.getChannelId() + "' and AREA_CODE='" + model.getAreaCode() + "' and PRICE_DATE='" + model.getPriceData() + "' ");

                    //如果不存在则进行 insert
                    StringBuffer insertSqlBuffer = new StringBuffer("insert into smoc.config_channel_price_history(ID,SOURCE_ID,CHANNEL_ID,PRICE_STYLE,AREA_CODE,CHANNEL_PRICE,PRICE_DATE,CREATE_TIME) ");
                    insertSqlBuffer.append(" select '" + UUID.uuid32() + "',ID,CHANNEL_ID,PRICE_STYLE,AREA_CODE,CHANNEL_PRICE,'" + model.getPriceData() + "' PRICE_DATE ,now() CREATE_TIME from smoc.config_channel_price where ID ='" + model.getId() + "' ");
                    insertSqlBuffer.append(" and NOT EXISTS(select * from smoc.config_channel_price_history t where t.CHANNEL_ID='" + model.getChannelId() + "' and t.AREA_CODE='" + model.getAreaCode() + "' and t.PRICE_DATE='" + model.getPriceData() + "' )");

                    //修改原来数据的批处理日期
                    String updateSql = "update smoc.config_channel_price set BATCH_DATE = now() where ID ='" + model.getId() + "'";

                    sql.add(updateSqlBuffer.toString());
                    sql.add(insertSqlBuffer.toString());
                    sql.add(updateSql);
                } else {//创建后，没有进行批处理，虽然不是当天创建得，也要把数据补齐

                    //要补齐得天数+加当天
                    int days = model.getDays() + 1;
                    for (int i = 0; i < days; i++) {

                        //价格日期
                        String dataDate = DateTimeUtils.checkOption(model.getCreateTime(), i);

                        StringBuffer updateSqlBuffer = new StringBuffer("update smoc.config_channel_price_history set CHANNEL_PRICE =" + model.getChannelPrice() + ",SOURCE_ID='" + model.getId() + "',UPDATED_TIME = now() where CHANNEL_ID='" + model.getChannelId() + "' and AREA_CODE='" + model.getAreaCode() + "' and PRICE_DATE='" + dataDate + "' ");

                        //如果不存在则进行 insert
                        StringBuffer insertSqlBuffer = new StringBuffer("insert into smoc.config_channel_price_history(ID,SOURCE_ID,CHANNEL_ID,PRICE_STYLE,AREA_CODE,CHANNEL_PRICE,PRICE_DATE,CREATE_TIME) ");
                        insertSqlBuffer.append(" select '" + UUID.uuid32() + "',ID,CHANNEL_ID,PRICE_STYLE,AREA_CODE,CHANNEL_PRICE,'" + dataDate + "' PRICE_DATE ,now() CREATE_TIME from smoc.config_channel_price where ID ='" + model.getId() + "' ");
                        insertSqlBuffer.append(" and NOT EXISTS(select * from smoc.config_channel_price_history t where t.CHANNEL_ID='" + model.getChannelId() + "' and t.AREA_CODE='" + model.getAreaCode() + "' and t.PRICE_DATE='" + dataDate + "' )");

                        //修改原来数据的批处理日期
                        String updateSql = "update smoc.config_channel_price set BATCH_DATE = now() where ID ='" + model.getId() + "'";

                        sql.add(updateSqlBuffer.toString());
                        sql.add(insertSqlBuffer.toString());
                        sql.add(updateSql);
                    }

                }
            }

            //log.info("[通道价格批处理 新数据]:{}", sql.size());
            batchRepository.batchSave(sql);
        } catch (Exception e) {
            log.error("[通道价格批处理]:{}", e.getMessage());
        }

    }

    /**
     * 修改消息日统计 的通道价格
     */
    public void updateMessageDailyStatisticsChannelPrice() {
        try {

            //获取当前日期 为结束日期
            String updateSql = "update smoc.message_daily_statistics m INNER JOIN(select c.CHANNEL_PRICE,c.CHANNEL_ID,c.PRICE_DATE,c.AREA_CODE from smoc.config_channel_price_history  c ) b on m.CHANNEL_ID = b.CHANNEL_ID and m.PRICE_AREA_CODE = b.AREA_CODE and m.MESSAGE_DATE=b.PRICE_DATE set m.CHANNEL_PRICE = b.CHANNEL_PRICE,CHANNEL_BATCH_DATE = now() where m.CHANNEL_BATCH_DATE ='0' ";
            batchRepository.update(updateSql);
            log.info("[消息日汇总 通道价格更新]");
        } catch (Exception e) {
            log.error("[消息日汇总 通道价格更新]:{}", e.getMessage());
        }
    }


}
