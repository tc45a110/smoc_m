package com.smoc.cloud.message.repository;

import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.message.MessageDetailInfoValidator;
import com.smoc.cloud.message.rowmapper.MessageDetailInfoRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MessageDetailInfoRepositoryImpl extends BasePageRepository {

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public PageList<MessageDetailInfoValidator> page(PageParams<MessageDetailInfoValidator> pageParams) {

        //查询条件
        MessageDetailInfoValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");

        sqlBuffer.append(" t.ID,");
        sqlBuffer.append(" t.BUSINESS_ACCOUNT,");
        sqlBuffer.append(" t.PHONE_NUMBER,");
        sqlBuffer.append(" t.SUBMIT_TIME,");
        sqlBuffer.append(" t.SEND_NUMBER,");
        sqlBuffer.append(" t.SUBMIT_STYLE,");
        sqlBuffer.append(" t.SIGN,");
        sqlBuffer.append(" t.CARRIER,");
        sqlBuffer.append(" t.AREA,");
        sqlBuffer.append(" t.CUSTOMER_SUBMIT_STATUS,");
        sqlBuffer.append(" t.SEND_TIME,");
        sqlBuffer.append(" t.CHANNEL_ID,");
        sqlBuffer.append(" t.REPORT_TIME,");
        sqlBuffer.append(" t.REPORT_STATUS,");
        sqlBuffer.append(" t.CUSTOMER_REPORT_TIME,");
        sqlBuffer.append(" t.DELAY_TIMES,");
        sqlBuffer.append(" t.TOTAL_DELAY_TIMES,");
        sqlBuffer.append(" t.CUSTOMER_STATUS,");
        sqlBuffer.append(" t.MESSAGE_CONTENT,");
        sqlBuffer.append(" t.CREATED_BY,");
        sqlBuffer.append(" DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME ");
        sqlBuffer.append(" from message_detail_info t ");
        sqlBuffer.append(" where (1=1) ");

        List<Object> paramsList = new ArrayList<Object>();

        //手机号
        if (!StringUtils.isEmpty(qo.getPhoneNumber())) {
            sqlBuffer.append(" and t.PHONE_NUMBER =?");
            paramsList.add(qo.getPhoneNumber().trim());
        }

        //业务账号
        if (!StringUtils.isEmpty(qo.getBusinessAccount())) {
            sqlBuffer.append(" and t.BUSINESS_ACCOUNT =?");
            paramsList.add(qo.getBusinessAccount().trim());
        }

        //运营商
        if (!StringUtils.isEmpty(qo.getCarrier())) {
            sqlBuffer.append(" and t.CARRIER =?");
            paramsList.add(qo.getCarrier().trim());
        }

        //时间起
        if (!StringUtils.isEmpty(qo.getStartDate())) {
            sqlBuffer.append(" and DATE_FORMAT(t.CREATED_TIME,'%Y-%m-%d') >=? ");
            paramsList.add(qo.getStartDate().trim());
        }
        //时间止
        if (!StringUtils.isEmpty(qo.getEndDate())) {
            sqlBuffer.append(" and DATE_FORMAT(t.CREATED_TIME,'%Y-%m-%d') <=? ");
            paramsList.add(qo.getEndDate().trim());
        }

        sqlBuffer.append(" order by t.CREATED_TIME desc");
        //log.info("[SQL]:{}",sqlBuffer);
        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);
        //log.info("[SQL1]:{}",sqlBuffer);
        PageList<MessageDetailInfoValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new MessageDetailInfoRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;

    }

    public int statisticMessageNumber(MessageDetailInfoValidator qo){

        //查询sql
        StringBuffer sqlBuffer =  new StringBuffer("select count(t.id) from message_detail_info t where 1=1 ");

        List<Object> paramsList = new ArrayList<Object>();

        //手机号
        if (!StringUtils.isEmpty(qo.getPhoneNumber())) {
            sqlBuffer.append(" and t.PHONE_NUMBER =?");
            paramsList.add(qo.getPhoneNumber().trim());
        }

        //时间起
        if (!StringUtils.isEmpty(qo.getStartDate())) {
            sqlBuffer.append(" and DATE_FORMAT(t.CREATED_TIME,'%Y-%m-%d') >=? ");
            paramsList.add(qo.getStartDate().trim());
        }
        //时间止
        if (!StringUtils.isEmpty(qo.getEndDate())) {
            sqlBuffer.append(" and DATE_FORMAT(t.CREATED_TIME,'%Y-%m-%d') <=? ");
            paramsList.add(qo.getEndDate().trim());
        }

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        int number = jdbcTemplate.queryForObject(sqlBuffer.toString(), params,Integer.class);

        return number;
    }
}
