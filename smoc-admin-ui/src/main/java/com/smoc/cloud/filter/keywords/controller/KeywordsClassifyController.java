package com.smoc.cloud.filter.keywords.controller;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.qo.Dict;
import com.smoc.cloud.common.auth.qo.DictType;
import com.smoc.cloud.common.auth.qo.Nodes;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.filter.ExcelModel;
import com.smoc.cloud.common.smoc.filter.FilterKeyWordsInfoValidator;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.filter.keywords.service.KeywordsService;
import com.smoc.cloud.filter.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 分类关键词
 */
@Slf4j
@RestController
@RequestMapping("/filter/keywords")
public class KeywordsClassifyController {

    @Autowired
    private KeywordsService keywordsService;

    /**
     * 分类关键词 main 页面
     *
     * @return
     */
    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public ModelAndView main() {

        ModelAndView view = new ModelAndView("filter/keywords/keyword_classify_main");
        view.addObject("parentId", "root");
        return view;

    }

    /**
     * 分类关键词 列表页
     *
     * @param code     大分类下的小分类 code
     * @param classify 大分类编码  CARRIER(运营商)，INDUSTRY_TYPE（行业分类）、INFO_TYPE（信息分类）
     * @param request
     * @return
     */
    @RequestMapping(value = "/classify/{classify}/list/{code}", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable String code, @PathVariable String classify, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/keywords/keyword_classify_list");


        //初始化数据
        PageParams<FilterKeyWordsInfoValidator> params = new PageParams<FilterKeyWordsInfoValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        FilterKeyWordsInfoValidator filterKeyWordsInfoValidator = new FilterKeyWordsInfoValidator();
        filterKeyWordsInfoValidator.setKeyWordsBusinessType(classify);
        filterKeyWordsInfoValidator.setBusinessId(code);
        params.setParams(filterKeyWordsInfoValidator);

        //查询
        ResponseData<PageList<FilterKeyWordsInfoValidator>> data = keywordsService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("filterKeyWordsInfoValidator", filterKeyWordsInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        view.addObject("classify", classify);
        view.addObject("code", code);
        view.addObject("dictValueMap", dictMap(request));

        return view;

    }

    /**
     * 列表查询
     * @param classify
     * @param code
     * @param filterKeyWordsInfoValidator
     * @param pageParams
     * @param request
     * @return
     */
    @RequestMapping(value = "/classify/{classify}/page/{code}", method = RequestMethod.POST)
    public ModelAndView page(@PathVariable String classify, @PathVariable String code, @ModelAttribute FilterKeyWordsInfoValidator filterKeyWordsInfoValidator, PageParams pageParams, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/keywords/keyword_classify_list");

        //分页查询
        filterKeyWordsInfoValidator.setKeyWordsBusinessType(classify);
        filterKeyWordsInfoValidator.setBusinessId(code);
        pageParams.setParams(filterKeyWordsInfoValidator);

        ResponseData<PageList<FilterKeyWordsInfoValidator>> data = keywordsService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }


        view.addObject("filterKeyWordsInfoValidator", filterKeyWordsInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());


        view.addObject("classify", classify);
        view.addObject("code", code);
        view.addObject("dictValueMap", dictMap(request));

        return view;

    }

    /**
     * 添加关键字
     *
     * @return
     */
    @RequestMapping(value = "/classify/{classify}/{code}/add", method = RequestMethod.GET)
    public ModelAndView add(@PathVariable String code, @PathVariable String classify, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/keywords/keyword_classify_edit_batch");


        view.addObject("classify", classify);
        view.addObject("code", code);
        view.addObject("dictValueMap", dictMap(request));

        return view;

    }

    /**
     * 编辑关键字
     *
     * @return
     */
    @RequestMapping(value = "/classify/{classify}/{code}/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String code, @PathVariable String classify,@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/keywords/keyword_classify_edit");


        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询数据
        ResponseData<FilterKeyWordsInfoValidator> data = keywordsService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("filterKeyWordsInfoValidator", data.getData());
        view.addObject("op", "edit");

        view.addObject("classify", classify);
        view.addObject("code", code);
        view.addObject("dictValueMap", dictMap(request));
        return view;

    }

    /**
     * 保存关键字
     *
     * @param classify
     * @param request
     * @return
     */
    @RequestMapping(value = "/classify/{classify}/batchSave/{code}", method = RequestMethod.POST)
    public ModelAndView batchSave(@PathVariable String classify,@PathVariable String code,HttpServletRequest request) {
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");
        ModelAndView view = new ModelAndView("filter/keywords/keyword_classify_edit_batch");

        FilterKeyWordsInfoValidator filterKeyWordsInfoValidator = new FilterKeyWordsInfoValidator();
        List<FilterKeyWordsInfoValidator> filterKeyWords = new ArrayList<>();

        //初始化其他变量
        filterKeyWordsInfoValidator.setKeyWordsBusinessType(classify);
        filterKeyWordsInfoValidator.setBusinessId(code);
        filterKeyWordsInfoValidator.setCreatedTime(new Date());
        filterKeyWordsInfoValidator.setCreatedBy(user.getRealName());

        //封装数据
        for (int i = 1; i <= 5; i++) {
            FilterKeyWordsInfoValidator validator = new FilterKeyWordsInfoValidator();
            String keyWordsType = request.getParameter("keyWordsType" + i);
            String keyWords = request.getParameter("keyWords" + i);
            String keyDesc = request.getParameter("keyDesc" + i);
            if (!StringUtils.isEmpty(keyWordsType) && !StringUtils.isEmpty(keyWords)) {
                validator.setKeyWordsType(keyWordsType);
                validator.setKeyWords(keyWords);
                validator.setKeyDesc(keyDesc);
                filterKeyWords.add(validator);
            }
        }

        filterKeyWordsInfoValidator.setFilterKeyWordsList(filterKeyWords);

        //保存操作
        if (!StringUtils.isEmpty(filterKeyWords) && filterKeyWords.size() > 0) {

            //记录日志
            log.info("[分类关键词库管理][{}][{}]数据:{}", "add", user.getUserName(), JSON.toJSONString(filterKeyWordsInfoValidator));

            ResponseData data = keywordsService.batchSave(filterKeyWordsInfoValidator, "add");
            if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
                view.addObject("error", data.getCode() + ":" + data.getMessage());
                return view;
            }

        }

        view.setView(new RedirectView("/filter/keywords/classify/"+classify+"/list/"+code, true, false));
        return view;
    }

    @RequestMapping(value = "/classify/{classify}/save/{code}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated FilterKeyWordsInfoValidator filterKeyWordsInfoValidator, BindingResult result, @PathVariable String classify, @PathVariable String code, HttpServletRequest request) {
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");
        ModelAndView view = new ModelAndView("filter/keywords/keyword_classify_edit");

        //完成参数规则验证
        if (result.hasErrors()) {
            view.addObject("classify", classify);
            view.addObject("code", code);
            view.addObject("dictValueMap", dictMap(request));
            view.addObject("filterKeyWordsInfoValidator", filterKeyWordsInfoValidator);
            return view;
        }

        //初始化其他变量
        ResponseData<FilterKeyWordsInfoValidator> indoData = keywordsService.findById(filterKeyWordsInfoValidator.getId());
        filterKeyWordsInfoValidator.setCreatedTime(indoData.getData().getCreatedTime());
        filterKeyWordsInfoValidator.setUpdatedBy(user.getRealName());
        filterKeyWordsInfoValidator.setUpdatedTime(new Date());

        //记录日志
        log.info("[分类关键词库管理][{}][{}]数据:{}", "edit", user.getUserName(), JSON.toJSONString(filterKeyWordsInfoValidator));

        //保存操作
        ResponseData data = keywordsService.save(filterKeyWordsInfoValidator, "edit");
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.setView(new RedirectView("/filter/keywords/classify/"+classify+"/list/"+code, true, false));
        return view;
    }

    /**
     * 删除
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value = "/classify/deleteById/{id}", method = RequestMethod.GET)
    public ModelAndView deleteById(@PathVariable String id, HttpServletRequest request) {
        SecurityUser user = (SecurityUser)request.getSession().getAttribute("user");
        ModelAndView view = new ModelAndView("filter/keywords/keyword_classify_edit");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询数据
        ResponseData<FilterKeyWordsInfoValidator> keyData = keywordsService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(keyData.getCode())) {
            view.addObject("error", keyData.getCode() + ":" + keyData.getMessage());
            return view;
        }

        //记录日志
        log.info("[分类关键词库管理][delete][{}]数据::{}", user.getUserName(), JSON.toJSONString(keyData.getData()));

        //删除操作
        ResponseData data = keywordsService.deleteById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.setView(new RedirectView("/filter/keywords/classify/"+keyData.getData().getKeyWordsBusinessType()+"/list/"+keyData.getData().getBusinessId(), true, false));
        return view;

    }

    @RequestMapping(value = "/classify/{classify}/upFilesView/{code}", method = RequestMethod.GET)
    public ModelAndView upFilesView(@PathVariable String classify,@PathVariable String code,HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/keywords/keyword_classify_upfiles_view");

        FilterKeyWordsInfoValidator filterKeyWordsInfoValidator = new FilterKeyWordsInfoValidator();
        filterKeyWordsInfoValidator.setKeyWordsBusinessType(classify);
        filterKeyWordsInfoValidator.setBusinessId(code);

        view.addObject("filterKeyWordsInfoValidator", filterKeyWordsInfoValidator);

        view.addObject("classify", classify);
        view.addObject("code", code);
        view.addObject("dictValueMap", dictMap(request));

        return view;
    }



    /**
     *  关键字导入
     * @param request
     * @return
     */
    @RequestMapping(value = "/classify/upFiles", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute FilterKeyWordsInfoValidator filterKeyWordsInfoValidator, BindingResult result, HttpServletRequest request) {
        SecurityUser user = (SecurityUser)request.getSession().getAttribute("user");

        ModelAndView view = new ModelAndView("filter/keywords/keyword_classify_upfiles_view");

        if(StringUtils.isEmpty(filterKeyWordsInfoValidator.getKeyWordsType())){
            FieldError err = new FieldError("关键词类型", "keyWordsType", "关键词类型不能为空");
            result.addError(err);
        }
        //完成参数规则验证
        if (result.hasErrors()) {
            view.addObject("classify", filterKeyWordsInfoValidator.getKeyWordsBusinessType());
            view.addObject("code", filterKeyWordsInfoValidator.getBusinessId());
            view.addObject("filterKeyWordsInfoValidator", filterKeyWordsInfoValidator);
            view.addObject("dictValueMap", dictMap(request));
            return view;
        }

        /**
         * 获取文件信息
         */
        MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
        List<MultipartFile> file = mRequest.getFiles("file[]");
        if(!StringUtils.isEmpty(file) && file.size()>0){

            List<ExcelModel> list = FileUtils.readFile(file.get(0),"2");

            //批量保存
            if(!StringUtils.isEmpty(list) && list.size()>0){
                FilterKeyWordsInfoValidator validator = new FilterKeyWordsInfoValidator();
                validator.setKeyWordsBusinessType(filterKeyWordsInfoValidator.getKeyWordsBusinessType());
                validator.setBusinessId(filterKeyWordsInfoValidator.getBusinessId());
                validator.setKeyWordsType(filterKeyWordsInfoValidator.getKeyWordsType());
                validator.setExccelList(list);
                validator.setCreatedBy(user.getRealName());
                ResponseData data  = keywordsService.expBatchSave(validator);
                if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
                    view.addObject("error", data.getCode() + ":" + data.getMessage());
                    return view;
                }
            }

            log.info("[分类关键词管理][导入][{}]数据::{}", user.getUserName(), list.size());
        }

        view.setView(new RedirectView("/filter/keywords/classify/"+filterKeyWordsInfoValidator.getKeyWordsBusinessType()+"/list/"+filterKeyWordsInfoValidator.getBusinessId(), true, false));

        return view;
    }

    /**
     * 取字典数据，对关键词进行分类
     */
    private Map<String,String> dictMap(HttpServletRequest request) {
        Map<String, DictType> dictMap = (Map<String, DictType>) request.getServletContext().getAttribute("dict");
        //运营商
        DictType carrier = dictMap.get("carrier");
        //行业分类
        DictType industryType = dictMap.get("industryType");
        //信息分类
        DictType infoType = dictMap.get("infoType");

        Map<String,String> dictValueMap = new HashMap<>();
        for (Dict dict : carrier.getDict()) {
            dictValueMap.put(dict.getFieldCode(),dict.getFieldName());
        }
        for (Dict dict : industryType.getDict()) {
            dictValueMap.put(dict.getFieldCode(),dict.getFieldName());
        }
        for (Dict dict : infoType.getDict()) {
            dictValueMap.put(dict.getFieldCode(),dict.getFieldName());
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
        root.setText("分类关键词");

        Map<String, DictType> dictMap = (Map<String, DictType>) request.getServletContext().getAttribute("dict");

        //运营商
        DictType carrier = dictMap.get("carrier");
        Nodes carrierNode = new Nodes();
        carrierNode.setId("1");
        carrierNode.setHref("0");
        carrierNode.setLazyLoad(false);
        carrierNode.setSvcType("CLASSIFY");
        carrierNode.setText("运营商");
        List<Nodes> carrierNodes = new ArrayList<>();
        for (Dict dict : carrier.getDict()) {
            Nodes dictNode = new Nodes();
            dictNode.setId(dict.getFieldCode());
            dictNode.setHref(dict.getFieldCode());
            dictNode.setLazyLoad(false);
            dictNode.setSvcType("leaf");
            dictNode.setOrgCode("CARRIER");
            dictNode.setText(dict.getFieldName());
            dictNode.setIcon(carrier.getIcon());
            if("CMCC".equals(dict.getFieldCode())){
                Map<String, Object> stateMap = new HashMap<String, Object>();
                stateMap.put("selected", true);
                dictNode.setState(stateMap);
            }
            carrierNodes.add(dictNode);
        }
        carrierNode.setNodes(carrierNodes);


        //行业分类
        DictType industryType = dictMap.get("industryType");
        Nodes industryTypeNode = new Nodes();
        industryTypeNode.setId("1");
        industryTypeNode.setHref("0");
        industryTypeNode.setLazyLoad(false);
        industryTypeNode.setSvcType("CLASSIFY");
        industryTypeNode.setText("行业分类");
        List<Nodes> industryTypeNodes = new ArrayList<>();
        for (Dict dict : industryType.getDict()) {
            Nodes dictNode = new Nodes();
            dictNode.setId(dict.getFieldCode());
            dictNode.setHref(dict.getFieldCode());
            dictNode.setLazyLoad(false);
            dictNode.setSvcType("leaf");
            dictNode.setOrgCode("INDUSTRY_TYPE");
            dictNode.setText(dict.getFieldName());
            dictNode.setIcon(industryType.getIcon());
            industryTypeNodes.add(dictNode);
        }
        industryTypeNode.setNodes(industryTypeNodes);

        //信息分类
        DictType infoType = dictMap.get("infoType");
        Nodes infoTypeNode = new Nodes();
        infoTypeNode.setId("1");
        infoTypeNode.setHref("0");
        infoTypeNode.setLazyLoad(false);
        infoTypeNode.setSvcType("CLASSIFY");
        infoTypeNode.setText("信息分类");
        List<Nodes> infoTypeNodes = new ArrayList<>();
        for (Dict dict : infoType.getDict()) {
            Nodes dictNode = new Nodes();
            dictNode.setId(dict.getFieldCode());
            dictNode.setHref(dict.getFieldCode());
            dictNode.setLazyLoad(false);
            dictNode.setSvcType("leaf");
            dictNode.setOrgCode("INFO_TYPE");
            dictNode.setText(dict.getFieldName());
            dictNode.setIcon(infoType.getIcon());
            infoTypeNodes.add(dictNode);
        }
        infoTypeNode.setNodes(infoTypeNodes);

        List<Nodes> rootList = new ArrayList<>();
        rootList.add(carrierNode);
        rootList.add(industryTypeNode);
        rootList.add(infoTypeNode);
        root.setNodes(rootList);

        List<Nodes> list = new ArrayList<>();
        list.add(root);

        return list;
    }
}
