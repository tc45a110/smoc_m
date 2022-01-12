package com.smoc.cloud.customer.repository;


import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseInvoiceInfoValidator;
import com.smoc.cloud.customer.rowmapper.EnterpriseInvoiceInfoRowMapper;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 2020/5/28 15:44
 **/
public class EnterpriseInvoiceRepositoryImpl extends BasePageRepository {


    public List<EnterpriseInvoiceInfoValidator> page(EnterpriseInvoiceInfoValidator qo) {
        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ID");
        sqlBuffer.append(", t.ENTERPRISE_ID");
        sqlBuffer.append(", t.INVOICE_TYPE");
        sqlBuffer.append(", t.INVOICE_TITLE");
        sqlBuffer.append(", t.TAX_PAYER_NUMBER");
        sqlBuffer.append(", t.OPEN_BANK");
        sqlBuffer.append(", t.OPEN_ACCOUNT");
        sqlBuffer.append(", t.REGISTER_ADDRESS");
        sqlBuffer.append(", t.INVOICE_MARK");
        sqlBuffer.append(", t.INVOICE_STATUS");
        sqlBuffer.append("  from enterprise_invoice_info t  ");
        sqlBuffer.append("  where 1=1 ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getEnterpriseId())) {
            sqlBuffer.append(" and t.ENTERPRISE_ID =?");
            paramsList.add( qo.getEnterpriseId().trim());
        }

        sqlBuffer.append(" order by t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        List<EnterpriseInvoiceInfoValidator> list = this.queryForObjectList(sqlBuffer.toString(), params, new EnterpriseInvoiceInfoRowMapper());
        return list;
    }

}
