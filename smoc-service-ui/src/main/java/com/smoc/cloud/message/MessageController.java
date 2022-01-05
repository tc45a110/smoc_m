package com.smoc.cloud.message;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping("/message")
public class MessageController {

    /**
     * 投诉溯源检索分页
     * @return
     */
    @RequestMapping(value = "/center", method = RequestMethod.GET)
    public ModelAndView edit() {
        ModelAndView view = new ModelAndView("message/message_center");
        return view;
    }
}
