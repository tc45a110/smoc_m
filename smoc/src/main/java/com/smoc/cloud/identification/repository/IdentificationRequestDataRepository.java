package com.smoc.cloud.identification.repository;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.identification.validator.IdentificationRequestDataValidator;
import com.smoc.cloud.identification.entity.IdentificationOrdersInfo;
import com.smoc.cloud.identification.entity.IdentificationRequestData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IdentificationRequestDataRepository extends JpaRepository<IdentificationRequestData, String> {

    PageList<IdentificationRequestDataValidator> page(PageParams<IdentificationRequestDataValidator> pageParams);

    Optional<IdentificationRequestData> findIdentificationRequestDataByOrderNo(String orderNo);
}