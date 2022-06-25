package com.smoc.cloud.configure.number.service;


import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.smoc.cloud.common.filters.utils.RedisConstant;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigNumberInfoValidator;
import com.smoc.cloud.common.smoc.filter.ExcelModel;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.configure.number.entity.ConfigNumberInfo;
import com.smoc.cloud.configure.number.repository.ConfigNumberInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * 号段管理
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ConfigNumberInfoService {

    @Resource
    private ConfigNumberInfoRepository configNumberInfoRepository;

    @Resource(name = "redisTemplate3")
    private RedisTemplate redisTemplate;

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    public PageList<ConfigNumberInfoValidator> page(PageParams<ConfigNumberInfoValidator> pageParams) {
        return configNumberInfoRepository.page(pageParams);
    }

    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    public ResponseData findById(String id) {
        Optional<ConfigNumberInfo> data = configNumberInfoRepository.findById(id);
        if(!data.isPresent()){
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }

        ConfigNumberInfo entity = data.get();
        ConfigNumberInfoValidator codeNumberInfoValidator = new ConfigNumberInfoValidator();
        BeanUtils.copyProperties(entity, codeNumberInfoValidator);

        //转换日期
        codeNumberInfoValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(entity.getCreatedTime()));

        return ResponseDataUtil.buildSuccess(codeNumberInfoValidator);
    }

    /**
     * 保存或修改
     *
     * @param codeNumberInfoValidator
     * @param op     操作类型 为add、edit
     * @return
     */
    @Transactional
    public ResponseData<ConfigNumberInfo> save(ConfigNumberInfoValidator codeNumberInfoValidator, String op) {

        //转BaseUser存放对象
        ConfigNumberInfo entity = new ConfigNumberInfo();
        BeanUtils.copyProperties(codeNumberInfoValidator, entity);

        List<ConfigNumberInfo> data = configNumberInfoRepository.findByNumberCodeAndCarrierAndNumberCodeType(codeNumberInfoValidator.getNumberCode(),codeNumberInfoValidator.getCarrier(),codeNumberInfoValidator.getNumberCodeType());

        //add查重
        if (data != null && data.iterator().hasNext() && "add".equals(op)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
        }
        //edit查重orgName
        else if (data != null && data.iterator().hasNext() && "edit".equals(op)) {
            boolean status = false;
            Iterator iter = data.iterator();
            while (iter.hasNext()) {
                ConfigNumberInfo organization = (ConfigNumberInfo) iter.next();
                if (!entity.getId().equals(organization.getId())) {
                    status = true;
                    break;
                }
            }
            if (status) {
                return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
            }
        }

        entity.setCreatedTime(new Date());

        entity.setIsSync("1");

        //记录日志
        log.info("[携号转网][{}]数据:{}",op, JSON.toJSONString(codeNumberInfoValidator));

        configNumberInfoRepository.saveAndFlush(entity);

        //放到redis里
        redisTemplate.opsForValue().set(RedisConstant.MOBILE_PORTABILITY + entity.getNumberCode(),entity.getCarrier());

        return ResponseDataUtil.buildSuccess();
    }

    @Transactional
    public ResponseData deleteById(String id) {
        ConfigNumberInfo data = configNumberInfoRepository.findById(id).get();

        //记录日志
        log.info("[携号转网][delete]数据:{}", JSON.toJSONString(data));
        configNumberInfoRepository.deleteById(id);

        redisTemplate.delete(RedisConstant.MOBILE_PORTABILITY + data.getNumberCode());

        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 批量保存
     * @param configNumberInfoValidator
     * @return
     */
    @Async
    public void batchSave(ConfigNumberInfoValidator configNumberInfoValidator) {
        configNumberInfoRepository.batchSave(configNumberInfoValidator);

        this.loadPortabilityList(configNumberInfoValidator);
    }

    public void loadPortabilityList(ConfigNumberInfoValidator configNumberInfoValidator) {
        List<ExcelModel> list = configNumberInfoValidator.getExcelModelList();

        if (null == list || list.size() < 1) {
            return;
        }

        log.info("[携号转网导入redis结束]数据：{}", System.currentTimeMillis());

        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            connection.openPipeline();
            list.forEach((value) -> {
                connection.set(RedisSerializer.string().serialize(RedisConstant.MOBILE_PORTABILITY + value.getColumn1()),
                        RedisSerializer.string().serialize(new Gson().toJson(configNumberInfoValidator.getCarrier())));
            });
            connection.close();
            return null;
        });

        log.info("[携号转网导入redis结束]数据：{}", System.currentTimeMillis());

        //变更加载状态
        //this.configNumberInfoRepository.bathUpdateStatus(list);
    }
}
