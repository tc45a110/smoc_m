package com.smoc.cloud.auth.data.provider.repository;

import com.smoc.cloud.auth.data.provider.entity.BaseCommDict;
import com.smoc.cloud.common.auth.qo.Dict;
import com.smoc.cloud.common.auth.validator.DictValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BaseCommDictRepository extends CrudRepository<BaseCommDict, String>, JpaRepository<BaseCommDict, String> {

    List<BaseCommDict>  findBaseCommDictByAndTypeIdAndDictTypeAndActiveOrderBySortAsc(String typeId, String dictType, Integer active);

    List<BaseCommDict> findBaseCommDictByDictNameAndTypeIdAndActive(String dictName, String typeId, Integer active);

    List<Dict> findDictByTypeId(String typeId);

    PageList<DictValidator> page(PageParams<DictValidator> dictValidator);
}
