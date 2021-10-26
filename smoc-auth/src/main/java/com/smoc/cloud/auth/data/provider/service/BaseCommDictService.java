package com.smoc.cloud.auth.data.provider.service;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.auth.data.provider.entity.BaseCommDict;
import com.smoc.cloud.auth.data.provider.entity.BaseCommDictType;
import com.smoc.cloud.auth.data.provider.repository.BaseCommDictRepository;
import com.smoc.cloud.auth.data.provider.repository.BaseCommDictTypeRepository;
import com.smoc.cloud.common.auth.qo.Dict;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * 公用字典服务
 * 2019/5/21 16:32
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BaseCommDictService {

    @Resource
    private BaseCommDictRepository baseCommDictRepository;

    @Resource
    private BaseCommDictTypeRepository baseCommDictTypeRepository;

    /**
     * 根据系统标识查询字典
     *
     * @param system
     * @return
     */
    public Map<String, List<Dict>> getDict(String system) {

        Map<String, List<Dict>> resultMap = new HashMap<>();

        List<BaseCommDictType> types = baseCommDictTypeRepository.findBaseCommDictTypeByActiveAndDictTypeSystemLike(1,"%"+system+"%");
        for (BaseCommDictType type : types) {
            List<Dict> list = baseCommDictRepository.findDictByTypeId(type.getId());
            resultMap.put(type.getDictTypeCode(), list);
        }

        return resultMap;
    }


    /**
     * 根据typeId dictType查询
     *
     * @return
     */
    public ResponseData<List<BaseCommDict>> listByDictType(String typeId, String dictType) {
        List<BaseCommDict> data = baseCommDictRepository.findBaseCommDictByAndTypeIdAndDictTypeAndActiveOrderBySortAsc(typeId, dictType, 1);
        return ResponseDataUtil.buildSuccess(data);
    }


    /**
     * 根据id 查询
     *
     * @param id
     * @return
     */
    public ResponseData<BaseCommDict> findById(String id) {

        Optional<BaseCommDict> data = baseCommDictRepository.findById(id);

        //使用了 Optional 的判断方式，判断优雅
        return ResponseDataUtil.buildSuccess(data.orElse(null));
    }

    /**
     * 保存或修改
     *
     * @param entity
     * @param op     操作类型 为add、edit
     * @return
     */
    @Transactional
    public ResponseData<BaseCommDict> save(BaseCommDict entity, String op) {

        List<BaseCommDict> data = baseCommDictRepository.findBaseCommDictByDictNameAndTypeIdAndActive(entity.getDictName(), entity.getTypeId(), 1);

        //add查重dictName
        if (data != null && data.iterator().hasNext() && "add".equals(op)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
        }
        //edit查重dictName
        else if (data != null && data.iterator().hasNext() && "edit".equals(op)) {
            boolean status = false;
            Iterator iter = data.iterator();
            while (iter.hasNext()) {
                BaseCommDict system = (BaseCommDict) iter.next();
                if (!entity.getId().equals(system.getId())) {
                    status = true;
                    break;
                }
            }
            if (status) {
                return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
            }
        }

        //op 不为 edit 或 add
        if (!("edit".equals(op) || "add".equals(op))) {
            return ResponseDataUtil.buildError();
        }

        //记录日志
        log.info("[字典管理][{}]数据:{}",op,JSON.toJSONString(entity));
        baseCommDictRepository.saveAndFlush(entity);
        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 根据id 删除
     *
     * @param id
     * @return
     */
    @Transactional
    public ResponseData<BaseCommDict> deleteById(String id) {

        BaseCommDict data = baseCommDictRepository.findById(id).get();

        //记录日志
        log.info("[字典管理][delete]数据:{}",JSON.toJSONString(data));
        baseCommDictRepository.deleteById(id);
        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 根据id判断对象是否存在
     *
     * @param id
     * @return
     */
    public boolean exists(String id) {
        return baseCommDictRepository.existsById(id);
    }
}
