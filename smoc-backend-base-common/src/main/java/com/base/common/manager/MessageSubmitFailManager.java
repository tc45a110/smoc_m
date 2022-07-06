package com.base.common.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.common.constant.FixedConstant;
import com.base.common.constant.InsideStatusCodeConstant;
import com.base.common.util.DateUtil;
import com.base.common.vo.BusinessRouteValue;
import com.base.common.worker.MessageSubmitFailWorker;
import com.base.common.worker.SuperQueueWorker;

/**
 * 提交信息失败管理
 */
public class MessageSubmitFailManager extends SuperQueueWorker<BusinessRouteValue>{
	private static final Logger logger = LoggerFactory
			.getLogger(MessageSubmitFailManager.class);
	private static MessageSubmitFailManager manager = new MessageSubmitFailManager();
	public static MessageSubmitFailManager getInstance(){
		return manager;
	}
	
	private MessageSubmitFailManager(){
		//启动cpu的数量*8的系数
		for(int i=0;i<FixedConstant.CPU_NUMBER*2;i++){
			MessageSubmitFailWorker messageSubmitFailWorker = new MessageSubmitFailWorker(superQueue);
			messageSubmitFailWorker.setName(new StringBuilder("MessageSubmitFailWorker-").append(i).toString());
			messageSubmitFailWorker.start();
		}
		this.start();
	}
	
	/**
	 * 处理没有成功下发到通道的信息
	 * @param businessRouteValue
	 */
	public void process(BusinessRouteValue businessRouteValue){
		businessRouteValue.setRouteLabel(FixedConstant.RouteLable.MR.name());
		businessRouteValue.setSuccessCode(InsideStatusCodeConstant.FAIL_CODE);
		businessRouteValue.setChannelReportTime(DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_STANDARD_MILLI));
		add(businessRouteValue);
	}
	
	@Override
	protected void doRun() throws Exception {
		logger.info("下发失败缓存队列数量{}",size());
		Thread.sleep(FixedConstant.COMMON_MONITOR_INTERVAL_TIME);
	}
	
}


