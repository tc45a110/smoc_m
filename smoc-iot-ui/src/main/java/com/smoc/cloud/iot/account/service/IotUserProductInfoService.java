package com.smoc.cloud.iot.account.service;

import com.smoc.cloud.common.iot.validator.AccountProduct;
import com.smoc.cloud.common.iot.validator.IotProductInfoValidator;
import com.smoc.cloud.common.iot.validator.IotUserProductInfoValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.iot.account.remote.IotUserProductInfoFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Slf4j
@Service
public class IotUserProductInfoService {

    @Resource
    private IotUserProductInfoFeignClient iotUserProductInfoFeignClient;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<IotUserProductInfoValidator>> page(PageParams<IotUserProductInfoValidator> pageParams) {
        try {
            ResponseData<PageList<IotUserProductInfoValidator>> data = this.iotUserProductInfoFeignClient.page(pageParams);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 查询列表
     *
     * @param account
     * @return
     */
    public ResponseData<List<IotProductInfoValidator>> list(String account) {
        try {
            ResponseData<List<IotProductInfoValidator>> data = this.iotUserProductInfoFeignClient.list(account);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    public ResponseData<IotUserProductInfoValidator> findById(String id) {
        try {
            ResponseData<IotUserProductInfoValidator> data = this.iotUserProductInfoFeignClient.findById(id);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 添加、修改
     *
     * @return
     */
    public ResponseData save(AccountProduct accountProduct) {
        try {
            ResponseData data = this.iotUserProductInfoFeignClient.save(accountProduct);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

}
