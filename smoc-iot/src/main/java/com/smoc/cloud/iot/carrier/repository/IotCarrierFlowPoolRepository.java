package com.smoc.cloud.iot.carrier.repository;

import com.smoc.cloud.common.iot.validator.IotCarrierFlowPoolValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.iot.carrier.entity.IotCarrierFlowPool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IotCarrierFlowPoolRepository extends JpaRepository<IotCarrierFlowPool, String> {

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    PageList<IotCarrierFlowPoolValidator> page(PageParams<IotCarrierFlowPoolValidator> pageParams);

    List<IotCarrierFlowPool> findByPoolName(String poolName);

    @Modifying
    @Query(value = "update  iot_carrier_flow_pool set POOL_STATUS=:status where ID = :id ", nativeQuery = true)
    void forbidden(@Param("id") String id, @Param("status") String status);
}