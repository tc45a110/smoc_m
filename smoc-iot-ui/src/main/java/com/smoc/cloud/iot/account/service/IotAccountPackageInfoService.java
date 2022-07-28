package com.smoc.cloud.iot.account.service;

import com.smoc.cloud.common.iot.validator.AccountPackage;
import com.smoc.cloud.common.iot.validator.IotAccountPackageItemsValidator;
import com.smoc.cloud.common.iot.validator.IotFlowCardsPrimaryInfoValidator;
import com.smoc.cloud.common.iot.validator.IotPackageInfoValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.iot.account.remote.IotAccountPackageInfoFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Slf4j
@Service
public class IotAccountPackageInfoService {

    @Resource
    private IotAccountPackageInfoFeignClient iotAccountPackageInfoFeignClient;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<IotAccountPackageItemsValidator>> page(PageParams<IotAccountPackageItemsValidator> pageParams) {
        try {
            ResponseData<PageList<IotAccountPackageItemsValidator>> data = this.iotAccountPackageInfoFeignClient.page(pageParams);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }


    /**
     * 查询账号配置得套餐
     *
     * @param account
     * @return
     */
    public ResponseData<List<IotPackageInfoValidator>> listAccountPackages(String account) {
        try {
            ResponseData<List<IotPackageInfoValidator>> data = this.iotAccountPackageInfoFeignClient.listAccountPackages(account);
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
    public ResponseData<List<IotPackageInfoValidator>> list(String account) {
        try {
            ResponseData<List<IotPackageInfoValidator>> data = this.iotAccountPackageInfoFeignClient.list(account);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据套餐id，查询套餐绑定的物联网卡
     *
     * @param packageId
     * @return
     */
    public ResponseData<List<IotFlowCardsPrimaryInfoValidator>> listCardsByPackageId(String account,String packageId) {
        try {
            ResponseData<List<IotFlowCardsPrimaryInfoValidator>> data = this.iotAccountPackageInfoFeignClient.listCardsByPackageId(account,packageId);
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
    public ResponseData<IotAccountPackageItemsValidator> findById(String id) {
        try {
            ResponseData<IotAccountPackageItemsValidator> data = this.iotAccountPackageInfoFeignClient.findById(id);
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
    public ResponseData save(AccountPackage accountPackage) {
        try {
            ResponseData data = this.iotAccountPackageInfoFeignClient.save(accountPackage);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

}
