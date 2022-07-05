package com.smoc.cloud.scheduler.initialize.repository;

import com.smoc.cloud.scheduler.initialize.entity.AccountBaseInfo;
import com.smoc.cloud.scheduler.initialize.rowmapper.AccountBaseInfoRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AccountRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

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
