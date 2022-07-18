package com.smoc.cloud.iot.product.service;


import com.smoc.cloud.common.iot.validator.IotProductCardValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.iot.product.remote.IotProductCardFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class IotProductCardService {

    @Resource
    private IotProductCardFeignClient iotProductCardFeignClient;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<IotProductCardValidator>> page(PageParams<IotProductCardValidator> pageParams) {
        try {
            ResponseData<PageList<IotProductCardValidator>> data = this.iotProductCardFeignClient.page(pageParams);
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
    ResponseData<IotProductCardValidator> findById(String id) {
        try {
            ResponseData<IotProductCardValidator> data = this.iotProductCardFeignClient.findById(id);
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
    ResponseData save(List<IotProductCardValidator> cards) {
        try {
            ResponseData data = this.iotProductCardFeignClient.save(cards);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

}
