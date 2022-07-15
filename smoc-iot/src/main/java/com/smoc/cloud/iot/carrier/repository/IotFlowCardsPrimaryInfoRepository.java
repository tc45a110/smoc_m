package com.smoc.cloud.iot.carrier.repository;

import com.smoc.cloud.iot.carrier.entity.IotFlowCardsPrimaryInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IotFlowCardsPrimaryInfoRepository extends JpaRepository<IotFlowCardsPrimaryInfo, String> {
}