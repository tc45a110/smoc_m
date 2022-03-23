package com.smoc.cloud.configure.advance.repository;

import com.google.gson.Gson;
import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.configuate.validator.SystemHistoryPriceChangeRecordValidator;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.configure.advance.rowmapper.SystemHistoryPriceChangeRecordRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SystemHistoryPriceChangeRecordRepositoryImpl extends BasePageRepository {

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    public PageList<SystemHistoryPriceChangeRecordValidator> page(PageParams<SystemHistoryPriceChangeRecordValidator> pageParams) {

        SystemHistoryPriceChangeRecordValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append(" t.ID,");
        sqlBuffer.append(" t.CHANGE_TYPE,");
        sqlBuffer.append(" t.BUSINESS_ID,");
        sqlBuffer.append(" t.PRICE_AREA,");
        sqlBuffer.append(" t.AREA_TYPE,");
        sqlBuffer.append(" DATE_FORMAT(t.START_DATE, '%Y-%m-%d')START_DATE, ");
        sqlBuffer.append(" t.CHANGE_PRICE,");
        sqlBuffer.append(" t.TASK_TYPE,");
        sqlBuffer.append(" t.TASK_STATUS,");
        sqlBuffer.append(" t.CREATED_BY,");
        sqlBuffer.append(" DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME ");
        sqlBuffer.append(" from system_history_price_change_record t ");
        sqlBuffer.append(" where 1=1  ");

        List<Object> paramsList = new ArrayList<Object>();

        //变更类型
        if (!StringUtils.isEmpty(qo.getBusinessId())) {
            sqlBuffer.append(" and t.BUSINESS_ID =?");
            paramsList.add(qo.getBusinessId().trim());
        }

        //变更类型
        if (!StringUtils.isEmpty(qo.getChangeType())) {
            sqlBuffer.append(" and t.CHANGE_TYPE =?");
            paramsList.add(qo.getChangeType().trim());
        }


        sqlBuffer.append(" order by t.CREATED_TIME desc");

        //log.info("[sql]:{}",sqlBuffer.toString());

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);
        //log.info("[params]:{}",new Gson().toJson(paramsList));

        PageList<SystemHistoryPriceChangeRecordValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new SystemHistoryPriceChangeRecordRowMapper());
        pageList.getPageParams().setParams(qo);

        return pageList;


    }

    /**
     * 批量更新 历史价格
     *
     * @param list
     * @param changeType
     */
    public void batchUpdateHistoryPrice(List<SystemHistoryPriceChangeRecordValidator> list, String changeType, String taskType) {

        if ("CHANNEL".equals(changeType)) {
            batchUpdateChannel(list, taskType);
        }

        if ("ACCOUNT".equals(changeType)) {
            batchUpdateAccount(list, taskType);
        }


    }

    public void batchUpdateChannel(List<SystemHistoryPriceChangeRecordValidator> list, String taskType) {

        if ("future".equals(taskType)) {

            List<String> sqlList = new ArrayList();
            for (SystemHistoryPriceChangeRecordValidator validator : list) {

                StringBuffer insertSql = new StringBuffer("insert into system_history_price_change_record(ID,CHANGE_TYPE,BUSINESS_ID,PRICE_AREA,START_DATE,CHANGE_PRICE,CREATED_BY,CREATED_TIME,TASK_STATUS,TASK_TYPE,DATA_ID) ");
                insertSql.append(" values('" + UUID.uuid32() + "','CHANNEL','" + validator.getBusinessId() + "','" + validator.getPriceArea() + "','" + validator.getStartDate() + "'," + validator.getChangePrice() + ",'" + validator.getCreatedBy() + "',now(),'0','2','"+validator.getId()+"')");
                sqlList.add(insertSql.toString());
            }

            //log.info("[业务账号历史价格修改]：{}", sqlList.toString());
            //根据参数个数，组织参数值
            String[] params = new String[sqlList.size()];
            sqlList.toArray(params);
            jdbcTemplate.batchUpdate(params);

        }

        if ("history".equals(taskType)) {
            List<String> sqlList = new ArrayList();


            for (SystemHistoryPriceChangeRecordValidator validator : list) {

                StringBuffer insertSql = new StringBuffer("insert into system_history_price_change_record(ID,CHANGE_TYPE,BUSINESS_ID,PRICE_AREA,START_DATE,CHANGE_PRICE,CREATED_BY,CREATED_TIME,TASK_STATUS,TASK_TYPE,DATA_ID) ");
                insertSql.append(" values('" + UUID.uuid32() + "','CHANNEL','" + validator.getBusinessId() + "','" + validator.getPriceArea() + "','" + validator.getStartDate() + "'," + validator.getChangePrice() + ",'" + validator.getCreatedBy() + "',now(),'1','1','"+validator.getId()+"')");
                sqlList.add(insertSql.toString());

                //更新当前价格
                String updateChannelPrice = "update config_channel_price set CHANNEL_PRICE=" + validator.getChangePrice() + ",UPDATED_TIME = now(),UPDATED_BY='" + validator.getCreatedBy() + "' where id = '" + validator.getId() + "'";
                sqlList.add(updateChannelPrice);

                //更新通道历史价格中的，价格
                String updateChannelHistoryPrice = "update config_channel_price_history set CHANNEL_PRICE=" + validator.getChangePrice() + ",UPDATED_TIME = now(),UPDATED_BY='" + validator.getCreatedBy() + "' where CHANNEL_ID='" + validator.getBusinessId() + "' and AREA_CODE ='" + validator.getPriceArea() + "' and PRICE_DATE>='" + validator.getStartDate() + "' ";
                sqlList.add(updateChannelHistoryPrice);

                String updateMessageDailyStatisticsChannelPrice = "update message_daily_statistics set CHANNEL_PRICE=" + validator.getChangePrice() + ",UPDATED_TIME = now(),UPDATED_BY='" + validator.getCreatedBy() + "' where CHANNEL_ID='" + validator.getBusinessId() + "' and PRICE_AREA_CODE ='" + validator.getPriceArea() + "' and MESSAGE_DATE>='" + validator.getStartDate() + "' ";
                sqlList.add(updateMessageDailyStatisticsChannelPrice);

            }

            //log.info("[通道历史价格修改]：{}", sqlList.toString());
            //根据参数个数，组织参数值
            String[] params = new String[sqlList.size()];
            sqlList.toArray(params);
            jdbcTemplate.batchUpdate(params);
        }

    }

    public void batchUpdateAccount(List<SystemHistoryPriceChangeRecordValidator> list, String taskType) {

        if ("future".equals(taskType)) {

            List<String> sqlList = new ArrayList();
            for (SystemHistoryPriceChangeRecordValidator validator : list) {

                //更新当前价格
                StringBuffer insertSql = new StringBuffer("insert into system_history_price_change_record(ID,CHANGE_TYPE,BUSINESS_ID,PRICE_AREA,START_DATE,CHANGE_PRICE,CREATED_BY,CREATED_TIME,AREA_TYPE,TASK_STATUS,TASK_TYPE,DATA_ID) ");
                insertSql.append(" values('" + UUID.uuid32() + "','ACCOUNT','" + validator.getBusinessId() + "','" + validator.getPriceArea() + "','" + validator.getStartDate() + "'," + validator.getChangePrice() + ",'" + validator.getCreatedBy() + "',now(),'" + validator.getAreaType() + "','0','2','"+validator.getId()+"')");
                sqlList.add(insertSql.toString());
            }

            //log.info("[业务账号历史价格修改]：{}", sqlList.toString());
            //根据参数个数，组织参数值
            String[] params = new String[sqlList.size()];
            sqlList.toArray(params);
            jdbcTemplate.batchUpdate(params);

        }

        if ("history".equals(taskType)) {
            List<String> sqlList = new ArrayList();

            for (SystemHistoryPriceChangeRecordValidator validator : list) {

                //更新当前价格
                StringBuffer insertSql = new StringBuffer("insert into system_history_price_change_record(ID,CHANGE_TYPE,BUSINESS_ID,PRICE_AREA,START_DATE,CHANGE_PRICE,CREATED_BY,CREATED_TIME,AREA_TYPE,TASK_STATUS,TASK_TYPE,DATA_ID) ");
                insertSql.append(" values('" + UUID.uuid32() + "','ACCOUNT','" + validator.getBusinessId() + "','" + validator.getPriceArea() + "','" + validator.getStartDate() + "'," + validator.getChangePrice() + ",'" + validator.getCreatedBy() + "',now(),'" + validator.getAreaType() + "','1','1','"+validator.getId()+"')");
                sqlList.add(insertSql.toString());

                //更新当前价格
                String updatePrice = "update account_finance_info set CARRIER_PRICE=" + validator.getChangePrice() + ",UPDATED_TIME = now(),UPDATED_BY='" + validator.getCreatedBy() + "' where id = '" + validator.getId() + "'";
                sqlList.add(updatePrice);

                //更新通道历史价格中的，价格
                String updateChannelHistoryPrice = "update account_price_history set CARRIER_PRICE=" + validator.getChangePrice() + ",UPDATED_TIME = now(),UPDATED_BY='" + validator.getCreatedBy() + "' where ACCOUNT_ID='" + validator.getBusinessId() + "' and CARRIER ='" + validator.getPriceArea() + "' and PRICE_DATE>='" + validator.getStartDate() + "' ";
                sqlList.add(updateChannelHistoryPrice);

                String updateMessageDailyStatisticsChannelPrice = "update message_daily_statistics set ACCOUNT_PRICE=" + validator.getChangePrice() + ",UPDATED_TIME = now(),UPDATED_BY='" + validator.getCreatedBy() + "' where BUSINESS_ACCOUNT='" + validator.getBusinessId() + "' and PRICE_AREA_CODE ='" + validator.getPriceArea() + "' and MESSAGE_DATE>='" + validator.getStartDate() + "' ";
                sqlList.add(updateMessageDailyStatisticsChannelPrice);
            }

            //log.info("[业务账号历史价格修改]：{}", sqlList.toString());
            //根据参数个数，组织参数值
            String[] params = new String[sqlList.size()];
            sqlList.toArray(params);
            jdbcTemplate.batchUpdate(params);
        }
    }
}
