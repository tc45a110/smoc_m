package com.smoc.cloud.book.service;

import com.smoc.cloud.book.remote.BookFeignClient;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBookInfoValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;


/**
 * 通讯录管理服务
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BookService {

    @Autowired
    private BookFeignClient bookFeignClient;

    /**
     * 根据群id查询
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<EnterpriseBookInfoValidator>> page(PageParams<EnterpriseBookInfoValidator> pageParams) {
        try {
            PageList<EnterpriseBookInfoValidator> pageList = this.bookFeignClient.page(pageParams);
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
    public ResponseData<EnterpriseBookInfoValidator> findById(String id) {
        try {
            ResponseData data = this.bookFeignClient.findById(id);
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
    public ResponseData save(EnterpriseBookInfoValidator enterpriseBookInfoValidator, String op) {
        try {
            ResponseData data = this.bookFeignClient.save(enterpriseBookInfoValidator, op);
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
            ResponseData data = this.bookFeignClient.deleteById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 批量保存
     * @param enterpriseBookInfoValidator
     * @return
     */
    public ResponseData batchSave(EnterpriseBookInfoValidator enterpriseBookInfoValidator) {
        try {
            this.bookFeignClient.bathSave(enterpriseBookInfoValidator);
            return ResponseDataUtil.buildSuccess();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

}
