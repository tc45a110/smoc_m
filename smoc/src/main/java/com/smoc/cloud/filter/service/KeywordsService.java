package com.smoc.cloud.filter.service;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.smoc.cloud.common.filters.utils.RedisConstant;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.filter.FilterKeyWordsInfoValidator;
import com.smoc.cloud.filter.entity.FilterKeyWordsInfo;
import com.smoc.cloud.filter.entity.KeyWordsMaskKeyWords;
import com.smoc.cloud.filter.repository.KeywordsRepository;
import com.smoc.cloud.tools.message.RocketProducerFilterMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 关键词管理
 */
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class KeywordsService {

    @Resource
    private KeywordsRepository keywordsRepository;

    @Resource(name = "defaultRedisTemplate")
    private RedisTemplate redisTemplate;

    @Autowired
    private RocketProducerFilterMessage rocketProducerFilterMessage;

    /**
     * 根据群组id查询通讯录
     *
     * @param pageParams
     * @return
     */
    public PageList<FilterKeyWordsInfoValidator> page(PageParams<FilterKeyWordsInfoValidator> pageParams) {
        return keywordsRepository.page(pageParams);
    }

    /**
     * 根据id 查询
     *
     * @param id
     * @return
     */
    public ResponseData findById(String id) {

        Optional<FilterKeyWordsInfo> data = keywordsRepository.findById(id);

        //使用了 Optional 的判断方式，判断优雅
        return ResponseDataUtil.buildSuccess(data.orElse(null));
    }

    /**
     * 保存或修改
     *
     * @param filterKeyWordsInfoValidator
     * @param op                          操作类型 为add、edit
     * @return
     */
    @Transactional
    public ResponseData save(FilterKeyWordsInfoValidator filterKeyWordsInfoValidator, String op) {

        //转BaseUser存放对象
        FilterKeyWordsInfo entity = new FilterKeyWordsInfo();
        BeanUtils.copyProperties(filterKeyWordsInfoValidator, entity);

        List<FilterKeyWordsInfo> data = keywordsRepository.findByKeyWordsBusinessTypeAndBusinessIdAndKeyWordsTypeAndKeyWordsAndWaskKeyWords(entity.getKeyWordsBusinessType(), entity.getBusinessId(), entity.getKeyWordsType(), entity.getKeyWords(), entity.getWaskKeyWords());

        //add查重
        if (data != null && data.iterator().hasNext() && "add".equals(op)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
        }
        //edit查重orgName
        else if (data != null && data.iterator().hasNext() && "edit".equals(op)) {
            boolean status = false;
            Iterator iter = data.iterator();
            while (iter.hasNext()) {
                FilterKeyWordsInfo organization = (FilterKeyWordsInfo) iter.next();

                log.info("[FilterKeyWordsInfo]:{}", organization.getKeyWords());
                if (!entity.getId().equals(organization.getId())) {
                    status = true;
                    break;
                }
            }
            if (status) {
                return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
            }
        }

        /**
         * 如果存在记录，则先把存在的关键词从缓存删除
         */
        if (isExist(filterKeyWordsInfoValidator.getId())) {
            Optional<FilterKeyWordsInfo> keyWordsInfo = keywordsRepository.findById(filterKeyWordsInfoValidator.getId());
            this.delFromSet(RedisConstant.FILTERS_CONFIG_CHANNEL_SENSITIVE+filterKeyWordsInfoValidator.getBusinessId(), keyWordsInfo.get().getKeyWords());
        }

        //op 不为 edit 或 add
        if (!("edit".equals(op) || "add".equals(op))) {
            return ResponseDataUtil.buildError();
        }

        //记录日志
        log.info("[关键词管理][{}]数据:{}", op, JSON.toJSONString(entity));

        entity.setIsSync("0");
        keywordsRepository.saveAndFlush(entity);
        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 根据id 删除
     *
     * @param id
     * @return
     */
    @Transactional
    public ResponseData deleteById(String id) {

        FilterKeyWordsInfo data = keywordsRepository.findById(id).get();

        //记录日志
        log.info("[关键词管理][delete]数据:{}", JSON.toJSONString(data));
        keywordsRepository.deleteById(id);

        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 批量保存
     *
     * @param filterKeyWordsInfoValidator
     * @return
     */
    @Async
    public void bathSave(FilterKeyWordsInfoValidator filterKeyWordsInfoValidator) {
        keywordsRepository.bathSave(filterKeyWordsInfoValidator);

        //系统敏感词
        if ("SYSTEM".equals(filterKeyWordsInfoValidator.getKeyWordsBusinessType()) && "BLACK".equals(filterKeyWordsInfoValidator.getBusinessId()) && "BLACK".equals(filterKeyWordsInfoValidator.getKeyWordsType())) {
            this.loadSystemSensitiveWords();
        }
        //系统审核词
        if ("SYSTEM".equals(filterKeyWordsInfoValidator.getKeyWordsBusinessType()) && "CHECK".equals(filterKeyWordsInfoValidator.getBusinessId()) && "CHECK".equals(filterKeyWordsInfoValidator.getKeyWordsType())) {
            this.loadSystemCheckWords();
        }
        //系统洗敏白词
        if ("SYSTEM".equals(filterKeyWordsInfoValidator.getKeyWordsBusinessType()) && "WHITE".equals(filterKeyWordsInfoValidator.getBusinessId()) && "WHITE_AVOID_BLACK".equals(filterKeyWordsInfoValidator.getKeyWordsType())) {
            this.loadSystemWhiteSensitiveWords();
        }
        //系统免审白词
        if ("SYSTEM".equals(filterKeyWordsInfoValidator.getKeyWordsBusinessType()) && "WHITE".equals(filterKeyWordsInfoValidator.getBusinessId()) && "WHITE_AVOID_CHECK".equals(filterKeyWordsInfoValidator.getKeyWordsType())) {
            this.loadSystemWhiteCheckWords();
        }
        //行业敏感词
        if ("INFO_TYPE".equals(filterKeyWordsInfoValidator.getKeyWordsBusinessType()) && "BLACK".equals(filterKeyWordsInfoValidator.getKeyWordsType())) {
            this.loadIndustrySensitiveWords(filterKeyWordsInfoValidator.getBusinessId());
        }
        //业务账号敏感词
        if ("BUSINESS_ACCOUNT".equals(filterKeyWordsInfoValidator.getKeyWordsBusinessType()) && "BLACK".equals(filterKeyWordsInfoValidator.getKeyWordsType())) {
            this.loadAccountSensitiveWords(filterKeyWordsInfoValidator.getBusinessId());
        }
        //业务账号审核词
        if ("BUSINESS_ACCOUNT".equals(filterKeyWordsInfoValidator.getKeyWordsBusinessType()) && "CHECK".equals(filterKeyWordsInfoValidator.getKeyWordsType())) {
            this.loadAccountCheckWords(filterKeyWordsInfoValidator.getBusinessId());
        }
        //业务账号洗敏白词
        if ("BUSINESS_ACCOUNT".equals(filterKeyWordsInfoValidator.getKeyWordsBusinessType()) && "WHITE_AVOID_BLACK".equals(filterKeyWordsInfoValidator.getKeyWordsType())) {
            this.loadAccountWhiteSensitiveWords(filterKeyWordsInfoValidator.getBusinessId());
        }
        //业务账号免审白词
        if ("BUSINESS_ACCOUNT".equals(filterKeyWordsInfoValidator.getKeyWordsBusinessType()) && "WHITE_AVOID_CHECK".equals(filterKeyWordsInfoValidator.getKeyWordsType())) {
            this.loadAccountWhiteCheckWords(filterKeyWordsInfoValidator.getBusinessId());
        }
        //通道敏感词
        if ("CHANNEL".equals(filterKeyWordsInfoValidator.getKeyWordsBusinessType()) && "BLACK".equals(filterKeyWordsInfoValidator.getKeyWordsType())) {
            this.loadChannelSensitiveWords(filterKeyWordsInfoValidator.getBusinessId());
        }
    }

    /**
     * 关键字导入
     *
     * @param filterKeyWordsInfoValidator
     */
    public void expBatchSave(FilterKeyWordsInfoValidator filterKeyWordsInfoValidator) {
        keywordsRepository.expBatchSave(filterKeyWordsInfoValidator);
        log.info("[关键词]{}", new Gson().toJson(filterKeyWordsInfoValidator));

        //系统敏感词
        if ("SYSTEM".equals(filterKeyWordsInfoValidator.getKeyWordsBusinessType()) && "BLACK".equals(filterKeyWordsInfoValidator.getBusinessId()) && "BLACK".equals(filterKeyWordsInfoValidator.getKeyWordsType())) {
            this.loadSystemSensitiveWords();
        }
        //系统审核词
        if ("SYSTEM".equals(filterKeyWordsInfoValidator.getKeyWordsBusinessType()) && "CHECK".equals(filterKeyWordsInfoValidator.getBusinessId()) && "CHECK".equals(filterKeyWordsInfoValidator.getKeyWordsType())) {
            this.loadSystemCheckWords();
        }
        //系统洗敏白词
        if ("SYSTEM".equals(filterKeyWordsInfoValidator.getKeyWordsBusinessType()) && "WHITE".equals(filterKeyWordsInfoValidator.getBusinessId()) && "WHITE_AVOID_BLACK".equals(filterKeyWordsInfoValidator.getKeyWordsType())) {
            this.loadSystemWhiteSensitiveWords();
        }
        //系统免审白词
        if ("SYSTEM".equals(filterKeyWordsInfoValidator.getKeyWordsBusinessType()) && "WHITE".equals(filterKeyWordsInfoValidator.getBusinessId()) && "WHITE_AVOID_CHECK".equals(filterKeyWordsInfoValidator.getKeyWordsType())) {
            this.loadSystemWhiteCheckWords();
        }
        //行业敏感词
        if ("INFO_TYPE".equals(filterKeyWordsInfoValidator.getKeyWordsBusinessType()) && "BLACK".equals(filterKeyWordsInfoValidator.getKeyWordsType())) {
            this.loadIndustrySensitiveWords(filterKeyWordsInfoValidator.getBusinessId());
        }
        //业务账号敏感词
        if ("BUSINESS_ACCOUNT".equals(filterKeyWordsInfoValidator.getKeyWordsBusinessType()) && "BLACK".equals(filterKeyWordsInfoValidator.getKeyWordsType())) {
            this.loadAccountSensitiveWords(filterKeyWordsInfoValidator.getBusinessId());
        }
        //业务账号审核词
        if ("BUSINESS_ACCOUNT".equals(filterKeyWordsInfoValidator.getKeyWordsBusinessType()) && "CHECK".equals(filterKeyWordsInfoValidator.getKeyWordsType())) {
            this.loadAccountCheckWords(filterKeyWordsInfoValidator.getBusinessId());
        }
        //业务账号洗敏白词
        if ("BUSINESS_ACCOUNT".equals(filterKeyWordsInfoValidator.getKeyWordsBusinessType()) && "WHITE_AVOID_BLACK".equals(filterKeyWordsInfoValidator.getKeyWordsType())) {
            this.loadAccountWhiteSensitiveWords(filterKeyWordsInfoValidator.getBusinessId());
        }
        //业务账号免审白词
        if ("BUSINESS_ACCOUNT".equals(filterKeyWordsInfoValidator.getKeyWordsBusinessType()) && "WHITE_AVOID_CHECK".equals(filterKeyWordsInfoValidator.getKeyWordsType())) {
            this.loadAccountWhiteCheckWords(filterKeyWordsInfoValidator.getBusinessId());
        }
        //通道敏感词
        if ("CHANNEL".equals(filterKeyWordsInfoValidator.getKeyWordsBusinessType()) && "BLACK".equals(filterKeyWordsInfoValidator.getKeyWordsType())) {
            this.loadChannelSensitiveWords(filterKeyWordsInfoValidator.getBusinessId());
        }
    }

    /**
     * 是否存在记录
     *
     * @param id
     * @return
     */
    public Boolean isExist(String id) {
        Boolean isExist = keywordsRepository.existsById(id);
        return isExist;
    }

    /**
     * 加载敏感词到缓存
     */
    @Async
    public void loadSystemSensitiveWords() {
        //加载数据
        List<FilterKeyWordsInfoValidator> sensitiveWords = keywordsRepository.loadWords("SYSTEM", "BLACK", "BLACK");
        if (null == sensitiveWords || sensitiveWords.size() < 1) {
            return;
        }
        log.info("加载系统敏感词条数：{}", sensitiveWords.size());
        this.multiSaveSet(RedisConstant.FILTERS_CONFIG_SYSTEM_WORDS_SENSITIVE, sensitiveWords);
        //更新关键词同步状态
        keywordsRepository.updateIsSyncStatus(sensitiveWords);
        //发送广播通知
        this.rocketProducerFilterMessage.sendRocketMessage(RedisConstant.MESSAGE_SYSTEM_SENSITIVE);
    }

    /**
     * 加载洗敏白词到缓存
     */
    @Async
    public void loadSystemWhiteSensitiveWords() {
        //加载数据
        List<FilterKeyWordsInfoValidator> whiteSensitiveWords = keywordsRepository.loadWords("SYSTEM", "WHITE", "WHITE_AVOID_BLACK");
        if (null == whiteSensitiveWords || whiteSensitiveWords.size() < 1) {
            return;
        }
        log.info("加载洗敏白词条数：{}", whiteSensitiveWords.size());
        this.multiSaveHash(RedisConstant.FILTERS_CONFIG_SYSTEM_WORDS_WHITE_SENSITIVE, whiteSensitiveWords);
        //更新关键词同步状态
        keywordsRepository.updateIsSyncStatus(whiteSensitiveWords);
    }

    /**
     * 加载审核词到缓存
     */
    @Async
    public void loadSystemCheckWords() {
        //加载数据
        List<FilterKeyWordsInfoValidator> checkWords = keywordsRepository.loadWords("SYSTEM", "CHECK", "CHECK");
        if (null == checkWords || checkWords.size() < 1) {
            return;
        }
        log.info("加载系统审核词条数：{}", checkWords.size());
        this.multiSaveSet(RedisConstant.FILTERS_CONFIG_SYSTEM_WORDS_CHECK, checkWords);
        //更新关键词同步状态
        keywordsRepository.updateIsSyncStatus(checkWords);
        //发送广播通知
        this.rocketProducerFilterMessage.sendRocketMessage(RedisConstant.MESSAGE_SYSTEM_CHECK);
    }

    /**
     * 加载免审白词到缓存
     */
    @Async
    public void loadSystemWhiteCheckWords() {
        //加载数据
        List<FilterKeyWordsInfoValidator> whiteCheckWords = keywordsRepository.loadWords("SYSTEM", "WHITE", "WHITE_AVOID_CHECK");
        if (null == whiteCheckWords || whiteCheckWords.size() < 1) {
            return;
        }
        log.info("加载免审核词条数：{}", whiteCheckWords.size());
        this.multiSaveHash(RedisConstant.FILTERS_CONFIG_SYSTEM_WORDS_WHITE_NO_CHECK, whiteCheckWords);
        //更新关键词同步状态
        keywordsRepository.updateIsSyncStatus(whiteCheckWords);
    }

    /**
     * 加载行业敏感词到缓存
     */
    @Async
    public void loadIndustrySensitiveWords(String type) {
        //加载数据
        List<FilterKeyWordsInfoValidator> industrySensitiveWords = keywordsRepository.loadWords("INFO_TYPE", type, "BLACK");
        if (null == industrySensitiveWords || industrySensitiveWords.size() < 1) {
            return;
        }
        log.info("加载行业敏感词条数：{}", industrySensitiveWords.size());

        this.multiSaveSetAndDict(RedisConstant.FILTERS_CONFIG_SYSTEM_WORDS_INFO_TYPE_SENSITIVE + type, industrySensitiveWords);
        //更新关键词同步状态
        keywordsRepository.updateIsSyncStatus(industrySensitiveWords);
        //发送广播通知
        this.rocketProducerFilterMessage.sendRocketMessage(RedisConstant.MESSAGE_TYPE_INFO_SENSITIVE);
    }

    /**
     * 加载通道敏感词到缓存
     */
    @Async
    public void loadChannelSensitiveWords(String businessId) {
        //加载数据
        List<FilterKeyWordsInfoValidator> channelSensitiveWords = keywordsRepository.loadWords("CHANNEL", businessId, "BLACK");
        if (null == channelSensitiveWords || channelSensitiveWords.size() < 1) {
            return;
        }
        log.info("加载通道敏感词条数：{}", channelSensitiveWords.size());
        this.multiSaveSetBatch(RedisConstant.FILTERS_CONFIG_CHANNEL_SENSITIVE, channelSensitiveWords);
        //更新关键词同步状态
        keywordsRepository.updateIsSyncStatus(channelSensitiveWords);
        //发送广播通知
        this.rocketProducerFilterMessage.sendRocketMessage(RedisConstant.MESSAGE_CHANNEL_SENSITIVE);
    }

    /**
     * 加载业务账号敏感词到缓存
     */
    @Async
    public void loadAccountSensitiveWords(String businessId) {
        //加载数据
        List<FilterKeyWordsInfoValidator> accountSensitiveWords = keywordsRepository.loadWords("BUSINESS_ACCOUNT", businessId, "BLACK");
        if (null == accountSensitiveWords || accountSensitiveWords.size() < 1) {
            return;
        }
        log.info("加载业务账号敏感词条数：{}", accountSensitiveWords.size());
        this.multiSaveSetBatch(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_SENSITIVE, accountSensitiveWords);
        //更新关键词同步状态
        keywordsRepository.updateIsSyncStatus(accountSensitiveWords);
        //发送广播通知
        this.rocketProducerFilterMessage.sendRocketMessage(RedisConstant.MESSAGE_ACCOUNT_SENSITIVE);
    }

    /**
     * 加载业务账号审核词到缓存
     */
    @Async
    public void loadAccountCheckWords(String businessId) {
        //加载数据
        List<FilterKeyWordsInfoValidator> accountCheckWords = keywordsRepository.loadWords("BUSINESS_ACCOUNT", businessId, "CHECK");
        if (null == accountCheckWords || accountCheckWords.size() < 1) {
            return;
        }
        log.info("加载业务账号审核词条数：{}", accountCheckWords.size());
        this.multiSaveSetBatch(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_CHECK, accountCheckWords);
        //更新关键词同步状态
        keywordsRepository.updateIsSyncStatus(accountCheckWords);
        //发送广播通知
        this.rocketProducerFilterMessage.sendRocketMessage(RedisConstant.MESSAGE_ACCOUNT_CHECK);
    }

    /**
     * 加载业务账号洗敏白词到缓存
     */
    @Async
    public void loadAccountWhiteSensitiveWords(String businessId) {
        //加载数据
        List<FilterKeyWordsInfoValidator> whiteSensitiveWords = keywordsRepository.loadWords("BUSINESS_ACCOUNT", businessId, "WHITE_AVOID_BLACK");
        if (null == whiteSensitiveWords || whiteSensitiveWords.size() < 1) {
            return;
        }
        log.info("加载业务账号洗敏白词条数：{}", whiteSensitiveWords.size());
        this.multiSaveHash(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_WHITE_SENSITIVE + businessId, whiteSensitiveWords);
        //更新关键词同步状态
        keywordsRepository.updateIsSyncStatus(whiteSensitiveWords);
    }

    /**
     * 加载业务账号免审白词到缓存
     */
    @Async
    public void loadAccountWhiteCheckWords(String businessId) {
        //加载数据
        List<FilterKeyWordsInfoValidator> whiteCheckWords = keywordsRepository.loadWords("BUSINESS_ACCOUNT", businessId, "WHITE_AVOID_CHECK");
        if (null == whiteCheckWords || whiteCheckWords.size() < 1) {
            return;
        }
        log.info("加载业务账号免审白词条数：{}", whiteCheckWords.size());
        this.multiSaveHash(RedisConstant.FILTERS_CONFIG_ACCOUNT_WORDS_WHITE_NO_CHECK + businessId, whiteCheckWords);
        //更新关键词同步状态
        keywordsRepository.updateIsSyncStatus(whiteCheckWords);
    }


    /**
     * 批量保存到Set  行业敏感词专用
     *
     * @param key
     * @param list
     */
    public void multiSaveSet(String key, List<FilterKeyWordsInfoValidator> list) {
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            connection.openPipeline();
            list.forEach((value) -> {
                connection.sAdd(RedisSerializer.string().serialize(key), RedisSerializer.string().serialize(new Gson().toJson(value.getKeyWords())));

            });
            connection.close();
            return null;
        });
    }

    /**
     * 批量保存到Set，并且保存字典
     *
     * @param key
     * @param list
     */
    public void multiSaveSetAndDict(String key, List<FilterKeyWordsInfoValidator> list) {
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            connection.openPipeline();
            list.forEach((value) -> {
                connection.sAdd(RedisSerializer.string().serialize(RedisConstant.FILTERS_CONFIG_SYSTEM_WORDS_INFO_TYPE), RedisSerializer.string().serialize(new Gson().toJson(value.getBusinessId())));
                connection.sAdd(RedisSerializer.string().serialize(key), RedisSerializer.string().serialize(new Gson().toJson(value.getKeyWords())));
            });
            connection.close();
            return null;
        });
    }

    /**
     * multiSaveSetBatch Set 同时维护一个list
     *
     * @param redisKey
     * @param list
     */
    public void multiSaveSetBatch(String redisKey, List<FilterKeyWordsInfoValidator> list) {
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            connection.openPipeline();
            list.forEach((value) -> {
                connection.sAdd(RedisSerializer.string().serialize(redisKey + "list"), RedisSerializer.string().serialize(new Gson().toJson(value.getBusinessId())));
                connection.sAdd(RedisSerializer.string().serialize(redisKey + value.getBusinessId()), RedisSerializer.string().serialize(new Gson().toJson(value.getKeyWords())));
            });
            connection.close();
            return null;
        });
    }

    /**
     * 批量保存到Hash
     *
     * @param list
     * @param redisKey
     */
    public void multiSaveHash(String redisKey, List<FilterKeyWordsInfoValidator> list) {
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            connection.openPipeline();
            list.forEach((value) -> {
                connection.hSet(RedisSerializer.string().serialize(redisKey), RedisSerializer.string().serialize(value.getWaskKeyWords()), RedisSerializer.string().serialize(new Gson().toJson(value.getKeyWords())));
            });
            connection.close();
            return null;
        });
    }

    /**
     * 从Set中删除
     *
     * @param redisKey
     * @param keyWord
     */
    public void delFromSet(String redisKey, String keyWord) {
        redisTemplate.opsForSet().remove(redisKey, keyWord);
    }

    /**
     * 从Hash中删除
     *
     * @param redisKey
     * @param keyWord
     */
    public void delFromHash(String redisKey, String keyWord) {
        redisTemplate.opsForHash().delete(redisKey, keyWord);
    }
}
