package com.smoc.cloud.scheduler.initialize.repository;

import com.smoc.cloud.scheduler.initialize.entity.AccountBaseInfo;
import com.smoc.cloud.scheduler.initialize.entity.AccountFinanceInfo;
import com.smoc.cloud.scheduler.initialize.entity.AccountMessagePrice;
import com.smoc.cloud.scheduler.initialize.model.MessagePriceBusinessModel;
import com.smoc.cloud.scheduler.initialize.rowmapper.AccountBaseInfoRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AccountRepository {

    @Autowired
    public DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    /**
     * 加载业务账号短信价格信息
     *
     * @return
     */
    public Map<String, MessagePriceBusinessModel> getAccountMessagePrice() {
        Map<String, MessagePriceBusinessModel> resultMap = new HashMap<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuffer sql = new StringBuffer("select ");
        sql.append(" base.ACCOUNT_ID,");
        sql.append(" base.PAY_TYPE,");
        sql.append(" base.CHARGE_TYPE,");
        sql.append(" base.CARRIER_TYPE,");
        sql.append(" base.CARRIER,");
        sql.append(" base.CARRIER_PRICE");
        sql.append(" from smoc.account_finance_info base ");
        try {
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(sql.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                AccountMessagePrice qo = new AccountMessagePrice();
                qo.setAccountId(rs.getString("ACCOUNT_ID"));
                qo.setPayType(rs.getString("PAY_TYPE"));
                qo.setChargeType(rs.getString("CHARGE_TYPE"));
                qo.setCarrierType(rs.getString("CARRIER_TYPE"));
                qo.setCarrier(rs.getString("CARRIER"));
                qo.setCarrierPrice(rs.getBigDecimal("CARRIER_PRICE"));
                MessagePriceBusinessModel businessModel = resultMap.get(qo.getAccountId());
                if (null == businessModel) {
                    businessModel = new MessagePriceBusinessModel();
                }
                businessModel.add(qo);
                resultMap.put(qo.getAccountId(), businessModel);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != conn) {
                    conn.close();
                }
                if (null != pstmt) {
                    pstmt.close();
                }
                if (null != rs) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultMap;
    }

    /**
     * 加载业务账号财务信息
     *
     * @return
     */
    public Map<String, AccountFinanceInfo> getAccountFinanceInfo() {
        Map<String, AccountFinanceInfo> resultMap = new HashMap<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuffer sql = new StringBuffer("select ");
        sql.append(" base.ACCOUNT_ID,");
        sql.append(" base.ACCOUNT_USABLE_SUM,");
        sql.append(" base.ACCOUNT_CREDIT_SUM,");
        sql.append(" base.IS_SHARE,");
        sql.append(" share.SHARE_ACCOUNT_ID");
        sql.append(" from smoc.finance_account base LEFT JOIN smoc.finance_account_share_detail share");
        sql.append(" on base.IS_SHARE = '1' and share.SHARE_STATUS='1' and base.ACCOUNT_ID = share.ACCOUNT_ID ");
        sql.append(" where base.ACCOUNT_STATUS = '1' ");
        try {
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(sql.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                AccountFinanceInfo qo = new AccountFinanceInfo();
                qo.setAccountId(rs.getString("ACCOUNT_ID"));
                qo.setAccountUsableSum(rs.getBigDecimal("ACCOUNT_USABLE_SUM"));
                qo.setAccountCreditSum(rs.getBigDecimal("ACCOUNT_CREDIT_SUM"));
                qo.setIsShare(rs.getString("IS_SHARE"));
                qo.setShareId(rs.getString("SHARE_ACCOUNT_ID"));
                resultMap.put(qo.getAccountId(), qo);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != conn) {
                    conn.close();
                }
                if (null != pstmt) {
                    pstmt.close();
                }
                if (null != rs) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultMap;
    }

    /**
     * 加载业务账号基本信息
     *
     * @return
     */
    public Map<String, AccountBaseInfo> getAccountBaseInfo() {
        Map<String, AccountBaseInfo> resultMap = new HashMap<>();
        StringBuffer sql = new StringBuffer("SELECT ");
        sql.append(" base.ACCOUNT_ID,");
        sql.append(" ent.ENTERPRISE_FLAG,");
        sql.append(" base.ACCOUNT_NAME,");
        sql.append(" base.ACCOUNT_PRIORITY,");
        sql.append(" base.TRANSFER_TYPE,");
        sql.append(" base.BUSINESS_TYPE,");
        sql.append(" base.INFO_TYPE,");
        sql.append(" base.INDUSTRY_TYPE,");
        sql.append(" base.CARRIER,");
        sql.append(" base.EXTEND_CODE,");
        sql.append(" base.CONSUME_TYPE ");
        sql.append(" from smoc.account_base_info base,smoc.enterprise_basic_info ent ");
        sql.append(" where base.ENTERPRISE_ID = ent.ENTERPRISE_ID ");
        List<AccountBaseInfo> result = this.jdbcTemplate.query(sql.toString(), new AccountBaseInfoRowMapper());
        if (null == result || result.size() < 1) {
            return resultMap;
        }
        for (AccountBaseInfo baseInfo : result) {
            resultMap.put(baseInfo.getAccountId(), baseInfo);
        }
        return resultMap;
    }
}
