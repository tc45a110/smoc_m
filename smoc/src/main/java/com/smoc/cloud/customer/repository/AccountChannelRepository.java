package com.smoc.cloud.customer.repository;


import com.smoc.cloud.common.smoc.configuate.qo.ChannelBasicInfoQo;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelGroupInfoValidator;
import com.smoc.cloud.common.smoc.customer.qo.AccountChannelInfoQo;
import com.smoc.cloud.common.smoc.customer.validator.AccountChannelInfoValidator;
import com.smoc.cloud.customer.entity.AccountChannelInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


/**
 * 账号通道配置操作类
 */
public interface AccountChannelRepository extends CrudRepository<AccountChannelInfo, String>, JpaRepository<AccountChannelInfo, String> {


    List<AccountChannelInfoQo> findAccountChannelConfig(AccountChannelInfoQo accountChannelInfoQo);

    List<ChannelBasicInfoQo> findChannelList(ChannelBasicInfoQo channelBasicInfoQo);

    AccountChannelInfo findByAccountIdAndCarrier(String accountId, String carrier);

    List<AccountChannelInfoQo> findAccountChannelGroupConfig(AccountChannelInfoQo accountChannelInfoQo);

    List<ChannelGroupInfoValidator> findChannelGroupList(ChannelGroupInfoValidator channelGroupInfoValidator);
}
