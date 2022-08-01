package com.smoc.cloud.iot.account.repository;

import com.smoc.cloud.api.response.info.SimBaseInfoResponse;
import com.smoc.cloud.common.iot.validator.IotPackageInfoValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
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

    /**
     * 根据套餐id，查询套餐下绑定的物联网卡
     *
     * @return
     */
    PageList<SimBaseInfoResponse> queryCardsByPackageId(String account, String packageId, PageParams pageParams);

    /**
     * 根据iccid，查询物联网卡明细
     *
     * @return
     */
    SimBaseInfoResponse querySimBaseInfo(String account, String iccid);

    /**
     * 根据iccids，批量查询物联网卡信息
     *
     * @return
     */
    PageList<SimBaseInfoResponse> queryBatchSimBaseInfo(String account, List<String> iccids,PageParams pageParams);
}