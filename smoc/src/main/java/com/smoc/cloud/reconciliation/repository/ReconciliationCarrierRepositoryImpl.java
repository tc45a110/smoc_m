package com.smoc.cloud.reconciliation.repository;

import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.reconciliation.ReconciliationCarrierItemsValidator;
import com.smoc.cloud.common.smoc.reconciliation.model.ReconciliationChannelCarrierModel;
import com.smoc.cloud.reconciliation.rowmapper.ReconciliationCarrierRowMapper;
import com.smoc.cloud.reconciliation.rowmapper.ReconciliationChannelRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

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
        sqlBuffer.append(" a.MESSAGE_DATE,");
        sqlBuffer.append(" a.CHANNEL_PROVDER,");
        sqlBuffer.append(" IFNULL(b.CHANNEL_PERIOD_STATUS,0)CHANNEL_PERIOD_STATUS");
        sqlBuffer.append(" from (select LEFT (t.MESSAGE_DATE, 7)MESSAGE_DATE,t.CHANNEL_PROVDER from view_reconciliation_carrier t group by LEFT (t.MESSAGE_DATE, 7),t.CHANNEL_PROVDER order by LEFT (t.MESSAGE_DATE, 7) desc )a ");
        sqlBuffer.append(" left join ");
        sqlBuffer.append(" (select LEFT (t.CHANNEL_PERIOD, 7)MESSAGE_DATE,t.CHANNEL_PROVDER,t.CHANNEL_PERIOD_STATUS from reconciliation_carrier_items t group by LEFT (t.CHANNEL_PERIOD, 7),t.CHANNEL_PROVDER,t.CHANNEL_PERIOD_STATUS order by LEFT (t.CHANNEL_PERIOD, 7) desc )b ");
        sqlBuffer.append(" on a.MESSAGE_DATE=b.MESSAGE_DATE and a.CHANNEL_PROVDER=b.CHANNEL_PROVDER ");
        sqlBuffer.append(" where 1=1 ");

        List<Object> paramsList = new ArrayList<Object>();

        //账期
        if (!StringUtils.isEmpty(qo.getChannelPeriod())) {
            sqlBuffer.append(" and a.MESSAGE_DATE =?");
            paramsList.add(qo.getChannelPeriod().trim());
        }
        //账期类型
        if (!StringUtils.isEmpty(qo.getChannelProvder())) {
            sqlBuffer.append(" and a.CHANNEL_PROVDER =?");
            paramsList.add(qo.getChannelProvder().trim());
        }

        //有效状态
        if (!StringUtils.isEmpty(qo.getChannelPeriodStatus())) {
            sqlBuffer.append(" and t.CHANNEL_PERIOD_STATUS =?");
            paramsList.add(qo.getChannelPeriodStatus().trim());
        }

        sqlBuffer.append(" order by a.MESSAGE_DATE desc,a.CHANNEL_PROVDER ");
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
        sqlBuffer.append(" t.MESSAGE_DATE,");
        sqlBuffer.append(" t.CHANNEL_PROVDER,");
        sqlBuffer.append(" t.CHANNEL_ID,");
        sqlBuffer.append(" t.SRC_ID,");
        sqlBuffer.append(" t.CHANNEL_PRICE, ");
        sqlBuffer.append(" t.BUSINESS_TYPE, ");
        sqlBuffer.append(" t.MESSAGE_SUCCESS_NUM,");
        sqlBuffer.append(" a.CARRIER_TOTAL_AMOUNT,");
        sqlBuffer.append(" a.CARRIER_TOTAL_SEND_QUANTITY,");
        sqlBuffer.append(" a.CHANNEL_PERIOD_STATUS");
        sqlBuffer.append(" from view_reconciliation_carrier t left join reconciliation_carrier_items a  ");
        sqlBuffer.append(" on t.MESSAGE_DATE=a.CHANNEL_PERIOD and t.CHANNEL_PROVDER=a.CHANNEL_PROVDER and t.CHANNEL_ID=a.CHANNEL_ID ");
        sqlBuffer.append(" where t.MESSAGE_DATE =? and t.CHANNEL_PROVDER =? ");

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
