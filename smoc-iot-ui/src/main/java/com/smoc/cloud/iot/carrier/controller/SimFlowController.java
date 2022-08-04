package com.smoc.cloud.iot.carrier.controller;

import com.smoc.cloud.common.iot.reponse.SimFlowUsedMonthlyResponse;
import com.smoc.cloud.common.iot.request.SimsFlowMonthlyRequest;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.iot.carrier.service.SimFlowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
@RequestMapping("/sim/flow")
public class SimFlowController {

    @Autowired
    private SimFlowService simFlowService;

    /**
     * 列表
     *
     * @return
     */
    @RequestMapping(value = "/monthly/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView("carrier/sim_flow_list");

        //初始化数据
        PageParams<SimsFlowMonthlyRequest> params = new PageParams<>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        SimsFlowMonthlyRequest validator = new SimsFlowMonthlyRequest();
        params.setParams(validator);

        //查询
        ResponseData<PageList<SimFlowUsedMonthlyResponse>> data = simFlowService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

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
    public ModelAndView page(@ModelAttribute SimsFlowMonthlyRequest validator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("carrier/sim_flow_list");

        //初始化数据
        pageParams.setParams(validator);

        //查询
        ResponseData<PageList<SimFlowUsedMonthlyResponse>> data = simFlowService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("validator", validator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;

    }
}
