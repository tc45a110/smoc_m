package com.smoc.cloud.auth.data.provider.service;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.auth.data.provider.entity.BaseSystem;
import com.smoc.cloud.auth.data.provider.repository.BaseSystemRepository;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author 武基慧
 * @Description //完成资源模块、客户端模块，优化菜单显示及资源服务器鉴权：判断资源对应的系统、资源是否有效，系统登出后增加回调地址到客户端
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BaseSystemService {

    @Resource
    private BaseSystemRepository baseSystemRepository;

    @Resource
    private RedisTemplate<String, BaseSystem> redisTemplate;

    /**
     * 得到所有的系统
     *
     * @return
     */
    public ResponseData<List<BaseSystem>> list() {

        List<BaseSystem> data = baseSystemRepository.findAllByOrderBySortAsc();
        return ResponseDataUtil.buildSuccess(data);
    }

    public Map<String, BaseSystem> getSystemList() {

        Map<String, BaseSystem> resultMap = new HashMap<>();

        ResponseData<List<BaseSystem>> data = this.list();
        for(BaseSystem system:data.getData()){
            resultMap.put(system.getProjectName(),system);
        }

        return resultMap;

    }


    /**
     * 根据id 查询
     *
     * @param id
     * @return
     */
    public ResponseData<BaseSystem> findById(String id) {

        Optional<BaseSystem> data = baseSystemRepository.findById(id);

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
    public ResponseData<BaseSystem> save(BaseSystem entity, String op) {

        Iterable<BaseSystem> data = baseSystemRepository.findBaseSystemBySystemNameOrProjectName(entity.getSystemName(), entity.getProjectName());

        //add查重SystemName、ProjectName
        if (data != null && data.iterator().hasNext() && "add".equals(op)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
        }
        //edit查重SystemName、ProjectName
        else if (data != null && data.iterator().hasNext() && "edit".equals(op)) {
            boolean status = false;
            Iterator iter = data.iterator();
            while (iter.hasNext()) {
                BaseSystem system = (BaseSystem) iter.next();
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
        log.info("[系统管理][{}]数据:{}",op,JSON.toJSONString(entity));
        baseSystemRepository.saveAndFlush(entity);
        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 根据id 删除
     *
     * @param id
     * @return
     */
    @Transactional
    public ResponseData<BaseSystem> deleteById(String id) {

        BaseSystem data = baseSystemRepository.findById(id).get();
        //记录日志
        log.info("[系统管理][delete]数据:{}",JSON.toJSONString(data));
        baseSystemRepository.deleteById(id);
        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 根据id判断对象是否存在
     *
     * @param id
     * @return
     */
    public boolean exists(String id) {
        return baseSystemRepository.existsById(id);
    }
}
