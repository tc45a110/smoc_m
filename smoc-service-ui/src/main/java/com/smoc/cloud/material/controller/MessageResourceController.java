package com.smoc.cloud.material.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.qo.Dict;
import com.smoc.cloud.common.auth.qo.DictType;
import com.smoc.cloud.common.auth.qo.Nodes;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.template.AccountResourceInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.material.service.*;
import com.smoc.cloud.properties.ResourceProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 资源管理
 */
@Slf4j
@RestController
@RequestMapping("/resource")
public class MessageResourceController {


    @Autowired
    private MessageResourceService messageResourceService;

    @Autowired
    private ResourceProperties resourceProperties;

    @Autowired
    private SystemUserLogService systemUserLogService;

    /**
     * 资源维护主页面
     */
    @RequestMapping(value = "/main/{businessType}", method = RequestMethod.GET)
    public ModelAndView main(@PathVariable String businessType, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("resource/resource_main");

        view.addObject("businessType", businessType);

        return view;
    }


    /**
     * 资源类型树
     */
    @RequestMapping(value = "/typetree/{parentId}", method = RequestMethod.GET)
    public List<Nodes> typetree(@PathVariable String parentId, HttpServletRequest request) {
        List<Nodes> typeNodes = new ArrayList<Nodes>();

        ServletContext context = request.getServletContext();
        Map<String, DictType> dictMap = (Map<String, DictType>) context.getAttribute("dict");

        if (dictMap != null) {
            DictType dictType = dictMap.get("helpSelfType");
            if (!StringUtils.isEmpty(dictType.getDict()) && dictType.getDict().size() > 0) {
                for (Dict dict_tmp : dictType.getDict()) {
                    Nodes node_tmp = new Nodes();
                    node_tmp.setId(dict_tmp.getFieldCode());
                    node_tmp.setHref("0");
                    node_tmp.setLazyLoad(false);
                    node_tmp.setText(dict_tmp.getFieldName());

                    typeNodes.add(node_tmp);
                }
            }
        }

        return typeNodes;
    }

    /**
     * 首次加载的资源列表
     */
    @RequestMapping(value = "/list/{businessType}", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable String businessType, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("resource/resource_list");

        //默认查询字典中取出的第一个类型的资源
        String firstTypeCode = null;
        ServletContext context = request.getServletContext();
        Map<String, DictType> dictMap = (Map<String, DictType>) context.getAttribute("dict");
        if (dictMap != null) {
            DictType dictType = dictMap.get("helpSelfType");
            if (dictType.getDict() != null && dictType.getDict().size() > 0) {
                firstTypeCode = dictType.getDict().get(0).getFieldCode();
            }
        }

        return listByType(businessType, firstTypeCode, request);
    }

    /**
     * 资源类型下资源列表
     */
    @RequestMapping(value = "/list/{businessType}/{resourceType}", method = RequestMethod.GET)
    public ModelAndView listByType(@PathVariable String businessType, @PathVariable String resourceType, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("resource/resource_list");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        PageParams<AccountResourceInfoValidator> params = new PageParams<AccountResourceInfoValidator>();
        params.setPageSize(15);
        params.setCurrentPage(1);
        AccountResourceInfoValidator accountResourceInfoValidator = new AccountResourceInfoValidator();
        accountResourceInfoValidator.setEnterpriseId(user.getOrganization());
        accountResourceInfoValidator.setBusinessType(businessType);
        accountResourceInfoValidator.setResourceType(resourceType);
        params.setParams(accountResourceInfoValidator);

        ResponseData<PageList<AccountResourceInfoValidator>> data = messageResourceService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("accountResourceInfoValidator", accountResourceInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("businessType", businessType);
        view.addObject("resourceType", resourceType);
        return view;
    }

    /**
     * 资源类型下资源列表
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute AccountResourceInfoValidator accountResourceInfoValidator,PageParams params, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("resource/resource_list");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //转换文件类型，将文件1，音频2，视频3转成具体的jpg,mp3等后缀，在查询数据时做后缀名过滤
        if("1".equals(accountResourceInfoValidator.getFileType())){
            accountResourceInfoValidator.setResourceAttchmentType(resourceProperties.getResourceAllowFormat()[0]);
        }else if("2".equals(accountResourceInfoValidator.getFileType())){
            accountResourceInfoValidator.setResourceAttchmentType(resourceProperties.getResourceAllowFormat()[1]);
        }else if("3".equals(accountResourceInfoValidator.getFileType())){
            accountResourceInfoValidator.setResourceAttchmentType(resourceProperties.getResourceAllowFormat()[2]);
        }

        accountResourceInfoValidator.setEnterpriseId(user.getOrganization());
        params.setParams(accountResourceInfoValidator);

        ResponseData<PageList<AccountResourceInfoValidator>> data = messageResourceService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("accountResourceInfoValidator", accountResourceInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("businessType", accountResourceInfoValidator.getBusinessType());
        view.addObject("resourceType", accountResourceInfoValidator.getResourceType());
        return view;
    }

    /**
     * 添加
     *
     * @param resourceType
     * @param request
     * @return
     */
    @RequestMapping(value = "/add/{businessType}/{resourceType}", method = RequestMethod.GET)
    public ModelAndView add(@PathVariable String businessType, @PathVariable String resourceType, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("resource/resource_edit");

        AccountResourceInfoValidator accountResourceInfoValidator = new AccountResourceInfoValidator();
        accountResourceInfoValidator.setId(UUID.uuid32());
        accountResourceInfoValidator.setResourceStatus("1");
        accountResourceInfoValidator.setResourceType(resourceType);
        accountResourceInfoValidator.setBusinessType(businessType);

        view.addObject("op", "add");
        view.addObject("accountResourceInfoValidator", accountResourceInfoValidator);

        return view;
    }

    /**
     * 编辑资源
     * @param id
     * @return
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("resource/resource_edit");
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        ResponseData<AccountResourceInfoValidator> data = messageResourceService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        AccountResourceInfoValidator accountResourceInfoValidator = data.getData();

        //查看是否是自己企业
        if(!user.getOrganization().equals(accountResourceInfoValidator.getEnterpriseId())){
            view.addObject("error", "不能进行操作！");
            return view;
        }

        view.addObject("op", "edit");
        view.addObject("accountResourceInfoValidator", accountResourceInfoValidator);

        return view;
    }

    /**
     * 保存资源
     *
     * @param accountResourceInfoValidator
     * @param result
     * @param op
     * @param request
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated AccountResourceInfoValidator accountResourceInfoValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("resource/resource_edit");
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        if (result.hasErrors()) {
            view.addObject("accountResourceInfoValidator", accountResourceInfoValidator);
            view.addObject("op", op);
            return view;
        }

        //资源文件不能为空
        if (StringUtils.isEmpty(accountResourceInfoValidator.getResourceAttchment())) {
            view.addObject("accountResourceInfoValidator", accountResourceInfoValidator);
            view.addObject("op", op);
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":资源文件不能为空");
            return view;
        }

        if ("add".equals(op)) {
            accountResourceInfoValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));
            accountResourceInfoValidator.setCreatedBy(user.getRealName());
        } else if ("edit".equals(op)) {
            accountResourceInfoValidator.setUpdatedTime(new Date());
            accountResourceInfoValidator.setUpdatedBy(user.getRealName());
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }

        accountResourceInfoValidator.setEnterpriseId(user.getOrganization());

        //保存操作
        ResponseData data = messageResourceService.save(accountResourceInfoValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("MESSAGE_RESOURCE", accountResourceInfoValidator.getId(), "add".equals(op) ? accountResourceInfoValidator.getCreatedBy() : accountResourceInfoValidator.getUpdatedBy(), op, "add".equals(op) ? "添加模板资源" : "修改模板资源", JSON.toJSONString(accountResourceInfoValidator));
        }

        //记录日志
        log.info("[资源管理][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(accountResourceInfoValidator));

        view.setView(new RedirectView("/resource/list/" + accountResourceInfoValidator.getBusinessType() + "/" + accountResourceInfoValidator.getResourceType(), true, false));
        return view;
    }

    /**
     * 删除信息
     *
     * @return
     */
    @RequestMapping(value = "/deleteById/{id}", method = RequestMethod.GET)
    public ModelAndView deleteById(@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("resource/resource_edit");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询信息
        ResponseData<AccountResourceInfoValidator> infoData = messageResourceService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(infoData.getCode())) {
            view.addObject("error", infoData.getCode() + ":" + infoData.getMessage());
            return view;
        }

        //查看是否是自己企业
        if(!user.getOrganization().equals(infoData.getData().getEnterpriseId())){
            view.addObject("error", "不能进行删除操作！");
            return view;
        }

        //删除操作
        ResponseData data = messageResourceService.deleteById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("MESSAGE_RESOURCE", infoData.getData().getId(),user.getRealName(), "delete", "删除模板资源",JSON.toJSONString(infoData.getData()));
        }

        //记录日志
        log.info("[资源管理][{}][{}]数据:{}", "delete", user.getUserName(), JSON.toJSONString(infoData.getData()));

        view.setView(new RedirectView("/resource/list/" + infoData.getData().getBusinessType() + "/" + infoData.getData().getResourceType(), true, false));
        return view;
    }


    /**
     * 上传资源文件
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String uploadFile(HttpServletRequest request) {
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        String filePath = "";

        MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = mRequest.getFile("file");
        if (file != null && file.getSize() > 0) {
            try {
                //文件超出大小限制，前端获取后提示用户  getResourceFileSizeLimit以K为单位，故*1024
                if (file.getSize() > resourceProperties.getResourceFileSizeLimit() * 1024) {
                    return "上传失败，资源文件大小不能超过" + resourceProperties.getResourceFileSizeLimit() + "k";
                }

                //文件格式判断，获取配置项设置的值，如为空则表示无限制，如不符合返回-1，前端获取后提示用户
                String suffixName = null;
                String resourceImgFormat = resourceProperties.getResourceAllowFormat()[0];
                String resourceAudioFormat = resourceProperties.getResourceAllowFormat()[1];
                String resourceVedioFormat = resourceProperties.getResourceAllowFormat()[2];
                StringBuilder fileFormat = new StringBuilder();
                if (!StringUtils.isEmpty(resourceImgFormat)) {
                    fileFormat.append(resourceImgFormat);
                }
                if (!StringUtils.isEmpty(resourceAudioFormat)) {
                    if (fileFormat.length() > 0) {
                        fileFormat.append(",");
                    }
                    fileFormat.append(resourceAudioFormat);
                }
                if (!StringUtils.isEmpty(resourceVedioFormat)) {
                    if (fileFormat.length() > 0) {
                        fileFormat.append(",");
                    }
                    fileFormat.append(resourceVedioFormat).append(",");
                }
                if (!StringUtils.isEmpty(fileFormat.toString())) {
                    suffixName = checkFileFormat(file.getOriginalFilename(), fileFormat.toString().split(","));
                    if (suffixName == null) {
                        return "上传失败，资源文件不符合格式，必须为" + fileFormat.toString() + "文件";
                    }
                } else {
                    suffixName = file.getOriginalFilename().split("\\.")[1];
                }

                String nowDay = DateTimeUtils.currentDate(new Date());
                String uuid = UUID.uuid32();
                filePath = "/" + nowDay + "/" + user.getOrganization() + "/" + uuid + "." + suffixName;

                File desFile = new File(resourceProperties.getResourceFileRootPath() + filePath);
                if (!desFile.getParentFile().exists()) {
                    desFile.getParentFile().mkdirs();
                }

                //file.transferTo(desFile);
                FileUtils.copyInputStreamToFile(file.getInputStream(), desFile);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("[资源管理][upload][{}]数据::{}", user.getUserName(), e.getMessage());
                return "上传失败，请稍后重试";
            }
        }

        return filePath;
    }

    /**
     * 下载资源文件
     *
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value = "/download/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadFile(@PathVariable String id, HttpServletRequest request) {
        ResponseEntity<byte[]> entity = null;

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //去掉id中的时间戳
        if (!StringUtils.isEmpty(id)) {
            id = id.split("_")[0];
        }

        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return entity;
        }

        ResponseData<AccountResourceInfoValidator> data = messageResourceService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            return entity;
        }

        AccountResourceInfoValidator resourceValidator = data.getData();
        if (StringUtils.isEmpty(resourceValidator.getResourceAttchment())) {
            return entity;
        }

        //查看是否是自己企业
        if(!user.getOrganization().equals(resourceValidator.getEnterpriseId())){
            return entity;
        }

        File downloadFile = new File(resourceProperties.getResourceFileRootPath() + resourceValidator.getResourceAttchment());
        //文件不存在
        if (!downloadFile.exists()) {
            log.info("[资源管理][download][{}]数据::{}", user.getUserName(), "文件不存在:" + downloadFile.getAbsolutePath());
            return entity;
        }

        //读取文件内容输入流中
        InputStream in = null;
        try {
            in = new FileInputStream(downloadFile);
            byte[] body = new byte[in.available()];
            in.read(body);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attchement;filename=" + downloadFile.getName());
            HttpStatus statusCode = HttpStatus.OK;
            entity = new ResponseEntity<byte[]>(body, headers, statusCode);
        } catch (Exception e) {
            log.error("[资源管理][download][{}]数据::{}", user.getUserName(), e.getMessage());
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return entity;
    }

    /**
     * 显示缩略图
     * @param resUrl
     * @param request
     * @param response
     */
    @RequestMapping(value = "/show/{resUrl}", method = RequestMethod.GET)
    public void show(@PathVariable String resUrl, HttpServletRequest request, HttpServletResponse response) {

        SecurityUser user = (SecurityUser)request.getSession().getAttribute("user");
/*
        //去掉id中的时间戳
        if(!StringUtils.isEmpty(id)){
            id = id.split("_")[0];
        }

        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return;
        }

        ResponseData<AccountResourceInfoValidator> data = messageResourceService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            return;
        }

        AccountResourceInfoValidator resourceValidator = data.getData();
        if(StringUtils.isEmpty(resourceValidator.getResourceAttchment())){
            return;
        }

        //查看是否是自己企业
        if(!user.getOrganization().equals(data.getData().getEnterpriseId())){
            return;
        }*/

        File downloadFile = new File(resourceProperties.getResourceFileRootPath() + resUrl);
        //文件不存在
        if(!downloadFile.exists()){
            log.info("[资源管理][download][{}]数据::{}", user.getUserName(), "文件不存在:"+downloadFile.getAbsolutePath());
            return;
        }

        String suffixType = resUrl.substring(resUrl.lastIndexOf(".") + 1);

        //图片
        if(resourceProperties.getResourceAllowFormat()[0].contains(suffixType)){
            image(suffixType,downloadFile,user,response);
        }
        //视频或音频
        if(resourceProperties.getResourceAllowFormat()[2].contains(suffixType) || resourceProperties.getResourceAllowFormat()[1].contains(suffixType)){
            video(suffixType,downloadFile,user,request,response);
        }

    }

    private void video(String suffixType, File file, SecurityUser user, HttpServletRequest request,HttpServletResponse response) {
        //获取从那个字节开始读取文件
        String rangeString = request.getHeader("Range");

        try {
            //获取响应的输出流
            OutputStream outputStream = response.getOutputStream();
            if(file.exists()){
                RandomAccessFile targetFile = new RandomAccessFile(file, "r");
                long fileLength = targetFile.length();
                //播放
                if(rangeString != null){

                    long range = Long.valueOf(rangeString.substring(rangeString.indexOf("=") + 1, rangeString.indexOf("-")));
                    //设置内容类型
                    if("mp4".equals(suffixType)){
                        response.setHeader("Content-Type", "video/mp4");
                    }
                    if("3gp".equals(suffixType)){
                        response.setHeader("Content-Type", "video/3gp");
                    }
                    if("mp3".equals(suffixType)){
                        response.setHeader("Content-Type", "audio/mp3");
                    }
                    if("amr".equals(suffixType)){
                        response.setHeader("Content-Type", "audio/amr");
                    }
                    if("aac".equals(suffixType)){
                        response.setHeader("Content-Type", "audio/aac");
                    }
                    if("midi".equals(suffixType)){
                        response.setHeader("Content-Type", "audio/midi");
                    }
                    if("wav".equals(suffixType)){
                        response.setHeader("Content-Type", "audio/wav");
                    }
                    //设置此次相应返回的数据长度
                    response.setHeader("Content-Length", String.valueOf(fileLength - range));
                    //设置此次相应返回的数据范围
                    response.setHeader("Content-Range", "bytes "+range+"-"+(fileLength-1)+"/"+fileLength);
                    //返回码需要为206，而不是200
                    response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                    //设定文件读取开始位置（以字节为单位）
                    targetFile.seek(range);
                }

                byte[] cache = new byte[1024 * 300];
                int flag;
                while ((flag = targetFile.read(cache))!=-1){
                    outputStream.write(cache, 0, flag);
                }
            }

            outputStream.flush();
            outputStream.close();

        } catch (IOException e) {
            log.error("[资源管理][download][{}]数据::{}", user.getUserName(), e.getMessage());
        }
    }

    private void image(String suffixType,File downloadFile,SecurityUser user, HttpServletResponse response) {
        //读取文件内容输入流中
        InputStream in = null;
        try {
            //设置相应类型,告诉浏览器输出的内容为图片
            response.setContentType("image/jpeg");

            //设置响应头信息，告诉浏览器不要缓存此内容
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expire", 0);

            in = new FileInputStream(downloadFile);
            BufferedImage bImage = ImageIO.read(in);
            ImageIO.write(bImage, suffixType, response.getOutputStream());
        } catch (Exception e) {
            log.error("[资源管理][download][{}]数据::{}", user.getUserName(), e.getMessage());
            e.printStackTrace();
        } finally {
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String checkFileFormat(String fileName, String[] suffixNames) {
        if (StringUtils.isEmpty(fileName)) {
            return null;
        }
        if (suffixNames == null || suffixNames.length == 0) {
            return null;
        }

        for (String suffixName : suffixNames) {
            if (StringUtils.isEmpty(suffixName)) {
                continue;
            }
            if (fileName.endsWith(suffixName)) {
                return suffixName;
            }
        }

        return null;
    }

    /*
     *资源列表显示，创建模板时选择使用
     */
    @RequestMapping(value = "/templResource", method = RequestMethod.POST)
    public JSONObject templResource(@RequestBody JSONObject queryCondition, HttpServletRequest request) {
        JSONObject result = new JSONObject();

        SecurityUser user = (SecurityUser)request.getSession().getAttribute("user");

        String fileType = null;
        String resourceType = null;
        String currentPage = "1";
        String screenWidth = null;
        if(queryCondition!=null){
            fileType = queryCondition.getString("fileType");
            resourceType = queryCondition.getString("resourceType");
            currentPage = queryCondition.getString("currentPage");
            screenWidth = queryCondition.getString("screenWidth");
        }

        //根据屏幕尺寸调整显示资源的条数，避免前端撑开div
        Integer pageSize = 11;
        if(new Integer(screenWidth)<=1366){
            pageSize = 7;
        }
        PageParams<AccountResourceInfoValidator> params = new PageParams<AccountResourceInfoValidator>();
        params.setPageSize(pageSize);
        params.setCurrentPage(new Integer(currentPage));
        AccountResourceInfoValidator accountResourceInfoValidator = new AccountResourceInfoValidator();
        accountResourceInfoValidator.setEnterpriseId(user.getOrganization());
        accountResourceInfoValidator.setResourceType(resourceType);
        accountResourceInfoValidator.setFileType(fileType);
        if("1".equals(accountResourceInfoValidator.getFileType())){
            accountResourceInfoValidator.setResourceAttchmentType(resourceProperties.getResourceAllowFormat()[0]);
        }else if("2".equals(accountResourceInfoValidator.getFileType())){
            accountResourceInfoValidator.setResourceAttchmentType(resourceProperties.getResourceAllowFormat()[1]);
        }else if("3".equals(accountResourceInfoValidator.getFileType())){
            accountResourceInfoValidator.setResourceAttchmentType(resourceProperties.getResourceAllowFormat()[2]);
        }

        params.setParams(accountResourceInfoValidator);
        ResponseData<PageList<AccountResourceInfoValidator>> data = messageResourceService.page(params);

        result.put("list", data.getData().getList());
        result.put("pages", data.getData().getPageParams().getPages());
        result.put("currentPage", data.getData().getPageParams().getCurrentPage());

        return result;
    }
}
