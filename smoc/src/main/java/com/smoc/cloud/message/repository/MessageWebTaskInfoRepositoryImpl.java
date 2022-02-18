package com.smoc.cloud.message.repository;

import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.template.MessageWebTaskInfoValidator;
import com.smoc.cloud.message.rowmapper.MessageWebTaskInfoRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MessageWebTaskInfoRepositoryImpl extends BasePageRepository {

    /**
     * 分页查询
     * @param pageParams
     * @return
     */
    public PageList<MessageWebTaskInfoValidator> page(PageParams<MessageWebTaskInfoValidator> pageParams){

        //查询条件
        MessageWebTaskInfoValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");

        sqlBuffer.append(" t.ID,");
        sqlBuffer.append(" t.SUBJECT,");
        sqlBuffer.append(" e.ENTERPRISE_NAME,");
        sqlBuffer.append(" t.TEMPLATE_ID,");
        sqlBuffer.append(" t.BUSINESS_ACCOUNT,");
        sqlBuffer.append(" t.BUSINESS_TYPE,");
        sqlBuffer.append(" t.SEND_TYPE,");
        sqlBuffer.append(" t.TIMING_TIME,");
        sqlBuffer.append(" t.EXPAND_NUMBER,");
        sqlBuffer.append(" t.SUBMIT_NUMBER,");
        sqlBuffer.append(" t.SUCCESS_NUMBER,");
        sqlBuffer.append(" t.SUCCESS_SEND_NUMBER,");
        sqlBuffer.append(" t.FAILURE_NUMBER,");
        sqlBuffer.append(" t.NO_REPORT_NUMBER,");
        sqlBuffer.append(" t.APPLE_SEND_TIME,");
        sqlBuffer.append(" t.SEND_TIME,");
        sqlBuffer.append(" t.SEND_STATUS,");
        sqlBuffer.append(" t.INPUT_NUMBER,");
        sqlBuffer.append(" t.NUMBER_FILES,");
        sqlBuffer.append(" t.MESSAGE_CONTENT,");
        sqlBuffer.append(" t.CREATED_BY,");
        sqlBuffer.append(" DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME ");
        sqlBuffer.append(" from message_web_task_info t,account_base_info a,enterprise_basic_info e ");
        sqlBuffer.append(" where t.BUSINESS_ACCOUNT = a.ACCOUNT_ID and a.ENTERPRISE_ID = e.ENTERPRISE_ID ");

        List<Object> paramsList = new ArrayList<Object>();

        //任务Id
        if (!StringUtils.isEmpty(qo.getId())) {
            sqlBuffer.append(" and t.ID =?");
            paramsList.add(qo.getId().trim());
        }

        //企业名称
        if (!StringUtils.isEmpty(qo.getEnterpriseName())) {
            sqlBuffer.append(" and e.ENTERPRISE_NAME like ? ");
            paramsList.add("%" + qo.getEnterpriseName().trim() + "%");
        }

        //业务账号
        if (!StringUtils.isEmpty(qo.getBusinessAccount())) {
            sqlBuffer.append(" and t.BUSINESS_ACCOUNT =?");
            paramsList.add(qo.getBusinessAccount().trim());
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

        //发送状态
        if (!StringUtils.isEmpty(qo.getSendStatus())) {
            sqlBuffer.append(" and t.SEND_STATUS =?");
            paramsList.add(qo.getSendStatus().trim());
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
        log.info("[SQL]:{}",sqlBuffer);
        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);
        PageList<MessageWebTaskInfoValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new MessageWebTaskInfoRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;
    }
}
