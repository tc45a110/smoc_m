package com.smoc.cloud.filter.service;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.filter.FilterBlackListValidator;
import com.smoc.cloud.filter.entity.FilterBlackList;
import com.smoc.cloud.filter.repository.BlackRepository;
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
public class BlackService {

    @Resource
    private BlackRepository blackRepository;

    /**
     * 根据群组id查询通讯录
     * @param pageParams
     * @return
     */
    public PageList<FilterBlackListValidator> page(PageParams<FilterBlackListValidator> pageParams) {
        return blackRepository.page(pageParams);
    }

    /**
     * 根据id 查询
     * @param id
     * @return
     */
    public ResponseData findById(String id) {

        Optional<FilterBlackList> data = blackRepository.findById(id);

        //使用了 Optional 的判断方式，判断优雅
        return ResponseDataUtil.buildSuccess(data.orElse(null));
    }

    /**
     * 保存或修改
     *
     * @param filterBlackListValidator
     * @param op     操作类型 为add、edit
     * @return
     */
    @Transactional
    public ResponseData save(FilterBlackListValidator filterBlackListValidator, String op) {

        //转BaseUser存放对象
        FilterBlackList entity = new FilterBlackList();
        BeanUtils.copyProperties(filterBlackListValidator, entity);

        List<FilterBlackList> data = blackRepository.findByGroupIdAndMobileAndStatus(entity.getGroupId(),entity.getMobile(), "1");

        //add查重
        if (data != null && data.iterator().hasNext() && "add".equals(op)) {
            return ResponseDataUtil.buildError("组里已经存在此手机号码，请修改");
        }
        //edit查重orgName
        else if (data != null && data.iterator().hasNext() && "edit".equals(op)) {
            boolean status = false;
            Iterator iter = data.iterator();
            while (iter.hasNext()) {
                FilterBlackList organization = (FilterBlackList) iter.next();
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
        log.info("[黑名单管理][{}]数据:{}",op,JSON.toJSONString(entity));

        blackRepository.saveAndFlush(entity);
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

        FilterBlackList data = blackRepository.findById(id).get();

        //记录日志
        log.info("[黑名单管理][delete]数据:{}",JSON.toJSONString(data));
        blackRepository.deleteById(id);

        return ResponseDataUtil.buildSuccess();
    }


}
