package com.smoc.cloud.configure.advance.repository;

import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.configuate.validator.SystemHistoryPriceChangeRecordValidator;
import com.smoc.cloud.configure.advance.rowmapper.SystemHistoryPriceChangeRecordRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SystemHistoryPriceChangeRecordRepositoryImpl extends BasePageRepository {

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    public PageList<SystemHistoryPriceChangeRecordValidator> page(PageParams<SystemHistoryPriceChangeRecordValidator> pageParams){

        SystemHistoryPriceChangeRecordValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append(" t.ID,");
        sqlBuffer.append(" t.CHANGE_TYPE,");
        sqlBuffer.append(" t.BUSINESS_ID,");
        sqlBuffer.append(" t.PRICE_AREA,");
        sqlBuffer.append(" DATE_FORMAT(t.START_DATE, '%Y-%m-%d')START_DATE, ");
        sqlBuffer.append(" t.CHANGE_PRICE,");
        sqlBuffer.append(" t.CREATED_BY,");
        sqlBuffer.append(" DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME ");
        sqlBuffer.append(" from system_history_price_change_record t ");
        sqlBuffer.append(" where 1=1  ");

        List<Object> paramsList = new ArrayList<Object>();

        //变更类型
        if (!StringUtils.isEmpty(qo.getBusinessId())) {
            sqlBuffer.append(" and t.BUSINESS_ID =?");
            paramsList.add(qo.getBusinessId().trim());
        }

        //变更类型
        if (!StringUtils.isEmpty(qo.getChangeType())) {
            sqlBuffer.append(" and t.CHANGE_TYPE =?");
            paramsList.add(qo.getChangeType().trim());
        }


        sqlBuffer.append(" order by t.CREATED_TIME desc");

        //log.info("[sql]:{}",sqlBuffer.toString());

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);
        //log.info("[params]:{}",new Gson().toJson(paramsList));

        PageList<SystemHistoryPriceChangeRecordValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new SystemHistoryPriceChangeRecordRowMapper());
        pageList.getPageParams().setParams(qo);

        return pageList;


    }
}
