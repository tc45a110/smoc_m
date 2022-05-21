package com.smoc.cloud.filters.service;

import com.smoc.cloud.common.filters.utils.RedisConstant;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.filters.utils.Initialize;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Set;
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
        Boolean isExistBlackList = filtersRedisDataService.contains(RedisConstant.FILTERS_CONFIG_SYSTEM_BLACK, phone);

        //洗白操作
        if (isExistBlackList) {
            //是否存在白名单中
            Boolean isExistWhiteList = filtersRedisDataService.contains(RedisConstant.FILTERS_CONFIG_SYSTEM_WHITE, phone);
            if (isExistWhiteList) {
                isExistBlackList = false;
            }
        }

        return isExistBlackList;
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
        return filtersRedisDataService.limiter(RedisConstant.FILTERS_TEMPORARY_ACCOUNT_NUMBER_LIMIT + account, mobile, maxBurst, tokens, seconds, times);
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
        return filtersRedisDataService.limiter(RedisConstant.FILTERS_TEMPORARY_SYSTEM_NUMBER_LIMIT + mobile, maxBurst, tokens, seconds, times);
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
        String key = RedisConstant.FILTERS_DAILY_LIMIT + account + ":" + carrier + ":" + dateFormat;
        log.info("[账号-运营商日限量]-key:{}", key);
        Object count = filtersRedisDataService.get(key);
        if (null == count) {
            return false;
        }
        Long existNum = new Long(count.toString());
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
     * List<String>
     *
     * @return
     */
    public Set<String> loadSensitiveWords() {
        Set<String> keyWords = Initialize.sensitiveWords;
        log.info("[加载敏感词................................]{}", keyWords);
        return keyWords;
    }

}
