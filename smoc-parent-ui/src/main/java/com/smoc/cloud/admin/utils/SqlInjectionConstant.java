package com.smoc.cloud.admin.utils;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Lists;

/**
 * 通过拦击 防止sql 注入 这只是第一步 在后面建立数据库语句标准，不能拼接sql
 *
 * @author Administrator
 */
public class SqlInjectionConstant {

    /**
     * 防止sql注入的关键字集合
     *
     * @return
     */
    public static List<String> sqlKeyWordCollection() {

        List<String> list = Lists.newArrayList();
        list.add("exec ");
        list.add("execute ");
        list.add("insert ");
        list.add("select ");
        list.add("delete ");
        list.add("update ");
        list.add("count ");
        list.add("drop ");
        list.add("master ");
        list.add("truncate ");
        list.add("where ");
        list.add("grant ");
        list.add("like ");
        list.add("create ");
        list.add("from ");
        list.add("net user");
        list.add("order ");
        list.add("char ");
        list.add("and ");
        list.add("or ");
        list.add("column_name ");
        //list.add("%");有的密码会用到%
        return list;

    }

    /**
     * 清除数据库关键字
     *
     * @param str
     * @return
     */
    public static String clean(String str) {
        if (StringUtils.isNotBlank(str)) {
            for (String obj : sqlKeyWordCollection()) {
                str = str.replace(obj, "");
            }
        }

        return str;
    }

    public static void main(String[] args) {

        String text = "select drop from aaa where 1=1 and 2=2  or ";
        System.out.println(SqlInjectionConstant.clean(text));
    }

}