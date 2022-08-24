package com.smoc.cloud.iot.carrier.service;

import com.smoc.cloud.common.iot.validator.IotCardImportModel;
import com.smoc.cloud.common.iot.validator.IotFlowCardsInfo;
import com.smoc.cloud.common.iot.validator.IotFlowCardsPrimaryInfoValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.iot.carrier.remote.IotFlowCardsPrimaryInfoFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class IotFlowCardsPrimaryInfoService {

    @Resource
    private IotFlowCardsPrimaryInfoFeignClient iotFlowCardsPrimaryInfoFeignClient;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<IotFlowCardsPrimaryInfoValidator>> page(PageParams<IotFlowCardsPrimaryInfoValidator> pageParams) {
        try {
            ResponseData<PageList<IotFlowCardsPrimaryInfoValidator>> data = this.iotFlowCardsPrimaryInfoFeignClient.page(pageParams);
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
    public ResponseData<IotFlowCardsInfo> findById(String id) {
        try {
            ResponseData<IotFlowCardsInfo> data = this.iotFlowCardsPrimaryInfoFeignClient.findById(id);
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
    public ResponseData save(IotFlowCardsPrimaryInfoValidator iotFlowCardsPrimaryInfoValidator, String op) {
        try {
            ResponseData data = this.iotFlowCardsPrimaryInfoFeignClient.save(iotFlowCardsPrimaryInfoValidator, op);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 批量导入物联网卡
     *
     * @return
     */
    public ResponseData saveBatch(IotCardImportModel iotCardImportModel) {
        try {
            ResponseData data = this.iotFlowCardsPrimaryInfoFeignClient.saveBatch(iotCardImportModel);
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
            ResponseData data = this.iotFlowCardsPrimaryInfoFeignClient.forbidden(id);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
