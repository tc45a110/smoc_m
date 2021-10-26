package com.smoc.cloud.admin.controller;

import com.smoc.cloud.common.utils.RSAUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 首页
 * 2019/4/18 12:22
 **/
@Slf4j
@RestController
public class IndexController {

    /**
     * 首页入口
     *
     * @return
     */
    @RequestMapping("/index")
    public ModelAndView index() {

        ModelAndView view = new ModelAndView("index");

        return view;
    }

    @RequestMapping(value = "/getPublicKey", method = RequestMethod.GET)
    public String getKey(HttpServletRequest request){
        String publicKey = RSAUtils.generateBase64PublicKey();
        return publicKey;
    }
}
