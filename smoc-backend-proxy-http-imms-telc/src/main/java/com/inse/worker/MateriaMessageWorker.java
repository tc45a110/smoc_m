package com.inse.worker;
import com.alibaba.fastjson.JSONObject;
import com.base.common.manager.AccountInfoManager;
import com.base.common.manager.ChannelInfoManager;
import com.base.common.manager.ResourceManager;
import com.base.common.worker.SuperQueueWorker;
import com.inse.manager.ChannelInteractiveStatusManager;
import com.inse.message.AccountTemplateInfo;
import com.inse.message.ResponseMessage;
import com.inse.util.ChannelInterfaceUtil;
import com.inse.util.DAO;
import com.inse.util.TemplateTransition;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.File;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
public class MateriaMessageWorker extends SuperQueueWorker<String> {
	private String channelID;
	public static String MMS_PATH = ResourceManager.getInstance().getValue("mms.resource.path");
	private static int TIMEOUT = ResourceManager.getInstance().getIntValue("timeout");
	private static int RESPONSE_TIMEOUT= ResourceManager.getInstance().getIntValue("response.timeout");

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
						//获取账号的扩展码
						String extend = AccountInfoManager.getInstance().getAccountExtendCode(businessAccount);

						// 按照通道模板规范提交模板信息
						Map<String, String> resultMap = TemplateTransition.getTemplate(
								accounttemplateinfo.getMmAttchnent(), channelID,
								accounttemplateinfo.getTemplateTitle(),extend);

						String response = submitTemplate(resultMap.get("mmdl"),resultMap.get("urlpath"), channelID);
						logger.info("响应消息={}",response);
						ChannelInteractiveStatusManager.getInstance().process(channelID,response);
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

								DAO.insertAccountChannelTemplateInfo(responsemessage, accounttemplateinfo, "3",
										channelID, options,extend);
								logger.info("平台模板ID={},通道模板ID={}",accounttemplateinfo.getTemplateId(),responsemessage.getMsgId());

							} else {
								ResponseMessage responsemessage = new ResponseMessage();
								responsemessage.setCode(object.getString("ResCode"));
								responsemessage.setMessage(object.getString("ResMsg"));
								DAO.insertAccountChannelTemplateInfo(responsemessage, accounttemplateinfo, "0",
										channelID, options,extend);
								logger.info("提交失败的平台模板ID={},失败原因={}",accounttemplateinfo.getTemplateId(),responsemessage.getMessage());

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
	 * @param
	 * @return
	 */
	private String submitTemplate(String jsonReqElements,String urlpath,String channelID) {

		CloseableHttpClient httpclient = null;
		CloseableHttpResponse response = null;
		String result = null;
		try {
			//获取通道接口参数
			Map<String, String> resultMap = ChannelInterfaceUtil.getArgMap(channelID);

			String urls=resultMap.get("url")+"/sapi/material";

			httpclient = HttpClients.createDefault();
			HttpPost httppost = new HttpPost(urls);
			httppost.setConfig(RequestConfig.custom().setConnectTimeout(TIMEOUT).setSocketTimeout(RESPONSE_TIMEOUT).build());
			// 将字符串转换成集合
			List<String> urlList = Arrays.asList(urlpath.split(","));

			StringBody stringBody = new StringBody(jsonReqElements, ContentType.APPLICATION_JSON);

			MultipartEntityBuilder builder = MultipartEntityBuilder.create()
			.addPart("Data", stringBody).setCharset(Charset.forName("UTF-8"));

			for (String url:urlList) {
				File uploadFile1 = new File(MMS_PATH+url);
				if(! uploadFile1.exists()){
					logger.error("文件不存在："+uploadFile1.getName());
					continue;
				}
				FileBody uploadFileBody1 = new FileBody(uploadFile1);
				builder.addPart(uploadFile1.getName(), uploadFileBody1);
			}

			// 生成 HTTP POST 实体
			HttpEntity reqEntity = builder.build();
			httppost.setEntity(reqEntity);

			response = httpclient.execute(httppost);
			HttpEntity resEntity = response.getEntity();
			if (resEntity != null) {
				result = EntityUtils.toString(resEntity);
			}
			EntityUtils.consume(resEntity);

		} catch (Exception e) {
			logger.error("提交多媒体素材异常：", e);
		} finally {

			try {
				if (response != null) {
					response.close();
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}

			try {
				if (httpclient != null) {
					httpclient.close();
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		return result;
	}

}
