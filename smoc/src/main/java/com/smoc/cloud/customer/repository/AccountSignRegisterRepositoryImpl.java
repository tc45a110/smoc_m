package com.smoc.cloud.customer.repository;

import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.customer.validator.AccountSignRegisterValidator;
import com.smoc.cloud.customer.rowmapper.AccountSignRegisterRowMapper;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class AccountSignRegisterRepositoryImpl extends BasePageRepository {

    /**
     * 分页查询
     * @param pageParams
     * @return
     */
    public PageList<AccountSignRegisterValidator> page(PageParams<AccountSignRegisterValidator> pageParams){
        //查询条件
        AccountSignRegisterValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select");
        sqlBuffer.append("  t.ID");
        sqlBuffer.append(", t.ACCOUNT");
        sqlBuffer.append(", t.SIGN");
        sqlBuffer.append(", t.SIGN_EXTEND_NUMBER");
        sqlBuffer.append(", t.EXTEND_TYPE");
        sqlBuffer.append(", t.EXTEND_DATA");
        sqlBuffer.append(", t.ENTERPRISE_ID");
        sqlBuffer.append(", t.APP_NAME");
        sqlBuffer.append(", t.SERVICE_TYPE");
        sqlBuffer.append(", t.MAIN_APPLICATION");
        sqlBuffer.append(", t.REGISTER_STATUS");
        sqlBuffer.append(", t.CREATED_BY");
        sqlBuffer.append(", DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME");
        sqlBuffer.append("  from account_sign_register t ");
        sqlBuffer.append("  where 1=1");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getAccount())) {
            sqlBuffer.append(" and t.ACCOUNT = ?");
            paramsList.add(qo.getAccount().trim());
        }

        if (!StringUtils.isEmpty(qo.getSign())) {
            sqlBuffer.append(" and t.SIGN like ?");
            paramsList.add("%" + qo.getSign().trim() + "%");
        }

        sqlBuffer.append(" order by t.CREATED_TIME desc ");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<AccountSignRegisterValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new AccountSignRegisterRowMapper());
        pageList.getPageParams().setParams(qo);

        return pageList;
    }
}
