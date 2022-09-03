package com.protocol.proxy.worker;
import com.alibaba.fastjson.JSONObject;
import com.base.common.constant.DynamicConstant;
import com.base.common.constant.FixedConstant;
import com.base.common.manager.ResourceManager;
import com.base.common.util.DateUtil;
import com.base.common.util.HttpClientUtil;
import com.base.common.worker.SuperQueueWorker;
import com.protocol.proxy.manager.ChannellnteractiveStatusManager;
import com.protocol.proxy.message.AccountTemplatelnfo;
import com.protocol.proxy.message.ResponseMessage;
import com.protocol.proxy.util.ChannelInterfaceUtil;
import com.protocol.proxy.util.DAO;
import com.protocol.proxy.util.MyStringUtils;
import com.protocol.proxy.util.TemplateTransition;
import org.apache.commons.lang3.StringUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MateriaMessageWorker extends SuperQueueWorker<String> {

    private String channelID;
    private static int TIMEOUT = ResourceManager.getInstance().getIntValue("timeout");
    private static int RESPONSE_TIMEOUT = ResourceManager.getInstance().getIntValue("response.timeout");

    public MateriaMessageWorker(String channelID) {
        this.channelID = channelID;
    }
    /**
     *
     */
    @Override
    protected void doRun() throws Exception {
        try {
            long startTime = System.currentTimeMillis();

            // 从数据库account_template_info和account_channel_template_info,获取平台多模板信息，按照通道素材组织格式提交

            List<AccountTemplatelnfo> accounttemplateinfoList = DAO.getaccountTemplateInfo(channelID);
            if (accounttemplateinfoList.size() > 0) {
                for (AccountTemplatelnfo  accounttemplateinfo : accounttemplateinfoList) {
                    startTime = System.currentTimeMillis();
                    String businessAccount = accounttemplateinfo.getBusinessAccount();
                    // 根据账号获取配置的通道是否包含该通道
                    Set<String> channelIDs = DAO.getChannels(businessAccount);
                    if (channelIDs.contains(channelID)) {
                        String signaTureNonce = DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_MILLI)+ MyStringUtils.getRandom(10);
                        String timesTamp =DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_MILLI);
                        // 按照通道模板规范提交模板信息
                        Map<String, String> resultMap = TemplateTransition.getTemplate(
                                accounttemplateinfo.getMmAttchnent(), channelID,
                                accounttemplateinfo.getTemplateTitle(),signaTureNonce,timesTamp,accounttemplateinfo.getTemplateFlag());
                        String response = submitTemplate(resultMap.get("mmdl"),signaTureNonce,timesTamp, channelID);
                        logger.info("提交模板响应消息={}",response);
                        ChannellnteractiveStatusManager.getInstance().process(channelID,response);

                        if (StringUtils.isNotEmpty(response)&&response.contains("code")) {
                            JSONObject object = null;
                            try {
                                object = JSONObject.parseObject(response);

                            } catch (Exception e) {
                                logger.error(e.getMessage(), e);
                            }

                            if (object.containsKey("code") && DynamicConstant.RESPONSE_SUCCESS_CODE.equals(object.getString("code"))) {
                                JSONObject jsonData = JSONObject.parseObject(object.getString("data"));
                                ResponseMessage responsemessage = new ResponseMessage();
                                responsemessage.setMessage(object.getString("message"));
                                responsemessage.setCode(object.getString("code"));
                                responsemessage.setMsgId(jsonData .getString("templateId"));

                                DAO.insertAccountChannelTemplateInfo(responsemessage, accounttemplateinfo, String.valueOf(FixedConstant.TemplateStatus.NO_APPROVED.ordinal()), channelID);
                                logger.info("平台模板ID={},通道模板ID={}",accounttemplateinfo.getTemplateId(),responsemessage.getMsgId());

                            } else {
                                ResponseMessage responsemessage = new ResponseMessage();
                                responsemessage.setCode(object.getString("code"));
                                responsemessage.setMessage(object.getString("message"));
                                DAO.insertAccountChannelTemplateInfo(responsemessage, accounttemplateinfo, String.valueOf(FixedConstant.TemplateStatus.REJECT.ordinal()), channelID);
                                logger.info("提交失败的平台模板ID={},失败原因={}",accounttemplateinfo.getTemplateId(),responsemessage.getMessage());
                            }
                        }
                    }

                }

            }
            controlSubmitSpeed(INTERVAL, (System.currentTimeMillis() - startTime));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
    /**
     * 按照通道规范提交多媒体素材
     * @param
     * @return
     */
    private String submitTemplate(String jsonReqElements,String signaTureNonce,String timesTamp,String channelID) {
        // 获取通道接口扩展参数
        Map<String, String> resultMap = ChannelInterfaceUtil.getArgMap(channelID);
        String loginname= resultMap.get("login-name");
        String loginpass= resultMap.get("login-pass");
        String signature= MyStringUtils.getSignaTure(signaTureNonce, loginname, timesTamp, loginpass);
        Map<String, String> headerMap=new HashMap<>();
        headerMap.put("Content-Type", "application/json; charset=utf-8");
        headerMap.put("signature-nonce", signaTureNonce);
        headerMap.put("account", loginname);
        headerMap.put("signature", signature);

        String url = resultMap.get("url")+"/template/addMultimediaTemplate";
        return HttpClientUtil.doRequest(url,headerMap,jsonReqElements,TIMEOUT,RESPONSE_TIMEOUT);
    }

}
