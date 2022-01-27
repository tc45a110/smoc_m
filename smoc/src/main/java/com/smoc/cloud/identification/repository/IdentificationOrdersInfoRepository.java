package com.smoc.cloud.identification.repository;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.identification.validator.IdentificationOrdersInfoValidator;
import com.smoc.cloud.identification.entity.IdentificationOrdersInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IdentificationOrdersInfoRepository extends JpaRepository<IdentificationOrdersInfo, String> {

    Optional<IdentificationOrdersInfo> findIdentificationOrdersInfoByOrderNo(String orderNo);

    PageList<IdentificationOrdersInfoValidator> page(PageParams<IdentificationOrdersInfoValidator> pageParams);

}