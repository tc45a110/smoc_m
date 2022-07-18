package com.smoc.cloud.iot.account.repository;

import com.smoc.cloud.common.iot.validator.IotProductInfoValidator;
import com.smoc.cloud.common.iot.validator.IotUserProductInfoValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.iot.account.rowmapper.IotUserProductInfoRowMapper;
import com.smoc.cloud.iot.common.BasePageRepository;
import com.smoc.cloud.iot.product.rowmapper.IotProductInfoRowMapper;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class IotUserProductInfoRepositoryImpl extends BasePageRepository {

    public PageList<IotUserProductInfoValidator> page(PageParams<IotUserProductInfoValidator> pageParams) {

        //查询条件
        IotUserProductInfoValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ID");
        sqlBuffer.append(", t.USER_ID");
        sqlBuffer.append(", t.CREATED_BY");
        sqlBuffer.append(", DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME");
        sqlBuffer.append("  from iot_user_product_info t where 1=1 ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getUserId())) {
            sqlBuffer.append(" and t.USER_ID = ?");
            paramsList.add(qo.getUserId().trim());
        }

        sqlBuffer.append(" order by t.CREATED_TIME desc");
        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);
        PageList<IotUserProductInfoValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new IotUserProductInfoRowMapper());
        if (null != pageList.getList() && pageList.getList().size() > 0) {
            for (IotUserProductInfoValidator validator : pageList.getList()) {
                List<IotProductInfoValidator> list = this.list(validator.getUserId());
                validator.setProductList(list);
            }
        }
        pageList.getPageParams().setParams(qo);
        return pageList;
    }

    public List<IotProductInfoValidator> list(String userId) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ID");
        sqlBuffer.append(", t.PRODUCT_NAME");
        sqlBuffer.append(", t.PRODUCT_TYPE");
        sqlBuffer.append(", t.CARDS_CHANGING");
        sqlBuffer.append(", t.PRODUCT_POOL_SIZE");
        sqlBuffer.append(", t.CHANGING_CYCLE");
        sqlBuffer.append(", t.CYCLE_QUOTA");
        sqlBuffer.append(", t.ABOVE_QUOTA_CHANGING");
        sqlBuffer.append(", t.PRODUCT_CARDS_NUM");
        sqlBuffer.append(", t.REMARK");
        sqlBuffer.append(", t.USE_STATUS");
        sqlBuffer.append(", t.PRODUCT_STATUS");
        sqlBuffer.append(", t.CREATED_BY");
        sqlBuffer.append(", DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME");
        sqlBuffer.append("  from iot_product_info t,iot_user_product_items u where t.ID = u.PRODUCT_ID and u.USER_ID =? ");
        sqlBuffer.append(" order by t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[1];
        params[0] = userId;
        List<IotProductInfoValidator> list = this.queryForObjectList(sqlBuffer.toString(), params, new IotProductInfoRowMapper());
        return list;
    }
}
