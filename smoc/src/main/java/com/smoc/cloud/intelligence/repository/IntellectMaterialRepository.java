package com.smoc.cloud.intelligence.repository;

import com.smoc.cloud.intelligence.entity.IntellectMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IntellectMaterialRepository extends JpaRepository<IntellectMaterial, String> {

    List<IntellectMaterial> findIntellectMaterialByMaterialTypeId(String materialTypeId);
}