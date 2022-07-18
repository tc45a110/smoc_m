package com.smoc.cloud.iot.carrier.service;

import com.smoc.cloud.common.iot.validator.IotCarrierInfoValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.iot.carrier.remote.IotCarrierInfoFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;

/**
 * 运营商接入
 */
@Slf4j
@Service
public class IotCarrierInfoService {

    @Resource
    private IotCarrierInfoFeignClient iotCarrierInfoFeignClient;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<IotCarrierInfoValidator>> page(PageParams<IotCarrierInfoValidator> pageParams) {
        try {
            ResponseData<PageList<IotCarrierInfoValidator>> data = this.iotCarrierInfoFeignClient.page(pageParams);
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
    public ResponseData<IotCarrierInfoValidator> findById(String id) {
        try {
            ResponseData<IotCarrierInfoValidator> data = this.iotCarrierInfoFeignClient.findById(id);
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
    public ResponseData save(IotCarrierInfoValidator iotCarrierInfoValidator, String op) {
        try {
            ResponseData data = this.iotCarrierInfoFeignClient.save(iotCarrierInfoValidator, op);
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
    public ResponseData forbidden(@PathVariable String id) {
        try {
            ResponseData data = this.iotCarrierInfoFeignClient.forbidden(id);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
