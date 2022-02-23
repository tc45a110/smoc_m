package com.smoc.cloud.filter.keywords.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.filter.FilterKeyWordsInfoValidator;
import com.smoc.cloud.filter.keywords.remote.KeywordsFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;


/**
 * 关键词管理服务
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class KeywordsService {

    @Autowired
    private KeywordsFeignClient keywordsFeignClient;

    /**
     * 根据群id查询
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<FilterKeyWordsInfoValidator>> page(PageParams<FilterKeyWordsInfoValidator> pageParams) {
        try {
            PageList<FilterKeyWordsInfoValidator> pageList = this.keywordsFeignClient.page(pageParams);
            return ResponseDataUtil.buildSuccess(pageList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据ID 查询
     *
     * @param id
     * @return
     */
    public ResponseData<FilterKeyWordsInfoValidator> findById(String id) {
        try {
            ResponseData data = this.keywordsFeignClient.findById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 保存、修改数据
     * op 是类型 表示了保存或修改
     */
    public ResponseData save(FilterKeyWordsInfoValidator filterKeyWordsInfoValidator, String op) {
        try {
            ResponseData data = this.keywordsFeignClient.save(filterKeyWordsInfoValidator, op);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据id删除菜单数据
     */
    public ResponseData deleteById(String id) {
        try {
            ResponseData data = this.keywordsFeignClient.deleteById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 批量保存
     * @param filterKeyWordsInfoValidator
     * @param op
     * @return
     */
    public ResponseData batchSave(FilterKeyWordsInfoValidator filterKeyWordsInfoValidator, String op) {
        try {
            this.keywordsFeignClient.bathSave(filterKeyWordsInfoValidator,op);
            return ResponseDataUtil.buildSuccess();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 关键字导入
     * @param validator
     * @return
     */
    public ResponseData expBatchSave(FilterKeyWordsInfoValidator validator) {
        try {
            this.keywordsFeignClient.expBatchSave(validator);
            return ResponseDataUtil.buildSuccess();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
