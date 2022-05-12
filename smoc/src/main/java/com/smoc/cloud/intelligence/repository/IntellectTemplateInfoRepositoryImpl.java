package com.smoc.cloud.intelligence.repository;

import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.intelligence.IntellectTemplateInfoValidator;
import com.smoc.cloud.intelligence.rowmapper.IntellectTemplateInfoRwoMapper;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class IntellectTemplateInfoRepositoryImpl extends BasePageRepository {

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public PageList<IntellectTemplateInfoValidator> page(PageParams<IntellectTemplateInfoValidator> pageParams) {

        IntellectTemplateInfoValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  i.ID");
        sqlBuffer.append(", i.ENTERPRISE_ID");
        sqlBuffer.append(", t.ENTERPRISE_NAME");
        sqlBuffer.append(", i.ACCOUNT_ID");
        sqlBuffer.append(", i.TEMPLATE_ID");
        sqlBuffer.append(", i.TPL_ID");
        sqlBuffer.append(", i.CARD_ID");
        sqlBuffer.append(", i.TPL_NAME");
        sqlBuffer.append(", i.SCENE");
        sqlBuffer.append(", i.PAGES");
        sqlBuffer.append(", i.MESSAGE_TYPE");
        sqlBuffer.append(", i.BIZ_ID");
        sqlBuffer.append(", i.BIZ_FLAG");
        sqlBuffer.append(", i.SMS_EXAMPLE");
        sqlBuffer.append(", i.TEMPLATE_CHECK_STATUS");
        sqlBuffer.append(", i.TEMPLATE_STATUS");
        sqlBuffer.append(", i.CREATED_BY");
        sqlBuffer.append(", DATE_FORMAT(i.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME");
        sqlBuffer.append("  from intellect_template_info i,enterprise_basic_info t  ");
        sqlBuffer.append("  where i.ENTERPRISE_ID = t.ENTERPRISE_ID and i.TEMPLATE_STATUS= 1 ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getAccountId())) {
            sqlBuffer.append(" and i.ACCOUNT_ID = ? ");
            paramsList.add(qo.getAccountId().trim());
        }

        if (!StringUtils.isEmpty(qo.getTplId())) {
            sqlBuffer.append(" and i.TPL_ID = ? ");
            paramsList.add(qo.getTplId().trim());
        }

        if (!StringUtils.isEmpty(qo.getEnterpriseName())) {
            sqlBuffer.append(" and t.ENTERPRISE_NAME like ?");
            paramsList.add("%" + qo.getEnterpriseName().trim() + "%");
        }

        if (!StringUtils.isEmpty(qo.getTplName())) {
            sqlBuffer.append(" and i.TPL_NAME like ?");
            paramsList.add("%" + qo.getTplName().trim() + "%");
        }

        if (!StringUtils.isEmpty(qo.getTemplateId())) {
            sqlBuffer.append(" and i.TEMPLATE_ID = ? ");
            paramsList.add(qo.getTemplateId().trim());
        }

        sqlBuffer.append(" order by i.CREATED_TIME desc");

        //log.info("[EnterpriseBasicInfoValidator]:{}", new Gson().toJson(qo));
        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<IntellectTemplateInfoValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new IntellectTemplateInfoRwoMapper());
        pageList.getPageParams().setParams(qo);

        return pageList;
    }
}
