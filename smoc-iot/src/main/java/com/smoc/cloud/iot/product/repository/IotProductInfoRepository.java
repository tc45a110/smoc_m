package com.smoc.cloud.iot.product.repository;

import com.smoc.cloud.common.iot.validator.IotProductInfoValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.iot.product.entity.IotProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IotProductInfoRepository extends JpaRepository<IotProductInfo, String> {

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    PageList<IotProductInfoValidator> page(PageParams<IotProductInfoValidator> pageParams);

    List<IotProductInfo> findByProductName(String productName);

    @Modifying
    @Query(value = "update  iot_product_info set PRODUCT_STATUS=:status where ID = :id ", nativeQuery = true)
    void forbidden(@Param("id") String id, @Param("status") String status);

}