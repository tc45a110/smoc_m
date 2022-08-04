package com.smoc.cloud.iot.carrier.repository;

import com.smoc.cloud.api.response.flow.SimFlowUsedMonthlyResponse;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.iot.carrier.rowmapper.IotFlowCardsFlowMonthlyResponseRowMapper;
import com.smoc.cloud.iot.common.BasePageRepository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class IotFlowCardsFlowMonthlyRepositoryImpl extends BasePageRepository {

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public PageList<SimFlowUsedMonthlyResponse> page(String account, String iccid, String queryMonth, PageParams pageParams) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append(" t.ICCID");
        sqlBuffer.append(",t.PACKAGE_ID");
        sqlBuffer.append(",p.PACKAGE_NAME");
        sqlBuffer.append(",t.CHARGING_TYPE");
        sqlBuffer.append(",t.OPEN_CARD_FEE");
        sqlBuffer.append(",t.CYCLE_FUNCTION_FEE");
        sqlBuffer.append(",t.CYCLE_QUOTA");
        sqlBuffer.append(",t.USED_AMOUNT");
        sqlBuffer.append(",t.TOTAL_AMOUNT");
        sqlBuffer.append(",t.REMAIN_AMOUNT");
        sqlBuffer.append(",t.SETTLEMENT_FEE");
        sqlBuffer.append(",t.USED_MONTH");
        sqlBuffer.append(",t.SETTLEMENT_STATUS");
        sqlBuffer.append(",DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME");
        sqlBuffer.append(" from iot_flow_cards_flow_monthly t,iot_package_info p where p.ID=t.PACKAGE_ID  ");
        System.out.println(sqlBuffer);
        List<Object> paramsList = new ArrayList<Object>();
        if (!StringUtils.isEmpty(account)) {
            sqlBuffer.append(" and t.ACCOUNT=? ");
            paramsList.add(account);
        }
        if (!StringUtils.isEmpty(queryMonth)) {
            sqlBuffer.append(" and t.USED_MONTH=? ");
            paramsList.add(queryMonth);
        }
        if (!StringUtils.isEmpty(iccid)) {
            sqlBuffer.append(" and t.ICCID=? ");
            paramsList.add(iccid);
        }

        sqlBuffer.append(" order by t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<SimFlowUsedMonthlyResponse> page = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new IotFlowCardsFlowMonthlyResponseRowMapper());
        return page;
    }

}
