package com.smoc.cloud.message.repository;

import com.alicloud.openservices.tablestore.SyncClient;
import com.alicloud.openservices.tablestore.core.protocol.OtsInternalApi;
import com.alicloud.openservices.tablestore.model.sql.*;
import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.message.MessageDetailInfoValidator;
import com.smoc.cloud.common.smoc.message.model.MessageTaskDetail;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.message.rowmapper.MessageDetailInfoRowMapper;
import com.smoc.cloud.message.rowmapper.MessageMessageRecordRowMapper;
import com.smoc.cloud.message.rowmapper.MessageTaskDetailRowMapper;
import com.smoc.cloud.tablestore.utils.TableStoreUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StringUtils;

import java.util.*;

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
        StringBuilder sql = new StringBuilder("select ");
        sql.append(" count(*) messageTotal ");
        sql.append(" from information_schema.TABLES where TABLE_SCHEMA = 'smoc_route' AND TABLE_NAME = 'enterprise_message_mr_info_"+qo.getEnterpriseFlag()+"' ");
        Long messageTotal = jdbcTemplate.queryForObject(sql.toString(), Long.class);

        if(messageTotal>=1){
            //查询sql
            StringBuilder sqlBuffer = new StringBuilder("select ");
            sqlBuffer.append(" t.ID,");
            sqlBuffer.append(" t.ACCOUNT_ID,");
            sqlBuffer.append(" t.PHONE_NUMBER,");
            sqlBuffer.append(" t.CARRIER,");
            sqlBuffer.append(" t.AREA_NAME,");
            sqlBuffer.append(" t.REPORT_TIME,");
            sqlBuffer.append(" t.SUBMIT_TIME,");
            sqlBuffer.append(" t.STATUS_CODE,");
            sqlBuffer.append(" t.MESSAGE_CONTENT,");
            sqlBuffer.append(" t.MESSAGE_TOTAL,");
            sqlBuffer.append(" t.SIGN,");
            sqlBuffer.append(" t.SUBMIT_STYLE");
            sqlBuffer.append(" ,t.TIME_ELAPSED");
            sqlBuffer.append(" from smoc_route.enterprise_message_mr_info_"+qo.getEnterpriseFlag()+" t ");
            sqlBuffer.append(" where (1=1) ");

            List<Object> paramsList = new ArrayList<Object>();

            //手机号
            if (!StringUtils.isEmpty(qo.getPhoneNumber())) {
                sqlBuffer.append(" and t.PHONE_NUMBER =?");
                paramsList.add(qo.getPhoneNumber().trim());
            }

            //业务账号
            if (!StringUtils.isEmpty(qo.getBusinessAccount())) {
                sqlBuffer.append(" and t.ACCOUNT_ID =?");
                paramsList.add(qo.getBusinessAccount().trim());
            }

            //运营商
            if (!StringUtils.isEmpty(qo.getCarrier())) {
                sqlBuffer.append(" and t.CARRIER =?");
                paramsList.add(qo.getCarrier().trim());
            }

            //省份
            if (!StringUtils.isEmpty(qo.getArea())) {
                sqlBuffer.append(" and t.AREA_NAME =?");
                paramsList.add(qo.getArea().trim());
            }

            //状态码
            if (!StringUtils.isEmpty(qo.getCustomerStatus())) {
                sqlBuffer.append(" and t.STATUS_CODE =?");
                paramsList.add(qo.getCustomerStatus().trim());
            }

            //状态码
            if (!StringUtils.isEmpty(qo.getStatusFlag())) {
                if("success".equals(qo.getStatusFlag())){
                    sqlBuffer.append(" and t.STATUS_CODE = 'DELIVRD' ");
                }else{
                    sqlBuffer.append(" and (t.STATUS_CODE !='DELIVRD' or t.STATUS_CODE is null )");
                }
            }

            //签名
            if (!StringUtils.isEmpty(qo.getSign())) {
                sqlBuffer.append(" and t.SIGN like ?");
                paramsList.add("%"+qo.getSign().trim()+"%");
            }
            //接口类型
            if (!StringUtils.isEmpty(qo.getSubmitStyle())) {
                sqlBuffer.append(" and t.SUBMIT_STYLE =?");
                paramsList.add(qo.getSubmitStyle().trim());
            }

            //耗时
            if(!StringUtils.isEmpty(qo.getTimeElapsed())){
                String[] elapsed = qo.getTimeElapsed().split("-");
                if(!StringUtils.isEmpty(elapsed[0])){
                    sqlBuffer.append(" and t.TIME_ELAPSED >?");
                    paramsList.add(elapsed[0]);
                }
                if(elapsed.length>=2 && !StringUtils.isEmpty(elapsed[1])){
                    sqlBuffer.append(" and t.TIME_ELAPSED <=?");
                    paramsList.add(elapsed[1]);
                }
            }

            //时间起
            if (!StringUtils.isEmpty(qo.getStartDate())) {
                sqlBuffer.append(" and DATE_FORMAT(t.SUBMIT_TIME,'%Y-%m-%d %H:%i:%S') >=? ");
                paramsList.add(qo.getStartDate().trim());
            }
            //时间止
            if (!StringUtils.isEmpty(qo.getEndDate())) {
                sqlBuffer.append(" and DATE_FORMAT(t.SUBMIT_TIME,'%Y-%m-%d %H:%i:%S') <=? ");
                paramsList.add(qo.getEndDate().trim());
            }

            sqlBuffer.append(" order by t.SUBMIT_TIME desc");
            //log.info("[SQL]:{}",sqlBuffer);
            //根据参数个数，组织参数值
            Object[] params = new Object[paramsList.size()];
            paramsList.toArray(params);
            //log.info("[SQL1]:{}",sqlBuffer);
            PageList<MessageDetailInfoValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new MessageDetailInfoRowMapper());
            pageList.getPageParams().setParams(qo);
            return pageList;
        }


        //返回分页格式数据
        PageList<MessageDetailInfoValidator> pageList = new PageList<>();
        //设置当前页
        pageList.getPageParams().setCurrentPage(pageParams.getCurrentPage());
        //设置每页显示条数
        pageList.getPageParams().setPageSize(pageParams.getPageSize());
        //设置当前页数据
        pageList.setList(null);

        return pageList;
    }

    /**
     * 根据企业实时查询成功发送总量
     * @param qo
     * @return
     */
    public Map<String, Object> statisticEnterpriseSendMessage(MessageDetailInfoValidator qo) {

        //查询sql
        StringBuilder sql = new StringBuilder("select ");
        sql.append(" count(*) messageTotal ");
        sql.append(" from information_schema.TABLES where TABLE_SCHEMA = 'smoc_route' AND TABLE_NAME = 'enterprise_message_mr_info_"+qo.getEnterpriseFlag()+"' ");
        Long messageTotal = jdbcTemplate.queryForObject(sql.toString(), Long.class);

        if(messageTotal>=1){
            //查询sql
            StringBuilder sqlBuffer = new StringBuilder("select ");
            sqlBuffer.append(" count(*) SUCCESS_SEND_SUM ");
            sqlBuffer.append(" from smoc_route.enterprise_message_mr_info_"+qo.getEnterpriseFlag()+" t ");
            sqlBuffer.append(" where t.STATUS_CODE = 'DELIVRD' ");

            List<Object> paramsList = new ArrayList<Object>();

            //手机号
            if (!StringUtils.isEmpty(qo.getPhoneNumber())) {
                sqlBuffer.append(" and t.PHONE_NUMBER =?");
                paramsList.add(qo.getPhoneNumber().trim());
            }

            //业务账号
            if (!StringUtils.isEmpty(qo.getBusinessAccount())) {
                sqlBuffer.append(" and t.ACCOUNT_ID =?");
                paramsList.add(qo.getBusinessAccount().trim());
            }

            //运营商
            if (!StringUtils.isEmpty(qo.getCarrier())) {
                sqlBuffer.append(" and t.CARRIER =?");
                paramsList.add(qo.getCarrier().trim());
            }

            //省份
            if (!StringUtils.isEmpty(qo.getArea())) {
                sqlBuffer.append(" and t.AREA_NAME =?");
                paramsList.add(qo.getArea().trim());
            }

            //状态码
            if (!StringUtils.isEmpty(qo.getCustomerStatus())) {
                sqlBuffer.append(" and t.STATUS_CODE =?");
                paramsList.add(qo.getCustomerStatus().trim());
            }

            //状态码
            if (!StringUtils.isEmpty(qo.getStatusFlag())) {
                if("success".equals(qo.getStatusFlag())){
                    sqlBuffer.append(" and t.STATUS_CODE = 'DELIVRD' ");
                }else{
                    sqlBuffer.append(" and (t.STATUS_CODE !='DELIVRD' or t.STATUS_CODE is null ) ");
                }
            }

            //签名
            if (!StringUtils.isEmpty(qo.getSign())) {
                sqlBuffer.append(" and t.SIGN like ?");
                paramsList.add("%"+qo.getSign().trim()+"%");
            }
            //接口类型
            if (!StringUtils.isEmpty(qo.getSubmitStyle())) {
                sqlBuffer.append(" and t.SUBMIT_STYLE =?");
                paramsList.add(qo.getSubmitStyle().trim());
            }

            //耗时
            if(!StringUtils.isEmpty(qo.getTimeElapsed())){
                String[] elapsed = qo.getTimeElapsed().split("-");
                if(!StringUtils.isEmpty(elapsed[0])){
                    sqlBuffer.append(" and t.TIME_ELAPSED >?");
                    paramsList.add(elapsed[0]);
                }
                if(elapsed.length>=2 && !StringUtils.isEmpty(elapsed[1])){
                    sqlBuffer.append(" and t.TIME_ELAPSED <=?");
                    paramsList.add(elapsed[1]);
                }
            }

            //时间起
            if (!StringUtils.isEmpty(qo.getStartDate())) {
                sqlBuffer.append(" and DATE_FORMAT(t.SUBMIT_TIME,'%Y-%m-%d %H:%i:%S') >=? ");
                paramsList.add(qo.getStartDate().trim());
            }
            //时间止
            if (!StringUtils.isEmpty(qo.getEndDate())) {
                sqlBuffer.append(" and DATE_FORMAT(t.SUBMIT_TIME,'%Y-%m-%d %H:%i:%S') <=? ");
                paramsList.add(qo.getEndDate().trim());
            }

            //根据参数个数，组织参数值
            Object[] params = new Object[paramsList.size()];
            paramsList.toArray(params);

            Map<String, Object>  map = jdbcTemplate.queryForMap(sqlBuffer.toString(), params);
            return map;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("SUCCESS_SEND_SUM","0");

        return map;
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

    /**
     * 统计自服务平台短信明细列表
     * @param pageParams
     * @return
     */
    public PageList<MessageDetailInfoValidator> servicerPage(PageParams<MessageDetailInfoValidator> pageParams) {

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
        sqlBuffer.append(" from message_detail_info t left join account_base_info a on t.BUSINESS_ACCOUNT = a.ACCOUNT_ID ");
        sqlBuffer.append(" where (1=1) ");

        List<Object> paramsList = new ArrayList<Object>();

        //手机号
        if (!StringUtils.isEmpty(qo.getPhoneNumber())) {
            sqlBuffer.append(" and t.PHONE_NUMBER like ?");
            paramsList.add("%"+qo.getPhoneNumber().trim()+"%");
        }

        //企业
        if (!StringUtils.isEmpty(qo.getEnterpriseId())) {
            sqlBuffer.append(" and a.ENTERPRISE_ID =?");
            paramsList.add(qo.getEnterpriseId().trim());
        }

        //业务类型
        if (!StringUtils.isEmpty(qo.getBusinessType())) {
            sqlBuffer.append(" and a.BUSINESS_TYPE =?");
            paramsList.add(qo.getBusinessType().trim());
        }

        //业务账号
        if (!StringUtils.isEmpty(qo.getBusinessAccount())) {
            sqlBuffer.append(" and t.BUSINESS_ACCOUNT like ?");
            paramsList.add("%"+qo.getBusinessAccount().trim()+"%");
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

    /**
     * 查询自服务web短信明细列表
     * @param pageParams
     * @return
     */
    public PageList<MessageTaskDetail> webTaskDetailList(PageParams<MessageTaskDetail> pageParams) {

        //查询条件
        MessageTaskDetail qo = pageParams.getParams();

        //查询sql
        StringBuilder sql = new StringBuilder("select ");
        sql.append(" count(*) messageTotal ");
        sql.append(" from information_schema.TABLES where TABLE_SCHEMA = 'smoc_route' AND TABLE_NAME = 'enterprise_message_mr_info_"+qo.getEnterpriseFlag()+"' ");
        Long messageTotal = jdbcTemplate.queryForObject(sql.toString(), Long.class);

        if(messageTotal>=1){
            //查询sql
            StringBuilder sqlBuffer = new StringBuilder("select ");
            sqlBuffer.append(" t.PHONE_NUMBER,");
            sqlBuffer.append(" t.MESSAGE_CONTENT,");
            sqlBuffer.append(" t.MESSAGE_TOTAL as CHARGE_NUMBER,");
            sqlBuffer.append(" t.AREA_NAME,");
            sqlBuffer.append(" t.CARRIER,");
            sqlBuffer.append(" t.REPORT_TIME,");
            sqlBuffer.append(" t.STATUS_CODE");
            sqlBuffer.append(" from smoc_route.enterprise_message_mr_info_"+qo.getEnterpriseFlag()+" t ");
            sqlBuffer.append(" where 1=1 ");

            List<Object> paramsList = new ArrayList<Object>();

            //任务Id
            if (!StringUtils.isEmpty(qo.getTaskId())) {
                sqlBuffer.append(" and t.MESSAGE_ID = ?");
                paramsList.add(qo.getTaskId().trim());
            }

            //手机号
            if (!StringUtils.isEmpty(qo.getMobile())) {
                sqlBuffer.append(" and t.PHONE_NUMBER = ? ");
                paramsList.add(qo.getMobile().trim());
            }

            sqlBuffer.append(" order by t.REPORT_TIME desc");
            //log.info("[SQL]:{}",sqlBuffer);
            //根据参数个数，组织参数值
            Object[] params = new Object[paramsList.size()];
            paramsList.toArray(params);
            PageList<MessageTaskDetail> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new MessageTaskDetailRowMapper());
            pageList.getPageParams().setParams(qo);
            return pageList;
        }

        //返回分页格式数据
        PageList<MessageTaskDetail> pageList = new PageList<>();
        //设置当前页
        pageList.getPageParams().setCurrentPage(pageParams.getCurrentPage());
        //设置每页显示条数
        pageList.getPageParams().setPageSize(pageParams.getPageSize());
        //设置当前页数据
        pageList.setList(null);

        return pageList;
    }

    /**
     * 查询自服务http短信明细列表
     * @param pageParams
     * @return
     */
    public PageList<MessageTaskDetail> httpTaskDetailList(PageParams<MessageTaskDetail> pageParams) {

        //查询条件
        MessageTaskDetail qo = pageParams.getParams();

        //查询sql
        StringBuilder sql = new StringBuilder("select ");
        sql.append(" count(*) messageTotal ");
        sql.append(" from information_schema.TABLES where TABLE_SCHEMA = 'smoc_route' AND TABLE_NAME = 'enterprise_message_mr_info_"+qo.getEnterpriseFlag()+"' ");
        Long messageTotal = jdbcTemplate.queryForObject(sql.toString(), Long.class);

        if(messageTotal>=1){
            //查询sql
            StringBuilder sqlBuffer = new StringBuilder("select ");
            sqlBuffer.append(" t.PHONE_NUMBER,");
            sqlBuffer.append(" t.MESSAGE_CONTENT,");
            sqlBuffer.append(" t.MESSAGE_TOTAL as CHARGE_NUMBER,");
            sqlBuffer.append(" t.AREA_NAME,");
            sqlBuffer.append(" t.CARRIER,");
            sqlBuffer.append(" t.REPORT_TIME,");
            sqlBuffer.append(" t.STATUS_CODE");
            sqlBuffer.append(" from smoc_route.enterprise_message_mr_info_"+qo.getEnterpriseFlag()+" t ");
            sqlBuffer.append(" where 1=1 ");

            List<Object> paramsList = new ArrayList<Object>();

            //任务Id
            if (!StringUtils.isEmpty(qo.getTaskId())) {
                sqlBuffer.append(" and t.MESSAGE_ID like ?");
                paramsList.add("%" + qo.getTaskId().trim() + "%");
            }

            //手机号
            if (!StringUtils.isEmpty(qo.getMobile())) {
                sqlBuffer.append(" and t.PHONE_NUMBER = ? ");
                paramsList.add(qo.getMobile().trim());
            }

            sqlBuffer.append(" order by t.REPORT_TIME desc");
            //log.info("[SQL]:{}",sqlBuffer);
            //根据参数个数，组织参数值
            Object[] params = new Object[paramsList.size()];
            paramsList.toArray(params);
            PageList<MessageTaskDetail> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new MessageTaskDetailRowMapper());
            pageList.getPageParams().setParams(qo);
            return pageList;
        }

        //返回分页格式数据
        PageList<MessageTaskDetail> pageList = new PageList<>();
        //设置当前页
        pageList.getPageParams().setCurrentPage(pageParams.getCurrentPage());
        //设置每页显示条数
        pageList.getPageParams().setPageSize(pageParams.getPageSize());
        //设置当前页数据
        pageList.setList(null);

        return pageList;
    }

    /**
     * 单条短信发送记录
     * @param pageParams
     * @return
     */
    public PageList<MessageDetailInfoValidator> sendMessageList(PageParams<MessageDetailInfoValidator> pageParams) {

        //查询条件
        MessageDetailInfoValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sql = new StringBuilder("select ");
        sql.append(" count(*) messageTotal ");
        sql.append(" from information_schema.TABLES where TABLE_SCHEMA = 'smoc_route' AND TABLE_NAME = 'enterprise_message_mr_info_"+qo.getEnterpriseFlag()+"' ");
        Long messageTotal = jdbcTemplate.queryForObject(sql.toString(), Long.class);

        if(messageTotal>=1){
            //查询sql
            StringBuilder sqlBuffer = new StringBuilder("select ");
            sqlBuffer.append(" t.ACCOUNT_ID,");
            sqlBuffer.append(" t.PHONE_NUMBER,");
            sqlBuffer.append(" t.MESSAGE_ID,");
            sqlBuffer.append(" t.MESSAGE_CONTENT,");
            sqlBuffer.append(" t.AREA_NAME,");
            sqlBuffer.append(" t.CARRIER,");
            sqlBuffer.append(" t.SUBMIT_TIME,");
            sqlBuffer.append(" t.REPORT_TIME,");
            sqlBuffer.append(" t.STATUS_CODE,");
            sqlBuffer.append(" t.SUBMIT_STYLE PROTOCOL");
            sqlBuffer.append(" from smoc_route.enterprise_message_mr_info_"+qo.getEnterpriseFlag()+" t ");
            sqlBuffer.append(" where 1=1 ");

            List<Object> paramsList = new ArrayList<Object>();

            if (!StringUtils.isEmpty(qo.getTaskId())) {
                sqlBuffer.append(" and t.MESSAGE_ID like ?");
                paramsList.add("%" + qo.getTaskId().trim() + "%");
            }

            //手机号
            if (!StringUtils.isEmpty(qo.getPhoneNumber())) {
                sqlBuffer.append(" and t.PHONE_NUMBER = ?");
                paramsList.add(qo.getPhoneNumber().trim());
            }

            //业务账号
            if (!StringUtils.isEmpty(qo.getBusinessAccount())) {
                sqlBuffer.append(" and t.ACCOUNT_ID =?");
                paramsList.add(qo.getBusinessAccount().trim());
            }

            //运营商
            if (!StringUtils.isEmpty(qo.getCarrier())) {
                sqlBuffer.append(" and t.CARRIER =?");
                paramsList.add(qo.getCarrier().trim());
            }

            //时间起
            if (!StringUtils.isEmpty(qo.getStartDate())) {
                sqlBuffer.append(" and DATE_FORMAT(t.SUBMIT_TIME,'%Y-%m-%d') >=? ");
                paramsList.add(qo.getStartDate().trim());
            }
            //时间止
            if (!StringUtils.isEmpty(qo.getEndDate())) {
                sqlBuffer.append(" and DATE_FORMAT(t.SUBMIT_TIME,'%Y-%m-%d') <=? ");
                paramsList.add(qo.getEndDate().trim());
            }

            sqlBuffer.append(" order by t.CREATED_TIME desc");
            //log.info("[SQL]:{}",sqlBuffer);
            //根据参数个数，组织参数值
            Object[] params = new Object[paramsList.size()];
            paramsList.toArray(params);
            //log.info("[SQL1]:{}",sqlBuffer);
            PageList<MessageDetailInfoValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new MessageMessageRecordRowMapper());
            pageList.getPageParams().setParams(qo);
            return pageList;
        }

        //返回分页格式数据
        PageList<MessageDetailInfoValidator> pageList = new PageList<>();
        //设置当前页
        pageList.getPageParams().setCurrentPage(pageParams.getCurrentPage());
        //设置每页显示条数
        pageList.getPageParams().setPageSize(pageParams.getPageSize());
        //设置当前页数据
        pageList.setList(null);

        return pageList;
    }

}
