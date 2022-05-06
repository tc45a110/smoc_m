package com.smoc.cloud.intelligence.controller;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.qo.Nodes;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBasicInfoValidator;
import com.smoc.cloud.common.smoc.intelligence.IntellectMaterialTypeValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.customer.service.BusinessAccountService;
import com.smoc.cloud.customer.service.EnterpriseService;
import com.smoc.cloud.intelligence.service.IntellectMaterialTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 智能短信素材分类
 */
@Slf4j
@RestController
@RequestMapping("intel/resource/type")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class IntelligenceTypeController {

    @Autowired
    private EnterpriseService enterpriseService;

    @Autowired
    private BusinessAccountService businessAccountService;

    @Autowired
    private SystemUserLogService systemUserLogService;

    @Autowired
    private IntellectMaterialTypeService intellectMaterialTypeService;

    /**
     * 素材分类
     *
     * @return
     */
    @RequestMapping(value = "/resourceMain", method = RequestMethod.GET)
    public ModelAndView menuMain() {

        ModelAndView view = new ModelAndView("intelligence/resource/resource_main");

        return view;
    }

    /**
     * 加载 开通智能短信账号的企业
     *
     * @return
     */
    @RequestMapping(value = "/getEnterprise", method = RequestMethod.GET)
    public List<Nodes> getEnterprise() {

        ResponseData<List<Nodes>> nodes = enterpriseService.findByAccountBusinessType("INTELLECT_SMS");
        //log.info("[enterprise]:{}", new Gson().toJson(nodes));
        return nodes.getData();
    }

    /**
     * 加载 开通智能短信账号的企业
     *
     * @return
     */
    @RequestMapping(value = "/getAccount/{enterpriseId}", method = RequestMethod.GET)
    public List<Nodes> getAccount(@PathVariable String enterpriseId) {

        List<Nodes> result = new ArrayList<>();
        ResponseData<List<AccountBasicInfoValidator>> accountData = businessAccountService.findBusinessAccountByEnterpriseIdAndBusinessType(enterpriseId, "INTELLECT_SMS");
        if (!ResponseCode.SUCCESS.getCode().equals(accountData.getCode())) {
            return result;
        }
        if (null != accountData.getData() && accountData.getData().size() > 0) {
            for (AccountBasicInfoValidator validator : accountData.getData()) {
                Nodes node = new Nodes();
                node.setId(validator.getAccountId());
                node.setText(validator.getAccountId());
                node.setHref(enterpriseId);
                node.setSvcType("leaf");
                node.setOrgCode("ACCOUNT");
                node.setLazyLoad(true);
                result.add(node);
            }
        }
        //log.info("[account]:{}", new Gson().toJson(result));
        return result;
    }

    /**
     * 根据业务账号查询素材分类（JSON）
     *
     * @return
     */
    @RequestMapping(value = "/getResourceType/{parentId}", method = RequestMethod.GET)
    public List<Nodes> getResourceType(@PathVariable String parentId) {

        List<Nodes> result = new ArrayList<>();
        ResponseData<List<IntellectMaterialTypeValidator>> resourceTypeData = intellectMaterialTypeService.findIntellectMaterialTypeByParentIdAndStatus(parentId);
        if (!ResponseCode.SUCCESS.getCode().equals(resourceTypeData.getCode())) {
            return result;
        }
        if (null != resourceTypeData.getData() && resourceTypeData.getData().size() > 0) {
            for (IntellectMaterialTypeValidator validator : resourceTypeData.getData()) {
                Nodes node = new Nodes();
                node.setId(validator.getId());
                node.setText(validator.getTitle());
                node.setHref(validator.getParentId());
                node.setSvcType("leaf");
                node.setOrgCode("RESOURCE_TYPE");
                node.setLazyLoad(false);
                result.add(node);
            }
        }
        //log.info("[account]:{}", new Gson().toJson(result));
        return result;
    }

    /**
     * 根据业务账号查询素材分类（页面）
     *
     * @return
     */
    @RequestMapping(value = "/page/getResourceType/{parentId}", method = RequestMethod.GET)
    public ModelAndView getResourceTypePage(@PathVariable String parentId) {

        ResponseData<AccountBasicInfoValidator> accountData = businessAccountService.findById(parentId);

        ResponseData<EnterpriseBasicInfoValidator> enterpriseData = enterpriseService.findById(accountData.getData().getEnterpriseId());

        ModelAndView view = new ModelAndView("intelligence/resource/resource_type_list");
        ResponseData<List<IntellectMaterialTypeValidator>> resourceTypeData = intellectMaterialTypeService.findIntellectMaterialTypeByParentIdAndStatus(parentId);
        if (!ResponseCode.SUCCESS.getCode().equals(resourceTypeData.getCode())) {
            view.addObject("error", resourceTypeData.getCode() + ":" + resourceTypeData.getMessage());
            return view;
        }

        view.addObject("enterpriseName", enterpriseData.getData().getEnterpriseName());
        view.addObject("enterpriseId", enterpriseData.getData().getEnterpriseId());
        view.addObject("accountName", accountData.getData().getAccountName());
        view.addObject("accountId", accountData.getData().getAccountId());
        view.addObject("list", resourceTypeData.getData());
        return view;
    }

    /**
     * 新建素材类别
     *
     * @return
     */
    @RequestMapping(value = "/add/{enterpriseId}/{parentId}", method = RequestMethod.GET)
    public ModelAndView add(@PathVariable String enterpriseId, @PathVariable String parentId) {
        ModelAndView view = new ModelAndView("intelligence/resource/resource_type_edit");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(parentId);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        ResponseData<EnterpriseBasicInfoValidator> enterpriseData = enterpriseService.findById(enterpriseId);

        //初始化参数
        IntellectMaterialTypeValidator intellectMaterialTypeValidator = new IntellectMaterialTypeValidator();
        intellectMaterialTypeValidator.setId(UUID.uuid32());
        intellectMaterialTypeValidator.setParentId(parentId);
        intellectMaterialTypeValidator.setEnterpriseId(enterpriseId);
        intellectMaterialTypeValidator.setDisplaySort("10000");
        intellectMaterialTypeValidator.setStatus("1");

        view.addObject("enterpriseName", enterpriseData.getData().getEnterpriseName());
        view.addObject("enterpriseId", enterpriseData.getData().getEnterpriseId());
        view.addObject("accountId", parentId);
        view.addObject("intellectMaterialTypeValidator", intellectMaterialTypeValidator);
        view.addObject("op", "add");

        return view;
    }

    /**
     * 保存
     *
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated IntellectMaterialTypeValidator intellectMaterialTypeValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("intelligence/resource/resource_type_edit");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        if (result.hasErrors()) {
//            view.addObject("enterpriseName", enterpriseData.getData().getEnterpriseName());
//            view.addObject("enterpriseId", enterpriseData.getData().getEnterpriseId());
//            view.addObject("accountId", parentId);
            view.addObject("intellectMaterialTypeValidator", intellectMaterialTypeValidator);
            view.addObject("op", op);
            return view;
        }

        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            intellectMaterialTypeValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));
            intellectMaterialTypeValidator.setCreatedBy(user.getRealName());
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            intellectMaterialTypeValidator.setUpdatedTime(new Date());
            intellectMaterialTypeValidator.setUpdatedBy(user.getRealName());
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }


        //保存数据
        ResponseData data = intellectMaterialTypeService.save(intellectMaterialTypeValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("RESOURCE_TYPE", intellectMaterialTypeValidator.getId(), "add".equals(op) ? intellectMaterialTypeValidator.getCreatedBy() : intellectMaterialTypeValidator.getUpdatedBy(), op, "add".equals(op) ? "添加企业开户" : "修改企业开户", JSON.toJSONString(intellectMaterialTypeValidator));
        }

        //记录日志
        log.info("[智能短信][素材类别][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(intellectMaterialTypeValidator));

        view.setView(new RedirectView("/intel/resource/type/page/getResourceType/" + intellectMaterialTypeValidator.getParentId(), true, false));
        return view;

    }

    /**
     * 修改素材类别
     *
     * @return
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String id) {
        ModelAndView view = new ModelAndView("intelligence/resource/resource_type_edit");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        ResponseData<IntellectMaterialTypeValidator> materialTypeData = intellectMaterialTypeService.findById(id);

        if (!ResponseCode.SUCCESS.getCode().equals(materialTypeData.getCode())) {
            view.addObject("error", materialTypeData.getCode() + ":" + materialTypeData.getMessage());
            return view;
        }

        ResponseData<EnterpriseBasicInfoValidator> enterpriseData = enterpriseService.findById(materialTypeData.getData().getEnterpriseId());

        view.addObject("enterpriseName", enterpriseData.getData().getEnterpriseName());
        view.addObject("enterpriseId", enterpriseData.getData().getEnterpriseId());
        view.addObject("accountId", materialTypeData.getData().getParentId());
        view.addObject("intellectMaterialTypeValidator", materialTypeData.getData());
        view.addObject("op", "edit");

        return view;
    }

    /**
     * 修改素材类别
     *
     * @return
     */
    @RequestMapping(value = "/cancel/{id}", method = RequestMethod.GET)
    public ModelAndView cancel(@PathVariable String id) {
        ModelAndView view = new ModelAndView("intelligence/resource/resource_type_list");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        ResponseData<IntellectMaterialTypeValidator> materialTypeData = intellectMaterialTypeService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(materialTypeData.getCode())) {
            view.addObject("error", materialTypeData.getCode() + ":" + materialTypeData.getMessage());
            return view;
        }
        String status = "0";
        if ("0".equals(materialTypeData.getData().getStatus())) {
            status = "1";
        }
        ResponseData cancelData = intellectMaterialTypeService.cancel(id, status);
        if (!ResponseCode.SUCCESS.getCode().equals(cancelData.getCode())) {
            view.addObject("error", cancelData.getCode() + ":" + cancelData.getMessage());
            return view;
        }

        view.setView(new RedirectView("/intel/resource/type/page/getResourceType/" + materialTypeData.getData().getParentId(), true, false));
        return view;
    }


}
