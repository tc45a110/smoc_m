package com.smoc.cloud.iot.product.repository;

import com.smoc.cloud.common.iot.validator.IotFlowCardsPrimaryInfoValidator;
import com.smoc.cloud.common.iot.validator.IotProductCardValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.iot.carrier.rowmapper.IotFlowCardsPrimaryInfoRowMapper;
import com.smoc.cloud.iot.common.BasePageRepository;
import com.smoc.cloud.iot.product.rowmapper.IotProductCardRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class IotProductCardRepositoryImpl extends BasePageRepository {

    @Autowired
    private DataSource dataSource;

    public PageList<IotProductCardValidator> page(PageParams<IotProductCardValidator> pageParams) {

        //查询条件
        IotProductCardValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ID");
        sqlBuffer.append(", t.PRODUCT_ID");
        sqlBuffer.append(", t.CARD_MSISDN");
        sqlBuffer.append(", t.CARD_IMSI");
        sqlBuffer.append(", t.CARD_ICCID");
        sqlBuffer.append(", t.STATUS");
        sqlBuffer.append(", t.CREATED_BY");
        sqlBuffer.append(", DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME");
        sqlBuffer.append("  from iot_product_cards t where 1=1 ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getProductId())) {
            sqlBuffer.append(" and t.PRODUCT_ID = ?");
            paramsList.add(qo.getProductId().trim());
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

        PageList<IotProductCardValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new IotProductCardRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;
    }

    /**
     * 根据产品id查询，产品配置的信息，及未配置的 物联网卡信息
     *
     * @param productId
     * @return
     */
    public List<IotFlowCardsPrimaryInfoValidator> list(String productId) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ID");
        sqlBuffer.append(", t.CARRIER");
        sqlBuffer.append(", t.CARD_TYPE");
        sqlBuffer.append(", t.ORDER_NUM");
        sqlBuffer.append(", t.MSISDN");
        sqlBuffer.append(", t.IMSI");
        sqlBuffer.append(", t.ICCID");
        sqlBuffer.append(", t.FLOW_POOL_ID");
        sqlBuffer.append(", t.CHANGING_TYPE");
        sqlBuffer.append(", t.CYCLE_QUOTA");
        sqlBuffer.append(", t.ACTIVE_DATE");
        sqlBuffer.append(", t.OPEN_DATE");
        sqlBuffer.append(", pc.PRODUCT_ID USE_STATUS");
        sqlBuffer.append(", t.CARD_STATUS");
        sqlBuffer.append(", t.CREATED_BY");
        sqlBuffer.append(", DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME");
        sqlBuffer.append("  from iot_flow_cards_primary_info t left join  iot_product_cards pc on t.ID=pc.CARD_ID  where pc.PRODUCT_ID=? or t.USE_STATUS=? ");

        sqlBuffer.append(" order by t.CREATED_TIME desc");
        log.info("[sqlBuffer]:{}", sqlBuffer);
        //根据参数个数，组织参数值
        Object[] params = new Object[2];
        params[0] = productId;
        params[1] = "0";

        List<IotFlowCardsPrimaryInfoValidator> list = this.jdbcTemplate.query(sqlBuffer.toString(), new IotFlowCardsPrimaryInfoRowMapper(), params);
        return list;
    }

    /**
     * 批量保存产品卡
     */
    public void insertProductCards(String productId, String cardsId) {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            //处理原来产品配置的物联网卡
            stmt.addBatch("update iot_flow_cards_primary_info c  inner join iot_product_cards pc  on  c.ID=pc.CARD_ID set c.USE_STATUS  ='0' where pc.PRODUCT_ID='" + productId + "' ");
            stmt.addBatch("delete from iot_product_cards where PRODUCT_ID='" + productId + "' ");
            if (!StringUtils.isEmpty(cardsId)) {
                String[] cards = cardsId.split(",");
                Integer cardsNum = cards.length;
                if (null != cards && cardsNum > 0) {
                    for (String cardId : cards) {
                        StringBuffer sqlBuffer = new StringBuffer();
                        sqlBuffer.append(" insert into iot_product_cards(ID,PRODUCT_ID,CARD_ID,CARD_MSISDN,CARD_IMSI,CARD_ICCID,STATUS,CREATED_BY,CREATED_TIME)");
                        sqlBuffer.append(" select '" + UUID.uuid32() + "','" + productId + "','" + cardId + "',MSISDN,IMSI,ICCID,'1','system',NOW() from iot_flow_cards_primary_info where id = '" + cardId + "' ");
                        stmt.addBatch(sqlBuffer.toString());
                        stmt.addBatch(" update iot_flow_cards_primary_info set USE_STATUS ='1' where id = '" + cardId + "' ");
                    }
                    stmt.addBatch(" update iot_product_info set PRODUCT_CARDS_NUM =" + cardsNum + " where id = '" + productId + "' ");
                }
            } else {
                stmt.addBatch(" update iot_product_info set PRODUCT_CARDS_NUM =0 where id = '" + productId + "' ");
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
