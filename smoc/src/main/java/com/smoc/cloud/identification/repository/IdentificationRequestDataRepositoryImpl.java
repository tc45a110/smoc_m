package com.smoc.cloud.identification.repository;

import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.identification.validator.IdentificationRequestDataValidator;
import com.smoc.cloud.identification.rowmapper.IdentificationRequestDataRowMapper;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class IdentificationRequestDataRepositoryImpl  extends BasePageRepository {

    public PageList<IdentificationRequestDataValidator> page(PageParams<IdentificationRequestDataValidator> pageParams){

        //查询条件
        IdentificationRequestDataValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ID ");
        sqlBuffer.append("  t.IDENTIFICATION_ACCOUNT ");
        sqlBuffer.append("  e.ENTERPRISE_NAME ");
        sqlBuffer.append("  t.ORDER_NO ");
        sqlBuffer.append("  t.ORDER_TYPE ");
        sqlBuffer.append("  t.CREATED_BY ");
        sqlBuffer.append("  DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME ");
        sqlBuffer.append("  from identification_request_data t ,identification_account_info i,enterprise_basic_info e  ");
        sqlBuffer.append("  where t.IDENTIFICATION_ACCOUNT= i.IDENTIFICATION_ACCOUNT and i.ENTERPRISE_ID =e.ENTERPRISE_ID  ");

        List<Object> paramsList = new ArrayList<Object>();
        //企业名称
        if (!StringUtils.isEmpty(qo.getEnterpriseName())) {
            sqlBuffer.append(" and e.ENTERPRISE_NAME like ?");
            paramsList.add("%" + qo.getEnterpriseName().trim() + "%");
        }
        //订单号
        if (!StringUtils.isEmpty(qo.getOrderNo())) {
            sqlBuffer.append(" and t.ORDER_NO =?");
            paramsList.add(qo.getOrderNo().trim());
        }
        //订单类型
        if (!StringUtils.isEmpty(qo.getOrderType())) {
            sqlBuffer.append(" and t.ORDER_TYPE =?");
            paramsList.add(qo.getOrderType().trim());
        }

        sqlBuffer.append(" order by t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<IdentificationRequestDataValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new IdentificationRequestDataRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;

    }
}
