package com.smoc.cloud.customer.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.validator.AccountSignRegisterExportRecordValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.customer.entity.AccountSignRegisterExportRecord;
import com.smoc.cloud.customer.repository.AccountSignRegisterExportRecordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class AccountSignRegisterExportRecordService {

    @Resource
    private AccountSignRegisterExportRecordRepository accountSignRegisterExportRecordRepository;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<AccountSignRegisterExportRecordValidator>> page(PageParams pageParams) {

        PageList<AccountSignRegisterExportRecordValidator> data = accountSignRegisterExportRecordRepository.page(pageParams);
        return ResponseDataUtil.buildSuccess(data);
    }

    public ResponseData<AccountSignRegisterExportRecordValidator> findByRegisterOrderNo(String registerOrderNo){
        List<AccountSignRegisterExportRecord> list = this.accountSignRegisterExportRecordRepository.findByRegisterOrderNo(registerOrderNo);
        if(null == list || list.size()<1){
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }

        AccountSignRegisterExportRecord registerExportRecord = list.get(0);
        AccountSignRegisterExportRecordValidator accountSignRegisterExportRecordValidator = new AccountSignRegisterExportRecordValidator();
        BeanUtils.copyProperties(registerExportRecord, accountSignRegisterExportRecordValidator);

        //转换日期
        accountSignRegisterExportRecordValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(registerExportRecord.getCreatedTime()));
        return ResponseDataUtil.buildSuccess(accountSignRegisterExportRecordValidator);
    }

}
