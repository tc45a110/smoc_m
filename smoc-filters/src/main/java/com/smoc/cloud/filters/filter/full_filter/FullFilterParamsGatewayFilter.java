package com.smoc.cloud.filters.filter.full_filter;

import com.google.gson.Gson;
import com.smoc.cloud.common.filters.utils.RedisConstant;
import com.smoc.cloud.filters.filter.BaseGatewayFilter;
import com.smoc.cloud.filters.service.account.CarrierDailyLimiterFilter;
import com.smoc.cloud.filters.service.account.MaskProvinceFilter;
import com.smoc.cloud.filters.service.account.SendTimeLimitFilter;
import com.smoc.cloud.filters.service.message.ChannelMessageFilter;
import com.smoc.cloud.filters.service.message.ExtendMessageParamsFilter;
import com.smoc.cloud.filters.service.message.FullMessageFilter;
import com.smoc.cloud.filters.service.number.ExtendNumberParamsFilter;
import com.smoc.cloud.filters.service.number.IndustryBlackListFilter;
import com.smoc.cloud.filters.service.number.PhoneSendFrequencyLimitFilter;
import com.smoc.cloud.filters.service.number.SystemPhoneFilter;
import com.smoc.cloud.filters.request.model.RequestFullParams;
import com.smoc.cloud.filters.service.FiltersService;
import com.smoc.cloud.filters.utils.DFA.FilterInitialize;
import com.smoc.cloud.filters.utils.FilterResponseCode;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * COMMON_高级扩展参数过滤；
 * 这些过滤参数，都有独特的逻辑，基本上参数是写死的，完成功能比较多
 */
@Slf4j
@Component
public class FullFilterParamsGatewayFilter extends BaseGatewayFilter implements Ordered, GatewayFilter {

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
    private ExtendMessageParamsFilter extendMessageParamsFilter;

    @Autowired
    private ExtendNumberParamsFilter extendNumberParamsFilter;

    @Autowired
    private IndustryBlackListFilter industryBlackListFilter;

    @Autowired
    private ChannelMessageFilter channelMessageFilter;

    @Autowired
    private FiltersService filtersService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        //获取body内容
        String requestBody = exchange.getAttribute("cachedRequestBodyObject");
        //log.info("[requestBody]:{}",requestBody);

        //校验数据请求的数据结构
        RequestFullParams model = new Gson().fromJson(requestBody, RequestFullParams.class);
        //log.info("[model]:{}",requestBody);
        /**
         * 关键参数校验
         */
        if (StringUtils.isEmpty(model.getAccount()) || StringUtils.isEmpty(model.getPhone()) || StringUtils.isEmpty(model.getMessage())) {
            return errorHandle(exchange, FilterResponseCode.PARAM_FORMAT_ERROR.getCode(), FilterResponseCode.PARAM_FORMAT_ERROR.getMessage());
        }


        /**
         * 查询业务账号配置的COMMON级别 配置参数
         */
        Map<Object, Object> entities = filtersService.getEntries(RedisConstant.FILTERS_CONFIG_ACCOUNT_COMMON + model.getAccount());
        //log.info("COMMON 配置参数:{}", new Gson().toJson(entities));
        if (null == entities || entities.size() < 1) {
            return success(exchange);
        }

        //////////////////////////////////////////////////////////////////////////////////////////
        //以下是业务账号，约定的配置参数，该过滤参数业务是固定的过滤逻辑
        //////////////////////////////////////////////////////////////////////////////////////////
        /**
         * 以下逻辑是业务账号，约定的配置参数，该过滤参数业务是固定的
         * 1、单手机号发送频率限制；未配置则跳过（调整到过滤的最后，如果过滤失败，不计次数）
         * 2、账号发送时间段限制；未配置则跳过
         * 3、业务账号日限量；未配置则跳过
         * 4、屏蔽省份；未配置则跳过
         */

        /**
         * 2、账号发送时间段限制
         */
        Object sendTimeLimit = entities.get("COMMON_SEND_TIME_LIMIT");
        Map<String, String> sendTimeLimitResult = sendTimeLimitFilter.filter(sendTimeLimit);
        if (!"false".equals(sendTimeLimitResult.get("result"))) {
            return errorHandle(exchange, sendTimeLimitResult.get("code"), sendTimeLimitResult.get("message"));
        }

        /**
         * 3、业务账号日限量
         */
        Object dailyLimitStyle = entities.get("COMMON_SEND_LIMIT_STYLE_DAILY");//运营商日限量方式
        Object dailyLimit = entities.get("COMMON_SEND_LIMIT_NUMBER_DAILY_" + model.getCarrier());//日限量
        Map<String, String> dailyLimitFilterResult = carrierDailyLimiterFilter.filter(filtersService, dailyLimitStyle, dailyLimit, model.getAccount(), model.getCarrier(), model.getNumbers());
        if (!"false".equals(dailyLimitFilterResult.get("result"))) {
            return errorHandle(exchange, dailyLimitFilterResult.get("code"), dailyLimitFilterResult.get("message"));
        }

        /**
         * 4、屏蔽省份
         */
        Object cmccMaskProvince = entities.get("COMMON_CMCC_MASK_PROVINCE");
        Object unicMaskProvince = entities.get("COMMON_UNIC_MASK_PROVINCE");
        Object telcMaskProvince = entities.get("COMMON_TELC_MASK_PROVINCE");
        Map<String, String> maskProvinceResult = maskProvinceFilter.filter(model.getProvinceCode(), model.getCarrier(), cmccMaskProvince, unicMaskProvince, telcMaskProvince);
        if (!"false".equals(maskProvinceResult.get("result"))) {
            return errorHandle(exchange, maskProvinceResult.get("code"), maskProvinceResult.get("message"));
        }

        //////////////////////////////////////////////////////////////////////////////////////////
        //以下是手机号过滤逻辑
        //////////////////////////////////////////////////////////////////////////////////////////
        /**
         *以下是手机号过滤逻辑
         * 1、手机号系统黑名单过滤；并且实现系统黑名单洗白，洗白依据系统白名单，业务账号配置的白名单规则
         * 2、行业黑名单过滤
         * 3、业务账号手机号扩展参数过滤
         */
        /**
         * 1、手机号黑名单过滤，
         */
        Object isBlackListType = entities.get("COMMON_BLACK_LIST_LEVEL_FILTERING");
        Object isIndustryBlackListType = entities.get("COMMON_INFO_BLACK_LIST_FILTERING");//行业黑名单
        Map<String, String> blackListFilterResult = systemPhoneFilter.filter(filtersService, isBlackListType, model.getAccount(), model.getPhone(), isIndustryBlackListType);
        if (!"false".equals(blackListFilterResult.get("result"))) {
            return errorHandle(exchange, blackListFilterResult.get("code"), blackListFilterResult.get("message"));
        }

        /**
         * 2、行业黑名单过滤
         */
        Map<String, String> industryBlackListFilterResult = industryBlackListFilter.filter(filtersService, isIndustryBlackListType, model.getAccount(), model.getPhone());
        if (!"false".equals(industryBlackListFilterResult.get("result"))) {
            return errorHandle(exchange, industryBlackListFilterResult.get("code"), industryBlackListFilterResult.get("message"));
        }

        /**
         * 3、业务账号手机号扩展参数过滤
         */
        Map<String, String> extendNumberParamsFilterResult = extendNumberParamsFilter.filter(filtersService, model.getAccount(), model.getPhone());
        if (!"false".equals(extendNumberParamsFilterResult.get("result"))) {
            return errorHandle(exchange, extendNumberParamsFilterResult.get("code"), extendNumberParamsFilterResult.get("message"));
        }

        //////////////////////////////////////////////////////////////////////////////////////////
        //以下是短信内容过滤逻辑
        //////////////////////////////////////////////////////////////////////////////////////////
        /**
         * 以下逻辑是内容过滤，内容过滤有着过滤的逻辑规则，下面说下顾虑的逻辑规则
         * 1、根据模版ID匹配，匹配成功，则跳过其他内容过滤 匹配不成功则响应错误
         * 2、固定模版匹配，匹配成功，则跳过其他内容过滤
         * 3、变量模版匹配，根据配置（1）匹配上变量模版 则跳过其他内容过滤 （2）匹配上变量模版，继续其他的内容过滤项
         * 4、签名模版匹配，（1）提取短信内容签名 （2）匹配签名；  如果匹配上签名，继续其他内容过滤，如果没匹配上签名，则返回过滤失败
         * 5、系统内容敏感词、审核词过滤、账号敏感词、行业敏感词账号审核词、各种白词洗白
         * 6、通道敏感词过滤
         * 7、业务账号内容扩展参数过滤
         */

        /**
         * 1、根据模版ID匹配，匹配成功，则跳过其他内容过滤 匹配不成功则响应错误
         */
        if (!StringUtils.isEmpty(model.getTemplateId())) {
            //先去匹配固定格式模版
            Object fixedTemplate = filtersService.getMapValue(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_TEMPLATE_HTTP_FIXED, model.getTemplateId());
            if (!StringUtils.isEmpty(fixedTemplate)) {
                if (model.getMessage().equals(fixedTemplate)) {
                    return success(exchange);
                }
            }
            //根据模版id 去找变量模版
            Object variableTemplate = filtersService.getMapValue(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_TEMPLATE_HTTP_VARIABLE, model.getTemplateId());
            if (!StringUtils.isEmpty(variableTemplate)) {
                Pattern pattern = Pattern.compile(variableTemplate.toString());
                Matcher matcher = pattern.matcher(model.getMessage());
                if (matcher.find()) {
                    return success(exchange);
                }
            }
            return errorHandle(exchange, FilterResponseCode.TEMPLATE_IS_NOT_FOUND.getCode(), FilterResponseCode.TEMPLATE_IS_NOT_FOUND.getMessage());
        }

        //是否模板匹配
        Object isMatchTemplate = entities.get("COMMON_CMPP_MATCH_TEMPLATE_FILTERING");
        log.info("isMatchTemplate:{}",isMatchTemplate);
        if (null == isMatchTemplate || isMatchTemplate.toString().equals("1")) {
            /**
             * 2、固定模版匹配，匹配成功，则跳过其他内容过滤
             */
            String noFilterFixedTemplate = FilterInitialize.accountFilterFixedTemplateMap.get(model.getAccount());
            if (!StringUtils.isEmpty(noFilterFixedTemplate)) {
                Pattern pattern = Pattern.compile(noFilterFixedTemplate);
                Matcher matcher = pattern.matcher(model.getMessage());
                if (matcher.find()) {
                    return success(exchange);
                }
            }

            /**
             * 3、变量模版匹配，根据配置
             */
            //（1）匹配上变量模版 则跳过其他内容过滤
            String noFilterVariableTemplate = FilterInitialize.accountNoFilterVariableTemplateMap.get(model.getAccount());
            if (!StringUtils.isEmpty(noFilterVariableTemplate)) {
                Pattern pattern = Pattern.compile(noFilterVariableTemplate);
                Matcher matcher = pattern.matcher(model.getMessage());
                if (matcher.find()) {
                    return success(exchange);
                }
            }
        }
        //（2）突然发觉过滤没有意义，匹配上匹配不上都要向下过滤
//        String filterVariableTemplate = FilterInitialize.accountFilterVariableTemplateMap.get(model.getAccount());
//        if (!StringUtils.isEmpty(filterVariableTemplate)) {
//            Pattern pattern = Pattern.compile(filterVariableTemplate);
//            Matcher matcher = pattern.matcher(model.getMessage());
//            if (matcher.find()) {
//                return success(exchange);
//            }
//        }

        /**
         * 4、签名模版匹配，（1）提取短信内容签名 （2）匹配签名；  如果匹配上签名，继续其他内容过滤，如果没匹配上签名，则返回过滤失败
         */
        String signTemplate = FilterInitialize.accountSignTemplateMap.get(model.getAccount());
        if (!StringUtils.isEmpty(signTemplate)) {

            //解析内容签名
            String sign = null;
            Pattern pattern = Pattern.compile("【.*】");
            Matcher matcher = pattern.matcher(model.getMessage());
            if (matcher.find()) {
                sign = matcher.group(0);
            }
            if (StringUtils.isEmpty(sign)) {
                return errorHandle(exchange, FilterResponseCode.SIGN_IS_NULL.getCode(), FilterResponseCode.SIGN_IS_NULL.getMessage());
            }
            //验证签名是否报备
            Pattern signPattern = Pattern.compile(signTemplate);
            Matcher signMatcher = signPattern.matcher(sign);
            if (!signMatcher.find()) {
                return errorHandle(exchange, FilterResponseCode.SIGN_IS_NOT_FOUND.getCode(), FilterResponseCode.SIGN_IS_NOT_FOUND.getMessage());
            }

        } else {
            return errorHandle(exchange, FilterResponseCode.SIGN_IS_NOT_FOUND.getCode(), FilterResponseCode.SIGN_IS_NOT_FOUND.getMessage());
        }

        /**
         * 5、系统内容敏感词、审核词过滤、账号敏感词、行业敏感词账号审核词、各种白词洗白
         */
        Object isBlackWordsFilter = entities.get("COMMON_BLACK_WORD_FILTERING"); //是否过滤黑词
        Object isCheckWordsFilter = entities.get("COMMON_AUDIT_WORD_FILTERING"); //是否过滤审核词
        Object infoTypeSensitiveWordsFilter = entities.get("COMMON_INFO_SENSITIVE_WORD_FILTERING"); //过滤行业敏感词
        Map<String, String> blackWordsFilterResult = fullMessageFilter.filter(filtersService, isBlackWordsFilter, isCheckWordsFilter, infoTypeSensitiveWordsFilter, model.getAccount(), model.getMessage());
        if (!"false".equals(blackWordsFilterResult.get("result"))) {
            return errorHandle(exchange, blackWordsFilterResult.get("code"), blackWordsFilterResult.get("message"));
        }

        /**
         * 6、通道过滤参数
         */
        Map<String, String> channelMessageParamsFilterResult = channelMessageFilter.filter(model.getChannelId(), model.getMessage());
        if (!"false".equals(channelMessageParamsFilterResult.get("result"))) {
            return errorHandle(exchange, channelMessageParamsFilterResult.get("code"), channelMessageParamsFilterResult.get("message"));
        }

        /**
         * 7、业务账号内容扩展参数过滤
         */
        Map<String, String> extendMessageParamsFilterResult = extendMessageParamsFilter.filter(filtersService, model.getAccount(), model.getMessage());
        if (!"false".equals(extendMessageParamsFilterResult.get("result"))) {
            return errorHandle(exchange, extendMessageParamsFilterResult.get("code"), extendMessageParamsFilterResult.get("message"));
        }


        /**
         * 1、单手机号发送频率限制
         */
        Object phoneFrequencyLimit = entities.get("COMMON_SEND_FREQUENCY_LIMIT");
        Map<String, String> phoneFrequencyLimitResult = phoneSendFrequencyLimitFilter.filter(filtersService, phoneFrequencyLimit, model.getAccount(), model.getPhone());
        if (!"false".equals(phoneFrequencyLimitResult.get("result"))) {
            return errorHandle(exchange, phoneFrequencyLimitResult.get("code"), phoneFrequencyLimitResult.get("message"));
        }

        return success(exchange);
    }

    @Override
    public int getOrder() {
        return 40;
    }


}
