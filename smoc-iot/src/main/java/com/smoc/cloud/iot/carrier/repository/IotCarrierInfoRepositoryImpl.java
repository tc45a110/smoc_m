package com.smoc.cloud.iot.carrier.repository;

import com.smoc.cloud.common.iot.validator.IotCarrierInfoValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.iot.carrier.rowmapper.IotCarrierInfoRowMapper;
import com.smoc.cloud.iot.common.BasePageRepository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class IotCarrierInfoRepositoryImpl extends BasePageRepository {

    public PageList<IotCarrierInfoValidator> page(PageParams<IotCarrierInfoValidator> pageParams) {

        //查询条件
        IotCarrierInfoValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ID");
        sqlBuffer.append(", t.CARRIER");
        sqlBuffer.append(", t.CARRIER_NAME");
        sqlBuffer.append(", t.CARRIER_IDENTIFYING");
        sqlBuffer.append(", t.CARRIER_USERNAME");
        sqlBuffer.append(", t.CARRIER_SERVER_URL");
        sqlBuffer.append(", t.API_TYPE");
        sqlBuffer.append(", t.CARRIER_STATUS");
        sqlBuffer.append(", t.CREATED_BY");
        sqlBuffer.append(", DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME");
        sqlBuffer.append("  from iot_carrier_info t where 1=1 ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getCarrier())) {
            sqlBuffer.append(" and t.CARRIER = ?");
            paramsList.add(qo.getCarrier().trim());
        }

        if (!StringUtils.isEmpty(qo.getCarrierName())) {
            sqlBuffer.append(" and t.CARRIER_NAME like ?");
            paramsList.add( "%"+qo.getCarrierName().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getCarrierIdentifying())) {
            sqlBuffer.append(" and t.CARRIER_IDENTIFYING = ?");
            paramsList.add(qo.getCarrierIdentifying().trim());
        }

        if (!StringUtils.isEmpty(qo.getCarrierUsername())) {
            sqlBuffer.append(" and t.CARRIER_USERNAME = ?");
            paramsList.add(qo.getCarrierUsername().trim());
        }

        if (!StringUtils.isEmpty(qo.getCarrierStatus())) {
            sqlBuffer.append(" and t.CARRIER_STATUS = ?");
            paramsList.add(qo.getCarrierStatus().trim());
        }

        sqlBuffer.append(" order by t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<IotCarrierInfoValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new IotCarrierInfoRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;
    }
}
