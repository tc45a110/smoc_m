package com.smoc.cloud.intelligence.service;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.intelligence.IntellectMaterialValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.intelligence.entity.IntellectMaterial;
import com.smoc.cloud.intelligence.repository.IntellectMaterialRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class IntellectMaterialService {

    @Resource
    private IntellectMaterialRepository intellectMaterialRepository;

    public ResponseData<List<IntellectMaterial>> findIntellectMaterialByMaterialTypeId(String materialTypeId) {
        List<IntellectMaterial> list = intellectMaterialRepository.findIntellectMaterialByMaterialTypeId(materialTypeId);
        return ResponseDataUtil.buildSuccess(list);
    }

    /**
     * 保存或修改
     *
     * @param intellectMaterialValidator
     * @param op                         操作类型 为add、edit
     * @return
     */
    @Transactional
    public ResponseData save(IntellectMaterialValidator intellectMaterialValidator, String op) {


        //op 不为 edit 或 add
        if (!("edit".equals(op) || "add".equals(op))) {
            return ResponseDataUtil.buildError();
        }

        Iterable<IntellectMaterial> data = intellectMaterialRepository.findIntellectMaterialByMaterialTypeIdAndMaterialName(intellectMaterialValidator.getMaterialTypeId(), intellectMaterialValidator.getMaterialName());

        IntellectMaterial entity = new IntellectMaterial();
        BeanUtils.copyProperties(intellectMaterialValidator, entity);
        //转换日期格式
        entity.setCreatedTime(DateTimeUtils.getDateTimeFormat(intellectMaterialValidator.getCreatedTime()));

        //add查重
        if (data != null && data.iterator().hasNext() && "add".equals(op)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
        }
        //edit查重
        else if (data != null && data.iterator().hasNext() && "edit".equals(op)) {
            boolean status = false;
            Iterator iter = data.iterator();
            while (iter.hasNext()) {
                IntellectMaterial info = (IntellectMaterial) iter.next();
                if (!entity.getId().equals(info.getId())) {
                    status = true;
                    break;
                }
            }
            if (status) {
                return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
            }
        }

        //记录日志
        log.info("[智能短信][素材管理][{}]数据:{}", op, JSON.toJSONString(entity));
        intellectMaterialRepository.saveAndFlush(entity);
        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    public ResponseData<IntellectMaterialValidator> findById(String id) {
        Optional<IntellectMaterial> data = intellectMaterialRepository.findById(id);

        if (!data.isPresent()) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }

        IntellectMaterial entity = data.get();
        IntellectMaterialValidator intellectMaterialValidator = new IntellectMaterialValidator();
        BeanUtils.copyProperties(entity, intellectMaterialValidator);


        //转换日期
        intellectMaterialValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(entity.getCreatedTime()));
        log.info("[智能短信][素材管理]数据:{}", JSON.toJSONString(intellectMaterialValidator));
        return ResponseDataUtil.buildSuccess(intellectMaterialValidator);
    }
}
