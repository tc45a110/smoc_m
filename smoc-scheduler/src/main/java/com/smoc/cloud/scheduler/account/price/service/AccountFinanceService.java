package com.smoc.cloud.scheduler.account.price.service;

import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.scheduler.account.price.service.model.AccountFutruePriceModel;
import com.smoc.cloud.scheduler.account.price.service.model.AccountPriceModel;
import com.smoc.cloud.scheduler.common.repository.BatchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 业务账号价格历史 批处理
 */
@Slf4j
@Service
public class AccountFinanceService {

    @Autowired
    private BatchRepository batchRepository;

    /**
     * 为已经跑过批处理得 价格生成历史数据
     *
     * @param list
     */
    public void saveHistory(List<? extends AccountPriceModel> list) {

        try {
            //组织执行的sql
            List<String> sql = new ArrayList<>();
            for (AccountPriceModel model : list) {
                //days 存的是今天与上次上次生成记录相差的天数
                if (null != model.getDays() && model.getDays() > 0) {
                    int days = model.getDays();
                    for (int i = 0; i < days; i++) {
                        String dataDate = DateTimeUtils.checkOption(model.getBatchDate(), i + 1);

                        //如果存在则进行修改操作
                        StringBuffer updateSqlBuffer = new StringBuffer("update smoc.account_price_history set CARRIER_PRICE =" + model.getCarrierPrice() + ",SOURCE_ID='" + model.getId() + "',UPDATED_TIME = now() where ACCOUNT_ID='" + model.getAccountId() + "' and CARRIER='" + model.getCarrier() + "' and PRICE_DATE='" + dataDate + "' ");

                        //进行插入，并判断是否存在
                        StringBuffer sqlBuffer = new StringBuffer(" insert into smoc.account_price_history(ID,SOURCE_ID,ACCOUNT_ID,CARRIER_TYPE,CARRIER,CARRIER_PRICE,PRICE_DATE,CREATED_TIME) ");
                        sqlBuffer.append(" select '" + UUID.uuid32() + "' ,ID,ACCOUNT_ID,CARRIER_TYPE,CARRIER,CARRIER_PRICE,'" + dataDate + "' PRICE_DATE ,now() CREATED_TIME from smoc.account_finance_info where ID ='" + model.getId() + "' ");
                        sqlBuffer.append(" and NOT EXISTS(select * from smoc.account_price_history t where t.ACCOUNT_ID='" + model.getAccountId() + "' and t.CARRIER='" + model.getCarrier() + "' and t.PRICE_DATE='" + dataDate + "')");

                        //修改原来数据的批处理日期
                        String updateSql = "update smoc.account_finance_info set BATCH_DATE = now() where ID ='" + model.getId() + "'";

                        sql.add(updateSqlBuffer.toString());
                        sql.add(sqlBuffer.toString());
                        sql.add(updateSql);
                    }
                }
            }
            //log.info("[业务账号价格批处理]:{}条", sql.size());
            batchRepository.batchSave(sql);
        } catch (Exception e) {
            log.error("[业务账号价格批处理]:{}", e.getMessage());
        }

    }

    /**
     * 处理当天创建数据 或 创建后 没有跑过批处理数据  为这些数据生成价格历史数据
     *
     * @param list
     */
    public void updateOrSaveAccountPrice(List<? extends AccountPriceModel> list) {


        if (null == list || list.size() < 1) {
            return;
        }

        try {
            //组织执行的sql
            List<String> sql = new ArrayList<>();
            for (AccountPriceModel model : list) {

                //如果是当天数据
                if (0 == model.getDays()) {
                    //如果存在则进行修改操作
                    StringBuffer updateSqlBuffer = new StringBuffer("update smoc.account_price_history set CARRIER_PRICE =" + model.getCarrierPrice() + ",SOURCE_ID='" + model.getId() + "',UPDATED_TIME = now() where ACCOUNT_ID='" + model.getAccountId() + "' and CARRIER='" + model.getCarrier() + "' and PRICE_DATE='" + model.getPriceData() + "' ");

                    //进行插入，并判断是否存在
                    StringBuffer sqlBuffer = new StringBuffer(" insert into smoc.account_price_history(ID,SOURCE_ID,ACCOUNT_ID,CARRIER_TYPE,CARRIER,CARRIER_PRICE,PRICE_DATE,CREATED_TIME) ");
                    sqlBuffer.append(" select '" + UUID.uuid32() + "' ,ID,ACCOUNT_ID,CARRIER_TYPE,CARRIER,CARRIER_PRICE,'" + model.getPriceData() + "' PRICE_DATE ,now() CREATED_TIME from smoc.account_finance_info where ID ='" + model.getId() + "' ");
                    sqlBuffer.append(" and NOT EXISTS(select * from smoc.account_price_history t where t.ACCOUNT_ID='" + model.getAccountId() + "' and t.CARRIER='" + model.getCarrier() + "' and t.PRICE_DATE='" + model.getPriceData() + "')");

                    //修改原来数据的批处理日期
                    String updateSql = "update smoc.account_finance_info set BATCH_DATE = now() where ID ='" + model.getId() + "'";

                    sql.add(updateSqlBuffer.toString());
                    sql.add(sqlBuffer.toString());
                    sql.add(updateSql);
                } else {//创建后，没有进行批处理，虽然不是当天创建得，也要把数据补齐

                    //要补齐得天数+加当天
                    int days = model.getDays();
                    for (int i = 0; i <= days; i++) {
                        //价格日期
                        String dataDate = DateTimeUtils.checkOption(model.getCreateTime(), i);
                        //如果存在则进行修改操作
                        StringBuffer updateSqlBuffer = new StringBuffer("update smoc.account_price_history set CARRIER_PRICE =" + model.getCarrierPrice() + ",SOURCE_ID='" + model.getId() + "' where ACCOUNT_ID='" + model.getAccountId() + "' and CARRIER='" + model.getCarrier() + "' and PRICE_DATE='" + dataDate + "' ");

                        //进行插入，并判断是否存在
                        StringBuffer sqlBuffer = new StringBuffer(" insert into smoc.account_price_history(ID,SOURCE_ID,ACCOUNT_ID,CARRIER_TYPE,CARRIER,CARRIER_PRICE,PRICE_DATE,CREATED_TIME) ");
                        sqlBuffer.append(" select '" + UUID.uuid32() + "' ,ID,ACCOUNT_ID,CARRIER_TYPE,CARRIER,CARRIER_PRICE,'" + dataDate + "' PRICE_DATE ,now() CREATED_TIME from smoc.account_finance_info where ID ='" + model.getId() + "' ");
                        sqlBuffer.append(" and NOT EXISTS(select * from smoc.account_price_history t where t.ACCOUNT_ID='" + model.getAccountId() + "' and t.CARRIER='" + model.getCarrier() + "' and t.PRICE_DATE='" + dataDate + "')");

                        //修改原来数据的批处理日期
                        String updateSql = "update smoc.account_finance_info set BATCH_DATE = now() where ID ='" + model.getId() + "'";

                        sql.add(updateSqlBuffer.toString());
                        sql.add(sqlBuffer.toString());
                        sql.add(updateSql);

                    }

                }

            }
            //log.info("[业务账号价格批处理 新数据]:{}条", sql.size());
            batchRepository.batchSave(sql);
        } catch (Exception e) {
            log.error("[业务账号价格批处理 新数据]:{}", e.getMessage());
        }

    }

    /**
     * 修改消息日统计 的业务账号价格
     */
    public void updateMessageDailyStatisticsAccountPrice() {
        try {
            String updateSql = "update smoc.message_daily_statistics m INNER JOIN(select c.CARRIER_PRICE,c.ACCOUNT_ID,c.PRICE_DATE,c.CARRIER from smoc.account_price_history  c ) b on m.BUSINESS_ACCOUNT = b.ACCOUNT_ID and m.CARRIER = b.CARRIER and m.MESSAGE_DATE=b.PRICE_DATE set m.ACCOUNT_PRICE = b.CARRIER_PRICE,ACCOUNT_BATCH_DATE = now() where m.ACCOUNT_BATCH_DATE ='0' ";
            batchRepository.update(updateSql);
            log.info("[消息日汇总 账号价格更新]");
        } catch (Exception e) {
            log.error("[消息日汇总 账号价格更新]:{}", e.getMessage());
        }
    }

    /**
     * 生效未来价格
     * @param list
     */
    public void saveFutrue(List<? extends AccountFutruePriceModel> list) {

        if (null == list || list.size() < 1) {
            return;
        }

        try {
            //组织执行的sql
            List<String> sql = new ArrayList<>();
            for (AccountFutruePriceModel model : list) {

                //价格日期
                String dataDate = model.getPriceDate();
                //1国内运营商 2：国际
                String type = model.getCarrierType();
                //如果存在则进行修改操作
                StringBuffer updateSqlBuffer = new StringBuffer("update smoc.account_price_history set CARRIER_PRICE =" + model.getCarrierPrice() + ",SOURCE_ID='" + model.getSourceId() + "' where ACCOUNT_ID='" + model.getAccountId() + "' and CARRIER='" + model.getCarrier() + "' and PRICE_DATE='" + dataDate + "' ");

                //进行插入，并判断是否存在
                StringBuffer sqlBuffer = new StringBuffer(" insert into smoc.account_price_history(ID,SOURCE_ID,ACCOUNT_ID,CARRIER_TYPE,CARRIER,CARRIER_PRICE,PRICE_DATE,CREATED_TIME) ");
                sqlBuffer.append(" select '" + UUID.uuid32() + "' ,DATA_ID,BUSINESS_ID,'"+type+"' CARRIER_TYPE,PRICE_AREA ,CHANGE_PRICE,'" + dataDate + "' PRICE_DATE ,now() CREATED_TIME from smoc.system_history_price_change_record where ID ='" + model.getId() + "' ");
                sqlBuffer.append(" and NOT EXISTS(select * from smoc.account_price_history t where t.ACCOUNT_ID='" + model.getAccountId() + "' and t.CARRIER='" + model.getCarrier() + "' and t.PRICE_DATE='" + dataDate + "')");

                //修改原来数据的批处理日期
                String updateSql = "update smoc.account_finance_info set BATCH_DATE = now(),CARRIER_PRICE =" + model.getCarrierPrice()+" where ID ='" + model.getSourceId() + "'";

                //修改变更记录表为已处理
                String updateSystemSql = "update smoc.system_history_price_change_record set TASK_STATUS =1  where ID ='" + model.getId() + "'";

                sql.add(updateSqlBuffer.toString());
                sql.add(sqlBuffer.toString());
                sql.add(updateSql);
                sql.add(updateSystemSql);
            }
            batchRepository.batchSave(sql);
        } catch (Exception e) {
            log.error("[业务账号未来价格批处理 新数据]:{}", e.getMessage());
        }
    }
}
