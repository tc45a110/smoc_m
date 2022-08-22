/**
 * @desc
 * 
 */
package com.huawei.smproxy;

import com.huawei.insa2.comm.cmpp.message.CMPPActiveMessage;
import com.huawei.insa2.comm.cmpp.message.CMPPActiveRepMessage;
import com.huawei.insa2.comm.cmpp.message.CMPPMessage;
import com.protocol.proxy.worker.ReportWorker;

public abstract class Proxy {
	private int base = 100000000;
	private int sequence=(int)(5*base*Math.random()+10*base);
	protected ReportWorker reportWorker;
	
	
	public synchronized int getSequence(){
		if(sequence == Integer.MAX_VALUE){
			return 10*base;
		}
		return sequence++;
	}
	
	public CMPPMessage onActive(CMPPMessage msg) {
		return new CMPPActiveRepMessage(0,((CMPPActiveMessage)msg).getSequenceId());
	}
	
	public abstract CMPPMessage onDeliver(CMPPMessage msg);
	
	public abstract int send(CMPPMessage message) throws Exception;
	
	/**
	 * 连接终止的处理，由API使用者实现 SMC连接终止后，需要执行动作的接口
	 */
	public abstract void onTerminate(String trigger);
	
	public abstract boolean isHealth();
}


