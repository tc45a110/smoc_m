package com.smoc.cloud.intelligence.repository;

import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.intelligence.IntellectCallbackTemplateStatusReportValidator;
import com.smoc.cloud.intelligence.rowmapper.IntellectCallbackTemplateStatusReportRowMapper;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class IntellectCallbackTemplateStatusReportRepositoryImpl extends BasePageRepository {

    /**
     * 分页查询
     * @param pageParams
     * @return
     */
    public PageList<IntellectCallbackTemplateStatusReportValidator> page(PageParams<IntellectCallbackTemplateStatusReportValidator> pageParams){
        IntellectCallbackTemplateStatusReportValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ID");
        sqlBuffer.append(", t.ORDER_NO");
        sqlBuffer.append(", t.TPL_ID");
        sqlBuffer.append(", t.BIZ_ID");
        sqlBuffer.append(", t.BIZ_FLAG");
        sqlBuffer.append(", t.TPL_STATE");
        sqlBuffer.append(", t.AUDIT_STATE");
        sqlBuffer.append(", t.FACTORY_INFO_LIST");
        sqlBuffer.append(", t.AUDIT_DESC");
        sqlBuffer.append(", t.FACTORY_TYPE");
        sqlBuffer.append(", t.STATE");
        sqlBuffer.append(", t.CREATED_BY");
        sqlBuffer.append(", DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME");
        sqlBuffer.append("  from intellect_callback_template_status_report t ");
        sqlBuffer.append("  where (1=1) ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getTplId())) {
            sqlBuffer.append(" and t.TPL_ID = ? ");
            paramsList.add(qo.getTplId().trim());
        }

        sqlBuffer.append(" order by t.CREATED_TIME desc");

        //log.info("[EnterpriseBasicInfoValidator]:{}", new Gson().toJson(qo));
        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<IntellectCallbackTemplateStatusReportValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new IntellectCallbackTemplateStatusReportRowMapper());
        pageList.getPageParams().setParams(qo);

        return pageList;
    }
}
