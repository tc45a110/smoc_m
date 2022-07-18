package com.smoc.cloud.iot.product.repository;

import com.smoc.cloud.common.iot.validator.IotProductCardValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.iot.product.entity.IotProductCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IotProductCardRepository extends JpaRepository<IotProductCard, String> {

    /**
     * 分页查询
     * @param pageParams
     * @return
     */
    PageList<IotProductCardValidator> page(PageParams<IotProductCardValidator> pageParams);
}