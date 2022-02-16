package com.smoc.cloud.message.repository;

import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.message.MessageDetailInfoValidator;
import com.smoc.cloud.message.rowmapper.MessageDetailInfoRowMapper;

import java.util.ArrayList;
import java.util.List;

public class MessageDetailInfoRepositoryImpl extends BasePageRepository {

    /**
     * 分页查询
     * @param pageParams
     * @return
     */
    public PageList<MessageDetailInfoValidator> page(PageParams<MessageDetailInfoValidator> pageParams){

        //查询条件
        MessageDetailInfoValidator qo = pageParams.getParams();

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

        PageList<MessageDetailInfoValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new MessageDetailInfoRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;

    }
}
