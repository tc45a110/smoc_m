// Decompiled by DJ v2.9.9.60 Copyright 2000 Atanas Neshkov  Date: 2005-7-11 18:01:14
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3)
// Source File Name:   SMProxy.java

package com.huawei.smproxy;

import java.util.concurrent.BlockingQueue;

import com.huawei.insa2.comm.cmpp.CMPPConnection;
import com.huawei.insa2.comm.cmpp.message.CMPPDeliverMessage;
import com.huawei.insa2.comm.cmpp.message.CMPPDeliverRepMessage;
import com.huawei.insa2.comm.cmpp.message.CMPPMessage;
import com.protocol.proxy.worker.ReportWorker;

// Referenced classes of package com.huawei.smproxy:
//            CMPPEventAdapter

/**
 * 对外提供的API接口。
 */
public class SMProxy extends Proxy{
	
	/**
	 * 收发消息使用的连接对象。
	 */
	private CMPPConnection conn;
	
	public boolean isHealth(){
		return conn.isHealth();
	}

	/**
	 * 建立连接并登录。
	 * 
	 * @param args
	 *            保存建立连接所需的参数。
	 */
	public SMProxy(String channelID,String index,BlockingQueue<CMPPMessage> sendSubmitRespQueue,ReportWorker reportWorker) {
		super.reportWorker = reportWorker;
		conn = new CMPPConnection(channelID,index,this,sendSubmitRespQueue);
	}
	
	public int send(CMPPMessage message) throws Exception {
		message.setSequenceId(getSequence());
		conn.send(message);
		return message.getSequenceId();
	}

	/**
	 * 连接终止的处理，由API使用者实现 SMC连接终止后，需要执行动作的接口
	 */
	public void onTerminate() {
		conn.close(true);
	}

	/**
	 * 对收到消息的处理。API使用者应该重载本方法来处理来自短信中心的Deliver消息， 并返回响应消息。这里缺省的实现是返回一个成功收到的响应。
	 * 
	 * @param msg
	 *            从短消息中心来的消息。
	 * @return 应该回的响应，由API使用者生成。
	 */
	public CMPPMessage onDeliver(CMPPMessage msg) {
		reportWorker.process(msg);
		return new CMPPDeliverRepMessage(((CMPPDeliverMessage)msg).getMsgId(), 0,msg.getSequenceId());
	}


	/**
	 * 提供给外部调用获取TCP连接对象
	 */
	public CMPPConnection getConn() {
		return conn;
	}
}