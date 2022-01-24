package com.smoc.cloud.identification.service;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.identification.validator.IdentificationRequestDataValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.identification.entity.IdentificationRequestData;
import com.smoc.cloud.identification.repository.IdentificationRequestDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Optional;

@Slf4j
@Service
public class IdentificationRequestDataService {

    @Resource
    private IdentificationRequestDataRepository identificationRequestDataRepository;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    public PageList<IdentificationRequestDataValidator> page(PageParams<IdentificationRequestDataValidator> pageParams) {
        return identificationRequestDataRepository.page(pageParams);
    }

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    public ResponseData findById(String id) {
        Optional<IdentificationRequestData> data = identificationRequestDataRepository.findById(id);

        if (!data.isPresent()) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }

        IdentificationRequestData entity = data.get();
        IdentificationRequestDataValidator identificationRequestDataValidator = new IdentificationRequestDataValidator();
        BeanUtils.copyProperties(entity, identificationRequestDataValidator);

        //转换日期
        identificationRequestDataValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(entity.getCreatedTime()));

        return ResponseDataUtil.buildSuccess(identificationRequestDataValidator);
    }

    /**
     * 保存或修改
     *
     * @param identificationRequestDataValidator
     * @param op  操作类型 为add、edit
     * @return
     */
    @Transactional
    public ResponseData save(IdentificationRequestDataValidator identificationRequestDataValidator, String op) {

        IdentificationRequestData entity = new IdentificationRequestData();
        BeanUtils.copyProperties(identificationRequestDataValidator, entity);

        //转换日期格式
        entity.setCreatedTime(DateTimeUtils.getDateTimeFormat(identificationRequestDataValidator.getCreatedTime()));

        //op 不为 edit 或 add
        if (!("edit".equals(op) || "add".equals(op))) {
            return ResponseDataUtil.buildError();
        }

        //记录日志
        log.info("[身份认证订单][{}]数据:{}", op, JSON.toJSONString(entity));
        identificationRequestDataRepository.saveAndFlush(entity);

        return ResponseDataUtil.buildSuccess();
    }
}
