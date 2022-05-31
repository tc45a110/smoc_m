package com.smoc.cloud.route.repository;

import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.route.RouteAuditMessageMtInfoValidator;
import com.smoc.cloud.route.rowmapper.RouteAuditMessageMtInfoRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 待审批下发信息
 */
@Service
public class RouteAuditMessageMtInfoRepository  extends BasePageRepository {

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public PageList<RouteAuditMessageMtInfoValidator> page(PageParams<RouteAuditMessageMtInfoValidator> pageParams) {
        //查询条件
        RouteAuditMessageMtInfoValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append(" t.ID,");
        sqlBuffer.append(" t.ACCOUNT_ID,");
        sqlBuffer.append(" t.INFO_TYPE,");
        sqlBuffer.append(" t.PHONE_NUMBER,");
        sqlBuffer.append(" t.ACCOUNT_SUBMIT_TIME,");
        sqlBuffer.append(" t.MESSAGE_CONTENT,");
        sqlBuffer.append(" t.CHANNEL_ID,");
        sqlBuffer.append(" t.REASON,");
        sqlBuffer.append(" t.AUDIT_FLAG,");
        sqlBuffer.append(" t.MESSAGE_MD5,");
        sqlBuffer.append(" str_to_date(t.CREATED_TIME,'%Y-%m-%d %H:%i:%S')CREATED_TIME");
        sqlBuffer.append(" from smoc_route.route_audit_message_mt_info t ");
        sqlBuffer.append(" where t.AUDIT_FLAG=0 ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getAccountId())) {
            sqlBuffer.append(" and t.ACCOUNT_ID like ? ");
            paramsList.add("%" + qo.getAccountId().trim() + "%");
        }

        if (!StringUtils.isEmpty(qo.getInfoType())) {
            sqlBuffer.append(" and t.INFO_TYPE =?");
            paramsList.add(qo.getInfoType().trim());
        }

        if (!StringUtils.isEmpty(qo.getChannelId())) {
            sqlBuffer.append(" and t.CHANNEL_ID =?");
            paramsList.add(qo.getChannelId().trim());
        }

        if (!StringUtils.isEmpty(qo.getPhoneNumber())) {
            sqlBuffer.append(" and t.PHONE_NUMBER like ? ");
            paramsList.add("%" + qo.getPhoneNumber().trim() + "%");
        }

        if (!StringUtils.isEmpty(qo.getMessageContent())) {
            sqlBuffer.append(" and t.MESSAGE_CONTENT like ? ");
            paramsList.add("%" + qo.getMessageContent().trim() + "%");
        }

        sqlBuffer.append(" order by t.CREATED_TIME desc,t.id");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<RouteAuditMessageMtInfoValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new RouteAuditMessageMtInfoRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;
    }

    /**
     * 统计未审批的总条数
     * @param qo
     * @return
     */
    public Map<String, Object> count(RouteAuditMessageMtInfoValidator qo) {
        StringBuffer sqlBuffer = new StringBuffer("select");
        sqlBuffer.append("  count(*) TOTAL_NUM ");
        sqlBuffer.append(" from smoc_route.route_audit_message_mt_info t where t.AUDIT_FLAG=0 ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getAccountId())) {
            sqlBuffer.append(" and t.ACCOUNT_ID like ? ");
            paramsList.add("%" + qo.getAccountId().trim() + "%");
        }

        if (!StringUtils.isEmpty(qo.getInfoType())) {
            sqlBuffer.append(" and t.INFO_TYPE =?");
            paramsList.add(qo.getInfoType().trim());
        }

        if (!StringUtils.isEmpty(qo.getChannelId())) {
            sqlBuffer.append(" and t.CHANNEL_ID =?");
            paramsList.add(qo.getChannelId().trim());
        }

        if (!StringUtils.isEmpty(qo.getPhoneNumber())) {
            sqlBuffer.append(" and t.PHONE_NUMBER like ? ");
            paramsList.add("%" + qo.getPhoneNumber().trim() + "%");
        }

        if (!StringUtils.isEmpty(qo.getMessageContent())) {
            sqlBuffer.append(" and t.MESSAGE_CONTENT like ? ");
            paramsList.add("%" + qo.getMessageContent().trim() + "%");
        }

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        Map<String, Object> map = jdbcTemplate.queryForMap(sqlBuffer.toString(),params);
        return map;
    }

    /**
     * 根据ID 查询
     * @param id
     * @return
     */
    public RouteAuditMessageMtInfoValidator findById(String id) {
        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append(" t.ID,");
        sqlBuffer.append(" t.ACCOUNT_ID,");
        sqlBuffer.append(" t.INFO_TYPE,");
        sqlBuffer.append(" t.PHONE_NUMBER,");
        sqlBuffer.append(" t.ACCOUNT_SUBMIT_TIME,");
        sqlBuffer.append(" t.MESSAGE_CONTENT,");
        sqlBuffer.append(" t.CHANNEL_ID,");
        sqlBuffer.append(" t.REASON,");
        sqlBuffer.append(" t.AUDIT_FLAG,");
        sqlBuffer.append(" t.MESSAGE_MD5,");
        sqlBuffer.append(" str_to_date(t.CREATED_TIME,'%Y-%m-%d %H:%i:%S')CREATED_TIME");
        sqlBuffer.append(" from smoc_route.route_audit_message_mt_info t ");
        sqlBuffer.append(" where t.ID = ? ");

        Object[] params = new Object[1];
        params[0] = id;

        List<RouteAuditMessageMtInfoValidator> list = this.queryForObjectList(sqlBuffer.toString(), params, new RouteAuditMessageMtInfoRowMapper());
        if(!StringUtils.isEmpty(list) && list.size()>0){
            return list.get(0);
        }
        return new RouteAuditMessageMtInfoValidator();
    }

    /**
     * 更新单条记录状态
     * @param auditFlag
     * @param id
     */
    public void updateAuditFlagById(Integer auditFlag, long id) {
        String sql = "update smoc_route.route_audit_message_mt_info t set t.AUDIT_FLAG = ? where id = ? ";

        Object[] params = new Object[2];
        params[0] = auditFlag;
        params[1] = id;

        jdbcTemplate.update(sql,params);
    }

    /**
     * 批量审批
     * @param auditFlag
     * @param messageMd5
     */
    public void updateAuditFlagByMessageMd5(Integer auditFlag, String messageMd5) {
        String sql = "update smoc_route.route_audit_message_mt_info t set t.AUDIT_FLAG = ? where MESSAGE_MD5 = ? ";

        Object[] params = new Object[2];
        params[0] = auditFlag;
        params[1] = messageMd5;

        jdbcTemplate.update(sql,params);
    }
}