package com.smoc.cloud.customer.repository;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBookInfoValidator;
import com.smoc.cloud.customer.entity.EnterpriseBookInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


/**
 * 企业通讯录操作类
 */
public interface EnterpriseBookRepository extends CrudRepository<EnterpriseBookInfo, String>, JpaRepository<EnterpriseBookInfo, String> {


    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    PageList<EnterpriseBookInfoValidator> page(PageParams<EnterpriseBookInfoValidator> pageParams);

    List<EnterpriseBookInfo> findByGroupIdAndMobileAndStatus(String groupId, String mobile, String status);

    void bathSave(EnterpriseBookInfoValidator enterpriseBookInfoValidator);
}
