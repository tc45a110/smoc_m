package com.smoc.cloud.iot.product.service;

import com.smoc.cloud.common.iot.validator.IotProductInfoValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.iot.product.remote.IotProductInfoFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class IotProductInfoService {

    @Resource
    private IotProductInfoFeignClient iotProductInfoFeignClient;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<IotProductInfoValidator>> page(PageParams<IotProductInfoValidator> pageParams) {
        try {
            ResponseData<PageList<IotProductInfoValidator>> data = this.iotProductInfoFeignClient.page(pageParams);
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
    ResponseData<IotProductInfoValidator> findById(String id) {
        try {
            ResponseData<IotProductInfoValidator> data = this.iotProductInfoFeignClient.findById(id);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 添加、修改
     *
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    ResponseData save(IotProductInfoValidator iotProductInfoValidator, String op) {
        try {
            ResponseData data = this.iotProductInfoFeignClient.save(iotProductInfoValidator, op);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 注销
     *
     * @param id
     * @return
     */
    public ResponseData forbidden(String id) {
        try {
            ResponseData data = this.iotProductInfoFeignClient.forbidden(id);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
