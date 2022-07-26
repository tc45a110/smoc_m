package com.smoc.cloud.iot.carrier.repository;

import com.google.gson.Gson;
import com.smoc.cloud.api.response.info.SimBaseInfoResponse;
import com.smoc.cloud.common.iot.validator.IotFlowCardsPrimaryInfoValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.iot.carrier.rowmapper.IotFlowCardsPrimaryInfoRowMapper;
import com.smoc.cloud.iot.common.BasePageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.util.StringUtils;

import java.lang.management.GarbageCollectorMXBean;
import java.util.ArrayList;
import java.util.List;
@Slf4j
public class IotFlowCardsPrimaryInfoRepositoryImpl extends BasePageRepository {

    public PageList<IotFlowCardsPrimaryInfoValidator> page(PageParams<IotFlowCardsPrimaryInfoValidator> pageParams) {

        //查询条件
        IotFlowCardsPrimaryInfoValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ID");
        sqlBuffer.append(",t.CARRIER");
        sqlBuffer.append(",t.CARD_TYPE");
        sqlBuffer.append(",t.ORDER_NUM");
        sqlBuffer.append(",t.MSISDN");
        sqlBuffer.append(",t.IMSI");
        sqlBuffer.append(",t.ICCID");
        sqlBuffer.append(",t.FLOW_POOL_ID");
        sqlBuffer.append(",t.CHANGING_TYPE");
        sqlBuffer.append(",t.CYCLE_QUOTA");
        sqlBuffer.append(",t.OPEN_CARD_FEE");
        sqlBuffer.append(",t.ACTIVE_DATE");
        sqlBuffer.append(",t.OPEN_DATE");
        sqlBuffer.append(",t.USE_STATUS");
        sqlBuffer.append(",t.CARD_STATUS");
        sqlBuffer.append(",t.CREATED_BY");
        sqlBuffer.append(",DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME");
        sqlBuffer.append("  from iot_flow_cards_primary_info t where 1=1 ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getCarrier())) {
            sqlBuffer.append(" and t.CARRIER = ?");
            paramsList.add(qo.getCarrier().trim());
        }

        if (!StringUtils.isEmpty(qo.getCardType())) {
            sqlBuffer.append(" and t.CARD_TYPE = ?");
            paramsList.add(qo.getCardType().trim());
        }

        if (!StringUtils.isEmpty(qo.getMsisdn())) {
            sqlBuffer.append(" and t.MSISDN = ?");
            paramsList.add(qo.getMsisdn().trim());
        }

        if (!StringUtils.isEmpty(qo.getImsi())) {
            sqlBuffer.append(" and t.IMSI = ?");
            paramsList.add(qo.getImsi().trim());
        }

        if (!StringUtils.isEmpty(qo.getIccid())) {
            sqlBuffer.append(" and t.ICCID = ?");
            paramsList.add(qo.getIccid().trim());
        }

        if (!StringUtils.isEmpty(qo.getOrderNum())) {
            sqlBuffer.append(" and t.ORDER_NUM = ?");
            paramsList.add(qo.getOrderNum().trim());
        }

        if (!StringUtils.isEmpty(qo.getFlowPoolId())) {
            sqlBuffer.append(" and t.FLOW_POOL_ID = ?");
            paramsList.add(qo.getFlowPoolId().trim());
        }

        if (!StringUtils.isEmpty(qo.getOpenDate())) {
            sqlBuffer.append(" and t.OPEN_DATE = ?");
            paramsList.add(qo.getOpenDate().trim());
        }

        if (!StringUtils.isEmpty(qo.getUseStatus())) {
            sqlBuffer.append(" and t.USE_STATUS = ?");
            paramsList.add(qo.getUseStatus().trim());
        }

        if (!StringUtils.isEmpty(qo.getCardStatus())) {
            sqlBuffer.append(" and t.CARD_STATUS = ?");
            paramsList.add(qo.getCardStatus().trim());
        }

        sqlBuffer.append(" order by t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<IotFlowCardsPrimaryInfoValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new IotFlowCardsPrimaryInfoRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;
    }

    /**
     * 根据账号及msisdn 查询物联网卡基本信息
     * @param account
     * @param msisdn
     * @return
     */
    public SimBaseInfoResponse findSimByAccountAndMsisdn(String account, String msisdn) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append(" t.MSISDN");
        sqlBuffer.append(",t.IMSI");
        sqlBuffer.append(",t.ICCID");
        sqlBuffer.append(",t.ACTIVE_DATE");
        sqlBuffer.append(",t.OPEN_DATE");
        sqlBuffer.append("  from iot_flow_cards_primary_info t,iot_account_package_items a,iot_package_cards pc where pc.PACKAGE_ID=a.PACKAGE_ID and pc.CARD_ID=t.ID and a.ACCOUNT_ID=? and t.MSISDN=? ");

        Object[] params = new Object[2];
        params[0] = account;
        params[1] = msisdn;
        SimBaseInfoResponse simBaseInfoResponse = this.jdbcTemplate.queryForObject(sqlBuffer.toString(), new BeanPropertyRowMapper<SimBaseInfoResponse>(SimBaseInfoResponse.class), params);
        return simBaseInfoResponse;
    }

    /**
     * 码号信息批量查询
     * @param account
     * @param msisdns
     * @return
     */
    public List<SimBaseInfoResponse> queryBatchSimBaseInfo(String account, List<String> msisdns) {

        StringBuffer msisdnBuffer = new StringBuffer();
        for(String msisdn:msisdns){
           if(msisdnBuffer.length()<1){
               msisdnBuffer.append("'"+msisdn+"'");
           }else {
               msisdnBuffer.append(",'"+msisdn+"'");
           }
        }

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append(" t.MSISDN");
        sqlBuffer.append(",t.IMSI");
        sqlBuffer.append(",t.ICCID");
        sqlBuffer.append(",t.ACTIVE_DATE");
        sqlBuffer.append(",t.OPEN_DATE");
        sqlBuffer.append("  from iot_flow_cards_primary_info t,iot_account_package_items a,iot_package_cards pc where pc.PACKAGE_ID=a.PACKAGE_ID and pc.CARD_ID=t.ID and a.ACCOUNT_ID=? and t.MSISDN in ("+msisdnBuffer+") ");

        Object[] params = new Object[1];
        params[0] = account;
        log.info("[sql]:{}",sqlBuffer);
        List<SimBaseInfoResponse> list = this.jdbcTemplate.query(sqlBuffer.toString(), new BeanPropertyRowMapper<SimBaseInfoResponse>(SimBaseInfoResponse.class), params);
        return list;
    }
}
