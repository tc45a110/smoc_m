package com.smoc.cloud.customer.service;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelBasicInfoQo;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelGroupInfoValidator;
import com.smoc.cloud.common.smoc.customer.qo.AccountChannelInfoQo;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.AccountChannelInfoValidator;
import com.smoc.cloud.configure.channel.group.entity.ConfigChannelGroup;
import com.smoc.cloud.configure.channel.group.repository.ConfigChannelGroupRepository;
import com.smoc.cloud.customer.entity.AccountBasicInfo;
import com.smoc.cloud.customer.entity.AccountChannelInfo;
import com.smoc.cloud.customer.repository.AccountChannelRepository;
import com.smoc.cloud.customer.repository.BusinessAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * 业务账号通道配置管理
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AccountChannelService {

    @Resource
    private AccountChannelRepository accountChannelRepository;

    @Resource
    private BusinessAccountRepository businessAccountRepository;

    @Resource
    private ConfigChannelGroupRepository configChannelGroupRepository;


    /**
     * 查询配置的业务账号通道
     *
     * @param accountChannelInfoQo
     * @return
     */
    public ResponseData<Map<String, AccountChannelInfoQo>> findAccountChannelConfig(AccountChannelInfoQo accountChannelInfoQo) {

        Map<String, AccountChannelInfoQo> map = new HashMap<>();
        String[] carrier = accountChannelInfoQo.getCarrier().split(",");
        for (int i = 0; i < carrier.length; i++) {
            map.put(carrier[i], null);
        }

        List<AccountChannelInfoQo> list = null;
        if("ACCOUNT_CHANNEL_GROUP".equals(accountChannelInfoQo.getAccountChannelType())){
            list = accountChannelRepository.findAccountChannelGroupConfig(accountChannelInfoQo);

        }else if(("ACCOUNT_CHANNEL".equals(accountChannelInfoQo.getAccountChannelType()))){
            list = accountChannelRepository.findAccountChannelConfig(accountChannelInfoQo);
        }

        //循环已配置的通道
        if (!StringUtils.isEmpty(list)) {
            for (AccountChannelInfoQo info : list) {
                map.put(info.getCarrier(), info);
            }
        }

        return ResponseDataUtil.buildSuccess(map);
    }

    /**
     * 检索通道列表
     * @param channelBasicInfoQo
     * @return
     */
    public ResponseData<List<ChannelBasicInfoQo>> findChannelList(ChannelBasicInfoQo channelBasicInfoQo) {
        List<ChannelBasicInfoQo> list = accountChannelRepository.findChannelList(channelBasicInfoQo);
        return ResponseDataUtil.buildSuccess(list);
    }

    @Transactional
    public ResponseData save(AccountChannelInfoValidator accountChannelInfoValidator, String op) {

        List<AccountChannelInfo> data = accountChannelRepository.findByAccountIdAndCarrier(accountChannelInfoValidator.getAccountId(),accountChannelInfoValidator.getCarrier());

        AccountChannelInfo entity = new AccountChannelInfo();
        BeanUtils.copyProperties(accountChannelInfoValidator, entity);

        //add查重
        if (!StringUtils.isEmpty(data) && data.size()>0 &&"add".equals(op)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
        }

        //op 不为 edit 或 add
        if (!("edit".equals(op) || "add".equals(op))) {
            return ResponseDataUtil.buildError();
        }

        //记录日志
        log.info("[EC业务账号管理][业务账号通道配置][{}]数据:{}", op, JSON.toJSONString(entity));

        //保存通道
        if(!StringUtils.isEmpty(entity.getChannelId())){
            accountChannelRepository.saveAndFlush(entity);
        }

        //保存通道组
        if(!StringUtils.isEmpty(entity.getChannelGroupId())){
            List<ConfigChannelGroup> list = configChannelGroupRepository.findByChannelGroupId(entity.getChannelGroupId());
            accountChannelRepository.batchSave(entity,list);
        }

        //设置账号完成进度
        if("add".equals(op)){
            AccountBasicInfo accountBasicInfo = businessAccountRepository.findById(entity.getAccountId()).get();
            String carrier = accountBasicInfo.getCarrier();
            if("INTERNATIONAL".equals(accountBasicInfo.getCarrier())){
                carrier = accountBasicInfo.getCountryCode();
            }
            List<AccountChannelInfoQo> list = accountChannelRepository.accountChannelByAccountIdAndCarrier(entity.getAccountId(),carrier,accountBasicInfo.getAccountChannelType());
            String[] carrierLength = carrier.split(",");
            if(!StringUtils.isEmpty(list) && list.size()==carrierLength.length){
                StringBuffer accountProcess = new StringBuffer(accountBasicInfo.getAccountProcess());
                accountProcess = accountProcess.replace(3, 4, "1");
                accountBasicInfo.setAccountProcess(accountProcess.toString());
                businessAccountRepository.save(accountBasicInfo);
            }
        }

        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 查询配置通道
     * @param accountId
     * @param carrier
     * @return
     */
    public ResponseData findByAccountIdAndCarrier(String accountId, String carrier) {
        List<AccountChannelInfo> list = accountChannelRepository.findByAccountIdAndCarrier(accountId,carrier);

        return ResponseDataUtil.buildSuccess(list);
    }

    /**
     * 根据ID 查询数据
     * @param id
     * @return
     */
    public ResponseData findById(String id) {
        Optional<AccountChannelInfo> data = accountChannelRepository.findById(id);

        if (!data.isPresent()) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }

        AccountChannelInfoValidator accountChannelInfoValidator = new AccountChannelInfoValidator();
        BeanUtils.copyProperties(data.get(), accountChannelInfoValidator);

        return ResponseDataUtil.buildSuccess(accountChannelInfoValidator);
    }

    /**
     * 根据ID 删除数据
     *
     * @param id
     * @return
     */
    @Transactional
    public ResponseData<AccountChannelInfo> deleteById(String id) {

        AccountChannelInfo data = accountChannelRepository.findById(id).get();
        //记录日志
        log.info("[EC业务账号管理][移除账号已配置通道][delete]数据:{}",JSON.toJSONString(data));
        accountChannelRepository.deleteById(id);

        //设置进度
        Optional<AccountBasicInfo> optional = businessAccountRepository.findById(data.getAccountId());
        if(optional.isPresent()){
            AccountBasicInfo accountBasicInfo = optional.get();
            StringBuffer accountProcess = new StringBuffer(accountBasicInfo.getAccountProcess());
            accountProcess = accountProcess.replace(3, 4, "0");
            accountBasicInfo.setAccountProcess(accountProcess.toString());
            businessAccountRepository.save(accountBasicInfo);
        }

        return ResponseDataUtil.buildSuccess();
    }

    public ResponseData<List<ChannelGroupInfoValidator>> findChannelGroupList(ChannelGroupInfoValidator channelGroupInfoValidator) {
        List<ChannelGroupInfoValidator> list = accountChannelRepository.findChannelGroupList(channelGroupInfoValidator);
        return ResponseDataUtil.buildSuccess(list);
    }

    /**
     * 查询已配置通道组数据
     * @param accountId
     * @param carrier
     * @param channelGroupId
     * @return
     */
    public ResponseData findByAccountIdAndCarrierAndChannelGroupId(String accountId, String carrier, String channelGroupId) {
        List<AccountChannelInfo> list = accountChannelRepository.findByAccountIdAndCarrierAndChannelGroupId(accountId,carrier,channelGroupId);

        return ResponseDataUtil.buildSuccess(list);
    }

    /**
     * 移除已配置通道组
     * @param accountId
     * @param carrier
     * @param channelGroupId
     * @return
     */
    @Transactional
    public ResponseData deleteChannelGroup(String accountId, String carrier, String channelGroupId) {
        List<AccountChannelInfo> list = accountChannelRepository.findByAccountIdAndCarrierAndChannelGroupId(accountId,carrier,channelGroupId);
        //记录日志
        log.info("[EC业务账号管理][移除账号已配置通道][delete]数据:{}",JSON.toJSONString(list));
        accountChannelRepository.deleteByAccountIdAndCarrierAndChannelGroupId(accountId,carrier,channelGroupId);

        //设置进度
        Optional<AccountBasicInfo> optional = businessAccountRepository.findById(accountId);
        if(optional.isPresent()){
            AccountBasicInfo accountBasicInfo = optional.get();
            StringBuffer accountProcess = new StringBuffer(accountBasicInfo.getAccountProcess());
            accountProcess = accountProcess.replace(3, 4, "0");
            accountBasicInfo.setAccountProcess(accountProcess.toString());
            businessAccountRepository.save(accountBasicInfo);
        }

        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 业务账号通道明细
     * @param accountChannelInfoValidator
     * @return
     */
    public ResponseData<List<AccountChannelInfoValidator>> channelDetail(AccountChannelInfoValidator accountChannelInfoValidator) {
        List<AccountChannelInfoValidator> list = accountChannelRepository.channelDetail(accountChannelInfoValidator);

        return ResponseDataUtil.buildSuccess(list);
    }

    /**
     * 通过channelId 查询 该通道的业务账号引用情况
     * @param channelId
     * @return
     */
    public ResponseData<List<AccountChannelInfoValidator>> channelDetail(String channelId){

        List<AccountChannelInfoValidator> list = accountChannelRepository.channelDetail(channelId);
        return ResponseDataUtil.buildSuccess(list);

    }
}
