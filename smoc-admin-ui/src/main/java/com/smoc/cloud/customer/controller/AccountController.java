package com.smoc.cloud.customer.controller;


import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.AccountBaseInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBasicInfoValidator;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.customer.service.EnterpriseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private EnterpriseService enterpriseService;

    /**
     * 客户通道账号列表
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView("customer/account/account_list");

        //查询数据
        PageParams params = new PageParams<>();
        params.setPages(3);
        params.setPageSize(10);
        params.setStartRow(1);
        params.setEndRow(10);
        params.setCurrentPage(1);
        params.setTotalRows(22);

        view.addObject("pageParams",params);
        return view;

    }

    /**
     * 客户通道账号列表
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page() {
        ModelAndView view = new ModelAndView("customer/account/account_list");

        //查询数据
        PageParams params = new PageParams<>();
        params.setPages(3);
        params.setPageSize(10);
        params.setStartRow(1);
        params.setEndRow(10);
        params.setCurrentPage(1);
        params.setTotalRows(22);

        view.addObject("pageParams",params);
        return view;

    }

    /**
     * 查询EC列表
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

        view.addObject("pageParams",params);
        return view;
    }



    /**
     * EC业务账号配置中心
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

        view.addObject("flag", flag);
        view.addObject("accountId", accountId);

        return view;

    }

    /**
     * 业务账号基本信息
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
        if("base".equals(flag)){
            AccountBaseInfoValidator accountBaseInfoValidator = new AccountBaseInfoValidator();
            accountBaseInfoValidator.setEnterpriseId(accountId);
            accountBaseInfoValidator.setAccountStauts("1");
            accountBaseInfoValidator.setAccountProcess("10000");

            //查询企业数据
            ResponseData<EnterpriseBasicInfoValidator> data = enterpriseService.findById(accountId);
            if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
                view.addObject("error", data.getCode() + ":" + data.getMessage());
            }

            //op操作标记，add表示添加，edit表示修改
            view.addObject("op", "add");
            view.addObject("accountBaseInfoValidator", accountBaseInfoValidator);
            view.addObject("enterpriseBasicInfoValidator", data.getData());
            return view;
        }

        /**
         * 修改:查询数据
         */
        view.addObject("op", "edit");
        view.addObject("accountBaseInfoValidator", new AccountBaseInfoValidator());
        view.addObject("enterpriseBasicInfoValidator", new EnterpriseBasicInfoValidator());

        return view;

    }

    /**
     * 业务账号财务配置
     * @return
     */
    @RequestMapping(value = "/edit/finance/{accountId}", method = RequestMethod.GET)
    public ModelAndView edit_finance(@PathVariable String accountId, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_edit_finance");
        return view;

    }

    /**
     * 业务账号接口配置
     * @return
     */
    @RequestMapping(value = "/edit/interface/{accountId}", method = RequestMethod.GET)
    public ModelAndView edit_interface(@PathVariable String accountId, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_edit_interface");
        return view;

    }

    /**
     * 业务账号产品配置
     * @return
     */
    @RequestMapping(value = "/edit/product/{accountId}", method = RequestMethod.GET)
    public ModelAndView edit_product(@PathVariable String accountId, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_edit_channel_group");
        return view;

    }
    /**
     * 业务账号通道配置
     * @return
     */
    @RequestMapping(value = "/edit/channel/{accountId}", method = RequestMethod.GET)
    public ModelAndView edit_channel(@PathVariable String accountId, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_edit_channel");
        return view;

    }


    /**
     * EC业务账号中心
     * @return
     */
    @RequestMapping(value = "/center/{enterpriseId}", method = RequestMethod.GET)
    public ModelAndView center(@PathVariable String enterpriseId, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_account_center");

        return view;

    }

    /**
     * EC业务账号查看中心
     * @return
     */
    @RequestMapping(value = "/view/center/{accountId}", method = RequestMethod.GET)
    public ModelAndView view(@PathVariable String accountId, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_view_center");

        return view;

    }

    /**
     * EC业务账号查看中心
     * @return
     */
    @RequestMapping(value = "/view/base/{accountId}", method = RequestMethod.GET)
    public ModelAndView view_base(@PathVariable String accountId, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_view_base");

        return view;

    }

    /**
     * EC业务账号通道明细
     * @return
     */
    @RequestMapping(value = "/view/channel/detail/{accountId}", method = RequestMethod.GET)
    public ModelAndView account_channel(@PathVariable String accountId, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_view_channel_detail");

        return view;

    }

}
