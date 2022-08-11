package com.smoc.cloud.customer.controller;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.AccountSignRegisterForFileValidator;
import com.smoc.cloud.customer.service.AccountSignRegisterForFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@RestController
@RequestMapping("sign/register/file")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class AccountSignRegisterForFileController {

    @Autowired
    private AccountSignRegisterForFileService accountSignRegisterForFileService;

    /**
     * 查询
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView("sign/register/sign_register_file_list");

        //初始化数据
        PageParams<AccountSignRegisterForFileValidator> params = new PageParams<>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        AccountSignRegisterForFileValidator accountSignRegisterForFileValidator = new AccountSignRegisterForFileValidator();
        params.setParams(accountSignRegisterForFileValidator);

        //查询
        ResponseData<PageList<AccountSignRegisterForFileValidator>> data = accountSignRegisterForFileService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("accountSignRegisterForFileValidator", accountSignRegisterForFileValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;

    }

    /**
     * 分页
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute AccountSignRegisterForFileValidator accountSignRegisterForFileValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("sign/register/sign_register_file_list");

        //分页查询
        pageParams.setParams(accountSignRegisterForFileValidator);

        ResponseData<PageList<AccountSignRegisterForFileValidator>> data = accountSignRegisterForFileService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("accountSignRegisterForFileValidator", accountSignRegisterForFileValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;

    }
}
