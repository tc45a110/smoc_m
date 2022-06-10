package com.smoc.cloud.configure.channel.repository;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelBasicInfoValidator;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelRepairRuleValidator;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelRepairValidator;
import com.smoc.cloud.configure.channel.entity.ConfigChannelRepairRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


/**
 * 通道失败补发操作类
 */
public interface ChannelRepairRepository extends CrudRepository<ConfigChannelRepairRule, String>, JpaRepository<ConfigChannelRepairRule, String> {

    PageList<ConfigChannelRepairValidator> page(PageParams<ConfigChannelRepairValidator> pageParams);

    /**
     * 根据运营商、业务类型、信息分类查询符合要求的备用通道
     * @param channelBasicInfoValidator
     * @return
     */
    List<ConfigChannelRepairValidator> findSpareChannel(ChannelBasicInfoValidator channelBasicInfoValidator);

    List<ConfigChannelRepairRuleValidator> findByChannelIdAndBusinessType(String channelId, String businessType);


    @Modifying
    @Query(value = "update config_channel_repair_rule set REPAIR_STATUS = 0 where ID =:id ",nativeQuery = true)
    void updateStatusById(@Param("id") String id);

    List<ConfigChannelRepairRule> findByChannelIdAndChannelRepairIdAndBusinessTypeAndRepairStatus(String channelId, String channelRepairId, String businessType, String repairStatus);

}
