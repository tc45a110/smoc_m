package com.smoc.cloud.iot.carrier.repository;

import com.smoc.cloud.iot.carrier.entity.IotFlowCardsChangeHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IotFlowCardsChangeHistoryRepository extends JpaRepository<IotFlowCardsChangeHistory, String> {
}