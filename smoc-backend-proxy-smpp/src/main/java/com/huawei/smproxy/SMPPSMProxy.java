// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi
// Source File Name:   SMPPSMProxy.java

package com.huawei.smproxy;

import java.util.concurrent.BlockingQueue;

import com.huawei.insa2.comm.smpp.SMPPConnection;
import com.huawei.insa2.comm.smpp.message.SMPPDeliverMessage;
import com.huawei.insa2.comm.smpp.message.SMPPDeliverRespMessage;
import com.huawei.insa2.comm.smpp.message.SMPPEnquireLinkMessage;
import com.huawei.insa2.comm.smpp.message.SMPPEnquireLinkRespMessage;
import com.huawei.insa2.comm.smpp.message.SMPPMessage;

/**
 * 对外提供的API接口。
 */
public class SMPPSMProxy extends Proxy{
	/**
	 * 收发消息使用的连接对象。
	 */
	private SMPPConnection conn;
	
	public boolean isHealth(){
		return conn.isHealth();
	}

	/**
	 * 建立连接并登录。
	 * 
	 * @param args
	 *            保存建立连接所需的参数。
	 */
	public SMPPSMProxy(String channelID,String index,BlockingQueue<SMPPMessage> sendSubmitRespQueue,ReportWorker reportWorker) {
		super.reportWorker = reportWorker;
		conn = new SMPPConnection(channelID,index,this,sendSubmitRespQueue);
	}
	
	/**
	 * 接收心跳
	 */
	public SMPPMessage onActive(SMPPMessage msg) {
		return new SMPPEnquireLinkRespMessage(((SMPPEnquireLinkMessage)msg).getSequenceId());
	}
	
	
	public int send(SMPPMessage message) throws Exception {
		message.setSequenceId(getSequence());
		conn.send(message);
		return message.getSequenceId();
	}

	/**
	 * 连接终止的处理，由API使用者实现 SMC连接终止后，需要执行动作的接口
	 */
	public void onTerminate(String trigger) {
		conn.close(true,trigger);
	}

	/**
	 * 对收到消息的处理。API使用者应该重载本方法来处理来自短信中心的Deliver消息， 并返回响应消息。这里缺省的实现是返回一个成功收到的响应。
	 * 
	 * @param msg
	 *            从短消息中心来的消息。
	 * @return 应该回的响应，由API使用者生成。
	 */
	public SMPPMessage onDeliver(SMPPMessage msg) {
		reportWorker.process(msg);
		SMPPDeliverMessage smppDeliverMessage = ((SMPPDeliverMessage)msg);
		return new SMPPDeliverRespMessage(0,smppDeliverMessage.getSequenceId(),smppDeliverMessage.getMessageId());
	}

	/**
	 * 提供给外部调用获取TCP连接对象
	 */
	public SMPPConnection getConn() {
		return conn;
	}
    
}
