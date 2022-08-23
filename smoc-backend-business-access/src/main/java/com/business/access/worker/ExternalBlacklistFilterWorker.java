/**
 * @desc
 * 
 */
package com.business.access.worker;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.base.common.constant.FixedConstant;
import com.base.common.constant.InsideStatusCodeConstant;
import com.base.common.manager.AlarmManager;
import com.base.common.manager.BusinessDataManager;
import com.base.common.manager.MessageSubmitFailManager;
import com.base.common.manager.ResourceManager;
import com.base.common.util.HttpClientUtil;
import com.base.common.vo.AlarmMessage;
import com.base.common.vo.BusinessRouteValue;
import com.base.common.worker.SuperCacheWorker;
import com.business.access.manager.ChannelWorkerManager;

//调用外部过滤服务的线程
public class ExternalBlacklistFilterWorker extends SuperCacheWorker{
	
	private static String EXTERNAL_BLACKLIST_FILTER_REQUEST_URL = "external.blacklist.filter.request.url";
	private static String EXTERNAL_BLACKLIST_FILTER_REQUEST_CONNECT_TIMEOUT = "external.blacklist.filter.request.connect.timeout";
	private static String EXTERNAL_BLACKLIST_FILTER_REQUEST_SOCKET_TIMEOUT = "external.blacklist.filter.request.socket.timeout";
	private static String EXTERNAL_BLACKLIST_FILTER_REQUEST_PARAM = "external.blacklist.filter.request.param";
	private static String EXTERNAL_BLACKLIST_FILTER_REQUEST_INTERVAL = "external.blacklist.filter.request.interval";
	
	
	private BlockingQueue<BusinessRouteValue> businessRouteValueQueue; 

	public ExternalBlacklistFilterWorker(
			BlockingQueue<BusinessRouteValue> businessRouteValueQueue) {
		super();
		this.businessRouteValueQueue = businessRouteValueQueue;
	}

	@Override
	protected void doRun() throws Exception {
		BusinessRouteValue businessRouteValue = businessRouteValueQueue.take();

		long startTime = System.currentTimeMillis();
		
		String blackListLevelFiltering = BusinessDataManager.getInstance().getBlackListLevelFiltering(businessRouteValue.getAccountID());
		//进行二次判断的机制，当大量数据拥堵后，可以通过临时调整个别账号的过滤级别来提升处理速率
		if(StringUtils.isEmpty(blackListLevelFiltering) || !blackListLevelFiltering.startsWith("HIGH")){
			ChannelWorkerManager.getInstance().process(businessRouteValue);
			logger.info(
					new StringBuilder().append("黑名单过滤级别已调低")
					.append("{}accountID={}")
					.append("{}phoneNumber={}")
					.append("{}messageContent={}")
					.append("{}blackListLevelFiltering={}")
					.toString(),
					FixedConstant.SPLICER,businessRouteValue.getAccountID(),
					FixedConstant.SPLICER,businessRouteValue.getPhoneNumber(),
					FixedConstant.SPLICER,businessRouteValue.getMessageContent(),
					FixedConstant.SPLICER,blackListLevelFiltering
					);
			return;
		}
		//通过高等级黑名单过滤参数获取外部黑名单过滤配置参数
		String url = getExternalBlacklistFilterUrl(blackListLevelFiltering);
		String connectTimeout = getExternalBlacklistFilterConnectTimeout(blackListLevelFiltering);
		String  socketTimeout = getExternalBlacklistFilterSocketTimeout(blackListLevelFiltering);
		String param = getExternalBlacklistFilterParam(blackListLevelFiltering);
		
		String requestBody = buildRequestBody(businessRouteValue, param);
		
		String responseBody = HttpClientUtil.doRequest(url, requestBody, Integer.parseInt(connectTimeout), Integer.parseInt(socketTimeout));

		String errorCode = parseResponseBody(responseBody);
		
		//当错误码不为空代表被是黑名单
		if(StringUtils.isNotEmpty(errorCode)){
			//TODO生成模拟状态报告
			businessRouteValue.setStatusCodeSource(FixedConstant.StatusReportSource.ACCESS.name());
			businessRouteValue.setStatusCode(errorCode);
			businessRouteValue.setSubStatusCode("");
			MessageSubmitFailManager.getInstance().process(businessRouteValue);
		}else{
			ChannelWorkerManager.getInstance().process(businessRouteValue);
		}
		
		long endTime = System.currentTimeMillis();		
		long costTime = endTime  - startTime;
		logger.info(
				new StringBuilder().append("外部黑名单过滤")
				.append("{}accountID={}")
				.append("{}phoneNumber={}")
				.append("{}messageContent={}")
				.append("{}过滤结果={}")
				.append("{}耗时={}")
				.toString(),
				FixedConstant.SPLICER,businessRouteValue.getAccountID(),
				FixedConstant.SPLICER,businessRouteValue.getPhoneNumber(),
				FixedConstant.SPLICER,businessRouteValue.getMessageContent(),
				FixedConstant.SPLICER,errorCode,
				FixedConstant.SPLICER,(endTime-startTime)
				);
		controlSubmitSpeed(getExternalBlacklistFilterInterval(blackListLevelFiltering),costTime);
		
	}
	
	/**
	 * 构建请求体
	 * @param businessRouteValue
	 * @return
	 */
	private String buildRequestBody(BusinessRouteValue businessRouteValue,String param){
		//构建过滤服务请求体
		JSONObject requestBody = new JSONObject();
		requestBody.put("uuid", UUID.randomUUID().toString());
		requestBody.put("mobile", businessRouteValue.getPhoneNumber());
		requestBody.put("username", businessRouteValue.getAccountID());
		
		if(StringUtils.isNotEmpty(param)){
			String[] array = param.split(",");
			if(array.length > 0){
				requestBody.put("poly", array[0]);
			}
			//过滤类型如金融、游戏、教育、信用卡、股票、房地产
			if(array.length > 1){
				requestBody.put("type", array[1]);
			}
		}
		return requestBody.toJSONString();
	}
	
	/**
	 * 解析响应报文
	 * @param responseBody
	 * @return
	 */
	private String parseResponseBody(String responseBody){
		try {
			if(StringUtils.isNotEmpty(responseBody)){
				JSONObject jsonObject = JSON.parseObject(responseBody);
				String code = jsonObject.getString("flag");
				if("1".equals(code)){
					return InsideStatusCodeConstant.StatusCode.TBLACK.name();
				}
			}else{
				AlarmManager.getInstance().process(
						new AlarmMessage(AlarmMessage.AlarmKey.ExternalBlacklistFilter,new StringBuilder().append("响应为空").toString())
						);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			AlarmManager.getInstance().process(
					new AlarmMessage(AlarmMessage.AlarmKey.ExternalBlacklistFilter,e.getMessage())
					);
		}
		return "";
	}
	
	/**
	 * 获取外部黑名单过滤地址
	 * @param key
	 * @return
	 */
	private String getExternalBlacklistFilterUrl(String blackListLevelFiltering){
		String url = ResourceManager.getInstance().getValue(
				new StringBuilder(blackListLevelFiltering)
				.append(".")
				.append(EXTERNAL_BLACKLIST_FILTER_REQUEST_URL).toString()
				);
		if(StringUtils.isNotEmpty(url)){
			//调用地址可能包含多个，随机获取
			String[] array = url.split(",");
			int index = (int)(Math.random() * array.length);
			return array[index];
		}
		return ResourceManager.getInstance().getValue(EXTERNAL_BLACKLIST_FILTER_REQUEST_URL);
	}
	
	/**
	 * 获取外部黑名单过滤连接超时时间
	 * @param key
	 * @return
	 */
	private String getExternalBlacklistFilterConnectTimeout(String blackListLevelFiltering){
		String connectTimeout = ResourceManager.getInstance().getValue(
				new StringBuilder(blackListLevelFiltering)
				.append(".")
				.append(EXTERNAL_BLACKLIST_FILTER_REQUEST_CONNECT_TIMEOUT).toString()
				);
		if(StringUtils.isNotEmpty(connectTimeout)){
			return connectTimeout;
		}
		return ResourceManager.getInstance().getValue(EXTERNAL_BLACKLIST_FILTER_REQUEST_CONNECT_TIMEOUT);
	}
	
	/**
	 * 获取外部黑名单过滤响应超时时间
	 * @param key
	 * @return
	 */
	private String getExternalBlacklistFilterSocketTimeout(String blackListLevelFiltering){
		String socketTimeout = ResourceManager.getInstance().getValue(
				new StringBuilder(blackListLevelFiltering)
				.append(".")
				.append(EXTERNAL_BLACKLIST_FILTER_REQUEST_SOCKET_TIMEOUT).toString()
				);
		
		if(StringUtils.isNotEmpty(socketTimeout)){
			return socketTimeout;
		}
		return ResourceManager.getInstance().getValue(EXTERNAL_BLACKLIST_FILTER_REQUEST_SOCKET_TIMEOUT);
	}
	
	/**
	 * 获取外部黑名单过滤参数
	 * @param key
	 * @return
	 */
	private String getExternalBlacklistFilterParam(String blackListLevelFiltering){
	
		String param = ResourceManager.getInstance().getValue(
				new StringBuilder(blackListLevelFiltering)
				.append(".")
				.append(EXTERNAL_BLACKLIST_FILTER_REQUEST_PARAM).toString()
				);
		
		if(StringUtils.isNotEmpty(param)){
			return param;
		}
		return ResourceManager.getInstance().getValue(EXTERNAL_BLACKLIST_FILTER_REQUEST_PARAM);
	}
	
	/**
	 * 获取外部黑名单过滤间隔时间
	 * @param blackListLevelFiltering
	 * @return
	 */
	private long getExternalBlacklistFilterInterval(String blackListLevelFiltering){
		
		long interval = ResourceManager.getInstance().getLongValue(
				new StringBuilder(blackListLevelFiltering)
				.append(".")
				.append(EXTERNAL_BLACKLIST_FILTER_REQUEST_INTERVAL).toString()
				);
		
		if(interval > 0){
			return interval;
		}
		return ResourceManager.getInstance().getLongValue(EXTERNAL_BLACKLIST_FILTER_REQUEST_INTERVAL);
	}
	
}


