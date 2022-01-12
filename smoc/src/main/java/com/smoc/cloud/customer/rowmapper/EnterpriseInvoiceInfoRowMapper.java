package com.smoc.cloud.customer.rowmapper;

import com.smoc.cloud.common.smoc.customer.validator.EnterpriseInvoiceInfoValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 复杂查询对象封装
 * 2020/5/9 18:59
 **/
public class EnterpriseInvoiceInfoRowMapper implements RowMapper<EnterpriseInvoiceInfoValidator> {

    @Override
    public EnterpriseInvoiceInfoValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        EnterpriseInvoiceInfoValidator qo = new EnterpriseInvoiceInfoValidator();
        qo.setId(resultSet.getString("ID"));
        qo.setEnterpriseId(resultSet.getString("ENTERPRISE_ID"));
        qo.setInvoiceType(resultSet.getString("INVOICE_TYPE"));
        qo.setInvoiceTitle(resultSet.getString("INVOICE_TITLE"));
        qo.setTaxPayerNumber(resultSet.getString("TAX_PAYER_NUMBER"));
        qo.setOpenBank(resultSet.getString("OPEN_BANK"));
        qo.setOpenAccount(resultSet.getString("OPEN_ACCOUNT"));
        qo.setRegisterAddress(resultSet.getString("REGISTER_ADDRESS"));
        qo.setInvoiceMark(resultSet.getString("INVOICE_MARK"));
        qo.setInvoiceStatus(resultSet.getString("INVOICE_STATUS"));

        return qo;
    }
}
