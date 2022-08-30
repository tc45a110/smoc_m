/**
* @desc
* 从通道表中按照优先级及时间先后获取数据，每次按照通道的速率进行获取，存入到队列中
*/
package com.inse.worker;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.base.common.cache.CacheBaseService;
import com.base.common.constant.FixedConstant;
import com.base.common.manager.ChannelInfoManager;
import com.base.common.manager.ChannelRunStatusManager;
import com.base.common.util.DateUtil;
import com.base.common.util.HttpClientUtil;
import com.base.common.vo.BusinessRouteValue;
import com.base.common.worker.SuperQueueWorker;
import com.inse.manager.AccountChanelTemplateInfoManager;
import com.inse.manager.ChannelInteractiveStatusManager;
import com.inse.util.ChannelInterfaceUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubmitPullWorker extends SuperQueueWorker<BusinessRouteValue> {
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
				String loginpass = resultMap.get("login-pass");

				JSONObject jsonobject = new JSONObject();
				// 获取平台模板id
				String templateId = businessRouteValue.getAccountTemplateID();
				// 获取通道模板id
				String channelTemplateID=AccountChanelTemplateInfoManager.getInstance().getChannelTemplateID(templateId);
				if(channelTemplateID==null){
					return;
				}
				jsonobject.put("MsgID", channelTemplateID);


				// 封装接口所需要的参数
				String data = DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_STANDARD_SECONDE);
				String authenticator = DigestUtils.md5Hex(loginname + data + loginpass).toUpperCase();
				List<String> PhonesList = new ArrayList<String>();
				PhonesList.add(businessRouteValue.getPhoneNumber());
				jsonobject.put("Phones", PhonesList);
				jsonobject.put("SiID", loginname);
				jsonobject.put("Date", data);
				jsonobject.put("Authenticator", authenticator);
				String url = null;


				//获取通道模板标识,1表示普通模板,2 表示变量模板
				if ("1".equals(AccountChanelTemplateInfoManager.getInstance().getTemplateFlag(templateId))) {

					jsonobject.put("Method", "send");
					url = resultMap.get("url") + "/sapi/send";

				} else {
					// 获取通道模板变量格式
					String option = AccountChanelTemplateInfoManager.getInstance().getChannelTemplateVariableFormat(templateId);
					// 获取变量值
					String dataList = businessRouteValue.getMessageContent();

					String[] datas = dataList.split("\\|");

					List<JSONObject> listcontent = new ArrayList<JSONObject>();
					JSONArray array = JSONObject.parseArray(option);

					for (int i = 0; i < array.size(); i++) {
						String str = array.get(i)+"";

						JSONObject jsonObject_ = JSONObject.parseObject(str);

						JSONObject jsonObject = new JSONObject();
						jsonObject.put("Frame", jsonObject_.get("Frame"));

						Map<String, String> paramMap = new HashMap<String, String>();
						JSONArray array_ = jsonObject_.getJSONArray("Param");

						for (Object object : array_) {
							int index = (Integer) object;
							paramMap.put(String.valueOf(index), datas[index]);
						}

						jsonObject.put("Param", paramMap);
						listcontent.add(jsonObject);

					}
					jsonobject.put("Method", "option");
					jsonobject.put("Content", listcontent);
					url = resultMap.get("url") + "/sapi/option";
				}

				// 连接超时时间
				int timeOut = (int) ChannelInfoManager.getInstance().getSubmitInterval(channelID);
				// 提交响应超时时间
				int reponseTimeout = (int) ChannelInfoManager.getInstance().getResponseTimeout(channelID);
				String response = HttpClientUtil.doRequest(url, jsonobject.toString(), timeOut,
						reponseTimeout);
				logger.info("响应消息={}",response);
				//维护通道运行状态
				ChannelInteractiveStatusManager.getInstance().process(channelID, response);
				BusinessRouteValue newBusinessRouteValue = businessRouteValue.clone();
				//获取账号扩展码
				String extend = AccountChanelTemplateInfoManager.getInstance().getAccountExtendCode(templateId);
				//获取通道接入码
				String channelSRCID = ChannelInfoManager.getInstance().getChannelSRCID(channelID);
				newBusinessRouteValue.setAccountExtendCode(extend);
				newBusinessRouteValue.setChannelSubmitSRCID(channelSRCID + extend);

				if (StringUtils.isNotEmpty(response) && response.contains("ResCode") && response.contains("TransID")) {
					JSONObject json = JSONObject.parseObject(response);
					newBusinessRouteValue.setNextNodeCode(json.getString("ResCode"));
					newBusinessRouteValue.setChannelMessageID(json.getString("TransID"));
					newBusinessRouteValue
							.setChannelSubmitTime(DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_STANDARD_MILLI));

				} else {
					newBusinessRouteValue.setNextNodeCode("2");
					newBusinessRouteValue
							.setChannelMessageID(DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_MILLI));
					newBusinessRouteValue
							.setChannelSubmitTime(DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_STANDARD_MILLI));
				}


				// 添加响应消息到队列
				responseWorker.add(newBusinessRouteValue);
				logger.info(new StringBuilder().append("提交信息")
								.append("{}accountID={}")
								.append("{}phoneNumber={}")
								.append("{}messageContent={}")
								.append("{}channelID={}")
								.append("{}accountTemplateID={}").toString(),
						FixedConstant.SPLICER, businessRouteValue.getAccountID(),
						FixedConstant.SPLICER, businessRouteValue.getPhoneNumber(),
						FixedConstant.SPLICER, businessRouteValue.getMessageContent(),
						FixedConstant.SPLICER, businessRouteValue.getChannelID(),
						FixedConstant.SPLICER, businessRouteValue.getAccountTemplateID());
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
