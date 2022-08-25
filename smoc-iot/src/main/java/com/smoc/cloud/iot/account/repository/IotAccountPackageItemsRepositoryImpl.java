package com.smoc.cloud.iot.account.repository;

import com.smoc.cloud.api.response.account.IotAccountPackageInfo;
import com.smoc.cloud.api.response.account.IotAccountPackageInfoMonthly;
import com.smoc.cloud.api.response.info.SimBaseInfoResponse;
import com.smoc.cloud.common.iot.validator.IotPackageInfoValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.iot.account.rowmapper.SimBaseInfoResponseRowMapper;
import com.smoc.cloud.iot.common.BasePageRepository;
import com.smoc.cloud.iot.packages.rowmapper.IotAccountPackageInfoMonthlyRowMapper;
import com.smoc.cloud.iot.packages.rowmapper.IotAccountPackageInfoRowMapper;
import com.smoc.cloud.iot.packages.rowmapper.IotPackageInfoRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

@Slf4j
public class IotAccountPackageItemsRepositoryImpl extends BasePageRepository {

    @Autowired
    private DataSource dataSource;

    /**
     * 查询业务账号套餐及未使用套餐
     *
     * @param accountId
     * @return
     */
    public List<IotPackageInfoValidator> list(String accountId) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append(" t.ID");
        sqlBuffer.append(",t.PACKAGE_NAME");
        sqlBuffer.append(",t.PACKAGE_TYPE");
        sqlBuffer.append(",t.PACKAGE_CHARGING");
        sqlBuffer.append(",t.PACKAGE_CHARGING_DISCOUNT");
        sqlBuffer.append(",t.PACKAGE_POOL_SIZE");
        sqlBuffer.append(",t.CHARGING_CYCLE");
        sqlBuffer.append(",t.CYCLE_QUOTA");
        sqlBuffer.append(",t.ABOVE_QUOTA_CHARGING");
        sqlBuffer.append(",t.PACKAGE_TEMP_AMOUNT");
        sqlBuffer.append(",t.PACKAGE_TEMP_AMOUNT_FEE");
        sqlBuffer.append(",t.IS_OPEN_FEE");
        sqlBuffer.append(",t.IS_FUNCTION_FEE");
        //sqlBuffer.append(",t.CYCLE_FUNCTION_FEE");
        sqlBuffer.append(",t.WARNING_LEVEL");
        sqlBuffer.append(",t.PACKAGE_CARDS_NUM");
        sqlBuffer.append(",t.REMARK");
        sqlBuffer.append(",u.ACCOUNT_ID USE_STATUS");
        sqlBuffer.append(",t.THIS_MONTH_USED_AMOUNT");
        sqlBuffer.append(",t.LAST_MONTH_CARRY_AMOUNT");
        sqlBuffer.append(",t.SYNC_DATE");
        sqlBuffer.append(",t.PACKAGE_STATUS");
        sqlBuffer.append(",t.CREATED_BY");
        sqlBuffer.append(",DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME");
        sqlBuffer.append("  from iot_package_info t left join iot_account_package_items u on t.ID=u.PACKAGE_ID where u.ACCOUNT_ID =? or t.USE_STATUS=? ");
        sqlBuffer.append("  order by t.CREATED_TIME desc");
        log.info("[sqlBuffer]:{}", sqlBuffer);
        //根据参数个数，组织参数值
        Object[] params = new Object[2];
        params[0] = accountId;
        params[1] = "01";
        List<IotPackageInfoValidator> list = this.queryForObjectList(sqlBuffer.toString(), params, new IotPackageInfoRowMapper());
        return list;
    }

    /**
     * 查询账号配置得套餐
     *
     * @param accountId
     * @return
     */
    public List<IotPackageInfoValidator> listAccountPackages(String accountId) {
        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append(" t.ID");
        sqlBuffer.append(",t.PACKAGE_NAME");
        sqlBuffer.append(",t.PACKAGE_TYPE");
        sqlBuffer.append(",t.PACKAGE_CHARGING");
        sqlBuffer.append(",t.PACKAGE_CHARGING_DISCOUNT");
        sqlBuffer.append(",t.PACKAGE_POOL_SIZE");
        sqlBuffer.append(",t.CHARGING_CYCLE");
        sqlBuffer.append(",t.CYCLE_QUOTA");
        sqlBuffer.append(",t.ABOVE_QUOTA_CHARGING");
        sqlBuffer.append(",t.PACKAGE_TEMP_AMOUNT");
        sqlBuffer.append(",t.PACKAGE_TEMP_AMOUNT_FEE");
        sqlBuffer.append(",t.IS_OPEN_FEE");
        sqlBuffer.append(",t.IS_FUNCTION_FEE");
        //sqlBuffer.append(",t.CYCLE_FUNCTION_FEE");
        sqlBuffer.append(",t.WARNING_LEVEL");
        sqlBuffer.append(",t.PACKAGE_CARDS_NUM");
        sqlBuffer.append(",t.REMARK");
        sqlBuffer.append(",u.ACCOUNT_ID USE_STATUS");
        sqlBuffer.append(",t.THIS_MONTH_USED_AMOUNT");
        sqlBuffer.append(",t.LAST_MONTH_CARRY_AMOUNT");
        sqlBuffer.append(",t.SYNC_DATE");
        sqlBuffer.append(",t.PACKAGE_STATUS");
        sqlBuffer.append(",t.CREATED_BY");
        sqlBuffer.append(",DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME");
        sqlBuffer.append("  from iot_package_info t left join iot_account_package_items u on t.ID=u.PACKAGE_ID where u.ACCOUNT_ID =? ");
        sqlBuffer.append("  order by t.CREATED_TIME desc");
        log.info("[sqlBuffer]:{}", sqlBuffer);
        //根据参数个数，组织参数值
        Object[] params = new Object[1];
        params[0] = accountId;
        List<IotPackageInfoValidator> list = this.queryForObjectList(sqlBuffer.toString(), params, new IotPackageInfoRowMapper());
        return list;
    }

    /**
     * 根据套餐id，查询套餐下绑定的物联网卡
     *
     * @return
     */
    public PageList<SimBaseInfoResponse> queryCardsByPackageId(String account, String packageId, PageParams pageParams) {
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append(" t.CARD_TYPE");
        sqlBuffer.append(",t.ICCID");
        sqlBuffer.append(",t.MSISDN");
        sqlBuffer.append(",t.IMSI");
        sqlBuffer.append(",t.IMEI");
        sqlBuffer.append(",t.OPEN_CARD_FEE");
        sqlBuffer.append(",t.CYCLE_FUNCTION_FEE");
        sqlBuffer.append(",t.ACTIVE_DATE");
        sqlBuffer.append(",t.OPEN_DATE");
        sqlBuffer.append(",t.CARD_STATUS");
        sqlBuffer.append("  from iot_flow_cards_primary_info t,iot_account_package_items u,iot_package_cards pc");
        sqlBuffer.append("  where t.ID = pc.CARD_ID and pc.PACKAGE_ID = u.PACKAGE_ID and u.ACCOUNT_ID=? and u.PACKAGE_ID=? ");
        Object[] params = new Object[2];
        params[0] = account;
        params[1] = packageId;
        PageList<SimBaseInfoResponse> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new SimBaseInfoResponseRowMapper());
        return pageList;
    }

    /**
     * 根据iccid，查询物联网卡明细
     *
     * @return
     */
    public SimBaseInfoResponse querySimBaseInfo(String account, String iccid) {
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append(" t.CARD_TYPE");
        sqlBuffer.append(",t.ICCID");
        sqlBuffer.append(",t.MSISDN");
        sqlBuffer.append(",t.IMSI");
        sqlBuffer.append(",t.IMEI");
        sqlBuffer.append(",t.OPEN_CARD_FEE");
        sqlBuffer.append(",t.CYCLE_FUNCTION_FEE");
        sqlBuffer.append(",t.ACTIVE_DATE");
        sqlBuffer.append(",t.OPEN_DATE");
        sqlBuffer.append(",t.CARD_STATUS");
        sqlBuffer.append("  from iot_flow_cards_primary_info t,iot_account_package_items u,iot_package_cards pc");
        sqlBuffer.append("  where t.ID = pc.CARD_ID and pc.PACKAGE_ID = u.PACKAGE_ID and u.ACCOUNT_ID=? and t.ICCID=? ");
        Object[] params = new Object[2];
        params[0] = account;
        params[1] = iccid;
        log.info("[sql]:{}",sqlBuffer);
        List<SimBaseInfoResponse> list = this.queryForObjectList(sqlBuffer.toString(), params, new SimBaseInfoResponseRowMapper());
        if(null!= list && list.size()>0 ){
            return  list.get(0);
        }
        return null;
    }

    /**
     * 根据iccids，批量查询物联网卡信息
     *
     * @return
     */
    public PageList<SimBaseInfoResponse> queryBatchSimBaseInfo(String account, List<String> iccids,PageParams pageParams) {

        StringBuffer iccidBuffer = new StringBuffer();
        for (String iccid : iccids) {
            if (iccidBuffer.length() < 1) {
                iccidBuffer.append("'" + iccid + "'");
            } else {
                iccidBuffer.append(",'" + iccid + "'");
            }
        }

        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append(" t.CARD_TYPE");
        sqlBuffer.append(",t.ICCID");
        sqlBuffer.append(",t.MSISDN");
        sqlBuffer.append(",t.IMSI");
        sqlBuffer.append(",t.IMEI");
        sqlBuffer.append(",t.OPEN_CARD_FEE");
        sqlBuffer.append(",t.CYCLE_FUNCTION_FEE");
        sqlBuffer.append(",t.ACTIVE_DATE");
        sqlBuffer.append(",t.OPEN_DATE");
        sqlBuffer.append(",t.CARD_STATUS");
        sqlBuffer.append("  from iot_flow_cards_primary_info t,iot_account_package_items u,iot_package_cards pc");
        sqlBuffer.append("  where t.ID = pc.CARD_ID and pc.PACKAGE_ID = u.PACKAGE_ID and u.ACCOUNT_ID=? and t.ICCID in (" + iccidBuffer + ") ");
        Object[] params = new Object[1];
        params[0] = account;
        PageList<SimBaseInfoResponse> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new SimBaseInfoResponseRowMapper());
        return pageList;
    }

    /**
     * 批量保存产品卡
     */
    public void insertAccountPackageCards(String account, String packageIds) {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            //处理原来产品配置的物联网卡
            stmt.addBatch("update iot_package_info p inner join iot_account_package_items u  on  p.ID= u.PACKAGE_ID set p.USE_STATUS  ='01' where u.ACCOUNT_ID='" + account + "' ");
            stmt.addBatch("delete from iot_account_package_items where ACCOUNT_ID='" + account + "' ");
            if (!StringUtils.isEmpty(packageIds)) {
                String[] packages = packageIds.split(",");
                Integer packagesNum = packages.length;
                if (null != packages && packagesNum > 0) {
                    for (String packageId : packages) {
                        StringBuffer sqlBuffer = new StringBuffer();
                        sqlBuffer.append(" insert into iot_account_package_items(ID,ACCOUNT_ID,PACKAGE_ID,CREATED_BY,CREATED_TIME)");
                        sqlBuffer.append(" values('" + UUID.uuid32() + "','" + account + "','" + packageId + "','system',now())");
                        stmt.addBatch(sqlBuffer.toString());
                        stmt.addBatch(" update iot_package_info set USE_STATUS ='02' where id = '" + packageId + "' ");
                    }
                }
            }
            stmt.executeBatch();
            stmt.clearBatch();
            stmt.close();
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != conn) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
