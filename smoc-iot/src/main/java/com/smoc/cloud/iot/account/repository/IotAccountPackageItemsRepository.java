package com.smoc.cloud.iot.account.repository;

import com.smoc.cloud.common.iot.validator.IotPackageInfoValidator;
import com.smoc.cloud.iot.account.entity.IotAccountPackageItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IotAccountPackageItemsRepository extends JpaRepository<IotAccountPackageItems, String> {

    /**
     * 列表查询
     *
     * @param userId
     * @return
     */
    List<IotPackageInfoValidator> list(String userId);

    /**
     * 批量保存套餐卡
     */
    void insertAccountPackageCards(String account, String packageIds);
}