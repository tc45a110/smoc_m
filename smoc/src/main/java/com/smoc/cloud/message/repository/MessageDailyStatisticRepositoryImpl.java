package com.smoc.cloud.message.repository;

import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.message.MessageDailyStatisticValidator;

import com.smoc.cloud.message.rowmapper.MessageDailyStatisticRowMapper;


import java.util.ArrayList;
import java.util.List;

public class MessageDailyStatisticRepositoryImpl extends BasePageRepository {

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public PageList<MessageDailyStatisticValidator> page(PageParams<MessageDailyStatisticValidator> pageParams) {

        //查询条件
        MessageDailyStatisticValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");

        List<Object> paramsList = new ArrayList<Object>();


//        if (!StringUtils.isEmpty(qo.getAccountId())) {
//            sqlBuffer.append(" and t.ACCOUNT_ID =?");
//            paramsList.add(qo.getAccountId().trim());
//        }

        sqlBuffer.append(" order by t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<MessageDailyStatisticValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new MessageDailyStatisticRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;

    }
}
