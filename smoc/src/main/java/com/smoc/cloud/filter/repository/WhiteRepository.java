package com.smoc.cloud.filter.repository;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.filter.FilterWhiteListValidator;
import com.smoc.cloud.filter.entity.FilterWhiteList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface WhiteRepository extends CrudRepository<FilterWhiteList, String>, JpaRepository<FilterWhiteList, String> {


    /**
     *  根据群id查询白名单
     * @param pageParams
     * @return
     */
    PageList<FilterWhiteListValidator> page(PageParams<FilterWhiteListValidator> pageParams);

    /**
     * 查询组里是否存在手机号
     * @param groupId
     * @param mobile
     * @param status
     * @return
     */
    List<FilterWhiteList> findByGroupIdAndMobileAndStatus(String groupId, String mobile, String status);
}
