package com.smoc.cloud.template.repository;

import com.smoc.cloud.template.entity.AccountTemplateInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountTemplateInfoRepository extends JpaRepository<AccountTemplateInfo, String> {
}