package com.smoc.cloud.identification.service;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.identification.validator.IdentificationAccountInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.identification.entity.IdentificationAccountInfo;
import com.smoc.cloud.identification.repository.IdentificationAccountInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Optional;

@Slf4j
@Service
public class IdentificationAccountInfoService {

    @Resource
    private IdentificationAccountInfoRepository identificationAccountInfoRepository;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    public PageList<IdentificationAccountInfoValidator> page(PageParams<IdentificationAccountInfoValidator> pageParams) {
        return identificationAccountInfoRepository.page(pageParams);
    }

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    public ResponseData findById(String id) {
        Optional<IdentificationAccountInfo> data = identificationAccountInfoRepository.findById(id);

        if (!data.isPresent()) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }

        IdentificationAccountInfo entity = data.get();
        IdentificationAccountInfoValidator identificationAccountInfoValidator = new IdentificationAccountInfoValidator();
        BeanUtils.copyProperties(entity, identificationAccountInfoValidator);

        //转换日期
        identificationAccountInfoValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(entity.getCreatedTime()));

        return ResponseDataUtil.buildSuccess(identificationAccountInfoValidator);
    }

    /**
     * 保存或修改
     *
     * @param identificationAccountInfoValidator
     * @param op  操作类型 为add、edit
     * @return
     */
    @Transactional
    public ResponseData save(IdentificationAccountInfoValidator identificationAccountInfoValidator, String op) {

        IdentificationAccountInfo entity = new IdentificationAccountInfo();
        BeanUtils.copyProperties(identificationAccountInfoValidator, entity);

        //转换日期格式
        entity.setCreatedTime(DateTimeUtils.getDateTimeFormat(identificationAccountInfoValidator.getCreatedTime()));

        //op 不为 edit 或 add
        if (!("edit".equals(op) || "add".equals(op))) {
            return ResponseDataUtil.buildError();
        }

        //记录日志
        log.info("[身份认证开户][{}]数据:{}", op, JSON.toJSONString(entity));
        identificationAccountInfoRepository.saveAndFlush(entity);

        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 注销账号
     * @param id
     * @return
     */
    public ResponseData logoutAccount(String id){
        identificationAccountInfoRepository.logoutAccount(id,"LOGOUT");
        return ResponseDataUtil.buildSuccess();
    }

}
