package com.protocol.proxy.worker;
import com.alibaba.fastjson.JSONObject;
import com.base.common.constant.DynamicConstant;
import com.base.common.manager.ResourceManager;
import com.base.common.util.DateUtil;
import com.base.common.util.HttpClientUtil;
import com.base.common.worker.SuperQueueWorker;
import com.protocol.proxy.util.ChannelInterfaceUtil;
import com.protocol.proxy.util.DAO;
import com.protocol.proxy.util.MyStringUtils;
import org.apache.commons.lang3.StringUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PushTemplateStatusWorker extends SuperQueueWorker<String> {

    private String channelID;
    private static int TIMEOUT = ResourceManager.getInstance().getIntValue("timeout");
    private static int RESPONSE_TIMEOUT = ResourceManager.getInstance().getIntValue("response.timeout");

    public PushTemplateStatusWorker(String channelID){
        this.channelID = channelID;
    }

    @Override
    protected void doRun() throws Exception{
        try {
            long startTime = System.currentTimeMillis();
            List<String> list = DAO.getUnreviewedTempldateID(channelID);
            if (list != null && list.size() > 0) {
                for (String channelTemplateID : list) {
                    String response = getTemplateStatus(channelTemplateID);
                    logger.info("获取通道模板ID={},响应信息={},", channelTemplateID, response);
                    if (StringUtils.isNotEmpty(response) && response.contains("code")) {
                        JSONObject object = null;
                        try {
                            object = JSONObject.parseObject(response);
                        } catch (Exception e) {
                            logger.error(e.getMessage());
                        }
                        if (object.containsKey("code") && DynamicConstant.RESPONSE_SUCCESS_CODE.equals(object.getString("code"))) {
                            JSONObject jsonData = JSONObject.parseObject(object.getString("data"));
                            String statusDesc = jsonData.getString("statusDesc");
                            //获取通道模板状态
                            String channelTemplateStatus = jsonData.getString("templateStatus");

                            if (StringUtils.isNotEmpty(channelTemplateStatus)) {
                                DAO.updateAccountChannelTemplateInfo(channelTemplateStatus, statusDesc, channelID, channelTemplateID);
                                logger.info("获取通道模板ID={},通道模板状态={},转换平台状态={}", channelTemplateID, channelTemplateStatus, channelTemplateStatus);
                            }
                        }
                    }
                }
            }
            controlSubmitSpeed(INTERVAL, (System.currentTimeMillis() - startTime));
        } catch (Exception e) {
            logger.error("Worker error " + e.getMessage(), e);
        }
    }

    /**
     * 按照通道规范提交多媒体素材
     * @param
     * @return
     */
    private String getTemplateStatus(String templateId) {
        //获取通道接口扩展参数
        Map<String, String> resultMap = ChannelInterfaceUtil.getArgMap(channelID);
        String loginName= resultMap.get("login-name");
        String loginPass= resultMap.get("login-pass");
        String signatureNonce = DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_MILLI)+ MyStringUtils.getRandom(10);
        String timesTamp =DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_MILLI);
        //请求的数据
        JSONObject jsonobject = new JSONObject();
        jsonobject.put("orderNo", signatureNonce);
        jsonobject.put("account", loginName);
        jsonobject.put("templateId", templateId);
        jsonobject.put("timestamp", timesTamp);

        String signature= MyStringUtils.getSignature(signatureNonce, loginName, timesTamp, loginPass);
        Map<String, String> headerMap=new HashMap<>();
        headerMap.put("Content-Type", "application/json; charset=utf-8");
        headerMap.put("signature-nonce", signatureNonce);
        headerMap.put("account", loginName);
        headerMap.put("signature", signature);
        String url=resultMap.get("url")+"/template/getTemplateStatus";
        return HttpClientUtil.doRequest(url,headerMap,jsonobject.toString(),TIMEOUT,RESPONSE_TIMEOUT);
    }
}
