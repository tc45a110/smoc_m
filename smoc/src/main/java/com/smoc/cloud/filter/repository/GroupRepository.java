package com.smoc.cloud.filter.repository;

import com.smoc.cloud.common.auth.qo.Nodes;
import com.smoc.cloud.filter.entity.FilterGroupList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GroupRepository extends CrudRepository<FilterGroupList, String>, JpaRepository<FilterGroupList, String> {

    /**
     * 查询树形
     * @param enterprise
     * @param parentId
     * @return
     */
    List<Nodes> getGroupByParentId(String enterprise, String parentId);

    List<FilterGroupList> findByEnterpriseIdAndParentIdOrderByCreatedTimeDesc(String enterprise, String parentId);

    List<FilterGroupList> findByGroupNameAndEnterpriseIdAndStatus(String groupName, String enterpriseId, String status);

    List<FilterGroupList> findByEnterpriseIdAndParentIdOrderBySortAsc(String enterpriseId, String parentId);
}
