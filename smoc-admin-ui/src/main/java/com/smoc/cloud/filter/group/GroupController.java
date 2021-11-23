package com.smoc.cloud.filter.group;

import com.smoc.cloud.common.auth.qo.Nodes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分类管理
 **/
@RestController
@RequestMapping("/filter/group")
public class GroupController {

    /**
     * 树形
     * @param parentId
     * @param request
     * @return
     */
    @RequestMapping(value = "/tree/{parentId}", method = RequestMethod.GET)
    public List<Nodes> treeByParentId(@PathVariable String parentId, HttpServletRequest request) {

        if("white".equals(parentId)){
            return whiteGroupNodes();
        }

        if("black".equals(parentId)){
            return blackGroupNodes();
        }

        return new ArrayList<Nodes>();
    }

    private List<Nodes> blackGroupNodes() {
        List<Nodes> groupNodes = new ArrayList<Nodes>();

        //跟节点
        Nodes tmp = new Nodes();
        tmp.setId("root");
        tmp.setHref("0");
        tmp.setLazyLoad(false);
        tmp.setSvcType("black");
        tmp.setText("北京星语互联科技有限公司");

        List<Nodes> tmpgroupNodes = new ArrayList<>();

        Nodes tmpNodesA = new Nodes();
        tmpNodesA.setId("1");
        tmpNodesA.setHref("0");
        tmpNodesA.setLazyLoad(false);
        tmpNodesA.setSvcType("black");
        Map<String, Object> stateMap = new HashMap<String, Object>();
        stateMap.put("selected", true);
        tmpNodesA.setState(stateMap);
        tmpNodesA.setText("征信黑名单");

        Nodes tmpNodesB = new Nodes();
        tmpNodesB.setId("2");
        tmpNodesB.setHref("0");
        tmpNodesB.setLazyLoad(false);
        tmpNodesB.setSvcType("black");
        tmpNodesB.setText("网贷黑名单");


        Nodes tmpNodesC = new Nodes();
        tmpNodesC.setId("3");
        tmpNodesC.setHref("0");
        tmpNodesC.setLazyLoad(false);
        tmpNodesC.setSvcType("black");
        tmpNodesC.setText("网贷黑名单");

        Nodes tmpNodesD = new Nodes();
        tmpNodesD.setId("4");
        tmpNodesD.setHref("0");
        tmpNodesD.setLazyLoad(false);
        tmpNodesD.setSvcType("black");
        tmpNodesD.setText("网贷黑名单");

        tmpgroupNodes.add(tmpNodesA);
        tmpgroupNodes.add(tmpNodesB);
        tmpgroupNodes.add(tmpNodesC);
        tmpgroupNodes.add(tmpNodesD);

        tmp.setNodes(tmpgroupNodes);
        groupNodes.add(tmp);

        return groupNodes;
    }

    private List<Nodes> whiteGroupNodes() {
        List<Nodes> groupNodes = new ArrayList<Nodes>();

        //跟节点
        Nodes tmp = new Nodes();
        tmp.setId("root");
        tmp.setHref("0");
        tmp.setLazyLoad(false);
        tmp.setSvcType("white");
        tmp.setText("北京星语互联科技有限公司");

        List<Nodes> tmpgroupNodes = new ArrayList<>();

        Nodes tmpNodesA = new Nodes();
        tmpNodesA.setId("1");
        tmpNodesA.setHref("0");
        tmpNodesA.setLazyLoad(false);
        tmpNodesA.setSvcType("white");
        Map<String, Object> stateMap = new HashMap<String, Object>();
        stateMap.put("selected", true);
        tmpNodesA.setState(stateMap);
        tmpNodesA.setText("征信白名单");

        Nodes tmpNodesB = new Nodes();
        tmpNodesB.setId("2");
        tmpNodesB.setHref("0");
        tmpNodesB.setLazyLoad(false);
        tmpNodesB.setSvcType("white");
        tmpNodesB.setText("网贷白名单");


        Nodes tmpNodesC = new Nodes();
        tmpNodesC.setId("3");
        tmpNodesC.setHref("0");
        tmpNodesC.setLazyLoad(false);
        tmpNodesC.setSvcType("white");
        tmpNodesC.setText("网贷白名单");

        tmpgroupNodes.add(tmpNodesA);
        tmpgroupNodes.add(tmpNodesB);
        tmpgroupNodes.add(tmpNodesC);

        tmp.setNodes(tmpgroupNodes);
        groupNodes.add(tmp);

        return groupNodes;
    }


    /**
     * 分类列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/group/group_list");

        return view;

    }


    /**
     * 添加分类
     *
     * @return
     */
    @RequestMapping(value = "/add/{id}", method = RequestMethod.GET)
    public ModelAndView add(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/group/group_edit");

        return view;

    }

    /**
     * 编辑分类
     *
     * @return
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/group/group_edit");

        return view;

    }

}
