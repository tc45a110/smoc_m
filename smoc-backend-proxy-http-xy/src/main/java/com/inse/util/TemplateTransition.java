package com.inse.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.base.common.manager.ResourceManager;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemplateTransition {
    private static Logger logger = Logger.getLogger(TemplateTransition.class);
    public static String MMS_PATH = ResourceManager.getInstance().getValue("mms.resource.path");


    public static Map<String, String> getTemplate(String MmAttchnent, String channelID, String TemplateTitle, String signaTureNonce, String timesTamp, String templateFlag){
        // 获取通道接口扩展参数
        Map<String, String> resultMap = ChannelInterfaceUtil.getArgMap(channelID);
        String loginname= resultMap.get("login-name");
        String loginpass= resultMap.get("login-pass");
        // 请求的数据
        Map<String, Object> requestDataMap = new HashMap<>();
        // 订单号，成功后的订单不能重复
        requestDataMap.put("orderNo", signaTureNonce);
        // 业务账号；
        requestDataMap.put("account", loginname);
        // 时间戳
        requestDataMap.put("timestamp", timesTamp);

        List<Map<String, Object>> mediaItems = new ArrayList<>();
        try {
            JSONArray array = JSONObject.parseArray(MmAttchnent);
            for (int i = 0; i < array.size(); i++) {
                //总第几帧
                String str = array.get(i) + "";
                JSONObject object = JSONObject.parseObject(str);
                Map<String, Object> param = new HashMap<>();
                String frameTxt= object.getString("frameTxt");
                if (StringUtils.isNotEmpty(frameTxt)) {
                    param.put("frameTxt", frameTxt);
                } else {
                    param.put("frameTxt", "");
                }

                String resUrl = object.getString("resUrl");
                if(StringUtils.isNotEmpty(resUrl)) {
                    String path = MMS_PATH +resUrl;
                    param.put("mediaFile",  MyStringUtils.getFileToBase64(path));
                    param.put("mediaType", object.getString("resType"));
                    param.put("fileType", object.getString("resPostfix"));
                    param.put("stayTimes", object.getString("stayTimes"));
                } else {
                    param.put("mediaFile", "");
                    param.put("mediaType", "TXT");
                    param.put("fileType", "txt");
                    param.put("stayTimes", "");
                }
                param.put("subject", TemplateTitle);
                mediaItems.add(param);
            }
            requestDataMap.put("templateType", templateFlag);
            requestDataMap.put("templateTitle", TemplateTitle);
            requestDataMap.put("items", mediaItems);

            //转JSON请求数据
            String requestJsonData = new Gson().toJson(requestDataMap);

            resultMap.put("mmdl", requestJsonData);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return resultMap;
    }


    public static void main(String[] args) {
        String sa ="[{\"index\":0,\"resSize\":\"1867\",\"resUrl\":\"/20220531/38a23c7c6b2d4c6eb03e0e0ccb66b264/ec73039362cf432d93a645fa2228e997.png\",\"resType\":\"PIC\",\"resPostfix\":\"png\",\"stayTimes\":\"2\",\"frameTxt\":\"说几句急急急${1}，急急急${2}罗瑟科什\"},{\"index\":1,\"resSize\":\"6\",\"resUrl\":\"/20220422/38a23c7c6b2d4c6eb03e0e0ccb66b264/23086d60027049bb89725cc27f0899f1.png\",\"resType\":\"PIC\",\"resPostfix\":\"png\",\"stayTimes\":\"2\",\"frameTxt\":\"顶顶顶${3}顶顶顶顶顶\"}]";

    }

}
