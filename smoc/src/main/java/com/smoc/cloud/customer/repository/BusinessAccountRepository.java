package com.smoc.cloud.customer.repository;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.customer.entity.AccountBasicInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * 业务账号操作类
 */
public interface BusinessAccountRepository extends CrudRepository<AccountBasicInfo, String>, JpaRepository<AccountBasicInfo, String> {

    /**
     * 查询-分页
     * @param pageParams
     * @return
     */
    PageList<AccountBasicInfoValidator> page(PageParams<AccountBasicInfoValidator> pageParams);


    Iterable<AccountBasicInfo> findByAccountId(String account);
}
