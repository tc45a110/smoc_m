package com.smoc.cloud.http.service;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.smoc.cloud.common.gateway.utils.ValidatorUtil;
import com.smoc.cloud.common.http.server.message.request.TemplateAddRequestParams;
import com.smoc.cloud.common.http.server.message.request.TemplateStatusRequestParams;
import com.smoc.cloud.common.http.server.multimedia.request.MultimediaTemplateAddParams;
import com.smoc.cloud.common.http.server.multimedia.request.MultimediaTemplateModel;
import com.smoc.cloud.common.redis.RedisConstant;
import com.smoc.cloud.common.redis.RedisModel;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.http.entity.AccountTemplateInfo;
import com.smoc.cloud.http.entity.MultimediaFormat;
import com.smoc.cloud.http.properties.ResourceProperties;
import com.smoc.cloud.http.repository.AccountTemplateInfoRepository;
import com.smoc.cloud.http.utils.FileBASE64Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Slf4j
@Service
public class TemplateService {

    @Autowired
    private ResourceProperties resourceProperties;

    @Autowired
    private SequenceService sequenceService;

    @Autowired
    private SystemHttpApiRequestService systemHttpApiRequestService;

    @Resource
    private AccountTemplateInfoRepository accountTemplateInfoRepository;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 添加普通短信模板
     *
     * @param params
     * @return
     */
    public ResponseData<Map<String, String>> addTemplate(TemplateAddRequestParams params) {

        //异步保存请求记录
        systemHttpApiRequestService.save(params.getOrderNo(), params.getAccount(), "addTemplate", new Gson().toJson(params));

        //获取模板ID
        String templateId = "TEMP" + sequenceService.findSequence("TEMPLATE");

        AccountTemplateInfo entity = new AccountTemplateInfo();
        entity.setTemplateId(templateId);
        entity.setBusinessAccount(params.getAccount());
        entity.setTemplateType("TEXT_SMS");

        entity.setTemplateContent(params.getContent());
        //模板类型 1 表示普通模板 2 表示变量模板
        entity.setTemplateFlag(params.getTemplateType());
        entity.setTemplateAgreementType("HTTP");

        //取账户参数
        //redis 查询  模板状态  3表示待审核 2表示通过审核
        RedisModel redisModel = (RedisModel) redisTemplate.opsForValue().get(RedisConstant.HTTP_SERVER_KEY + params.getAccount());
        if(null != redisModel && "0".equals(redisModel.getNoCheck())){
            entity.setTemplateStatus("2");
        }else{
            entity.setTemplateStatus("3");
        }

        entity.setCreatedBy("API");
        entity.setCreatedTime(DateTimeUtils.getNowDateTime());

        this.save(entity);

        //组织响应结果
        Map<String, String> result = new HashMap<>();
        result.put("orderNo", params.getOrderNo());
        result.put("templateId", templateId);
        return ResponseDataUtil.buildSuccess(result);

    }

    /**
     * 添加多媒体短信模板
     *
     * @param params
     * @return
     */
    public ResponseData<Map<String, String>> addMultimediaTemplate(MultimediaTemplateAddParams params) {

        //获取模板ID
        String templateId = "TEMP" + sequenceService.findSequence("TEMPLATE");

        List<MultimediaTemplateModel> items = params.getItems();
        if (null == items || items.size() < 1) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR);
        }

        //变量模板：FrameTxt变量文件个数
        int a = 0;

        for (MultimediaTemplateModel model : items) {
            if (!ValidatorUtil.validate(model)) {
                return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), ValidatorUtil.validateMessage(model));
            }

            //普通模版
            if ("1".equals(params.getTemplateType())) {
                Pattern ipPattern = Pattern.compile("['$']");
                Matcher matcher = ipPattern.matcher(model.getFrameTxt());
                boolean status = matcher.find();
                //log.info("[matcher.find()]:{}",status);
                if (status) {
                    return ResponseDataUtil.buildError(ResponseCode.PARAM_TEMPLATE_ERROR.getCode(), ResponseCode.PARAM_TEMPLATE_ERROR.getMessage());
                }
            }

            //变量模版模版
            if ("2".equals(params.getTemplateType())) {
                Pattern ipPattern = Pattern.compile("['$']");
                Matcher matcher = ipPattern.matcher(model.getFrameTxt());
                boolean status = matcher.find();
                //log.info("[matcher.find()]:{}",status);
                if (status) {
                    a++;
                }
            }
        }

        //变量模版模版
        if ("2".equals(params.getTemplateType())) {
            //等于0说明没有变量字符
            if(a == 0){
                return ResponseDataUtil.buildError(ResponseCode.PARAM_TEMPLATE_VARIABLE_ERROR.getCode(), ResponseCode.PARAM_TEMPLATE_VARIABLE_ERROR.getMessage());
            }
        }

        List<MultimediaFormat> multimediaFormats = new ArrayList<>();

        //判定文件总大小
        double fileTotalSize = 0;
        try {
            int i = 0;
            for (MultimediaTemplateModel model : items) {

                MultimediaFormat format = new MultimediaFormat();
                format.setIndex(i);
                i++;
                format.setResPostfix(model.getFileType());
                format.setStayTimes(model.getStayTimes() + "");
                format.setFrameTxt(model.getFrameTxt());
                format.setResType(model.getMediaType());

                //处理文件大小
                double fileSize = FileBASE64Utils.base64FileSize(model.getMediaFile());
                format.setResSize((int) fileSize);
                fileTotalSize += fileSize;
                if (fileTotalSize > new Double(resourceProperties.getResourceFileSizeLimit())) {
                    return ResponseDataUtil.buildError(ResponseCode.PARAM_MULTIMEDIA_ERROR.getCode(), ResponseCode.PARAM_MULTIMEDIA_ERROR.getMessage());
                }

                //生成本地文件
                String folder = DateTimeUtils.getDateFormat(new Date(), "yyyyMMdd");
                String filePath = resourceProperties.getResourceFileRootPath() + "/" + folder;
                String resId = UUID.uuid32();
                String fileName = resId +"."+ model.getFileType();
                format.setResUrl("/"+folder +"/" + fileName);

                multimediaFormats.add(format);

                FileBASE64Utils.base64ToFile(model.getMediaFile(), filePath, fileName);
                model.setMediaFile(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDataUtil.buildError(ResponseCode.PARAM_MULTIMEDIA_FILE_ERROR.getCode(), ResponseCode.PARAM_MULTIMEDIA_FILE_ERROR.getMessage());
        }

        //异步保存请求记录
        systemHttpApiRequestService.save(params.getOrderNo(), params.getAccount(), "addMultimediaTemplate", new Gson().toJson(params));


        AccountTemplateInfo entity = new AccountTemplateInfo();
        entity.setTemplateId(templateId);
        entity.setBusinessAccount(params.getAccount());
        entity.setTemplateType("MULTI_SMS");
        entity.setTemplateContent(params.getTemplateTitle());

        //模板类型 1 表示普通模板 2 表示变量模板
        entity.setTemplateFlag(params.getTemplateType());
        entity.setTemplateAgreementType("HTTP");

        entity.setMmAttchment(new Gson().toJson(multimediaFormats));

        //取账户参数
        //redis 查询  模板状态  3表示待审核 2表示通过审核
        RedisModel redisModel = (RedisModel) redisTemplate.opsForValue().get(RedisConstant.HTTP_SERVER_KEY + params.getAccount());
        if(null != redisModel || "0".equals(redisModel.getNoCheck())){
            entity.setTemplateStatus("2");
        }else{
            entity.setTemplateStatus("3");
        }
        entity.setCreatedBy("API");
        entity.setCreatedTime(DateTimeUtils.getNowDateTime());
        //异步保存
        this.save(entity);

        //组织响应结果
        Map<String, String> result = new HashMap<>();
        result.put("orderNo", params.getOrderNo());
        result.put("templateId", templateId);
        return ResponseDataUtil.buildSuccess(result);
    }

    /**
     * 添加国际短信模板
     *
     * @param params
     * @return
     */
    public ResponseData<Map<String, String>> addInterTemplate(TemplateAddRequestParams params) {

        //异步保存请求记录
        systemHttpApiRequestService.save(params.getOrderNo(), params.getAccount(), "addInterTemplate", new Gson().toJson(params));

        //获取模板ID
        String templateId = "TEMP" + sequenceService.findSequence("TEMPLATE");

        AccountTemplateInfo entity = new AccountTemplateInfo();
        entity.setTemplateId(templateId);
        entity.setBusinessAccount(params.getAccount());
        entity.setTemplateType("INTERNATIONAL_SMS");


        entity.setTemplateContent(params.getContent());
        //模板类型 1 表示普通模板 2 表示变量模板
        entity.setTemplateFlag(params.getTemplateType());
        entity.setTemplateAgreementType("HTTP");

        //取账户参数
        //redis 查询  模板状态  3表示待审核 2表示通过审核
        RedisModel redisModel = (RedisModel) redisTemplate.opsForValue().get(RedisConstant.HTTP_SERVER_KEY + params.getAccount());
        if(null != redisModel || "0".equals(redisModel.getNoCheck())){
            entity.setTemplateStatus("2");
        }else{
            entity.setTemplateStatus("3");
        }
        entity.setCreatedBy("API");
        entity.setCreatedTime(DateTimeUtils.getNowDateTime());

        this.save(entity);

        //组织响应结果
        Map<String, String> result = new HashMap<>();
        result.put("orderNo", params.getOrderNo());
        result.put("templateId", templateId);
        return ResponseDataUtil.buildSuccess(result);
    }

    /**
     * 获取模版状态
     *
     * @param params
     * @return
     */
    public ResponseData<Map<String, String>> getTemplateStatus(TemplateStatusRequestParams params) {

        //异步保存请求记录
        systemHttpApiRequestService.save(params.getOrderNo(), params.getAccount(), "getTemplateStatus", new Gson().toJson(params));


        Optional<AccountTemplateInfo> optional = accountTemplateInfoRepository.findAccountTemplateInfoByBusinessAccountAndTemplateIdAndTemplateAgreementType(params.getAccount(), params.getTemplateId(), "HTTP");
        if (!optional.isPresent()) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR.getCode(), ResponseCode.PARAM_QUERY_ERROR.getMessage());
        }

        AccountTemplateInfo accountTemplateInfo = optional.get();
        //可以处理前提逻辑
        //获取模板ID
        log.info("[获取普通状态]：{}", new Gson().toJson(params));

        //组织响应结果
        Map<String, String> result = new HashMap<>();
        result.put("orderNo", params.getOrderNo());
        result.put("templateId", params.getTemplateId());
        result.put("templateStatus", accountTemplateInfo.getTemplateStatus());
        result.put("statusDesc", getTemplateStatusDesc().get(accountTemplateInfo.getTemplateStatus()));
//        result.put("checkOpinions",accountTemplateInfo.getCheckOpinions());
        return ResponseDataUtil.buildSuccess(result);

    }

    /**
     * 保存或修改
     *
     * @param entity
     * @return
     */
    @Async
    @Transactional
    public void save(AccountTemplateInfo entity) {
        //记录日志
        log.info("[模板管理][API接口添加模板]数据:{}", JSON.toJSONString(entity));
        accountTemplateInfoRepository.saveHandle(entity);
    }

    public static Map<String, String> getTemplateStatusDesc() {
        Map<String, String> map = new HashMap<>();
        map.put("0", "无效");
        map.put("1", "被拒绝");
        map.put("2", "通过审核");
        map.put("3", "等待审核");
        return map;
    }
}
