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
import com.smoc.cloud.template.entity.AccountTemplateContent;
import com.smoc.cloud.template.repository.AccountTemplateInfoRepository;
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
    @Resource
    private AccountTemplateInfoRepository accountTemplateInfoRepository;

    @Autowired
    private ParameterExtendFiltersValueRepository parameterExtendFiltersValueRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    public void initialize(InitializeFiltersData initializeFiltersData) {

        //系统黑名单
        if ("1".equals(initializeFiltersData.getReloadBlackList())) {
            this.initializeBlackList();
        }
        //系统白名单
        if ("1".equals(initializeFiltersData.getReloadWhiteList())) {
            this.initializeWhiteList();
        }
        //账号过滤参数
        if ("1".equals(initializeFiltersData.getReloadAccountFilterParams())) {
            this.initializeAccountFilterParams();
        }

        //系统敏感词
        if ("1".equals(initializeFiltersData.getReloadSystemSensitiveWords())) {
            this.initializeSensitiveWords();
            this.pubMessage(RedisConstant.MESSAGE_SYSTEM_SENSITIVE);
        }

        //系统审核词
        if ("1".equals(initializeFiltersData.getReloadSystemCheckWords())) {
            this.initializeCheckWords();
            this.pubMessage(RedisConstant.MESSAGE_SYSTEM_CHECK);
        }

        //系统超级白词
        if ("1".equals(initializeFiltersData.getReloadSystemSuperWhiteWords())) {
            this.initializeSuperWhiteWords();
            this.pubMessage(RedisConstant.MESSAGE_SYSTEM_SUPER_WHITE);
        }

        //系统洗敏白词
        if ("1".equals(initializeFiltersData.getReloadSystemWhiteBlackWords())) {
            this.initializeWhiteBlackWords();
        }
        //系统免审白词
        if ("1".equals(initializeFiltersData.getReloadSystemNoCheckWhiteWords())) {
            this.initializeNoCheckWhiteWords();
        }
        //系统正则白词
        if ("1".equals(initializeFiltersData.getReloadSystemRegularWhiteWords())) {
            this.initializeRegularWhiteWords();
        }

        //行业敏感词
        if ("1".equals(initializeFiltersData.getReloadInfoSensitiveWords())) {
            this.initializeInfoTypeSensitiveWords(initializeFiltersData.getInfoType());
            this.pubMessage(RedisConstant.MESSAGE_TYPE_INFO_SENSITIVE);
        }

        //业务账号敏感词
        if ("1".equals(initializeFiltersData.getReloadAccountSensitiveWords())) {
            this.initializeAccountSensitiveWords();
            this.pubMessage(RedisConstant.MESSAGE_ACCOUNT_SENSITIVE);
        }
        //业务账号审核词
        if ("1".equals(initializeFiltersData.getReloadAccountCheckWords())) {
            this.initializeAccountCheckWords();
            this.pubMessage(RedisConstant.MESSAGE_ACCOUNT_CHECK);
        }

        //业务账号超级白词
        if ("1".equals(initializeFiltersData.getReloadAccountSuperWhiteWords())) {
            this.initializeAccountSuperWhiteWords();
            this.pubMessage(RedisConstant.MESSAGE_ACCOUNT_SUPER_WHITE);
        }

        //业务账号洗黑白词
        if ("1".equals(initializeFiltersData.getReloadAccountWhiteBlackWords())) {
            this.initializeAccountWhiteBlackWords();
        }

        //业务账号免审白词
        if ("1".equals(initializeFiltersData.getReloadAccountNoCheckWhiteWords())) {
            this.initializeAccountNoCheckWhiteWords();
        }
        //业务账号正则白词
        if ("1".equals(initializeFiltersData.getReloadAccountRegularWhiteWords())) {
            this.initializeAccountRegularWhiteWords();
        }

        //业务账号模版
        if ("1".equals(initializeFiltersData.getReloadAccountTemplate())) {
            this.initializeAccountTemplate();
            this.pubMessage(RedisConstant.MESSAGE_TEMPLATE);
        }

    }

    /**
     * 初始化业务账号模版
     */
    public void initializeAccountTemplate() {
        //加载固定模版数据
        List<AccountTemplateContent> fixedTemplates = accountTemplateInfoRepository.findFixedTemplate();
        //加载变量模版数据,匹配后，不再进行后续过滤
        Map<String, String> variableNoFilterVariableTemplates = accountTemplateInfoRepository.findNoFilterVariableTemplate();
        log.info("不再进行后续过滤start：{}",new Gson().toJson(variableNoFilterVariableTemplates));
        //加载CMPP变量模版数据
        Map<String, String> variableCMPPTemplates = accountTemplateInfoRepository.findCMPPVariableTemplate();
        //加载CMPP签名模版数据
        Map<String, String> signCMPPTemplates = accountTemplateInfoRepository.findCMPPSignTemplate();
        //删除redis 现有缓存
        long start = System.currentTimeMillis();
        log.info("加载业务账号模版start：{}", start);
        this.batchDeleteByPatten(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_TEMPLATE_FIXED);
        this.multiSaveSetTemplate(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_TEMPLATE_FIXED, fixedTemplates);
        this.batchDeleteByPatten(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_TEMPLATE_VARIABLE_CMPP);
        this.multiSaveFiltersTemplate(variableNoFilterVariableTemplates,RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_TEMPLATE_VARIABLE_CMPP+"no_filter:");
        this.multiSaveFiltersTemplate(variableCMPPTemplates,RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_TEMPLATE_VARIABLE_CMPP+"filter:");
        this.batchDeleteByPatten(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_TEMPLATE_SIGN);
        this.multiSaveFiltersTemplate(signCMPPTemplates,RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_TEMPLATE_SIGN);
        long end = System.currentTimeMillis();
        log.info("加载业务账号模版  end：{}", end);
    }

    /**
     * 初始化业务账号正则白词
     */
    public void initializeAccountRegularWhiteWords() {
        //加载数据
        List<KeyWordsMaskKeyWords> wordsMaskKeyWords = keywordsRepository.loadKeyWordsAndMaskKeyWord("BUSINESS_ACCOUNT", null, "WHITE_AVOID_REGULAR");
        log.info("加载业务账号免审白词条数：{}", wordsMaskKeyWords.size());
        //删除redis现有缓存
        this.batchDeleteByPatten(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_WHITE_REGULAR);
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
        //加载数据
        List<KeyWordsMaskKeyWords> wordsMaskKeyWords = keywordsRepository.loadKeyWordsAndMaskKeyWord("BUSINESS_ACCOUNT", null, "WHITE_AVOID_CHECK");
        log.info("加载业务账号免审白词条数：{}", wordsMaskKeyWords.size());
        //删除redis现有缓存
        this.batchDeleteByPatten(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_WHITE_NO_CHECK);
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
        //加载数据
        List<KeyWordsMaskKeyWords> wordsMaskKeyWords = keywordsRepository.loadKeyWordsAndMaskKeyWord("BUSINESS_ACCOUNT", null, "WHITE_AVOID_BLACK");
        log.info("加载业务账号洗黑白词条数：{}", wordsMaskKeyWords.size());
        //删除redis现有缓存
        this.batchDeleteByPatten(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_WHITE_SENSITIVE);
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
        //加载数据
        List<KeyWordsMaskKeyWords> wordsMaskKeyWords = keywordsRepository.loadKeyWordsAndMaskKeyWord("BUSINESS_ACCOUNT", null, "SUPER_WHITE");
        log.info("加载业务账号超级白词条数：{}", wordsMaskKeyWords.size());
        //删除redis现有缓存
        this.batchDeleteByPatten(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_WHITE_SUPER);
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
        //加载数据
        List<KeyWordsMaskKeyWords> wordsMaskKeyWords = keywordsRepository.loadKeyWordsAndMaskKeyWord("BUSINESS_ACCOUNT", null, "CHECK");
        log.info("加载业务账号审核词条数：{}", wordsMaskKeyWords.size());
        //删除redis 现有缓存
        this.batchDeleteByPatten(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_CHECK);
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
        //加载数据
        List<KeyWordsMaskKeyWords> wordsMaskKeyWords = keywordsRepository.loadKeyWordsAndMaskKeyWord("BUSINESS_ACCOUNT", null, "BLACK");
        log.info("加载业务账号敏感词条数：{}", wordsMaskKeyWords.size());
        //删除redis现有缓存
        this.batchDeleteByPatten(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_SENSITIVE);
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
        //加载数据
        List<FilterBlackList> filterBlackListList = blackRepository.findAll();
        //删除现有redis缓存
        this.deleteByKey(RedisConstant.FILTERS_CONFIG_SYSTEM_BLACK);
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
        //加载数据
        List<FilterWhiteList> filterWhiteList = whiteRepository.findAll();
        //删除现有redis缓存
        this.deleteByKey(RedisConstant.FILTERS_CONFIG_SYSTEM_WHITE);
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
        //加载数据
        List<String> sensitiveWords = keywordsRepository.loadKeyWords("SYSTEM", "BLACK", "BLACK");
        log.info("加载系统敏感词条数：{}", sensitiveWords.size());
        //删除现有redis 缓存
        this.deleteByKey(RedisConstant.FILTERS_CONFIG_SYSTEM_WORDS_SENSITIVE);
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
        //加载数据
        List<String> checkWords = keywordsRepository.loadKeyWords("SYSTEM", "CHECK", "CHECK");
        log.info("加载系统审核词条数：{}", checkWords.size());
        //删除redis现有缓存
        this.deleteByKey(RedisConstant.FILTERS_CONFIG_SYSTEM_WORDS_CHECK);
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
        //加载数据
        List<String> superWhiteWords = keywordsRepository.loadKeyWords("SYSTEM", "WHITE", "SUPER_WHITE");
        log.info("加载系统超级白词条数：{}", superWhiteWords.size());
        //删除redis现有缓存
        this.deleteByKey(RedisConstant.FILTERS_CONFIG_SYSTEM_WORDS_WHITE_SUPER);
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
        //加载数据
        List<KeyWordsMaskKeyWords> wordsMaskKeyWords = keywordsRepository.loadKeyWordsAndMaskKeyWord("SYSTEM", "WHITE", "WHITE_AVOID_BLACK");
        log.info("加载系统洗黑白词条数：{}", wordsMaskKeyWords.size());
        //删除redis现有缓存
        this.deleteByKey(RedisConstant.FILTERS_CONFIG_SYSTEM_WORDS_WHITE_SENSITIVE);
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
        //加载数据
        List<KeyWordsMaskKeyWords> wordsMaskKeyWords = keywordsRepository.loadKeyWordsAndMaskKeyWord("SYSTEM", "WHITE", "WHITE_AVOID_CHECK");
        log.info("加载系统免审白词条数：{}", wordsMaskKeyWords.size());
        //删除redis 现有缓存
        this.deleteByKey(RedisConstant.FILTERS_CONFIG_SYSTEM_WORDS_WHITE_NO_CHECK);
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
        //加载数据
        List<String> regulars = keywordsRepository.loadKeyWords("SYSTEM", "WHITE", "WHITE_AVOID_REGULAR");
        log.info("加载系统正则白词条数：{}", regulars.size());
        //删除redis现有缓存
        this.deleteByKey(RedisConstant.FILTERS_CONFIG_SYSTEM_WORDS_WHITE_REGULAR);
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
        this.batchDeleteByPatten(RedisConstant.FILTERS_CONFIG_SYSTEM_WORDS_INFO_TYPE_SENSITIVE);
        this.batchDeleteByPatten(RedisConstant.FILTERS_CONFIG_SYSTEM_WORDS_INFO_TYPE);
        long start = System.currentTimeMillis();
        log.info("加载行业敏感词start：{}", start);
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
        log.info("加载行业敏感词  end：{}", end);
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

    public void multiSaveSetTemplate(String redisKey, List<AccountTemplateContent> list) {
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            connection.openPipeline();
            list.forEach((value) -> {
                // hset zset都是可以用的，但是要序列化
                connection.sAdd(RedisSerializer.string().serialize(redisKey + "list"),
                        RedisSerializer.string().serialize(new Gson().toJson(value.getAccount())));
                connection.sAdd(RedisSerializer.string().serialize(redisKey + value.getAccount()),
                        RedisSerializer.string().serialize(new Gson().toJson(value.getContent())));
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
        //加载并处理数据格式
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
            //删除redis现有缓存
            this.batchDeleteByPatten(RedisConstant.FILTERS_CONFIG_ACCOUNT_COMMON);
            this.multiSaveHash(commonMap, RedisConstant.FILTERS_CONFIG_ACCOUNT_COMMON);
            //删除redis现有缓存
            this.batchDeleteByPatten(RedisConstant.FILTERS_CONFIG_ACCOUNT_NUMBER);
            this.multiSaveFilters(numberMap, RedisConstant.FILTERS_CONFIG_ACCOUNT_NUMBER);
            //删除redis现有缓存
            this.batchDeleteByPatten(RedisConstant.FILTERS_CONFIG_ACCOUNT_MESSAGE);
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

    /**
     * @param source
     * @param prefix
     */
    public void multiSaveFiltersTemplate(Map<String, String> source, String prefix) {
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            // 这里逻辑简单不会抛异常
            // 否则需要加上try...catch...finally防止链接未正常关闭 造成泄漏
            connection.openPipeline();
            source.forEach((key, value) -> {
                // hset zset都是可以用的，但是要序列化
                connection.sAdd(RedisSerializer.string().serialize(prefix + "list"),
                        RedisSerializer.string().serialize(new Gson().toJson(key)));
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

    /**
     * 发布消息
     *
     * @param message
     */
    public void pubMessage(String message) {
        this.redisTemplate.convertAndSend(RedisConstant.CHANNEL, message);
    }


}
