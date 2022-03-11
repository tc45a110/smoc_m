package com.smoc.cloud.configure.channel.repository;

import com.google.gson.Gson;
import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelPriceHistoryValidator;
import com.smoc.cloud.configure.channel.rowmapper.ConfigChannelPriceHistoryRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ConfigChannelPriceHistoryRepositoryImpl extends BasePageRepository {

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    public PageList<ConfigChannelPriceHistoryValidator> page(PageParams<ConfigChannelPriceHistoryValidator> pageParams){

        ConfigChannelPriceHistoryValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append(" t.ID,");
        sqlBuffer.append(" t.SOURCE_ID,");
        sqlBuffer.append(" t.CHANNEL_ID,");
        sqlBuffer.append(" t.PRICE_STYLE,");
        sqlBuffer.append(" t.AREA_CODE,");
        sqlBuffer.append(" t.CHANNEL_PRICE,");
        sqlBuffer.append(" t.PRICE_DATE,");
        sqlBuffer.append(" DATE_FORMAT(t.CREATE_TIME, '%Y-%m-%d %H:%i:%S')CREATE_TIME, ");
        sqlBuffer.append(" DATE_FORMAT(t.UPDATED_TIME, '%Y-%m-%d %H:%i:%S')UPDATED_TIME ");
        sqlBuffer.append(" from config_channel_price_history t ");
        sqlBuffer.append(" where 1=1  ");

        List<Object> paramsList = new ArrayList<Object>();

        //通道ID
        if (!StringUtils.isEmpty(qo.getChannelId())) {
            sqlBuffer.append(" and t.CHANNEL_ID =?");
            paramsList.add(qo.getChannelId().trim());
        }

        if (!StringUtils.isEmpty(qo.getEndDate())) {
            sqlBuffer.append(" and DATE_FORMAT(t.PRICE_DATE,'%Y-%m-%d') >=? ");
            paramsList.add(qo.getEndDate().trim());
        }

        if (!StringUtils.isEmpty(qo.getStartDate())) {
            sqlBuffer.append(" and DATE_FORMAT(t.PRICE_DATE,'%Y-%m-%d') <=? ");
            paramsList.add(qo.getStartDate().trim());
        }


        sqlBuffer.append(" order by t.PRICE_DATE desc");

        //log.info("[sql]:{}",sqlBuffer.toString());

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);
        //log.info("[params]:{}",new Gson().toJson(paramsList));

        PageList<ConfigChannelPriceHistoryValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new ConfigChannelPriceHistoryRowMapper());
        pageList.getPageParams().setParams(qo);

        return pageList;

    }
}
