package com.smoc.cloud.customer.controller;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.security.remote.service.SysUserService;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.qo.Users;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBasicInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.customer.service.EnterpriseService;
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
import java.util.List;


@Slf4j
@Controller
@RequestMapping("/enterprise")
public class EnterpriseController {

    @Autowired
    private EnterpriseService enterpriseService;

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

        view.addObject("enterpriseBasicInfoValidator", enterpriseBasicInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

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

        view.addObject("enterpriseBasicInfoValidator", enterpriseBasicInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
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
        if ("0000".equals(parentId)) {
            enterpriseBasicInfoValidator.setEnterpriseProcess("1000");//配置进度
        } else {
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
        view.addObject("salesList", findSalesList());

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
        view.addObject("salesList", findSalesList());

        //修改:查询数据
        ResponseData<EnterpriseBasicInfoValidator> data = enterpriseService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
        }

        if ("0000".equals(data.getData().getEnterpriseParentId())) {
            level = "1";
        } else {
            level = "2";
        }

        view.addObject("level", level);
        view.addObject("enterpriseBasicInfoValidator", data.getData());
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
            view.addObject("salesList", findSalesList());
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

        //保存数据
        ResponseData data = enterpriseService.save(enterpriseBasicInfoValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("ENTERPRISE_BASE", enterpriseBasicInfoValidator.getEnterpriseId(), "add".equals(op) ? enterpriseBasicInfoValidator.getCreatedBy() : enterpriseBasicInfoValidator.getUpdatedBy(), op, "add".equals(op) ? "添加企业开户" : "修改企业开户", JSON.toJSONString(enterpriseBasicInfoValidator));
        }

        //记录日志
        log.info("[企业接入][企业开户信息][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(enterpriseBasicInfoValidator));

        view.setView(new RedirectView("/enterprise/center/0000", true, false));
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

        //查询数据
        PageParams params = new PageParams<>();
        params.setPages(3);
        params.setPageSize(10);
        params.setStartRow(1);
        params.setEndRow(10);
        params.setCurrentPage(1);
        params.setTotalRows(22);

        view.addObject("pageParams", params);

        if ("0000".equals(id)) {
            view.addObject("parentId", "0");
        } else {
            view.addObject("parentId", "1");
        }
        return view;

    }


    //查询销售人员
    private List<Users> findSalesList() {
        PageParams<Users> params = new PageParams<Users>();
        params.setPageSize(100);
        params.setCurrentPage(1);
        Users u = new Users();
        u.setActive(1);
        u.setType(3);
        params.setParams(u);
        PageList<Users> list = sysUserService.page(params);
        return list.getList();
    }

}
