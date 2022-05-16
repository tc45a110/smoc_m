package com.smoc.cloud.system.rowmapper;

import com.smoc.cloud.common.smoc.system.SystemAccountInfoValidator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SystemAccountInfoValidatorRowMapper implements RowMapper<SystemAccountInfoValidator> {
    @Override
    public SystemAccountInfoValidator mapRow(ResultSet resultSet, int i) throws SQLException {
        SystemAccountInfoValidator qo = new SystemAccountInfoValidator();
        qo.setId(resultSet.getString("ID"));
        qo.setAccount(resultSet.getString("ACCOUNT"));
        qo.setEnterpriseName(resultSet.getString("ENTERPRISE_NAME"));
        qo.setEnterpriseId(resultSet.getString("ENTERPRISE_ID"));
        qo.setMd5HmacKey(resultSet.getString("MD5_HMAC_KEY"));
        qo.setAesKey(resultSet.getString("AES_KEY"));
        qo.setAesIv(resultSet.getString("AES_IV"));
        BigDecimal price = resultSet.getBigDecimal("PRICE");
        if(!StringUtils.isEmpty(price)){
            qo.setPrice(new BigDecimal(price.stripTrailingZeros().toPlainString()));
        }else{
            qo.setPrice(price);
        }
        BigDecimal secondPrice = resultSet.getBigDecimal("SECOND_PRICE");
        if(!StringUtils.isEmpty(secondPrice)){
            qo.setSecondPrice(new BigDecimal(secondPrice.stripTrailingZeros().toPlainString()));
        }else{
            qo.setSecondPrice(secondPrice);
        }
        BigDecimal thirdPrice = resultSet.getBigDecimal("THIRD_PRICE");
        if(!StringUtils.isEmpty(thirdPrice)){
            qo.setThirdPrice(new BigDecimal(thirdPrice.stripTrailingZeros().toPlainString()));
        }else{
            qo.setThirdPrice(thirdPrice);
        }
        BigDecimal grantingCredit = resultSet.getBigDecimal("GRANTING_CREDIT");
        if(!StringUtils.isEmpty(grantingCredit)){
            qo.setGrantingCredit(new BigDecimal(grantingCredit.stripTrailingZeros().toPlainString()));
        }else{
            qo.setGrantingCredit(grantingCredit);
        }
        qo.setAccountType(resultSet.getString("ACCOUNT_TYPE"));
        qo.setBusinessType(resultSet.getString("BUSINESS_TYPE"));
        qo.setIsGateway(resultSet.getString("IS_GATEWAY"));
        qo.setAccountStatus(resultSet.getString("ACCOUNT_STATUS"));
        qo.setCreatedBy(resultSet.getString("CREATED_BY"));
        qo.setCreatedTime(resultSet.getString("CREATED_TIME"));
        return qo;
    }
}
