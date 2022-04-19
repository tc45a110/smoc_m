package com.smoc.cloud.parameter.errorcode.controller;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.qo.Dict;
import com.smoc.cloud.common.auth.qo.DictType;
import com.smoc.cloud.common.auth.qo.Nodes;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.parameter.SystemErrorCodeValidator;
import com.smoc.cloud.common.smoc.parameter.model.ErrorCodeExcelModel;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.parameter.errorcode.service.SystemErrorCodeService;
import com.smoc.cloud.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

/**
 * 错误码
 */
@Slf4j
@RestController
@RequestMapping("/errorCode")
public class SystemErrorCodeController {

    @Autowired
    private SystemErrorCodeService systemErrorCodeService;

    /**
     * 分类main 页面
     *
     * @return
     */
    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public ModelAndView main() {

        ModelAndView view = new ModelAndView("parameter/errorcode/error_code_main");
        view.addObject("parentId", "root");
        return view;

    }

    /**
     * 错误码列表
     *
     * @return
     */
    @RequestMapping(value = "/list/{parentId}", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable String parentId, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("parameter/errorcode/error_code_list");

        //初始化数据
        PageParams<SystemErrorCodeValidator> params = new PageParams<SystemErrorCodeValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        SystemErrorCodeValidator systemErrorCodeValidator = new SystemErrorCodeValidator();
        systemErrorCodeValidator.setCodeType(parentId);
        params.setParams(systemErrorCodeValidator);

        //查询
        ResponseData<PageList<SystemErrorCodeValidator>> data = systemErrorCodeService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("systemErrorCodeValidator", systemErrorCodeValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("parentId",parentId);

        return view;

    }

    /**
     * 错误码列表查询
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute SystemErrorCodeValidator systemErrorCodeValidator, PageParams pageParams) {

        ModelAndView view = new ModelAndView("parameter/errorcode/error_code_list");

        //分页查询
        pageParams.setParams(systemErrorCodeValidator);

        ResponseData<PageList<SystemErrorCodeValidator>> data = systemErrorCodeService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("systemErrorCodeValidator", systemErrorCodeValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("parentId",systemErrorCodeValidator.getCodeType());

        return view;

    }

    /**
     * 添加黑名单
     *
     * @return
     */
    @RequestMapping(value = "/add/{parentId}", method = RequestMethod.GET)
    public ModelAndView add(@PathVariable String parentId,HttpServletRequest request) {

        ModelAndView view = new ModelAndView("parameter/errorcode/error_code_edit");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(parentId);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //初始化参数
        SystemErrorCodeValidator systemErrorCodeService = new SystemErrorCodeValidator();
        systemErrorCodeService.setId(UUID.uuid32());
        systemErrorCodeService.setCodeType(parentId);
        systemErrorCodeService.setStatus("1");

        view.addObject("systemErrorCodeService",systemErrorCodeService);
        view.addObject("op","add");

        return view;

    }

    /**
     * 编辑黑名单
     *
     * @return
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("parameter/errorcode/error_code_edit");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        ResponseData<SystemErrorCodeValidator> data = systemErrorCodeService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("systemErrorCodeService",data.getData());
        view.addObject("op","edit");

        return view;

    }

    /**
     * 保存信息
     * @param systemErrorCodeValidator
     * @param result
     * @param op
     * @param request
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated SystemErrorCodeValidator systemErrorCodeValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {
        SecurityUser user = (SecurityUser)request.getSession().getAttribute("user");
        ModelAndView view = new ModelAndView("parameter/errorcode/error_code_edit");

        //完成参数规则验证
        if (result.hasErrors()) {
            view.addObject("systemErrorCodeValidator", systemErrorCodeValidator);
            view.addObject("op", op);
            return view;
        }

        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            systemErrorCodeValidator.setCreatedTime(new Date());
            systemErrorCodeValidator.setCreatedBy(user.getRealName());
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            ResponseData<SystemErrorCodeValidator> data = systemErrorCodeService.findById(systemErrorCodeValidator.getId());
            systemErrorCodeValidator.setCreatedTime(data.getData().getCreatedTime());
            systemErrorCodeValidator.setUpdatedBy(user.getRealName());
            systemErrorCodeValidator.setUpdatedTime(new Date());
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }

        //记录日志
        log.info("[错误码管理][{}][{}]数据:{}",op, user.getUserName(), JSON.toJSONString(systemErrorCodeValidator));

        //保存操作
        ResponseData data = systemErrorCodeService.save(systemErrorCodeValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.setView(new RedirectView("/errorCode/list/" + systemErrorCodeValidator.getCodeType(), true, false));
        return view;
    }

    /**
     * 删除信息
     *
     * @return
     */
    @RequestMapping(value = "/deleteById/{id}", method = RequestMethod.GET)
    public ModelAndView deleteById(@PathVariable String id, HttpServletRequest request) {
        SecurityUser user = (SecurityUser)request.getSession().getAttribute("user");
        ModelAndView view = new ModelAndView("parameter/errorcode/error_code_edit");
        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        ResponseData<SystemErrorCodeValidator> whiteData = systemErrorCodeService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(whiteData.getCode())) {
            view.addObject("error", whiteData.getCode() + ":" + whiteData.getMessage());
            return view;
        }

        //记录日志
        log.info("[错误码管理][delete][{}]数据::{}", user.getUserName(), JSON.toJSONString(whiteData.getData()));
        //删除操作
        ResponseData data = systemErrorCodeService.deleteById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.setView(new RedirectView("/errorCode/list/" + whiteData.getData().getCodeType(), true, false));
        return view;
    }

    /**
     * 显示上传页面
     *
     * @return
     */
    @RequestMapping(value = "/upFilesView/{parentId}", method = RequestMethod.GET)
    public ModelAndView upFilesView(@PathVariable String parentId, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("parameter/errorcode/error_code_upfiles");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(parentId);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        view.addObject("parentId", parentId);

        return view;

    }

    /**
     * 导入
     * @param request
     * @return
     */
    @RequestMapping(value = "/upFiles", method = RequestMethod.POST)
    public ModelAndView save(String parentId, HttpServletRequest request) {
        SecurityUser user = (SecurityUser)request.getSession().getAttribute("user");

        ModelAndView view = new ModelAndView("filter/black/black_upfiles_view");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(parentId);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        /**
         * 获取文件信息
         */
        MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
        List<MultipartFile> file = mRequest.getFiles("file");
        if(!StringUtils.isEmpty(file) && file.size()>0){

            List<ErrorCodeExcelModel> list = FileUtils.readErrorCodeFile(file.get(0));

            //批量保存
            if(!StringUtils.isEmpty(list) && list.size()>0){
                SystemErrorCodeValidator systemErrorCodeValidator = new SystemErrorCodeValidator();
                systemErrorCodeValidator.setCodeType(parentId);
                systemErrorCodeValidator.setExcelModelList(list);
                systemErrorCodeValidator.setStatus("1");
                systemErrorCodeValidator.setCreatedBy(user.getRealName());
                ResponseData data  = systemErrorCodeService.batchSave(systemErrorCodeValidator);
                if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
                    view.addObject("error", data.getCode() + ":" + data.getMessage());
                    return view;
                }
            }

            log.info("[错误码管理][导入][{}]数据::{}", user.getUserName(), list.size());
        }

        view.setView(new RedirectView("/errorCode/list/" +parentId, true, false));

        return view;
    }

    /**
     * 树形
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public List<Nodes> treeByParentId(HttpServletRequest request) {
        //跟节点
        Nodes root = new Nodes();
        root.setId("root");
        root.setHref("0");
        root.setLazyLoad(false);
        root.setSvcType("root");
        root.setText("分类错误码");

        Map<String, DictType> dictMap = (Map<String, DictType>) request.getServletContext().getAttribute("dict");

        DictType errorCode = dictMap.get("errorCode");
        List<Dict> nodeList = errorCode.getDict();

        List<Nodes> rootList = new ArrayList<>();
        for(Dict dict :nodeList){
            Nodes node = new Nodes();
            node.setId(dict.getId());
            node.setHref("0");
            node.setLazyLoad(false);
            node.setSvcType(dict.getFieldCode());
            node.setText(dict.getFieldName());
            if("SYSTEM".equals(dict.getFieldCode())){
                Map<String, Object> stateMap = new HashMap<String, Object>();
                stateMap.put("selected", true);
                node.setState(stateMap);
            }
            rootList.add(node);

        }

        root.setNodes(rootList);

        List<Nodes> list = new ArrayList<>();
        list.add(root);

        return list;
    }

    /**
     * 下载模板
     */
    @RequestMapping(value = "/downFileTemp", method = RequestMethod.GET)
    public void downFileTemp(HttpServletRequest request, HttpServletResponse response) {

        String fileName = "错误码.xlsx";

        //设置文件路径
        ClassPathResource classPathResource = new ClassPathResource("static/files/" + fileName);
        try {
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            // 下载文件能正常显示中文
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 实现文件下载
        byte[] buffer = new byte[1024];
        InputStream fis = null;
        BufferedInputStream bis = null;
        try {
            fis = classPathResource.getInputStream();;
            bis = new BufferedInputStream(fis);
            OutputStream os = response.getOutputStream();
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }
            return ;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
