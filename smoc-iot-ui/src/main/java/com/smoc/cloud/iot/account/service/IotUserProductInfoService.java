package com.smoc.cloud.iot.account.service;

import com.smoc.cloud.common.iot.validator.IotUserProductInfoValidator;
import com.smoc.cloud.common.iot.validator.IotUserProductItemsValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.iot.account.remote.IotUserProductInfoFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


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
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    public ResponseData save(Map<String, Object> map, List<IotUserProductItemsValidator> cards, String op) {
        try {
            ResponseData data = this.iotUserProductInfoFeignClient.save(map, cards, op);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

}
