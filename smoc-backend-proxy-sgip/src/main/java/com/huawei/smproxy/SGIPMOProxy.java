// Decompiled by DJ v2.9.9.60 Copyright 2000 Atanas Neshkov  Date: 2005-7-11 18:01:11
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3)
// Source File Name:   SGIPSMProxy.java

package com.huawei.smproxy;

import java.net.Socket;

import com.base.common.log.CategoryLog;
import com.huawei.insa2.comm.sgip.SGIPMOConnection;
import com.huawei.insa2.comm.sgip.message.SGIPDeliverRepMessage;
import com.huawei.insa2.comm.sgip.message.SGIPMessage;
import com.huawei.insa2.comm.sgip.message.SGIPReportRepMessage;
import com.huawei.insa2.comm.sgip.message.SGIPUserReportRepMessage;
import com.protocol.proxy.manager.MOWorkerManager;
import com.protocol.proxy.manager.ReportWorkerManager;

/**
 * 对外提供的API接口。
 */
public class SGIPMOProxy
{
  /**
   * 收发消息使用的连接对象。
   */
  private SGIPMOConnection conn;
  
	public SGIPMOProxy()
    {
    	conn = new SGIPMOConnection(this);
    }
	
	public void onConnect(Socket socket){
		conn.attach(socket);
	}

    /**
     * 对收到消息的处理。API使用者应该重载本方法来处理来自短信中心的Deliver消息，
     * 并返回响应消息。这里缺省的实现是返回一个成功收到的响应。
     * @param msg 从短消息中心来的消息。
     * @return 应该回的响应，由API使用者生成。
     */
    public SGIPMessage onDeliver(SGIPMessage msg)
    {
    	MOWorkerManager.getInstance().process(msg);
        return new SGIPDeliverRepMessage(0);
    }

    /**
     * 对收到状态报告消息的处理。API使用者应该重载本方法来处理来自短信中心的Report消息，
     * 并返回响应消息。这里缺省的实现是返回一个成功收到的响应。
     * @param msg 从短消息中心来的消息。
     * @return 应该回的响应，由API使用者生成。
     */
    public SGIPMessage onReport(SGIPMessage msg)
    {
    	ReportWorkerManager.getInstance().process(msg);
        return new SGIPReportRepMessage(0);
    }

    /**
     * 对收到手机状态配置消息的处理。API使用者应该重载本方法来处理来自短信中心的UserReport消息，
     * 并返回响应消息。这里缺省的实现是返回一个成功处理的响应。
     * @param msg 从短消息中心来的消息。
     * @return 应该回的响应，由API使用者生成。
     */
    public SGIPMessage onUserReport(SGIPMessage msg)
    {
    	CategoryLog.messageLogger.info(msg.toString());
        return new SGIPUserReportRepMessage(0);
    }
   
}