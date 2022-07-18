package com.smoc.cloud.iot.product.rowmapper;

import com.smoc.cloud.common.iot.validator.IotProductInfoValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IotProductInfoRowMapper implements RowMapper<IotProductInfoValidator> {
    @Override
    public IotProductInfoValidator mapRow(ResultSet rs, int rowNum) throws SQLException {
        IotProductInfoValidator qo = new IotProductInfoValidator();
        qo.setId(rs.getString("ID"));
        qo.setProductName(rs.getString("PRODUCT_NAME"));
        qo.setProductType(rs.getString("PRODUCT_TYPE"));
        qo.setCardsChanging(rs.getBigDecimal("CARDS_CHANGING"));
        qo.setProductPoolSize(rs.getBigDecimal("PRODUCT_POOL_SIZE"));
        qo.setChangingCycle(rs.getString("CHANGING_CYCLE"));
        qo.setCycleQuota(rs.getBigDecimal("CYCLE_QUOTA"));
        qo.setAboveQuotaChanging(rs.getBigDecimal("ABOVE_QUOTA_CHANGING"));
        qo.setProductCardsNum(rs.getInt("PRODUCT_CARDS_NUM"));
        qo.setRemark(rs.getString("REMARK"));
        qo.setUseStatus(rs.getString("USE_STATUS"));
        qo.setProductStatus(rs.getString("PRODUCT_STATUS"));
        qo.setCreatedBy(rs.getString("CREATED_BY"));
        qo.setCreatedTime(rs.getString("CREATED_TIME"));
        return qo;
    }
}
