package com.smoc.cloud.auth.data.provider.repository;

import com.smoc.cloud.auth.data.provider.entity.BaseOrganization;
import com.smoc.cloud.common.auth.qo.Nodes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BaseOrganizationRepository extends CrudRepository<BaseOrganization, String>, JpaRepository<BaseOrganization, String> {

    List<BaseOrganization> findBaseOrganizationByActiveAndOrgTypeAndParentIdOrderBySortAsc(Integer active,Integer orgType,String parentId);

    List<BaseOrganization> findBaseOrganizationByOrgNameAndActive(String orgName,Integer active);

    /**
     * 根据父ID 查询组织机构信息
     * @param parentId
     * @return
     */
    List<Nodes> getOrgByParentId(String parentId);

    /**
     * 根据id把数据修改为非叶子节点
     * @param id
     */
    @Modifying
    @Query(value = "update base_organization set IS_LEAF = :isLeaf where ID = :id",nativeQuery = true)
    void updateLeafById(@Param("isLeaf") Integer isLeaf,@Param("id") String id);

}
