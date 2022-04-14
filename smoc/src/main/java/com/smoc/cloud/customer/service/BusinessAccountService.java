package com.smoc.cloud.customer.service;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.qo.AccountChannelInfoQo;
import com.smoc.cloud.common.smoc.customer.qo.AccountInfoQo;
import com.smoc.cloud.common.smoc.customer.qo.AccountStatisticComplaintData;
import com.smoc.cloud.common.smoc.customer.qo.AccountStatisticSendData;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.common.smoc.message.MessageAccountValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.customer.entity.AccountBasicInfo;
import com.smoc.cloud.customer.entity.AccountChannelInfo;
import com.smoc.cloud.customer.repository.AccountChannelRepository;
import com.smoc.cloud.customer.repository.AccountFinanceRepository;
import com.smoc.cloud.customer.repository.BusinessAccountRepository;
import com.smoc.cloud.finance.entity.FinanceAccount;
import com.smoc.cloud.finance.repository.FinanceAccountRepository;
import com.smoc.cloud.utils.RandomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * 业务账号管理
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BusinessAccountService {

    @Resource
    private BusinessAccountRepository businessAccountRepository;

    @Resource
    private AccountFinanceRepository accountFinanceRepository;

    @Resource
    private FinanceAccountRepository financeAccountRepository;

    @Resource
    private AccountChannelRepository accountChannelRepository;

    @Autowired
    private RandomService randomService;

    @Resource
    private RedisTemplate<String, String> stringRedisTemplate;


    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    public PageList<AccountBasicInfoValidator> page(PageParams<AccountBasicInfoValidator> pageParams) {
        return businessAccountRepository.page(pageParams);
    }

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    public ResponseData findById(String id) {
        Optional<AccountBasicInfo> data = businessAccountRepository.findById(id);

        if (!data.isPresent()) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }

        AccountBasicInfo entity = data.get();
        AccountBasicInfoValidator accountBaseInfoValidator = new AccountBasicInfoValidator();
        BeanUtils.copyProperties(entity, accountBaseInfoValidator);

        //转换日期
        accountBaseInfoValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(entity.getCreatedTime()));

        return ResponseDataUtil.buildSuccess(accountBaseInfoValidator);
    }

    /**
     * 保存或修改
     *
     * @param accountBasicInfoValidator
     * @param op  操作类型 为add、edit
     * @return
     */
    @Transactional
    public ResponseData<AccountBasicInfo> save(AccountBasicInfoValidator accountBasicInfoValidator, String op) {

        Iterable<AccountBasicInfo> data = businessAccountRepository.findByAccountId(accountBasicInfoValidator.getAccountId());

        AccountBasicInfo entity = new AccountBasicInfo();
        BeanUtils.copyProperties(accountBasicInfoValidator, entity);

        String accountType = "";
        String accountChannelType = "";
        String carrier = "";
        String countryCode = "";

        //add查重
        if (data != null && data.iterator().hasNext() && "add".equals(op)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
        }
        //edit查重
        else if (data != null && data.iterator().hasNext() && "edit".equals(op)) {
            boolean status = false;
            Iterator iter = data.iterator();
            while (iter.hasNext()) {
                AccountBasicInfo info = (AccountBasicInfo) iter.next();
                accountType = info.getBusinessType();
                accountChannelType = info.getAccountChannelType();
                carrier = info.getCarrier();
                countryCode = info.getCountryCode();
                if (!entity.getAccountId().equals(info.getAccountId())) {
                    status = true;
                    break;
                }
            }
            if (status) {
                return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
            }
        }

        //转换日期格式
        entity.setCreatedTime(DateTimeUtils.getDateTimeFormat(accountBasicInfoValidator.getCreatedTime()));

        //op 不为 edit 或 add
        if (!("edit".equals(op) || "add".equals(op))) {
            return ResponseDataUtil.buildError();
        }

        //添加账户信息
        saveFinanceAccount(entity, op, accountType);

        if ("edit".equals(op)) {
            //根据运营商先删除库里多余的运营商的价格(比如：添加的时候选择了3个运营商，修改的时候选择了2个运营商，那么就得把多余的1个运营商删除)
            if("INTL".equals(accountBasicInfoValidator.getCarrier())){
                accountFinanceRepository.deleteByAccountIdAndCarrier(accountBasicInfoValidator.getAccountId(), accountBasicInfoValidator.getCountryCode());
            }else{
                accountFinanceRepository.deleteByAccountIdAndCarrier(accountBasicInfoValidator.getAccountId(), accountBasicInfoValidator.getCarrier());
            }


            //如果修改了通道方式，并且配置了通道，需要删除已经配置得通道
            if(!accountBasicInfoValidator.getAccountChannelType().equals(accountChannelType)){
                deleteByAccountId(entity);
            }

            //如果修改了运营商，并且配置了通道，需要删除通道
            if("INTL".equals(accountBasicInfoValidator.getCarrier())){
                if(!accountBasicInfoValidator.getCountryCode().equals(countryCode)){
                    deleteConfigChannelByCarrier(entity);
                }
            }else{
                if(!accountBasicInfoValidator.getCarrier().equals(carrier)){
                    deleteConfigChannelByCarrier(entity);
                }
            }
        }

        //记录日志
        log.info("[EC业务账号管理][业务账号基本信息][{}]数据:{}", op, JSON.toJSONString(entity));
        businessAccountRepository.saveAndFlush(entity);

        //把账号id放到redis里
        String accountId = entity.getAccountId();
        accountId = accountId.substring(accountId.length()-3);
        stringRedisTemplate.opsForValue().set(RandomService.PREFIX +":"+accountId ,accountId);

        return ResponseDataUtil.buildSuccess();
    }


    //添加账户信息
    private void saveFinanceAccount(AccountBasicInfo accountBasicInfo, String op, String accountType) {

        if ("add".equals(op)) {
            FinanceAccount financeAccount = new FinanceAccount();
            financeAccount.setAccountId(accountBasicInfo.getAccountId());
            financeAccount.setAccountType(accountBasicInfo.getBusinessType());
            financeAccount.setAccountTotalSum(new BigDecimal("0.00000"));
            financeAccount.setAccountUsableSum(new BigDecimal("0.00000"));
            financeAccount.setAccountFrozenSum(new BigDecimal("0.00000"));
            financeAccount.setAccountConsumeSum(new BigDecimal("0.00000"));
            financeAccount.setAccountRechargeSum(new BigDecimal("0.00000"));
            financeAccount.setAccountCreditSum(new BigDecimal("0.00000"));
            financeAccount.setAccountStatus("1");
            financeAccount.setIsShare("0");
            financeAccount.setCreatedTime(DateTimeUtils.getNowDateTime());
            financeAccount.setCreatedBy(accountBasicInfo.getCreatedBy());
            financeAccountRepository.saveAndFlush(financeAccount);
        }

        //如果是修改了业务类型，需要修改账户的业务类型
        if ("edit".equals(op) && !accountType.equals(accountBasicInfo.getBusinessType())) {
            financeAccountRepository.updateAccountTypeByAccountId(accountBasicInfo.getBusinessType(), accountBasicInfo.getAccountId());
        }
    }

    //修改运营商，并且配置了通道，需要删除通道
    private void deleteConfigChannelByCarrier(AccountBasicInfo entity) {
        String carrier = entity.getCarrier();
        if("INTL".equals(entity.getCarrier())){
            carrier = entity.getCountryCode();
        }
        List<AccountChannelInfoQo> list = accountChannelRepository.accountChannelByAccountIdAndCarrier(entity.getAccountId(),carrier,entity.getAccountChannelType());
        String[] carrierLength = carrier.split(",");
        if(StringUtils.isEmpty(list) || list.size()<carrierLength.length){
            //设置进度
            accountProcess(entity,"0");
        }else{
            accountProcess(entity,"1");
        }
        accountChannelRepository.deleteConfigChannelByCarrier(entity.getAccountId(),carrier);
    }

    //修改了通道方式，删除已经配置得通道
    private void deleteByAccountId(AccountBasicInfo entity) {
        accountChannelRepository.deleteByAccountId(entity.getAccountId());
        //设置进度
        accountProcess(entity,"0");
    }

    //设置进度
    private void accountProcess(AccountBasicInfo entity,String process) {
        Optional<AccountBasicInfo> optional = businessAccountRepository.findById(entity.getAccountId());
        if(optional.isPresent()){
            AccountBasicInfo accountBasicInfo = optional.get();
            StringBuffer accountProcess = new StringBuffer(accountBasicInfo.getAccountProcess());
            accountProcess = accountProcess.replace(3, 4, process);
            entity.setAccountProcess(accountProcess.toString());
        }
    }

    /**
     * 注销、启用业务账号
     * @param id
     * @param status
     * @return
     */
    @Transactional
    public ResponseData forbiddenAccountById(String id, String status) {
        String op = status;
        AccountBasicInfo entity = businessAccountRepository.findById(id).get();

        //账号状态转换
        if ("0".equals(status) ) {
            status = "1";
            if(Integer.parseInt(entity.getAccountProcess())<=11100){
                status = "2";
            }
        } else {
            status = "0";
        }

        businessAccountRepository.updateAccountStatusById(id,status);

        //记录日志
        log.info("[EC业务账号管理][{}]数据:{}", ("1".equals(op) || "2".equals(op))  ? "注销业务账号":"启用业务账号" ,  JSON.toJSONString(entity));

        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 查询企业所有的业务账号
     * @param enterpriseId
     * @return
     */
    public ResponseData<List<AccountBasicInfoValidator>> findBusinessAccountByEnterpriseId(String enterpriseId) {
        List<AccountBasicInfoValidator> list = businessAccountRepository.findBusinessAccountByEnterpriseId(enterpriseId);
        return ResponseDataUtil.buildSuccess(list);
    }

    /**
     * 生成业务账号
     * @param enterpriseFlag
     * @return
     */
    public ResponseData<String> createAccountId(String enterpriseFlag) {

        String accountId = randomService.getBusinessAccount(enterpriseFlag);

        return ResponseDataUtil.buildSuccess(accountId);
    }

    /**
     * 查询企业所有的业务账号
     * @param accountBasicInfoValidator
     * @return
     */
    public ResponseData<List<AccountBasicInfoValidator>> findBusinessAccount(AccountBasicInfoValidator accountBasicInfoValidator) {
        List<AccountBasicInfoValidator> list = businessAccountRepository.findBusinessAccount(accountBasicInfoValidator);
        return ResponseDataUtil.buildSuccess(list);
    }

    /**
     * 查询企业下的账户和余额
     * @param messageAccountValidator
     * @return
     */
    public ResponseData<List<MessageAccountValidator>> messageAccountList(MessageAccountValidator messageAccountValidator) {
        List<MessageAccountValidator> list = businessAccountRepository.messageAccountList(messageAccountValidator);
        return ResponseDataUtil.buildSuccess(list);
    }

    /**
     * 查询自服务平台发送账号列表
     * @param params
     * @return
     */
    public ResponseData<PageList<MessageAccountValidator>> messageAccountInfoList(PageParams<MessageAccountValidator> params) {
        PageList<MessageAccountValidator> list = businessAccountRepository.messageAccountInfoList(params);
        return ResponseDataUtil.buildSuccess(list);
    }

    /**
     * 账号按维度统计发送量
     * @param statisticSendData
     * @return
     */
    public ResponseData<List<AccountStatisticSendData>> statisticAccountSendNumber(AccountStatisticSendData statisticSendData) {
        List<AccountStatisticSendData> list = businessAccountRepository.statisticAccountSendNumber(statisticSendData);
        return ResponseDataUtil.buildSuccess(list);
    }

    /**
     * 账号投诉率统计
     * @param statisticComplaintData
     * @return
     */
    public ResponseData<List<AccountStatisticComplaintData>> statisticComplaintMonth(AccountStatisticComplaintData statisticComplaintData) {
        List<AccountStatisticComplaintData> list = businessAccountRepository.statisticComplaintMonth(statisticComplaintData);
        return ResponseDataUtil.buildSuccess(list);
    }

    /**
     * 业务账号综合查询
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<AccountInfoQo>> accountAll(PageParams<AccountInfoQo> pageParams) {
        PageList<AccountInfoQo> list = businessAccountRepository.accountAll(pageParams);
        return ResponseDataUtil.buildSuccess(list);
    }
}
