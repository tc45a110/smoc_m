package com.smoc.cloud.customer.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.qo.CarrierCount;
import com.smoc.cloud.common.smoc.customer.qo.ExportModel;
import com.smoc.cloud.common.smoc.customer.validator.AccountSignRegisterForFileValidator;
import com.smoc.cloud.customer.repository.AccountSignRegisterForFileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class AccountSignRegisterForFileService {

    @Resource
    private AccountSignRegisterForFileRepository accountSignRegisterForFileRepository;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<AccountSignRegisterForFileValidator>> page(PageParams<AccountSignRegisterForFileValidator> pageParams) {

        PageList<AccountSignRegisterForFileValidator> pageList = this.accountSignRegisterForFileRepository.page(pageParams);
        return ResponseDataUtil.buildSuccess(pageList);
    }

    /**
     * 根据运营商，统计未报备得条数
     *
     * @return
     */
    public ResponseData<List<CarrierCount>> countByCarrier() {
        List<CarrierCount> carrierCounts = this.accountSignRegisterForFileRepository.countByCarrier();
        return ResponseDataUtil.buildSuccess(carrierCounts);
    }

    /**
     * 查询导出数据
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<ExportModel>> export(PageParams<ExportModel> pageParams){
        PageList<ExportModel> pageList = this.accountSignRegisterForFileRepository.export(pageParams);
        return ResponseDataUtil.buildSuccess(pageList);
    }
}
