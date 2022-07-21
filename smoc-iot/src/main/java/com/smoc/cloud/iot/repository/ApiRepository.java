package com.smoc.cloud.iot.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class ApiRepository {

    @Resource
    public JdbcTemplate jdbcTemplate;

    /**
     * 判断订单是否存在
     *
     * @param account
     * @param orderNum
     * @return
     */
    public Boolean isExistAccountOrder(String account, String orderNum) {

        String sql = "select count(*) from iot_user_product_items u,iot_product_cards p,iot_flow_cards_primary_info c where u.PRODUCT_ID=p.PRODUCT_ID and p.CARD_ID = c.ID and u.USER_ID=? and c.ORDER_NUM=?";
        Object[] params = new Object[2];
        params[0] = account;
        params[1] = orderNum;

        Integer result = this.jdbcTemplate.queryForObject(sql, params, Integer.class);
        if (result > 0) {
            return true;
        }

        return false;
    }

    /**
     * 判断物联网卡是否存在
     *
     * @param account
     * @param msisdn
     * @return
     */
    public Boolean isExistAccountSim(String account, String msisdn) {

        String sql = "select count(*) from iot_user_product_items u,iot_product_cards p,iot_flow_cards_primary_info c where u.PRODUCT_ID=p.PRODUCT_ID and p.CARD_ID = c.ID and u.USER_ID=? and c.MSISDN=?";
        Object[] params = new Object[2];
        params[0] = account;
        params[1] = msisdn;

        Integer result = this.jdbcTemplate.queryForObject(sql, params, Integer.class);
        if (result > 0) {
            return true;
        }

        return false;
    }
}
