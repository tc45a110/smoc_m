package com.smoc.cloud.configure.number.repository;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigNumberInfoValidator;
import com.smoc.cloud.configure.number.entity.ConfigNumberInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 号段操作类
 */
public interface ConfigNumberInfoRepository extends CrudRepository<ConfigNumberInfo, String>, JpaRepository<ConfigNumberInfo, String> {


    PageList<ConfigNumberInfoValidator> page(PageParams<ConfigNumberInfoValidator> pageParams);

    List<ConfigNumberInfo> findByNumberCodeAndCarrierAndNumberCodeType(String numberCode, String carrier, String numberCodeType);

    void batchSave(ConfigNumberInfoValidator configNumberInfoValidator);

    List<ConfigNumberInfoValidator> findConfigNumberInfoList();

    void bathUpdateStatus(List<ConfigNumberInfoValidator> list);

    @Modifying
    @Query(value = "delete from config_number_info where NUMBER_CODE =:numberCode ",nativeQuery = true)
    void deleteByNumberCode(@Param("numberCode") String numberCode);
}
