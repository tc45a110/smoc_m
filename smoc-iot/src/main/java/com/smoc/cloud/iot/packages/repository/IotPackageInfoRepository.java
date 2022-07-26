package com.smoc.cloud.iot.packages.repository;

import com.smoc.cloud.api.response.account.IotAccountPackageInfo;
import com.smoc.cloud.common.iot.validator.IotPackageInfoValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.iot.packages.entity.IotPackageInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IotPackageInfoRepository extends JpaRepository<IotPackageInfo, String> {

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    PageList<IotPackageInfoValidator> page(PageParams<IotPackageInfoValidator> pageParams);

    List<IotPackageInfo> findByPackageName(String packageName);

    /**
     * 根据用户账号分页查询账号套餐
     *
     * @param account
     * @return
     */
    PageList<IotAccountPackageInfo> page(String account, PageParams<IotAccountPackageInfo> pageParams);

    /**
     * 根据用户账号分页查询账号套餐
     *
     * @param account
     * @param packageId
     * @return
     */
    IotAccountPackageInfo queryAccountPackageById(String account, String packageId);

    @Modifying
    @Query(value = "update iot_package_info set PACKAGE_STATUS=:status where ID = :id ", nativeQuery = true)
    void forbidden(@Param("id") String id, @Param("status") String status);

}