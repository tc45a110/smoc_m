package com.smoc.cloud.template.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.template.AccountTemplateInfoValidator;
import com.smoc.cloud.template.remote.AccountTemplateInfoFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 模板管理
 */
@Slf4j
@Service
public class AccountTemplateInfoService {

    @Autowired
    private AccountTemplateInfoFeignClient accountTemplateInfoFeignClient;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<AccountTemplateInfoValidator>> page(PageParams<AccountTemplateInfoValidator> pageParams) {

        try {

            ResponseData<PageList<AccountTemplateInfoValidator>> page = accountTemplateInfoFeignClient.page(pageParams);
            return page;
        } catch (Exception e) {
            return ResponseDataUtil.buildError(e.getMessage());
        }

    }

    /**
     * 注销
     *
     * @param id
     * @return
     */
    public ResponseData cancelTemplate(String id) {
        try {
            ResponseData data = this.accountTemplateInfoFeignClient.cancelTemplate(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    public ResponseData<AccountTemplateInfoValidator> findById(String id) {
        try {
            ResponseData<AccountTemplateInfoValidator> data = this.accountTemplateInfoFeignClient.findById(id);
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
    public ResponseData save(AccountTemplateInfoValidator accountTemplateInfoValidator, String op) {

        try {
            ResponseData data = this.accountTemplateInfoFeignClient.save(accountTemplateInfoValidator, op);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
