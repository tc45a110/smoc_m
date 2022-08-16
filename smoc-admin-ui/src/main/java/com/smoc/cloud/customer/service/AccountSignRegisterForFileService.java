package com.smoc.cloud.customer.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.qo.CarrierCount;
import com.smoc.cloud.common.smoc.customer.qo.ExportModel;
import com.smoc.cloud.common.smoc.customer.validator.AccountSignRegisterForFileValidator;
import com.smoc.cloud.customer.remote.AccountSignRegisterForFileFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
     * @return
     */
    public ResponseData<List<CarrierCount>> countByCarrier(){
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
}
