package com.smoc.cloud.filter.repository;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.filter.FilterSignFrequencyLimitValidator;
import com.smoc.cloud.filter.entity.FilterSignFrequencyLimit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface FilterSignFrequencyLimitRepository extends CrudRepository<FilterSignFrequencyLimit, String>, JpaRepository<FilterSignFrequencyLimit, String> {

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    PageList<FilterSignFrequencyLimitValidator> page(PageParams<FilterSignFrequencyLimitValidator> pageParams);
}
