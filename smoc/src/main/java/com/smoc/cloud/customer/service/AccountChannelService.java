package com.smoc.cloud.customer.service;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelBasicInfoQo;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelGroupInfoValidator;
import com.smoc.cloud.common.smoc.customer.qo.AccountChannelInfoQo;
import com.smoc.cloud.common.smoc.customer.validator.AccountChannelInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.AccountFinanceInfoValidator;
import com.smoc.cloud.common.utils.DES;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.PasswordUtils;
import com.smoc.cloud.customer.entity.AccountBasicInfo;
import com.smoc.cloud.customer.entity.AccountChannelInfo;
import com.smoc.cloud.customer.entity.AccountInterfaceInfo;
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

        //1:通道组
        List<AccountChannelInfoQo> list = null;
        if("1".equals(accountChannelInfoQo.getAccountChannelType())){
            list = accountChannelRepository.findAccountChannelGroupConfig(accountChannelInfoQo);

        }else{
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

        AccountChannelInfo data = accountChannelRepository.findByAccountIdAndCarrier(accountChannelInfoValidator.getAccountId(),accountChannelInfoValidator.getCarrier());

        AccountChannelInfo entity = new AccountChannelInfo();
        BeanUtils.copyProperties(accountChannelInfoValidator, entity);

        //add查重
        if (data != null &&"add".equals(op)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
        }
        //edit查重
        else if (data != null && "edit".equals(op)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
        }


        //op 不为 edit 或 add
        if (!("edit".equals(op) || "add".equals(op))) {
            return ResponseDataUtil.buildError();
        }

        //设置账号完成进度
        if("add".equals(op)){
            Optional<AccountBasicInfo> optional = businessAccountRepository.findById(entity.getAccountId());
            if(optional.isPresent()){
                AccountBasicInfo accountBasicInfo = optional.get();
                StringBuffer accountProcess = new StringBuffer(accountBasicInfo.getAccountProcess());
                accountProcess = accountProcess.replace(3, 4, "1");
                accountBasicInfo.setAccountProcess(accountProcess.toString());
                businessAccountRepository.save(accountBasicInfo);
            }
        }

        //记录日志
        log.info("[EC业务账号管理][业务账号通道配置][{}]数据:{}", op, JSON.toJSONString(entity));
        accountChannelRepository.saveAndFlush(entity);

        return ResponseDataUtil.buildSuccess();

    }

    public ResponseData findByAccountIdAndCarrier(String accountId, String carrier) {
        AccountChannelInfo entity = accountChannelRepository.findByAccountIdAndCarrier(accountId,carrier);

        AccountChannelInfoValidator accountChannelInfoValidator = new AccountChannelInfoValidator();
        if(!StringUtils.isEmpty(entity)){
            BeanUtils.copyProperties(entity, accountChannelInfoValidator);
            return ResponseDataUtil.buildSuccess(accountChannelInfoValidator);
        }
        return ResponseDataUtil.buildSuccess();
    }

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
}
