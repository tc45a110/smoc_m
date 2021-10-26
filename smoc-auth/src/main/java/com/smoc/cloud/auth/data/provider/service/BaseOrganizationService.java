package com.smoc.cloud.auth.data.provider.service;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.auth.data.provider.entity.BaseOrganization;
import com.smoc.cloud.auth.data.provider.repository.BaseOrganizationRepository;
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

@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BaseOrganizationService {

    @Resource
    private BaseOrganizationRepository baseOrganizationRepository;


    /**
     * 根据父ID 查询组织
     *
     * @param parentId
     * @return
     */
    public ResponseData<List<Nodes>> getOrgByParentId(String parentId) {

        List<Nodes> data = baseOrganizationRepository.getOrgByParentId(parentId);
        return ResponseDataUtil.buildSuccess(data);

    }

    /**
     * 根据父ID、组织类型查询
     *
     * @param parentId
     * @param orgType  组织类型 0标示组织  1标示部门
     * @return
     */
    public ResponseData<List<BaseOrganization>> findByParentIdAndOrgType(String parentId, Integer orgType) {
        List<BaseOrganization> data = baseOrganizationRepository.findBaseOrganizationByActiveAndOrgTypeAndParentIdOrderBySortAsc(1, orgType, parentId);
        return ResponseDataUtil.buildSuccess(data);
    }


    /**
     * 根据id 查询
     *
     * @param id
     * @return
     */
    public ResponseData<BaseOrganization> findById(String id) {

        Optional<BaseOrganization> data = baseOrganizationRepository.findById(id);

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
    public ResponseData<BaseOrganization> save(BaseOrganization entity, String op) {

        List<BaseOrganization> data = baseOrganizationRepository.findBaseOrganizationByOrgNameAndActive(entity.getOrgName(), 1);

        //add查重orgName
        if (data != null && data.iterator().hasNext() && "add".equals(op)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
        }
        //edit查重orgName
        else if (data != null && data.iterator().hasNext() && "edit".equals(op)) {
            boolean status = false;
            Iterator iter = data.iterator();
            while (iter.hasNext()) {
                BaseOrganization organization = (BaseOrganization) iter.next();
                if (!entity.getId().equals(organization.getId())) {
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
        log.info("[组织管理][{}]数据:{}",op,JSON.toJSONString(entity));
        //修改父节点为非叶子节点
        baseOrganizationRepository.updateLeafById(0, entity.getParentId());

        baseOrganizationRepository.saveAndFlush(entity);
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

        BaseOrganization data = baseOrganizationRepository.findById(id).get();
        //记录日志
        log.info("[组织管理][delete]数据:{}",JSON.toJSONString(data));
        baseOrganizationRepository.deleteById(id);

        //判断删除后该节点父ID是否还有子节点
        List<Nodes> list = baseOrganizationRepository.getOrgByParentId(data.getParentId());
        if (null == list || list.size() == 0) {
            //修改父节点为叶子节点
            baseOrganizationRepository.updateLeafById(1, data.getParentId());
        }

        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 根据id判断对象是否存在
     *
     * @param id
     * @return
     */
    public boolean exists(String id) {
        return baseOrganizationRepository.existsById(id);
    }
}
