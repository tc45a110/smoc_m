/**
 * @desc
 * 
 */
package com.huawei.smproxy;

import com.huawei.insa2.comm.smgp.message.SMGPMessage;
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
	
	public abstract SMGPMessage onActive(SMGPMessage msg);
	
	public abstract SMGPMessage onDeliver(SMGPMessage msg);
	
	public abstract int send(SMGPMessage message) throws Exception;
	
	/**
	 * 连接终止的处理，由API使用者实现 SMC连接终止后，需要执行动作的接口
	 */
	public abstract void onTerminate(String trigger);
	
	public abstract boolean isHealth();
}


