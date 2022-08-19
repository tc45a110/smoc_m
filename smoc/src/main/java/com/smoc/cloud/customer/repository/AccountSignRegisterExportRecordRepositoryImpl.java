package com.smoc.cloud.customer.repository;

import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.customer.validator.AccountSignRegisterExportRecordValidator;
import com.smoc.cloud.customer.rowmapper.AccountSignRegisterExportRecordRowMapper;

public class AccountSignRegisterExportRecordRepositoryImpl extends BasePageRepository {

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public PageList<AccountSignRegisterExportRecordValidator> page(PageParams pageParams){

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select");
        sqlBuffer.append(" t.ID");
        sqlBuffer.append(",t.REGISTER_ORDER_NO");
        sqlBuffer.append(",t.CARRIER");
        sqlBuffer.append(",t.REGISTER_NUMBER");
        sqlBuffer.append(",t.REGISTER_STATUS");
        sqlBuffer.append(",t.CREATED_BY");
        sqlBuffer.append(",DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME");
        sqlBuffer.append("  from account_sign_register_export_record t order by t.CREATED_TIME desc ");

        PageList<AccountSignRegisterExportRecordValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(),null, pageParams.getCurrentPage(), pageParams.getPageSize(), new AccountSignRegisterExportRecordRowMapper());

        return pageList;
    }
}
