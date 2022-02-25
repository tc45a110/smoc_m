package com.smoc.cloud.customer.repository;


import com.smoc.cloud.common.smoc.customer.validator.SystemAttachmentValidator;
import com.smoc.cloud.customer.entity.SystemAttachmentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


/**
 * 附件操作类
 */
public interface SystemAttachmentRepository extends CrudRepository<SystemAttachmentInfo, String>, JpaRepository<SystemAttachmentInfo, String> {


    /**
     * 批量保存附件
     * @param filesList
     */
    void batchSave(List<SystemAttachmentValidator> filesList);

    /**
     * 根据业务id查询附件
     * @param id
     * @param status
     * @return
     */
    List<SystemAttachmentInfo> findByMoudleIdAndAttachmentStatus(String id, String status);

    //更新状态
    @Modifying
    @Query(value = "update system_attachment_info set ATTACHMENT_STATUS = 0 where MOUDLE_ID = :id ",nativeQuery = true)
    void updateAttachmentStatusByMoudleId(@Param("id") String id);
}
