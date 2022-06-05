/**
 * @desc
 * @author ma
 * @date 2017年11月3日
 * 
 */
package com.base.common.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.base.common.constant.FixedConstant;
import com.base.common.vo.ChannelMO;
import com.base.common.worker.SuperQueueWorker;

public class LongSMSMOManager extends SuperQueueWorker<ChannelMO>{
	
	private static LongSMSMOManager manager = new LongSMSMOManager();
	
	public static LongSMSMOManager getInstance() {
		return manager;
	}

	/**
	 * 维护长短信匹配数据 key-序号-短信内容
	 */
	private Map<String,Map<Integer,ChannelMO>> longChannelMOMap = new HashMap<String, Map<Integer,ChannelMO>>();
	/**
	 * 维护指定时间不能合成的长短信
	 */
	private Map<String,TimerTask> taskMap = new HashMap<String,TimerTask>();
	/**
	 * 长短信合成线程
	 */
	CombinationWorker combinationWorker;
	
	Timer timer;
	
	private LongSMSMOManager(){
		timer = new Timer();
		combinationWorker = new CombinationWorker();
		combinationWorker.start();
		this.start();
	}
	
	/**
	 * 合成上行长短信
	 * @param channelMOMap
	 * @return
	 */
	public ChannelMO match(Map<Integer,ChannelMO> channelMOMap){
	
		ChannelMO channelMO = channelMOMap.get(1).clone();
		
		int size = channelMOMap.size();
		for(int i=2;i<=size;i++){
			channelMO.setMessageContent(channelMO.getMessageContent().concat(channelMOMap.get(i).getMessageContent()));
		}
		return channelMO;
	}

	@Override
	protected void doRun() throws Exception {
		ChannelMO  channelMO= superQueue.take();
		persistence(channelMO);	
	}
	
	/**
	 * 添加一次，执行一次match,match到的要删除
	 * @param channelMO
	 */
	private void persistence(ChannelMO channelMO){
		
		String key = 
				new StringBuilder(String.valueOf(channelMO.getMessageTotal()))
		.append(FixedConstant.SPLICER)
		.append(channelMO.getPhoneNumber())
		.append(FixedConstant.SPLICER)
		.append(channelMO.getMessageBatchNumber())
		.append(FixedConstant.SPLICER)
		.append(channelMO.getChannelMOSRCID())
		.append(FixedConstant.SPLICER)
		.append(channelMO.getChannelID())
		.toString();

		Map<Integer,ChannelMO>  channelMOMap= longChannelMOMap.get(key);
		if(channelMOMap == null){
			channelMOMap = new HashMap<Integer,ChannelMO>();
			longChannelMOMap.put(key, channelMOMap);
			TimerTask task = new RemindTask(key);				
			timer.schedule(task, 300000);
			taskMap.put(key,task);
		}
		
		if(!channelMOMap.containsKey(channelMO.getMessageNumber())){
			channelMOMap.put(channelMO.getMessageNumber(), channelMO);
		}else{
			logger.warn("上行长短信单条重复:{}",channelMO);
			return ;
		}
		
		//当满足条件则匹配成功
		if(channelMOMap.size() == Integer.parseInt(key.split(FixedConstant.SPLICER)[0])){
			combinationWorker.add(channelMOMap);
			longChannelMOMap.remove(key);
			TimerTask task = taskMap.remove(key);
			if(task != null){
				task.cancel();
			}
			logger.info("剩余未匹配长短信条数："+longChannelMOMap.size());
		}

	}
	
	class CombinationWorker extends SuperQueueWorker<Map<Integer,ChannelMO>>{

		@Override
		protected void doRun() throws Exception {
			Map<Integer,ChannelMO> channelMOMap = superQueue.take();
			ChannelMO channelMO = match(channelMOMap);
			ChannelMOManager.getInstance().add(channelMO);
		}
		
	}
	
	 class RemindTask extends TimerTask {
		 
		private String key;
		 
		public RemindTask(String key) {
			super();
			this.key = key;
		}

		@Override
		public void run() {
			taskMap.remove(key);
			Map<Integer,ChannelMO> temp = longChannelMOMap.remove(key);
			if(temp != null){
				for(Map.Entry<Integer,ChannelMO> entry:temp.entrySet()){
					ChannelMO channelMO = entry.getValue();
					logger.warn("上行长短信未合成{}",channelMO);
				}
			}
		}
		 
	 }
	
}


