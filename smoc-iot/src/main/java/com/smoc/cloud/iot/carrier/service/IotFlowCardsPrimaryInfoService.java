package com.smoc.cloud.iot.carrier.service;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.iot.validator.IotFlowCardsPrimaryInfoValidator;
import com.smoc.cloud.common.iot.validator.IotFlowCardsSecondaryInfoValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.iot.carrier.entity.IotCarrierFlowPool;
import com.smoc.cloud.iot.carrier.entity.IotFlowCardsPrimaryInfo;
import com.smoc.cloud.iot.carrier.entity.IotFlowCardsSecondaryInfo;
import com.smoc.cloud.iot.carrier.repository.IotFlowCardsPrimaryInfoRepository;
import com.smoc.cloud.iot.carrier.repository.IotFlowCardsSecondaryInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class IotFlowCardsPrimaryInfoService {

    @Resource
    private IotFlowCardsPrimaryInfoRepository iotFlowCardsPrimaryInfoRepository;

    @Resource
    private IotFlowCardsSecondaryInfoRepository iotFlowCardsSecondaryInfoRepository;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<IotFlowCardsPrimaryInfoValidator>> page(PageParams<IotFlowCardsPrimaryInfoValidator> pageParams) {
        PageList<IotFlowCardsPrimaryInfoValidator> page = iotFlowCardsPrimaryInfoRepository.page(pageParams);
        return ResponseDataUtil.buildSuccess(page);
    }

    /**
     * 根据ID 查询数据
     *
     * @param id
     * @return
     */
    public ResponseData<Map<String, Object>> findById(String id) {
        Optional<IotFlowCardsPrimaryInfo> data = iotFlowCardsPrimaryInfoRepository.findById(id);
        if (!data.isPresent()) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }

        Map<String, Object> result = new HashMap<>();
        IotFlowCardsPrimaryInfoValidator validator = new IotFlowCardsPrimaryInfoValidator();
        BeanUtils.copyProperties(data.get(), validator);
        //转换日期
        validator.setCreatedTime(DateTimeUtils.getDateTimeFormat(data.get().getCreatedTime()));
        result.put("primary", validator);

        Optional<IotFlowCardsSecondaryInfo> secondaryData = iotFlowCardsSecondaryInfoRepository.findById(id);
        IotFlowCardsSecondaryInfoValidator secondary = new IotFlowCardsSecondaryInfoValidator();
        BeanUtils.copyProperties(secondaryData.get(), secondary);
        result.put("secondary", secondary);
        return ResponseDataUtil.buildSuccess(result);
    }

    /**
     * 保存或修改
     *
     * @param map
     * @param op  操作类型 为add、edit
     * @return
     */
    @Transactional
    public ResponseData save(Map<String, Object> map, String op) {

        IotFlowCardsPrimaryInfoValidator validator = (IotFlowCardsPrimaryInfoValidator) map.get("primary");
        Iterable<IotFlowCardsPrimaryInfo> data = iotFlowCardsPrimaryInfoRepository.findByMsisdnOrImsiOrIccid(validator.getMsisdn(), validator.getImsi(), validator.getIccid());

        IotFlowCardsPrimaryInfo entity = new IotFlowCardsPrimaryInfo();
        BeanUtils.copyProperties(validator, entity);

        //add查重
        if (data != null && data.iterator().hasNext() && "add".equals(op)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
        }
        //edit查重
        else if (data != null && data.iterator().hasNext() && "edit".equals(op)) {
            boolean status = false;
            Iterator iterator = data.iterator();
            while (iterator.hasNext()) {
                IotCarrierFlowPool info = (IotCarrierFlowPool) iterator.next();
                if (!entity.getId().equals(info.getId())) {
                    status = true;
                    break;
                }
            }
            if (status) {
                return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
            }
        }

        //转换日期格式
        entity.setCreatedTime(DateTimeUtils.getDateTimeFormat(validator.getCreatedTime()));

        //op 不为 edit 或 add
        if (!("edit".equals(op) || "add".equals(op))) {
            return ResponseDataUtil.buildError();
        }

        IotFlowCardsSecondaryInfoValidator secondary = (IotFlowCardsSecondaryInfoValidator) map.get("secondary");
        if (null != secondary) {
            IotFlowCardsSecondaryInfo secondaryModel = new IotFlowCardsSecondaryInfo();
            BeanUtils.copyProperties(secondary, secondaryModel);
            iotFlowCardsSecondaryInfoRepository.saveAndFlush(secondaryModel);
        }

        //记录日志
        log.info("[运营接入][{}]数据:{}", op, JSON.toJSONString(entity));
        iotFlowCardsPrimaryInfoRepository.saveAndFlush(entity);

        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 禁用
     *
     * @param id
     */
    public ResponseData forbidden(String id) {
        this.iotFlowCardsPrimaryInfoRepository.forbidden(id, "0");
        return ResponseDataUtil.buildSuccess();
    }
}
