package com.smoc.cloud.customer.repository;


import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseContractInfoValidator;
import com.smoc.cloud.customer.rowmapper.EnterpriseContractInfoRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 2020/5/28 15:44
 **/
@Slf4j
public class EnterpriseContractRepositoryImpl extends BasePageRepository {


    public PageList<EnterpriseContractInfoValidator> page(PageParams<EnterpriseContractInfoValidator> pageParams) {

        //查询条件
        EnterpriseContractInfoValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ID");
        sqlBuffer.append(", t.ENTERPRISE_ID");
        sqlBuffer.append(", t.CONTRACT_NO");
        sqlBuffer.append(", t.CONTRACT_KEY");
        sqlBuffer.append(", t.CONTRACT_DATE");
        sqlBuffer.append(", t.CONTRACT_EXPIRE_DATE");
        sqlBuffer.append(", t.CONTRACT_STATUS");
        sqlBuffer.append(", t.CREATED_BY");
        sqlBuffer.append(", DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME");
        sqlBuffer.append(", e.ENTERPRISE_NAME");
        sqlBuffer.append(", e.ENTERPRISE_TYPE");
        sqlBuffer.append("  from enterprise_contract_info t left join enterprise_basic_info e on t.ENTERPRISE_ID = e.ENTERPRISE_ID ");
        sqlBuffer.append("  where t.CONTRACT_STATUS=1 ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getEnterpriseType())) {
            sqlBuffer.append(" and e.ENTERPRISE_TYPE = ? ");
            paramsList.add(qo.getEnterpriseType().trim());
        }

        if (!StringUtils.isEmpty(qo.getEnterpriseName())) {
            sqlBuffer.append(" and e.ENTERPRISE_NAME like ? ");
            paramsList.add("%"+qo.getEnterpriseName().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getContractNo())) {
            sqlBuffer.append(" and t.CONTRACT_NO like ? ");
            paramsList.add("%"+qo.getContractNo().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getContractKey())) {
            sqlBuffer.append(" and t.CONTRACT_KEY like ? ");
            paramsList.add("%"+qo.getContractKey().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getStartDate())) {
            sqlBuffer.append(" and DATE_FORMAT(t.CONTRACT_DATE,'%Y-%m-%d') >= ? ");
            paramsList.add(qo.getStartDate().trim());
        }

        if (!StringUtils.isEmpty(qo.getEndDate())) {
            sqlBuffer.append(" and DATE_FORMAT(t.CONTRACT_DATE,'%Y-%m-%d') <= ? ");
            paramsList.add(qo.getEndDate().trim());
        }


        sqlBuffer.append(" order by t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<EnterpriseContractInfoValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new EnterpriseContractInfoRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;
    }


}
