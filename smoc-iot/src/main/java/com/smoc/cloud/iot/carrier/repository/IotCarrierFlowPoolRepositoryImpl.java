package com.smoc.cloud.iot.carrier.repository;

import com.smoc.cloud.common.iot.validator.IotCarrierFlowPoolValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.iot.carrier.rowmapper.IotCarrierFlowPoolRowMapper;
import com.smoc.cloud.iot.common.BasePageRepository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class IotCarrierFlowPoolRepositoryImpl extends BasePageRepository {


    public PageList<IotCarrierFlowPoolValidator> page(PageParams<IotCarrierFlowPoolValidator> pageParams) {

        //查询条件
        IotCarrierFlowPoolValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ID");
        sqlBuffer.append(", t.CARRIER_ID");
        sqlBuffer.append(", t.POOL_NAME");
        sqlBuffer.append(", t.POOL_CARD_NUMBER");
        sqlBuffer.append(", t.POOL_SIZE");
        sqlBuffer.append(", t.SYNC_DATE");
        sqlBuffer.append(", t.CONTINUE_TYPE");
        sqlBuffer.append(", t.POOL_STATUS");
        sqlBuffer.append(", t.CREATED_BY");
        sqlBuffer.append(", DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME");
        sqlBuffer.append("  from iot_carrier_flow_pool t where 1=1 ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getCarrierId())) {
            sqlBuffer.append(" and t.CARRIER_ID = ?");
            paramsList.add(qo.getCarrierId().trim());
        }

        if (!StringUtils.isEmpty(qo.getPoolName())) {
            sqlBuffer.append(" and t.POOL_NAME = ?");
            paramsList.add(qo.getPoolName().trim());
        }

        if (!StringUtils.isEmpty(qo.getContinueType())) {
            sqlBuffer.append(" and t.CONTINUE_TYPE = ?");
            paramsList.add(qo.getContinueType().trim());
        }

        if (!StringUtils.isEmpty(qo.getSyncDate())) {
            sqlBuffer.append(" and t.SYNC_DATE = ?");
            paramsList.add(qo.getSyncDate().trim());
        }

        if (!StringUtils.isEmpty(qo.getPoolStatus())) {
            sqlBuffer.append(" and t.POOL_STATUS = ?");
            paramsList.add(qo.getPoolStatus().trim());
        }

        sqlBuffer.append(" order by t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<IotCarrierFlowPoolValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new IotCarrierFlowPoolRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;
    }
}
