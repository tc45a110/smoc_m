package com.smoc.cloud.parameter.service;


import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.smoc.cloud.common.filters.utils.RedisConstant;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.parameter.ParameterExtendFiltersValueValidator;
import com.smoc.cloud.parameter.entity.ParameterExtendFiltersValue;
import com.smoc.cloud.parameter.repository.ParameterExtendFiltersValueRepository;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业务扩展字段值
 */
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ParameterExtendFiltersValueService {

    @Resource(name = "defaultRedisTemplate")
    private RedisTemplate redisTemplate;

    @Resource
    private ParameterExtendFiltersValueRepository parameterExtendFiltersValueRepository;

    /**
     * 根据业务id 查询业务扩展字段值
     *
     * @param businessId
     * @return
     */
    public ResponseData<List<ParameterExtendFiltersValue>> findParameterExtendFiltersValueByBusinessId(String businessId) {
        log.info("[businessId]:{}", businessId);
        List<ParameterExtendFiltersValue> data = parameterExtendFiltersValueRepository.findParameterExtendFiltersValueByBusinessId(businessId);
        return ResponseDataUtil.buildSuccess(data);
    }

    /**
     * 添加、修改,每次提交就会把原来数据删除
     *
     * @param list       要保存的数据列表
     * @param businessId 业务id
     * @return
     */
    @Transactional
    public ResponseData save(List<ParameterExtendFiltersValueValidator> list, String businessId) {

        parameterExtendFiltersValueRepository.deleteByBusinessId(businessId);
        parameterExtendFiltersValueRepository.batchSave(list);

        //记录日志
        log.info("[过滤扩展字段][批量保存]数据:{}", JSON.toJSONString(list));
        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 重新加载
     *
     * @param businessId
     */
    @Async
    public void reloadRedisCache(String businessId, List<ParameterExtendFiltersValueValidator> list) {
        if (null == list || list.size() < 1) {
            return;
        }
        //
        this.deleteRedisCache(businessId);

        //约定的配置参数
        Map<String, String> commonMap = new HashMap<>();
        //号码拦截参数
        String numberBlack = "";
        //号码洗白参数
        String numberWhite = "";
        //内容拦截参数
        String messageBlack = "";
        //内容洗白参数
        String messageWhite = "";

        for (ParameterExtendFiltersValueValidator validator : list) {
            if (validator.getParamKey().startsWith("COMMON_")) {
                commonMap.put(validator.getParamKey(), validator.getParamValue());
                continue;
            }
            if (validator.getParamKey().startsWith("NUMBER_BLACK_")) {
                if (StringUtils.isEmpty(numberBlack)) {
                    numberBlack = validator.getParamValue();
                } else {
                    numberBlack = numberBlack + "|" + validator.getParamValue();
                }
                continue;
            }
            if (validator.getParamKey().startsWith("NUMBER_WHITE_")) {
                if (StringUtils.isEmpty(numberWhite)) {
                    numberWhite = validator.getParamValue();
                } else {
                    numberWhite = numberWhite + "|" + validator.getParamValue();
                }
                continue;
            }
            if (validator.getParamKey().startsWith("MESSAGE_BLACK_")) {
                if (StringUtils.isEmpty(messageBlack)) {
                    messageBlack = validator.getParamValue();
                } else {
                    messageBlack = messageBlack + "|" + validator.getParamValue();
                }
                continue;
            }
            if (validator.getParamKey().startsWith("MESSAGE_WHITE_")) {
                if (StringUtils.isEmpty(messageWhite)) {
                    messageWhite = validator.getParamValue();
                } else {
                    messageWhite = messageWhite + "|" + validator.getParamValue();
                }
                continue;
            }
        }

        if (commonMap.size() > 0) {
            multiSaveHash(commonMap, RedisConstant.FILTERS_CONFIG_ACCOUNT_COMMON + businessId);
        }

        if (null != numberBlack && !"".equals(numberBlack)) {
            redisTemplate.opsForValue().set(RedisConstant.FILTERS_CONFIG_ACCOUNT_NUMBER + "black:" + businessId, numberBlack);
        }
        if (null != numberWhite && !"".equals(numberWhite)) {
            redisTemplate.opsForValue().set(RedisConstant.FILTERS_CONFIG_ACCOUNT_NUMBER + "white:" + businessId, numberWhite);
        }
        if (null != messageBlack && !"".equals(messageBlack)) {
            redisTemplate.opsForValue().set(RedisConstant.FILTERS_CONFIG_ACCOUNT_MESSAGE + "black:" + businessId, messageBlack);
        }

        if (null != messageWhite && !"".equals(messageWhite)) {
            redisTemplate.opsForValue().set(RedisConstant.FILTERS_CONFIG_ACCOUNT_MESSAGE + "white:" + businessId, messageWhite);
        }
    }

    /**
     * 批量保存到Hash
     *
     * @param source
     * @param prefix
     */
    public void multiSaveHash(Map<String, String> source, String prefix) {
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            connection.openPipeline();
            source.forEach((key, value) -> {
                connection.hSet(RedisSerializer.string().serialize(prefix),
                        RedisSerializer.string().serialize(key), RedisSerializer.string().serialize(new Gson().toJson(value)));
            });
            connection.close();
            return null;
        });
    }

    /**
     * 删除以前缓存的数据
     *
     * @param businessId
     */
    public void deleteRedisCache(String businessId) {
        this.redisTemplate.delete(RedisConstant.FILTERS_CONFIG_ACCOUNT_COMMON + businessId);
        this.redisTemplate.delete(RedisConstant.FILTERS_CONFIG_ACCOUNT_NUMBER + "black:" + businessId);
        this.redisTemplate.delete(RedisConstant.FILTERS_CONFIG_ACCOUNT_NUMBER + "white:" + businessId);
        this.redisTemplate.delete(RedisConstant.FILTERS_CONFIG_ACCOUNT_MESSAGE + "black:" + businessId);
        this.redisTemplate.delete(RedisConstant.FILTERS_CONFIG_ACCOUNT_MESSAGE + "white:" + businessId);
    }
}
