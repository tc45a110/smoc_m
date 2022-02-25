package com.smoc.cloud.customer.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseContractInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.SystemAttachmentValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.customer.remote.EnterpriseContractFeignClient;
import com.smoc.cloud.properties.SmocProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * EC合同管理服务
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class EnterpriseContractService {

    @Autowired
    private EnterpriseContractFeignClient enterpriseContractFeignClient;

    @Autowired
    private SystemAttachmentService systemAttachmentService;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<EnterpriseContractInfoValidator>> page(PageParams<EnterpriseContractInfoValidator> pageParams) {
        try {
            PageList<EnterpriseContractInfoValidator> pageList = this.enterpriseContractFeignClient.page(pageParams);
            return ResponseDataUtil.buildSuccess(pageList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    public ResponseData<EnterpriseContractInfoValidator> findById(String id) {
        try {
            ResponseData<EnterpriseContractInfoValidator> data = this.enterpriseContractFeignClient.findById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 保存、修改数据
     * op 是类型 表示了保存或修改
     */
    public ResponseData save(EnterpriseContractInfoValidator enterpriseContractInfoValidator, List<MultipartFile> files,String enterpriseName, String op) {

        try {

            //获取上传的文件
            List<SystemAttachmentValidator> list = systemAttachmentService.saveFile(files,"EC_CONTRACT",enterpriseContractInfoValidator.getId(),enterpriseName,enterpriseContractInfoValidator.getCreatedBy());
            enterpriseContractInfoValidator.setFilesList(list);
            ResponseData data = this.enterpriseContractFeignClient.save(enterpriseContractInfoValidator, op);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据id删除系统数据
     */
    public ResponseData deleteById(String id) {
        try {
            ResponseData data = this.enterpriseContractFeignClient.deleteById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
