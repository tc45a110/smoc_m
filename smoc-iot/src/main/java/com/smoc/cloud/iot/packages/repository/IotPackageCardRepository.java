package com.smoc.cloud.iot.packages.repository;

import com.smoc.cloud.common.iot.validator.IotFlowCardsPrimaryInfoValidator;
import com.smoc.cloud.common.iot.validator.IotPackageCardValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.iot.packages.entity.IotPackageCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IotPackageCardRepository extends JpaRepository<IotPackageCard, String> {

    /**
     * 分页查询
     * @param pageParams
     * @return
     */
    PageList<IotPackageCardValidator> page(PageParams<IotPackageCardValidator> pageParams);

    /**
     * 批量保存套餐卡
     */
    void insertPackageCards(String packageId, String cardsId);

    /**
     * 根据套餐id查询，套餐配置的信息，及未配置的 物联网卡信息
     *
     * @param packageId
     * @return
     */
    List<IotFlowCardsPrimaryInfoValidator> list(String packageId,String packageType);


    /**
     * 根据套餐id，查询套餐绑定的物联网卡
     *
     * @param packageId
     * @return
     */
    List<IotFlowCardsPrimaryInfoValidator> listCardsByPackageId(String account,String packageId);
}