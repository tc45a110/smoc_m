package com.smoc.cloud.filters.filter.full_filter;

import com.google.gson.Gson;
import com.smoc.cloud.common.filters.utils.RedisConstant;
import com.smoc.cloud.filters.filter.BaseGatewayFilter;
import com.smoc.cloud.filters.service.account.CarrierDailyLimiterFilter;
import com.smoc.cloud.filters.service.account.MaskProvinceFilter;
import com.smoc.cloud.filters.service.account.SendTimeLimitFilter;
import com.smoc.cloud.filters.service.message.FullMessageFilter;
import com.smoc.cloud.filters.service.number.PhoneSendFrequencyLimitFilter;
import com.smoc.cloud.filters.service.number.SystemPhoneFilter;
import com.smoc.cloud.filters.request.model.RequestFullParams;
import com.smoc.cloud.filters.service.FiltersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;


/**
 * COMMON_高级扩展参数过滤；
 * 这些过滤参数，都有独特的逻辑，基本上参数是写死的，完成功能比较多
 */
@Slf4j
@Component
public class CommonExtendFilterParamsGatewayFilter extends BaseGatewayFilter implements Ordered, GatewayFilter {

    @Autowired
    private SystemPhoneFilter systemPhoneFilter;

    @Autowired
    private FullMessageFilter fullMessageFilter;

    @Autowired
    private PhoneSendFrequencyLimitFilter phoneSendFrequencyLimitFilter;

    @Autowired
    private CarrierDailyLimiterFilter carrierDailyLimiterFilter;

    @Autowired
    private MaskProvinceFilter maskProvinceFilter;

    @Autowired
    private SendTimeLimitFilter sendTimeLimitFilter;

    @Autowired
    private FiltersService filtersService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        //获取body内容
        String requestBody = exchange.getAttribute("cachedRequestBodyObject");

        //校验数据请求的数据结构
        RequestFullParams model = new Gson().fromJson(requestBody, RequestFullParams.class);

        /**
         * 查询业务账号配置的COMMON级别 配置参数
         */
        Map<Object, Object> entities = filtersService.getEntries(RedisConstant.FILTERS_CONFIG_ACCOUNT_COMMON + model.getAccount());
        if (null == entities || entities.size() < 1) {
            return success(exchange);
        }

        /**
         * 单手机号发送频率限制
         */
        Object phoneFrequencyLimit = entities.get("COMMON_SEND_FREQUENCY_LIMIT");
        Map<String, String> phoneFrequencyLimitResult = phoneSendFrequencyLimitFilter.filter(filtersService, phoneFrequencyLimit, model.getAccount(), model.getPhone());
        if (!"false".equals(phoneFrequencyLimitResult.get("result"))) {
            return errorHandle(exchange, phoneFrequencyLimitResult.get("code"), phoneFrequencyLimitResult.get("message"));
        }

        /**
         * 账号发送时间段限制
         */
        Object sendTimeLimit = entities.get("COMMON_SEND_TIME_LIMIT");
        Map<String, String> sendTimeLimitResult = sendTimeLimitFilter.filter(sendTimeLimit);
        if (!"false".equals(sendTimeLimitResult.get("result"))) {
            return errorHandle(exchange, sendTimeLimitResult.get("code"), sendTimeLimitResult.get("message"));
        }

        /**
         * 手机号黑名单过滤
         */
        Object isBlackListType = entities.get("COMMON_BLACK_LIST_LEVEL_FILTERING");
        Map<String, String> blackListFilterResult = systemPhoneFilter.filter(filtersService, isBlackListType, model.getPhone());
        if (!"false".equals(blackListFilterResult.get("result"))) {
            return errorHandle(exchange, blackListFilterResult.get("code"), blackListFilterResult.get("message"));
        }

        /**
         * 系统内容敏感词、审核词过滤、账号敏感词、行业敏感词账号审核词、各种白词洗白
         */
        Object isBlackWordsFilter = entities.get("COMMON_BLACK_WORD_FILTERING"); //是否过滤黑词
        Object isCheckWordsFilter = entities.get("COMMON_AUDIT_WORD_FILTERING"); //是否过滤审核词
        Object infoTypeSensitiveWordsFilter = entities.get("COMMON_INFO_SENSITIVE_WORD_FILTERING"); //过滤行业敏感词
        Map<String, String> blackWordsFilterResult = fullMessageFilter.filter(filtersService, isBlackWordsFilter, isCheckWordsFilter,infoTypeSensitiveWordsFilter, model.getAccount(), model.getMessage());
        if (!"false".equals(blackWordsFilterResult.get("result"))) {
            return errorHandle(exchange, blackWordsFilterResult.get("code"), blackWordsFilterResult.get("message"));
        }

        /**
         * 业务账号日限量
         */
        Object dailyLimitStyle = entities.get("COMMON_SEND_LIMIT_STYLE_DAILY");//运营商日限量方式
        Object dailyLimit = entities.get("COMMON_SEND_LIMIT_NUMBER_DAILY_" + model.getCarrier());//日限量
        Map<String, String> dailyLimitFilterResult = carrierDailyLimiterFilter.filter(filtersService, dailyLimitStyle, dailyLimit, model.getAccount(), model.getCarrier(), model.getNumbers());
        if (!"false".equals(dailyLimitFilterResult.get("result"))) {
            return errorHandle(exchange, dailyLimitFilterResult.get("code"), dailyLimitFilterResult.get("message"));
        }

        /**
         * 屏蔽省份
         */
        Object cmccMaskProvince = entities.get("COMMON_CMCC_MASK_PROVINCE");
        Object unicMaskProvince = entities.get("COMMON_UNIC_MASK_PROVINCE");
        Object telcMaskProvince = entities.get("COMMON_TELC_MASK_PROVINCE");
        Map<String, String> maskProvinceResult = maskProvinceFilter.filter(model.getProvinceCode(), model.getCarrier(), cmccMaskProvince, unicMaskProvince, telcMaskProvince);
        if (!"false".equals(maskProvinceResult.get("result"))) {
            return errorHandle(exchange, maskProvinceResult.get("code"), maskProvinceResult.get("message"));
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 40;
    }


}
