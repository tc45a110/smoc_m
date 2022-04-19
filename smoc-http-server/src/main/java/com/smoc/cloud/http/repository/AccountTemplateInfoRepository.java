package com.smoc.cloud.http.repository;

import com.smoc.cloud.http.entity.AccountTemplateInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 模板管理
 */
public interface AccountTemplateInfoRepository extends JpaRepository<AccountTemplateInfo, String> {


    /**
     * 保存模板信息
     * @param accountTemplateInfo
     */
    void saveHandle(AccountTemplateInfo accountTemplateInfo);


}