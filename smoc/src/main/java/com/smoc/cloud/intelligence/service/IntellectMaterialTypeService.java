package com.smoc.cloud.intelligence.service;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.intelligence.IntellectMaterialTypeValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.intelligence.entity.IntellectMaterialType;
import com.smoc.cloud.intelligence.repository.IntellectMaterialTypeRepository;
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
public class IntellectMaterialTypeService {

    @Resource
    private IntellectMaterialTypeRepository intellectMaterialTypeRepository;

    /**
     * @param parentId
     * @return
     */
    public ResponseData<List<IntellectMaterialType>> findIntellectMaterialTypeByParentIdAndStatus(String parentId) {

        List<IntellectMaterialType> list = intellectMaterialTypeRepository.findIntellectMaterialTypeByParentIdAndStatusOrderByDisplaySortDesc(parentId, "1");

        return ResponseDataUtil.buildSuccess(list);
    }

    /**
     * 保存或修改
     *
     * @param intellectMaterialTypeValidator
     * @param op                             操作类型 为add、edit
     * @return
     */
    @Transactional
    public ResponseData save(IntellectMaterialTypeValidator intellectMaterialTypeValidator, String op) {


        //op 不为 edit 或 add
        if (!("edit".equals(op) || "add".equals(op))) {
            return ResponseDataUtil.buildError();
        }

        Iterable<IntellectMaterialType> data = intellectMaterialTypeRepository.findIntellectMaterialTypeByParentIdAndTitleAndStatus(intellectMaterialTypeValidator.getParentId(), intellectMaterialTypeValidator.getTitle(), "1");

        IntellectMaterialType entity = new IntellectMaterialType();
        BeanUtils.copyProperties(intellectMaterialTypeValidator, entity);
        //转换日期格式
        entity.setCreatedTime(DateTimeUtils.getDateTimeFormat(intellectMaterialTypeValidator.getCreatedTime()));

        //add查重
        if (data != null && data.iterator().hasNext() && "add".equals(op)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
        }
        //edit查重
        else if (data != null && data.iterator().hasNext() && "edit".equals(op)) {
            boolean status = false;
            Iterator iter = data.iterator();
            while (iter.hasNext()) {
                IntellectMaterialType info = (IntellectMaterialType) iter.next();
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
        log.info("[智能短信][素材类型][{}]数据:{}", op, JSON.toJSONString(entity));
        intellectMaterialTypeRepository.saveAndFlush(entity);
        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    public ResponseData<IntellectMaterialTypeValidator> findById(String id) {
        Optional<IntellectMaterialType> data = intellectMaterialTypeRepository.findById(id);

        if (!data.isPresent()) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }

        IntellectMaterialType entity = data.get();
        IntellectMaterialTypeValidator intellectMaterialTypeValidator = new IntellectMaterialTypeValidator();
        BeanUtils.copyProperties(entity, intellectMaterialTypeValidator);


        //转换日期
        intellectMaterialTypeValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(entity.getCreatedTime()));

        return ResponseDataUtil.buildSuccess(intellectMaterialTypeValidator);
    }

    /**
     * 注销或启用
     * @param id
     * @param status
     * @return
     */
    @Transactional
    public ResponseData cancel(String id, String status){
        intellectMaterialTypeRepository.cancel(id,status);
        return ResponseDataUtil.buildSuccess();

    }

}
