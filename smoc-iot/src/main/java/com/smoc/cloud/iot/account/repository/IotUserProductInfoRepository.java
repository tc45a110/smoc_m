package com.smoc.cloud.iot.account.repository;

import com.smoc.cloud.common.iot.validator.IotUserProductInfoValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.iot.account.entity.IotUserProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IotUserProductInfoRepository extends JpaRepository<IotUserProductInfo, String> {

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    PageList<IotUserProductInfoValidator> page(PageParams<IotUserProductInfoValidator> pageParams);

    List<IotUserProductInfo> findByUserId(String userId);
}