package com.smoc.cloud.customer.repository;


import com.smoc.cloud.common.smoc.customer.validator.EnterpriseInvoiceInfoValidator;
import com.smoc.cloud.customer.entity.EnterpriseInvoiceInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


/**
 * 企业发票信息操作类
 */
public interface EnterpriseInvoiceRepository extends CrudRepository<EnterpriseInvoiceInfo, String>, JpaRepository<EnterpriseInvoiceInfo, String> {


    Iterable<EnterpriseInvoiceInfo> findByEnterpriseIdAndInvoiceTypeAndInvoiceTitle(String enterpriseId, String invoiceType, String invoiceTitle);

    List<EnterpriseInvoiceInfoValidator> page(EnterpriseInvoiceInfoValidator enterpriseInvoiceInfoValidator);

    EnterpriseInvoiceInfo findByEnterpriseIdAndInvoiceType(String enterpriseId, String type);
}
