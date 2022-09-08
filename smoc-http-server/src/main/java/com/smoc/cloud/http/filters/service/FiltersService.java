package com.smoc.cloud.http.filters.service;

import com.smoc.cloud.common.filters.utils.RedisConstant;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.http.filters.utils.DFA.DFAUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class FiltersService {

    @Autowired
    private FiltersRedisDataService filtersRedisDataService;

    /**
     * 手机号系统黑名单，白名单过滤(进行洗白操作)
     *
     * @param phone 手机号
     * @return true 表示存在于黑名单中  false 表示不存在黑名单中
     */
    public Boolean systemNumberBlackListFilter(String phone) {

        //是否存在于系统黑名单
        Boolean isExistBlackList = filtersRedisDataService.isMember(RedisConstant.FILTERS_CONFIG_SYSTEM_BLACK, phone);
        //洗白操作
        if (isExistBlackList) {
            //是否存在白名单中
            Boolean isExistWhiteList = filtersRedisDataService.isMember(RedisConstant.FILTERS_CONFIG_SYSTEM_WHITE, phone);
            if (isExistWhiteList) {
                isExistBlackList = false;
            }
        }

        return isExistBlackList;
    }

    /**
     * set 中是否存在值
     * @param redisKey
     * @param value
     * @return
     */
    public Boolean isSetMember(String redisKey,String value){
        Boolean isExist = filtersRedisDataService.isMember(redisKey, value);
        return isExist;
    }

    /**
     * 获取map 中的key值
     * @param redisKey
     * @param key
     * @return
     */
    public Object getMapValue(String redisKey,String key){
        Object object = filtersRedisDataService.getMapValue(redisKey,key);
        //log.info("[object]:{}",object);
        return object;
    }

    /**
     * 手机号本地黑名单，白名单过滤(进行洗白操作)
     *
     * @param phone 手机号
     * @return true 表示存在于黑名单中  false 表示不存在黑名单中
     */
    public Boolean localNumberBlackListFilter(String phone) {

        //是否存在于本地黑名单
        Boolean isExistBlackList = false;

        return isExistBlackList;
    }

    /**
     * 手机号第三方黑名单，白名单过滤(进行洗白操作)
     *
     * @param phone 手机号
     * @return true 表示存在于黑名单中  false 表示不存在黑名单中
     */
    public Boolean thirdNumberBlackListFilter(String phone) {

        //是否存在于第三方黑名单
        Boolean isExistBlackList = false;

        return isExistBlackList;
    }

    /**
     * 根据key 获取字符串
     *
     * @param redisKey
     * @return
     */
    public Object get(String redisKey) {
        return filtersRedisDataService.get(redisKey);
    }


    /**
     * 按账号 检测手机号发送频率限制
     *
     * @param account  限流的账号
     * @param mobile   限流的手机号
     * @param maxBurst 初始化容量
     * @param tokens   每seconds 添加的容量
     * @param seconds  限流的时间间隔
     * @param times    本次要发送的条数
     * @return 返回true 表示，可以继续发送，返回false表示已触发限流
     */
    public Boolean phoneFrequencyLimiterByAccount(String account, String mobile, int maxBurst, int tokens, int seconds, int times) {
        return filtersRedisDataService.limiter(RedisConstant.FILTERS_TEMPORARY_LIMIT_ACCOUNT_NUMBER + account, mobile, maxBurst, tokens, seconds, times);
    }

    /**
     * 按系统手机号发送限流
     *
     * @param mobile   限流的手机号
     * @param maxBurst 初始化容量
     * @param tokens   每seconds 添加的容量
     * @param seconds  限流的时间间隔
     * @param times    本次要发送的条数
     * @return 返回true 表示，可以继续发送，返回false表示已触发限流
     */
    public Boolean phoneFrequencyLimiterBySystem(String mobile, int maxBurst, int tokens, int seconds, int times) {
        return filtersRedisDataService.limiter(RedisConstant.FILTERS_TEMPORARY_LIMIT_SYSTEM_NUMBER + mobile, maxBurst, tokens, seconds, times);
    }

    /**
     * 记录 业务账号某运营商的日限量
     *
     * @param account 业务账号
     * @param carrier 运营商
     * @param times   增加值
     * @return java.lang.Long
     * @description 给 map中指定字段的整数值加上 long型增量 increment
     */
    public Long incrementAccountDailyLimit(String account, String carrier, long times) {
        String dateFormat = DateTimeUtils.currentDate(new Date());
        String key = RedisConstant.FILTERS_TEMPORARY_LIMIT_FLOW_CARRIER_DATE + dateFormat;
        return filtersRedisDataService.incrementLong(key, account + "_" + carrier, times);
    }

    /**
     * 判断 业务账号某运营商的日限量
     *
     * @param account    业务账号
     * @param carrier    运营商
     * @param dailyLimit 日限量
     * @param times      计量次数
     * @return 返回false 表示没有触发到日限量
     */
    public Boolean accountDailyLimit(String account, String carrier, Long dailyLimit, Integer times) {
        String dateFormat = DateTimeUtils.currentDate(new Date());
        String key = RedisConstant.FILTERS_TEMPORARY_LIMIT_FLOW_CARRIER_DATE + dateFormat;
//        log.info("[账号-运营商日限量]-key:{}", key);
        Object count = filtersRedisDataService.getMapValue(key, account + "_" + carrier);
        if (null == count) {
            return false;
        }
        Long existNum = new Long(count.toString());
//        log.info("[existNum]:{}",existNum);
        return (dailyLimit < (existNum + times));

    }

    /**
     * 校验，只返回校验结果 后续会调整
     *
     * @param pattern
     * @param content
     * @return 存在则返回true
     */
    public Boolean validator(String pattern, String content) {
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(content);
        if (matcher.find()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * HashEntries
     *
     * @param redisKey
     * @return
     */
    public Map<Object, Object> getEntries(String redisKey) {
        Map<Object, Object> entries = filtersRedisDataService.hEntries(redisKey);
        return entries;
    }

    /**
     * 查询系统黑名单
     * List<String>
     *
     * @return
     */
    public Set<String> getSystemBlackList() {
        long start = System.currentTimeMillis();
        Set<String> sensitiveWords = filtersRedisDataService.sget(RedisConstant.FILTERS_CONFIG_SYSTEM_BLACK);
        long end = System.currentTimeMillis();
        if (null != sensitiveWords) {
            log.info("[加载系统黑名单]：{}条，耗时：{}毫秒", sensitiveWords.size(), end - start);
        }
        return sensitiveWords;
    }

    /**
     * 查询敏感词
     * List<String>
     *
     * @return
     */
    public Set<String> getSensitiveWords() {
        long start = System.currentTimeMillis();
        Set<String> sensitiveWords = filtersRedisDataService.sget(RedisConstant.FILTERS_CONFIG_SYSTEM_WORDS_SENSITIVE);
        long end = System.currentTimeMillis();
        if (null != sensitiveWords) {
            log.info("[加载系统敏感词]：{}条，耗时：{}毫秒", sensitiveWords.size(), end - start);
        }
        return sensitiveWords;
    }

    /**
     * 查询审核词
     * List<String>
     *
     * @return
     */
    public Set<String> getCheckWords() {
        long start = System.currentTimeMillis();
        Set<String> checkWords = filtersRedisDataService.sget(RedisConstant.FILTERS_CONFIG_SYSTEM_WORDS_CHECK);
        long end = System.currentTimeMillis();
        if (null != checkWords) {
            log.info("[加载系统审核词]：{}条，耗时：{}毫秒", checkWords.size(), end - start);
        }
        return checkWords;
    }

    /**
     * 查询审超级白词
     * List<String>
     *
     * @return
     */
    public Set<String> getSuperWhiteWords() {
        long start = System.currentTimeMillis();
        Set<String> checkWords = filtersRedisDataService.sget(RedisConstant.FILTERS_CONFIG_SYSTEM_WORDS_WHITE_SUPER);
        long end = System.currentTimeMillis();
        if (null != checkWords) {
            log.info("[加载系统超级白词]：{}条，耗时：{}毫秒", checkWords.size(), end - start);
        }
        return checkWords;
    }

    /**
     * 通过敏感词，获取洗黑白词(系统洗黑白词)
     *
     * @param set
     * @return
     */
    public List<Object> getWhiteWordsBySystemSensitive(Set<String> set) {
        List<Object> whiteWords = filtersRedisDataService.hashGetBatch(RedisConstant.FILTERS_CONFIG_SYSTEM_WORDS_WHITE_SENSITIVE, set);
        return whiteWords;
    }

    /**
     * 通过敏感词，获取洗黑白词(账号洗黑白词)
     *
     * @param set
     * @return
     */
    public List<Object> getWhiteWordsByAccountSensitive(String account, Set<String> set) {
        List<Object> whiteWords = filtersRedisDataService.hashGetBatch(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_WHITE_SENSITIVE + account, set);
        return whiteWords;
    }


    /**
     * 通过审核词，获取免审白词(系统免审白词)
     *
     * @param set
     * @return
     */
    public List<Object> getWhiteWordsBySystemCheck(Set<String> set) {
        List<Object> whiteWords = filtersRedisDataService.hashGetBatch(RedisConstant.FILTERS_CONFIG_SYSTEM_WORDS_WHITE_NO_CHECK, set);
        return whiteWords;
    }

    /**
     * 通过审核词，获取免审白词(业务账号免审白词)
     *
     * @param set
     * @return
     */
    public List<Object> getWhiteWordsByAccountCheck(String account, Set<String> set) {
        List<Object> whiteWords = filtersRedisDataService.hashGetBatch(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_WHITE_NO_CHECK + account, set);
        return whiteWords;
    }

    /**
     * 初始化 行业敏感词
     *
     * @return
     */
    public Map<String, Map> getInfoTypeSensitiveWords() {
        long start = System.currentTimeMillis();
        Map<String, Map> infoTypeSensitiveMap = new HashMap<>();
        Set<String> infoTypes = filtersRedisDataService.sget(RedisConstant.FILTERS_CONFIG_SYSTEM_WORDS_INFO_TYPE);
        //log.info("[infoTypes]:{}",infoTypes);
        if (null != infoTypes && infoTypes.size() > 0) {
            for (String infoType : infoTypes) {
                Set<String> infoTypeSensitiveWords = filtersRedisDataService.sget(RedisConstant.FILTERS_CONFIG_SYSTEM_WORDS_INFO_TYPE_SENSITIVE + infoType);
                infoTypeSensitiveMap.put(infoType, DFAUtils.buildDFADataModel(infoTypeSensitiveWords));
            }
        }
        long end = System.currentTimeMillis();
        log.info("[加载行业敏感词]：耗时：{}毫秒", end - start);
        //log.info("[infoTypeSensitiveMap]:{}", new Gson().toJson(infoTypeSensitiveMap));
        return infoTypeSensitiveMap;
    }

    /**
     * 初始化 通道敏感词
     *
     * @return
     */
    public Map<String, Map> getChannelSensitiveWords() {
        long start = System.currentTimeMillis();
        Map<String, Map> channelSensitiveMap = new HashMap<>();
        Set<String> accounts = filtersRedisDataService.sget(RedisConstant.FILTERS_CONFIG_CHANNEL_SENSITIVE + "list");
        if (null != accounts && accounts.size() > 0) {
            for (String account : accounts) {
                Set<String> accountSensitiveWords = filtersRedisDataService.sget(RedisConstant.FILTERS_CONFIG_CHANNEL_SENSITIVE + account);
                log.info("[加载通道敏感词]：耗时：{}毫秒", accountSensitiveWords);
                channelSensitiveMap.put(account, DFAUtils.buildDFADataModel(accountSensitiveWords));
            }
        }
        long end = System.currentTimeMillis();
        log.info("[加载通道敏感词]：耗时：{}毫秒", end - start);
        return channelSensitiveMap;
    }

    /**
     * 初始化 业务账号敏感词
     *
     * @return
     */
    public Map<String, Map> getAccountSensitiveWords() {
        long start = System.currentTimeMillis();
        Map<String, Map> accountSensitiveMap = new HashMap<>();
        Set<String> accounts = filtersRedisDataService.sget(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_SENSITIVE + "list");
        //log.info("[accounts]:{}",accounts);
        if (null != accounts && accounts.size() > 0) {
            for (String account : accounts) {
                Set<String> accountSensitiveWords = filtersRedisDataService.sget(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_SENSITIVE + account);
                accountSensitiveMap.put(account, DFAUtils.buildDFADataModel(accountSensitiveWords));
            }
        }
        long end = System.currentTimeMillis();
        log.info("[加载业务账号敏感词]：耗时：{}毫秒", end - start);
        //log.info("[accountSensitiveMap]:{}", new Gson().toJson(accountSensitiveMap));
        return accountSensitiveMap;
    }

    /**
     * 初始化 业务账号审核词
     *
     * @return
     */
    public Map<String, Map> getAccountCheckWords() {
        long start = System.currentTimeMillis();
        Map<String, Map> accountCheckMap = new HashMap<>();
        Set<String> accounts = filtersRedisDataService.sget(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_CHECK + "list");
        //log.info("[accounts]:{}",accounts);
        if (null != accounts && accounts.size() > 0) {
            for (String account : accounts) {
                Set<String> accountCheckWords = filtersRedisDataService.sget(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_CHECK + account);
                accountCheckMap.put(account, DFAUtils.buildDFADataModel(accountCheckWords));
            }
        }
        long end = System.currentTimeMillis();
        log.info("[加载业务账号审核词]：耗时：{}毫秒", end - start);
        //log.info("[accountCheckMap]:{}",new Gson().toJson(accountCheckMap));
        return accountCheckMap;
    }

    /**
     * 初始化 业务账号超级白词
     *
     * @return
     */
    public Map<String, Map> getAccountSuperWhiteWords() {
        long start = System.currentTimeMillis();
        Map<String, Map> accountSuperWhiteMap = new HashMap<>();
        Set<String> accounts = filtersRedisDataService.sget(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_WHITE_SUPER + "list");
        //log.info("[accounts]:{}",accounts);
        if (null != accounts && accounts.size() > 0) {
            for (String account : accounts) {
                Set<String> accountSuperWhiteWords = filtersRedisDataService.sget(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_WHITE_SUPER + account);
                accountSuperWhiteMap.put(account, DFAUtils.buildDFADataModel(accountSuperWhiteWords));
            }
        }
        long end = System.currentTimeMillis();
        log.info("[加载业务账号超级白词]：耗时：{}毫秒", end - start);
        //log.info("[accountSuperWhiteMap]:{}",new Gson().toJson(accountSuperWhiteMap));
        return accountSuperWhiteMap;
    }

    /**
     * 初始化 账号固定模版
     *
     * @return
     */
    public Map<String, String> getAccountFixedTemplates() {
        long start = System.currentTimeMillis();
        Map<String, String> accountFixedTemplateMap = new HashMap<>();
        Set<String> accounts = filtersRedisDataService.sget(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_TEMPLATE_FIXED + "list");
        //log.info("[accounts]:{}",accounts);
        if (null != accounts && accounts.size() > 0) {
            for (String account : accounts) {
                Object sign = filtersRedisDataService.get(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_TEMPLATE_FIXED + account);
                accountFixedTemplateMap.put(account, sign.toString());
            }
        }
        long end = System.currentTimeMillis();
        log.info("[加载业务账号固定模版]：耗时：{}毫秒", end - start);
        //log.info("[accountSignTemplateMap]:{}",new Gson().toJson(accountFixedTemplateMap));
        return accountFixedTemplateMap;
    }

    /**
     * 初始化 账号签名模版
     *
     * @return
     */
    public Map<String, String> getAccountSignTemplates() {
        long start = System.currentTimeMillis();
        Map<String, String> accountSignTemplateMap = new HashMap<>();
        Set<String> accounts = filtersRedisDataService.sget(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_TEMPLATE_SIGN + "list");
        //log.info("[accounts]:{}",accounts);
        if (null != accounts && accounts.size() > 0) {
            for (String account : accounts) {
                Object sign = filtersRedisDataService.get(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_TEMPLATE_SIGN + account);
                accountSignTemplateMap.put(account, sign.toString());
            }
        }
        long end = System.currentTimeMillis();
        log.info("[加载业务账号签名模版]：耗时：{}毫秒", end - start);
        //log.info("[accountSignTemplateMap]:{}",new Gson().toJson(accountSignTemplateMap));
        return accountSignTemplateMap;
    }

    /**
     * 初始化 账号签名变量模版，匹配模版后，要继续进行下一步过滤
     *
     * @return
     */
    public Map<String, String> getAccountFilterVariableTemplates() {
        long start = System.currentTimeMillis();
        Map<String, String> accountFilterVariableTemplatesMap = new HashMap<>();
        Set<String> accounts = filtersRedisDataService.sget(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_TEMPLATE_VARIABLE_CMPP + "filter:list");
        //log.info("[accounts]:{}",accounts);
        if (null != accounts && accounts.size() > 0) {
            for (String account : accounts) {
                Object sign = filtersRedisDataService.get(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_TEMPLATE_VARIABLE_CMPP + "filter:" + account);
                accountFilterVariableTemplatesMap.put(account, sign.toString());
            }
        }
        long end = System.currentTimeMillis();
        log.info("[加载业务账号变量模版-FILTER]：耗时：{}毫秒", end - start);
        //log.info("[accountFilterVariableTemplatesMap]:{}",new Gson().toJson(accountFilterVariableTemplatesMap));
        return accountFilterVariableTemplatesMap;
    }

    /**
     * 初始化 账号签名变量模版，匹配模版后，要继续进行下一步过滤
     *
     * @return
     */
    public Map<String, String> getAccountNoFilterVariableTemplates() {
        long start = System.currentTimeMillis();
        Map<String, String> accountNoFilterVariableTemplatesMap = new HashMap<>();
        Set<String> accounts = filtersRedisDataService.sget(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_TEMPLATE_VARIABLE_CMPP + "no_filter:list");
        //log.info("[accounts]:{}",accounts);
        if (null != accounts && accounts.size() > 0) {
            for (String account : accounts) {
                Object sign = filtersRedisDataService.get(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_TEMPLATE_VARIABLE_CMPP + "no_filter:" + account);
                accountNoFilterVariableTemplatesMap.put(account, sign.toString());
            }
        }
        long end = System.currentTimeMillis();
        log.info("[加载业务账号变量模版-NO_FILTER]：耗时：{}毫秒", end - start);
        //log.info("[accountNoFilterVariableTemplatesMap]:{}",new Gson().toJson(accountNoFilterVariableTemplatesMap));
        return accountNoFilterVariableTemplatesMap;
    }

}
