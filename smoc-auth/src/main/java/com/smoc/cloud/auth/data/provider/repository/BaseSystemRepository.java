package com.smoc.cloud.auth.data.provider.repository;

import com.smoc.cloud.auth.data.provider.entity.BaseSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * 系统数据操作类
 * 2019/3/29 14:29
 */
public interface BaseSystemRepository extends CrudRepository<BaseSystem, String>, JpaRepository<BaseSystem, String> {

    List<BaseSystem> findAllByOrderBySortAsc();

    Iterable<BaseSystem> findBaseSystemBySystemNameOrProjectName(String systemName, String projectName);

}
