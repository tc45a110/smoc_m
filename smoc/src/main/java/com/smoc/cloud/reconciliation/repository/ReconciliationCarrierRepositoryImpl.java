package com.smoc.cloud.reconciliation.repository;

import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.message.MessageComplaintInfoValidator;
import com.smoc.cloud.common.smoc.message.model.ComplaintExcelModel;
import com.smoc.cloud.common.smoc.reconciliation.ReconciliationCarrierItemsValidator;
import com.smoc.cloud.common.smoc.reconciliation.model.ReconciliationChannelCarrierModel;
import com.smoc.cloud.common.smoc.utils.ChannelUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.reconciliation.rowmapper.ReconciliationCarrierRowMapper;
import com.smoc.cloud.reconciliation.rowmapper.ReconciliationChannelRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ReconciliationCarrierRepositoryImpl extends BasePageRepository {

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public PageList<ReconciliationChannelCarrierModel> page(PageParams<ReconciliationChannelCarrierModel> pageParams) {
        //查询条件
        ReconciliationChannelCarrierModel qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
       /* sqlBuffer.append(" a.MESSAGE_DATE,");
        sqlBuffer.append(" a.CHANNEL_PROVDER,");
        sqlBuffer.append(" IFNULL(b.CHANNEL_PERIOD_STATUS,4)CHANNEL_PERIOD_STATUS");
        sqlBuffer.append(" from (select LEFT (t.MESSAGE_DATE, 7)MESSAGE_DATE,t.CHANNEL_PROVDER from view_reconciliation_carrier t group by LEFT (t.MESSAGE_DATE, 7),t.CHANNEL_PROVDER order by LEFT (t.MESSAGE_DATE, 7) desc )a ");
        sqlBuffer.append(" left join ");
        sqlBuffer.append(" (select LEFT (t.CHANNEL_PERIOD, 7)MESSAGE_DATE,t.CHANNEL_PROVDER,t.CHANNEL_PERIOD_STATUS from reconciliation_carrier_items t group by LEFT (t.CHANNEL_PERIOD, 7),t.CHANNEL_PROVDER,t.CHANNEL_PERIOD_STATUS order by LEFT (t.CHANNEL_PERIOD, 7) desc )b ");
        sqlBuffer.append(" on a.MESSAGE_DATE=b.MESSAGE_DATE and a.CHANNEL_PROVDER=b.CHANNEL_PROVDER ");
        sqlBuffer.append(" where a.CHANNEL_PROVDER is not null ");*/
        sqlBuffer.append(" LEFT (t.MESSAGE_DATE, 7) MESSAGE_DATE,");
        sqlBuffer.append(" a.SPECIFIC_PROVDER CHANNEL_PROVDER,");
        sqlBuffer.append(" IFNULL(b.CHANNEL_PERIOD_STATUS,4)CHANNEL_PERIOD_STATUS");
        sqlBuffer.append(" FROM message_daily_statistics t ");
        sqlBuffer.append(" left join config_channel_basic_info a on t.CHANNEL_ID = a.CHANNEL_ID ");
        sqlBuffer.append(" left join (select t.CHANNEL_PERIOD,t.CHANNEL_PROVDER,t.CHANNEL_PERIOD_STATUS from reconciliation_carrier_items t ");
        sqlBuffer.append(" where t.CHANNEL_PERIOD =? group by t.CHANNEL_PERIOD,t.CHANNEL_PROVDER,t.CHANNEL_PERIOD_STATUS)b ");
        sqlBuffer.append(" on LEFT (t.MESSAGE_DATE, 7) = b.CHANNEL_PERIOD and a.SPECIFIC_PROVDER = b.CHANNEL_PROVDER ");
        sqlBuffer.append(" where LEFT (t.MESSAGE_DATE, 7) =? and a.CHANNEL_PROVDER is not null  ");

        List<Object> paramsList = new ArrayList<Object>();

        //账期
        if (!StringUtils.isEmpty(qo.getChannelPeriod())) {
            paramsList.add(qo.getChannelPeriod().trim());
            paramsList.add(qo.getChannelPeriod().trim());
        }
        //账期类型
        if (!StringUtils.isEmpty(qo.getChannelProvder())) {
            sqlBuffer.append(" and a.SPECIFIC_PROVDER =?");
            paramsList.add(qo.getChannelProvder().trim());
        }

        //有效状态
        if (!StringUtils.isEmpty(qo.getChannelPeriodStatus())) {
            sqlBuffer.append(" and b.CHANNEL_PERIOD_STATUS =?");
            paramsList.add(qo.getChannelPeriodStatus().trim());
        }

        sqlBuffer.append(" GROUP BY LEFT (t.MESSAGE_DATE, 7),a.SPECIFIC_PROVDER order by LEFT (t.MESSAGE_DATE, 7) desc,IFNULL(b.CHANNEL_PERIOD_STATUS,4) desc,a.SPECIFIC_PROVDER ");
        //log.info("[SQL]:{}",sqlBuffer);
        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);
        //log.info("[SQL1]:{}",sqlBuffer);
        PageList<ReconciliationChannelCarrierModel> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new ReconciliationChannelRowMapper());
        pageList.getPageParams().setParams(qo);

        List<ReconciliationChannelCarrierModel> list = pageList.getList();
        if(null != pageList.getList() && pageList.getList().size()>0){
            for(ReconciliationChannelCarrierModel model :list){
                List<ReconciliationCarrierItemsValidator> carrierList = findReconciliationCarrier(model.getChannelPeriod(),model.getChannelProvder());
                if(null != carrierList && carrierList.size()>0){
                    model.setRowspan(carrierList.size());
                }else{
                    model.setRowspan(1);
                }
                model.setCarrierList(carrierList);
            }
        }

        return pageList;
    }

    public List<ReconciliationCarrierItemsValidator> findReconciliationCarrier(String channelPeriod, String channelProvder) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        /*sqlBuffer.append(" t.MESSAGE_DATE,");
        sqlBuffer.append(" t.CHANNEL_PROVDER,");
        sqlBuffer.append(" t.CHANNEL_ID,");
        sqlBuffer.append(" t.SRC_ID,");
        sqlBuffer.append(" IFNULL(t.CHANNEL_PRICE,0)CHANNEL_PRICE, ");
        sqlBuffer.append(" t.BUSINESS_TYPE, ");
        sqlBuffer.append(" t.MESSAGE_SUCCESS_NUM,");
        sqlBuffer.append(" a.CARRIER_TOTAL_AMOUNT,");
        sqlBuffer.append(" a.CARRIER_TOTAL_SEND_QUANTITY,");
        sqlBuffer.append(" a.CHANNEL_PERIOD_STATUS");
        sqlBuffer.append(" from view_reconciliation_carrier t left join reconciliation_carrier_items a  ");
        sqlBuffer.append(" on t.MESSAGE_DATE=a.CHANNEL_PERIOD and t.CHANNEL_PROVDER=a.CHANNEL_PROVDER and t.CHANNEL_ID=a.CHANNEL_ID and t.BUSINESS_TYPE=a.BUSINESS_TYPE and t.CHANNEL_PRICE=a.PRICE");
        sqlBuffer.append(" where t.MESSAGE_DATE =? and t.CHANNEL_PROVDER =? and (a.STATUS=1 or a.STATUS is null)");*/
        sqlBuffer.append(" a.MESSAGE_DATE,");
        sqlBuffer.append(" b.SPECIFIC_PROVDER CHANNEL_PROVDER,");
        sqlBuffer.append(" a.CHANNEL_ID,");
        sqlBuffer.append(" c.SRC_ID,");
        sqlBuffer.append(" IFNULL(a.CHANNEL_PRICE,0)CHANNEL_PRICE,");
        sqlBuffer.append(" a.BUSINESS_TYPE,");
        sqlBuffer.append(" a.MESSAGE_SUCCESS_NUM,");
        sqlBuffer.append(" d.CARRIER_TOTAL_AMOUNT,");
        sqlBuffer.append(" d.CARRIER_TOTAL_SEND_QUANTITY,");
        sqlBuffer.append(" d.CHANNEL_PERIOD_STATUS");
        sqlBuffer.append(" from( SELECT LEFT (t.MESSAGE_DATE, 7)MESSAGE_DATE, t.CHANNEL_ID, IFNULL(t.CHANNEL_PRICE, 0) CHANNEL_PRICE, t.BUSINESS_TYPE, sum(t.MESSAGE_SUCCESS_NUM)MESSAGE_SUCCESS_NUM FROM message_daily_statistics t ");
        sqlBuffer.append("  where LEFT (t.MESSAGE_DATE, 7) =? group by LEFT (t.MESSAGE_DATE, 7), t.CHANNEL_ID, t.CHANNEL_PRICE, t.BUSINESS_TYPE )a ");
        sqlBuffer.append(" left join config_channel_basic_info b on a.CHANNEL_ID = b.CHANNEL_ID left join config_channel_interface c on a.CHANNEL_ID = c.CHANNEL_ID ");
        sqlBuffer.append(" LEFT JOIN reconciliation_carrier_items d ON a.MESSAGE_DATE = d.CHANNEL_PERIOD AND b.SPECIFIC_PROVDER = d.CHANNEL_PROVDER AND a.CHANNEL_ID = d.CHANNEL_ID ");
        sqlBuffer.append(" AND a.BUSINESS_TYPE = d.BUSINESS_TYPE AND a.CHANNEL_PRICE = d.PRICE");
        sqlBuffer.append(" where b.SPECIFIC_PROVDER = ? and (d.STATUS=1 or d.STATUS is null) ");


        List<Object> paramsList = new ArrayList<Object>();
        paramsList.add(channelPeriod.trim());
        paramsList.add(channelProvder.trim());

        sqlBuffer.append(" order by a.CHANNEL_ID ");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        List<ReconciliationCarrierItemsValidator> list = this.queryForObjectList(sqlBuffer.toString(), params, new ReconciliationCarrierRowMapper());
        return list;
    }

    public void batchSave(ReconciliationChannelCarrierModel reconciliationChannelCarrierModel) {

        Connection connection = null;
        PreparedStatement statement = null;
        List<ReconciliationCarrierItemsValidator> list= reconciliationChannelCarrierModel.getCarrierList();
        final String sql = "insert into reconciliation_carrier_items(ID,CHANNEL_PERIOD,CHANNEL_PROVDER,CHANNEL_ID,SRC_ID,BUSINESS_TYPE,TOTAL_SEND_QUANTITY,TOTAL_SUBMIT_QUANTITY,TOTAL_AMOUNT,TOTAL_NO_REPORT_QUANTITY,PRICE," +
                "CARRIER_TOTAL_AMOUNT,CARRIER_TOTAL_SEND_QUANTITY,CHANNEL_PERIOD_STATUS,STATUS,CREATED_BY,CREATED_TIME) " +
                " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now()) ";
        try {
            connection = jdbcTemplate.getDataSource().getConnection();
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(sql);
            for (ReconciliationCarrierItemsValidator entry : list) {
                statement.setString(1, UUID.uuid32());
                statement.setString(2, entry.getChannelPeriod());
                statement.setString(3, entry.getChannelProvder());
                statement.setString(4, entry.getChannelId());
                statement.setString(5, entry.getSrcId());
                statement.setString(6, entry.getBusinessType());
                statement.setLong(7, entry.getTotalSendQuantity());
                statement.setLong(8, entry.getTotalSubmitQuantity());
                statement.setBigDecimal(9, new BigDecimal(entry.getTotalSendQuantity()).multiply(entry.getPrice()));
                statement.setLong(10, entry.getTotalNoReportQuantity());
                statement.setBigDecimal(11, entry.getPrice());
                statement.setBigDecimal(12, entry.getCarrierTotalAmount());
                statement.setLong(13, entry.getCarrierTotalSendQuantity());
                statement.setString(14, entry.getChannelPeriodStatus());
                statement.setString(15, "1");
                statement.setString(16, entry.getCreatedBy());

                statement.addBatch();
            }
            statement.executeBatch();
            connection.commit();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != statement) {
                    statement.close();
                }
                if (null != connection) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public PageList<ReconciliationChannelCarrierModel> reconciliationCarrierRecord(PageParams<ReconciliationChannelCarrierModel> pageParams) {

        //查询条件
        ReconciliationChannelCarrierModel qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append(" t.CHANNEL_PERIOD MESSAGE_DATE,");
        sqlBuffer.append(" t.CHANNEL_PROVDER,");
        sqlBuffer.append(" t.CHANNEL_PERIOD_STATUS");
        sqlBuffer.append(" from reconciliation_carrier_items t where t.STATUS=1 ");

        List<Object> paramsList = new ArrayList<Object>();

        //账期
        if (!StringUtils.isEmpty(qo.getChannelPeriod())) {
            sqlBuffer.append(" and t.CHANNEL_PERIOD =?");
            paramsList.add(qo.getChannelPeriod().trim());
        }
        //账期类型
        if (!StringUtils.isEmpty(qo.getChannelProvder())) {
            sqlBuffer.append(" and t.CHANNEL_PROVDER =?");
            paramsList.add(qo.getChannelProvder().trim());
        }

        sqlBuffer.append("  group by t.CHANNEL_PERIOD,t.CHANNEL_PROVDER order by t.CHANNEL_PERIOD desc,t.CHANNEL_PROVDER ");
        //log.info("[SQL]:{}",sqlBuffer);
        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);
        //log.info("[SQL1]:{}",sqlBuffer);
        PageList<ReconciliationChannelCarrierModel> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new ReconciliationChannelRowMapper());
        pageList.getPageParams().setParams(qo);

        List<ReconciliationChannelCarrierModel> list = pageList.getList();
        if(null != pageList.getList() && pageList.getList().size()>0){
            for(ReconciliationChannelCarrierModel model :list){
                List<ReconciliationCarrierItemsValidator> carrierList = findReconciliationCarrierRecord(model.getChannelPeriod(),model.getChannelProvder());
                if(null != carrierList && carrierList.size()>0){
                    model.setRowspan(carrierList.size());
                }else{
                    model.setRowspan(1);
                }
                model.setCarrierList(carrierList);
            }
        }

        return pageList;

    }

    public List<ReconciliationCarrierItemsValidator> findReconciliationCarrierRecord(String channelPeriod, String channelProvder) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append(" t.CHANNEL_PERIOD MESSAGE_DATE,");
        sqlBuffer.append(" t.CHANNEL_PROVDER,");
        sqlBuffer.append(" t.CHANNEL_ID,");
        sqlBuffer.append(" t.SRC_ID,");
        sqlBuffer.append(" IFNULL(t.PRICE,0)CHANNEL_PRICE, ");
        sqlBuffer.append(" t.BUSINESS_TYPE, ");
        sqlBuffer.append(" t.TOTAL_SEND_QUANTITY MESSAGE_SUCCESS_NUM,");
        sqlBuffer.append(" t.CARRIER_TOTAL_AMOUNT,");
        sqlBuffer.append(" t.CARRIER_TOTAL_SEND_QUANTITY,");
        sqlBuffer.append(" t.CHANNEL_PERIOD_STATUS");
        sqlBuffer.append(" from reconciliation_carrier_items t  ");
        sqlBuffer.append(" where t.CHANNEL_PERIOD =? and t.CHANNEL_PROVDER =? and t.STATUS=1 ");

        List<Object> paramsList = new ArrayList<Object>();
        paramsList.add(channelPeriod.trim());
        paramsList.add(channelProvder.trim());

        sqlBuffer.append(" order by t.CHANNEL_ID ");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        List<ReconciliationCarrierItemsValidator> list = this.queryForObjectList(sqlBuffer.toString(), params, new ReconciliationCarrierRowMapper());
        return list;
    }
}
