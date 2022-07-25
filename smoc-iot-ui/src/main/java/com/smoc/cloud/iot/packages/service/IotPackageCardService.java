package com.smoc.cloud.iot.packages.service;


import com.smoc.cloud.common.iot.validator.IotPackageCardValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.iot.packages.remote.IotPackageCardFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class IotPackageCardService {

    @Resource
    private IotPackageCardFeignClient iotPackageCardFeignClient;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<IotPackageCardValidator>> page(PageParams<IotPackageCardValidator> pageParams) {
        try {
            ResponseData<PageList<IotPackageCardValidator>> data = this.iotPackageCardFeignClient.page(pageParams);
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
    ResponseData<IotPackageCardValidator> findById(String id) {
        try {
            ResponseData<IotPackageCardValidator> data = this.iotPackageCardFeignClient.findById(id);
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
    ResponseData save(List<IotPackageCardValidator> cards) {
        try {
            ResponseData data = this.iotPackageCardFeignClient.save(cards);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

}
