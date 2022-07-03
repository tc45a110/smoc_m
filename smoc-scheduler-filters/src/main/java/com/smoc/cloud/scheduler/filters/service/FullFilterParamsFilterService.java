package com.smoc.cloud.scheduler.filters.service;

import com.smoc.cloud.common.filters.utils.RedisConstant;

import com.smoc.cloud.scheduler.filters.service.service.FiltersService;
import com.smoc.cloud.scheduler.filters.service.service.account.CarrierDailyLimiterFilter;
import com.smoc.cloud.scheduler.filters.service.service.account.MaskProvinceFilter;
import com.smoc.cloud.scheduler.filters.service.service.account.SendTimeLimitFilter;
import com.smoc.cloud.scheduler.filters.service.service.message.ChannelMessageFilter;
import com.smoc.cloud.scheduler.filters.service.service.message.ExtendMessageParamsFilter;
import com.smoc.cloud.scheduler.filters.service.service.message.FullMessageFilter;
import com.smoc.cloud.scheduler.filters.service.service.number.ExtendNumberParamsFilter;
import com.smoc.cloud.scheduler.filters.service.service.number.IndustryBlackListFilter;
import com.smoc.cloud.scheduler.filters.service.service.number.PhoneSendFrequencyLimitFilter;
import com.smoc.cloud.scheduler.filters.service.service.number.SystemPhoneFilter;
import com.smoc.cloud.scheduler.filters.service.utils.DFA.FilterInitialize;
import com.smoc.cloud.scheduler.filters.service.utils.FilterResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * COMMON_高级扩展参数过滤；
 * 这些过滤参数，都有独特的逻辑，基本上参数是写死的，完成功能比较多
 */
@Slf4j
@Service
public class FullFilterParamsFilterService {

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

    public Map<String, String> filter(String phone, String account, String message, String templateId, String carrier, String provinceCode, String channelId) {

        /**
         * 关键参数校验
         */
        if (StringUtils.isEmpty(account) || StringUtils.isEmpty(phone) || StringUtils.isEmpty(message)) {
            return errorHandle(FilterResponseCode.PARAM_FORMAT_ERROR.getCode(), FilterResponseCode.PARAM_FORMAT_ERROR.getMessage());
        }


        /**
         * 查询业务账号配置的COMMON级别 配置参数
         */
        Map<Object, Object> entities = filtersService.getEntries(RedisConstant.FILTERS_CONFIG_ACCOUNT_COMMON + account);
        //log.info("COMMON 配置参数:{}", new Gson().toJson(entities));
        if (null == entities || entities.size() < 1) {
            return success();
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
            return errorHandle(sendTimeLimitResult.get("code"), sendTimeLimitResult.get("message"));
        }

        /**
         * 3、业务账号日限量
         */
        Object dailyLimitStyle = entities.get("COMMON_SEND_LIMIT_STYLE_DAILY");//运营商日限量方式
        Object dailyLimit = entities.get("COMMON_SEND_LIMIT_NUMBER_DAILY_" + carrier);//日限量
        Map<String, String> dailyLimitFilterResult = carrierDailyLimiterFilter.filter(filtersService, dailyLimitStyle, dailyLimit, account, carrier, 1);
        if (!"false".equals(dailyLimitFilterResult.get("result"))) {
            return errorHandle(dailyLimitFilterResult.get("code"), dailyLimitFilterResult.get("message"));
        }

        /**
         * 4、屏蔽省份
         */
        Object cmccMaskProvince = entities.get("COMMON_CMCC_MASK_PROVINCE");
        Object unicMaskProvince = entities.get("COMMON_UNIC_MASK_PROVINCE");
        Object telcMaskProvince = entities.get("COMMON_TELC_MASK_PROVINCE");
        Map<String, String> maskProvinceResult = maskProvinceFilter.filter(provinceCode, carrier, cmccMaskProvince, unicMaskProvince, telcMaskProvince);
        if (!"false".equals(maskProvinceResult.get("result"))) {
            return errorHandle(maskProvinceResult.get("code"), maskProvinceResult.get("message"));
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
//        Map<String, String> blackListFilterResult = systemPhoneFilter.filter(filtersService, isBlackListType, account, phone, isIndustryBlackListType);
//        if (!"false".equals(blackListFilterResult.get("result"))) {
//            return errorHandle(blackListFilterResult.get("code"), blackListFilterResult.get("message"));
//        }

        /**
         * 2、行业黑名单过滤
         */
        Map<String, String> industryBlackListFilterResult = industryBlackListFilter.filter(filtersService, isIndustryBlackListType, account, phone);
        if (!"false".equals(industryBlackListFilterResult.get("result"))) {
            return errorHandle(industryBlackListFilterResult.get("code"), industryBlackListFilterResult.get("message"));
        }

        /**
         * 3、业务账号手机号扩展参数过滤
         */
        Map<String, String> extendNumberParamsFilterResult = extendNumberParamsFilter.filter(filtersService, account, phone);
        if (!"false".equals(extendNumberParamsFilterResult.get("result"))) {
            return errorHandle(extendNumberParamsFilterResult.get("code"), extendNumberParamsFilterResult.get("message"));
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
        if (!StringUtils.isEmpty(templateId)) {
            //先去匹配固定格式模版
            Object fixedTemplate = filtersService.getMapValue(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_TEMPLATE_HTTP_FIXED, templateId);
            if (!StringUtils.isEmpty(fixedTemplate)) {
                if (message.equals(fixedTemplate)) {
                    return success();
                }
            }
            //根据模版id 去找变量模版
            Object variableTemplate = filtersService.getMapValue(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_TEMPLATE_HTTP_VARIABLE, templateId);
            if (!StringUtils.isEmpty(variableTemplate)) {
                String template = this.clean(variableTemplate.toString());
                Pattern pattern = Pattern.compile(template);
                Matcher matcher = pattern.matcher(this.clean(message));
                if (matcher.find()) {
                    return success();
                }
            }
            return errorHandle(FilterResponseCode.TEMPLATE_IS_NOT_FOUND.getCode(), FilterResponseCode.TEMPLATE_IS_NOT_FOUND.getMessage());
        }

        //是否模板匹配
        Object isMatchTemplate = entities.get("COMMON_CMPP_MATCH_TEMPLATE_FILTERING");
        if (null == isMatchTemplate || isMatchTemplate.toString().equals("1")) {
            /**
             * 2、固定模版匹配，匹配成功，则跳过其他内容过滤
             */
            String noFilterFixedTemplate = FilterInitialize.accountFilterFixedTemplateMap.get(account);
            if (!StringUtils.isEmpty(noFilterFixedTemplate)) {
                noFilterFixedTemplate = this.clean(noFilterFixedTemplate);
                String target = this.clean(message);
                Pattern pattern = Pattern.compile(noFilterFixedTemplate);
                Matcher matcher = pattern.matcher(target);
                if (matcher.find()) {
                    return success();
                }
            }

            /**
             * 3、变量模版匹配，根据配置
             */
            //（1）匹配上变量模版 则跳过其他内容过滤
            String noFilterVariableTemplate = FilterInitialize.accountNoFilterVariableTemplateMap.get(account);
            if (!StringUtils.isEmpty(noFilterVariableTemplate)) {
                noFilterVariableTemplate = this.clean(noFilterVariableTemplate);
                Pattern pattern = Pattern.compile(noFilterVariableTemplate);
                String target = this.clean(message);
                Matcher matcher = pattern.matcher(target);
                if (matcher.find()) {
                    return success();
                }
            }
        }
        //（2）突然发觉过滤没有意义，匹配上匹配不上都要向下过滤
//        String filterVariableTemplate = FilterInitialize.accountFilterVariableTemplateMap.get(account);
//        if (!StringUtils.isEmpty(filterVariableTemplate)) {
//            Pattern pattern = Pattern.compile(filterVariableTemplate);
//            Matcher matcher = pattern.matcher(message);
//            if (matcher.find()) {
//                return success(exchange);
//            }
//        }

        /**
         * 4、签名模版匹配，（1）提取短信内容签名 （2）匹配签名；  如果匹配上签名，继续其他内容过滤，如果没匹配上签名，则返回过滤失败
         */
        String signTemplate = FilterInitialize.accountSignTemplateMap.get(account);
        //是否模板匹配，如果选择是，才会去判断签名
        if (null == isMatchTemplate || isMatchTemplate.toString().equals("1")) {
            if (!StringUtils.isEmpty(signTemplate)) {
                //解析内容签名
                String sign = null;
                Pattern pattern = Pattern.compile("【.*】");
                Matcher matcher = pattern.matcher(message);
                if (matcher.find()) {
                    sign = matcher.group(0);
                }
                if (StringUtils.isEmpty(sign)) {
                    return errorHandle(FilterResponseCode.SIGN_IS_NULL.getCode(), FilterResponseCode.SIGN_IS_NULL.getMessage());
                }
                //验证签名是否报备
                Pattern signPattern = Pattern.compile(signTemplate);
                Matcher signMatcher = signPattern.matcher(sign);
                if (!signMatcher.find()) {
                    return errorHandle(FilterResponseCode.SIGN_IS_NOT_FOUND.getCode(), FilterResponseCode.SIGN_IS_NOT_FOUND.getMessage());
                }

            } else {
                return errorHandle(FilterResponseCode.SIGN_IS_NOT_FOUND.getCode(), FilterResponseCode.SIGN_IS_NOT_FOUND.getMessage());
            }
        }

        /**
         * 5、系统内容敏感词、审核词过滤、账号敏感词、行业敏感词账号审核词、各种白词洗白
         */
        Object isBlackWordsFilter = entities.get("COMMON_BLACK_WORD_FILTERING"); //是否过滤黑词
        Object isCheckWordsFilter = entities.get("COMMON_AUDIT_WORD_FILTERING"); //是否过滤审核词
        Object infoTypeSensitiveWordsFilter = entities.get("COMMON_INFO_SENSITIVE_WORD_FILTERING"); //过滤行业敏感词
        Map<String, String> blackWordsFilterResult = fullMessageFilter.filter(filtersService, isBlackWordsFilter, isCheckWordsFilter, infoTypeSensitiveWordsFilter, account, message);
        if (!"false".equals(blackWordsFilterResult.get("result"))) {
            return errorHandle(blackWordsFilterResult.get("code"), blackWordsFilterResult.get("message"));
        }

        /**
         * 6、通道过滤参数
         */
        Map<String, String> channelMessageParamsFilterResult = channelMessageFilter.filter(channelId, message);
        if (!"false".equals(channelMessageParamsFilterResult.get("result"))) {
            return errorHandle(channelMessageParamsFilterResult.get("code"), channelMessageParamsFilterResult.get("message"));
        }

        /**
         * 7、业务账号内容扩展参数过滤
         */
        Map<String, String> extendMessageParamsFilterResult = extendMessageParamsFilter.filter(filtersService, account, message);
        if (!"false".equals(extendMessageParamsFilterResult.get("result"))) {
            return errorHandle(extendMessageParamsFilterResult.get("code"), extendMessageParamsFilterResult.get("message"));
        }


        /**
         * 1、单手机号发送频率限制
         */
        Object phoneFrequencyLimit = entities.get("COMMON_SEND_FREQUENCY_LIMIT");
//        Map<String, String> phoneFrequencyLimitResult = phoneSendFrequencyLimitFilter.filter(filtersService, phoneFrequencyLimit, account, phone);
//        if (!"false".equals(phoneFrequencyLimitResult.get("result"))) {
//            return errorHandle(phoneFrequencyLimitResult.get("code"), phoneFrequencyLimitResult.get("message"));
//        }

        return success();
    }

    /**
     * 清除模版及内容中的 干扰正则表达式的特殊字符
     *
     * @param str
     * @return
     */
    public String clean(String str) {
        return str.replace("?", "").replace("+", "").replace("$", "").replace("^", "").replace("&", "").replace("[", "").replace("]", "").replace("-", "").replace("\\", "");
    }

    public Map<String, String> errorHandle(String code, String message) {
        Map<String, String> result = new HashMap<>();
        result.put("result", "true");
        result.put("code", code);
        result.put("message", message);
        return result;
    }

    public Map<String, String> success() {
        Map<String, String> result = new HashMap<>();
        result.put("result", "false");
        result.put("code", "0000");
        result.put("message", "操作成功！");
        return result;
    }

}
