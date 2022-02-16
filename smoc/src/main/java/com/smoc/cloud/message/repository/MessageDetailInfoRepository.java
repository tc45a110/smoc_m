package com.smoc.cloud.message.repository;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.message.MessageDetailInfoValidator;
import com.smoc.cloud.message.entity.MessageDetailInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 短信明细
 */
public interface MessageDetailInfoRepository extends JpaRepository<MessageDetailInfo, String> {

    /**
     * 分页查询
     * @param pageParams
     * @return
     */
    PageList<MessageDetailInfoValidator> page(PageParams<MessageDetailInfoValidator> pageParams);
}