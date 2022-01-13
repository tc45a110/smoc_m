package com.smoc.cloud.customer.repository;


import com.smoc.cloud.common.smoc.customer.validator.EnterpriseExpressInfoValidator;
import com.smoc.cloud.customer.entity.EnterpriseExpressInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


/**
 * 企业邮寄信息操作类
 */
public interface EnterpriseExpressRepository extends CrudRepository<EnterpriseExpressInfo, String>, JpaRepository<EnterpriseExpressInfo, String> {


    Iterable<EnterpriseExpressInfo> findByEnterpriseIdAndPostContactsAndPostPhoneAndPostAddress(String enterpriseId, String postContacts, String postPhone, String postAddress);

    List<EnterpriseExpressInfoValidator> page(EnterpriseExpressInfoValidator enterpriseExpressInfoValidator);

    List<EnterpriseExpressInfo> findByEnterpriseId(String enterpriseId);
}
