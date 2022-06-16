package com.smoc.cloud.configure.channel.repository;


import com.smoc.cloud.configure.channel.entity.ConfigRepairRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


/**
 * 失败补发规则操作类
 */
public interface ConfigRepairRuleRepository extends CrudRepository<ConfigRepairRule, String>, JpaRepository<ConfigRepairRule, String> {


    List<ConfigRepairRule> findByBusinessIdAndBusinessTypeAndRepairStatus(String businessId, String businessType, String s);

    ConfigRepairRule findByBusinessIdAndRepairStatus(String businessId, String status);
}
