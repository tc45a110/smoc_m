package com.smoc.cloud.scheduler.service.finance;

import com.smoc.cloud.scheduler.initialize.Reference;
import com.smoc.cloud.scheduler.initialize.entity.AccountFinanceInfo;
import com.smoc.cloud.scheduler.initialize.model.MessagePriceBusinessModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

@Slf4j
@Service
public class FinanceService {


    /**
     * 获取运营商短信单价
     *
     * @param accountId
     * @param carrier
     * @param areaCode  当为国际运营商时候，用到这个字段
     * @return
     */
    public BigDecimal getAccountMessagePrice(String accountId, String carrier, String areaCode) {
        MessagePriceBusinessModel messagePriceBusinessModel = Reference.accountMessagePrices.get(accountId);
        if (null == messagePriceBusinessModel) {
            return null;
        }

        BigDecimal price = messagePriceBusinessModel.getMessagePrice(carrier, areaCode);
        return price;

    }

    /**
     * 检查财务账户余额是否充足
     *
     * @param accountId
     * @param messageTotal 本条消息计数条数
     * @return true 表示余额充足  false 表示余额不够
     */
    public Boolean checkingAccountFinance(String accountId, Integer messageTotal, BigDecimal price) {
        AccountFinanceInfo accountFinanceInfo = Reference.accountFinances.get(accountId);
        if (null == accountFinanceInfo) {
            return false;
        }

        BigDecimal zero = new BigDecimal(0);
        BigDecimal consumeTotal = price.multiply(new BigDecimal(messageTotal));//本次消费总金额
        /**
         * 这要注意共享账户逻辑，现在逻辑是，如果有财务共享账户，则直接使用财务共享账户
         */
        if (!StringUtils.isEmpty(accountFinanceInfo.getShareId())) { //共享财务账户
            AccountFinanceInfo shareAccountFinanceInfo = Reference.accountFinances.get(accountFinanceInfo.getShareId());
            if (null == shareAccountFinanceInfo) {
                return false;
            }
            BigDecimal result = shareAccountFinanceInfo.getAccountUsableSum().add(shareAccountFinanceInfo.getAccountCreditSum()).subtract(consumeTotal);
            return !(result.compareTo(zero) == -1);
        }

        /**
         * 如果 不存在共享账户
         */
        BigDecimal result = accountFinanceInfo.getAccountUsableSum().add(accountFinanceInfo.getAccountCreditSum()).subtract(consumeTotal);
        return !(result.compareTo(zero) == -1);
    }
}
