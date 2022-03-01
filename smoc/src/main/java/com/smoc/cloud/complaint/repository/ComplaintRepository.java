package com.smoc.cloud.complaint.repository;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.message.MessageComplaintInfoValidator;
import com.smoc.cloud.complaint.entity.MessageComplaintInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;



/**
 * 投诉管理操作类
 */
public interface ComplaintRepository extends CrudRepository<MessageComplaintInfo, String>, JpaRepository<MessageComplaintInfo, String> {


    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    PageList<MessageComplaintInfoValidator> page(PageParams<MessageComplaintInfoValidator> pageParams);

    /**
     * 批量导入投诉
     * @param messageComplaintInfoValidator
     */
    void batchSave(MessageComplaintInfoValidator messageComplaintInfoValidator);
}
