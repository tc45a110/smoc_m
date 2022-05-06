package com.smoc.cloud.intelligence.repository;

import com.smoc.cloud.intelligence.entity.IntellectMaterialType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IntellectMaterialTypeRepository extends JpaRepository<IntellectMaterialType, String> {

    List<IntellectMaterialType> findIntellectMaterialTypeByParentIdAndStatusOrderByDisplaySortDesc(String parentId,String status);

    List<IntellectMaterialType> findIntellectMaterialTypeByParentIdAndTitleAndStatus(String parentId,String title,String status);

    @Modifying
    @Query(value = "update intellect_material_type set STATUS = :status where ID = :id ",nativeQuery = true)
    void cancel(@Param("id") String id, @Param("status") String status);

}