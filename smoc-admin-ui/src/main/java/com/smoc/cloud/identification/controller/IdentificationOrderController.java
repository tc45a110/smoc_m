package com.smoc.cloud.identification.controller;

import com.google.gson.Gson;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.validator.CodeNumberInfoValidator;
import com.smoc.cloud.common.smoc.identification.validator.IdentificationAccountInfoValidator;
import com.smoc.cloud.common.smoc.identification.validator.IdentificationOrdersInfoValidator;
import com.smoc.cloud.common.smoc.identification.validator.IdentificationRequestDataValidator;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.identification.service.IdentificationOrdersInfoService;
import com.smoc.cloud.identification.service.IdentificationRequestDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 身份认证订单
 */
@Slf4j
@Controller
@RequestMapping("/identification/order")
public class IdentificationOrderController {

    @Autowired
    private IdentificationOrdersInfoService identificationOrdersInfoService;

    @Autowired
    private IdentificationRequestDataService identificationRequestDataService;

    /**
     * 认账订单列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView("identification/orders/identification_orders_list");

        //初始化数据
        PageParams<IdentificationOrdersInfoValidator> params = new PageParams<IdentificationOrdersInfoValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        IdentificationOrdersInfoValidator identificationOrdersInfoValidator = new IdentificationOrdersInfoValidator();
        params.setParams(identificationOrdersInfoValidator);

        //查询
        ResponseData<PageList<IdentificationOrdersInfoValidator>> data = identificationOrdersInfoService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("identificationOrdersInfoValidator", identificationOrdersInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;

    }

    /**
     * 认账订单列表页
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute IdentificationOrdersInfoValidator identificationOrdersInfoValidator, PageParams pageParams) {

        ModelAndView view = new ModelAndView("identification/orders/identification_orders_list");
        //分页查询
        pageParams.setParams(identificationOrdersInfoValidator);

        ResponseData<PageList<IdentificationOrdersInfoValidator>> data = identificationOrdersInfoService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("identificationOrdersInfoValidator", identificationOrdersInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;
    }

    /**
     * 账号消费记录
     *
     * @return
     */
    @RequestMapping(value = "/list/record/{accountId}", method = RequestMethod.GET)
    public ModelAndView recordList(@PathVariable String accountId) {
        ModelAndView view = new ModelAndView("identification/orders/identification_orders_list_record");

        //初始化数据
        PageParams<IdentificationOrdersInfoValidator> params = new PageParams<IdentificationOrdersInfoValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        IdentificationOrdersInfoValidator identificationOrdersInfoValidator = new IdentificationOrdersInfoValidator();
        identificationOrdersInfoValidator.setIdentificationAccount(accountId);
        params.setParams(identificationOrdersInfoValidator);

        //查询
        ResponseData<PageList<IdentificationOrdersInfoValidator>> data = identificationOrdersInfoService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("identificationOrdersInfoValidator", identificationOrdersInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;

    }

    /**
     * 认账订单列表页
     *
     * @return
     */
    @RequestMapping(value = "/page/record", method = RequestMethod.POST)
    public ModelAndView pageRecord(@ModelAttribute IdentificationOrdersInfoValidator identificationOrdersInfoValidator, PageParams pageParams) {

        ModelAndView view = new ModelAndView("identification/orders/identification_orders_list_record");
        //分页查询
        pageParams.setParams(identificationOrdersInfoValidator);

        ResponseData<PageList<IdentificationOrdersInfoValidator>> data = identificationOrdersInfoService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("identificationOrdersInfoValidator", identificationOrdersInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;
    }

    /**
     * 查看请求订单数据
     *
     * @return
     */
    @RequestMapping(value = "/view/{orderNo}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String orderNo, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("identification/orders/identification_orders_view");

        //修改:查询数据
        ResponseData<IdentificationRequestDataValidator> data = identificationRequestDataService.findByOrderNo(orderNo);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
        }

        view.addObject("jsonData",new Gson().toJson(data.getData()));

        return view;

    }
}
