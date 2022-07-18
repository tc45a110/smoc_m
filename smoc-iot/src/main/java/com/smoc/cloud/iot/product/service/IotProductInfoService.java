package com.smoc.cloud.iot.product.service;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.iot.validator.IotProductInfoValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.iot.product.entity.IotProductInfo;
import com.smoc.cloud.iot.product.repository.IotProductInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.Optional;

@Slf4j
@Service
public class IotProductInfoService {

    @Resource
    private IotProductInfoRepository iotProductInfoRepository;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<IotProductInfoValidator>> page(PageParams<IotProductInfoValidator> pageParams) {
        PageList<IotProductInfoValidator> page = iotProductInfoRepository.page(pageParams);
        return ResponseDataUtil.buildSuccess(page);
    }

    /**
     * 根据ID 查询数据
     *
     * @param id
     * @return
     */
    public ResponseData<IotProductInfoValidator> findById(String id) {
        Optional<IotProductInfo> data = iotProductInfoRepository.findById(id);

        if (!data.isPresent()) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }

        IotProductInfoValidator validator = new IotProductInfoValidator();
        BeanUtils.copyProperties(data.get(), validator);
        //转换日期
        validator.setCreatedTime(DateTimeUtils.getDateTimeFormat(data.get().getCreatedTime()));
        return ResponseDataUtil.buildSuccess(validator);
    }

    /**
     * 保存或修改
     *
     * @param validator
     * @param op        操作类型 为add、edit
     * @return
     */
    @Transactional
    public ResponseData save(IotProductInfoValidator validator, String op) {

        Iterable<IotProductInfo> data = iotProductInfoRepository.findByProductName(validator.getProductName());

        IotProductInfo entity = new IotProductInfo();
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
                IotProductInfo info = (IotProductInfo) iterator.next();
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

        //记录日志
        log.info("[运营接入][{}]数据:{}", op, JSON.toJSONString(entity));
        iotProductInfoRepository.saveAndFlush(entity);

        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 禁用
     *
     * @param id
     */
    public ResponseData forbidden(String id) {
        this.iotProductInfoRepository.forbidden(id, "0");
        return ResponseDataUtil.buildSuccess();
    }
}
