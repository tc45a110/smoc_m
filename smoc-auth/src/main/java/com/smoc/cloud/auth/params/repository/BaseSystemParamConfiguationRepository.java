package com.smoc.cloud.auth.params.repository;

import com.smoc.cloud.auth.params.entity.BaseSystemParamConfiguation;
import com.smoc.cloud.common.auth.qo.ConfiguationParams;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * 2020/5/30 14:34
 **/
public interface BaseSystemParamConfiguationRepository extends CrudRepository<BaseSystemParamConfiguation, String>, JpaRepository<BaseSystemParamConfiguation, String> {


    //分页查询
    PageList<ConfiguationParams> page(PageParams<ConfiguationParams> pageParams);

    void deleteAllByUserId(String userId);

    List<ConfiguationParams> findConfiguationParams(ConfiguationParams params);

}
