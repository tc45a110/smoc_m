package com.smoc.cloud.customer.repository;


import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseDocumentInfoValidator;
import com.smoc.cloud.customer.rowmapper.EnterpriseDocumentInfoRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 2020/5/28 15:44
 **/
@Slf4j
public class EnterpriseDocumentRepositoryImpl extends BasePageRepository {


    public PageList<EnterpriseDocumentInfoValidator> page(PageParams<EnterpriseDocumentInfoValidator> pageParams) {

        //查询条件
        EnterpriseDocumentInfoValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ID");
        sqlBuffer.append(", t.ENTERPRISE_ID");
        sqlBuffer.append(", t.SIGN_NAME");
        sqlBuffer.append(", t.BUSINESS_TYPE");
        sqlBuffer.append(", t.INFO_TYPE");
        sqlBuffer.append(", t.SHORT_LINK");
        sqlBuffer.append(", t.DOC_KEY");
        sqlBuffer.append(", t.DOC_STATUS");
        sqlBuffer.append(", t.CREATED_BY");
        sqlBuffer.append(", DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME");
        sqlBuffer.append(", e.ENTERPRISE_NAME");
        sqlBuffer.append(", e.ENTERPRISE_TYPE");
        sqlBuffer.append("  from enterprise_document_info t left join enterprise_basic_info e on t.ENTERPRISE_ID = e.ENTERPRISE_ID ");
        sqlBuffer.append("  where t.DOC_STATUS=1  ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getEnterpriseType())) {
            sqlBuffer.append(" and e.ENTERPRISE_TYPE = ? ");
            paramsList.add(qo.getEnterpriseType().trim());
        }

        if (!StringUtils.isEmpty(qo.getEnterpriseName())) {
            sqlBuffer.append(" and e.ENTERPRISE_NAME like ? ");
            paramsList.add("%"+qo.getEnterpriseName().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getBusinessType())) {
            sqlBuffer.append(" and t.BUSINESS_TYPE = ? ");
            paramsList.add(qo.getBusinessType().trim());
        }

        if (!StringUtils.isEmpty(qo.getInfoType())) {
            sqlBuffer.append(" and t.INFO_TYPE = ? ");
            paramsList.add(qo.getInfoType().trim());
        }

        if (!StringUtils.isEmpty(qo.getSignName())) {
            sqlBuffer.append(" and t.SIGN_NAME like ? ");
            paramsList.add("%"+qo.getSignName().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getDocKey())) {
            sqlBuffer.append(" and t.DOC_KEY like ? ");
            paramsList.add("%"+qo.getDocKey().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getStartDate())) {
            sqlBuffer.append(" and DATE_FORMAT(t.CREATED_TIME,'%Y-%m-%d') >= ? ");
            paramsList.add(qo.getStartDate().trim());
        }

        if (!StringUtils.isEmpty(qo.getEndDate())) {
            sqlBuffer.append(" and DATE_FORMAT(t.CREATED_TIME,'%Y-%m-%d') <= ? ");
            paramsList.add(qo.getEndDate().trim());
        }


        sqlBuffer.append(" order by t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<EnterpriseDocumentInfoValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new EnterpriseDocumentInfoRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;
    }


}
