package com.smoc.cloud.filter.industry.controller;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.qo.Dict;
import com.smoc.cloud.common.auth.qo.DictType;
import com.smoc.cloud.common.auth.qo.Nodes;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.filter.*;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.filter.black.service.BlackService;
import com.smoc.cloud.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * 行业黑名单
 */
@Slf4j
@RestController
@RequestMapping("/filter/industry/black")
public class IndustryBlackController {

    @Autowired
    private BlackService blackService;

    private String industry = "INDUSTRY";

    /**
     * 分类关键词 main 页面
     *
     * @return
     */
    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public ModelAndView main(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/industry/industry_main");
        view.addObject("parentId", "root");

        Map<String, DictType> dictMap = (Map<String, DictType>) request.getServletContext().getAttribute("dict");
        DictType infoType = dictMap.get("industryBlackList");
        List<Dict> list = infoType.getDict();
        String code = "root";
        if(!StringUtils.isEmpty(list) && list.size()>0){
            code = list.get(0).getFieldCode();
        }
        view.addObject("code", code);
        return view;

    }

    /**
     * 名单 列表页
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/list/{parentId}", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable String parentId, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/industry/black/industry_black_list");

        //初始化数据
        PageParams<FilterBlackListValidator> params = new PageParams<FilterBlackListValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        FilterBlackListValidator filterBlackListValidator = new FilterBlackListValidator();
        filterBlackListValidator.setEnterpriseId(industry);
        filterBlackListValidator.setGroupId(parentId);
        params.setParams(filterBlackListValidator);

        //查询
        ResponseData<PageList<FilterBlackListValidator>> data = blackService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("filterBlackListValidator", filterBlackListValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("parentId",parentId);
        view.addObject("dictValueMap", dictMap(request));

        return view;

    }

    /**
     * 黑名单列表查询
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute FilterBlackListValidator filterBlackListValidator, PageParams pageParams, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/industry/black/industry_black_list");

        //分页查询
        filterBlackListValidator.setEnterpriseId(industry);
        pageParams.setParams(filterBlackListValidator);

        ResponseData<PageList<FilterBlackListValidator>> data = blackService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("filterBlackListValidator", filterBlackListValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("parentId",filterBlackListValidator.getGroupId());
        view.addObject("dictValueMap", dictMap(request));

        return view;

    }

    /**
     * 添加黑名单
     *
     * @return
     */
    @RequestMapping(value = "/add/{parentId}", method = RequestMethod.GET)
    public ModelAndView add(@PathVariable String parentId,HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/industry/black/industry_black_edit");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(parentId);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //初始化参数
        FilterBlackListValidator filterBlackListValidator = new FilterBlackListValidator();
        filterBlackListValidator.setId(UUID.uuid32());
        filterBlackListValidator.setGroupId(parentId);
        filterBlackListValidator.setEnterpriseId(industry);
        filterBlackListValidator.setStatus("1");
        filterBlackListValidator.setIsSync("0");

        view.addObject("filterBlackListValidator",filterBlackListValidator);
        view.addObject("op","add");
        view.addObject("parentId",parentId);
        view.addObject("dictValueMap", dictMap(request));

        return view;

    }

    /**
     * 编辑黑名单
     *
     * @return
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/industry/black/industry_black_edit");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        ResponseData<FilterBlackListValidator> data = blackService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("filterBlackListValidator",data.getData());
        view.addObject("op","edit");
        view.addObject("parentId",data.getData().getGroupId());
        view.addObject("dictValueMap", dictMap(request));

        return view;

    }

    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated FilterBlackListValidator filterBlackListValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {
        SecurityUser user = (SecurityUser)request.getSession().getAttribute("user");
        ModelAndView view = new ModelAndView("filter/industry/black/industry_black_edit");


        //完成参数规则验证
        if (result.hasErrors()) {
            view.addObject("dictValueMap", dictMap(request));
            view.addObject("parentId",filterBlackListValidator.getGroupId());
            view.addObject("filterBlackListValidator", filterBlackListValidator);
            view.addObject("op", op);
            return view;
        }

        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            filterBlackListValidator.setCreatedTime(new Date());
            filterBlackListValidator.setCreatedBy(user.getRealName());
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            ResponseData<FilterBlackListValidator> data = blackService.findById(filterBlackListValidator.getId());
            filterBlackListValidator.setCreatedTime(data.getData().getCreatedTime());
            filterBlackListValidator.setUpdatedBy(user.getRealName());
            filterBlackListValidator.setUpdatedTime(new Date());
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }

        //记录日志
        log.info("[行业黑名单管理][{}][{}]数据:{}",op, user.getUserName(), JSON.toJSONString(filterBlackListValidator));

        //保存操作
        ResponseData data = blackService.save(filterBlackListValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.setView(new RedirectView("/filter/industry/black/list/" + filterBlackListValidator.getGroupId(), true, false));
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
        ModelAndView view = new ModelAndView("filter/industry/black/industry_black_edit");
        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        ResponseData<FilterBlackListValidator> whiteData = blackService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(whiteData.getCode())) {
            view.addObject("error", whiteData.getCode() + ":" + whiteData.getMessage());
            return view;
        }

        //记录日志
        log.info("[行业黑名单管理][delete][{}]数据::{}", user.getUserName(), JSON.toJSONString(whiteData.getData()));
        //删除操作
        ResponseData data = blackService.deleteById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.setView(new RedirectView("/filter/industry/black/list/" + whiteData.getData().getGroupId(), true, false));
        return view;
    }

    /**
     * 添加黑名单
     *
     * @return
     */
    @RequestMapping(value = "/upFilesView/{parentId}", method = RequestMethod.GET)
    public ModelAndView upFilesView(@PathVariable String parentId, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/industry/black/industry_black_upfiles_view");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(parentId);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        view.addObject("parentId",parentId);
        view.addObject("dictValueMap", dictMap(request));

        return view;

    }

    /**
     * 导入
     * @param request
     * @return
     */
    @RequestMapping(value = "/upFiles", method = RequestMethod.POST)
    public ModelAndView save(String groupId, HttpServletRequest request) {
        SecurityUser user = (SecurityUser)request.getSession().getAttribute("user");

        ModelAndView view = new ModelAndView("filter/industry/black/industry_black_upfiles_view");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(groupId);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        /**
         * 获取文件信息
         */
        MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = mRequest.getFile("file");
        if (file != null && file.getSize() > 0) {

            List<ExcelModel> list = FileUtils.readFile(file,"1");

            //批量保存
            if(!StringUtils.isEmpty(list) && list.size()>0){
                FilterBlackListValidator filterBlackListValidator = new FilterBlackListValidator();
                filterBlackListValidator.setEnterpriseId(industry);
                filterBlackListValidator.setGroupId(groupId);
                filterBlackListValidator.setExcelModelList(list);
                filterBlackListValidator.setIsSync("0");
                filterBlackListValidator.setStatus("1");
                filterBlackListValidator.setCreatedBy(user.getRealName());
                ResponseData data  = blackService.batchSave(filterBlackListValidator);
                if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
                    view.addObject("error", data.getCode() + ":" + data.getMessage());
                    return view;
                }
            }

            log.info("[行业黑名单管理][导入][{}]数据::{}", user.getUserName(), list.size());
        }

        view.setView(new RedirectView("/filter/industry/black/list/" + groupId, true, false));

        return view;
    }

    /**
     * 导出黑名单
     *
     * @return
     */
    @RequestMapping(value = "/downFilesView/{parentId}", method = RequestMethod.GET)
    public ModelAndView downFilesView(@PathVariable String parentId,HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/industry/black/industry_black_downfiles_view");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(parentId);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        view.addObject("type", "1");
        view.addObject("parentId",parentId);
        view.addObject("dictValueMap", dictMap(request));

        return view;

    }

    /**
     * 导出
     * @param expType
     * @param groupId
     * @param request
     * @param response
     */
    @RequestMapping(value = "/downFiles", method = RequestMethod.POST)
    public void exceBookData(String groupId,String expType,HttpServletRequest request, HttpServletResponse response) {

        /**
         * 查询要导出的数据
         */
        PageParams<FilterWhiteListValidator> params = new PageParams<FilterWhiteListValidator>();
        params.setPageSize(100000);
        params.setCurrentPage(1);
        FilterWhiteListValidator filterWhiteListValidator = new FilterWhiteListValidator();
        filterWhiteListValidator.setGroupId(groupId);
        params.setParams(filterWhiteListValidator);
        ResponseData<List<ExcelModel>> data = blackService.excelModel(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            return ;
        }

        Map<String, String> dictMap = dictMap(request);
        String excelname = dictMap.get(groupId)+"黑名单";

        FileUtils.downFiles(excelname,expType,data.getData(),request,response);
    }

    /**
     * 取字典数据，对关键词进行分类
     */
    private Map<String, String> dictMap(HttpServletRequest request) {
        Map<String, DictType> dictMap = (Map<String, DictType>) request.getServletContext().getAttribute("dict");
        //行业分类
        DictType infoType = dictMap.get("industryBlackList");

        Map<String, String> dictValueMap = new HashMap<>();

        for (Dict dict : infoType.getDict()) {
            dictValueMap.put(dict.getFieldCode(), dict.getFieldName());
        }

        return dictValueMap;
    }

    /**
     * 树形
     *
     * @param parentId
     * @param request
     * @return
     */
    @RequestMapping(value = "/tree/{parentId}", method = RequestMethod.GET)
    public List<Nodes> treeByParentId(@PathVariable String parentId, HttpServletRequest request) {
        //跟节点
        Nodes root = new Nodes();
        root.setId("root");
        root.setHref("0");
        root.setLazyLoad(false);
        root.setSvcType("root");
        root.setText("行业分类");

        Map<String, DictType> dictMap = (Map<String, DictType>) request.getServletContext().getAttribute("dict");

        //行业黑名单
        DictType infoType = dictMap.get("industryBlackList");
        Nodes blackNode = new Nodes();
        blackNode.setId("1");
        blackNode.setHref("0");
        blackNode.setLazyLoad(false);
        blackNode.setSvcType("BLACK");
        blackNode.setText("行业黑名单");
        List<Nodes> blackNodeList = new ArrayList<>();

        List<Dict> dictList = infoType.getDict();
        if(!StringUtils.isEmpty(dictList) && dictList.size()>0){
            for(int i=0;i<dictList.size();i++){
                Nodes dictNode = new Nodes();
                Dict dict = dictList.get(i);
                dictNode.setId(dict.getFieldCode());
                dictNode.setHref(dict.getFieldCode());
                dictNode.setLazyLoad(false);
                dictNode.setSvcType("leaf");
                dictNode.setOrgCode("BLACK");
                dictNode.setText(dict.getFieldName());
                dictNode.setIcon(infoType.getIcon());
                if(i==0){
                    Map<String, Object> stateMap = new HashMap<String, Object>();
                    stateMap.put("selected", true);
                    dictNode.setState(stateMap);
                }
                blackNodeList.add(dictNode);
            }
        }
        blackNode.setNodes(blackNodeList);

        //行业白名单
        Nodes whiteNode = new Nodes();
        whiteNode.setId("2");
        whiteNode.setHref("0");
        whiteNode.setLazyLoad(false);
        whiteNode.setSvcType("WHITE");
        whiteNode.setText("行业白名单");

        //使用stream拷贝list
        List<Nodes> whiteList = blackNodeList.stream()
                .map(e -> {
                    Nodes dictNode = new Nodes();
                    dictNode.setId(e.getId());
                    dictNode.setHref(e.getHref());
                    dictNode.setLazyLoad(false);
                    dictNode.setSvcType("leaf");
                    dictNode.setOrgCode("WHITE");
                    dictNode.setText(e.getText());
                    dictNode.setIcon(infoType.getIcon());
                    return dictNode;
                })
                .collect(Collectors.toList());
        //whiteList.stream().forEach(f -> f.setOrgCode("WHITE"));

        whiteNode.setNodes(whiteList);

        List<Nodes> rootList = new ArrayList<>();
        rootList.add(blackNode);
        rootList.add(whiteNode);
        root.setNodes(rootList);

        List<Nodes> list = new ArrayList<>();
        list.add(root);

        return list;
    }
}
