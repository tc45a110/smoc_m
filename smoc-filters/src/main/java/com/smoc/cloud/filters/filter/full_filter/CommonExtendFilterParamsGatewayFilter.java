package com.smoc.cloud.filters.filter.full_filter;

import com.google.gson.Gson;
import com.smoc.cloud.common.filters.utils.RedisConstant;
import com.smoc.cloud.filters.filter.BaseGatewayFilter;
import com.smoc.cloud.filters.service.account.CarrierDailyLimiterFilter;
import com.smoc.cloud.filters.service.account.MaskProvinceFilter;
import com.smoc.cloud.filters.service.account.SendTimeLimitFilter;
import com.smoc.cloud.filters.service.message.InfoTypeMessageFilter;
import com.smoc.cloud.filters.service.message.SystemMessageFilter;
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
import org.springframework.util.StringUtils;
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
    private SystemMessageFilter systemMessageFilter;

    @Autowired
    private InfoTypeMessageFilter infoTypeMessageFilter;

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
        //Long startCommonParam = System.currentTimeMillis();
        Map<Object, Object> entities = filtersService.getEntries(RedisConstant.FILTERS_CONFIG_ACCOUNT_COMMON + model.getAccount());
        //Long endCommonParam = System.currentTimeMillis();
        //log.info("[普通_过滤参数查询]耗时{}毫秒", endCommonParam - startCommonParam);
        if (null == entities || entities.size() < 1) {
            return success(exchange);
        }

        /**
         * 单手机号发送频率限制
         */
        Object phoneFrequencyLimit = entities.get("COMMON_SEND_FREQUENCY_LIMIT");
        //Long startFrequency = System.currentTimeMillis();
        Map<String, String> phoneFrequencyLimitResult = phoneSendFrequencyLimitFilter.filter(filtersService, phoneFrequencyLimit, model.getAccount(), model.getPhone());
        //Long endFrequency = System.currentTimeMillis();
        //log.info("[号码_发送频率限制]：耗时{}毫秒", endFrequency - startFrequency);
        if (!"false".equals(phoneFrequencyLimitResult.get("result"))) {
            return errorHandle(exchange, phoneFrequencyLimitResult.get("code"), phoneFrequencyLimitResult.get("message"));
        }

        /**
         * 账号发送时间段限制
         */
        Object sendTimeLimit = entities.get("COMMON_SEND_TIME_LIMIT");
        //Long startTimeLimit = System.currentTimeMillis();
        Map<String, String> sendTimeLimitResult = sendTimeLimitFilter.filter(sendTimeLimit);
        //Long endTimeLimit = System.currentTimeMillis();
        //log.info("[发送时间限制]：耗时{}毫秒", endTimeLimit - startTimeLimit);
        if (!"false".equals(sendTimeLimitResult.get("result"))) {
            return errorHandle(exchange, sendTimeLimitResult.get("code"), sendTimeLimitResult.get("message"));
        }

        /**
         * 手机号黑名单过滤
         */
        Object isBlackListType = entities.get("COMMON_BLACK_LIST_LEVEL_FILTERING");
        //Long startBlackList = System.currentTimeMillis();
        Map<String, String> blackListFilterResult = systemPhoneFilter.filter(filtersService, isBlackListType, model.getPhone());
        //Long endBlackList = System.currentTimeMillis();
        //log.info("[号码_黑名单过滤]:耗时{}毫秒", endBlackList - startBlackList);
        if (!"false".equals(blackListFilterResult.get("result"))) {
            return errorHandle(exchange, blackListFilterResult.get("code"), blackListFilterResult.get("message"));
        }

        /**
         * 系统内容敏感词、审核词过滤
         */
        Object isBlackWordsFilter = entities.get("COMMON_BLACK_WORD_FILTERING"); //是否过滤黑词
        Object isCheckWordsFilter = entities.get("COMMON_AUDIT_WORD_FILTERING"); //是否过滤审核词
        //Long startSensitive = System.currentTimeMillis();
        Map<String, String> blackWordsFilterResult = systemMessageFilter.filter(filtersService, isBlackWordsFilter, isCheckWordsFilter, model.getMessage());
        //Long endSensitive = System.currentTimeMillis();
        //log.info("[内容_敏感词、审核词过滤]:耗时{}毫秒", endSensitive - startSensitive);
        if (!"false".equals(blackWordsFilterResult.get("result"))) {
            return errorHandle(exchange, blackWordsFilterResult.get("code"), blackWordsFilterResult.get("message"));
        }

        /**
         * 行业敏感词过滤
         */
        Object infoTypeSensitiveWordsFilter = entities.get("COMMON_INFO_SENSITIVE_WORD_FILTERING"); //是否过滤审核词
        Long startSensitive = System.currentTimeMillis();
        Map<String, String> infoTypeSensitiveFilterResult = infoTypeMessageFilter.filter(filtersService, infoTypeSensitiveWordsFilter, model.getAccount(), model.getMessage());
        Long endSensitive = System.currentTimeMillis();
        log.info("[行业_敏感词过滤]:耗时{}毫秒", endSensitive - startSensitive);
        if (!"false".equals(infoTypeSensitiveFilterResult.get("result"))) {
            return errorHandle(exchange, infoTypeSensitiveFilterResult.get("code"), infoTypeSensitiveFilterResult.get("message"));
        }




        /**
         * 业务账号日限量
         */
        Object dailyLimitStyle = entities.get("COMMON_SEND_LIMIT_STYLE_DAILY");//运营商日限量方式
        Object dailyLimit = entities.get("COMMON_SEND_LIMIT_NUMBER_DAILY_" + model.getCarrier());//日限量
        //Long startDailyLimit = System.currentTimeMillis();
        Map<String, String> dailyLimitFilterResult = carrierDailyLimiterFilter.filter(filtersService, dailyLimitStyle, dailyLimit, model.getAccount(), model.getCarrier(), model.getNumbers());
        //Long endDailyLimit = System.currentTimeMillis();
        //log.info("[账号-运营商-日限量]:耗时{}毫秒", endDailyLimit - startDailyLimit);
        if (!"false".equals(dailyLimitFilterResult.get("result"))) {
            return errorHandle(exchange, dailyLimitFilterResult.get("code"), dailyLimitFilterResult.get("message"));
        }

        /**
         * 屏蔽省份
         */
        Object cmccMaskProvince = entities.get("COMMON_CMCC_MASK_PROVINCE");
        Object unicMaskProvince = entities.get("COMMON_UNIC_MASK_PROVINCE");
        Object telcMaskProvince = entities.get("COMMON_TELC_MASK_PROVINCE");
        //Long startMaskProvince = System.currentTimeMillis();
        Map<String, String> maskProvinceResult = maskProvinceFilter.filter(model.getProvinceCode(), model.getCarrier(), cmccMaskProvince, unicMaskProvince, telcMaskProvince);
        //Long endMaskProvince = System.currentTimeMillis();
        //log.info("[账号-运营商-省份屏蔽]:耗时{}毫秒", endMaskProvince - startMaskProvince);
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
