package com.smoc.cloud.customer.service;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelBasicInfoQo;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelGroupInfoValidator;
import com.smoc.cloud.common.smoc.customer.qo.AccountChannelInfoQo;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.AccountChannelInfoValidator;
import com.smoc.cloud.customer.remote.AccountChannelFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * 账号通道配置管理服务
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AccountChannelService {

    @Autowired
    private AccountChannelFeignClient accountChannelFeignClient;

    /**
     * 查询配置的业务账号通道
     * @param accountChannelInfoQo
     * @return
     */
    public ResponseData<Map<String, AccountChannelInfoQo>> findAccountChannelConfig(AccountChannelInfoQo accountChannelInfoQo) {
        try {
            ResponseData<Map<String, AccountChannelInfoQo>> list = this.accountChannelFeignClient.findAccountChannelConfig(accountChannelInfoQo);
            return list;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 检索通道列表
     * @param channelBasicInfoQo
     * @return
     */
    public ResponseData<List<ChannelBasicInfoQo>> findChannelList(ChannelBasicInfoQo channelBasicInfoQo) {
        try {
            ResponseData<List<ChannelBasicInfoQo>> list = this.accountChannelFeignClient.findChannelList(channelBasicInfoQo);
            return list;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 保存账号通道
     * @param accountChannelInfoValidator
     * @param op
     * @return
     */
    public ResponseData save(AccountChannelInfoValidator accountChannelInfoValidator, String op) {
        try {
            ResponseData data = this.accountChannelFeignClient.save(accountChannelInfoValidator,op);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 查询账号下运营商是否配置过通道
     * @param accountId
     * @param carrier
     * @return
     */
    public ResponseData<List<AccountChannelInfoValidator>> findByAccountIdAndCarrier(String accountId, String carrier) {
        try {
            ResponseData<List<AccountChannelInfoValidator>> data = this.accountChannelFeignClient.findByAccountIdAndCarrier(accountId,carrier);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    public ResponseData<AccountChannelInfoValidator> findById(String id) {
        try {
            ResponseData<AccountChannelInfoValidator> data = this.accountChannelFeignClient.findById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据id删除信息
     * @param id
     * @return
     */
    public ResponseData deleteById(String id) {
        try {
            ResponseData data = this.accountChannelFeignClient.deleteById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 检索通道组
     * @param channelGroupInfoValidator
     * @return
     */
    public ResponseData<List<ChannelGroupInfoValidator>> findChannelGroupList(ChannelGroupInfoValidator channelGroupInfoValidator) {
        try {
            ResponseData<List<ChannelGroupInfoValidator>> list = this.accountChannelFeignClient.findChannelGroupList(channelGroupInfoValidator);
            return list;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 查询已配置通道组数据
     * @param accountId
     * @param carrier
     * @param channelGroupId
     * @return
     */
    public ResponseData<List<AccountChannelInfoValidator>> findByAccountIdAndCarrierAndChannelGroupId(String accountId, String carrier, String channelGroupId) {
        try {
            ResponseData<List<AccountChannelInfoValidator>> list = this.accountChannelFeignClient.findByAccountIdAndCarrierAndChannelGroupId(accountId,carrier,channelGroupId);
            return list;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 移除已配置通道组
     * @param accountId
     * @param carrier
     * @param channelGroupId
     * @return
     */
    public ResponseData deleteChannelGroup(String accountId, String carrier, String channelGroupId) {
        try {
            ResponseData data = this.accountChannelFeignClient.deleteChannelGroup(accountId,carrier,channelGroupId);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 业务账号通道明细
     * @param accountChannelInfoValidator
     * @return
     */
    public ResponseData<List<AccountChannelInfoValidator>> channelDetail(AccountChannelInfoValidator accountChannelInfoValidator) {
        try {
            ResponseData<List<AccountChannelInfoValidator>> list = this.accountChannelFeignClient.channelDetail(accountChannelInfoValidator);
            return list;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 通过channelId 查询 该通道的业务账号引用情况
     * @param channelId
     * @return
     */
    public ResponseData<List<AccountChannelInfoValidator>> channelDetail(String channelId) {
        try {
            ResponseData<List<AccountChannelInfoValidator>> list = this.accountChannelFeignClient.channelDetail(channelId);
            return list;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
