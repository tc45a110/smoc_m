package com.smoc.cloud.message.repository;

import com.smoc.cloud.message.entity.AccountTemplateInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountTemplateInfoRepository extends JpaRepository<AccountTemplateInfo, String> {
}