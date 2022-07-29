package com.smoc.cloud.iot.packages.repository;

import com.google.gson.Gson;
import com.smoc.cloud.common.iot.validator.IotFlowCardsPrimaryInfoValidator;
import com.smoc.cloud.common.iot.validator.IotPackageCardValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.iot.carrier.rowmapper.IotFlowCardsPrimaryInfoRowMapper;
import com.smoc.cloud.iot.common.BasePageRepository;
import com.smoc.cloud.iot.packages.rowmapper.IotPackageCardRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class IotPackageCardRepositoryImpl extends BasePageRepository {

    @Autowired
    private DataSource dataSource;

    public PageList<IotPackageCardValidator> page(PageParams<IotPackageCardValidator> pageParams) {

        //查询条件
        IotPackageCardValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ID");
        sqlBuffer.append(",t.PACKAGE_ID");
        sqlBuffer.append(",t.CARD_MSISDN");
        sqlBuffer.append(",t.CARD_IMSI");
        sqlBuffer.append(",t.CARD_ICCID");
        sqlBuffer.append(",t.STATUS");
        sqlBuffer.append(",t.CREATED_BY");
        sqlBuffer.append(",DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME");
        sqlBuffer.append("  from iot_product_cards t where 1=1 ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getPackageId())) {
            sqlBuffer.append(" and t.PACKAGE_ID = ?");
            paramsList.add(qo.getPackageId().trim());
        }

        if (!StringUtils.isEmpty(qo.getCardMsisdn())) {
            sqlBuffer.append(" and t.CARD_MSISDN = ?");
            paramsList.add(qo.getCardMsisdn().trim());
        }

        if (!StringUtils.isEmpty(qo.getCardImsi())) {
            sqlBuffer.append(" and t.CARD_IMSI = ?");
            paramsList.add(qo.getCardImsi().trim());
        }

        if (!StringUtils.isEmpty(qo.getCardIccid())) {
            sqlBuffer.append(" and t.CARD_ICCID = ?");
            paramsList.add(qo.getCardIccid().trim());
        }

        if (!StringUtils.isEmpty(qo.getStatus())) {
            sqlBuffer.append(" and t.STATUS = ?");
            paramsList.add(qo.getStatus().trim());
        }

        sqlBuffer.append(" order by t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<IotPackageCardValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new IotPackageCardRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;
    }

    /**
     * 根据套餐id查询，套餐配置的信息，及未配置的 物联网卡信息
     *
     * @param packageId
     * @return
     */
    public List<IotFlowCardsPrimaryInfoValidator> list(String packageId, String packageType) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append(" t.ID");
        sqlBuffer.append(",t.CARRIER");
        sqlBuffer.append(",t.CARRIER CARRIER_NAME");
        sqlBuffer.append(",t.CARD_TYPE");
        sqlBuffer.append(",t.ORDER_NUM");
        sqlBuffer.append(",t.ICCID");
        sqlBuffer.append(",t.MSISDN");
        sqlBuffer.append(",t.IMSI");
        sqlBuffer.append(",t.IMEI");
        sqlBuffer.append(",t.CHARGE_CYCLE");
        sqlBuffer.append(",t.CHARGE_TYPE");
        sqlBuffer.append(",t.FLOW_POOL_ID");
        sqlBuffer.append(",t.CYCLE_QUOTA");
        sqlBuffer.append(",t.OPEN_CARD_FEE");
        sqlBuffer.append(",t.CYCLE_FUNCTION_FEE");
        sqlBuffer.append(",t.ACTIVE_DATE");
        sqlBuffer.append(",t.OPEN_DATE");
        sqlBuffer.append(",pc.PACKAGE_ID USE_STATUS");
        sqlBuffer.append(",t.CARD_STATUS");
        sqlBuffer.append(",t.CREATED_BY");
        sqlBuffer.append(",DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME");
        sqlBuffer.append("  from iot_flow_cards_primary_info t left join  iot_package_cards pc on t.ID=pc.CARD_ID  where (pc.PACKAGE_ID=? or t.USE_STATUS=?) and t.CARD_TYPE=? ");

        sqlBuffer.append(" order by t.CREATED_TIME desc");
        log.info("[sqlBuffer]:{}", sqlBuffer);
        //根据参数个数，组织参数值
        Object[] params = new Object[3];
        params[0] = packageId;
        params[1] = "0";
        params[2] = packageType;

        List<IotFlowCardsPrimaryInfoValidator> list = this.jdbcTemplate.query(sqlBuffer.toString(), new IotFlowCardsPrimaryInfoRowMapper(), params);
        return list;
    }

    /**
     * 根据套餐id，查询套餐绑定的物联网卡
     *
     * @param packageId
     * @return
     */
    public List<IotFlowCardsPrimaryInfoValidator> listCardsByPackageId(String account, String packageId) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append(" t.ID");
        sqlBuffer.append(",t.CARRIER");
        sqlBuffer.append(",c.CARRIER_NAME");
        sqlBuffer.append(",t.CARD_TYPE");
        sqlBuffer.append(",t.ORDER_NUM");
        sqlBuffer.append(",t.ICCID");
        sqlBuffer.append(",t.MSISDN");
        sqlBuffer.append(",t.IMSI");
        sqlBuffer.append(",t.IMEI");
        sqlBuffer.append(",t.CHARGE_CYCLE");
        sqlBuffer.append(",t.CHARGE_TYPE");
        sqlBuffer.append(",t.FLOW_POOL_ID");
        sqlBuffer.append(",t.CYCLE_QUOTA");
        sqlBuffer.append(",t.OPEN_CARD_FEE");
        sqlBuffer.append(",t.CYCLE_FUNCTION_FEE");
        sqlBuffer.append(",t.ACTIVE_DATE");
        sqlBuffer.append(",t.OPEN_DATE");
        sqlBuffer.append(",pc.PACKAGE_ID USE_STATUS");
        sqlBuffer.append(",t.CARD_STATUS");
        sqlBuffer.append(",t.CREATED_BY");
        sqlBuffer.append(",DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME");
        sqlBuffer.append(" from iot_flow_cards_primary_info t,iot_carrier_info c,iot_package_cards pc where c.Id=t.CARRIER and t.ID=pc.CARD_ID and pc.PACKAGE_ID=? ");

        sqlBuffer.append(" order by t.CREATED_TIME desc");
        //根据参数个数，组织参数值
        Object[] params = new Object[1];
        params[0] = packageId;

        List<IotFlowCardsPrimaryInfoValidator> list = this.jdbcTemplate.query(sqlBuffer.toString(), new IotFlowCardsPrimaryInfoRowMapper(), params);
        return list;
    }

    /**
     * 批量保存套餐卡
     */
    public void insertPackageCards(String packageId, String cardsId) {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            /**
             * 处理原来套餐配置的物联网卡
             */
            //取消原来配置的物联网卡
            stmt.addBatch("update iot_flow_cards_primary_info c inner join iot_package_cards pc on c.ID=pc.CARD_ID set c.USE_STATUS ='0' where pc.PACKAGE_ID='" + packageId + "' ");
            stmt.addBatch("delete from iot_package_cards where PACKAGE_ID='" + packageId + "' ");
            stmt.addBatch(" update iot_package_info set PACKAGE_CARDS_NUM =0 where id = '" + packageId + "' ");

            /**
             * 处理套餐新设置的物联网卡
             */
            if (!StringUtils.isEmpty(cardsId)) {
                String[] cards = cardsId.split(",");
                Integer cardsNum = cards.length;
                if (null != cards && cardsNum > 0) {
                    for (String cardId : cards) {
                        StringBuffer sqlBuffer = new StringBuffer();
                        sqlBuffer.append(" insert into iot_package_cards(ID,PACKAGE_ID,CARD_ID,CARD_MSISDN,CARD_IMSI,CARD_ICCID,STATUS,CREATED_BY,CREATED_TIME)");
                        sqlBuffer.append(" select '" + UUID.uuid32() + "','" + packageId + "','" + cardId + "',MSISDN,IMSI,ICCID,'1','system',NOW() from iot_flow_cards_primary_info where id = '" + cardId + "' ");
                        stmt.addBatch(sqlBuffer.toString());
                        stmt.addBatch(" update iot_flow_cards_primary_info set USE_STATUS ='1' where id = '" + cardId + "' ");
                    }
                    stmt.addBatch(" update iot_package_info set PACKAGE_CARDS_NUM =" + cardsNum + " where id = '" + packageId + "' ");
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
