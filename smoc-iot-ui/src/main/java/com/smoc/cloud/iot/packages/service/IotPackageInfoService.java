package com.smoc.cloud.iot.packages.service;

import com.smoc.cloud.common.iot.validator.IotFlowCardsPrimaryInfoValidator;
import com.smoc.cloud.common.iot.validator.IotPackageInfoValidator;
import com.smoc.cloud.common.iot.validator.PackageCards;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.iot.packages.remote.IotPackageInfoFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class IotPackageInfoService {

    @Resource
    private IotPackageInfoFeignClient iotPackageInfoFeignClient;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<IotPackageInfoValidator>> page(PageParams<IotPackageInfoValidator> pageParams) {
        try {
            ResponseData<PageList<IotPackageInfoValidator>> data = this.iotPackageInfoFeignClient.page(pageParams);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据产品id查询，产品配置的信息，及未配置的 物联网卡信息
     *
     * @param packageId
     * @return
     */
    public ResponseData<List<IotFlowCardsPrimaryInfoValidator>> list(String packageId) {
        try {
            ResponseData<List<IotFlowCardsPrimaryInfoValidator>> data = this.iotPackageInfoFeignClient.list(packageId);
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
    public ResponseData<IotPackageInfoValidator> findById(String id) {
        try {
            ResponseData<IotPackageInfoValidator> data = this.iotPackageInfoFeignClient.findById(id);
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
    public ResponseData save(IotPackageInfoValidator iotPackageInfoValidator, String op) {
        try {
            ResponseData data = this.iotPackageInfoFeignClient.save(iotPackageInfoValidator, op);
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
    public ResponseData saveConfig(PackageCards packageCards) {
        try {
            ResponseData data = this.iotPackageInfoFeignClient.saveConfig(packageCards);
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
            ResponseData data = this.iotPackageInfoFeignClient.forbidden(id);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
