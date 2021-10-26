package com.smoc.cloud.auth.data.provider.repository;

import com.smoc.cloud.auth.data.provider.entity.BaseCommDictType;
import com.smoc.cloud.common.auth.qo.Nodes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BaseCommDictTypeRepository extends CrudRepository<BaseCommDictType, String>, JpaRepository<BaseCommDictType, String> {

    List<BaseCommDictType> findBaseCommDictTypeByActiveOrderBySortAsc(Integer active);

    List<BaseCommDictType> findBaseCommDictTypeByDictTypeSystemAndDictTypeCodeAndActive(String system, String dictType, Integer active);

    List<BaseCommDictType> findBaseCommDictTypeByActiveAndDictTypeSystemLike(Integer active, String system);

    List<Nodes> getDictTree();

    List<Nodes> getDictTree(String projectName);
}
