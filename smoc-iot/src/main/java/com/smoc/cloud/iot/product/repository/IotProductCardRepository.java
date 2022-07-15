package com.smoc.cloud.iot.product.repository;

import com.smoc.cloud.iot.product.entity.IotProductCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IotProductCardRepository extends JpaRepository<IotProductCard, String> {
}