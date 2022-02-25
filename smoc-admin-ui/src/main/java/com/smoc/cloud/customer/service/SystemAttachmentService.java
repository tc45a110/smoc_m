package com.smoc.cloud.customer.service;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseInvoiceInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.SystemAttachmentValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.customer.remote.SystemAttachmentFeignClient;
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
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 附件管理
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SystemAttachmentService {


    @Autowired
    private SmocProperties smocProperties;

    @Autowired
    private SystemAttachmentFeignClient systemAttachmentFeignClient;

    /**
     * 根据业务id查询附件
     * @param id
     * @return
     */
    public ResponseData<List<SystemAttachmentValidator>> findByMoudleId(String id) {
        try {
            ResponseData<List<SystemAttachmentValidator>> data = this.systemAttachmentFeignClient.findByMoudleId(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    public ResponseData<SystemAttachmentValidator> findById(String id) {
        try {
            ResponseData<SystemAttachmentValidator> data = this.systemAttachmentFeignClient.findById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据id删除系统数据
     */
    public ResponseData deleteById(String id){
        try {
            ResponseData data = this.systemAttachmentFeignClient.deleteById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     *  上传文件
     * @param files 文件
     * @param module 模块名称
     * @param businessId 业务id
     * @param pathName  创建文件夹名称
     * @param createdBy 创建用户
     */
    public List<SystemAttachmentValidator> saveFile(List<MultipartFile> files, String module, String businessId, String pathName, String createdBy) throws IOException {

        //上传文件
        List<SystemAttachmentValidator> list = new ArrayList<>();
        if (!StringUtils.isEmpty(files.get(0).getOriginalFilename())) {
            String day = DateTimeUtils.currentDateFomat(new Date());
            for (MultipartFile file : files) {
                String fileName = file.getOriginalFilename();//文件名
                String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);// 后缀名
                String url = module+"/"+pathName+"/"+day+"/"+fileName;
                String path = smocProperties.getEnterpriseFilePath()+url;
                File savefile = new File(path) ;
                if (!savefile.getParentFile().exists())
                    savefile.getParentFile().mkdirs();

                //上传到目录文件夹
                file.transferTo(savefile);

                //封装附件数据
                SystemAttachmentValidator info = new SystemAttachmentValidator();
                info.setId(UUID.uuid32());
                info.setMoudleId(businessId);
                info.setMoudleInentification(module);
                info.setAttachmentName(fileName);
                info.setAttachmentUri(url);
                info.setDocType(prefix);
                info.setDocSize(new BigDecimal(file.getSize()));
                info.setAttachmentStatus("1");
                info.setCreatedBy(createdBy);
                info.setCreatedTime(new Date());
                list.add(info);
            }
        }
        return list;
    }


}
