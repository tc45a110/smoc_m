// Decompiled by DJ v2.9.9.60 Copyright 2000 Atanas Neshkov  Date: 2005-7-11 18:01:11
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3)
// Source File Name:   SGIPSMProxy.java

package com.huawei.smproxy;

import java.util.concurrent.BlockingQueue;

import com.base.common.log.CategoryLog;
import com.base.common.util.DateUtil;
import com.huawei.insa2.comm.sgip.SGIPMTConnection;
import com.huawei.insa2.comm.sgip.message.SGIPMessage;
import com.huawei.insa2.comm.sgip.message.SGIPUnbindMessage;

/**
 * 对外提供的API接口。
 */
public class SGIPMTProxy
{
	
  /**
   * 收发消息使用的连接对象。
   */
  private SGIPMTConnection conn;
  
  private int node;
  
  private BlockingQueue<SGIPMessage> sendSubmitRespQueue;
  
  private String channelID;
  
  private String index;
  
  public SGIPMTProxy(String channelID, String index,
		BlockingQueue<SGIPMessage> sendSubmitRespQueue) {
	  this.channelID = channelID;
	  this.index = index;
	  this.sendSubmitRespQueue = sendSubmitRespQueue;
	  //使用通道数字部分作为node
	  StringBuilder sb = new StringBuilder();
	  for(int i=0; i< channelID.length() ;i++){
		  if(channelID.charAt(i)>=48 && channelID.charAt(i)<=57){
			  sb.append(channelID.charAt(i));
		  }
	  }
	  if(sb.length() > 0 ){
		  node = Integer.parseInt(sb.toString());
	  }else{
		  node = 1000;
	  }
	 
  }



  public boolean isHealth(){
	return conn.isHealth();
  }
  
  /**
   * 是否存在连接
   * @return
   */
  public boolean isConnecting(){
	  if(conn == null){
		  return false;
	  }
	 return conn.isConnecting();
  }
  
  public synchronized void connect(){
	  conn = new SGIPMTConnection(channelID, index, this, sendSubmitRespQueue);
  }
  

    /**
     * 发送消息，阻塞直到收到响应或超时。
     * 返回为收到的消息
     * @exception PException 超时或通信异常。
     */
    public int send(SGIPMessage message)
    {
    	message.setSrcNodeId(node);
    	message.setTimeStamp(Integer.parseInt(DateUtil.getCurDateTime("MMddHHmmss")));
    	message.setSequenceId(ProxyUtil.getSequence());
		boolean result = conn.send(message);
		if(result){
			return message.getSequenceId();
		}
		return 0;
    }
    
    public void unbind(String trigger){
    	if(conn != null && isConnecting()){
        	SGIPMessage message = new SGIPUnbindMessage();
    		int sequenceID = send(message);
    		try {
				Thread.sleep(50);
			} catch (Exception e) {
				CategoryLog.connectionLogger.error(e.getMessage(),e);
			}
    		conn.close((trigger+",sequenceID="+sequenceID),true);
    	}
    }

}