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
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.base.common.constant.FixedConstant;
import com.base.common.log.CategoryLog;
import com.base.common.manager.ChannelRunStatusManager;
import com.base.common.manager.ResourceManager;
import com.base.common.worker.SuperCacheWorker;
import com.huawei.insa2.comm.PReader;
import com.huawei.insa2.comm.PWriter;
import com.huawei.insa2.comm.sgip.message.SGIPBindMessage;
import com.huawei.insa2.comm.sgip.message.SGIPBindRepMessage;
import com.huawei.insa2.comm.sgip.message.SGIPMessage;
import com.huawei.insa2.comm.sgip.message.SGIPUnbindRepMessage;
import com.huawei.insa2.util.Args;
import com.huawei.insa2.util.Resource;
import com.huawei.insa2.util.WatchThread;
import com.huawei.smproxy.ProxyUtil;
import com.huawei.smproxy.SGIPMTProxy;
import com.protocol.proxy.util.ChannelInterfaceUtil;

public abstract class SGIPMTSocketConnection
{
	
	protected Map<Integer, TimerTask> taskMap = new HashMap<Integer, TimerTask>();
	protected BlockingQueue<SGIPMessage> connectionSubmitRespQueue = new LinkedBlockingQueue<SGIPMessage>();
	
	protected BlockingQueue<SGIPMessage> sendSubmitRespQueue;
	
	private int times=1;
	private int step=3;
	
	protected String channelID;
	//通道链路编号
	protected String index;
	
	private String login_name;
	private String login_pass;
	
	protected SGIPMTProxy proxy;
	
	protected boolean health = false;
	protected boolean networkStatus = false;
	//非首次连接标识 false代表是首次连接
	private boolean notInitFlag = false;
	
	  /**
	   * 开始连接标识
	   */
	protected boolean connecting  = false;
	
	protected Timer timer;
	SubmitRespWorker connectionSubmitRespWorker;
	
	//标识一个链接，当重连成功时，会更新
	protected void resetTimes(){
		//更新运行状态
		times = 1;
		ChannelRunStatusManager.getInstance().process(channelID, String.valueOf(FixedConstant.ChannelRunStatus.NORMAL.ordinal()));
	}
	
	protected void increaseTimes(){
		times++;
		int threshold = ResourceManager.getInstance().getIntValue("reconnect.times.threshold");
		if(threshold == 0){
			threshold = 5;
		}
		//当重连次数达到阈值时进行维护
		if(times == threshold){
			ChannelRunStatusManager.getInstance().process(channelID, String.valueOf(FixedConstant.ChannelRunStatus.ABNORMAL.ordinal()));
		}
	}

    protected void init(BlockingQueue<SGIPMessage> sendSubmitRespQueue)
    {
    	//setAttributes(new Args(ChannelInterfaceUtil.getArgMap(channelID)));
    	connecting = true;
    	
		timer = new Timer("Connection-Timer-" + channelID + "-" + index);

		this.sendSubmitRespQueue = sendSubmitRespQueue;
		
		connectionSubmitRespWorker = new SubmitRespWorker();
		connectionSubmitRespWorker.setName("connectionSubmitRespWorker-" + channelID + "-" + index);
		connectionSubmitRespWorker.start();
    	
        class ReceiveThread extends WatchThread
        {

            public void task()
            {
                try
                {
                    if(networkStatus)
                    {
                    	SGIPMessage m = in.read();
                        if(m != null)
                            onReceive(m);
                        else{
                        	sleep(5);
                        }
                    } else
                    {
                		//进行网络层连接时，应用层连接肯定是不可用的
                		health =false;          		
						if (notInitFlag) { // 如果不是第一次建立连接
							try {
								int temp = times*step*1000;
								int interval =  temp> reconnectInterval?reconnectInterval:temp;
								CategoryLog.connectionLogger.info("重连次数="+times+",休眠时间="+interval+"毫秒");
								increaseTimes();
								sleep(interval); // 休眠一会儿，防止频繁连接
							} catch (Exception ex) {
								CategoryLog.connectionLogger.error(ex.getMessage(),ex);
							}
						}
						connect();
                    }
                }
                catch(Exception ex) { 
                	health = false;
                	networkStatus = false;
					close("excepiton:"+ex.getMessage(),false);
					CategoryLog.connectionLogger.error(ex.getMessage(), ex);
                }
            }

            public ReceiveThread()
            {
            	super((channelID+"-"+index).concat("-receive-mt"));
            }
        }

        receiveThread = new ReceiveThread();
        receiveThread.start();
    }

    public void setAttributes(Args args)
    {
		CategoryLog.connectionLogger.info("连接参数:"+args);
		host = args.get("host", "");
		port = Integer.parseInt(args.get("port", ""));
		
		name = host + ':' + port; 
		login_name = args.get("login-name", "");
		login_pass = args.get("login-pass", "");
		

		// 读取数据最长等待时间，超过此时间没有读到数据认为连接中断。0表示永不超时。
		readTimeout = 1000 * args.get("read-timeout", 90);

		// 事务超时时间
		transactionTimeout = 1000 * args.get("transaction-timeout", 10);
		
		reconnectInterval = 1000 * 30;
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

    public synchronized void close(String trigger,boolean flag)
    {
    	CategoryLog.connectionLogger.info("关闭下行链接{},trigger={},flag={}",socket,trigger,flag);
        try
        {
            if(socket != null)
            {
                socket.close();            
                in = null;
                out = null;
                socket = null;
            }
            if(flag){
                timer.cancel();
                receiveThread.kill();
                connectionSubmitRespWorker.exit();
                notInitFlag = false;
                networkStatus = false;
                health = false;
                connecting = false;
            }
        }
        catch(Exception e) {
        	CategoryLog.connectionLogger.error(e.getMessage(),e);
        }
    }

    protected synchronized void connect()
    {
		setAttributes(new Args(ChannelInterfaceUtil.getArgMap(channelID)));
		notInitFlag = true;
		if (socket != null) { // 如果socket对象存在，在连接前先把它关闭。
			CategoryLog.connectionLogger.info(channelID+"-"+index+",isConnected="+socket.isConnected()+",isClosed="+socket.isClosed());
			try {
				socket.close();
			} catch (IOException ex) {
				CategoryLog.connectionLogger.error(ex.getMessage(), ex);
			}
		}
		networkStatus = false;
		// 尝试连接或重连
		try {
			if (port <= 0 || port > 65535) {
				CategoryLog.connectionLogger.error("无效端口:"+port);
				return;
			}

			socket = new Socket(host, port);
			
			// 读超时时间，单位毫秒，非负，=0表示不超时。read()方法超过此时间若还没有
			// 数据返回则抛出InterruptedIOException异常。用来处理SMP心跳超时。
			socket.setSoTimeout(readTimeout);
			// 如果加缓冲则在使用前嵌套一个BufferedXXXStream，即可在这里加，也可在
			// Reader和Writer的实现类里加。这里约定统一在编解码器处加，防止重复。
			out = getWriter(socket.getOutputStream());
			in = getReader(socket.getInputStream());
			
			localPort = socket.getLocalPort();
			CategoryLog.connectionLogger.info("建立网络连接:["+channelID+"-"+index+"][localport="+localPort+"]");
			networkStatus = true;
		} catch (Exception ex) {
			CategoryLog.connectionLogger.error("建立网络连接:["+channelID+"-"+index+"]", ex);
			networkStatus = false;
		}
		
		if (!networkStatus){
			return;
		}
		
		try {
			SGIPBindMessage request = new SGIPBindMessage(1, login_name, login_pass);
			request.setSequenceId(ProxyUtil.getSequence());
			ConnectionTask task = new ConnectionTask(request.getSequenceId(), request);
			taskMap.put(request.getSequenceId(),task);
			timer.schedule(task, readTimeout);
			boolean result = send(request);
			if(!result){
				close("send exception",false);
			}
		} catch (Exception e) {
			CategoryLog.connectionLogger.error(e.getMessage(), e);
			close("excepiton:"+e.getMessage(),false);
		}
    }

    protected abstract PWriter getWriter(OutputStream outputstream);

    protected abstract PReader getReader(InputStream inputstream);

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
    protected String localHost;
    protected int localPort;
    protected int heartbeatInterval;
    protected PReader in;
    protected PWriter out;
    protected static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    protected int readTimeout;
    protected int reconnectInterval;
    protected Socket socket;
    protected WatchThread receiveThread;
    protected int transactionTimeout;
    protected Resource resource;
	
	//触发task则代表在规定时间内没有响应的sequenceid
	protected class ConnectionTask extends TimerTask {

		private int key;
		private long time;
		private SGIPMessage message;

		public ConnectionTask(int key,SGIPMessage message) {
			this.key = key;
			this.message = message;
			time = System.currentTimeMillis();
		}

		@Override
		public void run() {
			try {
				taskMap.remove(key);
				CategoryLog.connectionLogger.warn("连接无响应sequence=" + key+",command="+Integer.toHexString(message.getCommandId()));
			} catch (Exception e) {
				CategoryLog.connectionLogger.error(e.getMessage(),e);
			}
		}
		
		public long getTime() {
			return time;
		}

	}


	public void onReceive(SGIPMessage message) {
		int sequence = message.getSequenceId();
		int command = message.getCommandId();
		CategoryLog.connectionLogger.info("接收:CommandId={},sequence={},localPort={}",Integer.toHexString(command),sequence,localPort);
		//Submit_Rep_Command_Id、Connect_Rep_Command_Id、Active_Test_Rep_Command_Id只需要接收
		SGIPMessage responseMessage = null;
		boolean closeFlag = false;
		if(command == SGIPConstant.Submit_Rep_Command_Id ){
			this.sendSubmitRespQueue.add(message);
		}else if(command == SGIPConstant.Bind_Rep_Command_Id){
			this.connectionSubmitRespQueue.add(message);
		}else if(command == SGIPConstant.Unbind_Command_Id){
			responseMessage = new SGIPUnbindRepMessage();
			closeFlag = true;
		}
		
		if(responseMessage != null){
            responseMessage.setSrcNodeId(message.getSrcNodeId());
            responseMessage.setTimeStamp(message.getTimeStamp());
			responseMessage.setSequenceId(sequence);
			send(responseMessage);
			if(closeFlag){
				try {
					Thread.sleep(5);
				} catch (Exception e) {
					CategoryLog.connectionLogger.error(e.getMessage(),e);
				}
				close("server unbind",true);
			}
		}
		
	}
	
	class SubmitRespWorker extends SuperCacheWorker {

		@Override
		protected void doRun() throws Exception {
			SGIPMessage message = connectionSubmitRespQueue.poll(FixedConstant.COMMON_POLL_INTERVAL_TIME,TimeUnit.SECONDS);
			if(message != null){
				CategoryLog.connectionLogger.debug("响应:CommandId={},sequence={},localPort={}",Integer.toHexString(message.getCommandId()),message.getSequenceId(),localPort);
				ConnectionTask task = (ConnectionTask)taskMap.remove(message.getSequenceId());
				if(task != null ){
					task.cancel();
					if(message instanceof SGIPBindRepMessage){
						SGIPBindRepMessage rsp = (SGIPBindRepMessage) message;
						CategoryLog.connectionLogger.info("连接响应[" + host + ":" + port+"("+login_name+")"+ "]["+channelID+"-"+index+"]" + rsp);
						if(rsp.getResult() == 0){
							health = true;
							resetTimes();
						}else{
							close("login fail",false);
						}
					} 
				}
			}
		}

	}

}