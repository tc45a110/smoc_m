package com.smoc.cloud.tablestore.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.message.TableStoreMessageDetailInfoValidator;
import com.smoc.cloud.parameter.errorcode.service.SystemErrorCodeService;
import com.smoc.cloud.tablestore.repository.TableStoreMessageDetailInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 短信明细
 */
@Slf4j
@Service
public class TableStoreMessageDetailInfoService {

    @Resource
    private TableStoreMessageDetailInfoRepository tableStoreMessageDetailInfoRepository;

    @Resource
    private SystemErrorCodeService systemErrorCodeService;

    public ResponseData<PageList<TableStoreMessageDetailInfoValidator>> tableStorePage(PageParams<TableStoreMessageDetailInfoValidator> pageParams) {
        PageList<TableStoreMessageDetailInfoValidator> page = tableStoreMessageDetailInfoRepository.tableStorePage(pageParams);

        //匹配错误码描述
        List<TableStoreMessageDetailInfoValidator> list = page.getList();
        if(!StringUtils.isEmpty(list) && list.size()>0){
            for(TableStoreMessageDetailInfoValidator info :list){
                if(!StringUtils.isEmpty(info.getStatusCode())){
                    String remark = systemErrorCodeService.findErrorRemark(info.getStatusCode(),info.getBusinessCarrier());
                    if(!StringUtils.isEmpty(remark)){
                        info.setStatusCode(info.getStatusCode()+"("+remark+")");
                    }
                }
            }
        }

        return ResponseDataUtil.buildSuccess(page);
    }
}
