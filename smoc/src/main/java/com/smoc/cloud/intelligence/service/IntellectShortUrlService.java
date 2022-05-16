package com.smoc.cloud.intelligence.service;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.intelligence.IntellectShortUrlValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.finance.repository.FinanceAccountRepository;
import com.smoc.cloud.intelligence.entity.IntellectShortUrl;
import com.smoc.cloud.intelligence.repository.IntellectShortUrlRepository;
import com.smoc.cloud.parameter.entity.ParameterExtendSystemParamValue;
import com.smoc.cloud.parameter.repository.ParameterExtendSystemParamValueRepository;
import com.smoc.cloud.system.entity.SystemAccountInfo;
import com.smoc.cloud.system.repository.SystemAccountInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Optional;


@Slf4j
@Service
public class IntellectShortUrlService {

    @Resource
    private FinanceAccountRepository financeAccountRepository;

    @Resource
    private SystemAccountInfoRepository systemAccountInfoRepository;

    @Resource
    private IntellectShortUrlRepository intellectShortUrlRepository;

    @Resource
    private ParameterExtendSystemParamValueRepository parameterExtendSystemParamValueRepository;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public PageList<IntellectShortUrlValidator> page(PageParams<IntellectShortUrlValidator> pageParams) {
        return intellectShortUrlRepository.page(pageParams);
    }

    /**
     * 保存
     *
     * @param intellectShortUrlValidator
     * @return
     */
    @Transactional
    public ResponseData save(IntellectShortUrlValidator intellectShortUrlValidator) {

        IntellectShortUrl entity = new IntellectShortUrl();
        BeanUtils.copyProperties(intellectShortUrlValidator, entity);
        //转换日期格式
        entity.setCreatedTime(DateTimeUtils.getDateTimeFormat(intellectShortUrlValidator.getCreatedTime()));
        entity.setIsGiveBack("0");

        //成本价格
        ParameterExtendSystemParamValue costPriceParam = this.parameterExtendSystemParamValueRepository.findByBusinessTypeAndBusinessIdAndParamKey("INTELLECT_COST","INTELLECT_COST","INTELLECT_PRICE");
        BigDecimal costPrice = new BigDecimal(costPriceParam.getParamValue());
        entity.setCostPrice(costPrice);

        /**
         * 处理计费
         */
        //查询账号
        Optional<SystemAccountInfo> accountOptional = systemAccountInfoRepository.findById(intellectShortUrlValidator.getCustId());
        if (!accountOptional.isPresent()) {
            return ResponseDataUtil.buildError("财务账户不存在");
        }
        BigDecimal price = accountOptional.get().getPrice();
        entity.setCurrentPrice(price);
        //计算支付金额
        BigDecimal paySum = price.multiply(new BigDecimal(intellectShortUrlValidator.getShowTimes()));

        //检查账户余额是否充足
        Boolean accountUsable = financeAccountRepository.checkAccountUsableSum(intellectShortUrlValidator.getCustId(), paySum);
        if (!accountUsable) {
            return ResponseDataUtil.buildError("账户余额不足");
        }
        //冻结金额
        financeAccountRepository.freeze(intellectShortUrlValidator.getCustId(), paySum);

        //记录日志
        log.info("[智能短信][保存短链][{}]数据:{}", JSON.toJSONString(entity));
        intellectShortUrlRepository.saveAndFlush(entity);


        return ResponseDataUtil.buildSuccess();
    }
}
