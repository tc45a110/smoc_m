package com.smoc.cloud.filter.service;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.auth.qo.Nodes;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.filter.FilterGroupListValidator;
import com.smoc.cloud.filter.entity.FilterGroupList;
import com.smoc.cloud.filter.repository.BlackRepository;
import com.smoc.cloud.filter.repository.GroupRepository;
import com.smoc.cloud.filter.repository.WhiteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
public class GroupService {

    @Resource
    private GroupRepository groupRepository;

    @Resource
    private BlackRepository blackRepository;

    @Resource
    private WhiteRepository whiteRepository;

    /**
     * 根据id 查询
     *
     * @param id
     * @return
     */
    public ResponseData<FilterGroupList> findById(String id) {

        Optional<FilterGroupList> data = groupRepository.findById(id);

        //使用了 Optional 的判断方式，判断优雅
        return ResponseDataUtil.buildSuccess(data.orElse(null));

    }

    /**
     * 保存或修改
     *
     * @param groupValidator
     * @param op     操作类型 为add、edit
     * @return
     */
    @Transactional
    public ResponseData save(FilterGroupListValidator groupValidator, String op) {
        //转BaseUser存放对象
        FilterGroupList entity = new FilterGroupList();
        BeanUtils.copyProperties(groupValidator, entity);

        List<FilterGroupList> data = groupRepository.findByGroupNameAndEnterpriseIdAndStatus(entity.getGroupName(),entity.getEnterpriseId(), "1");

        //add查重orgName
        if (data != null && data.iterator().hasNext() && "add".equals(op)) {
            return ResponseDataUtil.buildError("群组名称重复，请修改");
        }
        //edit查重
        else if (data != null && data.iterator().hasNext() && "edit".equals(op)) {
            boolean status = false;
            Iterator iter = data.iterator();
            while (iter.hasNext()) {
                FilterGroupList organization = (FilterGroupList) iter.next();
                if (!entity.getId().equals(organization.getId()) ) {
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
        log.info("[群组管理][{}]数据:{}",op,JSON.toJSONString(entity));

        groupRepository.saveAndFlush(entity);
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

        FilterGroupList data = groupRepository.findById(id).get();

        //记录日志
        log.info("[群组管理][delete]数据:{}",JSON.toJSONString(data));
        groupRepository.deleteById(id);

        //查询是否有下级，没有修改isLeaf为1
        List<FilterGroupList> list = groupRepository.findByEnterpriseIdAndParentIdOrderBySortAsc(data.getEnterpriseId(),data.getParentId());


        //查询是否有联系人
        if("smoc_black".equals(data.getEnterpriseId())){
            blackRepository.deleteByGroupId(data.getId());
        }
        if("smoc_white".equals(data.getEnterpriseId())){
            whiteRepository.deleteByGroupId(data.getId());
        }


        return ResponseDataUtil.buildSuccess();
    }

    public ResponseData<List<FilterGroupList>> findByParentId(String enterprise, String parentId) {
        List<FilterGroupList> data = groupRepository.findByEnterpriseIdAndParentIdOrderByCreatedTimeDesc(enterprise,parentId);
        return ResponseDataUtil.buildSuccess(data);
    }

    public ResponseData<List<Nodes>> getGroupByParentId(String enterprise, String parentId) {
        List<Nodes> data = groupRepository.getGroupByParentId(enterprise,parentId);
        return ResponseDataUtil.buildSuccess(data);
    }

}
