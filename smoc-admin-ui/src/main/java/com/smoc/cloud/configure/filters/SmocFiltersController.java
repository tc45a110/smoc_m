package com.smoc.cloud.configure.filters;

import com.google.gson.Gson;
import com.smoc.cloud.common.response.ResponseData;
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
@RequestMapping("/filters")
public class SmocFiltersController {

    @Autowired
    private SmocFiltersService smocFiltersService;

    /**
     *
     *
     * @return
     */
    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public ModelAndView filter(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/filters/post_filters_data");

        RequestFullParams requestFullParams = new RequestFullParams();
        requestFullParams.setAccount("WHZ119");
        requestFullParams.setPhone("13910986771");
        requestFullParams.setMessage("【招商银行】尊敬的客户，为更好保障持卡人用卡权益，并逐步关闭部分账单日，通讯详单记录建议您可拨打热线400-820-5555申请更改至新账单日com。退订回#C");
        requestFullParams.setCarrier("");
        requestFullParams.setProvinceCode("");
        requestFullParams.setTemplateId("");
        requestFullParams.setNumbers(1);
        view.addObject("requestFullParams", requestFullParams);

        return view;

    }

    /**
     *
     *
     * @return
     */
    @RequestMapping(value = "/post", method = RequestMethod.POST)
    public ModelAndView submit(@ModelAttribute RequestFullParams requestFullParams, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/filters/post_filters_data");
        ResponseData responseData = smocFiltersService.filter(requestFullParams);
        view.addObject("requestFullParams", requestFullParams);
        view.addObject("result", new Gson().toJson(responseData));
        return view;

    }
}
