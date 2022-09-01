package com.inse.worker;
import com.alibaba.fastjson.JSONObject;
import com.base.common.manager.ResourceManager;
import com.base.common.util.DateUtil;
import com.base.common.util.HttpClientUtil;
import com.base.common.worker.SuperQueueWorker;
import com.inse.util.ChannelInterfaceUtil;
import com.inse.util.DAO;
import com.inse.util.MyStringUtils;
import org.apache.commons.lang3.StringUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PushTemplateStatusWorker extends SuperQueueWorker<String> {

    private String channelID;
    private static int TIMEOUT = ResourceManager.getInstance().getIntValue("timeout");
    private static int RESPONSE_TIMEOUT = ResourceManager.getInstance().getIntValue("response.timeout");
    //平台模板状态 0无效、1：拒绝、2：审核通过、3：待审核
    /**
     * 通道模板状态
     * 0: 无效模板
     * 1:等待审核
     * 2:通过审核
     * 3: 拒绝
     */

    /**
     * 通道模板状态和平台模板状态的对应关系Map
     */
    private static final Map<String, String> CHANNEL_TEMPLATE_STATUS_MAP = new HashMap<String, String>();
    static {
        CHANNEL_TEMPLATE_STATUS_MAP.put("0", "0");
        CHANNEL_TEMPLATE_STATUS_MAP.put("1", "3");
        CHANNEL_TEMPLATE_STATUS_MAP.put("2", "2");
        CHANNEL_TEMPLATE_STATUS_MAP.put("3", "1");
    }
    public PushTemplateStatusWorker(String channelID){
        this.channelID = channelID;
    }

    @Override
    protected void doRun() throws Exception{
        try {
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
                        if (object.containsKey("code") && "0000".equals(object.getString("code"))) {
                            JSONObject jsonData = JSONObject.parseObject(object.getString("data"));
                            String statusDesc = jsonData.getString("statusDesc");
                            //获取通道模板状态
                            String channelTemplateStatus = jsonData.getString("templateStatus");
                            String platformTemplateStatus = CHANNEL_TEMPLATE_STATUS_MAP.get(channelTemplateStatus);
                            if (StringUtils.isNotEmpty(platformTemplateStatus)) {
                                DAO.updateAccountChannelTemplateInfo(channelTemplateStatus, statusDesc, channelID, channelTemplateID, 2);
                                logger.info("运营商模板ID={},运营商状态={},转换平台状态={}", channelTemplateID, channelTemplateStatus, platformTemplateStatus);
                            } else {
                                logger.warn("运营商模板ID={},运营商状态={},转换平台状态为空", channelTemplateID, channelTemplateStatus);
                            }
                        }
                    }
                }
            }
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
        String loginname= resultMap.get("login-name");
        String loginpass= resultMap.get("login-pass");
        String signaTureNonce = DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_MILLI)+ MyStringUtils.getRandom(10);
        String timesTamp =DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_MILLI);
        //请求的数据
        JSONObject jsonobject = new JSONObject();
        jsonobject.put("orderNo", signaTureNonce);
        jsonobject.put("account", loginname);
        jsonobject.put("templateId", templateId);
        jsonobject.put("timestamp", timesTamp);

        String signature= MyStringUtils.getSignaTure(signaTureNonce, loginname, timesTamp, loginpass);
        Map<String, String> headerMap=new HashMap<>();
        headerMap.put("Content-Type", "application/json; charset=utf-8");
        headerMap.put("signature-nonce", signaTureNonce);
        headerMap.put("account", loginname);
        headerMap.put("signature", signature);
        String url=resultMap.get("url")+"/template/getTemplateStatus";
        return HttpClientUtil.doRequest(url,headerMap,jsonobject.toString(),TIMEOUT,RESPONSE_TIMEOUT);
    }
}
