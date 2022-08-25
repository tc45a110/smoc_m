package com.smoc.cloud.iot.repository;

import com.smoc.cloud.api.remote.cmcc.response.info.CarrierInfo;
import com.smoc.cloud.api.response.info.SimBaseInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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

        String sql = "select count(*) from iot_account_package_items u,iot_package_cards p,iot_flow_cards_primary_info c where u.PACKAGE_ID=p.PACKAGE_ID and p.CARD_ID = c.ID and u.ACCOUNT_ID=? and c.ORDER_NUM=?";
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
     * @param iccid
     * @return
     */
    public CarrierInfo isExistAccountSim(String account, String iccid) {

        String sql = "select carrier.CARRIER_IDENTIFYING,carrier.CARRIER_PASSWORD from iot_account_package_items u,iot_package_cards p,iot_flow_cards_primary_info c,iot_carrier_info carrier where u.PACKAGE_ID=p.PACKAGE_ID and p.CARD_ID = c.ID and c.CARRIER=carrier.ID and u.ACCOUNT_ID=? and c.ICCID=?";
        Object[] params = new Object[2];
        params[0] = account;
        params[1] = iccid;

        List<CarrierInfo> list = this.jdbcTemplate.query(sql, new BeanPropertyRowMapper<CarrierInfo>(CarrierInfo.class), params);
        if(null != list && list.size()>0){
            return list.get(0);
        }

        return null;
    }
}
