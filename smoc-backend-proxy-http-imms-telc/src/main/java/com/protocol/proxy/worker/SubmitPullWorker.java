/**
 * @desc 从通道表中按照优先级及时间先后获取数据，每次按照通道的速率进行获取，存入到队列中
 */
package com.protocol.proxy.worker;
import com.alibaba.fastjson.JSONArray;
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
import com.protocol.proxy.manager.AccountChanelTemplateInfoManager;
import com.protocol.proxy.manager.ChannelInteractiveStatusManager;
import com.protocol.proxy.util.ChannelInterfaceUtil;
import org.apache.commons.codec.digest.DigestUtils;
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
        this.setName(new StringBuilder("SubmitPullWorker-").append(channelID).append("-").append(index).toString());
        this.start();
    }

    @Override
    protected void doRun() throws Exception {
        long startTime = System.currentTimeMillis();
        //获取提交的时间间隔
        long interval = ChannelInfoManager.getInstance().getSubmitInterval(channelID);
        //获取提交的数据
        BusinessRouteValue businessRouteValue = CacheBaseService.getSubmitFromMiddlewareCache(channelID);
        try {
            if (businessRouteValue != null) {
                // 发送多媒体信息,获取响应信息
                // 获取平台模板id
                String accountTemplateID = businessRouteValue.getAccountTemplateID();
                // 获取通道模板id
                String channelTemplateID = AccountChanelTemplateInfoManager.getInstance().getChannelTemplateID(accountTemplateID);
                if (channelTemplateID == null) {
                    logger.error("获取通道模板失败");
                     return;
                }
                Map<String, String> argMap = ChannelInterfaceUtil.getArgMap(channelID);

                String url = null;
                //获取通道模板标识,1表示普通模板,2 表示变量模板
                if (String.valueOf(FixedConstant.TemplateFlag.COMMON_TEMPLATE.ordinal()).equals(
                        AccountChanelTemplateInfoManager.getInstance().getTemplateFlag(accountTemplateID))) {
                    url = argMap.get("url") + "/sapi/send";
                } else {
                    url = argMap.get("url") + "/sapi/option";
                }
                String requestBody = requestBody(businessRouteValue,argMap,channelTemplateID);
                Map<String,String>  headerMap=new HashMap<>();
                headerMap.put("Content-Type","application/json");

                String response = HttpClientUtil.doRequest(url,headerMap,requestBody, TIMEOUT, RESPONSE_TIMEOUT);

                //维护通道运行状态
                ChannelInteractiveStatusManager.getInstance().process(channelID, response);
                BusinessRouteValue newBusinessRouteValue = businessRouteValue.clone();
                //获取账号扩展码
                String extendCode = AccountChanelTemplateInfoManager.getInstance().getAccountExtendCode(accountTemplateID);
                //获取通道接入码
                String channelSRCID = ChannelInfoManager.getInstance().getChannelSRCID(channelID);
                newBusinessRouteValue.setAccountExtendCode(extendCode);
                newBusinessRouteValue.setChannelSubmitSRCID(channelSRCID + extendCode);
                newBusinessRouteValue.setChannelSubmitTime(DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_STANDARD_MILLI));

                if (StringUtils.isNotEmpty(response) && response.contains("ResCode") && response.contains("TransID")) {
                    JSONObject json = JSONObject.parseObject(response);
                    newBusinessRouteValue.setNextNodeErrorCode(json.getString("ResCode"));
                    newBusinessRouteValue.setChannelMessageID(json.getString("TransID"));
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
                                .append("{}requestBody={}")
                                .append("{}url={}").toString(),
                        FixedConstant.SPLICER, businessRouteValue.getAccountID(),
                        FixedConstant.SPLICER, businessRouteValue.getPhoneNumber(),
                        FixedConstant.SPLICER, businessRouteValue.getMessageContent(),
                        FixedConstant.SPLICER, businessRouteValue.getChannelID(),
                        FixedConstant.SPLICER, businessRouteValue.getAccountTemplateID(),
                        FixedConstant.SPLICER, requestBody,
                        FixedConstant.SPLICER, url);

                logger.info(new StringBuilder().append("返回结果")
                        .append("{}response={}").toString(),
                        FixedConstant.SPLICER, response);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        controlSubmitSpeed(interval, (System.currentTimeMillis() - startTime));
    }

    private String requestBody(BusinessRouteValue businessRouteValue, Map<String, String> argMap, String channelTemplateID) {
        // 发送多媒体信息,获取响应信息
        // 获取通道接口参数
        String loginName = argMap.get("login-name");
        String loginPass = argMap.get("login-pass");

        JSONObject bodyJsonObject = new JSONObject();
        // 获取平台模板id
        String accountTemplateID = businessRouteValue.getAccountTemplateID();
        bodyJsonObject.put("MsgID", channelTemplateID);

        // 封装接口所需要的参数
        String data = DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_STANDARD_SECONDE);
        String authenticator = DigestUtils.md5Hex(loginName.concat(data).concat(loginPass)).toUpperCase();
        bodyJsonObject.put("Phones", new String[]{businessRouteValue.getPhoneNumber()});
        bodyJsonObject.put("SiID", loginName);
        bodyJsonObject.put("Date", data);
        bodyJsonObject.put("Authenticator", authenticator);

        //获取通道模板标识,1表示普通模板,2 表示变量模板
        if (String.valueOf(FixedConstant.TemplateFlag.COMMON_TEMPLATE.ordinal()).equals(
                AccountChanelTemplateInfoManager.getInstance().getTemplateFlag(accountTemplateID))) {
            bodyJsonObject.put("Method", "send");
        } else {
            bodyJsonObject.put("Method", "option");
            // 获取通道模板变量格式
            String channelTemplateVariableFormat = AccountChanelTemplateInfoManager.getInstance().getChannelTemplateVariableFormat(accountTemplateID);
            // 获取变量值
            String[] paramArr = businessRouteValue.getMessageContent().split("\\|");
            List<JSONObject> contentList = new ArrayList<JSONObject>();
            //构建变量参数
            JSONArray jsonArray = JSONObject.parseArray(channelTemplateVariableFormat);
            JSONObject jsonObject = null;
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject paramJSONObject = jsonArray.getJSONObject(i);
                jsonObject = new JSONObject();
                JSONArray variableParamJSONArray = paramJSONObject.getJSONArray("Param");

                Map<String, String> paramMap = new HashMap<String, String>();
                for (Object object : variableParamJSONArray) {
                    int index = (Integer) object;
                    paramMap.put(String.valueOf(index), paramArr[index]);
                }
                jsonObject.put("Frame", paramJSONObject.get("Frame"));
                jsonObject.put("Param", paramMap);
                contentList.add(jsonObject);
            }
            bodyJsonObject.put("Content", contentList);
        }
        return bodyJsonObject.toJSONString();
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
