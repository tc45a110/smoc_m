package com.smoc.cloud.customer.repository;


import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBasicInfoValidator;
import com.smoc.cloud.customer.rowmapper.EnterpriseBasicInfoRowMapper;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 2020/5/28 15:44
 **/
public class EnterpriseRepositoryImpl extends BasePageRepository {


    public PageList<EnterpriseBasicInfoValidator> page(PageParams<EnterpriseBasicInfoValidator> pageParams) {

        //查询条件
        EnterpriseBasicInfoValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ENTERPRISE_ID");
        sqlBuffer.append(", t.ENTERPRISE_PARENT_ID");
        sqlBuffer.append(", t.ENTERPRISE_NAME");
        sqlBuffer.append(", t.ENTERPRISE_TYPE");
        sqlBuffer.append(", t.ACCESS_CORPORATION");
        sqlBuffer.append(", t.ENTERPRISE_CONTACTS");
        sqlBuffer.append(", t.ENTERPRISE_CONTACTS_PHONE");
        sqlBuffer.append(", t.SALER");
        sqlBuffer.append(", t.ENTERPRISE_PROCESS");
        sqlBuffer.append(", t.ENTERPRISE_STATUS");
        sqlBuffer.append(", DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME");
        sqlBuffer.append("  from enterprise_basic_info t ");
        sqlBuffer.append("  where t.ENTERPRISE_PARENT_ID='0000' ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getEnterpriseName())) {
            sqlBuffer.append(" and t.ENTERPRISE_NAME like ?");
            paramsList.add( "%"+qo.getEnterpriseName().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getEnterpriseContacts())) {
            sqlBuffer.append(" and t.ENTERPRISE_CONTACTS like ?");
            paramsList.add( "%"+qo.getEnterpriseContacts().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getEnterpriseContactsPhone())) {
            sqlBuffer.append(" and t.ENTERPRISE_CONTACTS_PHONE like ?");
            paramsList.add( "%"+qo.getEnterpriseContactsPhone().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getSaler())) {
            sqlBuffer.append(" and t.SALER =?");
            paramsList.add( qo.getSaler().trim());
        }

        if (!StringUtils.isEmpty(qo.getEnterpriseType())) {
            sqlBuffer.append(" and t.ENTERPRISE_TYPE =?");
            paramsList.add(qo.getEnterpriseType().trim());
        }

        if (!StringUtils.isEmpty(qo.getAccessCorporation())) {
            sqlBuffer.append(" and t.ACCESS_CORPORATION =?");
            paramsList.add( qo.getAccessCorporation().trim());
        }

        if(!StringUtils.isEmpty(qo.getFlag()) && "1".equals(qo.getFlag())){
            sqlBuffer.append(" and t.ENTERPRISE_TYPE !='IDENTIFICATION' ");
        }

        sqlBuffer.append(" order by t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<EnterpriseBasicInfoValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new EnterpriseBasicInfoRowMapper());
        pageList.getPageParams().setParams(qo);

        //查询二级企业信息
        List<EnterpriseBasicInfoValidator> list = pageList.getList();
        for(int i =0;i<list.size();i++){
            EnterpriseBasicInfoValidator basicInfo = list.get(i);
            List<EnterpriseBasicInfoValidator> validator = findByEnterpriseParentId(basicInfo);
            basicInfo.setEnterprises(validator);
        }

        return pageList;
    }

    public List<EnterpriseBasicInfoValidator> findByEnterpriseParentId(EnterpriseBasicInfoValidator qo) {
        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ENTERPRISE_ID");
        sqlBuffer.append(", t.ENTERPRISE_PARENT_ID");
        sqlBuffer.append(", t.ENTERPRISE_NAME");
        sqlBuffer.append(", t.ENTERPRISE_TYPE");
        sqlBuffer.append(", t.ACCESS_CORPORATION");
        sqlBuffer.append(", t.ENTERPRISE_CONTACTS");
        sqlBuffer.append(", t.ENTERPRISE_CONTACTS_PHONE");
        sqlBuffer.append(", t.SALER");
        sqlBuffer.append(", t.ENTERPRISE_PROCESS");
        sqlBuffer.append(", t.ENTERPRISE_STATUS");
        sqlBuffer.append(", DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME");
        sqlBuffer.append("  from enterprise_basic_info t ");
        sqlBuffer.append("  where 1=1 ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getEnterpriseId())) {
            sqlBuffer.append(" and t.ENTERPRISE_PARENT_ID =?");
            paramsList.add( qo.getEnterpriseId().trim());
        }

        sqlBuffer.append(" order by t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        List<EnterpriseBasicInfoValidator> list = this.queryForObjectList(sqlBuffer.toString(), params, new EnterpriseBasicInfoRowMapper());
        return list;
    }

}
