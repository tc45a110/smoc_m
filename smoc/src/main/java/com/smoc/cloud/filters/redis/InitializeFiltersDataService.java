package com.smoc.cloud.filters.redis;

import com.google.gson.Gson;
import com.smoc.cloud.common.filters.utils.InitializeFiltersData;
import com.smoc.cloud.common.filters.utils.RedisConstant;
import com.smoc.cloud.filter.entity.FilterBlackList;
import com.smoc.cloud.filter.entity.FilterWhiteList;
import com.smoc.cloud.filter.repository.BlackRepository;
import com.smoc.cloud.filter.repository.WhiteRepository;
import com.smoc.cloud.parameter.entity.ParameterExtendFiltersValue;
import com.smoc.cloud.parameter.repository.ParameterExtendFiltersValueRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 初始化 系统过滤服务数据
 */
@Slf4j
@Service
public class InitializeFiltersDataService {

    @Resource
    private BlackRepository blackRepository;
    @Resource
    private WhiteRepository whiteRepository;

    @Autowired
    private ParameterExtendFiltersValueRepository parameterExtendFiltersValueRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    public void initialize(InitializeFiltersData initializeFiltersData) {

        //系统黑名单
        if ("1".equals(initializeFiltersData.getReloadBlackList())) {
            this.deleteByKey(RedisConstant.FILTERS_CONFIG_SYSTEM_BLACK);
            this.initializeBlackList();
        }
        //系统白名单
        if ("1".equals(initializeFiltersData.getReloadWhiteList())) {
            this.deleteByKey(RedisConstant.FILTERS_CONFIG_SYSTEM_WHITE);
            this.initializeWhiteList();
        }
        //系统白名单
        if ("1".equals(initializeFiltersData.getReloadAccountFilterParams())) {
            this.batchDeleteByPatten(RedisConstant.FILTERS_CONFIG_ACCOUNT);
            this.initializeAccountFilterParams();
        }
    }


    public void initializeBlackList() {
        List<FilterBlackList> filterBlackListList = blackRepository.findAll();
        if (null != filterBlackListList && filterBlackListList.size() > 0) {
            long start = System.currentTimeMillis();
            log.info("加载系统黑名单start：{}", start);
            Map<String, String> maps = filterBlackListList.stream().collect(Collectors.toMap(FilterBlackList::getMobile, FilterBlackList::getMobile, (key1, key2) -> key2));
            this.multiSaveSet(maps, RedisConstant.FILTERS_CONFIG_SYSTEM_BLACK);
            long end = System.currentTimeMillis();
            log.info("加载系统黑名单  end：{}", end);
        }
    }

    public void initializeWhiteList() {
        List<FilterWhiteList> filterWhiteList = whiteRepository.findAll();
        if (null != filterWhiteList && filterWhiteList.size() > 0) {
            long start = System.currentTimeMillis();
            log.info("加载系统白名单start：{}", start);
            Map<String, String> maps = filterWhiteList.stream().collect(Collectors.toMap(FilterWhiteList::getMobile, FilterWhiteList::getMobile, (key1, key2) -> key2));
            this.multiSaveSet(maps, RedisConstant.FILTERS_CONFIG_SYSTEM_WHITE);
            long end = System.currentTimeMillis();
            log.info("加载系统白名单  end：{}", end);
        }
    }

    /**
     * 加载黑白名单
     *
     * @param source
     * @param prefix
     */
    public void multiSaveSet(Map<String, String> source, String prefix) {
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            // 这里逻辑简单不会抛异常
            // 否则需要加上try...catch...finally防止链接未正常关闭 造成泄漏
            connection.openPipeline();
            source.forEach((key, value) -> {
                // hset zset都是可以用的，但是要序列化
                connection.sAdd(RedisSerializer.string().serialize(prefix),
                        RedisSerializer.string().serialize(value));
            });
            connection.close();
            // executePipelined源码要求RedisCallback必须返回null，否则抛异常
            return null;
        });
    }

    /**
     * 业务账号过滤参数
     */
    public void initializeAccountFilterParams() {
        List<ParameterExtendFiltersValue> params = this.parameterExtendFiltersValueRepository.findParameterExtendFiltersValueByBusinessType("BUSINESS_ACCOUNT_FILTER");
        if (null != params && params.size() > 0) {
            Map<String, Map<String, Object>> commonMap = new HashMap<>();
            Map<String, String> numberMap = new HashMap<>();
            Map<String, String> messageMap = new HashMap<>();
            for (ParameterExtendFiltersValue param : params) {

                if (param.getParamKey().startsWith("COMMON_")) {
                    Map<String, Object> map = commonMap.get(param.getBusinessId());
                    if (null == map) {
                        map = new HashMap<>();
                        map.put(param.getParamKey(), param.getParamValue());
                    } else {
                        map.put(param.getParamKey(), param.getParamValue());
                    }
                    commonMap.put(param.getBusinessId(), map);
                    continue;
                }
                if (param.getParamKey().startsWith("NUMBER_BLACK_")) {
                    String blackNumberPatten = numberMap.get("black:" + param.getBusinessId());
                    if (StringUtils.isEmpty(blackNumberPatten)) {
                        blackNumberPatten = param.getParamValue();
                    } else {
                        blackNumberPatten += "|" + param.getParamValue();
                    }
                    numberMap.put("black:" + param.getBusinessId(), blackNumberPatten);
                    continue;
                }
                if (param.getParamKey().startsWith("NUMBER_WHITE_")) {
                    String whiteNumberPatten = numberMap.get("white:" + param.getBusinessId());
                    if (StringUtils.isEmpty(whiteNumberPatten)) {
                        whiteNumberPatten = param.getParamValue();
                    } else {
                        whiteNumberPatten += "|" + param.getParamValue();
                    }
                    numberMap.put("white:" + param.getBusinessId(), whiteNumberPatten);
                    continue;

                }
                if (param.getParamKey().startsWith("NUMBER_REGULAR_")) {
                    String regularNumberPatten = numberMap.get("regular:" + param.getBusinessId());
                    if (StringUtils.isEmpty(regularNumberPatten)) {
                        regularNumberPatten = param.getParamValue();
                    } else {
                        regularNumberPatten += "|" + param.getParamValue();
                    }
                    numberMap.put("regular:" + param.getBusinessId(), regularNumberPatten);
                    continue;
                }
                if (param.getParamKey().startsWith("MESSAGE_BLACK_")) {
                    String blackMessagePatten = messageMap.get("black:" + param.getBusinessId());
                    if (StringUtils.isEmpty(blackMessagePatten)) {
                        blackMessagePatten = param.getParamValue();
                    } else {
                        blackMessagePatten += "|" + param.getParamValue();
                    }
                    messageMap.put("black:" + param.getBusinessId(), blackMessagePatten);
                    continue;
                }
                if (param.getParamKey().startsWith("MESSAGE_WHITE_")) {
                    String whiteMessagePatten = messageMap.get("white:" + param.getBusinessId());
                    if (StringUtils.isEmpty(whiteMessagePatten)) {
                        whiteMessagePatten = param.getParamValue();
                    } else {
                        whiteMessagePatten += "|" + param.getParamValue();
                    }
                    messageMap.put("white:" + param.getBusinessId(), whiteMessagePatten);
                    continue;
                }
                if (param.getParamKey().startsWith("MESSAGE_REGULAR_")) {
                    String regularMessagePatten = messageMap.get("regular:" + param.getBusinessId());
                    if (StringUtils.isEmpty(regularMessagePatten)) {
                        regularMessagePatten = param.getParamValue();
                    } else {
                        regularMessagePatten += "|" + param.getParamValue();
                    }
                    messageMap.put("regular:" + param.getBusinessId(), regularMessagePatten);
                    continue;
                }
            }
            long start = System.currentTimeMillis();
            log.info("加载业务账号过滤参数start：{}", start);
            this.multiSaveHash(commonMap, RedisConstant.FILTERS_CONFIG_ACCOUNT_COMMON);
            this.multiSaveFilters(numberMap, RedisConstant.FILTERS_CONFIG_ACCOUNT_NUMBER);
            this.multiSaveFilters(messageMap, RedisConstant.FILTERS_CONFIG_ACCOUNT_MESSAGE);
            long end = System.currentTimeMillis();
            log.info("加载业务账号过滤参数  end：{}", end);
        }
    }

    /**
     * @param source
     * @param prefix
     */
    public void multiSaveFilters(Map<String, String> source, String prefix) {
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            // 这里逻辑简单不会抛异常
            // 否则需要加上try...catch...finally防止链接未正常关闭 造成泄漏
            connection.openPipeline();
            source.forEach((key, value) -> {
                // hset zset都是可以用的，但是要序列化
                connection.set(RedisSerializer.string().serialize(prefix + key),
                        RedisSerializer.string().serialize(new Gson().toJson(value)));
//                // 设置过期时间 10天
//                connection.expire(RedisSerializer.string().serialize(prefix + key), TimeUnit.DAYS.toSeconds(1));
            });
            connection.close();
            // executePipelined源码要求RedisCallback必须返回null，否则抛异常
            return null;
        });
    }


    public void multiSaveHash(Map<String, Map<String, Object>> source, String prefix) {
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            // 这里逻辑简单不会抛异常
            // 否则需要加上try...catch...finally防止链接未正常关闭 造成泄漏
            connection.openPipeline();
            source.forEach((key, value) -> {
                // hset zset都是可以用的，但是要序列化
                value.forEach((dataKey, dataValue) -> {
                    connection.hSet(RedisSerializer.string().serialize(prefix + key),
                            RedisSerializer.string().serialize(dataKey), RedisSerializer.string().serialize(new Gson().toJson(dataValue)));
//                    // 设置过期时间 10天
//                    connection.expire(RedisSerializer.string().serialize(prefix + key), TimeUnit.DAYS.toSeconds(1));
                });
            });
            connection.close();
            // executePipelined源码要求RedisCallback必须返回null，否则抛异常
            return null;
        });
    }


    /**
     * 根据key 删除
     *
     * @param redisKey
     */
    public void deleteByKey(String redisKey) {
        this.redisTemplate.delete(redisKey);
    }

    public void batchDeleteByPatten(String keyPatten) {
        this.redisTemplate.delete(redisTemplate.keys(keyPatten + "*"));
    }


}
