package com.smoc.cloud.finance.repository;

import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountRefundValidator;
import com.smoc.cloud.finance.rowmapper.FinanceAccountRefundRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class FinanceAccountRefundRepositoryImpl extends BasePageRepository {

    /**
     * 分页查询  业务账号退款记录
     * @param pageParams
     * @return
     */
    public PageList<FinanceAccountRefundValidator> pageBusiness(PageParams<FinanceAccountRefundValidator> pageParams){
        //查询条件
        FinanceAccountRefundValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ID,");
        sqlBuffer.append("  t.ACCOUNT_ID,");
        sqlBuffer.append("  t.ENTERPRISE_NAME,");
        sqlBuffer.append("  t.ENTERPRISE_ID,");
        sqlBuffer.append("  t.BUSINESS_TYPE,");
        sqlBuffer.append("  t.ACCOUNT_NAME,");
        sqlBuffer.append("  t.REFUND_FLOW_NO,");
        sqlBuffer.append("  t.REFUND_SOURCE,");
        sqlBuffer.append("  t.REFUND_SUM,");
        sqlBuffer.append("  t.REFUND_COST,");
        sqlBuffer.append("  t.REFUND_ACCOUNT_USABLE,");
        sqlBuffer.append("  t.CREATED_BY,");
        sqlBuffer.append("  t.REMARK,");
        sqlBuffer.append("  DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME ");
        sqlBuffer.append("  from finance_refund_view t ");
        sqlBuffer.append("  where (1=1) ");
        //log.info("[sql]:{}",sqlBuffer.toString());
        List<Object> paramsList = new ArrayList<Object>();

        //企业名称
        if (!StringUtils.isEmpty(qo.getEnterpriseName())) {
            sqlBuffer.append(" and t.ENTERPRISE_NAME like ? ");
            paramsList.add("%" + qo.getEnterpriseName().trim() + "%");
        }
        //企业ID
        if (!StringUtils.isEmpty(qo.getEnterpriseId())) {
            sqlBuffer.append(" and t.ENTERPRISE_ID = ?");
            paramsList.add(qo.getEnterpriseId().trim());
        }
        //认证账号
        if (!StringUtils.isEmpty(qo.getAccountId())) {
            sqlBuffer.append(" and t.ACCOUNT_ID =? ");
            paramsList.add(qo.getAccountId().trim());
        }
        //业务类型
        if (!StringUtils.isEmpty(qo.getBusinessType())) {
            sqlBuffer.append(" and t.BUSINESS_TYPE =? ");
            paramsList.add(qo.getBusinessType().trim());
        }

        if (!StringUtils.isEmpty(qo.getStartDate())) {
            sqlBuffer.append(" and DATE_FORMAT(t.CREATED_TIME,'%Y-%m-%d') >=? ");
            paramsList.add(qo.getStartDate().trim());
        }
        if (!StringUtils.isEmpty(qo.getEndDate())) {
            sqlBuffer.append(" and DATE_FORMAT(t.CREATED_TIME,'%Y-%m-%d') <=? ");
            paramsList.add(qo.getEndDate().trim());
        }

        sqlBuffer.append(" order by t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<FinanceAccountRefundValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new FinanceAccountRefundRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;
    }

}
