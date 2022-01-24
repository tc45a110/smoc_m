package com.smoc.cloud.identification.service;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.identification.validator.IdentificationOrdersInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.identification.entity.IdentificationOrdersInfo;
import com.smoc.cloud.identification.repository.IdentificationOrdersInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Optional;

@Slf4j
@Service
public class IdentificationOrdersInfoService {

    @Resource
    private IdentificationOrdersInfoRepository identificationOrdersInfoRepository;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    public PageList<IdentificationOrdersInfoValidator> page(PageParams<IdentificationOrdersInfoValidator> pageParams) {
        return identificationOrdersInfoRepository.page(pageParams);
    }

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    public ResponseData findById(String id) {
        Optional<IdentificationOrdersInfo> data = identificationOrdersInfoRepository.findById(id);

        if (!data.isPresent()) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }

        IdentificationOrdersInfo entity = data.get();
        IdentificationOrdersInfoValidator identificationOrdersInfoValidator = new IdentificationOrdersInfoValidator();
        BeanUtils.copyProperties(entity, identificationOrdersInfoValidator);

        //转换日期
        identificationOrdersInfoValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(entity.getCreatedTime()));

        return ResponseDataUtil.buildSuccess(identificationOrdersInfoValidator);
    }

    /**
     * 保存或修改
     *
     * @param identificationOrdersInfoValidator
     * @param op  操作类型 为add、edit
     * @return
     */
    @Transactional
    public ResponseData save(IdentificationOrdersInfoValidator identificationOrdersInfoValidator, String op) {

        IdentificationOrdersInfo entity = new IdentificationOrdersInfo();
        BeanUtils.copyProperties(identificationOrdersInfoValidator, entity);

        //转换日期格式
        entity.setCreatedTime(DateTimeUtils.getDateTimeFormat(identificationOrdersInfoValidator.getCreatedTime()));

        //op 不为 edit 或 add
        if (!("edit".equals(op) || "add".equals(op))) {
            return ResponseDataUtil.buildError();
        }

        //记录日志
        log.info("[身份认证订单][{}]数据:{}", op, JSON.toJSONString(entity));
        identificationOrdersInfoRepository.saveAndFlush(entity);

        return ResponseDataUtil.buildSuccess();
    }
}
