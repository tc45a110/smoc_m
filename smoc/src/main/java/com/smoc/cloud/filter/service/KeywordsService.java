package com.smoc.cloud.filter.service;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.filter.FilterKeyWordsInfoValidator;
import com.smoc.cloud.filter.entity.FilterKeyWordsInfo;
import com.smoc.cloud.filter.repository.KeywordsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * 关键词管理
 */
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class KeywordsService {

    @Resource
    private KeywordsRepository keywordsRepository;

    /**
     * 根据群组id查询通讯录
     * @param pageParams
     * @return
     */
    public PageList<FilterKeyWordsInfoValidator> page(PageParams<FilterKeyWordsInfoValidator> pageParams) {
        return keywordsRepository.page(pageParams);
    }

    /**
     * 根据id 查询
     * @param id
     * @return
     */
    public ResponseData findById(String id) {

        Optional<FilterKeyWordsInfo> data = keywordsRepository.findById(id);

        //使用了 Optional 的判断方式，判断优雅
        return ResponseDataUtil.buildSuccess(data.orElse(null));
    }

    /**
     * 保存或修改
     *
     * @param filterKeyWordsInfoValidator
     * @param op     操作类型 为add、edit
     * @return
     */
    @Transactional
    public ResponseData save(FilterKeyWordsInfoValidator filterKeyWordsInfoValidator, String op) {

        //转BaseUser存放对象
        FilterKeyWordsInfo entity = new FilterKeyWordsInfo();
        BeanUtils.copyProperties(filterKeyWordsInfoValidator, entity);

        List<FilterKeyWordsInfo> data = keywordsRepository.findByKeyWordsBusinessTypeAndBusinessIdAndKeyWordsTypeAndKeyWordsAndWaskKeyWords(entity.getKeyWordsBusinessType(),entity.getBusinessId(),entity.getKeyWordsType(),entity.getKeyWords(),entity.getWaskKeyWords());

        //add查重
        if (data != null && data.iterator().hasNext() && "add".equals(op)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
        }
        //edit查重orgName
        else if (data != null && data.iterator().hasNext() && "edit".equals(op)) {
            boolean status = false;
            Iterator iter = data.iterator();
            while (iter.hasNext()) {
                FilterKeyWordsInfo organization = (FilterKeyWordsInfo) iter.next();
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
        log.info("[关键词管理][{}]数据:{}",op,JSON.toJSONString(entity));

        keywordsRepository.saveAndFlush(entity);
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

        FilterKeyWordsInfo data = keywordsRepository.findById(id).get();

        //记录日志
        log.info("[关键词管理][delete]数据:{}",JSON.toJSONString(data));
        keywordsRepository.deleteById(id);

        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 批量保存
     * @param filterKeyWordsInfoValidator
     * @return
     */
    @Async
    public void bathSave(FilterKeyWordsInfoValidator filterKeyWordsInfoValidator) {
        keywordsRepository.bathSave(filterKeyWordsInfoValidator);
    }

    /**
     * 关键字导入
     * @param filterKeyWordsInfoValidator
     */
    public void expBatchSave(FilterKeyWordsInfoValidator filterKeyWordsInfoValidator) {
        keywordsRepository.expBatchSave(filterKeyWordsInfoValidator);
    }
}
