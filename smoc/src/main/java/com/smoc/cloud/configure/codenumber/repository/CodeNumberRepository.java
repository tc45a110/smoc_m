package com.smoc.cloud.configure.codenumber.repository;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.validator.CodeNumberInfoValidator;
import com.smoc.cloud.configure.codenumber.entity.ConfigNumberCodeInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * 码号操作类
 */
public interface CodeNumberRepository extends CrudRepository<ConfigNumberCodeInfo, String>, JpaRepository<ConfigNumberCodeInfo, String> {


    Iterable<ConfigNumberCodeInfo> findBySrcIdAndCarrierAndProvinceAndBusinessType(String srcId, String carrier, String province, String businessType);

    PageList<CodeNumberInfoValidator> page(PageParams<CodeNumberInfoValidator> pageParams);
}
