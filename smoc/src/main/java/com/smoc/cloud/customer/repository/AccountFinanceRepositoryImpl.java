package com.smoc.cloud.customer.repository;


import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelPriceValidator;
import com.smoc.cloud.common.smoc.customer.validator.AccountFinanceInfoValidator;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.configure.channel.rowmapper.ChannelPriceRowMapper;
import com.smoc.cloud.customer.rowmapper.AccountFinanceInfoRowMapper;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class AccountFinanceRepositoryImpl extends BasePageRepository {

    @Resource
    public JdbcTemplate jdbcTemplate;

    public List<AccountFinanceInfoValidator> findByAccountId(AccountFinanceInfoValidator accountFinanceInfoValidator) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ACCOUNT_ID");
        sqlBuffer.append(", t.PAY_TYPE");
        sqlBuffer.append(", t.CHARGE_TYPE");
        sqlBuffer.append(", t.FROZEN_RETURN_DATE");
        sqlBuffer.append(", t.ACCOUNT_CREDIT_SUM");
        sqlBuffer.append(", t.CARRIER");
        sqlBuffer.append(", t.CARRIER_PRICE");
        sqlBuffer.append(", t.CARRIER_TYPE");
        sqlBuffer.append("  from account_finance_info t where 1=1 ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(accountFinanceInfoValidator.getAccountId())) {
            sqlBuffer.append(" and t.ACCOUNT_ID = ?");
            paramsList.add( accountFinanceInfoValidator.getAccountId().trim());
        }

        sqlBuffer.append(" order by t.CARRIER ");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        List<AccountFinanceInfoValidator> list = this.queryForObjectList(sqlBuffer.toString(), params,  new AccountFinanceInfoRowMapper());
        return list;

    }

    public void deleteByAccountIdAndCarrier(String accountId, String carrier) {
        String[] carriers = carrier.split(",");

        StringBuilder sql = new StringBuilder("delete from account_finance_info where ACCOUNT_ID = '"+accountId+"' ");
        for(int i=0;i<carriers.length;i++){
            sql.append("and  CARRIER !='"+carriers[i]+"' ");
        }
        jdbcTemplate.execute(sql.toString());
    }

    public void batchSave(AccountFinanceInfoValidator accountFinanceInfoValidator) {

        List<AccountFinanceInfoValidator> list = accountFinanceInfoValidator.getPrices();

        final String sql = "insert into account_finance_info(ID,ACCOUNT_ID,PAY_TYPE,CHARGE_TYPE,FROZEN_RETURN_DATE,ACCOUNT_CREDIT_SUM,CARRIER_TYPE,CARRIER,CARRIER_PRICE,CREATED_BY,CREATED_TIME,UPDATED_BY,UPDATED_TIME) " +
                "values(?,?,?,?,?,?,?,?,?,?,now(),?,now()) ";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            public int getBatchSize() {
                return list.size();
            }

            public void setValues(PreparedStatement ps, int i) throws SQLException {
                AccountFinanceInfoValidator info = list.get(i);
                ps.setString(1, UUID.uuid32());
                ps.setString(2, accountFinanceInfoValidator.getAccountId());
                ps.setString(3, accountFinanceInfoValidator.getPayType());
                ps.setString(4, accountFinanceInfoValidator.getChargeType());
                ps.setString(5, accountFinanceInfoValidator.getFrozenReturnDate());
                ps.setBigDecimal(6, accountFinanceInfoValidator.getAccountCreditSum());
                ps.setString(7, accountFinanceInfoValidator.getCarrierType());
                ps.setString(8, info.getCarrier());
                ps.setBigDecimal(9, info.getCarrierPrice());
                ps.setString(10, accountFinanceInfoValidator.getCreatedBy());
                ps.setString(11, accountFinanceInfoValidator.getCreatedBy());
            }

        });
    }

}
