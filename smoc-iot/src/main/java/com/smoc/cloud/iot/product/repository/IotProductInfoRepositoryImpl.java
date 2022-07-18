package com.smoc.cloud.iot.product.repository;

import com.smoc.cloud.common.iot.validator.IotProductInfoValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.iot.common.BasePageRepository;
import com.smoc.cloud.iot.product.rowmapper.IotProductInfoRowMapper;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class IotProductInfoRepositoryImpl extends BasePageRepository {

    public PageList<IotProductInfoValidator> page(PageParams<IotProductInfoValidator> pageParams) {

        //查询条件
        IotProductInfoValidator qo = pageParams.getParams();

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
        sqlBuffer.append("  from iot_product_info t where 1=1 ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getProductName())) {
            sqlBuffer.append(" and t.PRODUCT_NAME like ?");
            paramsList.add( "%"+qo.getProductName().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getProductType())) {
            sqlBuffer.append(" and t.PRODUCT_TYPE = ?");
            paramsList.add(qo.getProductType().trim());
        }

        if (!StringUtils.isEmpty(qo.getUseStatus())) {
            sqlBuffer.append(" and t.USE_STATUS = ?");
            paramsList.add(qo.getUseStatus().trim());
        }

        if (!StringUtils.isEmpty(qo.getProductStatus())) {
            sqlBuffer.append(" and t.PRODUCT_STATUS = ?");
            paramsList.add(qo.getProductStatus().trim());
        }

        sqlBuffer.append(" order by t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<IotProductInfoValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new IotProductInfoRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;
    }
}
