// Decompiled by DJ v2.9.9.60 Copyright 2000 Atanas Neshkov  Date: 2005-7-11 18:01:11
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3)
// Source File Name:   SGIPSMProxy.java

package com.huawei.smproxy;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import com.base.common.util.DateUtil;
import com.huawei.insa2.comm.sgip.SGIPConnection;
import com.huawei.insa2.comm.sgip.message.SGIPDeliverMessage;
import com.huawei.insa2.comm.sgip.message.SGIPDeliverRepMessage;
import com.huawei.insa2.comm.sgip.message.SGIPMessage;
import com.huawei.insa2.comm.sgip.message.SGIPReportMessage;
import com.huawei.insa2.comm.sgip.message.SGIPReportRepMessage;
import com.huawei.insa2.comm.sgip.message.SGIPUserReportMessage;
import com.huawei.insa2.comm.sgip.message.SGIPUserReportRepMessage;

/**
 * 对外提供的API接口。
 */
public class SGIPMTProxy extends Proxy
{
	
  /**
   * 收发消息使用的连接对象。
   */
  private SGIPConnection conn;
  
  private int node;
  
  public boolean isHealth(){
	return conn.isHealth();
  }
  
  /**
   * 建立连接并登录。
   * @param args 保存建立连接所需的参数。
   */
	public SGIPMTProxy(String channelID,String index,BlockingQueue<SGIPMessage> sendSubmitRespQueue)
    { 	
    	conn = new SGIPConnection(channelID, index, this, sendSubmitRespQueue);
    	node = 
    }

    /**
     * 发送消息，阻塞直到收到响应或超时。
     * 返回为收到的消息
     * @exception PException 超时或通信异常。
     */
    public int send(SGIPMessage message)
        throws IOException
    {
    	message.setSrcNodeId(src_nodeid);
    	message.setTimeStamp(Integer.parseInt(DateUtil.getCurDateTime("MMddHHmmss")));
    	message.setSequenceId(getSequence());
		conn.send(message);
		return message.getSequenceId();
    }

    /**
     * 连接终止的处理，由API使用者实现
     * SMC连接终止后，需要执行动作的接口
     */
    public void onTerminate()
    {
    	conn.close(true);
    }

    /**
     * 对收到消息的处理。API使用者应该重载本方法来处理来自短信中心的Deliver消息，
     * 并返回响应消息。这里缺省的实现是返回一个成功收到的响应。
     * @param msg 从短消息中心来的消息。
     * @return 应该回的响应，由API使用者生成。
     */
    public SGIPMessage onDeliver(SGIPDeliverMessage msg)
    {
        return new SGIPDeliverRepMessage(0);
    }

    /**
     * 对收到状态报告消息的处理。API使用者应该重载本方法来处理来自短信中心的Report消息，
     * 并返回响应消息。这里缺省的实现是返回一个成功收到的响应。
     * @param msg 从短消息中心来的消息。
     * @return 应该回的响应，由API使用者生成。
     */
    public SGIPMessage onReport(SGIPReportMessage msg)
    {
        return new SGIPReportRepMessage(0);
    }

    /**
     * 对收到手机状态配置消息的处理。API使用者应该重载本方法来处理来自短信中心的UserReport消息，
     * 并返回响应消息。这里缺省的实现是返回一个成功处理的响应。
     * @param msg 从短消息中心来的消息。
     * @return 应该回的响应，由API使用者生成。
     */
    public SGIPMessage onUserReport(SGIPUserReportMessage msg)
    {
        return new SGIPUserReportRepMessage(0);
    }

    /**
     * 提供给外部调用获取TCP连接对象
     */
    public SGIPConnection getConn()
    {
        return conn;
    }
   
}