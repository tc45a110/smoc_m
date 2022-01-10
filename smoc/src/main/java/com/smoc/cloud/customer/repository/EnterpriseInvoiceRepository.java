package com.smoc.cloud.customer.repository;


import com.smoc.cloud.customer.entity.EnterpriseInvoiceInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;


/**
 * 企业发票信息操作类
 */
public interface EnterpriseInvoiceRepository extends CrudRepository<EnterpriseInvoiceInfo, String>, JpaRepository<EnterpriseInvoiceInfo, String> {


    Iterable<EnterpriseInvoiceInfo> findByEnterpriseIdAndInvoiceTypeAndInvoiceTitle(String enterpriseId, String invoiceType, String invoiceTitle);
}
