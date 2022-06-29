package com.smoc.cloud.tablestore.repository;

import com.alicloud.openservices.tablestore.SyncClient;
import com.alicloud.openservices.tablestore.model.sql.*;
import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.message.MessageDetailInfoValidator;
import com.smoc.cloud.common.smoc.message.TableStoreMessageDetailInfoValidator;
import com.smoc.cloud.common.smoc.message.model.MessageTaskDetail;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.message.rowmapper.MessageDetailInfoRowMapper;
import com.smoc.cloud.message.rowmapper.MessageMessageRecordRowMapper;
import com.smoc.cloud.message.rowmapper.MessageTaskDetailRowMapper;
import com.smoc.cloud.tablestore.utils.TableStoreUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class TableStoreMessageDetailInfoRepository extends TableStorePageRepository {

    @Autowired
    private TableStoreUtils tableStoreUtils;

    public PageList<TableStoreMessageDetailInfoValidator> tableStorePage(PageParams<TableStoreMessageDetailInfoValidator> pageParams) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select * from access_log where 1=1 ");

        //查询条件
        TableStoreMessageDetailInfoValidator qo = pageParams.getParams();

        //手机号
        if (!StringUtils.isEmpty(qo.getPhoneNumber())) {
            sqlBuffer.append(" and phone_number = '"+qo.getPhoneNumber().trim()+"'");
        }

        //账号
        if (!StringUtils.isEmpty(qo.getBusinessAccount())) {
            sqlBuffer.append(" and account_id = '"+qo.getBusinessAccount().trim()+"'");
        }

        //开始时间：时间戳
        if (!StringUtils.isEmpty(qo.getStartDate())) {
            sqlBuffer.append(" and user_submit_time >= "+DateTimeUtils.getDateFormat(qo.getStartDate()).getTime()+"");
        }

        //结束时间：时间戳
        if (!StringUtils.isEmpty(qo.getEndDate())) {
            sqlBuffer.append(" and user_submit_time <= "+DateTimeUtils.getDateFormat(qo.getEndDate()).getTime()+"");
        }

        sqlBuffer.append(" order by user_submit_time desc ");

        return queryMessageDatail(sqlBuffer, pageParams);

    }

    //封装阿里云返回数据
    private List<TableStoreMessageDetailInfoValidator> querySQLRow(SQLResultSet resultSet) {
        List<TableStoreMessageDetailInfoValidator> list = new ArrayList<>();
        while (resultSet.hasNext()) {
            SQLRow row = resultSet.next();
            TableStoreMessageDetailInfoValidator messageDetailInfoValidator = new TableStoreMessageDetailInfoValidator();
            messageDetailInfoValidator.setPhoneNumber(row.getString("phone_number"));
            messageDetailInfoValidator.setBusinessAccount(row.getString("account_id"));
            messageDetailInfoValidator.setBusinessMessageFlag(row.getString("business_message_flag"));
            messageDetailInfoValidator.setUserSubmitTime(DateTimeUtils.getDateTimeFormat(new Date(row.getLong("user_submit_time"))));
           /* messageDetailInfoValidator.setUserSubmitType(row.getString("user_submit_type"));
            messageDetailInfoValidator.setBusinessMessageFlag(row.getString("business_message_flag"));
            messageDetailInfoValidator.setSegmentCarrier(row.getString("segment_carrier"));
            messageDetailInfoValidator.setBusinessCarrier(row.getString("business_carrier"));
            messageDetailInfoValidator.setAreaName(row.getString("area_name"));
            messageDetailInfoValidator.setCityName(row.getString("city_name"));
            messageDetailInfoValidator.setUserSubmitSrcid(row.getString("user_submit_srcid"));
            messageDetailInfoValidator.setExtcode(row.getString("extcode"));
            messageDetailInfoValidator.setBusinessCode(row.getString("business_code"));
            messageDetailInfoValidator.setMessageContent(row.getString("message_content"));
            messageDetailInfoValidator.setMessageNumber(row.getString("message_number"));
            messageDetailInfoValidator.setInfoType(row.getString("info_type"));
            messageDetailInfoValidator.setIndustryTypes(row.getString("industry_types"));
            messageDetailInfoValidator.setChannelId(row.getString("channel_id"));
            messageDetailInfoValidator.setChannelSrcid(row.getString("channel_srcid"));
            messageDetailInfoValidator.setFinanceAccountId(row.getString("finance_account_id"));
            messageDetailInfoValidator.setMessagePrice(row.getString("message_price"));
            messageDetailInfoValidator.setMessageFee(row.getString("message_fee"));
            messageDetailInfoValidator.setBusinessType(row.getString("business_type"));
            messageDetailInfoValidator.setTemplateId(row.getString("template_id"));
            messageDetailInfoValidator.setConsumeType(row.getString("consume_type"));
            messageDetailInfoValidator.setMrTime(DateTimeUtils.getDateTimeFormat(new Date(row.getLong("mr_time"))));
            messageDetailInfoValidator.setStatusCodeExtend(row.getString("status_code_extend"));*/

            list.add(messageDetailInfoValidator);
        }

        return list;
    }

    private PageList<TableStoreMessageDetailInfoValidator> queryMessageDatail(StringBuilder sqlBuffer, PageParams<TableStoreMessageDetailInfoValidator> pageParams) {

        //初始化
        SyncClient client = tableStoreUtils.client();

        //总页数
        int pages = 0;
        //查询总行数
        Long rowsColumn = (Long) this.queryOneColumnForSigetonRow(sqlBuffer.toString(),Long.class);
        int rows = rowsColumn.intValue();
        //开始行
        int startRow = 0;
        //结束行
        int endRow = 0;
        //每页数量
        int pageSize = pageParams.getPageSize();
        //当前页
        int currentPage = pageParams.getCurrentPage();
        //判断页数,如果是页大小的整数倍就为rows/pageRow如果不是整数倍就为rows/pageRow+1
        if (rows % pageSize == 0) {
            pages = rows / pageSize;
        } else {
            pages = rows / pageSize + 1;
        }

        if (currentPage > pages) {
            currentPage = pages;
        }

        //查询第page页的数据sql语句
        if (currentPage <= 1) {
            endRow = pageSize < rows ? pageSize : rows;
            sqlBuffer.append(" limit 0," + pageSize);
        } else {
            startRow = ((currentPage - 1) * pageSize);
            endRow = (((currentPage - 1) * pageSize) + pageSize) < rows ? (((currentPage - 1) * pageSize) + pageSize) : rows;
            sqlBuffer.append(" limit " + ((currentPage - 1) * pageSize) + "," + pageSize);
        }

        // 创建SQL请求。
        SQLQueryRequest request = new SQLQueryRequest(sqlBuffer.toString());
        // 获取SQL的响应结果。
        SQLQueryResponse response = client.sqlQuery(request);
        // 获取SQL返回值的Schema。
        SQLTableMeta tableMeta = response.getSQLResultSet().getSQLTableMeta();
        System.out.println("response table meta: " + tableMeta.getSchema());
        // 获取SQL的返回结果。
        SQLResultSet resultSet = response.getSQLResultSet();
        List<TableStoreMessageDetailInfoValidator> list = querySQLRow(resultSet);

        //返回分页格式数据
        PageList<TableStoreMessageDetailInfoValidator> pageList = new PageList<>();
        //设置当前页
        pageList.getPageParams().setCurrentPage(currentPage);
        //设置总页数
        pageList.getPageParams().setPages(pages);
        //设置每页显示条数
        pageList.getPageParams().setPageSize(pageSize);
        //设置总记录数
        pageList.getPageParams().setTotalRows(rows);
        //设置开始行
        pageList.getPageParams().setStartRow(startRow + 1);
        //设置结束行
        pageList.getPageParams().setEndRow(endRow);
        //设置当前页数据
        pageList.setList(list);
        return pageList;
    }

    /**
     * 根据手机号查询30天内的发送次数
     * @param qo
     * @return
     */
    public int statisticMessageNumberByMobile(MessageDetailInfoValidator qo) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select * from access_log where 1=1 ");

        //手机号
        if (!StringUtils.isEmpty(qo.getPhoneNumber())) {
            sqlBuffer.append(" and phone_number = '"+qo.getPhoneNumber().trim()+"'");
        }

        //开始时间：时间戳
        if (!StringUtils.isEmpty(qo.getStartDate())) {
            sqlBuffer.append(" and user_submit_time >= "+DateTimeUtils.getDateFormat(qo.getStartDate()).getTime()+"");
        }

        //结束时间：时间戳
        if (!StringUtils.isEmpty(qo.getEndDate())) {
            sqlBuffer.append(" and user_submit_time <= "+DateTimeUtils.getDateFormat(qo.getEndDate()).getTime()+"");
        }

        Long num = (Long) this.queryOneColumnForSigetonRow(sqlBuffer.toString(),Long.class);
        return num.intValue();
    }

    /**
     * 根据运营商、手机号、内容查询记录
     * @param carrier
     * @param reportNumber
     * @param reportContent
     * @return
     */
    public TableStoreMessageDetailInfoValidator findByCarrierAndPhoneNumberAndMessageContent(String carrier, String reportNumber, String reportContent) {
        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select * from access_log where 1=1 ");
        //手机号
        sqlBuffer.append(" and phone_number = '"+reportNumber.trim()+"'");
        //运营商
        sqlBuffer.append(" and segment_carrier = '"+carrier.trim()+"'");
        //运营商
        sqlBuffer.append(" and message_content = '"+reportContent.trim()+"'");
        sqlBuffer.append(" limit 0,1");

        //初始化
        SyncClient client = tableStoreUtils.client();
        // 创建SQL请求。
        SQLQueryRequest request = new SQLQueryRequest(sqlBuffer.toString());
        // 获取SQL的响应结果。
        SQLQueryResponse response = client.sqlQuery(request);
        // 获取SQL返回值的Schema。
        SQLTableMeta tableMeta = response.getSQLResultSet().getSQLTableMeta();
        // 获取SQL的返回结果。
        SQLResultSet resultSet = response.getSQLResultSet();

        TableStoreMessageDetailInfoValidator messageDetailInfoValidator =null;
        while (resultSet.hasNext()) {
            SQLRow row = resultSet.next();
            messageDetailInfoValidator = new TableStoreMessageDetailInfoValidator();
            messageDetailInfoValidator.setBusinessAccount(row.getString("account_id"));
            messageDetailInfoValidator.setUserSubmitTime(DateTimeUtils.getDateTimeFormat(new Date(row.getLong("user_submit_time"))));
            /*messageDetailInfoValidator.setChannelId(row.getString("channel_id"));
            messageDetailInfoValidator.setChannelSrcid(row.getString("channel_srcid"));*/
        }
        return messageDetailInfoValidator;
    }
}
