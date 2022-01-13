package com.smoc.cloud.filter.keywords;

import com.smoc.cloud.common.auth.qo.Dict;
import com.smoc.cloud.common.auth.qo.DictType;
import com.smoc.cloud.common.auth.qo.Nodes;
import com.smoc.cloud.common.page.PageParams;
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
 * 分类关键词
 */
@RestController
@RequestMapping("/filter/keywords")
public class KeywordsClassifyController {

    /**
     * 分类关键词 main 页面
     *
     * @return
     */
    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public ModelAndView main() {

        ModelAndView view = new ModelAndView("filter/keywords/keyword_classify_main");
        view.addObject("parentId", "root");
        return view;

    }

    /**
     * 分类关键词 列表页
     *
     * @param code     大分类下的小分类 code
     * @param classify 大分类编码  CARRIER(运营商)，INDUSTRY_TYPE（行业分类）、INFO_TYPE（信息分类）
     * @param request
     * @return
     */
    @RequestMapping(value = "/classify/{classify}/list/{code}", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable String code, @PathVariable String classify, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/keywords/keyword_classify_list");

        /**
         * 取字典数据，对关键词进行分类
         */
        Map<String, DictType> dictMap = (Map<String, DictType>) request.getServletContext().getAttribute("dict");
        //运营商
        DictType carrier = dictMap.get("carrier");
        //行业分类
        DictType industryType = dictMap.get("industryType");
        //信息分类
        DictType infoType = dictMap.get("infoType");

        Map<String,String> dictValueMap = new HashMap<>();
        for (Dict dict : carrier.getDict()) {
            dictValueMap.put(dict.getFieldCode(),dict.getFieldName());
        }
        for (Dict dict : industryType.getDict()) {
            dictValueMap.put(dict.getFieldCode(),dict.getFieldName());
        }
        for (Dict dict : infoType.getDict()) {
            dictValueMap.put(dict.getFieldCode(),dict.getFieldName());
        }

        //查询数据
        PageParams params = new PageParams<>();
        params.setPages(10);
        params.setPageSize(1000);
        params.setStartRow(1);
        params.setEndRow(10);
        params.setCurrentPage(1);
        params.setTotalRows(80);

        view.addObject("pageParams",params);

        view.addObject("classify", classify);
        view.addObject("code", code);
        view.addObject("dictValueMap", dictValueMap);

        return view;

    }

    /**
     * 添加关键字
     *
     * @return
     */
    @RequestMapping(value = "/classify/{classify}/{code}/add", method = RequestMethod.GET)
    public ModelAndView add(@PathVariable String code, @PathVariable String classify, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/keywords/keyword_classify_edit_batch");

        /**
         * 取字典数据，对关键词进行分类
         */
        Map<String, DictType> dictMap = (Map<String, DictType>) request.getServletContext().getAttribute("dict");
        //运营商
        DictType carrier = dictMap.get("carrier");
        //行业分类
        DictType industryType = dictMap.get("industryType");
        //信息分类
        DictType infoType = dictMap.get("infoType");

        Map<String,String> dictValueMap = new HashMap<>();
        for (Dict dict : carrier.getDict()) {
            dictValueMap.put(dict.getFieldCode(),dict.getFieldName());
        }
        for (Dict dict : industryType.getDict()) {
            dictValueMap.put(dict.getFieldCode(),dict.getFieldName());
        }
        for (Dict dict : infoType.getDict()) {
            dictValueMap.put(dict.getFieldCode(),dict.getFieldName());
        }

        view.addObject("classify", classify);
        view.addObject("code", code);
        view.addObject("dictValueMap", dictValueMap);

        return view;

    }

    /**
     * 编辑关键字
     *
     * @return
     */
    @RequestMapping(value = "/classify/{classify}/{code}/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String code, @PathVariable String classify,@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/keywords/keyword_classify_edit");

        /**
         * 取字典数据，对关键词进行分类
         */
        Map<String, DictType> dictMap = (Map<String, DictType>) request.getServletContext().getAttribute("dict");
        //运营商
        DictType carrier = dictMap.get("carrier");
        //行业分类
        DictType industryType = dictMap.get("industryType");
        //信息分类
        DictType infoType = dictMap.get("infoType");

        Map<String,String> dictValueMap = new HashMap<>();
        for (Dict dict : carrier.getDict()) {
            dictValueMap.put(dict.getFieldCode(),dict.getFieldName());
        }
        for (Dict dict : industryType.getDict()) {
            dictValueMap.put(dict.getFieldCode(),dict.getFieldName());
        }
        for (Dict dict : infoType.getDict()) {
            dictValueMap.put(dict.getFieldCode(),dict.getFieldName());
        }

        view.addObject("classify", classify);
        view.addObject("code", code);
        view.addObject("dictValueMap", dictValueMap);
        return view;

    }

    /**
     * 树形
     *
     * @param parentId
     * @param request
     * @return
     */
    @RequestMapping(value = "/tree/{parentId}", method = RequestMethod.GET)
    public List<Nodes> treeByParentId(@PathVariable String parentId, HttpServletRequest request) {
        //跟节点
        Nodes root = new Nodes();
        root.setId("root");
        root.setHref("0");
        root.setLazyLoad(false);
        root.setSvcType("root");
        root.setText("分类关键词");

        Map<String, DictType> dictMap = (Map<String, DictType>) request.getServletContext().getAttribute("dict");

        //运营商
        DictType carrier = dictMap.get("carrier");
        Nodes carrierNode = new Nodes();
        carrierNode.setId("1");
        carrierNode.setHref("0");
        carrierNode.setLazyLoad(false);
        carrierNode.setSvcType("CLASSIFY");
        carrierNode.setText("运营商");
        List<Nodes> carrierNodes = new ArrayList<>();
        for (Dict dict : carrier.getDict()) {
            Nodes dictNode = new Nodes();
            dictNode.setId(dict.getFieldCode());
            dictNode.setHref(dict.getFieldCode());
            dictNode.setLazyLoad(false);
            dictNode.setSvcType("leaf");
            dictNode.setOrgCode("CARRIER");
            dictNode.setText(dict.getFieldName());
            dictNode.setIcon(carrier.getIcon());
            carrierNodes.add(dictNode);
        }
        carrierNode.setNodes(carrierNodes);


        //行业分类
        DictType industryType = dictMap.get("industryType");
        Nodes industryTypeNode = new Nodes();
        industryTypeNode.setId("1");
        industryTypeNode.setHref("0");
        industryTypeNode.setLazyLoad(false);
        industryTypeNode.setSvcType("CLASSIFY");
        industryTypeNode.setText("行业分类");
        List<Nodes> industryTypeNodes = new ArrayList<>();
        for (Dict dict : industryType.getDict()) {
            Nodes dictNode = new Nodes();
            dictNode.setId(dict.getFieldCode());
            dictNode.setHref(dict.getFieldCode());
            dictNode.setLazyLoad(false);
            dictNode.setSvcType("leaf");
            dictNode.setOrgCode("INDUSTRY_TYPE");
            dictNode.setText(dict.getFieldName());
            dictNode.setIcon(industryType.getIcon());
            industryTypeNodes.add(dictNode);
        }
        industryTypeNode.setNodes(industryTypeNodes);

        //信息分类
        DictType infoType = dictMap.get("infoType");
        Nodes infoTypeNode = new Nodes();
        infoTypeNode.setId("1");
        infoTypeNode.setHref("0");
        infoTypeNode.setLazyLoad(false);
        infoTypeNode.setSvcType("CLASSIFY");
        infoTypeNode.setText("信息分类");
        List<Nodes> infoTypeNodes = new ArrayList<>();
        for (Dict dict : infoType.getDict()) {
            Nodes dictNode = new Nodes();
            dictNode.setId(dict.getFieldCode());
            dictNode.setHref(dict.getFieldCode());
            dictNode.setLazyLoad(false);
            dictNode.setSvcType("leaf");
            dictNode.setOrgCode("INFO_TYPE");
            dictNode.setText(dict.getFieldName());
            dictNode.setIcon(infoType.getIcon());
            infoTypeNodes.add(dictNode);
        }
        infoTypeNode.setNodes(infoTypeNodes);

        List<Nodes> rootList = new ArrayList<>();
        rootList.add(carrierNode);
        rootList.add(industryTypeNode);
        rootList.add(infoTypeNode);
        root.setNodes(rootList);

        List<Nodes> list = new ArrayList<>();
        list.add(root);

        return list;
    }
}
