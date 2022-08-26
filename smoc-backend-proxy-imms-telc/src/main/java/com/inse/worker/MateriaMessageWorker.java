package com.inse.worker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import com.alibaba.fastjson.JSONObject;
import com.base.common.manager.ChannelInfoManager;
import com.base.common.util.HttpClientUtil;
import com.base.common.worker.SuperQueueWorker;
import com.inse.message.ResponseMessage;
import com.inse.message.AccountTemplateInfo;
import com.inse.util.ChannelInterfaceUtil;
import com.inse.util.DAO;
import com.inse.util.TemplateTransition;

public class MateriaMessageWorker extends SuperQueueWorker<String> {
	private static Logger logger = Logger.getLogger(MateriaMessageWorker.class);
	private String channelID;
	private ChannelRunStatusWorker channelRunStatusWorker;

	public MateriaMessageWorker(String channelID) {
		this.channelID = channelID;
		channelRunStatusWorker=new ChannelRunStatusWorker();
		channelRunStatusWorker.start();
	}

	/**
	 *
	 */
	@Override
	protected void doRun() throws Exception {
		try {
			long startTime = System.currentTimeMillis();
			// 获取提交的时间间隔
			long interval = ChannelInfoManager.getInstance().getSubmitInterval(channelID);
			// 从数据库account_template_info和account_channel_template_info,获取平台多模板信息，按照通道素材组织格式提交

			List<AccountTemplateInfo> accounttemplateinfoList = DAO.getaccountTemplateInfo(channelID);
			if (accounttemplateinfoList.size() > 0) {
				for (AccountTemplateInfo accounttemplateinfo : accounttemplateinfoList) {
					startTime = System.currentTimeMillis();
					String businessAccount = accounttemplateinfo.getBusinessAccount();
					// 根据账号获取配置的通道是否包含该通道
					Set<String> channelIDs = DAO.getChannels(businessAccount);
					if (channelIDs.contains(channelID)) {

						// 按照通道模板规范提交模板信息
						Map<String, String> resultMap = TemplateTransition.getTemplate(
								accounttemplateinfo.getMmAttchnent(), channelID,
								accounttemplateinfo.getTemplateTitle());

						String response = submitTemplate(resultMap.get("mmdl"), channelID);
						logger.info("响应消息："+response);
						channelRunStatusWorker.process(channelID,response);
						String options = resultMap.get("options");
						
						if (StringUtils.isNotEmpty(response)&&response.contains("ResCode")) {
							JSONObject object = null;
							try {
								object = JSONObject.parseObject(response);

							} catch (Exception e) {
								logger.error(e.getMessage(), e);
							}

							if (object.containsKey("ResCode") && "1000".equals(object.getString("ResCode"))) {
								ResponseMessage responsemessage = new ResponseMessage();
								responsemessage.setMessage(object.getString("ResMsg"));
								responsemessage.setCode(object.getString("ResCode"));
								responsemessage.setMsgId(object.getString("MsgID"));

								DAO.insertAccountChannelTemplateInfo(responsemessage, accounttemplateinfo, "1",
										channelID, options);
								logger.info("保存模板" + accounttemplateinfo.getTemplateId() + ",通道模板"
										+ responsemessage.getMsgId());
							} else if (object.containsKey("ResCode") && object.containsKey("ResMsg")) {
								ResponseMessage responsemessage = new ResponseMessage();
								responsemessage.setCode(object.getString("ResCode"));
								responsemessage.setMessage(object.getString("ResMsg"));
								DAO.insertAccountChannelTemplateInfo(responsemessage, accounttemplateinfo, "8",
										channelID, options);
								logger.info("保存未通过模板" + accounttemplateinfo.getTemplateId() + ",通道模板"
										+ responsemessage.getMsgId());
							}
						}
					}
					long endTime = System.currentTimeMillis();
					controlSubmitSpeed(interval, (endTime - startTime));
				}
				return;
			}
			long endTime = System.currentTimeMillis();
			controlSubmitSpeed(interval, (endTime - startTime));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 按照通道规范提交多媒体素材
	 * @param jsonReqElements
	 * @param channelID
	 * @return
	 */
	private static String submitTemplate(String jsoninformation, String channelID) {
		// 获取通道接口参数
		Map<String, String> resultMap = ChannelInterfaceUtil.getArgMap(channelID);
		Map<String, String> map = new HashMap<String, String>();
		map.put("Content-Type", "multipart/form-data");
		// 连接超时时间
		int Timeout = (int) ChannelInfoManager.getInstance().getSubmitInterval(channelID);
		// 提交响应超时时间
		int reponseTimeout = (int) ChannelInfoManager.getInstance().getResponseTimeout(channelID);
		String url=resultMap.get("url")+"/sapi/material";

		return HttpClientUtil.doRequest(url, map, jsoninformation, Timeout, reponseTimeout);

	}

	public void exit() {
		// 停止线程
		super.exit();
	}
}
