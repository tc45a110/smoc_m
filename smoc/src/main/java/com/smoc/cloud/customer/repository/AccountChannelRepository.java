package com.smoc.cloud.customer.repository;


import com.smoc.cloud.common.smoc.configuate.qo.ChannelBasicInfoQo;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelGroupInfoValidator;
import com.smoc.cloud.common.smoc.customer.qo.AccountChannelInfoQo;
import com.smoc.cloud.common.smoc.customer.validator.AccountChannelInfoValidator;
import com.smoc.cloud.configure.channel.group.entity.ConfigChannelGroup;
import com.smoc.cloud.customer.entity.AccountChannelInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

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
}
