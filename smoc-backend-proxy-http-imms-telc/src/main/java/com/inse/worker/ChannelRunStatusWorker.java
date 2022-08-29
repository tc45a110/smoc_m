package com.inse.worker;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.StringUtils;
import com.base.common.manager.ChannelRunStatusManager;
import com.base.common.worker.SuperConcurrentMapWorker;


public class ChannelRunStatusWorker extends SuperConcurrentMapWorker<String, Set<String>> {

	private static ChannelRunStatusWorker manager = new ChannelRunStatusWorker();

	public static ChannelRunStatusWorker getInstance() {
		return manager;
	}
	@Override
	protected void doRun() throws Exception {
		Thread.sleep(INTERVAL);
		mainTain();

	}

	public void process(String channelID, String response) {
		Set<String> channelRunStatusSet = superMap.get(channelID);
		if (channelRunStatusSet == null) {
			channelRunStatusSet = new HashSet<String>();
		}
		//如果通道有响应数据,并且响应数据符合接口响应数据格式,则通道正常
		if (StringUtils.isNotEmpty(response) &&response.contains("ResCode")&&response.contains("TransID")) {
			channelRunStatusSet.add("1");
			
		} else {
			channelRunStatusSet.add("2");
		}
		superMap.put(channelID, channelRunStatusSet);

	}

	public void mainTain() {
		if (superMap.size() > 0) {
			Map<String, Set<String>>  channelRunStatusMap=new ConcurrentHashMap<String, Set<String>>(superMap);
			for (String channelID : channelRunStatusMap.keySet()) {

				Set<String> set = channelRunStatusMap.get(channelID);
				superMap.remove(channelID);
				if (set.size() == 1) {
					
					//维护通道运行状态
					List<String> list=new ArrayList<String>(set);
					ChannelRunStatusManager.getInstance().process(channelID,list.get(0));

				} else if (set.size() ==2) {
					
					ChannelRunStatusManager.getInstance().process(channelID,"1");

				} 

			}

		}
	}

	public void exit() {
		// 停止线程
		super.exit();
	}

	
	
}
