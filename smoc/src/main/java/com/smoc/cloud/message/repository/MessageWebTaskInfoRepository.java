package com.smoc.cloud.message.repository;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.template.MessageWebTaskInfoValidator;
import com.smoc.cloud.message.entity.MessageWebTaskInfo;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * web任务单
 */
public interface MessageWebTaskInfoRepository extends JpaRepository<MessageWebTaskInfo, String> {

    /**
     * 分页查询
     * @param pageParams
     * @return
     */
    PageList<MessageWebTaskInfoValidator> page(PageParams<MessageWebTaskInfoValidator> pageParams);
}