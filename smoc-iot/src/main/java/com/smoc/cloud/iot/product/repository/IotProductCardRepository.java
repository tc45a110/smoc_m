package com.smoc.cloud.iot.product.repository;

import com.smoc.cloud.common.iot.validator.IotFlowCardsPrimaryInfoValidator;
import com.smoc.cloud.common.iot.validator.IotProductCardValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.iot.product.entity.IotProductCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IotProductCardRepository extends JpaRepository<IotProductCard, String> {

    /**
     * 分页查询
     * @param pageParams
     * @return
     */
    PageList<IotProductCardValidator> page(PageParams<IotProductCardValidator> pageParams);

    /**
     * 批量保存产品卡
     */
    void insertProductCards(String productId, String cardsId);

    /**
     * 根据产品id查询，产品配置的信息，及未配置的 物联网卡信息
     *
     * @param productId
     * @return
     */
    List<IotFlowCardsPrimaryInfoValidator> list(String productId);
}