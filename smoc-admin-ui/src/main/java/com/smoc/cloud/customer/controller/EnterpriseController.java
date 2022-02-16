package com.smoc.cloud.customer.controller;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.security.remote.service.SysUserService;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.qo.Users;
import com.smoc.cloud.common.auth.validator.UserValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBasicInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseExpressInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseInvoiceInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseWebAccountInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.customer.service.EnterpriseExpressService;
import com.smoc.cloud.customer.service.EnterpriseInvoiceService;
import com.smoc.cloud.customer.service.EnterpriseService;
import com.smoc.cloud.customer.service.EnterpriseWebService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Slf4j
@Controller
@RequestMapping("/enterprise")
public class EnterpriseController {

    @Autowired
    private EnterpriseService enterpriseService;

    @Autowired
    private EnterpriseWebService enterpriseWebService;

    @Autowired
    private EnterpriseExpressService enterpriseExpressService;

    @Autowired
    private EnterpriseInvoiceService enterpriseInvoiceService;

    @Autowired
    private SystemUserLogService systemUserLogService;

    @Autowired
    private SysUserService sysUserService;

    /**
     * 查询EC列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView("customer/enterprise/enterprise_list");

        //初始化数据
        PageParams<EnterpriseBasicInfoValidator> params = new PageParams<EnterpriseBasicInfoValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        EnterpriseBasicInfoValidator enterpriseBasicInfoValidator = new EnterpriseBasicInfoValidator();
        params.setParams(enterpriseBasicInfoValidator);

        //查询
        ResponseData<PageList<EnterpriseBasicInfoValidator>> data = enterpriseService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //查询销售
        List<Users> list = sysUserService.salesList();
        Map<String, Users> salesMap = new HashMap<>();
        if(!StringUtils.isEmpty(list) && list.size()>0){
            salesMap = list.stream().collect(Collectors.toMap(Users::getId, Function.identity()));
        }

        view.addObject("enterpriseBasicInfoValidator", enterpriseBasicInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("salesMap", salesMap);
        view.addObject("salesList", list);

        return view;

    }

    /**
     * 分页查询EC
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute EnterpriseBasicInfoValidator enterpriseBasicInfoValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("customer/enterprise/enterprise_list");

        //分页查询
        pageParams.setParams(enterpriseBasicInfoValidator);

        ResponseData<PageList<EnterpriseBasicInfoValidator>> data = enterpriseService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //查询销售
        List<Users> list = sysUserService.salesList();
        Map<String, Users> salesMap = new HashMap<>();
        if(!StringUtils.isEmpty(list) && list.size()>0){
            salesMap = list.stream().collect(Collectors.toMap(Users::getId, Function.identity()));
        }

        view.addObject("enterpriseBasicInfoValidator", enterpriseBasicInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("salesMap", salesMap);
        view.addObject("salesList", list);
        return view;

    }

    /**
     * 新建EC开户
     * level表示企业层级
     *
     * @return
     */
    @RequestMapping(value = "/add/{level}/{parentId}", method = RequestMethod.GET)
    public ModelAndView add(@PathVariable String level, @PathVariable String parentId) {
        ModelAndView view = new ModelAndView("customer/enterprise/enterprise_edit");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(parentId);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //初始化参数
        EnterpriseBasicInfoValidator enterpriseBasicInfoValidator = new EnterpriseBasicInfoValidator();
        enterpriseBasicInfoValidator.setEnterpriseId(UUID.uuid32());
        enterpriseBasicInfoValidator.setEnterpriseStatus("1");//初始状态
        enterpriseBasicInfoValidator.setEnterpriseParentId(parentId);//父ID
        enterpriseBasicInfoValidator.setEnterpriseProcess("10000");//配置进度
        if (!"0000".equals(parentId)) {
            //如果是二级企业，需要回显一级企业信息
            ResponseData<EnterpriseBasicInfoValidator> data = enterpriseService.findById(parentId);
            if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
                view.addObject("error", data.getCode() + ":" + data.getMessage());
            }
            enterpriseBasicInfoValidator.setParentEnterpriseName(data.getData().getEnterpriseName());
            enterpriseBasicInfoValidator.setEnterpriseType(data.getData().getEnterpriseType());
            enterpriseBasicInfoValidator.setEnterpriseProcess("1111");
            enterpriseBasicInfoValidator.setSaler(data.getData().getSaler());
            enterpriseBasicInfoValidator.setAccessCorporation(data.getData().getAccessCorporation());
        }

        //查询销售人员
        view.addObject("salesList", sysUserService.salesList());

        view.addObject("level", level);
        view.addObject("enterpriseBasicInfoValidator", enterpriseBasicInfoValidator);
        view.addObject("op", "add");

        return view;
    }

    /**
     * 编辑EC开户
     * level表示企业层级
     *
     * @return
     */
    @RequestMapping(value = "/edit/{level}/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String level, @PathVariable String id) {
        ModelAndView view = new ModelAndView("customer/enterprise/enterprise_edit");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询销售人员
        view.addObject("salesList", sysUserService.salesList());

        //修改:查询数据
        ResponseData<EnterpriseBasicInfoValidator> data = enterpriseService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
        }

        EnterpriseBasicInfoValidator enterpriseBasicInfoValidator = data.getData();

        if ("0000".equals(data.getData().getEnterpriseParentId())) {
            level = "1";
        } else {
            level = "2";
            //查询上级企业
            ResponseData<EnterpriseBasicInfoValidator> levelData = enterpriseService.findById(data.getData().getEnterpriseParentId());
            if (!ResponseCode.SUCCESS.getCode().equals(levelData.getCode())) {
                view.addObject("error", levelData.getCode() + ":" + levelData.getMessage());
            }
            enterpriseBasicInfoValidator.setParentEnterpriseName(levelData.getData().getEnterpriseName());
        }

        view.addObject("level", level);
        view.addObject("enterpriseBasicInfoValidator", enterpriseBasicInfoValidator);
        //op操作标记，add表示添加，edit表示修改
        view.addObject("op", "edit");

        return view;
    }

    /**
     * 保存企业开户
     *
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated EnterpriseBasicInfoValidator enterpriseBasicInfoValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/enterprise/enterprise_edit");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        if (result.hasErrors()) {
            //查询销售人员
            view.addObject("salesList", sysUserService.salesList());
            view.addObject("enterpriseBasicInfoValidator", enterpriseBasicInfoValidator);
            view.addObject("op", op);
            return view;
        }

        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            enterpriseBasicInfoValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));
            enterpriseBasicInfoValidator.setCreatedBy(user.getRealName());
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            enterpriseBasicInfoValidator.setUpdatedTime(new Date());
            enterpriseBasicInfoValidator.setUpdatedBy(user.getRealName());
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }

        //销售为空默认:0000
        if (StringUtils.isEmpty(enterpriseBasicInfoValidator.getSaler())) {
            enterpriseBasicInfoValidator.setSaler("0000");
        }

        //保存数据
        ResponseData data = enterpriseService.save(enterpriseBasicInfoValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("ENTERPRISE_INFO", enterpriseBasicInfoValidator.getEnterpriseId(), "add".equals(op) ? enterpriseBasicInfoValidator.getCreatedBy() : enterpriseBasicInfoValidator.getUpdatedBy(), op, "add".equals(op) ? "添加企业开户" : "修改企业开户", JSON.toJSONString(enterpriseBasicInfoValidator));
        }

        //记录日志
        log.info("[企业接入][企业开户信息][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(enterpriseBasicInfoValidator));

        view.setView(new RedirectView("/enterprise/center/" + enterpriseBasicInfoValidator.getEnterpriseId(), true, false));
        return view;

    }

    /**
     * EC开户
     * level表示企业层级
     *
     * @return
     */
    @RequestMapping(value = "/center/{id}", method = RequestMethod.GET)
    public ModelAndView config(@PathVariable String id) {
        ModelAndView view = new ModelAndView("customer/enterprise/enterprise_center");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询企业基本数据
        ResponseData<EnterpriseBasicInfoValidator> data = enterpriseService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
        }

        //查询销售
        if (!StringUtils.isEmpty(data.getData().getSaler())) {
            ResponseData<UserValidator> userValidator = sysUserService.userProfile(data.getData().getSaler());
            if (ResponseCode.SUCCESS.getCode().equals(userValidator.getCode())) {
                view.addObject("salerName", userValidator.getData().getBaseUserExtendsValidator().getRealName());
            }
        }

        //查询WEB登录账号
        findEnterpriseWebAccountInfo(view, data.getData());

        //查询邮寄信息
        findEnterpriseExpressInfo(view, data.getData());

        //查询开票信息
        findEnterpriseInvoiceInfo(view, data.getData());

        view.addObject("enterpriseBasicInfoValidator", data.getData());

        return view;

    }

    //查询邮寄信息
    private void findEnterpriseExpressInfo(ModelAndView view, EnterpriseBasicInfoValidator enterpriseBasicInfoValidator) {
        EnterpriseExpressInfoValidator enterpriseExpressInfoValidator = new EnterpriseExpressInfoValidator();
        enterpriseExpressInfoValidator.setEnterpriseId(enterpriseBasicInfoValidator.getEnterpriseId());
        ResponseData<List<EnterpriseExpressInfoValidator>> expressData = enterpriseExpressService.page(enterpriseExpressInfoValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(expressData.getCode())) {
            view.addObject("error", expressData.getCode() + ":" + expressData.getMessage());
        }
        view.addObject("enterpriseExpressInfoValidator", enterpriseExpressInfoValidator);
        view.addObject("expressList", expressData.getData());
    }

    //查询WEB登录账号
    private void findEnterpriseWebAccountInfo(ModelAndView view, EnterpriseBasicInfoValidator enterpriseBasicInfoValidator) {
        EnterpriseWebAccountInfoValidator enterpriseWebAccountInfoValidator = new EnterpriseWebAccountInfoValidator();
        enterpriseWebAccountInfoValidator.setEnterpriseId(enterpriseBasicInfoValidator.getEnterpriseId());
        ResponseData<List<EnterpriseWebAccountInfoValidator>> webData = enterpriseWebService.page(enterpriseWebAccountInfoValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(webData.getCode())) {
            view.addObject("error", webData.getCode() + ":" + webData.getMessage());
        }
        view.addObject("enterpriseWebAccountInfoValidator", enterpriseWebAccountInfoValidator);
        view.addObject("webList", webData.getData());
    }

    //查询开票信息
    private void findEnterpriseInvoiceInfo(ModelAndView view, EnterpriseBasicInfoValidator enterpriseBasicInfoValidator) {
        //普通发票
        ResponseData<EnterpriseInvoiceInfoValidator> commonData = enterpriseInvoiceService.findByEnterpriseIdAndInvoiceType(enterpriseBasicInfoValidator.getEnterpriseId(),"1");
        if (!ResponseCode.SUCCESS.getCode().equals(commonData.getCode())) {
            view.addObject("error", commonData.getCode() + ":" + commonData.getMessage());
        }
        view.addObject("commonInvoice", commonData.getData());

        //专用发票
        ResponseData<EnterpriseInvoiceInfoValidator> specialData = enterpriseInvoiceService.findByEnterpriseIdAndInvoiceType(enterpriseBasicInfoValidator.getEnterpriseId(),"2");
        if (!ResponseCode.SUCCESS.getCode().equals(specialData.getCode())) {
            view.addObject("error", specialData.getCode() + ":" + specialData.getMessage());
        }
        view.addObject("specialInvoice", specialData.getData());

        EnterpriseInvoiceInfoValidator enterpriseInvoiceInfoValidator = new EnterpriseInvoiceInfoValidator();
        enterpriseInvoiceInfoValidator.setEnterpriseId(enterpriseBasicInfoValidator.getEnterpriseId());
        view.addObject("enterpriseInvoiceInfoValidator", enterpriseInvoiceInfoValidator);
    }

    /**
     *  注销、启用企业业务
     * @param id
     * @param status
     * @param request
     * @return
     */
    @RequestMapping(value = "/forbiddenEnterprise/{id}/{status}", method = RequestMethod.GET)
    public ModelAndView forbiddenEnterprise(@PathVariable String id,@PathVariable String status, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/enterprise/enterprise_edit");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询企业信息
        ResponseData<EnterpriseBasicInfoValidator> enterpriseData = enterpriseService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(enterpriseData.getCode())) {
            view.addObject("error", enterpriseData.getCode() + ":" + enterpriseData.getMessage());
        }

        //注销、启用企业业务
        ResponseData data = enterpriseService.forbiddenEnterprise(id,status);

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("ENTERPRISE_INFO", enterpriseData.getData().getEnterpriseId(), user.getRealName(), "edit", "1".equals(status) ? "注销企业业务及WEB账号":"启用企业业务及WEB账号" , JSON.toJSONString(enterpriseData.getData()));
        }

        //记录日志
        log.info("[企业接入][{}][{}][{}]数据:{}", "1".equals(status) ? "注销企业业务及WEB账号":"启用企业业务及WEB账号","edit" , user.getUserName(), JSON.toJSONString(enterpriseData.getData()));

        view.setView(new RedirectView("/enterprise/center/"+enterpriseData.getData().getEnterpriseId(), true, false));
        return view;

    }


}
