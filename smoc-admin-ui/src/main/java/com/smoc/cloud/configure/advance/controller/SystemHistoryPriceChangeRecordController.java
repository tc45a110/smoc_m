package com.smoc.cloud.configure.advance.controller;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.validator.SystemHistoryPriceChangeRecordValidator;
import com.smoc.cloud.configure.advance.service.SystemHistoryPriceChangeRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Slf4j
@Controller
@RequestMapping("/configure/price/history")
public class SystemHistoryPriceChangeRecordController {

    @Autowired
    private SystemHistoryPriceChangeRecordService systemHistoryPriceChangeRecordService;

    /**
     * 列表
     *
     * @return
     */
    @RequestMapping(value = "/list/{changeType}", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable String changeType) {
        ModelAndView view = new ModelAndView("configure/advance/history_price_change_list");

        ///初始化数据
        PageParams<SystemHistoryPriceChangeRecordValidator> params = new PageParams<>();
        params.setPageSize(8);
        params.setCurrentPage(1);
        SystemHistoryPriceChangeRecordValidator systemHistoryPriceChangeRecordValidator = new SystemHistoryPriceChangeRecordValidator();
        systemHistoryPriceChangeRecordValidator.setChangeType(changeType);
        params.setParams(systemHistoryPriceChangeRecordValidator);

        //查询
        ResponseData<PageList<SystemHistoryPriceChangeRecordValidator>> data = systemHistoryPriceChangeRecordService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("systemHistoryPriceChangeRecordValidator", systemHistoryPriceChangeRecordValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;
    }

    /**
     * 分页查询
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute SystemHistoryPriceChangeRecordValidator systemHistoryPriceChangeRecordValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("configure/advance/history_price_change_list");

        //分页查询
        pageParams.setParams(systemHistoryPriceChangeRecordValidator);

        ResponseData<PageList<SystemHistoryPriceChangeRecordValidator>> data = systemHistoryPriceChangeRecordService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("systemHistoryPriceChangeRecordValidator", systemHistoryPriceChangeRecordValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;
    }

    /**
     * 创建 新历史价格变更记录
     *
     * @return
     */
    @RequestMapping(value = "/center/{changeType}/{businessId}", method = RequestMethod.GET)
    public ModelAndView center(@PathVariable String changeType,@PathVariable String businessId) {
        ModelAndView view = new ModelAndView("configure/advance/history_price_change_center");

        view.addObject("changeType",changeType);
        view.addObject("businessId",businessId);
        return view;
    }

    /**
     * 创建 新历史价格变更记录
     *
     * @return
     */
    @RequestMapping(value = "/add/{changeType}/{businessId}", method = RequestMethod.GET)
    public ModelAndView add(@PathVariable String changeType,@PathVariable String businessId) {
        ModelAndView view = new ModelAndView("configure/advance/history_price_change_edit");

        view.addObject("changeType",changeType);
        view.addObject("businessId",businessId);
        return view;
    }
}
