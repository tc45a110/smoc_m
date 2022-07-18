package com.smoc.cloud.iot.product.repository;

import com.smoc.cloud.common.iot.validator.IotProductCardValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.iot.common.BasePageRepository;
import com.smoc.cloud.iot.product.rowmapper.IotProductCardRowMapper;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class IotProductCardRepositoryImpl extends BasePageRepository {

    public PageList<IotProductCardValidator> page(PageParams<IotProductCardValidator> pageParams) {

        //查询条件
        IotProductCardValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ID");
        sqlBuffer.append(", t.PRODUCT_ID");
        sqlBuffer.append(", t.CARD_MSISDN");
        sqlBuffer.append(", t.CARD_IMSI");
        sqlBuffer.append(", t.CARD_ICCID");
        sqlBuffer.append(", t.STATUS");
        sqlBuffer.append(", t.CREATED_BY");
        sqlBuffer.append(", DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME");
        sqlBuffer.append("  from iot_product_cards t where 1=1 ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getProductId())) {
            sqlBuffer.append(" and t.PRODUCT_ID = ?");
            paramsList.add(qo.getProductId().trim());
        }

        if (!StringUtils.isEmpty(qo.getCardMsisdn())) {
            sqlBuffer.append(" and t.CARD_MSISDN = ?");
            paramsList.add(qo.getCardMsisdn().trim());
        }

        if (!StringUtils.isEmpty(qo.getCardImsi())) {
            sqlBuffer.append(" and t.CARD_IMSI = ?");
            paramsList.add(qo.getCardImsi().trim());
        }

        if (!StringUtils.isEmpty(qo.getCardIccid())) {
            sqlBuffer.append(" and t.CARD_ICCID = ?");
            paramsList.add(qo.getCardIccid().trim());
        }

        if (!StringUtils.isEmpty(qo.getStatus())) {
            sqlBuffer.append(" and t.STATUS = ?");
            paramsList.add(qo.getStatus().trim());
        }

        sqlBuffer.append(" order by t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<IotProductCardValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new IotProductCardRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;
    }
}
