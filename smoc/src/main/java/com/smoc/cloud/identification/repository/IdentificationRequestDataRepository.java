package com.smoc.cloud.identification.repository;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.identification.validator.IdentificationRequestDataValidator;
import com.smoc.cloud.identification.entity.IdentificationRequestData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdentificationRequestDataRepository extends JpaRepository<IdentificationRequestData, String> {

    PageList<IdentificationRequestDataValidator> page(PageParams<IdentificationRequestDataValidator> pageParams);
}