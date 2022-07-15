package com.smoc.cloud.iot.product.repository;

import com.smoc.cloud.iot.product.entity.IotProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IotProductInfoRepository extends JpaRepository<IotProductInfo, String> {
}