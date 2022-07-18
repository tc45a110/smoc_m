package com.smoc.cloud.tablestore.repository;

import com.alicloud.openservices.tablestore.SyncClient;
import com.alicloud.openservices.tablestore.model.sql.SQLQueryRequest;
import com.alicloud.openservices.tablestore.model.sql.SQLQueryResponse;
import com.alicloud.openservices.tablestore.model.sql.SQLResultSet;
import com.alicloud.openservices.tablestore.model.sql.SQLRow;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.smoc.message.MessageDetailInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.tablestore.utils.TableStoreUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 分页查询公用类
 * 2019/5/6 11:43
 **/

public class TableStorePageRepository {

    @Autowired
    private TableStoreUtils tableStoreUtils;

    /**
     * 只查询一列数据类型对象。用于只有一行查询结果的数据
     *
     * @param sql
     * @param cla    Integer.class
     * @return
     */
    public Object queryOneColumnForSigetonRow(SyncClient client,String sql, Class cla) {


        String rowsql = "select count(*)message_num from (" + sql + ") mySqlPage_";

        // 创建SQL请求。
        SQLQueryRequest request = new SQLQueryRequest(rowsql);

        // 获取SQL的响应结果。
        SQLQueryResponse response = client.sqlQuery(request);

        // 获取SQL的返回结果。
        SQLResultSet resultSet = response.getSQLResultSet();

        Long rows = 0L;
        while (resultSet.hasNext()) {
            SQLRow row = resultSet.next();
            rows = row.getLong("message_num");
        }

        return rows;
    }
}
