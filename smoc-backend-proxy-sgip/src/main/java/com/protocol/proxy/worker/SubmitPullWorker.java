 /**
 * @desc
 * 从通道表中按照优先级及时间先后获取数据，每次按照通道的速率进行获取，存入到队列中
 */
package com.protocol.proxy.worker;

import org.apache.commons.lang3.ArrayUtils;

import com.base.common.cache.CacheBaseService;
import com.base.common.constant.FixedConstant;
import com.base.common.log.CategoryLog;
import com.base.common.manager.ChannelInfoManager;
import com.base.common.manager.ChannelRunStatusManager;
import com.base.common.util.ChannelMTUtil;
import com.base.common.util.DateUtil;
import com.base.common.vo.BusinessRouteValue;
import com.base.common.worker.SuperQueueWorker;
import com.huawei.insa2.comm.sgip.message.SGIPMessage;
import com.huawei.smproxy.SGIPMTProxy;
import com.protocol.proxy.util.ChannelInterfaceUtil;
import com.protocol.proxy.util.SgipUtil;

public class SubmitPullWorker extends SuperQueueWorker<SGIPMessage>{
	private String channelID;
	/**
	 * 链接序号
	 */
	private String index;
	private SGIPMTProxy proxy;
	private ResponseWorker responseWorker;
	private boolean bindFlag = true;
	//上一次提交时间
	private long lastSubmitTime = System.currentTimeMillis();
 	
	public SubmitPullWorker(String channelID,String index) {
		this.channelID = channelID;
		this.index = index;
		responseWorker = new ResponseWorker(channelID,index,superQueue);
		init();
		this.setName(new StringBuilder("SubmitPullWorker-").append(channelID).append("-").append(index).toString());
		this.start();
	}
	
	/**
	 * 初始化:检查通道表
	 */
	private void init(){
		proxy = new SGIPMTProxy(channelID, index, superQueue);
	}
	
	/**
	 * 发送之前确保连接成功
	 */
	private void bind(long interval) {
		while(bindFlag){
			try {
				//当连接未成功连接时需要先进行连接
				if(!proxy.isConnecting()){
					proxy.connect();
				}
				if(proxy.isHealth()){
					break;
				}
				Thread.sleep(interval);
			} catch (Exception e) {
				CategoryLog.connectionLogger.error(e.getMessage(),e);
			}
		}
	}


	@Override
	protected void doRun() throws Exception {
		long startTime = System.currentTimeMillis();
		//获取提交的时间间隔
		long interval = ChannelInfoManager.getInstance().getSubmitInterval(channelID);
		
		//未知量没有超过滑动窗口
		if( !responseWorker.overGlideWindow()){
			BusinessRouteValue businessRouteValue = CacheBaseService.getSubmitFromMiddlewareCache(channelID);
			
			if(businessRouteValue != null){
				String channelSubmitSRCID = ChannelMTUtil.getChannelSubmitSRCID(ChannelInterfaceUtil.getArgMap(channelID), businessRouteValue.getAccountExtendCode());
				SGIPMessage[] messages = SgipUtil.packageSubmit(ChannelInterfaceUtil.getArgMap(channelID), businessRouteValue.getMessageContent(), businessRouteValue.getPhoneNumber(),channelSubmitSRCID,businessRouteValue.getMessageFormat());
				if(ArrayUtils.isEmpty(messages)){
					return;
				}
				businessRouteValue.setChannelTotal(messages.length);
				businessRouteValue.setChannelSubmitSRCID(channelSubmitSRCID);
				String accountMessageIDs = businessRouteValue.getAccountMessageIDs();
				String[] accountMessageIDArray =accountMessageIDs.split(FixedConstant.SPLICER);
				for(int i = 0;i<messages.length ;i++){
					bind(interval);
					startTime = System.currentTimeMillis();
					CategoryLog.messageLogger.info(messages[i].toString());
					int sequenceID = proxy.send(messages[i]);
					if(sequenceID == 0){
						logger.error(new StringBuilder().append("提交信息失败")
								.append("{}accountID={}")
								.append("{}phoneNumber={}")
								.append("{}messageContent={}")
								.append("{}channelID={}")
								.append("{}channelTotal={}")
								.append("{}channelIndex={}")
								.append("{}sequenceID={}")
								.toString(),
								FixedConstant.SPLICER,businessRouteValue.getAccountID(),
								FixedConstant.SPLICER,businessRouteValue.getPhoneNumber(),
								FixedConstant.SPLICER,businessRouteValue.getMessageContent(),
								FixedConstant.SPLICER,businessRouteValue.getChannelID(),
								FixedConstant.SPLICER,businessRouteValue.getChannelTotal(),
								FixedConstant.SPLICER,businessRouteValue.getChannelIndex(),
								FixedConstant.SPLICER,sequenceID
								);
						proxy.unbind("send exception");
						break;
					}
					
					lastSubmitTime = System.currentTimeMillis();
					
					BusinessRouteValue newBusinessRouteValue = businessRouteValue.clone();
					newBusinessRouteValue.setChannelSubmitTime(DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_STANDARD_MILLI));
					newBusinessRouteValue.setChannelIndex(i+1);
					newBusinessRouteValue.setMessageIndex(i+1);
					if(accountMessageIDArray.length>i){
						newBusinessRouteValue.setAccountMessageIDs(accountMessageIDArray[i]);
					}
		
					responseWorker.setResponseTimeoutTask(sequenceID, newBusinessRouteValue);
					
					logger.info(new StringBuilder().append("提交信息")
							.append("{}accountID={}")
							.append("{}phoneNumber={}")
							.append("{}messageContent={}")
							.append("{}channelID={}")
							.append("{}channelTotal={}")
							.append("{}channelIndex={}")
							.append("{}sequenceID={}")
							.toString(),
							FixedConstant.SPLICER,newBusinessRouteValue.getAccountID(),
							FixedConstant.SPLICER,newBusinessRouteValue.getPhoneNumber(),
							FixedConstant.SPLICER,newBusinessRouteValue.getMessageContent(),
							FixedConstant.SPLICER,newBusinessRouteValue.getChannelID(),
							FixedConstant.SPLICER,newBusinessRouteValue.getChannelTotal(),
							FixedConstant.SPLICER,newBusinessRouteValue.getChannelIndex(),
							FixedConstant.SPLICER,sequenceID
							);
					long endTime = System.currentTimeMillis();		
					controlSubmitSpeed(interval,(endTime - startTime));
				}
				return;
			}else{			
				long closeInterval = Integer.parseInt(ChannelInterfaceUtil.getArgMap(channelID).get("closeInterval")) * 1000;
				long freeTime =  System.currentTimeMillis() - lastSubmitTime;
				//当接收到所有应答、离上一次提交时间超过30秒 可以关闭链接
				if(responseWorker.getSize() == 0 && freeTime >= closeInterval){
					if(proxy.isConnecting()){
						proxy.unbind("all replies received,freeTime="+freeTime);
					}
				}
			}
				
		}
		long endTime = System.currentTimeMillis();
		controlSubmitSpeed(interval,(endTime - startTime));
	}
	
	public void exit(){
		//停止线程
		super.exit();
		responseWorker.exit();
		bindFlag = false;
		//释放链接
		proxy.unbind("channel exit");
		//维护通道运行状态
		ChannelRunStatusManager.getInstance().process(channelID, String.valueOf(FixedConstant.ChannelRunStatus.ABNORMAL.ordinal()));
	}
	
}


