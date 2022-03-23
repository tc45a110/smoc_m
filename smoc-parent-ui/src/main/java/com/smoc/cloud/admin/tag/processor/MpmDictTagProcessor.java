package com.smoc.cloud.admin.tag.processor;

import com.smoc.cloud.common.auth.qo.Dict;
import com.smoc.cloud.common.auth.qo.DictType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.util.StringUtils;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.context.WebEngineContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;
import org.unbescape.html.HtmlEscape;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * mpm字典标签方言处理器
 * 2019/5/23 15:19
 * <mpm:tag identity="userType" show-type="label" key="code"  name="userType" th:value="3333" title="用户类别"  data-icon="mdi mdi-select-all" required="true" class="radio-indddline" />
 * 标签属性
 * identity 字典类型（配合字典管理）   required
 * show-type 显示类型 （label、select、checkbox、radio） required
 * key      传值类型 key=“code” 传值为字典配置里的 value，key 为其他值 传值为ID
 * name html 元素 name 名称
 * th:value  传值
 * title  select 下拉提示
 * data-icon  select option 显示图标
 * required 是否必填
 * class 显示的 样式类+
 **/
@Slf4j
public class MpmDictTagProcessor extends AbstractAttributeTagProcessor {

    //自定义标签属性名
    private static final String ELEMENT_NAME = "tag";
    private static final String ATTRIBUTE_NAME = "identity";
    private static final int PRECEDENCE = 10000;

    //id
    private static final String ID = "id";

    private static final String DISABLED = "disabled";

    //字典标识  必输项
    private static final String IDENTITY = "identity";

    //显示方式 当前支持 label\select\checkbox\radio  必输项
    private static final String SHOWTYPE = "show-type";

    //字段名称
    private static final String NAME = "name";

    //字段值
    private static final String VALUE = "value";

    //key字段 对应于字典总的id和code  范围为id 或 code
    private static final String KEY = "key";

    //是否必填字段
    private static final String REQUIRED = "required";

    //样式
    private static final String CLASS = "class";

    //select 图标  默认mdi mdi-send
    private static final String DATAICON = "data-icon";

    //select 默认选择提示
    private static final String TITLE = "title";

    private static final String ONCHANGE = "onchange";

    private static final String STYLE = "style";

    //屏蔽数值
    private static final String MASKVALUE = "mask-value";

    //字典数据
    List<Dict> dictList = new ArrayList<>();

    /*templateMode: 模板模式，这里使用HTML模板。
     dialectPrefix: 标签前缀。即xxx:text中的xxx。在此例子中prefix为mpm。
     elementName：匹配标签元素名。举例来说如果是div，则我们的自定义标签只能用在div标签中。为null能够匹配所有的标签。
     prefixElementName: 标签名是否要求前缀。
     attributeName: 自定义标签属性名。
     prefixAttributeName：属性名是否要求前缀，如果为true，Thymeeleaf会要求使用text属性时必须加上前缀，即thSys:text。
     precedence：标签处理的优先级，此处使用和Thymeleaf标准方言相同的优先级。
     removeAttribute：标签处理后是否移除自定义属性。*/

    public MpmDictTagProcessor(String dialectPrefix) {
        super(
                TemplateMode.HTML,
                dialectPrefix,
                ELEMENT_NAME,
                true,
                ATTRIBUTE_NAME,
                false,
                PRECEDENCE,
                true);
    }

    //这里处理标签的逻辑
    @Override
    protected void doProcess(ITemplateContext iTemplateContext, IProcessableElementTag iProcessableElementTag, AttributeName attributeName, String s, IElementTagStructureHandler iElementTagStructureHandler) {

        //获取标签属性
        Map<String, String> attributeMap = iProcessableElementTag.getAttributeMap();

        //字典标识
        String identity = attributeMap.get(IDENTITY);
        if (StringUtils.isEmpty(identity)) {
            buildError(iTemplateContext, iElementTagStructureHandler, IDENTITY + "属性不能为空！");
            return;
        }

        //显示方式 当前支持 label\select\checkbox\radio
        String showType = attributeMap.get(SHOWTYPE);
        if (StringUtils.isEmpty(showType)) {
            buildError(iTemplateContext, iElementTagStructureHandler, SHOWTYPE + "属性不能为空！");
            return;
        }

        //检查 showType 类型合法性
        if (!this.checkShowType(showType)) {
            buildError(iTemplateContext, iElementTagStructureHandler, SHOWTYPE + "不支持属性：" + showType);
            return;
        }


        //取字典数据
        WebEngineContext webEngineContext = (WebEngineContext) iTemplateContext;
        HttpServletRequest request = webEngineContext.getRequest();
        ServletContext context = request.getServletContext();

        Map<String, DictType> dictMap = (Map<String, DictType>) context.getAttribute("dict");

        DictType dictType = dictMap.get(identity);

        //如果数据或字典标识数据为空
        if (null == dictMap || dictMap.size() < 1 || StringUtils.isEmpty(dictType) || null == dictType.getDict() || dictType.getDict().size() < 1) {
            //进行处理
            buildError(iTemplateContext, iElementTagStructureHandler, "数据为空或无效" + IDENTITY);
            return;
        }

        String dataIcon = dictType.getIcon();
        dictList = dictType.getDict();

        //根据showType组建对应的 元素
        if (showType.equals("label")) {
            buildLabel(iTemplateContext, iElementTagStructureHandler, dictList, attributeMap);
        }

        if (showType.equals("select")) {
            buildSelect(iTemplateContext, iElementTagStructureHandler, dictList, attributeMap, dataIcon);
        }

        if (showType.equals("checkbox")) {
            buildCheckBox(iTemplateContext, iElementTagStructureHandler, dictList, attributeMap);
        }

        if (showType.equals("radio")) {
            buildRadio(iTemplateContext, iElementTagStructureHandler, dictList, attributeMap);
        }

        if (showType.equals("button")) {
            buildButton(iTemplateContext, iElementTagStructureHandler, dictList, attributeMap);
        }
    }

    /**
     * 组建Label
     *
     * @param iTemplateContext
     * @param iElementTagStructureHandler
     * @param dictList
     * @param attributeMap
     */
    private void buildLabel(ITemplateContext iTemplateContext, IElementTagStructureHandler iElementTagStructureHandler, List<Dict> dictList, Map<String, String> attributeMap) {

        final IModelFactory modelFactory = iTemplateContext.getModelFactory();
        final IModel model = modelFactory.createModel();

        //样式
        String classValue = attributeMap.get(CLASS);
        if (StringUtils.isEmpty(classValue)) {
            //start span
            model.add(modelFactory.createOpenElementTag("span"));
        } else {
            //start span
            model.add(modelFactory.createOpenElementTag("span", "class", classValue));
        }

        //判断用那种key的方式  key值为code 表示用字典里的code值
        String key = attributeMap.get(KEY);
        Boolean status = false;
        if ("code".equals(key)) {
            status = true;
        }

        //字段值
        String value = attributeMap.get(VALUE);

        //回显值
        String showValue = value;

        String[] checkValue = value.split(",");
        if (checkValue.length > 1) {
            String checkboxValue = "";
            for (int a = 0; a < checkValue.length; a++) {
                for (int i = 0; i < dictList.size(); i++) {
                    Dict dict = dictList.get(i);
                    //根据fieldCode
                    if (status && checkValue[a].equals(dict.getFieldCode())) {
                        checkboxValue += dict.getFieldName() + " ";
                        break;
                    }
                }
            }
            showValue = checkboxValue;
        } else {
            if (!StringUtils.isEmpty(value)) {
                for (Dict dict : dictList) {

                    //根据fieldCode
                    if (status && value.equals(dict.getFieldCode())) {
                        showValue = dict.getFieldName();
                        break;
                    }
                    //根据id
                    else if (value.equals(dict.getId())) {
                        showValue = dict.getFieldName();
                        break;
                    }
                }
            }
        }


        //回显
        model.add(modelFactory.createText(HtmlEscape.escapeHtml5(showValue)));
        //end span
        model.add(modelFactory.createCloseElementTag("span"));

        iElementTagStructureHandler.replaceWith(model, false);
    }

    /**
     * 组建Select
     *
     * @param iTemplateContext
     * @param iElementTagStructureHandler
     * @param dictList
     * @param attributeMap
     */
    private void buildSelect(ITemplateContext iTemplateContext, IElementTagStructureHandler iElementTagStructureHandler, List<Dict> dictList, Map<String, String> attributeMap, String dataIcon) {

        final IModelFactory modelFactory = iTemplateContext.getModelFactory();
        final IModel model = modelFactory.createModel();

        //名称
        String name = attributeMap.get(NAME);
        if (StringUtils.isEmpty(name)) {
            buildError(iTemplateContext, iElementTagStructureHandler, NAME + "属性不能为空！");
        }

        //id
        String id = attributeMap.get(ID);

        Map<String, String> attri = new HashMap<String, String>();

        attri.put("id", id);
        attri.put("name", name);
        //样式
        String classValue = attributeMap.get(CLASS);
        if (StringUtils.isEmpty(classValue)) {

            attri.put("class", "selectpicker");
        } else {

            attri.put("class", "selectpicker " + classValue);
        }

        //失去焦点
        String onchange = attributeMap.get(ONCHANGE);
        if (!StringUtils.isEmpty(onchange)) {

            attri.put("onchange", onchange);
        }

        //是否必选
        String required = attributeMap.get(REQUIRED);
        if ("true".equals(required) || "required".equals(required)) {
            attri.put("required", "");
        }

        attri.put("data-live-search", "true");
        attri.put("data-style", "btn-default");

        //start select
        model.add(modelFactory.createOpenElementTag("select", attri, null, false));

        //判断用那种key的方式  key值为code 表示用字典里的code值
        String key = attributeMap.get(KEY);
        Boolean status = false;
        if ("code".equals(key)) {
            status = true;
        }

        //传入值
        String value = attributeMap.get(VALUE);

        //option 图标  默认mdi mdi-send
        //String dataIcon = attributeMap.get(DATAICON);
        if (StringUtils.isEmpty(dataIcon)) {
            dataIcon = "mdi mdi-equal-box";
        }

        //title 提示
        String title = attributeMap.get(TITLE);
        Map<String, String> attOption = new HashMap<String, String>();
        attOption.put("value", "");
        attOption.put("data-icon", dataIcon);
        model.add(modelFactory.createOpenElementTag("option", attOption, null, false));
        model.add(modelFactory.createText(HtmlEscape.escapeHtml5("--请选择" + title + "--")));
        model.add(modelFactory.createCloseElementTag("option"));

        String mvalue = attributeMap.get(MASKVALUE);

        for (Dict dict : dictList) {

            if (!StringUtils.isEmpty(mvalue)) {
                String[] mvalues = mvalue.split(",");
                if (ArrayUtils.contains(mvalues,dict.getFieldCode())){
                    continue;
                }
            }

            Map<String, String> attriOption = new HashMap<String, String>();
            if (status) {
                attriOption.put("data-icon", dataIcon);
                attriOption.put("value", dict.getFieldCode());
                if (!StringUtils.isEmpty(value) && dict.getFieldCode().equals(value)) {
                    attriOption.put("selected", "true");
                }
            } else {
                attriOption.put("value", dict.getId());
                if (!StringUtils.isEmpty(value) && dict.getId().equals(value)) {
                    attriOption.put("selected", "true");
                }
            }
            model.add(modelFactory.createOpenElementTag("option", attriOption, null, false));
            model.add(modelFactory.createText(HtmlEscape.escapeHtml5(dict.getFieldName())));
            model.add(modelFactory.createCloseElementTag("option"));
        }

        //end select
        model.add(modelFactory.createCloseElementTag("select"));
        iElementTagStructureHandler.replaceWith(model, false);

    }

    /**
     * 组建checkBox
     *
     * @param iTemplateContext
     * @param iElementTagStructureHandler
     * @param dictList
     * @param attributeMap
     */
    private void buildCheckBox(ITemplateContext iTemplateContext, IElementTagStructureHandler iElementTagStructureHandler, List<Dict> dictList, Map<String, String> attributeMap) {

        final IModelFactory modelFactory = iTemplateContext.getModelFactory();
        final IModel model = modelFactory.createModel();

        //ID
        String id = attributeMap.get(ID);

        //名称
        String name = attributeMap.get(NAME);
        if (StringUtils.isEmpty(name)) {
            buildError(iTemplateContext, iElementTagStructureHandler, NAME + "属性不能为空！");
        }

        //传入值
        String value = attributeMap.get(VALUE);
        //默认选中值
        Map<String, Boolean> selectMap = new HashMap<>();
        if (!StringUtils.isEmpty(value)) {
            String[] selectValues = value.split(",");
            for (int i = 0; i < selectValues.length; i++) {
                selectMap.put(selectValues[i], true);
            }
        }

        //判断用那种key的方式  key值为code 表示用字典里的code值
        String key = attributeMap.get(KEY);
        Boolean status = false;
        if ("code".equals(key)) {
            status = true;
        }

        //是否必选
        String required = attributeMap.get(REQUIRED);

        //样式
        Map<String, String> attri = new HashMap<String, String>();
        String classValue = attributeMap.get(CLASS);
        if (StringUtils.isEmpty(classValue)) {

            attri.put("class", "btn-switch btn-switch-info");
        } else {

            attri.put("class", "btn-switch btn-switch-info");
        }

        attri.put("style", "margin-left:10px;margin-bottom:10px");

        //查询屏蔽数值是否有值
        String mvalue = attributeMap.get(MASKVALUE);
        for (Dict dict : dictList) {
            if (!StringUtils.isEmpty(mvalue) && mvalue.indexOf(dict.getFieldCode()) >= 0) {
                continue;
            }

            //start div
            model.add(modelFactory.createOpenElementTag("div", attri, null, false));

            String fieldValue;
            if (status) {
                fieldValue = dict.getFieldCode();
            } else {
                fieldValue = dict.getId();
            }

            //样式
            Map<String, String> attriCheckBox = new HashMap<String, String>();
            //attriCheckBox.put("id", fieldValue);
            attriCheckBox.put("name", name);
            attriCheckBox.put("type", "checkbox");
            attriCheckBox.put("value", fieldValue);
            attriCheckBox.put("id", id + "-" + fieldValue);

            //事件
            String onchange = attributeMap.get(ONCHANGE);
            if (!StringUtils.isEmpty(onchange)) {
                attriCheckBox.put("onchange", onchange);
            }

            if (!StringUtils.isEmpty(required)) {
                attriCheckBox.put("required", "");
            }

            if (selectMap.size() > 0 && null != selectMap.get(fieldValue)) {
                attriCheckBox.put("checked", "true");
            }

            model.add(modelFactory.createOpenElementTag("input", attriCheckBox, null, false));


            //样式
            Map<String, String> attriLable = new HashMap<String, String>();
            attriLable.put("for", id + "-" + fieldValue);
            attriLable.put("class", "btn btn-rounded btn-info waves-effect waves-light");
            model.add(modelFactory.createOpenElementTag("label", attriLable, null, false));

            //em
            Map<String, String> attriEm = new HashMap<String, String>();
            attriEm.put("class", "glyphicon glyphicon-ok");
            model.add(modelFactory.createOpenElementTag("em", attriEm, null, false));
            model.add(modelFactory.createCloseElementTag("em"));

            //strong
            model.add(modelFactory.createOpenElementTag("strong"));
            model.add(modelFactory.createText(HtmlEscape.escapeHtml5(dict.getFieldName())));
            model.add(modelFactory.createCloseElementTag("strong"));

            model.add(modelFactory.createCloseElementTag("label"));

            //end div
            model.add(modelFactory.createCloseElementTag("div"));

        }


        iElementTagStructureHandler.replaceWith(model, false);

    }

    /**
     * 组建Radio
     *
     * @param iTemplateContext
     * @param iElementTagStructureHandler
     * @param dictList
     * @param attributeMap
     */
    private void buildRadio(ITemplateContext iTemplateContext, IElementTagStructureHandler iElementTagStructureHandler, List<Dict> dictList, Map<String, String> attributeMap) {

        final IModelFactory modelFactory = iTemplateContext.getModelFactory();
        final IModel model = modelFactory.createModel();

        //名称
        String name = attributeMap.get(NAME);
        if (StringUtils.isEmpty(name)) {
            buildError(iTemplateContext, iElementTagStructureHandler, NAME + "属性不能为空！");
        }

        //传入值
        String value = attributeMap.get(VALUE);


        //判断用那种key的方式  key值为code 表示用字典里的code值
        String key = attributeMap.get(KEY);
        Boolean status = false;
        if ("code".equals(key)) {
            status = true;
        }

        //是否必选
        String required = attributeMap.get(REQUIRED);

        //样式
        Map<String, String> attri = new HashMap<String, String>();
        String classValue = attributeMap.get(CLASS);
        if (StringUtils.isEmpty(classValue)) {

            attri.put("class", "radio radio-info checkbox-inline");
        } else {

            attri.put("class", "radio radio-info checkbox-inline " + classValue);
        }

        String style = attributeMap.get(STYLE);
        if (!StringUtils.isEmpty(style)) {
            attri.put("style", style);
        }

        //查询屏蔽数值是否有值
        String mvalue = attributeMap.get(MASKVALUE);
        for (Dict dict : dictList) {

            if (!StringUtils.isEmpty(mvalue) && mvalue.indexOf(dict.getFieldCode()) >= 0) {
                continue;
            }

            //start div
            model.add(modelFactory.createOpenElementTag("div", attri, null, false));

            String fieldValue;
            if (status) {
                fieldValue = dict.getFieldCode();
            } else {
                fieldValue = dict.getId();
            }

            //样式
            Map<String, String> attriCheckBox = new HashMap<String, String>();
            attriCheckBox.put("id", fieldValue);
            attriCheckBox.put("name", name);
            attriCheckBox.put("type", "radio");
            attriCheckBox.put("value", fieldValue);
            String disabled = attributeMap.get(DISABLED);
            if ("true".equals(disabled)) {
                attriCheckBox.put("disabled", "true");
            }

            if (!StringUtils.isEmpty(required)) {
                attriCheckBox.put("required", "");
            }

            if (!StringUtils.isEmpty(value) && fieldValue.equals(value)) {
                attriCheckBox.put("checked", "true");
            }

            //ID
            String id = attributeMap.get(ID);

            //事件
            String onchange = attributeMap.get(ONCHANGE);
            if (!StringUtils.isEmpty(onchange)) {
                attriCheckBox.put("onchange", onchange);
            }

            model.add(modelFactory.createOpenElementTag("input", attriCheckBox, null, false));

            model.add(modelFactory.createOpenElementTag("label"));
            model.add(modelFactory.createText(HtmlEscape.escapeHtml5(dict.getFieldName())));
            model.add(modelFactory.createCloseElementTag("label"));

            //end div
            model.add(modelFactory.createCloseElementTag("div"));

        }


        iElementTagStructureHandler.replaceWith(model, false);

    }

    /**
     * 组建Button
     *
     * @param iTemplateContext
     * @param iElementTagStructureHandler
     * @param dictList
     * @param attributeMap
     */
    private void buildButton(ITemplateContext iTemplateContext, IElementTagStructureHandler iElementTagStructureHandler, List<Dict> dictList, Map<String, String> attributeMap) {

        final IModelFactory modelFactory = iTemplateContext.getModelFactory();
        final IModel model = modelFactory.createModel();

        //字段值
        String value = attributeMap.get(VALUE);

        //样式
        String classValue = attributeMap.get(CLASS);
        if (StringUtils.isEmpty(classValue)) {
            //start a
            model.add(modelFactory.createOpenElementTag("<a class=\"btn btn-teal btn-rounded btn-sm waves-effect waves-light\">"));
        } else {
            String classShow = "btn btn-teal btn-rounded btn-sm waves-effect waves-light";
            if (!StringUtils.isEmpty(value)) {
                //获取class  并根据value 获取对应的 button 样式
                String[] classValueArray = classValue.split(";");
                //选中的样式

                for (int i = 0; i < classValueArray.length; i++) {
                    if (!StringUtils.isEmpty(classValueArray[i])) {
                        if (value.equals(classValueArray[i].split(",")[0])) {
                            classShow = classValueArray[i].split(",")[1];
                        }

                    }
                }
            }

            model.add(modelFactory.createOpenElementTag("span", "class", classShow));
        }

        //判断用那种key的方式  key值为code 表示用字典里的code值
        String key = attributeMap.get(KEY);
        Boolean status = false;
        if ("code".equals(key)) {
            status = true;
        }


        //回显值
        String showValue = value;

        if (!StringUtils.isEmpty(value)) {
            for (Dict dict : dictList) {

                //根据fieldCode
                if (status && value.equals(dict.getFieldCode())) {
                    showValue = dict.getFieldName();
                    break;
                }
                //根据id
                else if (value.equals(dict.getId())) {
                    showValue = dict.getFieldName();
                    break;
                }
            }
        }

        //回显
        model.add(modelFactory.createText(HtmlEscape.escapeHtml5(showValue)));
        //end span
        model.add(modelFactory.createCloseElementTag("a"));

        iElementTagStructureHandler.replaceWith(model, false);
    }

    /**
     * 标签异常处理
     *
     * @param iTemplateContext
     * @param iElementTagStructureHandler
     */
    private void buildError(ITemplateContext iTemplateContext, IElementTagStructureHandler iElementTagStructureHandler, String message) {

        final IModelFactory modelFactory = iTemplateContext.getModelFactory();
        final IModel model = modelFactory.createModel();
        model.add(modelFactory.createOpenElementTag("span", "class", "text-danger"));
        model.add(modelFactory.createText(HtmlEscape.escapeHtml5(message)));
        model.add(modelFactory.createCloseElementTag("span"));
        iElementTagStructureHandler.replaceWith(model, false);

    }

    /**
     * 检查标签显示类型数值
     *
     * @param showType
     * @return
     */
    private Boolean checkShowType(String showType) {

        Boolean status = false;

        String[] showTypeList = {"label", "select", "checkbox", "radio", "button"};
        for (int i = 0; i < showTypeList.length; i++) {
            if (showTypeList[i].equals(showType)) {
                status = true;
                break;
            }
        }

        return status;

    }


}
