package com.smoc.cloud.main;

import com.google.gson.Gson;
import com.smoc.cloud.admin.oauth2.service.OauthTokenService;
import com.smoc.cloud.admin.security.properties.SystemProperties;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.qo.Nodes;
import com.smoc.cloud.common.auth.validator.SystemValidator;
import com.smoc.cloud.common.constant.RedisConstant;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@RestController
public class ServiceMainController {

    @Autowired
    private OauthTokenService oauthTokenService;

    @Autowired
    private SystemProperties systemProperties;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private MainUtils mainUtils;

    /**
     * 系统入口
     *
     * @return
     */
    @RequestMapping("/service_main1")
    public ModelAndView main1(HttpServletRequest request) {

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

        view.addObject("indexUrl", businessTypes.getData()[0].getHref());

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

        return view;
    }

    /**
     * 系统入口
     *
     * @return
     */
    @RequestMapping(value = "/message1/{parentId}", method = RequestMethod.GET)
    public ModelAndView industry1(@PathVariable String parentId, HttpServletRequest request) {

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

        return view;
    }

    /**
     * 系统入口
     *
     * @return
     */
    @RequestMapping("/service_main")
    public ModelAndView main(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("main");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

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

        view.addObject("indexUrl", businessTypes.getData()[0].getHref());

        //log.info("[businessTypes]:{}",new Gson().toJson(businessTypes));
        view.addObject("businessTypes", businessTypes.getData());
        view.addObject("activeId", businessTypes.getData()[0].getId());
        view.addObject("businessTypeName", businessTypes.getData()[0].getText());

        //redis缓存
        ValueOperations<String,String> vo = redisTemplate.opsForValue();
        String value = vo.get(RedisConstant.SERICE_UI_MENUS+":"+user.getId()+":"+businessTypes.getData()[0].getText());
        Gson gson = new Gson();
        if(StringUtils.isEmpty(value)){
            //加载菜单数据
            ResponseData<Nodes[]> data = oauthTokenService.getAllSubMenusByParentId(businessTypes.getData()[0].getId());
            if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
                view.addObject("error", data.getCode() + ":" + data.getMessage());
                return view;
            }
            view.addObject("menus", data);
            vo.set(RedisConstant.SERICE_UI_MENUS+":"+user.getId()+":"+businessTypes.getData()[0].getText(), gson.toJson(data.getData()));
        }else{
            Nodes[] nodesData = gson.fromJson(value, Nodes[].class);
            view.addObject("menus", ResponseDataUtil.buildSuccess(nodesData));
        }

        //异步存放权限
        mainUtils.setReidsData(businessTypes,vo,user);

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

        //redis缓存中获取
        ValueOperations<String,String> vo = redisTemplate.opsForValue();
        String value = vo.get(RedisConstant.SERICE_UI_MENUS+":"+user.getId()+":"+businessTypeName);
        Gson gson = new Gson();
        if(StringUtils.isEmpty(value)){
            ResponseData<Nodes[]> data = oauthTokenService.getAllSubMenusByParentId(parentId);
            vo.set(RedisConstant.SERICE_UI_MENUS+":"+user.getId()+":"+businessTypeName, gson.toJson(data.getData()));
            view.addObject("menus", data);
        }else{
            Nodes[] nodesData = gson.fromJson(value, Nodes[].class);
            view.addObject("menus", ResponseDataUtil.buildSuccess(nodesData));
        }

        return view;
    }
}
