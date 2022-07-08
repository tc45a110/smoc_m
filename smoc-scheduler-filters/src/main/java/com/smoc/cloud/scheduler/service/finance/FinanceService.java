package com.smoc.cloud.scheduler.service.finance;

import com.google.gson.Gson;
import com.smoc.cloud.common.filters.utils.RedisConstant;
import com.smoc.cloud.scheduler.batch.filters.model.BusinessRouteValue;
import com.smoc.cloud.scheduler.initialize.Reference;
import com.smoc.cloud.scheduler.initialize.entity.AccountFinanceInfo;
import com.smoc.cloud.scheduler.initialize.entity.AccountMessagePrice;
import com.smoc.cloud.scheduler.initialize.model.MessagePriceBusinessModel;
import com.smoc.cloud.scheduler.service.redis.FiltersRedisDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 财务相关业务逻辑
 */
@Slf4j
@Service
public class FinanceService {

    @Autowired
    @Resource(name = "defaultRedisTemplate")
    private RedisTemplate redisTemplate;

    @Autowired
    public FiltersRedisDataService filtersRedisDataService;


    /**
     * 获取运营商短信单价
     *
     * @param businessRouteValue
     * @return
     */
    public BigDecimal getAccountMessagePrice(BusinessRouteValue businessRouteValue) {
        MessagePriceBusinessModel messagePriceBusinessModel = Reference.accountMessagePrices.get(businessRouteValue.getAccountId());
        if (null == messagePriceBusinessModel) {
            return null;
        }
        AccountMessagePrice accountMessagePrice = messagePriceBusinessModel.getAccountMessagePrice(businessRouteValue.getBusinessCarrier(), businessRouteValue.getAreaCode());
        businessRouteValue.setPayType(accountMessagePrice.getPayType());
        businessRouteValue.setChargeType(accountMessagePrice.getChargeType());
        businessRouteValue.setMessagePrice(accountMessagePrice.getCarrierPrice());
        return accountMessagePrice.getCarrierPrice();

    }

    /**
     * 检查财务账户余额是否充足，并返回财务账户id
     *
     * @param businessRouteValue
     * @return true 表示余额充足  false 表示余额不够；
     */
    public Boolean checkingAccountFinance(BusinessRouteValue businessRouteValue) {

        AccountFinanceInfo accountFinanceInfo = Reference.accountFinances.get(businessRouteValue.getAccountId());
        if (null == accountFinanceInfo) {
            return false;
        }
        businessRouteValue.setFinanceId(accountFinanceInfo.getAccountId());

        /**
         * 如果是后付费,不判断余额
         */
        if ("2".equals(businessRouteValue.getPayType())) {
            return true;
        }

        BigDecimal zero = new BigDecimal(0);//为评判标准
        BigDecimal consumeTotal = businessRouteValue.getMessagePrice().multiply(new BigDecimal(businessRouteValue.getMessageTotal()));//本次消费总金额

        /**
         * 判定操作财务账户及冻结、扣费逻辑
         * 1、如果没有共享账户，则消费本账户费用,判断依据可用余额、授信金额
         */
        if (StringUtils.isEmpty(accountFinanceInfo.getShareId())) { //无共享账户
            //redis中的账户余额
            Object usableSumRedis = filtersRedisDataService.hget(RedisConstant.FILTERS_CONFIG_ACCOUNT_FINANCE, accountFinanceInfo.getAccountId());
            if (null == usableSumRedis) {
                return false;
            }
            BigDecimal usableSum = new BigDecimal(usableSumRedis.toString());
            BigDecimal checkResult = usableSum.add(accountFinanceInfo.getAccountCreditSum()).subtract(consumeTotal);
            //余额充足
            if (!(checkResult.compareTo(zero) == -1)) {
                //更新redis中的账户余额
                BigDecimal remaining = usableSum.subtract(consumeTotal);
                filtersRedisDataService.hset(RedisConstant.FILTERS_CONFIG_ACCOUNT_FINANCE, accountFinanceInfo.getAccountId(), remaining.toString());
            }
            return !(checkResult.compareTo(zero) == -1);
        }

        /**
         *判定操作财务账户及冻结、扣费逻辑
         * 2、有共享账户情况下，判断本账户余额（不包含授信金额）是否充足,判断依据可用余额
         */
        //redis中的账户余额
        Object usableSumRedis = filtersRedisDataService.hget(RedisConstant.FILTERS_CONFIG_ACCOUNT_FINANCE, accountFinanceInfo.getAccountId());
        if (null == usableSumRedis) {
            return false;
        }
        BigDecimal usableSum = new BigDecimal(usableSumRedis.toString());
        BigDecimal checkResult = usableSum.subtract(consumeTotal);
        //本账号余额充足
        if (!(checkResult.compareTo(zero) == -1)) {
            //更新redis中的账户余额
            filtersRedisDataService.hset(RedisConstant.FILTERS_CONFIG_ACCOUNT_FINANCE, accountFinanceInfo.getAccountId(), checkResult.toString());
            return true;
        }

        /**
         *判定操作财务账户及冻结、扣费逻辑
         * 3、有共享账户，本账户余额不足的情况下，使用共享财务账户,判断依据可用余额、授信金额
         */
        AccountFinanceInfo shareAccountFinanceInfo = Reference.accountFinances.get(accountFinanceInfo.getShareId());
        if (null == shareAccountFinanceInfo) {
            return false;
        }
        businessRouteValue.setFinanceId(shareAccountFinanceInfo.getAccountId());

        //redis中的账户余额
        Object shareUsableSumRedis = filtersRedisDataService.hget(RedisConstant.FILTERS_CONFIG_ACCOUNT_FINANCE, shareAccountFinanceInfo.getAccountId());
        if (null == shareUsableSumRedis) {
            return false;
        }
        BigDecimal shareUsableSum = new BigDecimal(shareUsableSumRedis.toString());
        BigDecimal checkShareResult = shareUsableSum.add(shareAccountFinanceInfo.getAccountCreditSum()).subtract(consumeTotal);
        if (!(checkShareResult.compareTo(zero) == -1)) {
            //更新共享账户redis中的账户余额
            BigDecimal remaining = shareUsableSum.subtract(consumeTotal);
            filtersRedisDataService.hset(RedisConstant.FILTERS_CONFIG_ACCOUNT_FINANCE, accountFinanceInfo.getAccountId(), remaining.toString());
        }
        return !(checkShareResult.compareTo(zero) == -1);

    }

    /**
     * 业务账号财务账户同步到redis
     */
    public void syncFinanceAccountToRedis(Map<String, AccountFinanceInfo> accountFinanceInfoMap) {
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            connection.openPipeline();
            accountFinanceInfoMap.forEach((key, value) -> {
                String usableSum = value.getAccountUsableSum().toString();
                connection.hSet(RedisSerializer.string().serialize(RedisConstant.FILTERS_CONFIG_ACCOUNT_FINANCE), RedisSerializer.string().serialize(key), RedisSerializer.string().serialize(new Gson().toJson(usableSum)));
            });
            connection.close();
            return null;
        });
    }
}
