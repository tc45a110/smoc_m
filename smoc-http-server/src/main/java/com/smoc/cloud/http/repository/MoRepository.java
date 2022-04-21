package com.smoc.cloud.http.repository;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.http.entity.MobileOriginalAccount;
import com.smoc.cloud.http.rowmapper.MobileOriginalAccountRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 上行短信推送
 */
@Slf4j
@Service
public class MoRepository extends BasePageRepository {


    /**
     * 获取开通 https服务的 业务账号
     * @return
     */
    public List<MobileOriginalAccount> getMobileOriginalAccount() {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ACCOUNT_ID,");
        sqlBuffer.append("  t.MO_URL,");
        sqlBuffer.append("  t.STATUS_REPORT_URL  ");
        sqlBuffer.append("  from account_interface_info t  ");
        sqlBuffer.append("  where PROTOCOL='HTTPS' ");

        //log.info("[获取开通 https服务的 业务账号]:{}", sqlBuffer);

        PageList<MobileOriginalAccount> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), null, 0, 1000, new MobileOriginalAccountRowMapper());
        return pageList.getList();
    }
}
