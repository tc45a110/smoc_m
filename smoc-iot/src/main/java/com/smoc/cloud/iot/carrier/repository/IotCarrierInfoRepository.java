package com.smoc.cloud.iot.carrier.repository;

import com.smoc.cloud.common.iot.validator.IotCarrierInfoValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.iot.carrier.entity.IotCarrierInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IotCarrierInfoRepository extends JpaRepository<IotCarrierInfo, String>, JpaSpecificationExecutor<IotCarrierInfo> {

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    PageList<IotCarrierInfoValidator> page(PageParams<IotCarrierInfoValidator> pageParams);

    List<IotCarrierInfo> findByCarrierIdentifying(String carrierIdentifying);

    @Modifying
    @Query(value = "update  iot_carrier_info set CARRIER_STATUS=:status where ID = :id ", nativeQuery = true)
    void forbidden(@Param("id") String id, @Param("status") String status);
}