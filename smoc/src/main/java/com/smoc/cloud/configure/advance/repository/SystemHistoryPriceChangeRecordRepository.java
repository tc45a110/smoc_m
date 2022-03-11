package com.smoc.cloud.configure.advance.repository;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.configuate.validator.SystemHistoryPriceChangeRecordValidator;
import com.smoc.cloud.configure.advance.entity.SystemHistoryPriceChangeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface SystemHistoryPriceChangeRecordRepository extends CrudRepository<SystemHistoryPriceChangeRecord, String>, JpaRepository<SystemHistoryPriceChangeRecord, String> {

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    PageList<SystemHistoryPriceChangeRecordValidator> page(PageParams<SystemHistoryPriceChangeRecordValidator> pageParams);
}
