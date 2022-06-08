package com.smoc.cloud.message.repository;

import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.message.MessageHttpsTaskInfoValidator;
import com.smoc.cloud.common.smoc.message.model.StatisticMessageSendData;
import com.smoc.cloud.message.rowmapper.MessageHttpTaskInfoRowMapper;
import com.smoc.cloud.message.rowmapper.StatisticMessageSendNumberRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MessageHttpsTaskInfoRepositoryImpl extends BasePageRepository {

    /**
     * 查询自服务http列表
     *
     * @param pageParams
     * @return
     */
    public PageList<MessageHttpsTaskInfoValidator> page(PageParams<MessageHttpsTaskInfoValidator> pageParams) {

        //查询条件
        MessageHttpsTaskInfoValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");

        sqlBuffer.append(" t.ID,");
        sqlBuffer.append(" t.TEMPLATE_ID,");
        sqlBuffer.append(" t.BUSINESS_ACCOUNT,");
        sqlBuffer.append(" t.MESSAGE_CONTENT,");
        sqlBuffer.append(" t.SPLIT_NUMBER,");
        sqlBuffer.append(" t.SUBMIT_NUMBER,");
        sqlBuffer.append(" t.SUCCESS_NUMBER,");
        sqlBuffer.append(" t.SUCCESS_SEND_NUMBER,");
        sqlBuffer.append(" DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME ");
        sqlBuffer.append(" from message_https_task_info t ");
        sqlBuffer.append(" where 1=1 ");

        List<Object> paramsList = new ArrayList<Object>();

        //任务Id
        if (!StringUtils.isEmpty(qo.getId())) {
            sqlBuffer.append(" and t.ID =?");
            paramsList.add(qo.getId().trim());
        }

        //企业名称
        if (!StringUtils.isEmpty(qo.getEnterpriseId())) {
            sqlBuffer.append(" and t.ENTERPRISE_ID = ? ");
            paramsList.add(qo.getEnterpriseId().trim() );
        }

        //业务账号
        if (!StringUtils.isEmpty(qo.getBusinessAccount())) {
            sqlBuffer.append(" and t.BUSINESS_ACCOUNT like ?");
            paramsList.add("%" + qo.getBusinessAccount().trim() + "%");
        }

        //模板ID
        if (!StringUtils.isEmpty(qo.getTemplateId())) {
            sqlBuffer.append(" and t.TEMPLATE_ID =?");
            paramsList.add(qo.getTemplateId().trim());
        }

        //业务类型
        if (!StringUtils.isEmpty(qo.getBusinessType())) {
            sqlBuffer.append(" and t.BUSINESS_TYPE =?");
            paramsList.add(qo.getBusinessType().trim());
        }

        //信息分类
        if (!StringUtils.isEmpty(qo.getInfoType())) {
            sqlBuffer.append(" and t.INFO_TYPE =?");
            paramsList.add(qo.getInfoType().trim());
        }

        //短信类型
        if (!StringUtils.isEmpty(qo.getMessageType())) {
            sqlBuffer.append(" and t.MESSAGE_TYPE =?");
            paramsList.add(qo.getMessageType().trim());
        }

        //短信内容
        if (!StringUtils.isEmpty(qo.getMessageContent())) {
            sqlBuffer.append(" and t.MESSAGE_CONTENT like ? ");
            paramsList.add("%" + qo.getMessageContent().trim() + "%");
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
        PageList<MessageHttpsTaskInfoValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new MessageHttpTaskInfoRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;
    }

    /**
     * 自服务平台不同维度统计发送量
     * @param pageParams
     * @return
     */
    public PageList<StatisticMessageSendData> messageSendNumberList(PageParams<StatisticMessageSendData> pageParams) {

        //查询条件
        StatisticMessageSendData qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        String groupBySql = "";
        //按账号维度查询
        if("1".equals(qo)){
            sqlBuffer.append(" t.BUSINESS_ACCOUNT,");
            sqlBuffer.append(" ''PROTOCOL,");
            sqlBuffer.append(" ''SIGN,");
            sqlBuffer.append(" ''CARRIER,");
            sqlBuffer.append(" sum(t.SUCCESS_NUMBER),");
            sqlBuffer.append(" sum(t.SUCCESS_SEND_NUMBER),");
            sqlBuffer.append(" sum(t.FAILURE_NUMBER),");
            sqlBuffer.append(" sum(t.NO_REPORT_NUMBER),");
            sqlBuffer.append(" DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d')CREATED_TIME ");
            sqlBuffer.append(" from message_https_task_info t ");
            sqlBuffer.append(" where 1=1 ");

            groupBySql = "group by DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d'),t.BUSINESS_ACCOUNT ";
        }
        //按签名维度
        else if("2".equals(qo)){
            sqlBuffer.append(" ''SIGN,");
            sqlBuffer.append(" ''BUSINESS_ACCOUNT,");
            sqlBuffer.append(" ''PROTOCOL,");
            sqlBuffer.append(" ''CARRIER,");
            sqlBuffer.append(" sum(t.SUCCESS_NUMBER)SUCCESS_NUMBER,");
            sqlBuffer.append(" sum(t.SUCCESS_SEND_NUMBER)SUCCESS_SEND_NUMBER,");
            sqlBuffer.append(" sum(t.FAILURE_NUMBER)FAILURE_NUMBER,");
            sqlBuffer.append(" sum(t.NO_REPORT_NUMBER)NO_REPORT_NUMBER,");
            sqlBuffer.append(" DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d')CREATED_TIME ");
            sqlBuffer.append(" from message_https_task_info t ");
            sqlBuffer.append(" where 1=1 ");

            groupBySql = "group by DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d') ";
        }
        //按运营商维度
        else if("2".equals(qo)){
            sqlBuffer.append(" ''CARRIER,");
            sqlBuffer.append(" ''BUSINESS_ACCOUNT,");
            sqlBuffer.append(" ''PROTOCOL,");
            sqlBuffer.append(" ''SIGN,");
            sqlBuffer.append(" sum(t.SUCCESS_NUMBER)SUCCESS_NUMBER,");
            sqlBuffer.append(" sum(t.SUCCESS_SEND_NUMBER)SUCCESS_SEND_NUMBER,");
            sqlBuffer.append(" sum(t.FAILURE_NUMBER)FAILURE_NUMBER,");
            sqlBuffer.append(" sum(t.NO_REPORT_NUMBER)NO_REPORT_NUMBER,");
            sqlBuffer.append(" DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d')CREATED_TIME ");
            sqlBuffer.append(" from message_https_task_info t ");
            sqlBuffer.append(" where 1=1 ");

            groupBySql = "group by DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d') ";
        }else{
            sqlBuffer.append(" ''CARRIER,");
            sqlBuffer.append(" t.BUSINESS_ACCOUNT,");
            sqlBuffer.append(" ''PROTOCOL,");
            sqlBuffer.append(" ''SIGN,");
            sqlBuffer.append(" sum(t.SUCCESS_NUMBER)SUCCESS_NUMBER,");
            sqlBuffer.append(" sum(t.SUCCESS_SEND_NUMBER)SUCCESS_SEND_NUMBER,");
            sqlBuffer.append(" sum(t.FAILURE_NUMBER)FAILURE_NUMBER,");
            sqlBuffer.append(" sum(t.NO_REPORT_NUMBER)NO_REPORT_NUMBER,");
            sqlBuffer.append(" DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d')CREATED_TIME ");
            sqlBuffer.append(" from message_https_task_info t ");
            sqlBuffer.append(" where 1=1 ");

            groupBySql = "group by DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d'),t.BUSINESS_ACCOUNT ";
        }



        List<Object> paramsList = new ArrayList<Object>();

        //业务ID
        if (!StringUtils.isEmpty(qo.getEnterpriseId())) {
            sqlBuffer.append(" and t.ENTERPRISE_ID =?");
            paramsList.add(qo.getEnterpriseId().trim());
        }

        //业务账号
        if (!StringUtils.isEmpty(qo.getBusinessAccount())) {
            sqlBuffer.append(" and t.BUSINESS_ACCOUNT =?");
            paramsList.add(qo.getBusinessAccount().trim());
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

        sqlBuffer.append(groupBySql);
        sqlBuffer.append(" order by t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<StatisticMessageSendData> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new StatisticMessageSendNumberRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;

    }
}
