package com.smoc.cloud.auth.data.provider.repository;

import com.smoc.cloud.auth.data.provider.entity.BaseUser;
import com.smoc.cloud.auth.data.provider.entity.BaseUserExtends;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.qo.Users;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 用户数据操作类
 * 2019/3/29 14:29
 */
public interface BaseUserRepository extends CrudRepository<BaseUser, String>, JpaRepository<BaseUser, String> {

    List<BaseUser> findBaseUserByUserNameAndActive(String userName, Integer active);

    BaseUser findBaseUserByPhoneAndActive(String phone, Integer active);

    BaseUser findBaseUserByUserNameAndActiveOrPhone(String userName, Integer active, String phone);

    /**
     * 分页查询
     *
     * @param pageParams 分页参数
     * @return
     */
    PageList page(PageParams<Users> pageParams);

    /**
     * 重置密码
     * @param id
     * @param password
     */
    void resetPassword(String id, String password);

    /**
     * 禁用、启用用户
     * @param id
     */
    void forbiddenUser(String id, Integer status);

    SecurityUser findSecurityUserByUserName(String userName);

    SecurityUser findSecurityUserByCa(String ca);

    List<SecurityUser> findSecurityUserByOrgId(String orgId);

    //给票据表里添加客户
    void addCustomer(BaseUser user, BaseUserExtends userExtends);

    @Modifying
    @Query(value = "update base_user set ACTIVE = :status where USER_NAME = :entcode",nativeQuery = true)
    void updateUserActive(@Param("entcode") String entcode, @Param("status") String status);
}
