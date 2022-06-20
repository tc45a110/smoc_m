// Decompiled by DJ v2.9.9.60 Copyright 2000 Atanas Neshkov  Date: 2005-7-11 18:01:14
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3)
// Source File Name:   SMProxy30.java

package com.huawei.smproxy;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import com.huawei.insa2.comm.cmpp.message.CMPPMessage;
import com.huawei.insa2.comm.cmpp30.CMPP30Connection;
import com.huawei.insa2.comm.cmpp30.message.CMPP30DeliverMessage;
import com.huawei.insa2.comm.cmpp30.message.CMPP30DeliverRepMessage;
import com.protocol.proxy.worker.ReportWorker;

// Referenced classes of package com.huawei.smproxy:
//            CMPP30EventAdapter

/**
 * 对外提供的API接口。
 */
public class SMProxy30 extends Proxy
{
	
	/**
	 * 收发消息使用的连接对象。
	 */
	private CMPP30Connection conn;
	
	public boolean isHealth(){
		return conn.isHealth();
	}

	/**
	 * 建立连接并登录。
	 * 
	 * @param args
	 *            保存建立连接所需的参数。
	 */
	public SMProxy30(String channelID,String index,BlockingQueue<CMPPMessage> sendSubmitRespQueue,ReportWorker reportWorker) {
		super.reportWorker = reportWorker;
		// 完成初始化和向ISMG登录等工作
		conn = new CMPP30Connection(channelID,index,this,sendSubmitRespQueue);
	}

	/**
	 * 发送消息，阻塞直到收到响应或超时。 返回为收到的消息
	 * 
	 * @exception PException
	 *                超时或通信异常。
	 */
	public int send(CMPPMessage message) throws IOException {
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
	 * 对收到消息的处理。由API使用者实现。缺省返回成功收到的响应
	 * 
	 * @param msg
	 *            从短消息中心来的消息。
	 * @return 应该回的响应，由API使用者生成。
	 */
	public CMPPMessage onDeliver(CMPPMessage msg) {
		reportWorker.process(msg);
		return new CMPP30DeliverRepMessage(((CMPP30DeliverMessage)msg).getMsgId(), 0,msg.getSequenceId());
	}

	/**
	 * 提供给外部调用获取TCP连接对象
	 */
	public CMPP30Connection getConn() {
		return conn;
	}
	
}