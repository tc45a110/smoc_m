package com.smoc.cloud.auth.data.provider.repository;

import com.smoc.cloud.auth.common.page.BasePageRepository;
import com.smoc.cloud.auth.data.provider.entity.BaseUser;
import com.smoc.cloud.auth.data.provider.entity.BaseUserExtends;
import com.smoc.cloud.auth.data.provider.rowmapper.SecurityUserRowMapper;
import com.smoc.cloud.auth.data.provider.rowmapper.UsersRowMapper;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.qo.Users;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 用户操作实现类
 * 2019/5/6 12:21
 **/
@Slf4j
public class BaseUserRepositoryImpl extends BasePageRepository {


    public PageList page(PageParams<Users> pageParams) {

        //查询sql
        StringBuffer sqlBuffer = new StringBuffer("select user.ID,user.USER_NAME,user.PHONE,user.ACTIVE,extends.REAL_NAME,extends.CORPORATION,extends.CODE,extends.TYPE,extends.HEADER from base_user user,base_user_extends extends where user.ID = extends.ID and extends.type!=0 ");

        //组织查询条件
        Users user = pageParams.getParams();
        int paramSize = 0;
        Object[] tempParams = new Object[8];
        if (null != user) {
            if (!StringUtils.isEmpty(user.getUserName())) {
                sqlBuffer.append(" and user.USER_NAME =? ");
                tempParams[paramSize] = user.getUserName();
                paramSize++;
            }

            if (!StringUtils.isEmpty(user.getPhone())) {
                sqlBuffer.append("and user.PHONE=? ");
                tempParams[paramSize] = user.getPhone();
                paramSize++;
            }

            if (!StringUtils.isEmpty(user.getRealName())) {
                sqlBuffer.append("and extends.REAL_NAME=? ");
                tempParams[paramSize] = user.getRealName();
                paramSize++;
            }

//            if (!StringUtils.isEmpty(user.getCorporation())) {
//                sqlBuffer.append("and extends.CORPORATION=? ");
//                tempParams[paramSize] = user.getCorporation();
//                paramSize++;
//            }

            if (!StringUtils.isEmpty(user.getOrganization())) {
                sqlBuffer.append("and user.ORGANIZATION=? ");
                tempParams[paramSize] = user.getOrganization();
                paramSize++;
            }

            if (!StringUtils.isEmpty(user.getCode())) {
                sqlBuffer.append("and extends.CODE=? ");
                tempParams[paramSize] = user.getCode();
                paramSize++;
            }

        }

        //根据参数个数，组织参数值
        Object[] params = null;
        if (!(0 == paramSize)) {
            params = new Object[paramSize];
            int j = 0;
            for (int i = 0; i < tempParams.length; i++) {
                if (null != tempParams[i]) {
                    params[j] = tempParams[i];
                    j++;
                }
            }
        }

        PageList<Users> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new UsersRowMapper());
        pageList.getPageParams().setParams(user);
        return pageList;

    }

    public void resetPassword(String id, String password) {
        String sql = "update  base_user set PASSWORD = ? where ID = ?";

        Object[] params = new Object[2];
        params[0] = password;
        params[1] = id;
        jdbcTemplate.update(sql, params);
    }

    public void forbiddenUser(String id, Integer status) {
        String sql = "update  base_user set ACTIVE = ? where ID = ?";

        Object[] params = new Object[2];
        params[0] = status;
        params[1] = id;
        jdbcTemplate.update(sql, params);
    }

    public SecurityUser findSecurityUserByUserName(String userName) {
        if (StringUtils.isEmpty(userName)) {
            return null;
        }

        String sql = "select a.ID,a.USER_NAME,e.REAL_NAME,a.PASSWORD,a.PHONE,a.ORGANIZATION,e.CORPORATION,e.DEPARTMENT,e.PARENT_CODE,e.CODE,e.ADMINISTRATOR,e.TEAM_LEADER,e.TYPE,e.WEB_CHAT from base_user a,base_user_extends e where a.ID = e.ID and a.ACTIVE =1 and a.USER_NAME =?";

        Object[] params = new Object[1];
        params[0] = userName;
        List<SecurityUser> users = jdbcTemplate.query(sql, params, new SecurityUserRowMapper());
        if (null != users && users.size() > 0) {
            return users.get(0);
        }

        return null;
    }

    public SecurityUser findSecurityUserByCa(String ca) {
        if (StringUtils.isEmpty(ca)) {
            return null;
        }

        String sql = "select a.ID,a.USER_NAME,e.REAL_NAME,a.PASSWORD,a.PHONE,a.ORGANIZATION,e.CORPORATION,e.DEPARTMENT,e.PARENT_CODE,e.CODE,e.ADMINISTRATOR,e.TEAM_LEADER,e.TYPE,e.WEB_CHAT from base_user a,base_user_extends e,meip.meip_ent_info ent where a.ID = e.ID and a.ACTIVE =1 and a.USER_NAME = ent.ENT_CODE  and ent.LOGIN_CODE=? ";

        Object[] params = new Object[1];
        params[0] = ca;
        List<SecurityUser> users = jdbcTemplate.query(sql, params, new SecurityUserRowMapper());
        if (null != users && users.size() > 0) {
            return users.get(0);
        }

        return null;
    }

    public List<SecurityUser> findSecurityUserByOrgId(String orgId) {
        if (StringUtils.isEmpty(orgId)) {
            return null;
        }

        String sql = "select a.ID,a.USER_NAME,e.REAL_NAME,'aa' AS PASSWORD,a.PHONE,a.ORGANIZATION,e.CORPORATION,e.DEPARTMENT,e.PARENT_CODE,e.CODE,e.ADMINISTRATOR,e.TEAM_LEADER,e.TYPE,e.WEB_CHAT from base_user a,base_user_extends e where a.ID = e.ID and a.ACTIVE =1 and a.ORGANIZATION =?";

        Object[] params = new Object[1];
        params[0] = orgId;
        List<SecurityUser> users = jdbcTemplate.query(sql, params, new SecurityUserRowMapper());


        return users;
    }

    public void addCustomer(BaseUser user, BaseUserExtends userExtends){

        String sql = "insert into t_bill_customer "
                + "(ID,USER_NAME,PHONE,ORGANIZATION_ID,TEAM_ID,CREATE_DATE,ACTIVE) values('"+user.getId()+"','"+userExtends.getRealName()+"','"+user.getPhone()+"','"+user.getOrganization()+"','"+userExtends.getParentCode()+"',now(),1)";
        jdbcTemplate.update(sql);
    }

}
