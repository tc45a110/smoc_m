package com.smoc.cloud.customer.repository;

import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.customer.validator.AccountSignRegisterForFileValidator;
import com.smoc.cloud.customer.rowmapper.AccountSignRegisterForFileRowMapper;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class AccountSignRegisterForFileRepositoryImpl extends BasePageRepository {

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public PageList<AccountSignRegisterForFileValidator> page(PageParams<AccountSignRegisterForFileValidator> pageParams) {
        //查询条件
        AccountSignRegisterForFileValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select");
        sqlBuffer.append("  t.ID");
        sqlBuffer.append(", t.REGISTER_SIGN_ID");
        sqlBuffer.append(", t.ACCOUNT");
        sqlBuffer.append(", t.CHANNEL_ID");
        sqlBuffer.append(", t.CHANNEL_NAME");
        sqlBuffer.append(", t.ACCESS_PROVINCE");
        sqlBuffer.append(", t.REGISTER_CARRIER");
        sqlBuffer.append(", t.REGISTER_CODE_NUMBER");
        sqlBuffer.append(", t.REGISTER_EXTEND_NUMBER");
        sqlBuffer.append(", t.REGISTER_SIGN");
        sqlBuffer.append(", t.NUMBER_SEGMENT");
        sqlBuffer.append(", t.REGISTER_EXPIRE_DATE");
        sqlBuffer.append(", t.REGISTER_STATUS");
        sqlBuffer.append(", t.UPDATED_BY");
        sqlBuffer.append(", DATE_FORMAT(t.UPDATED_TIME, '%Y-%m-%d %H:%i:%S')UPDATED_TIME");
        sqlBuffer.append(", DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME");
        sqlBuffer.append("  from account_sign_register_for_file ");
        sqlBuffer.append("  where 1=1");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getAccount())) {
            sqlBuffer.append(" and t.ACCOUNT = ?");
            paramsList.add(qo.getAccount().trim());
        }

        if (!StringUtils.isEmpty(qo.getChannelId())) {
            sqlBuffer.append(" and t.CHANNEL_ID = ?");
            paramsList.add(qo.getChannelId().trim());
        }

        if (!StringUtils.isEmpty(qo.getChannelName())) {
            sqlBuffer.append(" and t.CHANNEL_NAME like ?");
            paramsList.add("%" + qo.getChannelName().trim() + "%");
        }

        if (!StringUtils.isEmpty(qo.getRegisterCodeNumber())) {
            sqlBuffer.append(" and t.REGISTER_CODE_NUMBER = ?");
            paramsList.add(qo.getRegisterCodeNumber().trim());
        }

        if (!StringUtils.isEmpty(qo.getNumberSegment())) {
            sqlBuffer.append(" and t.NUMBER_SEGMENT = ?");
            paramsList.add(qo.getNumberSegment().trim());
        }

        if (!StringUtils.isEmpty(qo.getRegisterExtendNumber())) {
            sqlBuffer.append(" and t.REGISTER_EXTEND_NUMBER = ?");
            paramsList.add(qo.getRegisterExtendNumber().trim());
        }

        if (!StringUtils.isEmpty(qo.getRegisterSign())) {
            sqlBuffer.append(" and t.REGISTER_SIGN like ?");
            paramsList.add("%" + qo.getRegisterSign().trim() + "%");
        }

        if (!StringUtils.isEmpty(qo.getRegisterStatus())) {
            sqlBuffer.append(" and t.REGISTER_STATUS = ?");
            paramsList.add(qo.getRegisterStatus().trim());
        }

        sqlBuffer.append(" order by t.REGISTER_STATUS asc ");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<AccountSignRegisterForFileValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new AccountSignRegisterForFileRowMapper());
        pageList.getPageParams().setParams(qo);

        return pageList;
    }

}
