package com.business.statistics.table;
import com.base.common.dao.LavenderDBSingleton;
import com.base.common.log.CategoryLog;
import com.base.common.manager.ResourceManager;
import com.base.common.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * 根据配置清理数据，执行频率一天一次
 */
public class TableDataClear {
    //获取smoc_route中所有的临时表名
    private static List<String> tableNames = getTable();


    public static void main(String[] args) {
        CategoryLog.accessLogger.info("***************临时表数据清除开始***************");
        String content = ResourceManager.getInstance().getValue("data.clear");
        if(StringUtils.isNotEmpty(content)){
            String[] datas = content.split(";");
            for (String tableName : tableNames) {
            for (String data : datas) {
                String[] strs = data.split(",");
                    if (tableName.startsWith(strs[0])) {
                        tableDataClear(tableName, strs[1]);
                    }
                }
            }
        }
        CategoryLog.accessLogger.info("***************临时表数据清除结束***************");
        System.exit(0);
    }
    private static void tableDataClear(String table,String days){
        int day=(~(Integer.parseInt(days)-1));
        String previousTmie= DateUtil.getAfterDayDateTime(day,DateUtil.DATE_FORMAT_COMPACT_STANDARD_SECONDE);
        StringBuffer sql = new StringBuffer();
        Connection conn = null;
        PreparedStatement pstmt =null;
        int rows=0;
        try {
            conn = LavenderDBSingleton.getInstance().getConnection();
            sql.append("DELETE FROM smoc_route.").append(table).append(" where CREATED_TIME < ? ");
            pstmt = conn.prepareStatement(sql.toString());

            pstmt.setString(1,previousTmie);
            rows=pstmt.executeUpdate();

            CategoryLog.accessLogger.info("表:{},清理数据:{}条",table,rows);
        } catch (Exception e) {
            CategoryLog.accessLogger.error(e.getMessage(), e);
        } finally {
            LavenderDBSingleton.getInstance().closeAll(null, pstmt,  conn);
        }
    }

    // 获取smoc_route中所有的临时表名
    private static List<String> getTable() {
        StringBuffer sql = new StringBuffer();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<String> list = new ArrayList<String>();

        sql.append("SELECT TABLE_NAME FROM information_schema.TABLES WHERE table_schema = 'smoc_route'");
        try {
            conn = LavenderDBSingleton.getInstance().getConnection();
            conn.setAutoCommit(false);

            pstmt = conn.prepareStatement(sql.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("TABLE_NAME"));
            }
        } catch (Exception e) {
            CategoryLog.accessLogger.error(e.getMessage(), e);
        } finally {
            LavenderDBSingleton.getInstance().closeAll(rs, pstmt, conn);
        }
        return list;
    }
}
