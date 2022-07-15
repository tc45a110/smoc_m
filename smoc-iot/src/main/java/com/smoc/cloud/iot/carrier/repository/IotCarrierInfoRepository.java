package com.smoc.cloud.iot.carrier.repository;

import com.smoc.cloud.iot.carrier.entity.IotCarrierInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IotCarrierInfoRepository extends JpaRepository<IotCarrierInfo, String>, JpaSpecificationExecutor<IotCarrierInfo> {
}