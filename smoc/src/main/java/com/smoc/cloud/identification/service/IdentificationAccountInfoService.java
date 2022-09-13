package com.smoc.cloud.identification.service;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.redis.RedisConstant;
import com.smoc.cloud.common.redis.RedisModel;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.identification.validator.IdentificationAccountInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.finance.entity.FinanceAccount;
import com.smoc.cloud.finance.repository.FinanceAccountRepository;
import com.smoc.cloud.identification.entity.IdentificationAccountInfo;
import com.smoc.cloud.identification.repository.IdentificationAccountInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Service
public class IdentificationAccountInfoService {

    @Resource
    private FinanceAccountRepository financeAccountRepository;

    @Resource
    private IdentificationAccountInfoRepository identificationAccountInfoRepository;

    @Resource(name = "redisTemplate4")
    private RedisTemplate<String, Object> redisTemplate4;


    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    public PageList<IdentificationAccountInfoValidator> page(PageParams<IdentificationAccountInfoValidator> pageParams) {
        return identificationAccountInfoRepository.page(pageParams);
    }

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    public ResponseData findById(String id) {
        Optional<IdentificationAccountInfo> data = identificationAccountInfoRepository.findById(id);

        if (!data.isPresent()) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }

        IdentificationAccountInfo entity = data.get();
        IdentificationAccountInfoValidator identificationAccountInfoValidator = new IdentificationAccountInfoValidator();
        BeanUtils.copyProperties(entity, identificationAccountInfoValidator);

        //转换日期
        identificationAccountInfoValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(entity.getCreatedTime()));

        return ResponseDataUtil.buildSuccess(identificationAccountInfoValidator);
    }

    /**
     * 保存或修改
     *
     * @param identificationAccountInfoValidator
     * @param op                                 操作类型 为add、edit
     * @return
     */
    @Transactional
    public ResponseData save(IdentificationAccountInfoValidator identificationAccountInfoValidator, String op) {

        IdentificationAccountInfo entity = new IdentificationAccountInfo();
        BeanUtils.copyProperties(identificationAccountInfoValidator, entity);

        //转换日期格式
        entity.setCreatedTime(DateTimeUtils.getDateTimeFormat(identificationAccountInfoValidator.getCreatedTime()));

        //op 不为 edit 或 add
        if (!("edit".equals(op) || "add".equals(op))) {
            return ResponseDataUtil.buildError();
        }

        //记录日志
        log.info("[身份认证开户][{}]数据:{}", op, JSON.toJSONString(entity));
        identificationAccountInfoRepository.saveAndFlush(entity);

        //开通身份认证财务账户
        if ("add".equals(op)) {
            FinanceAccount financeAccount = new FinanceAccount();
            financeAccount.setAccountId(entity.getIdentificationAccount());
            financeAccount.setAccountType("IDENTIFICATION_ACCOUNT");
            financeAccount.setAccountTotalSum(new BigDecimal("0.00000"));
            financeAccount.setAccountUsableSum(new BigDecimal("0.00000"));
            financeAccount.setAccountFrozenSum(new BigDecimal("0.00000"));
            financeAccount.setAccountConsumeSum(new BigDecimal("0.00000"));
            financeAccount.setAccountRechargeSum(new BigDecimal("0.00000"));
            financeAccount.setAccountRefundSum(new BigDecimal("0.00000"));
            financeAccount.setAccountCreditSum(entity.getGrantingCredit());
            financeAccount.setEnterpriseId(entity.getEnterpriseId());
            financeAccount.setAccountStatus("1");
            financeAccount.setCreatedTime(DateTimeUtils.getNowDateTime());
            financeAccount.setCreatedBy(entity.getCreatedBy());
            financeAccountRepository.saveAndFlush(financeAccount);
        }

        //修改授信额度
        if ("edit".equals(op)) {
            financeAccountRepository.updateAccountCreditSumByAccountId(entity.getIdentificationAccount(), entity.getGrantingCredit());
        }

        //如果状态不为004 则放到redis中
        if(!"004".equals(entity.getAccountStatus())) {

            //放到redis中对象
            RedisModel redisModel = new RedisModel();
            redisModel.setMd5HmacKey(identificationAccountInfoValidator.getMd5HmacKey());
            redisModel.setAesKey(identificationAccountInfoValidator.getAesKey());
            redisModel.setAesIv(identificationAccountInfoValidator.getAesIv());
            redisModel.setSubmitRate(entity.getSubmitLimiter());
            redisModel.setIps(identificationAccountInfoValidator.getIdentifyIp());
            //把数据放到redis里
            redisTemplate4.opsForValue().set(RedisConstant.HTTP_SERVER_KEY + identificationAccountInfoValidator.getIdentificationAccount(), redisModel);
        }else{
            //注销
            redisTemplate4.delete(RedisConstant.HTTP_SERVER_KEY + identificationAccountInfoValidator.getIdentificationAccount());
        }
        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 注销账号
     *
     * @param id
     * @return
     */
    @Transactional
    public ResponseData logoutAccount(String id) {
        //把数据放到redis里
        redisTemplate4.delete(RedisConstant.HTTP_SERVER_KEY + id);
        identificationAccountInfoRepository.logoutAccount(id, "004");
        return ResponseDataUtil.buildSuccess();
    }

}
