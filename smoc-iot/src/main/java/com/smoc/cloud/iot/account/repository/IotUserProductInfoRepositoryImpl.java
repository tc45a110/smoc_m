package com.smoc.cloud.iot.account.repository;

import com.smoc.cloud.common.iot.validator.IotProductInfoValidator;
import com.smoc.cloud.common.iot.validator.IotUserProductInfoValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.iot.account.rowmapper.IotUserProductInfoRowMapper;
import com.smoc.cloud.iot.common.BasePageRepository;
import com.smoc.cloud.iot.product.rowmapper.IotProductInfoRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
@Slf4j
public class IotUserProductInfoRepositoryImpl extends BasePageRepository {

    @Autowired
    private DataSource dataSource;

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
        sqlBuffer.append(", u.USER_ID USE_STATUS");
        sqlBuffer.append(", t.PRODUCT_STATUS");
        sqlBuffer.append(", t.CREATED_BY");
        sqlBuffer.append(", DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME");
        sqlBuffer.append("  from iot_product_info t left join iot_user_product_items u on t.ID=u.PRODUCT_ID where u.USER_ID =? or t.USE_STATUS=? ");
        sqlBuffer.append(" order by t.CREATED_TIME desc");
        log.info("[sqlBuffer]:{}", sqlBuffer);
        //根据参数个数，组织参数值
        Object[] params = new Object[2];
        params[0] = userId;
        params[1] = "01";
        List<IotProductInfoValidator> list = this.queryForObjectList(sqlBuffer.toString(), params, new IotProductInfoRowMapper());
        return list;
    }

    /**
     * 批量保存产品卡
     */
    public void insertAccountProductCards(String account, String productIds) {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            //处理原来产品配置的物联网卡
            stmt.addBatch("update iot_product_info p  inner join iot_user_product_items u  on  p.ID= u.PRODUCT_ID set p.USE_STATUS  ='01' where u.USER_ID='" + account + "' ");
            stmt.addBatch("delete from iot_user_product_items where USER_ID='" + account + "' ");
            if (!StringUtils.isEmpty(productIds)) {
                String[] products = productIds.split(",");
                Integer productsNum = products.length;
                if (null != products && productsNum > 0) {
                    for (String productId : products) {
                        StringBuffer sqlBuffer = new StringBuffer();
                        sqlBuffer.append(" insert into iot_user_product_items(ID,USER_ID,PRODUCT_ID,CREATED_BY,CREATED_TIME)");
                        sqlBuffer.append(" values('"+UUID.uuid32()+"','"+account+"','"+productId+"','system',now())");
                        stmt.addBatch(sqlBuffer.toString());
                        stmt.addBatch(" update iot_product_info set USE_STATUS ='02' where id = '" + productId + "' ");
                    }
                }
            }
            stmt.executeBatch();
            stmt.clearBatch();
            stmt.close();
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != conn) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
