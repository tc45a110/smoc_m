package com.smoc.cloud.customer.controller;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBasicInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.customer.service.BusinessAccountService;
import com.smoc.cloud.customer.service.EnterpriseService;
import com.smoc.cloud.sequence.service.SequenceService;
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

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * EC业务账号管理
 */
@Slf4j
@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private EnterpriseService enterpriseService;

    @Autowired
    private BusinessAccountService businessAccountService;

    @Autowired
    private SystemUserLogService systemUserLogService;

    @Autowired
    private SequenceService sequenceService;

    /**
     * 业务账号列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView("customer/account/account_list");

        //初始化数据
        PageParams<AccountBasicInfoValidator> params = new PageParams<AccountBasicInfoValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        AccountBasicInfoValidator accountBasicInfoValidator = new AccountBasicInfoValidator();
        params.setParams(accountBasicInfoValidator);

        //查询
        ResponseData<PageList<AccountBasicInfoValidator>> data = businessAccountService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("accountBasicInfoValidator", accountBasicInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;

    }

    /**
     * 业务账号列表查询
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute AccountBasicInfoValidator accountBasicInfoValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("customer/account/account_list");

        //分页查询
        pageParams.setParams(accountBasicInfoValidator);

        ResponseData<PageList<AccountBasicInfoValidator>> data = businessAccountService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("accountBasicInfoValidator", accountBasicInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;

    }

    /**
     * 查询EC列表
     *
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ModelAndView search() {
        ModelAndView view = new ModelAndView("customer/account/account_search");

        //查询数据
        PageParams params = new PageParams<>();
        params.setPages(3);
        params.setPageSize(10);
        params.setStartRow(1);
        params.setEndRow(10);
        params.setCurrentPage(1);
        params.setTotalRows(22);

        view.addObject("pageParams", params);
        return view;
    }


    /**
     * EC业务账号配置中心
     *
     * @return
     */
    @RequestMapping(value = "/edit/center/{flag}/{accountId}", method = RequestMethod.GET)
    public ModelAndView edit_center(@PathVariable String flag, @PathVariable String accountId, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_edit_center");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(accountId);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询账号配置的是通道组还是通道
        if(!"base".equals(flag)){
            ResponseData<AccountBasicInfoValidator> info = businessAccountService.findById(accountId);
            if (!ResponseCode.SUCCESS.getCode().equals(info.getCode())) {
                view.addObject("error", info.getCode() + ":" + info.getMessage());
                return view;
            }
            view.addObject("accountChannelType", info.getData().getAccountChannelType());
        }

        view.addObject("flag", flag);
        view.addObject("accountId", accountId);

        return view;

    }

    /**
     * 业务账号基本信息
     *
     * @return
     */
    @RequestMapping(value = "/edit/base/{flag}/{accountId}", method = RequestMethod.GET)
    public ModelAndView edit_base(@PathVariable String flag, @PathVariable String accountId, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_edit_base");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(accountId);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        /**
         * 如果flag值为base,那么accountId为企业id
         */
        if ("base".equals(flag)) {
            AccountBasicInfoValidator accountBasicInfoValidator = new AccountBasicInfoValidator();
            accountBasicInfoValidator.setEnterpriseId(accountId);
            accountBasicInfoValidator.setAccountStauts("1");
            accountBasicInfoValidator.setRandomExtendCodeLength(0);
            accountBasicInfoValidator.setAccountProcess("10000");

            //查询企业数据
            ResponseData<EnterpriseBasicInfoValidator> data = enterpriseService.findById(accountId);
            if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
                view.addObject("error", data.getCode() + ":" + data.getMessage());
            }

            //op操作标记，add表示添加，edit表示修改
            view.addObject("op", "add");
            view.addObject("accountBasicInfoValidator", accountBasicInfoValidator);
            view.addObject("enterpriseBasicInfoValidator", data.getData());
            return view;
        }

        /**
         * 修改:查询数据
         */
        ResponseData<AccountBasicInfoValidator> info = businessAccountService.findById(accountId);
        if (!ResponseCode.SUCCESS.getCode().equals(info.getCode())) {
            view.addObject("error", info.getCode() + ":" + info.getMessage());
            return view;
        }

        //查询企业数据
        ResponseData<EnterpriseBasicInfoValidator> data = enterpriseService.findById(info.getData().getEnterpriseId());
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
        }

        view.addObject("op", "edit");
        view.addObject("accountBasicInfoValidator", info.getData());
        view.addObject("enterpriseBasicInfoValidator", data.getData());

        return view;

    }

    /**
     * 业务账号基本信息提交
     *
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated AccountBasicInfoValidator accountBasicInfoValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_edit_base");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        if (result.hasErrors()) {
            view.addObject("accountBasicInfoValidator", accountBasicInfoValidator);
            view.addObject("op", op);
            return view;
        }

        String accountChannelType = "";

        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            //封装业务账号前缀
            String prefixId = sequenceService.getPrefixId("BUSINESS_ACCOUNT", accountBasicInfoValidator.getBusinessType());
            accountBasicInfoValidator.setAccountId(prefixId);
            accountBasicInfoValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));
            accountBasicInfoValidator.setCreatedBy(user.getRealName());
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            accountBasicInfoValidator.setUpdatedTime(new Date());
            accountBasicInfoValidator.setUpdatedBy(user.getRealName());
            ResponseData<AccountBasicInfoValidator> info = businessAccountService.findById(accountBasicInfoValidator.getAccountId());
            accountChannelType = info.getData().getAccountChannelType();
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }

        //保存数据
        ResponseData data = businessAccountService.save(accountBasicInfoValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("BUSINESS_ACCOUNT", accountBasicInfoValidator.getAccountId(), "add".equals(op) ? accountBasicInfoValidator.getCreatedBy() : accountBasicInfoValidator.getUpdatedBy(), op, "add".equals(op) ? "添加业务账号" : "修改业务账号", JSON.toJSONString(accountBasicInfoValidator));
        }

        //记录日志
        log.info("[EC业务账号管理][业务账号基本信息][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(accountBasicInfoValidator));

        ResponseData<AccountBasicInfoValidator> accountBaseInfo = businessAccountService.findById(accountBasicInfoValidator.getAccountId());

        //保存成功之后，重新加载页面，iframe刷新标识,只有添加或者是修改了通道方式才会刷新
        if ("add".equals(op) || (!accountBasicInfoValidator.getAccountChannelType().equals(accountChannelType))) {
            view.addObject("refreshFlag", "refreshFlag");
        }

        //查询企业数据
        ResponseData<EnterpriseBasicInfoValidator> enterpriseData = enterpriseService.findById(accountBaseInfo.getData().getEnterpriseId());
        view.addObject("enterpriseBasicInfoValidator", enterpriseData.getData());

        view.addObject("accountBasicInfoValidator", accountBaseInfo.getData());
        view.addObject("op", "edit");

        return view;
    }


    /**
     * 业务账号产品配置
     *
     * @return
     */
    @RequestMapping(value = "/edit/product/{accountId}", method = RequestMethod.GET)
    public ModelAndView edit_product(@PathVariable String accountId, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_edit_channel_group");
        return view;

    }

    /**
     * EC业务账号中心
     *
     * @return
     */
    @RequestMapping(value = "/center/{enterpriseId}", method = RequestMethod.GET)
    public ModelAndView center(@PathVariable String enterpriseId, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_account_center");

        return view;

    }

    /**
     * EC业务账号查看中心
     *
     * @return
     */
    @RequestMapping(value = "/view/center/{accountId}", method = RequestMethod.GET)
    public ModelAndView view(@PathVariable String accountId, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_view_center");

        return view;

    }

    /**
     * EC业务账号查看中心
     *
     * @return
     */
    @RequestMapping(value = "/view/base/{accountId}", method = RequestMethod.GET)
    public ModelAndView view_base(@PathVariable String accountId, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_view_base");

        return view;

    }

    /**
     * EC业务账号通道明细
     *
     * @return
     */
    @RequestMapping(value = "/view/channel/detail/{accountId}", method = RequestMethod.GET)
    public ModelAndView account_channel(@PathVariable String accountId, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_view_channel_detail");

        return view;

    }

}
