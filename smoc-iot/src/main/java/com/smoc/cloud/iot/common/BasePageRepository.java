package com.smoc.cloud.iot.common;

import com.smoc.cloud.common.page.PageList;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页查询公用类
 * 2019/5/6 11:43
 **/
public class BasePageRepository {

    @Resource
    public JdbcTemplate jdbcTemplate;

    /**
     * 只查询一列数据类型对象。用于只有一行查询结果的数据
     *
     * @param sql
     * @param params
     * @param cla    Integer.class
     * @return
     */
    public Object queryOneColumnForSigetonRow(String sql, Object[] params, Class cla) {
        Object result = null;
        try {
            if (params == null || params.length > 0) {
                result = jdbcTemplate.queryForObject(sql, params, cla);
            } else {
                result = jdbcTemplate.queryForObject(sql, cla);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    /**
     * 查询返回实体对象集合
     *
     * @param sql    sql语句
     * @param params 填充sql问号占位符数
     * @param mapper
     * @return
     */
    public <T> List<T> queryForObjectList(String sql, Object[] params, RowMapper<T> mapper) {

        List<T> list = new ArrayList();

        try {
            list = jdbcTemplate.query(sql, params, mapper);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (list.size() <= 0) {
            return null;
        }
        return list;
    }


    /**
     * 查询分页（MySQL数据库）
     *
     * @param sql         终执行查询的语句
     * @param params      填充sql语句中的问号占位符数
     * @param currentPage 想要第几页的数据
     * @param pageSize    每页显示多少条数
     * @param mapper
     * @return pageList对象
     */
    public <T> PageList<T> queryByPageForMySQL(String sql, Object[] params, int currentPage, int pageSize, RowMapper<T> mapper) {

        //查询总行数sql
        String rowsql = "select count(*) from (" + sql + ") mySqlPage_";

        //总页数
        int pages = 0;
        //查询总行数
        int rows = (Integer) queryOneColumnForSigetonRow(rowsql, params, Integer.class);

        //开始行
        int startRow = 0;

        //结束行
        int endRow = 0;

        //判断页数,如果是页大小的整数倍就为rows/pageRow如果不是整数倍就为rows/pageRow+1
        if (rows % pageSize == 0) {
            pages = rows / pageSize;
        } else {
            pages = rows / pageSize + 1;
        }

        if (currentPage > pages) {
            currentPage = pages;
        }

        //查询第page页的数据sql语句
        if (currentPage <= 1) {
            endRow = pageSize < rows ? pageSize : rows;
            sql += " limit 0," + pageSize;
        } else {
            startRow = ((currentPage - 1) * pageSize);
            endRow = (((currentPage - 1) * pageSize) + pageSize) < rows ? (((currentPage - 1) * pageSize) + pageSize) : rows;
            //sql += " limit " + ((currentPage - 1) * pageSize) + "," + (((currentPage - 1) * pageSize) + pageSize);
            sql += " limit " + ((currentPage - 1) * pageSize) + "," + pageSize;
        }

        //查询第page页数据
        List<T> list = null;
        if (mapper != null) {
            list = queryForObjectList(sql, params, mapper);
        } else {
            list = null;
        }

        //返回分页格式数据
        PageList<T> pageList = new PageList<>();
        //设置当前页
        pageList.getPageParams().setCurrentPage(currentPage);
        //设置总页数
        pageList.getPageParams().setPages(pages);
        //设置每页显示条数
        pageList.getPageParams().setPageSize(pageSize);
        //设置总记录数
        pageList.getPageParams().setTotalRows(rows);
        //设置开始行
        pageList.getPageParams().setStartRow(startRow + 1);
        //设置结束行
        pageList.getPageParams().setEndRow(endRow);
        //设置当前页数据
        pageList.setList(list);
        return pageList;
    }

}
