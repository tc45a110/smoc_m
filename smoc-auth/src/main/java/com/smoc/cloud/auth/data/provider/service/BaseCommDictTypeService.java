package com.smoc.cloud.auth.data.provider.service;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.auth.data.provider.entity.BaseCommDictType;
import com.smoc.cloud.auth.data.provider.repository.BaseCommDictTypeRepository;
import com.smoc.cloud.common.auth.qo.Nodes;
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
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * 公用字典类别服务
 * 2019/5/21 17:14
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BaseCommDictTypeService {

    @Resource
    private BaseCommDictTypeRepository baseCommDictTypeRepository;

    /**
     * 得到所有的系统
     *
     * @return
     */
    public ResponseData<List<BaseCommDictType>> list() {
        List<BaseCommDictType> data = baseCommDictTypeRepository.findBaseCommDictTypeByActiveOrderBySortAsc(1);
        return ResponseDataUtil.buildSuccess(data);
    }

    public List<Nodes> getDictTree() {
        List<Nodes> list = baseCommDictTypeRepository.getDictTree();
        return list;
    }

    public List<Nodes> getDictTree(String projectName) {
        List<Nodes> list = baseCommDictTypeRepository.getDictTree(projectName);
        return list;
    }


    /**
     * 根据id 查询
     *
     * @param id
     * @return
     */
    public ResponseData<BaseCommDictType> findById(String id) {

        Optional<BaseCommDictType> data = baseCommDictTypeRepository.findById(id);

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
    public ResponseData save(BaseCommDictType entity, String op) {

        List<BaseCommDictType> data = baseCommDictTypeRepository.findBaseCommDictTypeByDictTypeSystemAndDictTypeCodeAndActive(entity.getDictTypeSystem(), entity.getDictTypeCode(), 1);

        //add查重system、dictType
        if (data != null && data.iterator().hasNext() && "add".equals(op)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
        }
        //edit查重system、dictType
        else if (data != null && data.iterator().hasNext() && "edit".equals(op)) {
            boolean status = false;
            Iterator iter = data.iterator();
            while (iter.hasNext()) {
                BaseCommDictType baseCommDictType = (BaseCommDictType) iter.next();
                if (!entity.getId().equals(baseCommDictType.getId())) {
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
        log.info("[字典类型管理][{}]数据:{}",op,JSON.toJSONString(entity));
        baseCommDictTypeRepository.saveAndFlush(entity);
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

        BaseCommDictType data = baseCommDictTypeRepository.findById(id).get();
        //记录日志
        log.info("delete-数据:{}", JSON.toJSONString(data));
        log.info("[字典类型管理][delete]数据:{}",JSON.toJSONString(data));
        baseCommDictTypeRepository.deleteById(id);
        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 根据id判断对象是否存在
     *
     * @param id
     * @return
     */
    public boolean exists(String id) {
        return baseCommDictTypeRepository.existsById(id);
    }
}
