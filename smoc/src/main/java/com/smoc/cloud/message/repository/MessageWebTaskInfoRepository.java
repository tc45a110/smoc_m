package com.smoc.cloud.message.repository;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.template.MessageWebTaskInfoValidator;
import com.smoc.cloud.message.entity.MessageWebTaskInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;

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

    /**
     * 统计发送数量
     *
     * @param qo
     * @return
     */
    Map<String, Object> countSum(MessageWebTaskInfoValidator qo);
}