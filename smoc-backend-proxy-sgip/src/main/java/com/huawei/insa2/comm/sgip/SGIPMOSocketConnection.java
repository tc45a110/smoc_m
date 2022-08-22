// Decompiled by DJ v2.9.9.60 Copyright 2000 Atanas Neshkov  Date: 2005-7-16 19:21:46
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   SGIPSocketConnection.java

package com.huawei.insa2.comm.sgip;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.base.common.log.CategoryLog;
import com.huawei.insa2.comm.PReader;
import com.huawei.insa2.comm.PWriter;
import com.huawei.insa2.comm.sgip.message.SGIPBindRepMessage;
import com.huawei.insa2.comm.sgip.message.SGIPMessage;
import com.huawei.insa2.comm.sgip.message.SGIPUnbindRepMessage;
import com.huawei.insa2.util.Resource;
import com.huawei.insa2.util.WatchThread;
import com.huawei.smproxy.SGIPMOProxy;
import com.protocol.proxy.manager.SGIPMOConnectionManager;

public abstract class SGIPMOSocketConnection
{		
	protected SGIPMOProxy proxy;
	
	protected boolean networkStatus = false;

    public SGIPMOSocketConnection()
    {
    }

	//上行和状态回执
    protected void init(Socket socket)
    {
        if(socket != null)
        {
            this.socket = socket;
            try
            {
            	this.socket.setSoTimeout(60*1000);
                out = getWriter(this.socket.getOutputStream());
                in = getReader(this.socket.getInputStream());
                host = socket.getInetAddress().getHostAddress();
                port = socket.getPort();
                name = host + ':' + port; 
                networkStatus = true;
            }
            catch(Exception ex)
            {
            	CategoryLog.connectionLogger.error(ex.getMessage(),ex);
            }
            //网络链接正常
            if(networkStatus){
                class ReceiveThread extends WatchThread
                {

                    public void task()
                    {
                        try
                        {
                            if(networkStatus)
                            {
                            	CategoryLog.connectionLogger.debug("in={}",in);
                            	SGIPMessage m = in.read();
                                if(m != null){
                                    onReceive(m);
                                }else{
                                	sleep(5);
                                }
                            }
                        }
                        catch(Exception ex)
                        {
                        	networkStatus = false;
                        	CategoryLog.connectionLogger.error(ex.getMessage(),ex);
                        	kill();
                        	close("exception");
                        }
                    }

    	            public ReceiveThread()
    	            {
    	                super(String.valueOf(String.valueOf(name)).concat("-receive-mo"));
    	            }
                }

                receiveThread = new ReceiveThread();
                receiveThread.start();
            }
        }
    }

    public boolean send(SGIPMessage message)
    {
		try {		
			out.write(message);
			return true;
		} catch (Exception ex) {
			CategoryLog.connectionLogger.error(ex.getMessage(),ex);
		}
		return false;
    }

    public String getName()
    {
        return name;
    }

    public String getHost()
    {
        return host;
    }

    public int getPort()
    {
        return port;
    }

    public int getReconnectInterval()
    {
        return reconnectInterval / 1000;
    }

    public String toString()
    {
        return String.valueOf(String.valueOf((new StringBuffer("PShortConnection:")).append(name).append('(').append(host).append(':').append(port).append(')')));
    }

    public int getReadTimeout()
    {
        return readTimeout / 1000;
    }

    public boolean available()
    {
        return error == null;
    }

    public String getError()
    {
        return error;
    }

    public Date getErrorTime()
    {
        return errorTime;
    }

    public synchronized void close(String trigger)
    {
        try
        {
            if(socket != null)
            {
            	CategoryLog.connectionLogger.info("关闭上行链接{},trigger={}",socket,trigger);
                socket.close();
             	networkStatus = false;
                in = null;
                out = null;
                socket = null;
                if(receiveThread != null)
                receiveThread.kill();
                SGIPMOConnectionManager.getInstance().remove(name);
            }
            
        }
        catch(Exception e) {
        	CategoryLog.connectionLogger.error(e.getMessage(),e);
        }

    }

    protected abstract PWriter getWriter(OutputStream outputstream);

    protected abstract PReader getReader(InputStream inputstream);

    protected void heartbeat()
        throws IOException
    {
    }

    public void initResource()
    {
        NOT_INIT = resource.get("comm/not-init");
        CONNECTING = resource.get("comm/connecting");
        RECONNECTING = resource.get("comm/reconnecting");
        CONNECTED = resource.get("comm/connected");
        HEARTBEATING = resource.get("comm/heartbeating");
        RECEIVEING = resource.get("comm/receiveing");
        CLOSEING = resource.get("comm/closeing");
        CLOSED = resource.get("comm/closed");
        UNKNOWN_HOST = resource.get("comm/unknown-host");
        PORT_ERROR = resource.get("comm/port-error");
        CONNECT_REFUSE = resource.get("comm/connect-refused");
        NO_ROUTE_TO_HOST = resource.get("comm/no-route");
        RECEIVE_TIMEOUT = resource.get("comm/receive-timeout");
        CLOSE_BY_PEER = resource.get("comm/close-by-peer");
        RESET_BY_PEER = resource.get("comm/reset-by-peer");
        CONNECTION_CLOSED = resource.get("comm/connection-closed");
        COMMUNICATION_ERROR = resource.get("comm/communication-error");
        CONNECT_ERROR = resource.get("comm/connect-error");
        SEND_ERROR = resource.get("comm/send-error");
        RECEIVE_ERROR = resource.get("comm/receive-error");
        CLOSE_ERROR = resource.get("comm/close-error");
    }

    protected static String NOT_INIT;
    protected static String CONNECTING;
    protected static String RECONNECTING;
    protected static String CONNECTED;
    protected static String HEARTBEATING;
    protected static String RECEIVEING;
    protected static String CLOSEING;
    protected static String CLOSED;
    protected static String UNKNOWN_HOST;
    protected static String PORT_ERROR;
    protected static String CONNECT_REFUSE;
    protected static String NO_ROUTE_TO_HOST;
    protected static String RECEIVE_TIMEOUT;
    protected static String CLOSE_BY_PEER;
    protected static String RESET_BY_PEER;
    protected static String CONNECTION_CLOSED;
    protected static String COMMUNICATION_ERROR;
    protected static String CONNECT_ERROR;
    protected static String SEND_ERROR;
    protected static String RECEIVE_ERROR;
    protected static String CLOSE_ERROR;
    private String error;
    protected Date errorTime;
    protected String name;
    protected String host;
    protected int port;
    protected PReader in;
    protected PWriter out;
    protected static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    protected int readTimeout;
    protected int reconnectInterval;
    protected Socket socket;
    protected WatchThread receiveThread;
    protected int transactionTimeout;
    protected Resource resource;
	


	public void onReceive(SGIPMessage message) {
		int sequence = message.getSequenceId();
		int command = message.getCommandId();
		CategoryLog.connectionLogger.info("接收:CommandId={},sequence={},host={},port={}",Integer.toHexString(command),sequence,host,port);
		SGIPMessage responseMessage = null;
		boolean closeFlag = false;
		if(command == SGIPConstant.Bind_Command_Id){
            responseMessage = new SGIPBindRepMessage(0);
		}else if(command == SGIPConstant.Unbind_Command_Id){
			responseMessage = new SGIPUnbindRepMessage();
			closeFlag = true;
		}else if(command == SGIPConstant.Deliver_Command_Id){
			responseMessage = proxy.onDeliver(message);
		}else if(command == SGIPConstant.Report_Command_Id){
			responseMessage = proxy.onReport(message);
		}else if(command == SGIPConstant.UserReport_Command_Id){
			responseMessage = proxy.onUserReport(message);
		}
		
		if(responseMessage != null){
            responseMessage.setSrcNodeId(message.getSrcNodeId());
            responseMessage.setTimeStamp(message.getTimeStamp());
			responseMessage.setSequenceId(sequence);
			boolean result = send(responseMessage);
			if(closeFlag){
				try {
					Thread.sleep(5);
				} catch (Exception e) {
					CategoryLog.connectionLogger.error(e.getMessage(),e);
				}
				close("client unbind result="+result);
			}
		}
		
	}

}