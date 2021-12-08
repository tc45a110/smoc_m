package com.smoc.cloud.configure.codenumber.repository;


import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.validator.CodeNumberInfoValidator;
import com.smoc.cloud.configure.codenumber.rowmapper.CodeNumberInfoRowMapper;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 2020/5/28 15:44
 **/
public class CodeNumberRepositoryImpl extends BasePageRepository {


    public PageList<CodeNumberInfoValidator> page(PageParams<CodeNumberInfoValidator> pageParams) {

        //查询条件
        CodeNumberInfoValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ID");
        sqlBuffer.append(", t.SRC_ID");
        sqlBuffer.append(", t.MAX_COMPLAINT_RATE");
        sqlBuffer.append(", t.CARRIER");
        sqlBuffer.append(", t.BUSINESS_TYPE");
        sqlBuffer.append(", t.SRC_ID_RPICE");
        sqlBuffer.append(", t.USE_TYPE");
        sqlBuffer.append(", t.SRC_ID_SOURCE");
        sqlBuffer.append(", t.ACCESS_POINT");
        sqlBuffer.append(", t.MIN_CONSUME_DEMAND");
        sqlBuffer.append(", t.CA_SRC_ID");
        sqlBuffer.append(", t.PROVINCE");
        sqlBuffer.append(", t.CITY");
        sqlBuffer.append(", t.SRC_ID_REMARK");
        sqlBuffer.append(", t.SRC_ID_STATUS");
        sqlBuffer.append(", DATE_FORMAT(t.PRICE_EFFECTIVE_DATE, '%Y-%m-%d')PRICE_EFFECTIVE_DATE");
        sqlBuffer.append(", DATE_FORMAT(t.ACCESS_TIME, '%Y-%m-%d')ACCESS_TIME");
        sqlBuffer.append(", DATE_FORMAT(t.MIN_CONSUME_EFFECTIVE_DATE, '%Y-%m-%d')MIN_CONSUME_EFFECTIVE_DATE");
        sqlBuffer.append(", DATE_FORMAT(t.SRC_ID_EFFECTIVE_DATE, '%Y-%m-%d')SRC_ID_EFFECTIVE_DATE");
        sqlBuffer.append(", t.CREATED_BY");
        sqlBuffer.append(", DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d')CREATED_TIME");
        sqlBuffer.append("  from config_number_code_info t where 1=1 ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getSrcId())) {
            sqlBuffer.append(" and t.SRC_ID like ?");
            paramsList.add( "%"+qo.getSrcId().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getCarrier())) {
            sqlBuffer.append(" and t.CARRIER = ?");
            paramsList.add(qo.getCarrier().trim());
        }

        if (!StringUtils.isEmpty(qo.getBusinessType())) {
            sqlBuffer.append(" and t.BUSINESS_TYPE = ?");
            paramsList.add(qo.getBusinessType().trim());
        }

        if (!StringUtils.isEmpty(qo.getSrcIdStatus())) {
            sqlBuffer.append(" and t.SRC_ID_STATUS = ?");
            paramsList.add(qo.getSrcIdStatus().trim());
        }

        if (!StringUtils.isEmpty(qo.getAccessPoint())) {
            sqlBuffer.append(" and t.ACCESS_POINT = ?");
            paramsList.add(qo.getAccessPoint().trim());
        }

        if (!StringUtils.isEmpty(qo.getSrcIdSource())) {
            sqlBuffer.append(" and t.SRC_ID_SOURCE = ?");
            paramsList.add(qo.getSrcIdSource().trim());
        }

        if (!StringUtils.isEmpty(qo.getProvince())) {
            sqlBuffer.append(" and t.PROVINCE = ?");
            paramsList.add(qo.getProvince().trim());
        }

        sqlBuffer.append(" order by t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<CodeNumberInfoValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new CodeNumberInfoRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;
    }

}
