package com.smoc.cloud.intelligence.repository;

import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.intelligence.IntellectShortUrlValidator;
import com.smoc.cloud.intelligence.rowmapper.IntellectShortUrlRowMapper;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class IntellectShortUrlRepositoryImpl extends BasePageRepository {

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public PageList<IntellectShortUrlValidator> page(PageParams<IntellectShortUrlValidator> pageParams) {

        IntellectShortUrlValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  i.ID");
        sqlBuffer.append(", i.TPL_ID");
        sqlBuffer.append(", i.CUST_FLAG");
        sqlBuffer.append(", t.ENTERPRISE_NAME");
        sqlBuffer.append(", i.CUST_ID");
        sqlBuffer.append(", i.AIM_URL");
        sqlBuffer.append(", i.AIM_CODE");
        sqlBuffer.append(", i.EXPIRE_TIMES");
        sqlBuffer.append(", i.SHOW_TIMES");
        sqlBuffer.append(", i.SUCCESS_ANALYSIS");
        sqlBuffer.append(", temp.TPL_NAME");
        sqlBuffer.append(", i.FACTORIES");
        sqlBuffer.append(", i.DYNC_PARAMS");
        sqlBuffer.append(", i.CUSTOM_URL");
        sqlBuffer.append(", i.EXT_DATA");
        sqlBuffer.append(", i.RESULT_CODE");
        sqlBuffer.append(", i.ERROR_MESSAGE");
        sqlBuffer.append(", i.CREATED_BY");
        sqlBuffer.append(", DATE_FORMAT(i.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME");
        sqlBuffer.append("  from intellect_short_url i,enterprise_basic_info t,intellect_template_info temp  ");
        sqlBuffer.append("  where i.CUST_FLAG = t.ENTERPRISE_ID and i.TPL_ID= temp.TPL_ID ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getCustFlag())) {
            sqlBuffer.append(" and i.CUST_FLAG = ? ");
            paramsList.add(qo.getCustFlag().trim());
        }

        if (!StringUtils.isEmpty(qo.getCustId())) {
            sqlBuffer.append(" and i.CUST_ID = ? ");
            paramsList.add(qo.getCustId().trim());
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
            sqlBuffer.append(" and temp.TPL_NAME like ?");
            paramsList.add("%" + qo.getTplName().trim() + "%");
        }

        sqlBuffer.append(" order by i.CREATED_TIME desc");

        //log.info("[EnterpriseBasicInfoValidator]:{}", new Gson().toJson(qo));
        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<IntellectShortUrlValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new IntellectShortUrlRowMapper());
        pageList.getPageParams().setParams(qo);

        return pageList;

    }
}
