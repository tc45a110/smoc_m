package com.smoc.cloud.configure.search;

/**
 * 通道查询，更多的是其他模块 应用
 */

import com.smoc.cloud.admin.security.remote.service.SysUserService;
import com.smoc.cloud.common.auth.qo.Users;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelBasicInfoQo;
import com.smoc.cloud.configure.channel.service.ChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 业务账号通道挂起
 */
@Slf4j
@Controller
@RequestMapping("/channel/search")
public class ConfigureChannelSearchController {

    @Autowired
    private ChannelService channelService;

    @Autowired
    private SysUserService sysUserService;

    /**
     * 业务账号通道挂起 通道查询
     *
     * @return
     */
    @RequestMapping(value = "/suspend/list", method = RequestMethod.GET)
    public ModelAndView list(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/search/channel_search_channel_suspend_list");
        ///初始化数据
        PageParams<ChannelBasicInfoQo> params = new PageParams<ChannelBasicInfoQo>();
        params.setPageSize(8);
        params.setCurrentPage(1);
        ChannelBasicInfoQo channelBasicInfoQo = new ChannelBasicInfoQo();
        params.setParams(channelBasicInfoQo);

        //查询
        ResponseData<PageList<ChannelBasicInfoQo>> data = channelService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //查询销售
        List<Users> list = sysUserService.salesList();
        Map<String, Users> salesMap = new HashMap<>();
        if(!StringUtils.isEmpty(list) && list.size()>0){
            salesMap = list.stream().collect(Collectors.toMap(Users::getId, Function.identity()));
        }

        view.addObject("channelBasicInfoQo", channelBasicInfoQo);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("salesMap", salesMap);

        return view;
    }

    /**
     * 业务账号通道挂起 通道查询分页
     *
     * @return
     */
    @RequestMapping(value = "/suspend/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute ChannelBasicInfoQo channelBasicInfoQo, PageParams pageParams) {
        ModelAndView view = new ModelAndView("configure/search/channel_search_channel_suspend_list");

        //分页查询
        pageParams.setParams(channelBasicInfoQo);

        ResponseData<PageList<ChannelBasicInfoQo>> data = channelService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //查询销售
        List<Users> list = sysUserService.salesList();
        Map<String, Users> salesMap = new HashMap<>();
        if(!StringUtils.isEmpty(list) && list.size()>0){
            salesMap = list.stream().collect(Collectors.toMap(Users::getId, Function.identity()));
        }

        view.addObject("channelBasicInfoQo", channelBasicInfoQo);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("salesMap", salesMap);
        return view;
    }

    /**
     * 业务账号特权通道 通道查询
     *
     * @return
     */
    @RequestMapping(value = "/privilege/list", method = RequestMethod.GET)
    public ModelAndView privilege(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/search/channel_search_channel_privilege_list");
        //查询数据
        PageParams params = new PageParams<>();
        params.setPages(3);
        params.setPageSize(10);
        params.setStartRow(1);
        params.setEndRow(10);
        params.setCurrentPage(1);
        params.setTotalRows(22);

        view.addObject("pageParams", params);

        return view;
    }

    /**
     * 业务账号特权通道 通道查询分页
     *
     * @return
     */
    @RequestMapping(value = "/privilege/page", method = RequestMethod.POST)
    public ModelAndView privilege_page() {
        ModelAndView view = new ModelAndView("configure/search/channel_search_channel_privilege_list");
        //查询数据
        PageParams params = new PageParams<>();
        params.setPages(3);
        params.setPageSize(10);
        params.setStartRow(1);
        params.setEndRow(10);
        params.setCurrentPage(1);
        params.setTotalRows(22);

        view.addObject("pageParams", params);
        return view;
    }

    /**
     * 业务账号普通通道 通道查询
     *
     * @return
     */
    @RequestMapping(value = "/normal/list", method = RequestMethod.GET)
    public ModelAndView normal(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/search/channel_search_channel_normal_list");
        //查询数据
        PageParams params = new PageParams<>();
        params.setPages(3);
        params.setPageSize(10);
        params.setStartRow(1);
        params.setEndRow(10);
        params.setCurrentPage(1);
        params.setTotalRows(22);

        view.addObject("pageParams", params);

        return view;
    }

    /**
     * 业务账号普通通道 通道查询分页
     *
     * @return
     */
    @RequestMapping(value = "/normal/page", method = RequestMethod.POST)
    public ModelAndView normal_page() {
        ModelAndView view = new ModelAndView("configure/search/channel_search_channel_normal_list");
        //查询数据
        PageParams params = new PageParams<>();
        params.setPages(3);
        params.setPageSize(10);
        params.setStartRow(1);
        params.setEndRow(10);
        params.setCurrentPage(1);
        params.setTotalRows(22);

        view.addObject("pageParams", params);
        return view;
    }
}
