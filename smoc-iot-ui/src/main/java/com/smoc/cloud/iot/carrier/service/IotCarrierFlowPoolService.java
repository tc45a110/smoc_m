package com.smoc.cloud.iot.carrier.service;


import com.smoc.cloud.common.iot.validator.IotCarrierFlowPoolValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.iot.carrier.remote.IotCarrierFlowPoolFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;

/**
 * 运营商流量池
 */
@Slf4j
@Service
public class IotCarrierFlowPoolService {

    @Resource
    private IotCarrierFlowPoolFeignClient iotCarrierFlowPoolFeignClient;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<IotCarrierFlowPoolValidator>> page(PageParams<IotCarrierFlowPoolValidator> pageParams) {
        try {
            ResponseData<PageList<IotCarrierFlowPoolValidator>> data = this.iotCarrierFlowPoolFeignClient.page(pageParams);
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
    public ResponseData<IotCarrierFlowPoolValidator> findById(String id) {
        try {
            ResponseData<IotCarrierFlowPoolValidator> data = this.iotCarrierFlowPoolFeignClient.findById(id);
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
    public ResponseData save(IotCarrierFlowPoolValidator iotCarrierFlowPoolValidator, String op) {
        try {
            ResponseData data = this.iotCarrierFlowPoolFeignClient.save(iotCarrierFlowPoolValidator, op);
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
            ResponseData data = this.iotCarrierFlowPoolFeignClient.forbidden(id);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
