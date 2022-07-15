package com.smoc.cloud.iot.account.repository;

import com.smoc.cloud.iot.account.entity.IotUserProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IotUserProductInfoRepository extends JpaRepository<IotUserProductInfo, String> {
}