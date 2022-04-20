package com.smoc.cloud.http.repository;

import com.smoc.cloud.http.entity.AccountTemplateInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 模板管理
 */
public interface AccountTemplateInfoRepository extends JpaRepository<AccountTemplateInfo, String> {


    Optional<AccountTemplateInfo> findAccountTemplateInfoByBusinessAccountAndTemplateIdAndTemplateAgreementType(String businessAccount,String templateId,String http);

    /**
     * 保存模板信息
     * @param accountTemplateInfo
     */
    void saveHandle(AccountTemplateInfo accountTemplateInfo);


}