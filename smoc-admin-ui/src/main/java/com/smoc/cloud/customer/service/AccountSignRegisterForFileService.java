package com.smoc.cloud.customer.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.qo.CarrierCount;
import com.smoc.cloud.common.smoc.customer.qo.ExportModel;
import com.smoc.cloud.common.smoc.customer.qo.ExportRegisterModel;
import com.smoc.cloud.common.smoc.customer.validator.AccountSignRegisterForFileValidator;
import com.smoc.cloud.customer.remote.AccountSignRegisterForFileFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AccountSignRegisterForFileService {

    @Autowired
    private AccountSignRegisterForFileFeignClient accountSignRegisterForFileFeignClient;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<AccountSignRegisterForFileValidator>> page(PageParams<AccountSignRegisterForFileValidator> pageParams) {
        try {
            ResponseData<PageList<AccountSignRegisterForFileValidator>> responseData = this.accountSignRegisterForFileFeignClient.page(pageParams);
            return responseData;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据运营商，统计未报备得条数
     *
     * @return
     */
    public ResponseData<List<CarrierCount>> countByCarrier() {
        try {
            ResponseData<List<CarrierCount>> responseData = this.accountSignRegisterForFileFeignClient.countByCarrier();
            return responseData;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 查询导出数据
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<ExportModel>> export(PageParams<ExportModel> pageParams) {
        try {
            ResponseData<PageList<ExportModel>> responseData = this.accountSignRegisterForFileFeignClient.export(pageParams);
            return responseData;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 为导出数据生成报备订单号，并改变报备数据状态
     *
     * @param exportRegisterModel
     */
    @Async
    public ResponseData register(ExportRegisterModel exportRegisterModel) {
        try {
            ResponseData responseData = this.accountSignRegisterForFileFeignClient.register(exportRegisterModel);
            return responseData;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据报备订单号完成报备状态
     *
     * @param registerOrderNo
     * @param status
     * @return
     */
    @Async
    public ResponseData updateRegisterStatusByOrderNo(String registerOrderNo,String status) {
        try {
            ResponseData responseData = this.accountSignRegisterForFileFeignClient.updateRegisterStatusByOrderNo(registerOrderNo,status);
            return responseData;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     *  根据报备订单号查询导出数据
     *
     * @param registerOrderNo
     * @return
     */
    public ResponseData<PageList<ExportModel>> query(PageParams pageParams,String registerOrderNo) {
        try {
            ResponseData<PageList<ExportModel>> responseData = this.accountSignRegisterForFileFeignClient.query(pageParams,registerOrderNo);
            return responseData;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
