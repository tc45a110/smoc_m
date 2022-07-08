package com.smoc.cloud.customer.repository;


import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.customer.qo.AccountContentRepairQo;
import com.smoc.cloud.common.smoc.customer.validator.ConfigRouteContentRuleValidator;
import com.smoc.cloud.customer.rowmapper.*;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 2020/5/28 15:44
 **/
public class ConfigRouteContentRepositoryImpl extends BasePageRepository {


    public PageList<ConfigRouteContentRuleValidator> page(PageParams<ConfigRouteContentRuleValidator> pageParams) {

        //查询条件
        ConfigRouteContentRuleValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ID");
        sqlBuffer.append(", t.ACCOUNT_ID");
        sqlBuffer.append(", t.CARRIER");
        sqlBuffer.append(", t.AREA_CODES");
        sqlBuffer.append(", t.ROUTE_CONTENT");
        sqlBuffer.append(", t.ROUTE_REVERSE_CONTENT");
        sqlBuffer.append(", t.CHANNEL_ID");
        sqlBuffer.append(", t.MOBILE_NUM");
        sqlBuffer.append(", t.MIN_CONTENT");
        sqlBuffer.append(", t.MAX_CONTENT");
        sqlBuffer.append(", t.ROUTE_STATUS");
        sqlBuffer.append(", e.ACCOUNT_NAME");
        sqlBuffer.append(", b.CHANNEL_NAME");
        sqlBuffer.append(", e.BUSINESS_TYPE");
        sqlBuffer.append("  from config_route_content_rule t left join account_base_info e on t.ACCOUNT_ID = e.ACCOUNT_ID");
        sqlBuffer.append("  left join config_channel_basic_info b on t.CHANNEL_ID = b.CHANNEL_ID");
        sqlBuffer.append("  where 1=1");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getAccountName())) {
            sqlBuffer.append(" and t.ACCOUNT_NAME like ?");
            paramsList.add( "%"+qo.getAccountName().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getAccountId())) {
            sqlBuffer.append(" and t.ACCOUNT_ID like ?");
            paramsList.add( "%"+qo.getAccountId().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getCarrier())) {
            sqlBuffer.append(" and t.CARRIER like ?");
            paramsList.add( "%"+qo.getCarrier().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getBusinessType())) {
            sqlBuffer.append(" and e.BUSINESS_TYPE = ?");
            paramsList.add(qo.getBusinessType().trim());
        }

        if (!StringUtils.isEmpty(qo.getRouteContent())) {
            sqlBuffer.append(" and t.ROUTE_CONTENT like ?");
            paramsList.add("%"+qo.getRouteContent().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getAreaCodes())) {
            sqlBuffer.append(" and t.AREA_CODES = ?");
            paramsList.add(qo.getAreaCodes().trim());
        }

        if (!StringUtils.isEmpty(qo.getChannelName())) {
            sqlBuffer.append(" and b.CHANNEL_NAME like ?");
            paramsList.add("%"+qo.getChannelName().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getRouteStatus())) {
            sqlBuffer.append(" and t.REPAIR_STATUS = ?");
            paramsList.add(qo.getRouteStatus().trim());
        }

        sqlBuffer.append(" order by t.CREATED_TIME desc, t.id");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<ConfigRouteContentRuleValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new ConfigContentRepairRuleRowMapper());
        pageList.getPageParams().setParams(qo);

        return pageList;
    }

    /**
     * 业务账号列表
     */
    public PageList<AccountContentRepairQo> accountList(PageParams<AccountContentRepairQo> pageParams) {

        //查询条件
        AccountContentRepairQo qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ACCOUNT_ID");
        sqlBuffer.append(", t.ACCOUNT_NAME");
        sqlBuffer.append(", t.BUSINESS_TYPE");
        sqlBuffer.append(", t.CARRIER");
        sqlBuffer.append(", t.INFO_TYPE");
        sqlBuffer.append(", e.ENTERPRISE_NAME");
        sqlBuffer.append("  from account_base_info t left join enterprise_basic_info e on t.ENTERPRISE_ID = e.ENTERPRISE_ID");
        sqlBuffer.append("  where 1=1 ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getEnterpriseName())) {
            sqlBuffer.append(" and e.ENTERPRISE_NAME like ?");
            paramsList.add( "%"+qo.getEnterpriseName().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getAccountName())) {
            sqlBuffer.append(" and t.ACCOUNT_NAME like ?");
            paramsList.add( "%"+qo.getAccountName().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getAccountId())) {
            sqlBuffer.append(" and t.ACCOUNT_ID like ?");
            paramsList.add( "%"+qo.getAccountId().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getCarrier())) {
            sqlBuffer.append(" and t.CARRIER like ?");
            paramsList.add( "%"+qo.getCarrier().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getBusinessType())) {
            sqlBuffer.append(" and t.BUSINESS_TYPE = ?");
            paramsList.add(qo.getBusinessType().trim());
        }

        if (!StringUtils.isEmpty(qo.getInfoType())) {
            sqlBuffer.append(" and t.INFO_TYPE like ?");
            paramsList.add( "%"+qo.getInfoType().trim()+"%");
        }

        sqlBuffer.append(" order by t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<AccountContentRepairQo> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new AccountContentRepairQoRowMapper());
        pageList.getPageParams().setParams(qo);

        List<AccountContentRepairQo> list = pageList.getList();
        for (int i = 0; i < list.size(); i++) {
            AccountContentRepairQo info = list.get(i);
            String[] carrier = info.getCarrier().split(",");

            List<String> carrierList = new ArrayList<>();
            for (int a = 0; a < carrier.length; a++) {
                carrierList.add(carrier[a]);
            }

            info.setCarrierList(carrierList);
            info.setRowspan(carrierList.size());
        }

        return pageList;
    }
}
