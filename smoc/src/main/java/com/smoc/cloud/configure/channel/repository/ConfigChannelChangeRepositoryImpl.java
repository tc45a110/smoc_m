package com.smoc.cloud.configure.channel.repository;

import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelChangeValidator;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.configure.channel.entity.ConfigChannelChange;
import com.smoc.cloud.configure.channel.rowmapper.ConfigChannelChangeRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 *
 */
@Slf4j
public class ConfigChannelChangeRepositoryImpl extends BasePageRepository {

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public PageList<ConfigChannelChangeValidator> page(PageParams<ConfigChannelChangeValidator> pageParams) {
        //查询条件
        ConfigChannelChangeValidator qo = pageParams.getParams();
        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");

        sqlBuffer.append(" t.ID,");
        sqlBuffer.append(" t.CHANNEL_ID,");
        sqlBuffer.append(" a.CHANNEL_NAME,");
        sqlBuffer.append(" t.ACCOUNT_NUM,");
        sqlBuffer.append(" t.CHANGE_TYPE,");
        sqlBuffer.append(" t.CHANGE_REASON,");
        sqlBuffer.append(" t.CHANGE_STATUS,");
        sqlBuffer.append(" t.CREATED_BY,");
        sqlBuffer.append(" DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME ");
        sqlBuffer.append(" from config_channel_change t,config_channel_basic_info a");
        sqlBuffer.append(" where t.CHANNEL_ID = a.CHANNEL_ID  ");


        List<Object> paramsList = new ArrayList<Object>();
        //通道ID
        if (!StringUtils.isEmpty(qo.getChannelId())) {
            sqlBuffer.append(" and t.CHANNEL_ID =?");
            paramsList.add(qo.getChannelId().trim());
        }

        //企业名称
        if (!StringUtils.isEmpty(qo.getChannelName())) {
            sqlBuffer.append(" and a.CHANNEL_NAME like ?");
            paramsList.add("%" + qo.getChannelName().trim() + "%");
        }

        sqlBuffer.append(" order by t.CHANGE_STATUS desc,t.CREATED_TIME desc");
        log.info("[SQL]:{}",sqlBuffer);
        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<ConfigChannelChangeValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new ConfigChannelChangeRowMapper());
        pageList.getPageParams().setParams(qo);

        return pageList;
    }

    /**
     * 通道切换(变更通道在业务账户中的优先级)
     *
     * @param qo
     */
    public void addChannelChange(ConfigChannelChangeValidator qo) {

        //要操作的业务账号
        String accountIds = qo.getAccountIds();

        //如果选中的业务账号为空
        if (StringUtils.isEmpty(accountIds)) {
            StringBuffer channelChangeSql = new StringBuffer("insert into config_channel_change(ID,CHANNEL_ID,ACCOUNT_NUM,CHANGE_TYPE,CHANGE_REASON,CHANGE_STATUS,CREATED_BY,CREATED_TIME) ");
            channelChangeSql.append(" values('" + qo.getId() + "','" + qo.getChannelId() + "',0,'" + qo.getChannelName() + "','" + qo.getChangeReason() + "','" + qo.getChangeStatus() + "','" + qo.getCreatedBy() + "',now())");
            this.jdbcTemplate.update(channelChangeSql.toString());
            return;
        }

        //业务账号处理
        String[] accountArray = accountIds.split(",");
        int accountLength = accountArray.length;

        //要执行的sql数组
        String[] sql = new String[accountLength * 2 + 1];
        //sql数组索引
        int i = 0;
        for (int j = 0; j < accountLength; j++) {
            //插入变更业务账号的变更记录
            StringBuffer itemSql = new StringBuffer("insert into config_channel_change_items(ID,CHANGE_ID,BUSINESS_ACCOUNT,CHANGE_TYPE,CHANGE_BEFORE_PRIORITY,CHANGE_AFTER_PRIORITY,ACCOUNT_CHANNEL_ID,STATUS,CREATED_BY,CREATED_TIME) ");
            itemSql.append(" select '" + UUID.uuid32() + "',CHANNEL_ID,ACCOUNT_ID,'" + qo.getChangeType() + "',CHANNEL_PRIORITY,'" + qo.getChangeType() + "',ID,'1','" + qo.getCreatedBy() + "',now() from account_channel_info ");
            itemSql.append(" where ACCOUNT_ID ='" + accountArray[j] + "' and CHANNEL_ID='" + qo.getChannelId() + "' ");
            sql[i] = itemSql.toString();
            i++;
            //修改业务账号 通道配置 优先级
            StringBuffer updateSql = new StringBuffer("update account_channel_info ");
            updateSql.append(" set CHANNEL_PRIORITY ='" + qo.getChangeType() + "',CHANGE_SOURCE='CHANNEL_CHANGE' ");
            itemSql.append(" where ACCOUNT_ID ='" + accountArray[j] + "' and CHANNEL_ID='" + qo.getChannelId() + "' ");
            sql[i] = updateSql.toString();
            i++;
        }

        StringBuffer channelChangeSql = new StringBuffer("insert into config_channel_change(ID,CHANNEL_ID,ACCOUNT_NUM,CHANGE_TYPE,CHANGE_REASON,CHANGE_STATUS,CREATED_BY,CREATED_TIME) ");
        channelChangeSql.append(" values('" + qo.getId() + "','" + qo.getChannelId() + "'," + accountLength + ",'" + qo.getChannelName() + "','" + qo.getChangeReason() + "','" + qo.getChangeStatus() + "','" + qo.getCreatedBy() + "',now())");
        sql[i] = channelChangeSql.toString();
        log.info("[sql]:{}", sql);
        this.jdbcTemplate.batchUpdate(sql);

    }

    /**
     * 通道切换修改(变更通道在业务账户中的优先级)
     *
     * @param qo
     * @param originalAccountIds 修改前（原来）操作的业务账号
     */
    public void editChannelChange(ConfigChannelChangeValidator qo, String originalAccountIds) {

        //要操作的业务账号
        String accountIds = qo.getAccountIds();

        //如果选中的业务账号为空,并且原来的也为空
        if (StringUtils.isEmpty(originalAccountIds) && StringUtils.isEmpty(accountIds)) {
            StringBuffer channelChangeUpdateSql = new StringBuffer("update config_channel_change ");
            channelChangeUpdateSql.append(" set CHANGE_REASON='" + qo.getChangeReason() + "',UPDATED_BY='" + qo.getUpdatedBy() + "',UPDATED_TIME = now() ");
            channelChangeUpdateSql.append(" where ID ='" + qo.getId() + "' ");
            this.jdbcTemplate.update(channelChangeUpdateSql.toString());
            return;
        }

        //如果原来为空 originalAccountIds
        if (StringUtils.isEmpty(originalAccountIds)) {

            //业务账号处理
            String[] accountArray = accountIds.split(",");
            int accountLength = accountArray.length;
            //要执行的sql数组
            String[] sql = new String[accountLength * 2 + 1];
            //sql数组索引
            int i = 0;
            for (int j = 0; j < accountLength; j++) {
                //插入变更业务账号的变更记录
                StringBuffer itemSql = new StringBuffer("insert into config_channel_change_items(ID,CHANGE_ID,BUSINESS_ACCOUNT,CHANGE_TYPE,CHANGE_BEFORE_PRIORITY,CHANGE_AFTER_PRIORITY,ACCOUNT_CHANNEL_ID,STATUS,CREATED_BY,CREATED_TIME) ");
                itemSql.append(" select '" + UUID.uuid32() + "',CHANNEL_ID,ACCOUNT_ID,'" + qo.getChangeType() + "',CHANNEL_PRIORITY,'" + qo.getChangeType() + "',ID,'1','" + qo.getCreatedBy() + "',now() from account_channel_info ");
                itemSql.append(" where ACCOUNT_ID ='" + accountArray[j] + "' and CHANNEL_ID='" + qo.getChannelId() + "' ");
                sql[i] = itemSql.toString();
                i++;
                //修改业务账号 通道配置 优先级
                StringBuffer updateSql = new StringBuffer("update account_channel_info ");
                updateSql.append(" set CHANNEL_PRIORITY ='" + qo.getChangeType() + "',CHANGE_SOURCE='CHANNEL_CHANGE' ");
                itemSql.append(" where ACCOUNT_ID ='" + accountArray[j] + "' and CHANNEL_ID='" + qo.getChannelId() + "' ");
                sql[i] = updateSql.toString();
                i++;
            }

            StringBuffer channelChangeUpdateSql = new StringBuffer("update config_channel_change ");
            channelChangeUpdateSql.append(" set CHANGE_REASON='" + qo.getChangeReason() + "',ACCOUNT_NUM = " + accountLength + ",UPDATED_BY='" + qo.getUpdatedBy() + "',UPDATED_TIME = now() ");
            channelChangeUpdateSql.append(" where ID ='" + qo.getId() + "' ");
            sql[i] = channelChangeUpdateSql.toString();
            this.jdbcTemplate.batchUpdate(sql);
            return;
        }

        //如果新提交的为空 accountIds
        if (StringUtils.isEmpty(accountIds)) {

            //业务账号处理
            String[] originalAccountArray = originalAccountIds.split(",");
            int originalAccountLength = originalAccountArray.length;
            //要执行的sql数组
            String[] sql = new String[originalAccountLength * 2 + 1];
            //sql数组索引
            int i = 0;

            for (int j = 0; j < originalAccountLength; j++) {
                //修改变更业务账号的变更记录
                StringBuffer itemSql = new StringBuffer("update config_channel_change_items  ");
                itemSql.append(" set STATUS = '0");
                itemSql.append(" where ACCOUNT_ID ='" + originalAccountArray[j] + "' and CHANNEL_ID='" + qo.getChannelId() + "' ");
                sql[i] = itemSql.toString();
                i++;
                //修改业务账号 通道配置 优先级NORMAL
                StringBuffer updateSql = new StringBuffer("update account_channel_info ");
                updateSql.append(" set CHANNEL_PRIORITY ='NORMAL',CHANGE_SOURCE='CHANNEL_CHANGE' ");
                itemSql.append(" where ACCOUNT_ID ='" + originalAccountArray[j] + "' and CHANNEL_ID='" + qo.getChannelId() + "' ");
                sql[i] = updateSql.toString();
                i++;
            }

            StringBuffer channelChangeUpdateSql = new StringBuffer("update config_channel_change ");
            channelChangeUpdateSql.append(" set CHANGE_REASON='" + qo.getChangeReason() + "',ACCOUNT_NUM = 0,UPDATED_BY='" + qo.getUpdatedBy() + "',UPDATED_TIME = now() ");
            channelChangeUpdateSql.append(" where ID ='" + qo.getId() + "' ");
            sql[i] = channelChangeUpdateSql.toString();
            this.jdbcTemplate.batchUpdate(sql);
            return;
        }

        /**
         * accountIds 与 originalAccountIds 都不为空
         */

        //原来业务账号处理
        String[] originalAccountArray = originalAccountIds.split(",");
        int originalAccountLength = originalAccountArray.length;
        //要删除的个数
        int delete = originalAccountLength;
        //boolean 为false 表示已删除的 业务账号
        Map<String, Boolean> originalMap = new HashMap<>();
        for (int i = 0; i < originalAccountLength; i++) {
            originalMap.put(originalAccountArray[i], false);
        }
        //新业务账号处理
        String[] accountArray = accountIds.split(",");
        int accountLength = accountArray.length;
        //要增加的个数
        int add = 0;
        //boolean 为true表示要新建的账号,false 表示accountIds、originalAccountIds共有账号
        Map<String, Boolean> accountMap = new HashMap<>();
        for (int j = 0; j < accountLength; j++) {
            if (null != originalMap.get(accountArray[j])) {
                originalMap.put(accountArray[j], true);
                accountMap.put(accountArray[j], false);
                delete--;
            } else {
                accountMap.put(accountArray[j], true);
                add++;
            }

        }

        //sql的条数
        int sqlLength = (delete + add) * 2 + 1;
        String[] sql = new String[sqlLength];
        int x = 0;

        //删除操作
        if (delete > 0) {
            for (int i = 0; i < originalAccountLength; i++) {
                //false,表示要删除
                if (!originalMap.get(originalAccountArray[i])) {
                    //修改变更业务账号的变更记录
                    StringBuffer itemSql = new StringBuffer("update config_channel_change_items  ");
                    itemSql.append(" set STATUS = '0");
                    itemSql.append(" where ACCOUNT_ID ='" + originalAccountArray[i] + "' and CHANNEL_ID='" + qo.getChannelId() + "' ");
                    sql[x] = itemSql.toString();
                    x++;
                    //修改业务账号 通道配置 优先级NORMAL
                    StringBuffer updateSql = new StringBuffer("update account_channel_info ");
                    updateSql.append(" set CHANNEL_PRIORITY ='NORMAL',CHANGE_SOURCE='CHANNEL_CHANGE' ");
                    itemSql.append(" where ACCOUNT_ID ='" + originalAccountArray[i] + "' and CHANNEL_ID='" + qo.getChannelId() + "' ");
                    sql[x] = updateSql.toString();
                    x++;
                }
            }
        }

        //添加操作
        if (add > 0) {
            for (Map.Entry entry : accountMap.entrySet()) {
                Boolean value = (Boolean) entry.getValue();
                if (value) {
                    //插入变更业务账号的变更记录
                    StringBuffer itemSql = new StringBuffer("insert into config_channel_change_items(ID,CHANGE_ID,BUSINESS_ACCOUNT,CHANGE_TYPE,CHANGE_BEFORE_PRIORITY,CHANGE_AFTER_PRIORITY,ACCOUNT_CHANNEL_ID,STATUS,CREATED_BY,CREATED_TIME) ");
                    itemSql.append(" select '" + UUID.uuid32() + "',CHANNEL_ID,ACCOUNT_ID,'" + qo.getChangeType() + "',CHANNEL_PRIORITY,'" + qo.getChangeType() + "',ID,'1','" + qo.getCreatedBy() + "',now() from account_channel_info ");
                    itemSql.append(" where ACCOUNT_ID ='" + entry.getKey() + "' and CHANNEL_ID='" + qo.getChannelId() + "' ");
                    sql[x] = itemSql.toString();
                    x++;
                    //修改业务账号 通道配置 优先级
                    StringBuffer updateSql = new StringBuffer("update account_channel_info ");
                    updateSql.append(" set CHANNEL_PRIORITY ='" + qo.getChangeType() + "',CHANGE_SOURCE='CHANNEL_CHANGE' ");
                    itemSql.append(" where ACCOUNT_ID ='" + entry.getKey() + "' and CHANNEL_ID='" + qo.getChannelId() + "' ");
                    sql[x] = updateSql.toString();
                    x++;
                }
            }
        }

        int length = originalAccountLength - delete + add;
        StringBuffer channelChangeUpdateSql = new StringBuffer("update config_channel_change ");
        channelChangeUpdateSql.append(" set CHANGE_REASON='" + qo.getChangeReason() + "',ACCOUNT_NUM = " + length + ",UPDATED_BY='" + qo.getUpdatedBy() + "',UPDATED_TIME = now() ");
        channelChangeUpdateSql.append(" where ID ='" + qo.getId() + "' ");
        sql[x] = channelChangeUpdateSql.toString();
        this.jdbcTemplate.batchUpdate(sql);
        return;

    }

    /**
     * 取消通道变更
     *
     * @param qo
     * @param originalAccountIds
     */
    public void cancelChannelChange(ConfigChannelChange qo, String originalAccountIds) {

        //如果选中的业务账号为空,并且原来的也为空
        if (StringUtils.isEmpty(originalAccountIds)) {
            StringBuffer channelChangeUpdateSql = new StringBuffer("update config_channel_change ");
            channelChangeUpdateSql.append(" set CHANGE_STATUS = '0' ");
            channelChangeUpdateSql.append(" where ID ='" + qo.getId() + "' ");
            this.jdbcTemplate.update(channelChangeUpdateSql.toString());
            return;
        }

        //原来业务账号处理
        String[] originalAccountArray = originalAccountIds.split(",");
        int originalAccountLength = originalAccountArray.length;

        String[] sql = new String[originalAccountLength * 2 + 1];
        int j = 0;
        for (int i = 0; i < originalAccountLength; i++) {
            //修改变更业务账号的变更记录
            StringBuffer itemSql = new StringBuffer("update config_channel_change_items  ");
            itemSql.append(" set STATUS = '0");
            itemSql.append(" where ACCOUNT_ID ='" + originalAccountArray[i] + "' and CHANNEL_ID='" + qo.getChannelId() + "' ");
            sql[j] = itemSql.toString();
            j++;
            //修改业务账号 通道配置 优先级NORMAL
            StringBuffer updateSql = new StringBuffer("update account_channel_info ");
            updateSql.append(" set CHANNEL_PRIORITY ='NORMAL',CHANGE_SOURCE='CHANNEL_CHANGE' ");
            itemSql.append(" where ACCOUNT_ID ='" + originalAccountArray[i] + "' and CHANNEL_ID='" + qo.getChannelId() + "' ");
            sql[j] = updateSql.toString();
            j++;

        }

        StringBuffer channelChangeUpdateSql = new StringBuffer("update config_channel_change ");
        channelChangeUpdateSql.append(" set CHANGE_STATUS = '0' ");
        channelChangeUpdateSql.append(" where ID ='" + qo.getId() + "' ");
        sql[j] = channelChangeUpdateSql.toString();
        this.jdbcTemplate.batchUpdate(sql);

    }
}
