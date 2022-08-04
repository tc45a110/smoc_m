package com.smoc.cloud.iot.packages.controller;

import com.smoc.cloud.common.iot.reponse.IotAccountPackageInfoMonthly;
import com.smoc.cloud.common.iot.request.AccountPackageMonthPageRequest;
import com.smoc.cloud.common.iot.validator.IotPackageInfoValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.iot.packages.service.IotPackageInfoService;
import com.smoc.cloud.iot.packages.service.PackageFlowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping("/package/flow")
public class PackageFlowController {

    @Autowired
    private PackageFlowService packageFlowService;

    @Autowired
    private IotPackageInfoService iotPackageInfoService;

    /**
     * 列表
     *
     * @return
     */
    @RequestMapping(value = "/monthly/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView("package/package_flow_list");
//        String lastMonth = DateTimeUtils.getLastMonth();
        //初始化数据
        PageParams<AccountPackageMonthPageRequest> params = new PageParams<>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        AccountPackageMonthPageRequest validator = new AccountPackageMonthPageRequest();
//        validator.setQueryMonth(lastMonth);
        params.setParams(validator);

        //查询
        ResponseData<PageList<IotAccountPackageInfoMonthly>> data = packageFlowService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //初始化套餐信息
        PageParams<IotPackageInfoValidator> packageParams = new PageParams<>();
        packageParams.setPageSize(10000);
        packageParams.setCurrentPage(1);
        IotPackageInfoValidator packageInfoValidator = new IotPackageInfoValidator();
        packageParams.setParams(packageInfoValidator);

        //查询
        ResponseData<PageList<IotPackageInfoValidator>> pageListResponseData = iotPackageInfoService.page(packageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(pageListResponseData.getCode())) {
            view.addObject("error", pageListResponseData.getCode() + ":" + pageListResponseData.getMessage());
            return view;
        }

        view.addObject("packages", pageListResponseData.getData().getList());
        view.addObject("validator", validator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;

    }

    /**
     * 分页查询
     *
     * @return
     */
    @RequestMapping(value = "/monthly/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute AccountPackageMonthPageRequest validator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("package/package_flow_list");

        //初始化数据
        pageParams.setParams(validator);

        //查询
        ResponseData<PageList<IotAccountPackageInfoMonthly>> data = packageFlowService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }


        //初始化套餐信息
        PageParams<IotPackageInfoValidator> packageParams = new PageParams<>();
        packageParams.setPageSize(10000);
        packageParams.setCurrentPage(1);
        IotPackageInfoValidator packageInfoValidator = new IotPackageInfoValidator();
        packageParams.setParams(packageInfoValidator);

        //查询
        ResponseData<PageList<IotPackageInfoValidator>> pageListResponseData = iotPackageInfoService.page(packageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(pageListResponseData.getCode())) {
            view.addObject("error", pageListResponseData.getCode() + ":" + pageListResponseData.getMessage());
            return view;
        }

        view.addObject("packages", pageListResponseData.getData().getList());
        view.addObject("validator", validator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;

    }
}
