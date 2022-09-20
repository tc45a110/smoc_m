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
import com.smoc.cloud.common.smoc.configuate.validator.SystemSegmentProvinceCityValidator;
import com.smoc.cloud.common.smoc.filter.ExcelModel;
import com.smoc.cloud.common.smoc.filter.FilterBlackListValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.configure.number.entity.ConfigNumberInfo;
import com.smoc.cloud.configure.number.entity.SystemSegmentProvinceCity;
import com.smoc.cloud.configure.number.repository.ConfigNumberInfoRepository;
import com.smoc.cloud.configure.number.repository.SegmentProvinceCityRepository;
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
 * 省号码管理
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SegmentProvinceCityService {

    @Resource
    private SegmentProvinceCityRepository segmentProvinceCityRepository;

    @Resource(name = "defaultRedisTemplate")
    private RedisTemplate redisTemplate;

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    public PageList<SystemSegmentProvinceCityValidator> page(PageParams<SystemSegmentProvinceCityValidator> pageParams) {
        return segmentProvinceCityRepository.page(pageParams);
    }

    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    public ResponseData findById(String id) {
        Optional<SystemSegmentProvinceCity> data = segmentProvinceCityRepository.findById(id);
        if(!data.isPresent()){
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }

        SystemSegmentProvinceCity entity = data.get();
        SystemSegmentProvinceCityValidator systemSegmentProvinceCityValidator = new SystemSegmentProvinceCityValidator();
        BeanUtils.copyProperties(entity, systemSegmentProvinceCityValidator);

        return ResponseDataUtil.buildSuccess(systemSegmentProvinceCityValidator);
    }

    /**
     * 保存或修改
     *
     * @param systemSegmentProvinceCityValidator
     * @param op     操作类型 为add、edit
     * @return
     */
    @Transactional
    public ResponseData<ConfigNumberInfo> save(SystemSegmentProvinceCityValidator systemSegmentProvinceCityValidator, String op) {

        //转BaseUser存放对象
        SystemSegmentProvinceCity entity = new SystemSegmentProvinceCity();
        BeanUtils.copyProperties(systemSegmentProvinceCityValidator, entity);

        List<SystemSegmentProvinceCity> data = segmentProvinceCityRepository.findBySegmentAndProvinceCode(systemSegmentProvinceCityValidator.getSegment(),systemSegmentProvinceCityValidator.getProvinceCode());

        //add查重
        if (data != null && data.iterator().hasNext() && "add".equals(op)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
        }
        //edit查重orgName
        else if (data != null && data.iterator().hasNext() && "edit".equals(op)) {
            boolean status = false;
            Iterator iter = data.iterator();
            while (iter.hasNext()) {
                SystemSegmentProvinceCity organization = (SystemSegmentProvinceCity) iter.next();
                if (!entity.getId().equals(organization.getId())) {
                    status = true;
                    break;
                }
            }
            if (status) {
                return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
            }
        }


        //记录日志
        log.info("[省号码配置][{}]数据:{}",op, JSON.toJSONString(systemSegmentProvinceCityValidator));

        entity.setIsSync("1");

        segmentProvinceCityRepository.saveAndFlush(entity);

        redisTemplate.opsForHash().put(RedisConstant.FILTERS_CONFIG_SYSTEM_PROVINCE_NUMBER , entity.getSegment(), entity.getProvinceCode()+"-"+entity.getProvinceName());
        return ResponseDataUtil.buildSuccess();
    }

    @Transactional
    public ResponseData deleteById(String id) {
        SystemSegmentProvinceCity data = segmentProvinceCityRepository.findById(id).get();

        //记录日志
        log.info("[省号码配置][delete]数据:{}", JSON.toJSONString(data));
        segmentProvinceCityRepository.deleteById(id);

        redisTemplate.opsForHash().delete(RedisConstant.FILTERS_CONFIG_SYSTEM_PROVINCE_NUMBER, data.getSegment());

        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 批量保存
     * @param systemSegmentProvinceCityValidator
     * @return
     */
    @Async
    public void batchSave(SystemSegmentProvinceCityValidator systemSegmentProvinceCityValidator) {
        segmentProvinceCityRepository.batchSave(systemSegmentProvinceCityValidator);

        this.loadSystemSegmentProvinceList(systemSegmentProvinceCityValidator);
    }

    public void loadSystemSegmentProvinceList(SystemSegmentProvinceCityValidator systemSegmentProvinceCityValidator) {

        List<ExcelModel> provinceList = systemSegmentProvinceCityValidator.getExcelModelList();
        if (null == provinceList || provinceList.size() < 1) {
            return;
        }

        log.info("[省号码导入redis开始]数据：{}", System.currentTimeMillis());

        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            connection.openPipeline();
            provinceList.forEach((value) -> {
                connection.hSet(RedisSerializer.string().serialize(RedisConstant.FILTERS_CONFIG_SYSTEM_PROVINCE_NUMBER), RedisSerializer.string().serialize(value.getColumn1()), RedisSerializer.string().serialize(new Gson().toJson(systemSegmentProvinceCityValidator.getProvinceCode()+"-"+systemSegmentProvinceCityValidator.getProvinceName())));
            });
            connection.close();
            return null;
        });

        log.info("[省号码导入redis结束]数据：{}", System.currentTimeMillis());
    }
}
