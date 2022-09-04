package com.protocol.proxy.worker;
import com.alibaba.fastjson.JSONObject;
import com.base.common.cache.CacheBaseService;
import com.base.common.constant.FixedConstant;
import com.base.common.constant.InsideStatusCodeConstant;
import com.base.common.manager.ChannelInfoManager;
import com.base.common.manager.ChannelRunStatusManager;
import com.base.common.manager.ResourceManager;
import com.base.common.util.DateUtil;
import com.base.common.util.HttpClientUtil;
import com.base.common.vo.BusinessRouteValue;
import com.base.common.worker.SuperQueueWorker;
import com.protocol.proxy.manager.AccountChannelTemplatelnfoManager;
import com.protocol.proxy.manager.ChannellnteractiveStatusManager;
import com.protocol.proxy.util.ChannelInterfaceUtil;
import com.protocol.proxy.util.MyStringUtils;
import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubmitPullWorker extends SuperQueueWorker<BusinessRouteValue> {
    private static int TIMEOUT = ResourceManager.getInstance().getIntValue("timeout");
    private static int RESPONSE_TIMEOUT = ResourceManager.getInstance().getIntValue("response.timeout");
    private ResponseWorker responseWorker;
    private String channelID;

    public SubmitPullWorker(String channelID, String index) {
        this.channelID = channelID;
        responseWorker = new ResponseWorker(channelID, index);
        this.setName(new StringBuilder(channelID).append("-").append(index).toString());
        this.start();
    }

    @Override
    protected void doRun() throws Exception{

        long startTime = System.currentTimeMillis();

        //获取提交的时间间隔
        long interval = ChannelInfoManager.getInstance().getSubmitInterval(channelID);
        //获取提交的数据
        BusinessRouteValue businessRouteValue = CacheBaseService.getSubmitFromMiddlewareCache(channelID);
        try {
            if (businessRouteValue != null) {
                // 发送多媒体信息,获取响应信息
                // 获取通道接口参数
                Map<String, String> resultMap = ChannelInterfaceUtil.getArgMap(channelID);
                String loginname = resultMap.get("login-name");
                String loginnpass = resultMap.get("login-pass");

                JSONObject jsonobject = new JSONObject();
                // 获取平台模板id
                String templateId = businessRouteValue.getAccountTemplateID();
                // 获取通道模板id
                String channelTemplateID= AccountChannelTemplatelnfoManager.getInstance().getChannelTemplateID(templateId);
                if(channelTemplateID==null){
                    return;
                }

                String signaTureNonce = DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_MILLI)+ MyStringUtils.getRandom(10);
                String timesTamp =DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_MILLI);
                jsonobject.put("templateId", channelTemplateID);
                //订单号，成功后的订单不能重复
                jsonobject.put("orderNo", signaTureNonce);
                //业务账号
                jsonobject.put("account", loginname);
                //时间戳
                jsonobject.put("timestamp", timesTamp );
                String  extNumber="";
                if(StringUtils.isNotEmpty(businessRouteValue.getAccountExtendCode())) {
                    extNumber= businessRouteValue.getAccountExtendCode().substring(0, 4);
                    jsonobject.put("extNumber",extNumber);
                }
                List<String> PhonesList = new ArrayList<String>();
                //获取通道模板标识,1表示普通模板,2 表示变量模板
                if (String.valueOf(FixedConstant.TemplateFlag.COMMON_TEMPLATE.ordinal()).equals(AccountChannelTemplatelnfoManager .getInstance().getTemplateFlag(templateId))) {
                    PhonesList.add(businessRouteValue.getPhoneNumber());
                    jsonobject.put("content", PhonesList);
                } else {
                    String item=businessRouteValue.getPhoneNumber()+"|"+businessRouteValue.getMessageContent();
                    PhonesList.add(item);
                    jsonobject.put("content", PhonesList);
                }

                String url = resultMap.get("url")+"/message/sendMultimediaMessageByTemplate";
                String signature= MyStringUtils.getSignaTure(signaTureNonce, loginname, timesTamp, loginnpass);
                Map<String, String> headerMap=new HashMap<>();
                headerMap.put("Content-Type", "application/json; charset=utf-8");
                headerMap.put("signature-nonce", signaTureNonce);
                headerMap.put("account", loginname);
                headerMap.put("signature", signature);

                String response = HttpClientUtil.doRequest(url ,headerMap,jsonobject.toString(),TIMEOUT,RESPONSE_TIMEOUT);

                //维护通道运行状态
                ChannellnteractiveStatusManager.getInstance().process(channelID, response);
                BusinessRouteValue newBusinessRouteValue = businessRouteValue.clone();
                newBusinessRouteValue.setChannelSubmitSRCID(extNumber);
                newBusinessRouteValue.setChannelSubmitTime(DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_STANDARD_MILLI));

                if (StringUtils.isNotEmpty(response) && response.contains("code") && response.contains("data")) {
                    JSONObject json = JSONObject.parseObject(response);
                    JSONObject jsonData = JSONObject.parseObject(json.getString("data"));
                    newBusinessRouteValue.setNextNodeErrorCode(json.getString("code"));
                    newBusinessRouteValue.setChannelMessageID(jsonData.getString("msgId"));
                } else {
                    newBusinessRouteValue.setNextNodeErrorCode(InsideStatusCodeConstant.FAIL_CODE);
                }


                // 添加响应消息到队列
                responseWorker.add(newBusinessRouteValue);
                logger.info(new StringBuilder().append("提交信息")
                                .append("{}accountID={}")
                                .append("{}phoneNumber={}")
                                .append("{}messageContent={}")
                                .append("{}channelID={}")
                                .append("{}accountTemplateID={}")
                                .append("{}jsonobject={}").toString(),
                        FixedConstant.SPLICER, businessRouteValue.getAccountID(),
                        FixedConstant.SPLICER, businessRouteValue.getPhoneNumber(),
                        FixedConstant.SPLICER, businessRouteValue.getMessageContent(),
                        FixedConstant.SPLICER, businessRouteValue.getChannelID(),
                        FixedConstant.SPLICER, businessRouteValue.getAccountTemplateID(),
                        FixedConstant.SPLICER, jsonobject.toString());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        long endTime = System.currentTimeMillis();
        controlSubmitSpeed(interval, (endTime - startTime));

    }

    public void exit() {
        //停止线程
        responseWorker.exit();
        super.exit();
        // 维护通道运行状态
        ChannelRunStatusManager.getInstance().process(channelID,
                String.valueOf(FixedConstant.ChannelRunStatus.ABNORMAL.ordinal()));
    }

}
