package com.smoc.cloud.main;

import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.RSAUtils;
import com.smoc.cloud.statistic.data.service.IndexStatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * 公用系统用户（可自定义扩展）
 * 2019/5/14 11:37
 **/
@Slf4j
@RestController
public class IndexController {

    @Autowired
    private IndexStatisticsService indexStatisticsService;

    /**
     * 查看首页信息
     *
     * @return
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request) {
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");
        ModelAndView view = new ModelAndView("index");

        //营收总额、毛利总额、发送总量、客户总数、新增客户、通道总数、新增通道
        int year = DateTimeUtils.getNowYear();
        String start = year+"-01"+"-01";
        String endDate = DateTimeUtils.getDateFormat(new Date());
        ResponseData<Map<String, Object>> dataMap = indexStatisticsService.statisticsIndexData(start,endDate);

        view.addObject("countMap", dataMap.getData());
        view.addObject("endDate", endDate);
        view.addObject("year", year);

        return view;

    }

}
