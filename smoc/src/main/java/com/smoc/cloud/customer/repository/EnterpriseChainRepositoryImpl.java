package com.smoc.cloud.customer.repository;


import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseChainInfoValidator;
import com.smoc.cloud.customer.rowmapper.EnterpriseChainInfoRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 2020/5/28 15:44
 **/
@Slf4j
public class EnterpriseChainRepositoryImpl extends BasePageRepository {


    public List<EnterpriseChainInfoValidator> page(EnterpriseChainInfoValidator qo) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ID");
        sqlBuffer.append(", t.DOCUMENT_ID");
        sqlBuffer.append(", t.SIGN_CHAIN");
        sqlBuffer.append(", t.SIGN_DATE");
        sqlBuffer.append(", t.SIGN_EXPIRE_DATE");
        sqlBuffer.append(", t.SIGN_CHAIN_STATUS");
        sqlBuffer.append(", t.SORT");
        sqlBuffer.append(", t.CREATED_BY");
        sqlBuffer.append(", DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME");
        sqlBuffer.append("  from enterprise_chain_info t ");
        sqlBuffer.append("  where t.SIGN_CHAIN_STATUS=1  ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getDocumentId())) {
            sqlBuffer.append(" and t.DOCUMENT_ID = ? ");
            paramsList.add(qo.getDocumentId().trim());
        }


        sqlBuffer.append(" order by t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        List<EnterpriseChainInfoValidator> list = this.queryForObjectList(sqlBuffer.toString(), params, new EnterpriseChainInfoRowMapper());
        return list;
    }


}
