package com.smoc.cloud.filters.redis;

import com.google.gson.Gson;
import com.smoc.cloud.common.auth.qo.Dict;
import com.smoc.cloud.common.auth.qo.DictType;
import com.smoc.cloud.common.filters.utils.InitializeFiltersData;
import com.smoc.cloud.common.filters.utils.RedisConstant;
import com.smoc.cloud.filter.entity.FilterBlackList;
import com.smoc.cloud.filter.entity.FilterWhiteList;
import com.smoc.cloud.filter.entity.KeyWordsMaskKeyWords;
import com.smoc.cloud.filter.repository.BlackRepository;
import com.smoc.cloud.filter.repository.KeywordsRepository;
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
    @Resource
    private KeywordsRepository keywordsRepository;

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
        //账号过滤参数
        if ("1".equals(initializeFiltersData.getReloadAccountFilterParams())) {
            this.batchDeleteByPatten(RedisConstant.FILTERS_CONFIG_ACCOUNT_COMMON);
            this.initializeAccountFilterParams();
        }

        //系统敏感词
        if ("1".equals(initializeFiltersData.getReloadSystemSensitiveWords())) {
            this.deleteByKey(RedisConstant.FILTERS_CONFIG_SYSTEM_WORDS_SENSITIVE);
            this.initializeSensitiveWords();
        }

        //系统审核词
        if ("1".equals(initializeFiltersData.getReloadSystemCheckWords())) {
            this.deleteByKey(RedisConstant.FILTERS_CONFIG_SYSTEM_WORDS_CHECK);
            this.initializeCheckWords();
        }

        //系统超级白词
        if ("1".equals(initializeFiltersData.getReloadSystemSuperWhiteWords())) {
            this.deleteByKey(RedisConstant.FILTERS_CONFIG_SYSTEM_WORDS_WHITE_SUPER);
            this.initializeSuperWhiteWords();
        }

        //系统洗黑白词
        if ("1".equals(initializeFiltersData.getReloadSystemWhiteBlackWords())) {
            this.deleteByKey(RedisConstant.FILTERS_CONFIG_SYSTEM_WORDS_WHITE_SENSITIVE);
            this.initializeWhiteBlackWords();
        }
        //系统免审白词
        if ("1".equals(initializeFiltersData.getReloadSystemNoCheckWhiteWords())) {
            this.deleteByKey(RedisConstant.FILTERS_CONFIG_SYSTEM_WORDS_WHITE_NO_CHECK);
            this.initializeNoCheckWhiteWords();
        }
        //系统正则白词
        if ("1".equals(initializeFiltersData.getReloadSystemRegularWhiteWords())) {
            this.deleteByKey(RedisConstant.FILTERS_CONFIG_SYSTEM_WORDS_WHITE_REGULAR);
            this.initializeRegularWhiteWords();
        }

        //行业敏感词
        if ("1".equals(initializeFiltersData.getReloadInfoSensitiveWords())) {
            this.batchDeleteByPatten(RedisConstant.FILTERS_CONFIG_SYSTEM_WORDS_INFO_TYPE_SENSITIVE);
            this.initializeInfoTypeSensitiveWords(initializeFiltersData.getInfoType());
        }

        //业务账号敏感词
        if ("1".equals(initializeFiltersData.getReloadAccountSensitiveWords())) {
            this.batchDeleteByPatten(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_SENSITIVE);
            this.initializeAccountSensitiveWords();
        }
        //业务账号审核词
        if ("1".equals(initializeFiltersData.getReloadAccountCheckWords())) {
            this.batchDeleteByPatten(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_CHECK);
            this.initializeAccountCheckWords();
        }

        //业务账号超级白词
        if ("1".equals(initializeFiltersData.getReloadAccountSuperWhiteWords())) {
            this.batchDeleteByPatten(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_WHITE_SUPER);
            this.initializeAccountSuperWhiteWords();
        }

        //业务账号洗黑白词
        if ("1".equals(initializeFiltersData.getReloadAccountWhiteBlackWords())) {
            this.batchDeleteByPatten(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_WHITE_SENSITIVE);
            this.initializeAccountWhiteBlackWords();
        }

        //业务账号免审白词
        if ("1".equals(initializeFiltersData.getReloadAccountNoCheckWhiteWords())) {
            this.batchDeleteByPatten(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_WHITE_NO_CHECK);
            this.initializeAccountNoCheckWhiteWords();
        }
        //业务账号正则白词
        if ("1".equals(initializeFiltersData.getReloadAccountRegularWhiteWords())) {
            this.batchDeleteByPatten(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_WHITE_REGULAR);
            this.initializeAccountRegularWhiteWords();
        }


    }

    /**
     * 初始化业务账号正则白词
     */
    public void initializeAccountRegularWhiteWords() {
        List<KeyWordsMaskKeyWords> wordsMaskKeyWords = keywordsRepository.loadKeyWordsAndMaskKeyWord("BUSINESS_ACCOUNT", null, "WHITE_AVOID_REGULAR");
        log.info("加载业务账号免审白词条数：{}", wordsMaskKeyWords.size());
        long start = System.currentTimeMillis();
        log.info("加载业务账号免审白词start：{}", start);
        this.multiSaveSetBatch(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_WHITE_REGULAR, wordsMaskKeyWords);
        long end = System.currentTimeMillis();
        log.info("加载业务账号免审白词  end：{}", end);
    }

    /**
     * 初始化业务账号免审白词
     */
    public void initializeAccountNoCheckWhiteWords() {
        List<KeyWordsMaskKeyWords> wordsMaskKeyWords = keywordsRepository.loadKeyWordsAndMaskKeyWord("BUSINESS_ACCOUNT", null, "WHITE_AVOID_CHECK");
        log.info("加载业务账号免审白词条数：{}", wordsMaskKeyWords.size());
        long start = System.currentTimeMillis();
        log.info("加载业务账号免审白词start：{}", start);
        this.multiSaveHash(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_WHITE_NO_CHECK, wordsMaskKeyWords);
        long end = System.currentTimeMillis();
        log.info("加载业务账号免审白词  end：{}", end);
    }


    /**
     * 初始化业务账号洗黑白词
     */
    public void initializeAccountWhiteBlackWords() {
        List<KeyWordsMaskKeyWords> wordsMaskKeyWords = keywordsRepository.loadKeyWordsAndMaskKeyWord("BUSINESS_ACCOUNT", null, "WHITE_AVOID_BLACK");
        log.info("加载业务账号洗黑白词条数：{}", wordsMaskKeyWords.size());
        long start = System.currentTimeMillis();
        log.info("加载业务账号洗黑白词start：{}", start);
        this.multiSaveHash(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_WHITE_SENSITIVE, wordsMaskKeyWords);
        long end = System.currentTimeMillis();
        log.info("加载业务账号洗黑白词  end：{}", end);
    }

    /**
     * 初始化业务账号超级白词
     */
    public void initializeAccountSuperWhiteWords() {
        List<KeyWordsMaskKeyWords> wordsMaskKeyWords = keywordsRepository.loadKeyWordsAndMaskKeyWord("BUSINESS_ACCOUNT", null, "SUPER_WHITE");
        log.info("加载业务账号超级白词条数：{}", wordsMaskKeyWords.size());
        long start = System.currentTimeMillis();
        log.info("加载业务账号超级白词start：{}", start);
        this.multiSaveSetBatch(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_WHITE_SUPER, wordsMaskKeyWords);
        long end = System.currentTimeMillis();
        log.info("加载业务账号超级白词  end：{}", end);
    }

    /**
     * 初始化业务账号审核词
     */
    public void initializeAccountCheckWords() {
        List<KeyWordsMaskKeyWords> wordsMaskKeyWords = keywordsRepository.loadKeyWordsAndMaskKeyWord("BUSINESS_ACCOUNT", null, "CHECK");
        log.info("加载业务账号审核词条数：{}", wordsMaskKeyWords.size());
        long start = System.currentTimeMillis();
        log.info("加载业务账号审核词start：{}", start);
        this.multiSaveSetBatch(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_CHECK, wordsMaskKeyWords);
        long end = System.currentTimeMillis();
        log.info("加载业务账号审核词  end：{}", end);
    }

    /**
     * 初始化业务账号敏感词
     */
    public void initializeAccountSensitiveWords() {
        List<KeyWordsMaskKeyWords> wordsMaskKeyWords = keywordsRepository.loadKeyWordsAndMaskKeyWord("BUSINESS_ACCOUNT", null, "BLACK");
        log.info("加载业务账号敏感词条数：{}", wordsMaskKeyWords.size());
        long start = System.currentTimeMillis();
        log.info("加载业务账号敏感词start：{}", start);
        this.multiSaveSetBatch(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_SENSITIVE, wordsMaskKeyWords);
        long end = System.currentTimeMillis();
        log.info("加载业务账号敏感词  end：{}", end);
    }

    /**
     * 初始化黑名单
     */
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

    /**
     * 初始化黑名单
     */
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
     * 初始化系统敏感词
     */
    public void initializeSensitiveWords() {
        List<String> sensitiveWords = keywordsRepository.loadKeyWords("SYSTEM", "BLACK", "BLACK");
        log.info("加载系统敏感词条数：{}", sensitiveWords.size());
        long start = System.currentTimeMillis();
        log.info("加载系统敏感词start：{}", start);
        this.multiSaveSet(RedisConstant.FILTERS_CONFIG_SYSTEM_WORDS_SENSITIVE, sensitiveWords);
        long end = System.currentTimeMillis();
        log.info("加载系统敏感词  end：{}", end);
    }

    /**
     * 初始化系统审核词
     */
    public void initializeCheckWords() {
        List<String> checkWords = keywordsRepository.loadKeyWords("SYSTEM", "CHECK", "CHECK");
        log.info("加载系统审核词条数：{}", checkWords.size());
        long start = System.currentTimeMillis();
        log.info("加载系统审核词start：{}", start);
        this.multiSaveSet(RedisConstant.FILTERS_CONFIG_SYSTEM_WORDS_CHECK, checkWords);
        long end = System.currentTimeMillis();
        log.info("加载系统审核词  end：{}", end);
    }

    /**
     * 初始化系统超级白词
     */
    public void initializeSuperWhiteWords() {
        List<String> superWhiteWords = keywordsRepository.loadKeyWords("SYSTEM", "WHITE", "SUPER_WHITE");
        log.info("加载系统超级白词条数：{}", superWhiteWords.size());
        long start = System.currentTimeMillis();
        log.info("加载系统超级白词start：{}", start);
        this.multiSaveSet(RedisConstant.FILTERS_CONFIG_SYSTEM_WORDS_WHITE_SUPER, superWhiteWords);
        long end = System.currentTimeMillis();
        log.info("加载系统超级白词  end：{}", end);
    }

    /**
     * 初始化系统洗黑白词
     */
    public void initializeWhiteBlackWords() {
        List<KeyWordsMaskKeyWords> wordsMaskKeyWords = keywordsRepository.loadKeyWordsAndMaskKeyWord("SYSTEM", "WHITE", "WHITE_AVOID_BLACK");
        log.info("加载系统洗黑白词条数：{}", wordsMaskKeyWords.size());
        long start = System.currentTimeMillis();
        log.info("加载系统洗黑白词start：{}", start);
        this.multiSaveHashNoCheck(RedisConstant.FILTERS_CONFIG_SYSTEM_WORDS_WHITE_SENSITIVE, wordsMaskKeyWords);
        long end = System.currentTimeMillis();
        log.info("加载系统洗黑白词  end：{}", end);
    }

    /**
     * 初始化系统免审白词
     */
    public void initializeNoCheckWhiteWords() {
        List<KeyWordsMaskKeyWords> wordsMaskKeyWords = keywordsRepository.loadKeyWordsAndMaskKeyWord("SYSTEM", "WHITE", "WHITE_AVOID_CHECK");
        log.info("加载系统免审白词条数：{}", wordsMaskKeyWords.size());
        long start = System.currentTimeMillis();
        log.info("加载系统免审白词start：{}", start);
        this.multiSaveHashNoCheck(RedisConstant.FILTERS_CONFIG_SYSTEM_WORDS_WHITE_NO_CHECK, wordsMaskKeyWords);
        long end = System.currentTimeMillis();
        log.info("加载系统免审白词  end：{}", end);
    }

    /**
     * 初始化系统正则白词
     */
    public void initializeRegularWhiteWords() {
        List<String> regulars = keywordsRepository.loadKeyWords("SYSTEM", "WHITE", "WHITE_AVOID_REGULAR");
        log.info("加载系统正则白词条数：{}", regulars.size());
        long start = System.currentTimeMillis();
        log.info("加载系统正则白词start：{}", start);
        this.multiSaveSet(RedisConstant.FILTERS_CONFIG_SYSTEM_WORDS_WHITE_REGULAR, regulars);
        long end = System.currentTimeMillis();
        log.info("加载系统正则白词  end：{}", end);
    }

    /**
     * 初始化行业敏感词
     */
    public void initializeInfoTypeSensitiveWords(DictType infoType) {
        long start = System.currentTimeMillis();
        log.info("加载系统正则白词start：{}", start);
        if (null != infoType && infoType.getDict().size() > 0) {
            for (Dict dict : infoType.getDict()) {
                List<String> infoTypes = keywordsRepository.loadKeyWords("INFO_TYPE", dict.getFieldCode(), "BLACK");
                if (null != infoTypes && infoTypes.size() > 0) {
                    redisTemplate.opsForSet().add(RedisConstant.FILTERS_CONFIG_SYSTEM_WORDS_INFO_TYPE, dict.getFieldCode());
                    this.multiSaveSet(RedisConstant.FILTERS_CONFIG_SYSTEM_WORDS_INFO_TYPE_SENSITIVE + dict.getFieldCode(), infoTypes);
                }
            }
        }
        long end = System.currentTimeMillis();
        log.info("加载系统正则白词  end：{}", end);
    }

    public void multiSaveHash(String redisKey, List<KeyWordsMaskKeyWords> list) {
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            connection.openPipeline();
            list.forEach((value) -> {
                // hset zset都是可以用的，但是要序列化
                connection.hSet(RedisSerializer.string().serialize(redisKey + value.getBusinessId()),
                        RedisSerializer.string().serialize(value.getMaskKeyWord()), RedisSerializer.string().serialize(new Gson().toJson(value.getKeyWord())));
            });
            connection.close();
            // executePipelined源码要求RedisCallback必须返回null，否则抛异常
            return null;
        });
    }

    public void multiSaveHashNoCheck(String redisKey, List<KeyWordsMaskKeyWords> list) {
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            connection.openPipeline();
            list.forEach((value) -> {
                // hset zset都是可以用的，但是要序列化
                connection.hSet(RedisSerializer.string().serialize(redisKey),
                        RedisSerializer.string().serialize(value.getMaskKeyWord()), RedisSerializer.string().serialize(new Gson().toJson(value.getKeyWord())));
            });
            connection.close();
            // executePipelined源码要求RedisCallback必须返回null，否则抛异常
            return null;
        });
    }

    public void multiSaveHashBatch(String redisKey, List<KeyWordsMaskKeyWords> list) {
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            connection.openPipeline();
            list.forEach((value) -> {
                // hset zset都是可以用的，但是要序列化
                connection.hSet(RedisSerializer.string().serialize(redisKey),
                        RedisSerializer.string().serialize(value.getMaskKeyWord()), RedisSerializer.string().serialize(new Gson().toJson(value.getKeyWord())));
            });
            connection.close();
            // executePipelined源码要求RedisCallback必须返回null，否则抛异常
            return null;
        });
    }

    public void multiSaveSetBatch(String redisKey, List<KeyWordsMaskKeyWords> list) {
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            connection.openPipeline();
            list.forEach((value) -> {
                // hset zset都是可以用的，但是要序列化
                connection.sAdd(RedisSerializer.string().serialize(redisKey + "list"),
                        RedisSerializer.string().serialize(new Gson().toJson(value.getBusinessId())));
                connection.sAdd(RedisSerializer.string().serialize(redisKey + value.getBusinessId()),
                        RedisSerializer.string().serialize(new Gson().toJson(value.getKeyWord())));
            });
            connection.close();
            // executePipelined源码要求RedisCallback必须返回null，否则抛异常
            return null;
        });
    }

    /**
     * 批量保存到redis
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
                        RedisSerializer.string().serialize(new Gson().toJson(value)));
            });
            connection.close();
            // executePipelined源码要求RedisCallback必须返回null，否则抛异常
            return null;
        });
    }

    /**
     * 批量保存到redis
     *
     * @param source
     * @param key
     */
    public void multiSaveSet(String key, List<String> source) {
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            // 这里逻辑简单不会抛异常
            // 否则需要加上try...catch...finally防止链接未正常关闭 造成泄漏
            connection.openPipeline();
            source.forEach((value) -> {
                // hset zset都是可以用的，但是要序列化
                connection.sAdd(RedisSerializer.string().serialize(key),
                        RedisSerializer.string().serialize(new Gson().toJson(value)));
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

    /**
     * 批量删除
     *
     * @param keyPatten
     */
    public void batchDeleteByPatten(String keyPatten) {
        this.redisTemplate.delete(redisTemplate.keys(keyPatten + "*"));
    }


}
