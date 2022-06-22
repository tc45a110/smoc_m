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
import com.huawei.smproxy.Proxy;
import com.huawei.smproxy.SGIPMTProxy;
import com.protocol.proxy.util.ChannelInterfaceUtil;
import com.protocol.proxy.util.SgipUtil;

public class SubmitPullWorker extends SuperQueueWorker<SGIPMessage>{
	private String channelID;
	/**
	 * 链接序号
	 */
	private String index;
	private Proxy proxy;
	private ResponseWorker responseWorker;
	private ReportWorker reportWorker;
 	
	public SubmitPullWorker(String channelID,String index) {
		this.channelID = channelID;
		this.index = index;
		responseWorker = new ResponseWorker(channelID,index,superQueue);
		reportWorker = new ReportWorker(channelID, index);
		init();
		this.setName(new StringBuilder(channelID).append("-").append(index).toString());
		this.start();
	}
	
	/**
	 * 初始化:检查通道表
	 */
	private void init(){
		proxy = new SGIPMTProxy(channelID, index, superQueue);
	}


	@Override
	protected void doRun() throws Exception {
		long startTime = System.currentTimeMillis();
		//获取提交的时间间隔
		long interval = ChannelInfoManager.getInstance().getSubmitInterval(channelID);
		//连接正常、未知量没有超过滑动窗口
		if(proxy.isHealth() && !responseWorker.overGlideWindow()){
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
					startTime = System.currentTimeMillis();
					CategoryLog.messageLogger.info(messages[i].toString());
					int sequenceID = proxy.send(messages[i]);
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
			}
		}
		long endTime = System.currentTimeMillis();
		controlSubmitSpeed(interval,(endTime - startTime));
	}
	
	public void exit(){
		//停止线程
		super.exit();
		//释放链接
		proxy.onTerminate();
		//维护通道运行状态
		ChannelRunStatusManager.getInstance().process(channelID, String.valueOf(FixedConstant.ChannelRunStatus.ABNORMAL.ordinal()));
		responseWorker.exit();
		responseWorker.interrupt();
		reportWorker.exit();
		reportWorker.interrupt();
	}
	
}


