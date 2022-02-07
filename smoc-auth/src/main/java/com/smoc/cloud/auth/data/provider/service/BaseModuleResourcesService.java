package com.smoc.cloud.auth.data.provider.service;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.auth.data.provider.entity.BaseModuleResources;
import com.smoc.cloud.auth.data.provider.repository.BaseModuleResourcesRepository;
import com.smoc.cloud.auth.security.service.MpmTokenService;
import com.smoc.cloud.common.auth.qo.Nodes;
import com.smoc.cloud.common.constant.RedisConstant;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 完成系统模块，资源模块
 * 2019/3/29 14:29
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BaseModuleResourcesService {

    @Autowired
    private MpmTokenService mpmTokenService;

    @Resource
    private BaseModuleResourcesRepository baseModuleResourcesRepository;

    @Resource
    private RedisTemplate<String, Map<String, Object>> redisTemplate;

    @Resource
    private RedisTemplate<String, Nodes> nodesRedisTemplate;

    /**
     * 根据用户查询菜单
     *
     * @param userId
     * @return
     */
    public ResponseData<List<Map<String, Object>>> getMenusBsssyUserIdAndSystemId(String userId, String systemId) {

        List<Map<String, Object>> menus = null;
        menus = redisTemplate.opsForList().range(RedisConstant.USER_MENUS + ":" + userId, 0, -1);
        if (null == menus || menus.size() <= 0) {
            log.info("[菜单管理][查询]数据:数据库查询");
            menus = baseModuleResourcesRepository.getMenusByUserIdAndSystemId(userId, systemId);

            cacheMens(menus,userId);

        } else {
            //log.info("redis内查询");
        }
        return ResponseDataUtil.buildSuccess(menus);
    }

    /**
     * 根据角色查询菜单
     *
     * @param roleId
     * @return
     */
    public ResponseData<List<BaseModuleResources>> getMenussssByRoleId(String roleId) {
        List<BaseModuleResources> data = baseModuleResourcesRepository.findBaseModuleResourcesByRoleId(roleId);
        return ResponseDataUtil.buildSuccess(data);
    }

    /**
     * 根据项目标示、token查询用户左侧的菜单（三级）
     *
     * @param projectName 项目标示
     * @param userId
     * @return
     */
    public ResponseData<List<Nodes>> getUserMenus(String projectName, String userId) {

        //根据项目标示、用户id查询 项目左侧的菜单
        List<Nodes> data = null;
        //判断是否存在key
        boolean hasKey = redisTemplate.hasKey(RedisConstant.AUTH_USER_MENUS + ":" + projectName + ":" + userId);
        if (hasKey) {

            //redis查询
            data = nodesRedisTemplate.opsForList().range(RedisConstant.AUTH_USER_MENUS + ":" + projectName + ":" + userId, 0, -1);

        } else {
            //数据库查询
            data = baseModuleResourcesRepository.getUserMenus(userId, projectName);
            cacheLeftMens(data,userId,projectName);

        }
        return ResponseDataUtil.buildSuccess(data);
    }

    /**
     * 根据父ID 查询下面菜单
     *
     * @param parentId
     * @return
     */
    public ResponseData<List<Nodes>> getMenusByParentId(String parentId) {

        List<Nodes> nodes = baseModuleResourcesRepository.getMenusByParentId(parentId);
        return ResponseDataUtil.buildSuccess(nodes);
    }

    /**
     * 菜单选择 查询根目录
     *
     * @param projectName 项目标识
     * @return
     */
    public ResponseData<List<Nodes>> getRootByProjectName(String projectName) {

        List<Nodes> nodes = baseModuleResourcesRepository.getRootByProjectName(projectName);
        return ResponseDataUtil.buildSuccess(nodes);
    }

    /**
     * 根据父id查询所有 子菜单
     *
     * @param parentId
     * @return
     */
    public ResponseData<List<Nodes>> getAllSubMenusByParentId(String parentId) {
        List<Nodes> nodes = baseModuleResourcesRepository.getAllSubMenusByParentId(parentId);
        return ResponseDataUtil.buildSuccess(nodes);
    }

    /**
     * 根据 用户ID 父id 关联角色信息，查询 一级菜单
     * @param userId
     * @param parentId
     * @return
     */
    public List<Nodes> getSubNodes(String userId, String parentId){
        List<Nodes> nodes = baseModuleResourcesRepository.getSubNodes(userId,parentId);
        return nodes;
    }


    /**
     * 根据父id查询菜单列表
     *
     * @param parentId
     * @return
     */
    public ResponseData<List<BaseModuleResources>> findBaseModuleResourcesByParentId(String parentId) {
        List<BaseModuleResources> list = baseModuleResourcesRepository.findBaseModuleResourcesByParentIdOrderBySortAsc(parentId);
        return ResponseDataUtil.buildSuccess(list);
    }

    /**
     * 根据id查询菜单信息
     *
     * @param id
     * @return
     */
    public ResponseData<BaseModuleResources> findById(String id) {
        Optional<BaseModuleResources> data = baseModuleResourcesRepository.findById(id);

        //使用了 Optional 的判断方式，判断优雅
        return ResponseDataUtil.buildSuccess(data.orElse(null));
    }

    /**
     * 添加、修改菜单
     *
     * @param entity
     * @param op     操作方式  值为edit或add
     * @return
     */
    @Transactional
    public ResponseData<BaseModuleResources> save(BaseModuleResources entity, String op) {

        //记录日志
        log.info("[菜单管理][{}]数据:{}",op,JSON.toJSONString(entity));
        baseModuleResourcesRepository.saveAndFlush(entity);
        return ResponseDataUtil.buildSuccess();
    }


    /**
     * 根据id删除菜单
     *
     * @param id
     * @return
     */
    @Transactional
    public ResponseData<BaseModuleResources> deleteById(String id) {

        BaseModuleResources data = baseModuleResourcesRepository.findById(id).get();
        //记录日志
        log.info("[菜单管理][delete]数据:{}",JSON.toJSONString(data));
        baseModuleResourcesRepository.deleteById(id);
        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 根据id 判断数据是否存在
     *
     * @param id
     * @return
     */
    public boolean exists(String id) {
        return baseModuleResourcesRepository.existsById(id);
    }

    public void cacheMens(List<Map<String, Object>> menus,String userId){

        redisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public <K, V> Object execute(RedisOperations<K, V> redisOperations) throws DataAccessException {
                for (Map<String, Object> entity : menus) {
                    redisTemplate.opsForList().rightPush(RedisConstant.USER_MENUS + ":" + userId, entity);
                }
                return null;
            }
        });

    }

    public void cacheLeftMens(List<Nodes> data,String userId,String projectName){

        redisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public <K, V> Object execute(RedisOperations<K, V> redisOperations) throws DataAccessException {
                //数据存储到redis
                if (null != data && data.size() > 0) {
                    for (Nodes node : data) {
                        nodesRedisTemplate.opsForList().rightPush(RedisConstant.AUTH_USER_MENUS + ":" + projectName + ":" + userId, node);
                    }
                }
                return null;
            }
        });

    }
}
