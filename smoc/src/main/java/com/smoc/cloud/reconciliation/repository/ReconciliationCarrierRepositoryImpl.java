package com.smoc.cloud.reconciliation.repository;

import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.reconciliation.ReconciliationCarrierItemsValidator;
import com.smoc.cloud.common.smoc.reconciliation.ReconciliationPeriodValidator;
import com.smoc.cloud.reconciliation.rowmapper.ReconciliationPeriodRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ReconciliationCarrierRepositoryImpl extends BasePageRepository {

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
   /* public PageList<ReconciliationCarrierItemsValidator> page(PageParams<ReconciliationCarrierItemsValidator> pageParams) {
        //查询条件
        ReconciliationCarrierItemsValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");

        sqlBuffer.append(" t.ID,");
        sqlBuffer.append(" t.CHANNEL_PERIOD,");
        sqlBuffer.append(" t.CHANNEL_PROVDER,");
        sqlBuffer.append(" t.SRC_ID,");
        sqlBuffer.append(" t.TOTAL_SEND_QUANTITY,");
        sqlBuffer.append(" t.PRICE, ");
        sqlBuffer.append(" t.ACCOUNT_PERIOD_STATUS,");
        sqlBuffer.append(" t.STATUS,");
        sqlBuffer.append(" from reconciliation_period t ");
        sqlBuffer.append(" where (1=1) ");

        List<Object> paramsList = new ArrayList<Object>();

        //账期
        if (!StringUtils.isEmpty(qo.getChannelPeriod())) {
            sqlBuffer.append(" and t.CHANNEL_PERIOD =?");
            paramsList.add(qo.getChannelPeriod().trim());
        }
        //账期类型
        if (!StringUtils.isEmpty(qo.getChannelProvder())) {
            sqlBuffer.append(" and t.CHANNEL_PROVDER =?");
            paramsList.add(qo.getChannelProvder().trim());
        }

        //有效状态
        if (!StringUtils.isEmpty(qo.getStatus())) {
            sqlBuffer.append(" and t.ACCOUNT_PERIOD_STATUS =?");
            paramsList.add(qo.getStatus().trim());
        }

        //有效状态
        if (!StringUtils.isEmpty(qo.getStatus())) {
            sqlBuffer.append(" and t.STATUS =?");
            paramsList.add(qo.getStatus().trim());
        }

        sqlBuffer.append(" order by t.ACCOUNT_PERIOD desc");
        //log.info("[SQL]:{}",sqlBuffer);
        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);
        //log.info("[SQL1]:{}",sqlBuffer);
        PageList<ReconciliationPeriodValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new ReconciliationPeriodRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;

    }
*/
}
