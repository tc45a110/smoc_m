package com.smoc.cloud.customer.repository;

import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseSignCertifyValidator;
import com.smoc.cloud.customer.rowmapper.EnterpriseSignCertifyRowMapper;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class EnterpriseSignCertifyRepositoryImpl extends BasePageRepository {

    /**
     * 分页查询
     * @param pageParams
     * @return
     */
    public PageList<EnterpriseSignCertifyValidator> page(PageParams<EnterpriseSignCertifyValidator> pageParams){
        //查询条件
        EnterpriseSignCertifyValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select");
        sqlBuffer.append("  t.ID");
        sqlBuffer.append(", t.REGISTER_ENTERPRISE_ID");
        sqlBuffer.append(", t.REGISTER_ENTERPRISE_NAME");
        sqlBuffer.append(", t.SOCIAL_CREDIT_CODE");
        sqlBuffer.append(", t.BUSINESS_LICENSE");
        sqlBuffer.append(", t.PERSON_LIABLE_NAME");
        sqlBuffer.append(", t.PERSON_LIABLE_CERTIFICATE_TYPE");
        sqlBuffer.append(", t.PERSON_LIABLE_CERTIFICATE_NUMBER");
        sqlBuffer.append(", t.PERSON_LIABLE_CERTIFICATE_URL");
        sqlBuffer.append(", t.PERSON_HANDLED_NAME");
        sqlBuffer.append(", t.PERSON_HANDLED_CERTIFICATE_TYPE");
        sqlBuffer.append(", t.PERSON_HANDLED_CERTIFICATE_NUMBER");
        sqlBuffer.append(", t.PERSON_HANDLED_CERTIFICATE_URL");
        sqlBuffer.append(", t.AUTHORIZE_CERTIFICATE");
        sqlBuffer.append(", t.AUTHORIZE_START_DATE");
        sqlBuffer.append(", t.AUTHORIZE_EXPIRE_DATE");
        sqlBuffer.append(", t.POSITION");
        sqlBuffer.append(", t.OFFICE_PHOTOS");
        sqlBuffer.append(", t.CERTIFY_STATUS");
        sqlBuffer.append(", t.CREATED_BY");
        sqlBuffer.append(", DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME");
        sqlBuffer.append("  from enterprise_sign_certify t");
        sqlBuffer.append("  where t.CERTIFY_STATUS='1' ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getRegisterEnterpriseId())) {
            sqlBuffer.append(" and t.REGISTER_ENTERPRISE_ID = ?");
            paramsList.add(qo.getRegisterEnterpriseId().trim());
        }

        if (!StringUtils.isEmpty(qo.getRegisterEnterpriseName())) {
            sqlBuffer.append(" and t.REGISTER_ENTERPRISE_NAME like ?");
            paramsList.add("%" + qo.getRegisterEnterpriseName().trim() + "%");
        }

        sqlBuffer.append(" order by t.CREATED_TIME desc ");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<EnterpriseSignCertifyValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new EnterpriseSignCertifyRowMapper());
        pageList.getPageParams().setParams(qo);

        return pageList;
    }
}
