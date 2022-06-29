package com.smoc.cloud.scheduler.service.filters.service.message;

import com.smoc.cloud.scheduler.service.filters.service.FiltersService;
import com.smoc.cloud.scheduler.batch.filters.service.utils.DFA.DfaSensitiveWordsFilter;
import com.smoc.cloud.scheduler.batch.filters.service.utils.DFA.FilterInitialize;
import com.smoc.cloud.scheduler.batch.filters.service.utils.DFA.WordsSensitiveFilter;
import com.smoc.cloud.scheduler.batch.filters.service.utils.FilterResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 全量内容过滤
 * 系统级短信内容 黑词、审核次、超级白词洗白、洗黑白词洗白、免审白词洗白、业务账号白词洗白、业务账号免审白词洗白
 */
@Slf4j
@Component
public class FullMessageFilter {

    /**
     * @param filtersService               业务服务
     * @param isBlackWordsFilter           是否过滤黑词
     * @param isCheckWordsFilter           是否过滤审核词
     * @param infoTypeSensitiveWordsFilter 行业
     * @param account                      业务账号
     * @param message                      短信内容
     * @return
     */
    public Map<String, String> filter(FiltersService filtersService, Object isBlackWordsFilter, Object isCheckWordsFilter, Object infoTypeSensitiveWordsFilter, String account, String message) {

        Map<String, String> result = new HashMap<>();

        //long start = System.currentTimeMillis();

        /**
         * 业务账号超级白词 (暂时屏蔽)
         */
//        DfaSensitiveWordsFilter dfaAccountSuperWhiteWordsFilter = new DfaSensitiveWordsFilter(FilterInitialize.accountSuperWhiteMap.get(account));
//        Boolean isExistAccountSuperWhiteWords = dfaAccountSuperWhiteWordsFilter.isContain(message, 1);
//        if (isExistAccountSuperWhiteWords) {
//            result.put("result", "false");
//            return result;
//        }

        /**
         * 先过滤超级白词,超级白词比较少（暂时屏蔽）
         */
//        Boolean isExistSystemSuperWhiteWords = FilterInitialize.superWhiteWordsFilter.isContain(message, 1);
//        if (isExistSystemSuperWhiteWords) {
//            result.put("result", "false");
//            return result;
//        }

        /**
         * 业务账号敏感词过滤
         */
        Map accountSensitiveMap = FilterInitialize.accountSensitiveMap.get(account);
        //log.info("[accountSensitiveMap]:{}",new Gson().toJson(accountSensitiveMap));
        DfaSensitiveWordsFilter dfaAccountSensitiveWordsFilter = new DfaSensitiveWordsFilter(accountSensitiveMap);
        Set<String> accountSensitiveWords = dfaAccountSensitiveWordsFilter.getSensitiveWords(message, 1);
        //log.info("[检测出来账号敏感词]：{}",accountSensitiveWords);
        //洗白过滤出来的敏感词 业务账号洗黑白词，清洗敏感词
        if (null != accountSensitiveWords && accountSensitiveWords.size() > 0) {
            List<Object> whiteAccountWords = filtersService.getWhiteWordsByAccountSensitive(account, accountSensitiveWords);
            int a = 0;
            Set<String> washWords = new HashSet<String>(); //洗掉的敏感词
            for (String sensitiveWords : accountSensitiveWords) {
                if (!ObjectUtils.isEmpty(whiteAccountWords.get(a))) {
                    Pattern pattern = Pattern.compile(whiteAccountWords.get(a).toString());
                    Matcher matcher = pattern.matcher(message);
                    if (matcher.find()) {
                        washWords.add(sensitiveWords);
                    }
                }
                a++;
            }
            for (String washWord : washWords) {
                accountSensitiveWords.remove(washWord);
            }
        }
        //洗白过滤出来的敏感词 系统洗黑白词，清洗敏感词
        if (null != accountSensitiveWords && accountSensitiveWords.size() > 0) {
            List<Object> whiteSystemWords = filtersService.getWhiteWordsBySystemSensitive(accountSensitiveWords);
            int s = 0;
            Set<String> washWords = new HashSet<String>(); //洗掉的敏感词
            for (String sensitiveWords : accountSensitiveWords) {
                if (!ObjectUtils.isEmpty(whiteSystemWords.get(s))) {
                    Pattern pattern = Pattern.compile(whiteSystemWords.get(s).toString());
                    Matcher matcher = pattern.matcher(message);
                    if (matcher.find()) {
                        washWords.add(sensitiveWords);
                    }
                }
                s++;
            }
            for (String washWord : washWords) {
                accountSensitiveWords.remove(washWord);
            }

        }
        if (null != accountSensitiveWords && accountSensitiveWords.size() > 0) {
            result.put("result", "true");
            result.put("code", FilterResponseCode.MESSAGE_ACCOUNT_SENSITIVE_FILTER.getCode());
            result.put("message", "内容中包含业务账号敏感词" + accountSensitiveWords);
            return result;
        }

        /**
         * 系统敏感词过滤
         */
        if (!StringUtils.isEmpty(isBlackWordsFilter) && "1".equals(isBlackWordsFilter.toString())) {
            WordsSensitiveFilter sensitiveWordsFilter = FilterInitialize.sensitiveWordsFilter;
            Set<String> systemSensitiveWords = sensitiveWordsFilter.getSensitiveWords(message, 1);
            //log.info("[检测出来系统敏感词]：{}",systemSensitiveWords);
            //洗白过滤出来的敏感词 业务账号洗黑白词，清洗敏感词
            if (null != systemSensitiveWords && systemSensitiveWords.size() > 0) {
                List<Object> whiteAccountWords = filtersService.getWhiteWordsByAccountSensitive(account, systemSensitiveWords);
                int a = 0;
                Set<String> washWords = new HashSet<String>(); //洗掉的敏感词
                for (String sensitiveWords : systemSensitiveWords) {
                    if (!ObjectUtils.isEmpty(whiteAccountWords.get(a))) {
                        Pattern pattern = Pattern.compile(whiteAccountWords.get(a).toString());
                        Matcher matcher = pattern.matcher(message);
                        if (matcher.find()) {
                            washWords.add(sensitiveWords);
                        }
                    }
                    a++;
                }
                for (String washWord : washWords) {
                    systemSensitiveWords.remove(washWord);
                }
            }
            //log.info("[账号洗黑白词洗白]：{}",systemSensitiveWords);
            //洗白过滤出来的敏感词 系统洗黑白词，清洗敏感词
            if (null != systemSensitiveWords && systemSensitiveWords.size() > 0) {
                List<Object> whiteSystemWords = filtersService.getWhiteWordsBySystemSensitive(systemSensitiveWords);
                int s = 0;
                Set<String> washWords = new HashSet<String>(); //洗掉的敏感词
                for (String sensitiveWords : systemSensitiveWords) {
                    if (!ObjectUtils.isEmpty(whiteSystemWords.get(s))) {
                        Pattern pattern = Pattern.compile(whiteSystemWords.get(s).toString());
                        Matcher matcher = pattern.matcher(message);
                        if (matcher.find()) {
                            washWords.add(sensitiveWords);
                        }
                    }
                    s++;
                }
                for (String washWord : washWords) {
                    systemSensitiveWords.remove(washWord);
                }
            }
            //log.info("[账户洗黑白词洗白]：{}",systemSensitiveWords);
            if (null != systemSensitiveWords && systemSensitiveWords.size() > 0) {
                result.put("result", "true");
                result.put("code", FilterResponseCode.MESSAGE_SENSITIVE_FILTER.getCode());
                result.put("message", "内容中包含系统敏感词" + systemSensitiveWords);
                return result;
            }
        }

        /**
         * 行业敏感词
         */
        if (!StringUtils.isEmpty(infoTypeSensitiveWordsFilter)) {
            DfaSensitiveWordsFilter dfaSensitiveWordsFilter = new DfaSensitiveWordsFilter(FilterInitialize.infoTypeSensitiveMap.get(infoTypeSensitiveWordsFilter));
            Set<String> infoSensitiveWords = dfaSensitiveWordsFilter.getSensitiveWords(message, 1);
            //洗白过滤出来的行业敏感词 业务账号洗黑白词，清洗敏感词
            if (null != infoSensitiveWords && infoSensitiveWords.size() > 0) {
                List<Object> whiteAccountWords = filtersService.getWhiteWordsByAccountSensitive(account, infoSensitiveWords);
                int a = 0;
                Set<String> washWords = new HashSet<String>(); //洗掉的敏感词
                for (String sensitiveWords : infoSensitiveWords) {
                    if (!ObjectUtils.isEmpty(whiteAccountWords.get(a))) {
                        Pattern pattern = Pattern.compile(whiteAccountWords.get(a).toString());
                        Matcher matcher = pattern.matcher(message);
                        if (matcher.find()) {
                            washWords.add(sensitiveWords);
                        }
                    }
                    a++;
                }
                for (String washWord : washWords) {
                    infoSensitiveWords.remove(washWord);
                }
            }
            //洗白过滤出来的行业敏感词  系统洗黑白词，清洗敏感词
            if (null != infoSensitiveWords && infoSensitiveWords.size() > 0) {
                List<Object> whiteSystemWords = filtersService.getWhiteWordsBySystemSensitive(infoSensitiveWords);
                int s = 0;
                Set<String> washWords = new HashSet<String>(); //洗掉的敏感词
                for (String sensitiveWords : infoSensitiveWords) {
                    if (!ObjectUtils.isEmpty(whiteSystemWords.get(s))) {
                        Pattern pattern = Pattern.compile(whiteSystemWords.get(s).toString());
                        Matcher matcher = pattern.matcher(message);
                        if (matcher.find()) {
                            washWords.add(sensitiveWords);
                        }
                    }
                    s++;
                }
                for (String washWord : washWords) {
                    infoSensitiveWords.remove(washWord);
                }
            }
            if (null != infoSensitiveWords && infoSensitiveWords.size() > 0) {
                result.put("result", "true");
                result.put("code", FilterResponseCode.MESSAGE_INFO_TYPE_SENSITIVE_FILTER.getCode());
                result.put("message", "内容中包含行业敏感词" + infoSensitiveWords);
                return result;
            }
        }

        /**
         * 业务账号审核词
         */
        DfaSensitiveWordsFilter dfaAccountCheckWordsFilter = new DfaSensitiveWordsFilter(FilterInitialize.accountCheckMap.get(account));
        Set<String> accountCheckWords = dfaAccountCheckWordsFilter.getSensitiveWords(message, 1);
        //洗白过滤出来的审核词 用业务账号免审白词，洗白
        if (null != accountCheckWords && accountCheckWords.size() > 0) {
            List<Object> whiteAccountWords = filtersService.getWhiteWordsByAccountCheck(account, accountCheckWords);
            int c = 0;
            Set<String> washWords = new HashSet<String>(); //洗掉的敏感词
            for (String checkWords : accountCheckWords) {
                if (!ObjectUtils.isEmpty(whiteAccountWords.get(c))) {
                    Pattern pattern = Pattern.compile(whiteAccountWords.get(c).toString());
                    Matcher matcher = pattern.matcher(message);
                    if (matcher.find()) {
                        washWords.add(checkWords);
                    }
                }
                c++;
            }
            for (String washWord : washWords) {
                accountCheckWords.remove(washWord);
            }
        }
        //洗白过滤出来的审核词 用系统免审白词，洗白
        if (null != accountCheckWords && accountCheckWords.size() > 0) {
            List<Object> whiteSystemWords = filtersService.getWhiteWordsBySystemCheck(accountCheckWords);
            int d = 0;
            Set<String> washWords = new HashSet<String>(); //洗掉的敏感词
            for (String checkWords : accountCheckWords) {
                if (!ObjectUtils.isEmpty(whiteSystemWords.get(d))) {
                    Pattern pattern = Pattern.compile(whiteSystemWords.get(d).toString());
                    Matcher matcher = pattern.matcher(message);
                    if (matcher.find()) {
                        washWords.add(checkWords);
                    }
                }
                d++;
            }
            for (String washWord : washWords) {
                accountCheckWords.remove(washWord);
            }
        }
        if (null != accountCheckWords && accountCheckWords.size() > 0) {
            result.put("result", "true");
            result.put("code", FilterResponseCode.MESSAGE_ACCOUNT_CHECK_FILTER.getCode());
            result.put("message", "内容中包含业务账号审核词" + accountCheckWords);
            return result;
        }

        /**
         * 系统审核词过滤
         */
        if (!StringUtils.isEmpty(isCheckWordsFilter) && "1".equals(isCheckWordsFilter.toString())) {

            Set<String> systemCheckWords = FilterInitialize.checkWordsFilter.getCheckWords(message, 1);
            //包含审核词 用业务账号免审白词，洗白
            if (null != systemCheckWords && systemCheckWords.size() > 0) {
                List<Object> whiteAccountWords = filtersService.getWhiteWordsByAccountCheck(account, systemCheckWords);
                int c = 0;
                Set<String> washWords = new HashSet<String>(); //洗掉的敏感词
                for (String checkWords : systemCheckWords) {
                    if (!ObjectUtils.isEmpty(whiteAccountWords.get(c))) {
                        Pattern pattern = Pattern.compile(whiteAccountWords.get(c).toString());
                        Matcher matcher = pattern.matcher(message);
                        if (matcher.find()) {
                            washWords.add(checkWords);
                        }
                    }
                    c++;
                }
                for (String washWord : washWords) {
                    systemCheckWords.remove(washWord);
                }
            }
            //包含审核词 用系统免审白词，洗白
            if (null != systemCheckWords && systemCheckWords.size() > 0) {
                List<Object> whiteSystemWords = filtersService.getWhiteWordsBySystemCheck(systemCheckWords);
                int d = 0;
                Set<String> washWords = new HashSet<String>(); //洗掉的敏感词
                for (String checkWords : systemCheckWords) {
                    if (!ObjectUtils.isEmpty(whiteSystemWords.get(d))) {
                        Pattern pattern = Pattern.compile(whiteSystemWords.get(d).toString());
                        Matcher matcher = pattern.matcher(message);
                        if (matcher.find()) {
                            washWords.add(checkWords);
                        }
                    }
                    d++;
                }
                for (String washWord : washWords) {
                    systemCheckWords.remove(washWord);
                }
            }
            if (null != systemCheckWords && systemCheckWords.size() > 0) {
                result.put("result", "true");
                result.put("code", FilterResponseCode.MESSAGE_CHECK_FILTER.getCode());
                result.put("message", "内容中包含系统审核词" + systemCheckWords);
                return result;
            }

        }

//        log.info("[系统正则]:{}",RedisConstant.FILTERS_CONFIG_SYSTEM_WORDS_WHITE + "regular");
//        Object regularPatten = filtersService.get(RedisConstant.FILTERS_CONFIG_SYSTEM_WORDS_WHITE + "regular");
//        if (!StringUtils.isEmpty(regularPatten)) {
//            log.info("[内容_系统正则]{}:{}", regularPatten);
//            if (filtersService.validator(regularPatten.toString(), account)) {
//                result.put("result", "false");
//            } else {
//                result.put("result", "true");
//                result.put("code", FilterResponseCode.MESSAGE_REGULAR_FILTER.getCode());
//                result.put("message", FilterResponseCode.MESSAGE_REGULAR_FILTER.getMessage());
//            }
//        }

        //long end = System.currentTimeMillis();
        //log.info("[内容过滤]：耗时{}毫秒", end - start);
        result.put("result", "false");
        return result;

    }


}
