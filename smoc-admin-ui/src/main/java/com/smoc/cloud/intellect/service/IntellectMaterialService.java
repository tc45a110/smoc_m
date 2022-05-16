package com.smoc.cloud.intellect.service;

import com.google.gson.Gson;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.validator.SystemAttachmentValidator;
import com.smoc.cloud.common.smoc.intelligence.IntellectMaterialValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.customer.remote.SystemAttachmentFeignClient;
import com.smoc.cloud.intellect.remote.IntellectMaterialFeign;
import com.smoc.cloud.intellect.remote.configuration.IntelligenceProperties;
import com.smoc.cloud.intellect.remote.request.RequestTemplateResource;
import com.smoc.cloud.intellect.remote.service.RequestService;
import com.smoc.cloud.intellect.remote.utils.FileBase64Converter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
public class IntellectMaterialService {

    @Autowired
    private RequestService requestService;

    @Autowired
    private IntelligenceProperties intelligenceProperties;

    @Autowired
    private IntellectMaterialFeign intellectMaterialFeign;


    @Autowired
    private SystemAttachmentFeignClient systemAttachmentFeignClient;


    /**
     * 查询素材
     *
     * @return
     */
    public ResponseData<List<IntellectMaterialValidator>> getMaterial(String materialTypeId) {
        try {
            ResponseData<List<IntellectMaterialValidator>> data = intellectMaterialFeign.getMaterial(materialTypeId);
            return data;
        } catch (Exception e) {
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 添加、修改
     *
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    public ResponseData save(IntellectMaterialValidator intellectMaterialValidator, List<MultipartFile> files, String op) {
        try {

            RequestTemplateResource requestTemplateResource = new RequestTemplateResource();
            //获取上传的文件
            List<SystemAttachmentValidator> list = this.saveFile(files,"material",intellectMaterialValidator.getId(),intellectMaterialValidator.getParentId()+"/"+intellectMaterialValidator.getMaterialType(),intellectMaterialValidator.getCreatedBy());
            if(null != list && list.size()>0){
                SystemAttachmentValidator validator = list.get(0);
                intellectMaterialValidator.setFileUrl(validator.getAttachmentUri());
                intellectMaterialValidator.setFileType(validator.getDocType());
                intellectMaterialValidator.setFileSize(validator.getDocSize().intValue());
                intellectMaterialValidator.setFileName(validator.getAttachmentName());

                requestTemplateResource.setResourceType(intellectMaterialValidator.getMaterialType());
                requestTemplateResource.setBizId(intellectMaterialValidator.getBusiness());
                requestTemplateResource.setFileType("stream");
                String dataUri = "data:"+intellectMaterialValidator.getMaterialType()+"/"+intellectMaterialValidator.getFileType()+";base64,";
                requestTemplateResource.setFileStream(dataUri+validator.getBase64());
                requestTemplateResource.setImageRate(intellectMaterialValidator.getImageRate());
                requestTemplateResource.setDescription(intellectMaterialValidator.getMaterialName());

                //log.info("[requestTemplateResource ]:{}",new Gson().toJson(requestTemplateResource));
            }

            com.smoc.cloud.intellect.remote.response.ResponseDataUtil<Map<String, String>> responseDataUtil = requestService.uploadTemplateResource(requestTemplateResource);
            log.info("[responseDataUtil ]:{}",new Gson().toJson(responseDataUtil));
            if(0 != responseDataUtil.getSubCode()){
                return ResponseDataUtil.buildError("素材同步梦网失败："+responseDataUtil.getMessage());
            }
            //log.info("[上传的文件 ]:{}",new Gson().toJson(list));
            ResponseData data = intellectMaterialFeign.save(intellectMaterialValidator, op);
            return data;
        } catch (Exception e) {
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }


    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    public ResponseData<IntellectMaterialValidator> findById(String id) {
        try {
            ResponseData data = intellectMaterialFeign.findById(id);
            //log.info("[findById]:{}",new Gson().toJson(data));
            return data;
        } catch (Exception e) {
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

                String base64 = FileBase64Converter.convertFileToBase64(file);
                //log.info("[base64]:{}",base64);
                String fileName = file.getOriginalFilename();//文件名
                //log.info("[fileName]:{}",fileName);
                String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);// 后缀名
                String url = module+"/"+pathName+"/"+day+"/"+fileName;
                String path = intelligenceProperties.getResourceFileRootPath()+url;

                //log.info("[filePath]:{}",path);
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
                info.setBase64(base64);
                list.add(info);
            }
        }
        return list;
    }


}
