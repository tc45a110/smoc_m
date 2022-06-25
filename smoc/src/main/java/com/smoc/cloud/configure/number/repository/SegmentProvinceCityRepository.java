package com.smoc.cloud.configure.number.repository;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.configuate.validator.SystemSegmentProvinceCityValidator;
import com.smoc.cloud.configure.number.entity.SystemSegmentProvinceCity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * 省号码操作类
 */
public interface SegmentProvinceCityRepository extends CrudRepository<SystemSegmentProvinceCity, String>, JpaRepository<SystemSegmentProvinceCity, String> {


    PageList<SystemSegmentProvinceCityValidator> page(PageParams<SystemSegmentProvinceCityValidator> pageParams);

    List<SystemSegmentProvinceCity> findBySegmentAndProvinceCode(String segment, String provinceCode);

    void batchSave(SystemSegmentProvinceCityValidator systemSegmentProvinceCityValidator);
}
