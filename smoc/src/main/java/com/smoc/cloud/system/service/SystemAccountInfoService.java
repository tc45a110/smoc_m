package com.smoc.cloud.system.service;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.redis.RedisConstant;
import com.smoc.cloud.common.redis.RedisModel;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.system.SystemAccountInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.finance.entity.FinanceAccount;
import com.smoc.cloud.finance.repository.FinanceAccountRepository;
import com.smoc.cloud.system.entity.SystemAccountInfo;
import com.smoc.cloud.system.repository.SystemAccountInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Service
public class SystemAccountInfoService {

    @Resource(name = "redisTemplate2")
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private FinanceAccountRepository financeAccountRepository;

    @Resource
    private SystemAccountInfoRepository systemAccountInfoRepository;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    public PageList<SystemAccountInfoValidator> page(PageParams<SystemAccountInfoValidator> pageParams) {
        return systemAccountInfoRepository.page(pageParams);
    }

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    public ResponseData<SystemAccountInfoValidator> findById(String id) {
        Optional<SystemAccountInfo> data = systemAccountInfoRepository.findById(id);

        if (!data.isPresent()) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }

        SystemAccountInfo entity = data.get();
        SystemAccountInfoValidator systemAccountInfoValidator = new SystemAccountInfoValidator();
        BeanUtils.copyProperties(entity, systemAccountInfoValidator);

        //转换日期
        systemAccountInfoValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(entity.getCreatedTime()));
        if (!StringUtils.isEmpty(systemAccountInfoValidator.getPrice())) {
            systemAccountInfoValidator.setPrice(new BigDecimal(systemAccountInfoValidator.getPrice().stripTrailingZeros().toPlainString()));
        }
        if (!StringUtils.isEmpty(systemAccountInfoValidator.getSecondPrice())) {
            systemAccountInfoValidator.setSecondPrice(new BigDecimal(systemAccountInfoValidator.getSecondPrice().stripTrailingZeros().toPlainString()));
        }
        if (!StringUtils.isEmpty(systemAccountInfoValidator.getThirdPrice())) {
            systemAccountInfoValidator.setThirdPrice(new BigDecimal(systemAccountInfoValidator.getThirdPrice().stripTrailingZeros().toPlainString()));
        }
        if (!StringUtils.isEmpty(systemAccountInfoValidator.getGrantingCredit())) {
            systemAccountInfoValidator.setGrantingCredit(new BigDecimal(systemAccountInfoValidator.getGrantingCredit().stripTrailingZeros().toPlainString()));
        }
        return ResponseDataUtil.buildSuccess(systemAccountInfoValidator);
    }

    /**
     * 保存或修改
     *
     * @param systemAccountInfoValidator
     * @param op                         操作类型 为add、edit
     * @return
     */
    @Transactional
    public ResponseData save(SystemAccountInfoValidator systemAccountInfoValidator, String op) {

        SystemAccountInfo entity = new SystemAccountInfo();
        BeanUtils.copyProperties(systemAccountInfoValidator, entity);

        //转换日期格式
        entity.setCreatedTime(DateTimeUtils.getDateTimeFormat(systemAccountInfoValidator.getCreatedTime()));

        //op 不为 edit 或 add
        if (!("edit".equals(op) || "add".equals(op))) {
            return ResponseDataUtil.buildError();
        }

        //记录日志
        log.info("[共用账户开户][{}]数据:{}", op, JSON.toJSONString(entity));
        systemAccountInfoRepository.saveAndFlush(entity);

        //开通身份认证财务账户
        if ("add".equals(op)) {
            FinanceAccount financeAccount = new FinanceAccount();
            financeAccount.setAccountId(entity.getAccount());
            financeAccount.setAccountType(systemAccountInfoValidator.getBusinessType());
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
            financeAccountRepository.updateAccountCreditSumByAccountId(entity.getAccount(), entity.getGrantingCredit());
        }

        //是否同步到网关 则放到redis中
        if ("1".equals(entity.getIsGateway())) {

            //放到redis中对象
            RedisModel redisModel = new RedisModel();
            redisModel.setMd5HmacKey(systemAccountInfoValidator.getMd5HmacKey());
            redisModel.setAesKey(systemAccountInfoValidator.getAesKey());
            redisModel.setAesIv(systemAccountInfoValidator.getAesIv());
            redisModel.setSubmitRate(entity.getSubmitLimiter());
            redisModel.setIps(systemAccountInfoValidator.getIdentifyIp());
            //把数据放到redis里
            redisTemplate.opsForValue().set(RedisConstant.HTTP_SERVER_KEY + systemAccountInfoValidator.getAccount(), redisModel);
        } else {
            redisTemplate.delete(RedisConstant.HTTP_SERVER_KEY + systemAccountInfoValidator.getAccount());
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

        ResponseData<SystemAccountInfoValidator> responseData = this.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(responseData.getCode())) {
            return ResponseDataUtil.buildError();
        }
        //把数据放到redis里
        redisTemplate.delete(RedisConstant.HTTP_SERVER_KEY + id);
        systemAccountInfoRepository.logoutAccount(id, "0");
        return ResponseDataUtil.buildSuccess();
    }

}
