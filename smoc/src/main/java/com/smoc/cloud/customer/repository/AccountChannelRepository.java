package com.smoc.cloud.customer.repository;


import com.smoc.cloud.common.smoc.configuate.qo.ChannelBasicInfoQo;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelGroupInfoValidator;
import com.smoc.cloud.common.smoc.customer.qo.AccountChannelInfoQo;
import com.smoc.cloud.common.smoc.customer.validator.AccountChannelInfoValidator;
import com.smoc.cloud.configure.channel.group.entity.ConfigChannelGroup;
import com.smoc.cloud.customer.entity.AccountChannelInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


/**
 * 账号通道配置操作类
 */
public interface AccountChannelRepository extends CrudRepository<AccountChannelInfo, String>, JpaRepository<AccountChannelInfo, String> {

    /**
     * 查询配置的业务账号通道
     * @param accountChannelInfoQo
     * @return
     */
    List<AccountChannelInfoQo> findAccountChannelConfig(AccountChannelInfoQo accountChannelInfoQo);

    /**
     * 检索通道列表
     * @param channelBasicInfoQo
     * @return
     */
    List<ChannelBasicInfoQo> findChannelList(ChannelBasicInfoQo channelBasicInfoQo);

    List<AccountChannelInfo> findByAccountIdAndCarrier(String accountId, String carrier);

    /**
     * 查询配置的业务账号通道组
     * @param accountChannelInfoQo
     * @return
     */
    List<AccountChannelInfoQo> findAccountChannelGroupConfig(AccountChannelInfoQo accountChannelInfoQo);

    /**
     * 检索通道组列表
     * @param channelGroupInfoValidator
     * @return
     */
    List<ChannelGroupInfoValidator> findChannelGroupList(ChannelGroupInfoValidator channelGroupInfoValidator);

    /**
     * 根据账号删除已配置通道
     * @param accountId
     */
    void deleteByAccountId(String accountId);

    /**
     * 批量添加业务账号通道组下面的通道
     * @param entity
     * @param list
     */
    void batchSave(AccountChannelInfo entity, List<ConfigChannelGroup> list);

    /**
     * 查询已配置通道组数据
     * @param accountId
     * @param carrier
     * @param channelGroupId
     * @return
     */
    List<AccountChannelInfo> findByAccountIdAndCarrierAndChannelGroupId(String accountId, String carrier, String channelGroupId);

    /**
     * 移除已配置通道组
     * @param accountId
     * @param carrier
     * @param channelGroupId
     */
    void deleteByAccountIdAndCarrierAndChannelGroupId(String accountId, String carrier, String channelGroupId);

    /**
     * 修改账号的运营商，需要删除已配置的通道
     * @param accountId
     * @param carrier
     */
    void deleteConfigChannelByCarrier(String accountId, String carrier);

    List<AccountChannelInfoQo> accountChannelByAccountIdAndCarrier(String accountId, String carrier, String accountChannelType);

    /**
     * 业务账号通道明细
     * @param accountChannelInfoValidator
     * @return
     */
    List<AccountChannelInfoValidator> channelDetail(AccountChannelInfoValidator accountChannelInfoValidator);

    /**
     * 通过channelId 查询 该通道的业务账号引用情况
     * @param channelId
     * @return
     */
     List<AccountChannelInfoValidator> channelDetail(String channelId);

    /**
     * 分组查询业务账号
     * @param channelGroupId
     * @param carrier
     * @return
     */
    List<AccountChannelInfoQo> findAccountChannelGroupByChannelGroupIdAndCarrierAndAccountId(String channelGroupId, String carrier);

    /**
     * 在通道组管理里,在给通道组里添加通道时，要把通道添加到业务账号通道组里
     * @param list
     * @param entity
     */
    void batchSaveAccountChannel(List<AccountChannelInfoQo> list, ConfigChannelGroup entity);

    /**
     * 在通道组管理里移除通道的时候，需要把通道从业务账号通道组里也移除
     * @param channelGroupId
     * @param channelId
     */
    void deleteByChannelGroupIdAndChannelId(String channelGroupId, String channelId);

    /**
     * 在通道组管理里,如果修改了通道权重，需要把业务账号通道组里的通道权重修改
     * @param channelGroupId
     * @param channelId
     * @param channelWeight
     */
    @Modifying
    @Query(value = "update account_channel_info set CHANNEL_WEIGHT = :channelWeight where CHANNEL_GROUP_ID = :channelGroupId and CHANNEL_ID = :channelId ",nativeQuery = true)
    void updateAccountChannelWeight(@Param("channelGroupId") String channelGroupId, @Param("channelId") String channelId, @Param("channelWeight") int channelWeight);

    @Modifying
    @Query(value = "delete from account_channel_info where ACCOUNT_ID = :accountId and CARRIER = :carrier ",nativeQuery = true)
    void deleteByAccountIdAndCarrier(@Param("accountId")String accountId, @Param("carrier")String carrier);

    List<AccountChannelInfo> findByAccountId(String accountId);

    void batchChannelCopy(AccountChannelInfoValidator accountChannelInfoValidator, List<AccountChannelInfo> channelList);
}
