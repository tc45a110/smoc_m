package com.smoc.cloud.intelligence.repository;

import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.intelligence.IntellectCallbackShowReportValidator;
import com.smoc.cloud.intelligence.rowmapper.IntellectCallbackShowReportRowMapper;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class IntellectCallbackShowReportRepositoryImpl extends BasePageRepository {

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public PageList<IntellectCallbackShowReportValidator> page(PageParams<IntellectCallbackShowReportValidator> pageParams) {
        IntellectCallbackShowReportValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ID");
        sqlBuffer.append(", t.ORDER_NO");
        sqlBuffer.append(", t.CUST_FLAG");
        sqlBuffer.append(", e.ENTERPRISE_NAME");
        sqlBuffer.append(", t.CUST_ID");
        sqlBuffer.append(", t.AIM_URL");
        sqlBuffer.append(", t.AIM_CODE");
        sqlBuffer.append(", t.TPL_ID");
        sqlBuffer.append(", t.EXT_DATA");
        sqlBuffer.append(", t.STATUS");
        sqlBuffer.append(", t.DESCRIBE");
        sqlBuffer.append(", t.CREATED_BY");
        sqlBuffer.append(", DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME");
        sqlBuffer.append("  from intellect_callback_show_report t,enterprise_basic_info e   ");
        sqlBuffer.append("  where e.ENTERPRISE_ID = t.CUST_FLAG ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getCustId())) {
            sqlBuffer.append(" and t.CUST_ID = ? ");
            paramsList.add(qo.getCustId().trim());
        }

        if (!StringUtils.isEmpty(qo.getCustFlag())) {
            sqlBuffer.append(" and t.CUST_FLAG = ? ");
            paramsList.add(qo.getCustFlag().trim());
        }

        if (!StringUtils.isEmpty(qo.getTplId())) {
            sqlBuffer.append(" and t.TPL_ID = ? ");
            paramsList.add(qo.getTplId().trim());
        }

        if (!StringUtils.isEmpty(qo.getEnterpriseName())) {
            sqlBuffer.append(" and e.ENTERPRISE_NAME like ?");
            paramsList.add("%" + qo.getEnterpriseName().trim() + "%");
        }

        sqlBuffer.append(" order by t.CREATED_TIME desc");

        //log.info("[EnterpriseBasicInfoValidator]:{}", new Gson().toJson(qo));
        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<IntellectCallbackShowReportValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new IntellectCallbackShowReportRowMapper());
        pageList.getPageParams().setParams(qo);

        return pageList;
    }
}
