package com.smoc.cloud.configure.filters;

import com.smoc.cloud.common.auth.qo.DictType;
import com.smoc.cloud.common.filters.utils.InitializeFiltersData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 过滤数据初始化
 **/
@Slf4j
@Controller
@RequestMapping("/initialize/filters")
public class InitializeFiltersDataController {

    @Autowired
    private InitializeFiltersDataService initializeFiltersDataService;

    /**
     * 进入加载过滤参数页面
     *
     * @return
     */
    @RequestMapping(value = "/initialize", method = RequestMethod.GET)
    public ModelAndView list(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/filters/initialize_filters_data");

        InitializeFiltersData initializeFiltersData = new InitializeFiltersData();
        view.addObject("initialize", initializeFiltersData);

        return view;

    }

    /**
     * 重新加载过滤器数据
     *
     * @return
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public ModelAndView submit(@ModelAttribute InitializeFiltersData initializeFiltersData, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/filters/initialize_filters_data");
        Map<String, DictType> dictMap = (Map<String, DictType>) request.getServletContext().getAttribute("dict");
        //行业敏感词、行业黑白名单分类
        DictType infoType = dictMap.get("industryBlackList");
        initializeFiltersData.setInfoType(infoType);
        initializeFiltersDataService.initialize(initializeFiltersData);
        view.addObject("initialize", new InitializeFiltersData());
        return view;

    }
}
