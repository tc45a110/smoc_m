package com.smoc.cloud.iot.carrier.repository;

import com.smoc.cloud.iot.carrier.entity.IotFlowCardsSecondaryInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IotFlowCardsSecondaryInfoRepository extends JpaRepository<IotFlowCardsSecondaryInfo, String> {
}