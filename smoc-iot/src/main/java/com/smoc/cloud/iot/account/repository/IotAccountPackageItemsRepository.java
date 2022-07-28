package com.smoc.cloud.iot.account.repository;

import com.smoc.cloud.common.iot.validator.IotPackageInfoValidator;
import com.smoc.cloud.iot.account.entity.IotAccountPackageItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IotAccountPackageItemsRepository extends JpaRepository<IotAccountPackageItems, String> {

    /**
     * 查询业务账号套餐及未使用套餐
     *
     * @param accountId
     * @return
     */
    List<IotPackageInfoValidator> list(String accountId);

    /**
     * 查询账号配置得套餐
     *
     * @param accountId
     * @return
     */
    List<IotPackageInfoValidator> listAccountPackages(String accountId);

    /**
     * 批量保存套餐卡
     */
    void insertAccountPackageCards(String account, String packageIds);
}