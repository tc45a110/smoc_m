package com.smoc.cloud.iot.carrier.repository;

import com.smoc.cloud.common.iot.validator.IotFlowCardsChangeHistoryValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.iot.carrier.rowmapper.IotFlowCardsChangeHistoryRowMapper;
import com.smoc.cloud.iot.common.BasePageRepository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class IotFlowCardsChangeHistoryRepositoryImpl extends BasePageRepository {

    public PageList<IotFlowCardsChangeHistoryValidator> page(PageParams<IotFlowCardsChangeHistoryValidator> pageParams) {

        //查询条件
        IotFlowCardsChangeHistoryValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ID");
        sqlBuffer.append(", t.ICCID");
        sqlBuffer.append(", t.IMSI");
        sqlBuffer.append(", t.MSISDN");
        sqlBuffer.append(", t.ORIGINAL_STATUS");
        sqlBuffer.append(", t.TARGET_STATUS");
        sqlBuffer.append(", DATE_FORMAT(t.CHANGE_TIME, '%Y-%m-%d %H:%i:%S')CHANGE_TIME");
        sqlBuffer.append("  from iot_flow_cards_change_history t where 1=1 ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getIccid())) {
            sqlBuffer.append(" and t.ICCID = ?");
            paramsList.add(qo.getIccid().trim());
        }

        if (!StringUtils.isEmpty(qo.getImsi())) {
            sqlBuffer.append(" and t.IMSI = ?");
            paramsList.add(qo.getImsi().trim());
        }
        if (!StringUtils.isEmpty(qo.getMsisdn())) {
            sqlBuffer.append(" and t.MSISDN = ?");
            paramsList.add(qo.getMsisdn().trim());
        }
        sqlBuffer.append(" order by t.CHANGE_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<IotFlowCardsChangeHistoryValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new IotFlowCardsChangeHistoryRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;
    }
}
