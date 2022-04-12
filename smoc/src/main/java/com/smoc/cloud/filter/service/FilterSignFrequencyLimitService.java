package com.smoc.cloud.filter.service;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.filter.FilterSignFrequencyLimitValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.filter.entity.FilterSignFrequencyLimit;
import com.smoc.cloud.filter.repository.FilterSignFrequencyLimitRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Optional;

@Slf4j
@Service
public class FilterSignFrequencyLimitService {

    @Resource
    private FilterSignFrequencyLimitRepository filterSignFrequencyLimitRepository;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<FilterSignFrequencyLimitValidator>> page(PageParams<FilterSignFrequencyLimitValidator> pageParams) {
        return ResponseDataUtil.buildSuccess(filterSignFrequencyLimitRepository.page(pageParams));
    }

    /**
     * 根据id 查询
     *
     * @param id
     * @return
     */
    public ResponseData<FilterSignFrequencyLimitValidator> findById(String id) {

        Optional<FilterSignFrequencyLimit> data = filterSignFrequencyLimitRepository.findById(id);

        if (!data.isPresent()) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }

        FilterSignFrequencyLimit entity = data.get();
        FilterSignFrequencyLimitValidator filterSignFrequencyLimitValidator = new FilterSignFrequencyLimitValidator();
        BeanUtils.copyProperties(entity, filterSignFrequencyLimitValidator);

        //转换日期
        filterSignFrequencyLimitValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(entity.getCreatedTime()));

        //使用了 Optional 的判断方式，判断优雅
        return ResponseDataUtil.buildSuccess(filterSignFrequencyLimitValidator);
    }

    /**
     * 保存或修改
     *
     * @param filterSignFrequencyLimitValidator
     * @param op                                操作类型 为add、edit
     * @return
     */
    @Transactional
    public ResponseData save(FilterSignFrequencyLimitValidator filterSignFrequencyLimitValidator, String op) {

        //转BaseUser存放对象
        FilterSignFrequencyLimit entity = new FilterSignFrequencyLimit();
        BeanUtils.copyProperties(filterSignFrequencyLimitValidator, entity);
        //转换日期格式
        entity.setCreatedTime(DateTimeUtils.getDateTimeFormat(filterSignFrequencyLimitValidator.getCreatedTime()));

        //op 不为 edit 或 add
        if (!("edit".equals(op) || "add".equals(op))) {
            return ResponseDataUtil.buildError();
        }

        //记录日志
        log.info("[单号签名频次限制管理][{}]数据:{}", op, JSON.toJSONString(entity));

        filterSignFrequencyLimitRepository.saveAndFlush(entity);
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

        Optional<FilterSignFrequencyLimit> data = filterSignFrequencyLimitRepository.findById(id);

        if (!data.isPresent()) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }

        FilterSignFrequencyLimit entity = data.get();
        entity.setStatus("0");

        //记录日志
        log.info("[单号签名频次限制管理][delete]数据:{}", JSON.toJSONString(data));
        filterSignFrequencyLimitRepository.saveAndFlush(entity);

        return ResponseDataUtil.buildSuccess();
    }
}
