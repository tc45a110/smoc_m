package com.smoc.cloud.iot.main;

import com.smoc.cloud.admin.security.remote.service.SysUserService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.validator.UserValidator;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 公用系统用户（可自定义扩展）
 * 2019/5/14 11:37
 **/
@Controller
@RequestMapping("/sysUser")
public class SysUserController {

//    @Autowired
//    private SysUserService sysUserService;
//
//    /**
//     * 查看详细信息
//     *
//     * @return
//     */
//    @RequestMapping(value = "/userProfile/{id}", method = RequestMethod.GET)
//    public ModelAndView userProfile(@PathVariable String id, HttpServletRequest request) {
//        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");
//        ModelAndView view = new ModelAndView("sys_user/user_profile");
//
//        if(!user.getId().equals(id)){
//            view = new ModelAndView("denied");
//            view.addObject("error", ResponseCode.PARAM_ERROR.getCode());
//            return view;
//        }
//
//        ResponseData<UserValidator> data = sysUserService.userProfile(id);
//
//        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
//            view.addObject("error", data.getCode() + ":" + data.getMessage());
//        }
//        view.addObject("sysUser", data.getData());
//
//        return view;
//
//    }

}
