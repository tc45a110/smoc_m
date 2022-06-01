package com.smoc.cloud.message.repository;

import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.message.MessageMoInfoValidator;
import com.smoc.cloud.message.rowmapper.MessageMoInfoRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MessageMoInfoRepositoryImpl extends BasePageRepository {

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public  PageList<MessageMoInfoValidator> page(PageParams<MessageMoInfoValidator> pageParams) {

        //查询条件
        MessageMoInfoValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");

        sqlBuffer.append(" t.ID,");
        sqlBuffer.append(" t.ACCOUNT_ID,");
        sqlBuffer.append(" a.ACCOUNT_NAME,");
        sqlBuffer.append(" i.PROTOCOL,");
        sqlBuffer.append(" t.CHANNEL_ID,");
        sqlBuffer.append(" t.MO_SRC_ID,");
        sqlBuffer.append(" t.TASK_ID,");
        sqlBuffer.append(" t.BUSINESS_TYPE,");
        sqlBuffer.append(" t.INFO_TYPE,");
        sqlBuffer.append(" t.MOBILE,");
        sqlBuffer.append(" t.MO_MESSAGE_CONTENT,");
        sqlBuffer.append(" t.MT_MESSAGE_CONTENT,");
        sqlBuffer.append(" t.CARRIER,");
        sqlBuffer.append(" t.AREA,");
        sqlBuffer.append(" DATE_FORMAT(t.MO_DATE, '%Y-%m-%d %H:%i:%S')MO_DATE, ");
        sqlBuffer.append(" DATE_FORMAT(t.MT_DATE, '%Y-%m-%d %H:%i:%S')MT_DATE ");
        sqlBuffer.append(" from message_mo_info t left join account_base_info a on t.ACCOUNT_ID = a.ACCOUNT_ID ");
        sqlBuffer.append(" left join account_interface_info i on t.ACCOUNT_ID = i.ACCOUNT_ID ");
        sqlBuffer.append(" where 1=1 ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getEnterpriseId())) {
            sqlBuffer.append(" and t.ENTERPRISE_ID = ? ");
            paramsList.add(qo.getEnterpriseId().trim() );
        }

        if (!StringUtils.isEmpty(qo.getBusinessType())) {
            sqlBuffer.append(" and t.BUSINESS_TYPE = ? ");
            paramsList.add(qo.getBusinessType().trim() );
        }

        if (!StringUtils.isEmpty(qo.getInfoType())) {
            sqlBuffer.append(" and t.INFO_TYPE = ? ");
            paramsList.add(qo.getInfoType().trim() );
        }

        if (!StringUtils.isEmpty(qo.getTaskId())) {
            sqlBuffer.append(" and t.TASK_ID like ? ");
            paramsList.add("%" + qo.getTaskId().trim()+ "%" );
        }

        if (!StringUtils.isEmpty(qo.getAccountId())) {
            sqlBuffer.append(" and t.ACCOUNT_ID like ? ");
            paramsList.add("%" + qo.getAccountId().trim()+ "%" );
        }

        if (!StringUtils.isEmpty(qo.getMobile())) {
            sqlBuffer.append(" and t.MOBILE like ? ");
            paramsList.add("%" + qo.getMobile().trim()+ "%" );
        }

        if (!StringUtils.isEmpty(qo.getArea())) {
            sqlBuffer.append(" and t.AREA like ? ");
            paramsList.add("%" + qo.getArea().trim() + "%");
        }

        if (!StringUtils.isEmpty(qo.getChannelId())) {
            sqlBuffer.append(" and t.CHANNEL_ID like ? ");
            paramsList.add("%" + qo.getChannelId().trim() + "%");
        }

        if (!StringUtils.isEmpty(qo.getMoMessageContent())) {
            sqlBuffer.append(" and t.MO_MESSAGE_CONTENT like ? ");
            paramsList.add("%" + qo.getMoMessageContent().trim() + "%");
        }

        if (!StringUtils.isEmpty(qo.getMtMessageContent())) {
            sqlBuffer.append(" and t.MT_MESSAGE_CONTENT like ? ");
            paramsList.add("%" + qo.getMtMessageContent().trim() + "%");
        }

        //时间起
        if (!StringUtils.isEmpty(qo.getStartDate())) {
            sqlBuffer.append(" and DATE_FORMAT(t.MO_DATE,'%Y-%m-%d') >=? ");
            paramsList.add(qo.getStartDate().trim());
        }
        //时间止
        if (!StringUtils.isEmpty(qo.getEndDate())) {
            sqlBuffer.append(" and DATE_FORMAT(t.MO_DATE,'%Y-%m-%d') <=? ");
            paramsList.add(qo.getEndDate().trim());
        }

        //下行时间起
        if (!StringUtils.isEmpty(qo.getMtStartDate())) {
            sqlBuffer.append(" and DATE_FORMAT(t.MT_DATE,'%Y-%m-%d') >=? ");
            paramsList.add(qo.getMtStartDate().trim());
        }
        //下行时间止
        if (!StringUtils.isEmpty(qo.getMtEndDate())) {
            sqlBuffer.append(" and DATE_FORMAT(t.MT_DATE,'%Y-%m-%d') <=? ");
            paramsList.add(qo.getMtEndDate().trim());
        }

        sqlBuffer.append(" order by t.MO_DATE desc");
        //log.info("[SQL]:{}",sqlBuffer);
        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);
        PageList<MessageMoInfoValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new MessageMoInfoRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;
    }

}
