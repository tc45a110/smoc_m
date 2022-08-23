/**
 * @desc
 * 
 */
package com.business.access.worker;

import java.util.concurrent.BlockingQueue;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.base.common.constant.FixedConstant;
import com.base.common.constant.InsideStatusCodeConstant;
import com.base.common.manager.AccountInfoManager;
import com.base.common.manager.AlarmManager;
import com.base.common.manager.BusinessDictionaryManager;
import com.base.common.manager.MessageSubmitFailManager;
import com.base.common.manager.ResourceManager;
import com.base.common.util.HttpClientUtil;
import com.base.common.vo.AlarmMessage;
import com.base.common.vo.BusinessRouteValue;
import com.base.common.worker.SuperCacheWorker;
import com.business.access.manager.AuditWorkerManager;

//调用内部过滤服务的线程，哪个线程处理的快
public class InsideFilterWorker extends SuperCacheWorker{
	
	private BlockingQueue<BusinessRouteValue> businessRouteValueQueue; 

	public InsideFilterWorker(
			BlockingQueue<BusinessRouteValue> businessRouteValueQueue) {
		super();
		this.businessRouteValueQueue = businessRouteValueQueue;
	}

	@Override
	protected void doRun() throws Exception {
		BusinessRouteValue businessRouteValue = businessRouteValueQueue.take();

		long startTime = System.currentTimeMillis();
		
		String requestBody = buildRequestBody(businessRouteValue);
		String requestURL = getRequestURL();
		int connectTimeout = ResourceManager.getInstance().getIntValue("inside.filter.request.connect.timeout");
		int socketTimeout = ResourceManager.getInstance().getIntValue("inside.filter.request.socket.timeout");
		
		String responseBody = HttpClientUtil.doRequest(requestURL, requestBody, connectTimeout, socketTimeout);
		
		ResponseValue responseValue = parseResponseBody(responseBody);
		
		//当错误码不为空代表被拦截
		if(StringUtils.isNotEmpty(responseValue.getErrorCode())){
			//TODO生成模拟状态报告
			businessRouteValue.setStatusCodeSource(FixedConstant.StatusReportSource.ACCESS.name());
			businessRouteValue.setStatusCode(responseValue.getCode());
			businessRouteValue.setSubStatusCode("");
			MessageSubmitFailManager.getInstance().process(businessRouteValue);
		}else{
			businessRouteValue.setAuditReason(responseValue.getMessage());
			businessRouteValue.setNextNodeCode(responseValue.getCode());
			AuditWorkerManager.getInstance().process(businessRouteValue);
		}
		
		long endTime = System.currentTimeMillis();		
		long costTime = endTime  - startTime;
		logger.info(
				new StringBuilder().append("内部过滤")
				.append("{}accountID={}")
				.append("{}phoneNumber={}")
				.append("{}messageContent={}")
				.append("{}channelID={}")
				.append("{}code={}")
				.append("{}errorCode={}")
				.append("{}message={}")
				.append("{}耗时={}")
				.toString(),
				FixedConstant.SPLICER,businessRouteValue.getAccountID(),
				FixedConstant.SPLICER,businessRouteValue.getPhoneNumber(),
				FixedConstant.SPLICER,businessRouteValue.getMessageContent(),
				FixedConstant.SPLICER,businessRouteValue.getChannelID(),
				FixedConstant.SPLICER,responseValue.getCode(),
				FixedConstant.SPLICER,responseValue.getErrorCode(),
				FixedConstant.SPLICER,responseValue.getMessage(),
				FixedConstant.SPLICER,costTime
				);
		controlSubmitSpeed(ResourceManager.getInstance().getLongValue("inside.filter.request.interval"),costTime);
	}
	
	/**
	 * 获取过滤服务的请求路径  获取方式:随机
	 * @return
	 */
	private String getRequestURL(){
		//构建过滤服务请求体
		String requestURL = ResourceManager.getInstance().getValue("inside.filter.request.url");
		if(StringUtils.isNotEmpty(requestURL)) {
			String [] urlArr = requestURL.split(FixedConstant.DATABASE_SEPARATOR);
			int index = (int)(urlArr.length * Math.random());
			return urlArr[index];
		}
		return "";
	}
	
	/**
	 * 构建请求体
	 * @param businessRouteValue
	 * @return
	 */
	private String buildRequestBody(BusinessRouteValue businessRouteValue){
		//构建过滤服务请求体
		JSONObject requestBody = new JSONObject();
		requestBody.put("phone", businessRouteValue.getPhoneNumber());
		requestBody.put("account", businessRouteValue.getAccountID());
		requestBody.put("message", businessRouteValue.getMessageContent());
		
		//多媒体业务类型不用传templateId的值
		String businessType = AccountInfoManager.getInstance().getBusinessType(businessRouteValue.getAccountID());
		String noTemplateBusinessTypes = ResourceManager.getInstance().getValue("inside.filter.request.no.template.businessType");
		if(StringUtils.isNotEmpty(noTemplateBusinessTypes) && noTemplateBusinessTypes.contains(businessType)){
			requestBody.put("templateId", "");
		}else{
			requestBody.put("templateId", businessRouteValue.getAccountTemplateID());
		}
		requestBody.put("carrier", businessRouteValue.getBusinessCarrier());
		requestBody.put("provinceCode", businessRouteValue.getAreaCode());
		requestBody.put("channelId", businessRouteValue.getChannelID());
		return requestBody.toJSONString();
	}
	
	/**
	 * 解析响应报文得到响应码或相关信息，当响应报文为空时，则代表无任何拦截
	 * @param responseBody
	 * @return
	 */
	private ResponseValue parseResponseBody(String responseBody){
		
		try {
			if(StringUtils.isNotEmpty(responseBody)){
				JSONObject jsonObject = JSON.parseObject(responseBody);
				String code = jsonObject.getString("code");
				String responseCode = BusinessDictionaryManager.getInstance().getFilterStatusCode(code);
				String message = jsonObject.getString("message");
				ResponseValue responseValue = null;
				
				if(InsideStatusCodeConstant.SUCCESS_CODE.equals(responseCode)) {
					//通过
					responseValue = new ResponseValue(responseCode, null, message);
				} else if (InsideStatusCodeConstant.StatusCode.AUDIT.name().equals(responseCode)){
					//进审
					responseValue = new ResponseValue(responseCode, null, message);
				}else {
					//未通过
					responseValue = new ResponseValue(responseCode, code, message);
				}
				return responseValue;
			}else{
				AlarmManager.getInstance().process(
						new AlarmMessage(AlarmMessage.AlarmKey.InsideFilter,new StringBuilder().append("响应为空").toString())
						);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			AlarmManager.getInstance().process(
					new AlarmMessage(AlarmMessage.AlarmKey.InsideFilter,e.getMessage())
					);
		}
		
		return new ResponseValue(InsideStatusCodeConstant.SUCCESS_CODE,null,null);
	}
	
	class ResponseValue{
		//成功或进审标识
		private String code;
		//失败码
		private String errorCode;
		private String message;
		public ResponseValue(String code, String errorCode, String message) {
			super();
			this.code = code;
			this.errorCode = errorCode;
			this.message = message;
		}
		public String getCode() {
			return code;
		}
		public String getErrorCode() {
			return errorCode;
		}
		public String getMessage() {
			return message;
		}
		
	}
	
}


