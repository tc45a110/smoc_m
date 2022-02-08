package com.smoc.cloud.main;

import com.google.gson.Gson;
import com.smoc.cloud.admin.oauth2.service.OauthTokenService;
import com.smoc.cloud.admin.security.properties.SystemProperties;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.qo.Nodes;
import com.smoc.cloud.common.auth.validator.SystemValidator;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class ServiceMainController {

    @Autowired
    private OauthTokenService oauthTokenService;

    @Autowired
    private SystemProperties systemProperties;

    /**
     * 系统入口
     *
     * @return
     */
    @RequestMapping("/service_main")
    public ModelAndView main(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("main");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        Map<String, SystemValidator> sysMap = (Map<String, SystemValidator>) request.getServletContext().getAttribute("sysMap");

        //授权的短信业务类型
        ResponseData<Nodes[]> businessTypes = oauthTokenService.getSubNodes(user.getId(),"30703d7112a340f9ab4343d10bc2ef9c");

        if (!ResponseCode.SUCCESS.getCode().equals(businessTypes.getCode())) {
            view.addObject("error", businessTypes.getCode() + ":" + businessTypes.getMessage());
            return view;
        }

        if(null == businessTypes.getData() || businessTypes.getData().length<1){
            view.addObject("error", "没有业务授权");
            return view;
        }

        //log.info("[businessTypes]:{}",new Gson().toJson(businessTypes));
        view.addObject("businessTypes", businessTypes.getData());

        //加载菜单数据
        ResponseData<Nodes[]> data = oauthTokenService.getAllSubMenusByParentId(businessTypes.getData()[0].getId());
        view.addObject("activeId", businessTypes.getData()[0].getId());
        view.addObject("businessTypeName", businessTypes.getData()[0].getText());
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }
        view.addObject("menus", data);

        List<SystemValidator> list = new ArrayList();
        String[] scopes = user.getAuthScope().split(",");
        for (int i = 0; i < scopes.length; i++) {
            if (!systemProperties.getSystemMarking().equals(scopes[i])) {
                list.add(sysMap.get(scopes[i]));
            }
        }
        view.addObject("sysList", list);

        //引导页显示
        String guideCount = ""+request.getSession().getAttribute("guideCount");
        if(!StringUtils.isEmpty(guideCount) && !"null".equals(guideCount)){
            view.addObject("guideCount","1");
            request.getSession().setAttribute("guideCount", "");
        }

        return view;
    }

    /**
     * 系统入口
     *
     * @return
     */
    @RequestMapping(value = "/message/{parentId}", method = RequestMethod.GET)
    public ModelAndView industry(@PathVariable String parentId, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("main");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        Map<String, SystemValidator> sysMap = (Map<String, SystemValidator>) request.getServletContext().getAttribute("sysMap");

        //授权的短信业务类型
        ResponseData<Nodes[]> businessTypes = oauthTokenService.getSubNodes(user.getId(),"30703d7112a340f9ab4343d10bc2ef9c");

        if (!ResponseCode.SUCCESS.getCode().equals(businessTypes.getCode())) {
            view.addObject("error", businessTypes.getCode() + ":" + businessTypes.getMessage());
            return view;
        }

        if(null == businessTypes.getData() || businessTypes.getData().length<1){
            view.addObject("error", "没有业务授权");
            return view;
        }

        //log.info("[businessTypes]:{}",new Gson().toJson(businessTypes));
        view.addObject("businessTypes", businessTypes.getData());
        view.addObject("activeId",parentId);


        String businessTypeName = "";
        String indexUrl = "";
        for(Nodes obj:businessTypes.getData()){
           if(obj.getId().equals(parentId)){
               businessTypeName = obj.getText();
               indexUrl = obj.getHref();
               break;
           }
        }
        if("".equals(businessTypeName)){
            view.addObject("error", "非法请求");
            return view;
        }
        view.addObject("businessTypeName", businessTypeName);
        view.addObject("indexUrl", indexUrl);


        //加载菜单数据
        ResponseData<Nodes[]> data = oauthTokenService.getAllSubMenusByParentId(parentId);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }
        //log.info("[resource]:{}",new Gson().toJson(data));
        view.addObject("menus", data);

        List<SystemValidator> list = new ArrayList();
        String[] scopes = user.getAuthScope().split(",");
        for (int i = 0; i < scopes.length; i++) {
            if (!systemProperties.getSystemMarking().equals(scopes[i])) {
                list.add(sysMap.get(scopes[i]));
            }
        }
        view.addObject("sysList", list);

        //引导页显示
        String guideCount = ""+request.getSession().getAttribute("guideCount");
        if(!StringUtils.isEmpty(guideCount) && !"null".equals(guideCount)){
            view.addObject("guideCount","1");
            request.getSession().setAttribute("guideCount", "");
        }

        return view;
    }
}
