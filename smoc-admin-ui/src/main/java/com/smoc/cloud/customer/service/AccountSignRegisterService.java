package com.smoc.cloud.customer.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.validator.AccountSignRegisterValidator;
import com.smoc.cloud.customer.remote.AccountSignRegisterFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AccountSignRegisterService {

    @Autowired
    private AccountSignRegisterFeignClient accountSignRegisterFeignClient;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<AccountSignRegisterValidator>> page(PageParams<AccountSignRegisterValidator> pageParams) {
        try {
            ResponseData<PageList<AccountSignRegisterValidator>> responseData = this.accountSignRegisterFeignClient.page(pageParams);
            return responseData;
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
    public ResponseData<AccountSignRegisterValidator> findById(String id) {
        try {
            ResponseData<AccountSignRegisterValidator> data = this.accountSignRegisterFeignClient.findById(id);
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
    public ResponseData save(AccountSignRegisterValidator accountSignRegisterValidator, String op) {

        try {
            ResponseData data = this.accountSignRegisterFeignClient.save(accountSignRegisterValidator, op);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 注销
     *
     * @param id
     * @return
     */
    public ResponseData deleteById(String id) {
        try {
            ResponseData data = this.accountSignRegisterFeignClient.deleteById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据业务账号，查询已占用的签名自定义扩展号
     * @param account
     * @param id 当id 不为空时候，不查询本id的签名自定义扩展号
     * @return
     */
    public ResponseData<List<String>> findExtendDataByAccount(String account, String id) {
        try {
            ResponseData<List<String>> data = this.accountSignRegisterFeignClient.findExtendDataByAccount(account,id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
