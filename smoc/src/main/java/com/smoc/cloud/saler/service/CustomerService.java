package com.smoc.cloud.saler.service;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.saler.qo.CustomerAccountInfoQo;
import com.smoc.cloud.saler.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 客户管理
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CustomerService {

    @Resource
    private CustomerRepository customerRepository;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<CustomerAccountInfoQo>> page(PageParams<CustomerAccountInfoQo> pageParams) {

        PageList<CustomerAccountInfoQo> list = customerRepository.page(pageParams);
        return ResponseDataUtil.buildSuccess(list);
    }

}
