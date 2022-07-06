package com.huawei.insa2.comm;

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

import com.base.common.constant.FixedConstant;
import com.base.common.log.CategoryLog;
import com.base.common.manager.ChannelRunStatusManager;
import com.base.common.manager.ResourceManager;
import com.huawei.insa2.comm.sgip.SGIPConstant;
import com.huawei.insa2.comm.sgip.message.SGIPBindMessage;
import com.huawei.insa2.comm.sgip.message.SGIPBindRepMessage;
import com.huawei.insa2.comm.sgip.message.SGIPDeliverMessage;
import com.huawei.insa2.comm.sgip.message.SGIPMessage;
import com.huawei.insa2.comm.sgip.message.SGIPReportMessage;
import com.huawei.insa2.comm.sgip.message.SGIPUnbindMessage;
import com.huawei.insa2.comm.sgip.message.SGIPUserReportMessage;
import com.huawei.insa2.util.Args;
import com.huawei.insa2.util.Resource;
import com.huawei.insa2.util.WatchThread;
import com.huawei.smproxy.Proxy;
import com.protocol.proxy.util.ChannelInterfaceUtil;

/**
 * 这是一个基于Socket通信的消息通信的增强类。提供比JDK原始Socket更高一级的功能，例如： 非阻塞通信，连接中断重连，改变连接属性立即生效。
 * 
 * @author 李大伟
 * @version 1.0
 */
public abstract class PSocketConnection{
	
	protected String source_addr = null;
	protected int version;// 双方协商的版本号
	protected String shared_secret;// 事先商定的值，用于生成SP认证码
	
	//通道id
	protected String channelID;
	//通道链路编号
	protected String index;
	  
	private int times=1;
	private int step=3;
	
	protected Proxy proxy;
	
	protected boolean health = false;
	protected boolean networkStatus = false;
	//非首次连接标识 false代表是首次连接
	private boolean notInitFlag = false;
	
	protected Timer timer;
	
	protected Map<Integer, TimerTask> taskMap = new HashMap<Integer, TimerTask>();
	protected BlockingQueue<SGIPMessage> connectionSubmitRespQueue = new LinkedBlockingQueue<SGIPMessage>();
	
	protected BlockingQueue<SGIPMessage> sendSubmitRespQueue;
	
	private int  localPort;
	
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

	/** 状态常量-尚未初始化。 */
	protected static String NOT_INIT;

	/** 状态常量-正在建立连接。 */
	protected static String CONNECTING;

	/** 状态常量-正在重新建立连接。 */
	protected static String RECONNECTING;

	/** 状态常量-建立连接成功。 */
	protected static String CONNECTED;

	/** 状态常量－正在心跳。 */
	protected static String HEARTBEATING;

	/** 状态常量－正在接收数据。 */
	protected static String RECEIVEING;

	/** 状态常量－正在关闭连接。 */
	protected static String CLOSEING;

	/** 状态常量－连接已经正常关闭。 */
	protected static String CLOSED;

	/** 运行期错误常量-不认识的地址。(无法得到IP地址，可能DNS查询失败或IP地址格式错误) */
	protected static String UNKNOWN_HOST;

	/** 运行期错误常量-端口号配置错误。(不是整数或范围不在0~65535内)。 */
	protected static String PORT_ERROR;

	/** 运行期错误常量-连接被拒绝。(目的主机存在，但在指定的端口上没有TCP监听)。 */
	protected static String CONNECT_REFUSE;

	/** 运行期错误常量-主机不可达。(地址正确，但路由不通或主机没开)。 */
	protected static String NO_ROUTE_TO_HOST;

	/** 运行期错误常量-接收数据超时。(接收数据时出现错误)。 */
	protected static String RECEIVE_TIMEOUT;

	/** 运行期错误常量-连接被对方关闭。 */
	protected static String CLOSE_BY_PEER;

	/** 运行期错误常量-连接被对方重置。 */
	protected static String RESET_BY_PEER;

	/** 运行期错误常量－连接已经关闭。 */
	protected static String CONNECTION_CLOSED;

	/** 错误类别常量-通信异常。 */
	protected static String COMMUNICATION_ERROR;

	/** 错误类别常量-连接异常。 */
	protected static String CONNECT_ERROR;

	/** 错误类别常量-发送数据异常。 */
	protected static String SEND_ERROR;

	/** 错误类别常量-接收数据异常。 */
	protected static String RECEIVE_ERROR;

	/** 错误类别常量-关闭连接异常。 */
	protected static String CLOSE_ERROR;

	/** 最近一次出错状态变化时刻。 */
	protected Date errorTime = new Date();

	/** 该连接的名称。 */
	protected String name;

	/** 连接目的主机(IP或者主机名或域名)。 */
	protected String host;

	/** 连接目的主机端口号。 */
	protected int port;

	/** 心跳间隔。 */
	protected int heartbeatInterval;

	/** 协议解码器。 */
	protected PReader in;

	/** 协议编码器。 */
	protected PWriter out;

	/** 屏幕输出的时间格式。 */
	protected static DateFormat df = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.SSS");

	/**
	 * Socket读超时时间。读时超过该时间没收到数据认为连接中断，重新尝试连接。单位毫秒，0表示
	 * 永不超时。其值至少应大于通信对方发送心跳间隔的时间，若对方不发心跳则该值应设置为0。
	 */
	protected int readTimeout;

	/** 重连间隔时间。单位毫秒，缺省值10秒。 */
	protected int reconnectInterval;

	/** 该连接对应的Socket。 */
	protected Socket socket;

	/** 心跳线程。若该协议不需发送心跳则该成员为null。 */
	protected WatchThread heartbeatThread;

	/** 接收线程。 */
	protected WatchThread receiveThread;

	/** 事务超时时间。在事务上调用waitResponse()方法时最长阻塞时间。单位毫秒。 */
	protected int transactionTimeout;

	/** 用来保存国际化字符串的资源对象。该成员必须由子类赋值。 */
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

	
	/**
	 * 初始化操作。
	 */
	protected void init(BlockingQueue<SGIPMessage> sendSubmitRespQueue) {
		setAttributes(new Args(ChannelInterfaceUtil.getArgMap(channelID)));
		
		timer = new Timer("Connection-Timer-" + channelID + "-" + index);
		this.sendSubmitRespQueue = sendSubmitRespQueue;
		
		final long mark = System.currentTimeMillis();
		
		SubmitRespWorker connectionSubmitRespWorker = new SubmitRespWorker();
		connectionSubmitRespWorker.setName("connectionSubmitRespWorker-" + channelID + "-" + index);
		connectionSubmitRespWorker.start();

		// 接收线程定义
		class ReceiveThread extends WatchThread {
			// convertTools cts=null;//字符编码转化
			public ReceiveThread() {
				super("receive-["+channelID+"-"+index+"]-"+mark);
				// cts=convertTools.getInstance();
			}

			public void task() {
				try {
					
					if (networkStatus) { // 如果网络状态正常则一直读取数据
						if (socket != null) { // 如果socket对象存在，在连接前先把它关闭。
							CategoryLog.connectionLogger.debug(channelID+"-"+index+",isConnected="+socket.isConnected()+",isClosed="+socket.isClosed());
						}
						SGIPMessage m = in.read();
						// logger.info("m="+m);
						if (m != null) {
							onReceive(m);
						}
					} else { // 连接不正常则进行重连
						if (notInitFlag) { // 如果不是第一次建立连接
							try {
								//ChannelInterruptManager.getInstance().add(String.valueOf(channelID),String.valueOf(index));
								int temp = times*step*1000;
								int interval =  temp> reconnectInterval?reconnectInterval:temp;
								CategoryLog.connectionLogger.info("重连次数="+times+",休眠时间="+interval+"毫秒");
								increaseTimes();
								sleep(interval); // 休眠一会儿，防止频繁连接
							} catch (Exception ex) {
								CategoryLog.connectionLogger.error(ex.getMessage(),ex);
							}
						}else{
							
						}
						connect();
					}
				} catch (Exception ex) {
					networkStatus = false;
					CategoryLog.connectionLogger.error(ex.getMessage(), ex);
				}
			}
		}
		;

		// 创建并启动接收线程
		receiveThread = new ReceiveThread();
		receiveThread.start();
		CategoryLog.connectionLogger.info("接收线程启动"+  receiveThread.getName());
		// 这里不建立连接，在接收线程里做，防止被阻塞
	}

	/**
	 * 设置连接属性。参数列表中没有的属性保持不变。改变的参数立即生效，只有当地址或端口号改变 时才会对连接进行重连。
	 * 
	 * @param args
	 *            新属性内容。
	 */
	public void setAttributes(Args args) {
		
		CategoryLog.connectionLogger.info("连接参数:"+args);
		host = args.get("host", "");
		port = Integer.parseInt(args.get("port", ""));
		
		name = host + ':' + port; 
		source_addr = args.get("login-name", "");
		version = args.get("version", 32);
		shared_secret = args.get("login-pass", "");
		

		// 读取数据最长等待时间，超过此时间没有读到数据认为连接中断。0表示永不超时。
		readTimeout = 1000 * args.get("read-timeout", 90);

		// 连接中断时重连间隔时间
		reconnectInterval = 1000 * args.get("reconnect-interval", 30);

		// 心跳间隔时间，0则不发送心跳
		heartbeatInterval = 1000 * args.get("heartbeat-interval", 30);

		// 事务超时时间
		transactionTimeout = 1000 * args.get("transaction-timeout", 10);

	}
	
	//上行和状态回执
    protected void init(Socket socket)
    {
        //resource = getResource();
    	resource = SGIPConstant.resource;
        initResource();
        error = NOT_INIT;
        if(socket != null)
        {
            this.socket = socket;
            try
            {
                out = getWriter(this.socket.getOutputStream());
                in = getReader(this.socket.getInputStream());
                setError(null);
            }
            catch(Exception ex)
            {
            	logger.error(ex);
                setError(String.valueOf(CONNECT_ERROR));
            }
            if(args != null)
                setAttributes1(args);
            class ReceiveThread1 extends WatchThread
            {

                public void task()
                {
                    try
                    {
                        if(error == null)
                        {
                            PMessage m = in.read();
                            if(m != null){
                            	lastReadtime = System.currentTimeMillis();
                                onReceive(m);
                            }
                            else
                            	logger.warn("数据读取为空");
                        }else{
                        	logger.error("错误信息:"+error);
                        	kill();
                        	onReadTimeOut();
                        }
                    }
                    catch(Exception ex)
                    {
                    	logger.error(ex.getMessage(),ex);
                    	//this.interrupt();
                    	//System.out.println(new Date()+","+this.getName()+"中断线程");
                        setError(explain(ex));
                        //当超时才进行连接关闭和释放
                        //if(error == SGIPSocketConnection.RECEIVE_TIMEOUT)
                        //{
                        	kill();
                            //setError(null);
                            onReadTimeOut();
                        //}
                    }
                }

            public ReceiveThread1()
            {
                super(String.valueOf(String.valueOf(name)).concat("-receive-mo"));
            }
            }

            receiveThread = new ReceiveThread1();
            receiveThread.start();
        }
    }

	/**
	 * 发送消息。
	 * 
	 * @param message
	 *            发送的消息。
	 */
	public void send(SGIPMessage message){

		try {
			
			CategoryLog.connectionLogger.debug("start write sequence="+message.getSequenceId());		
			out.write(message);
			CategoryLog.connectionLogger.debug("end write sequence="+message.getSequenceId());
			
			CategoryLog.connectionLogger.debug("start fireEvent  ,sequence="+message.getSequenceId());
			//fireEvent(new PEvent(PEvent.MESSAGE_SEND_SUCCESS, this, message));

			CategoryLog.connectionLogger.debug("end fireEvent ,sequence="+message.getSequenceId());
			
	
		} catch (Exception ex) {
			CategoryLog.connectionLogger.error(ex.getMessage(),ex);
		}
	}
	
	public void close(boolean flag) {

		try {
			SGIPUnbindMessage msg = new SGIPUnbindMessage();
			send(msg);
			CategoryLog.connectionLogger.info("关闭链接["+channelID+"-"+index+"],flag="+flag);
			if(flag){
				if (socket != null) {
					// Log.info(CLOSEING + this);

					// Socket关闭时其输入、输出流自动关闭，不必再调用流的关闭
					socket.close();
					in = null;
					out = null;
					socket = null;
				}
				// 杀死心跳线程和接收线程
				if (heartbeatThread != null) {
					heartbeatThread.kill();
				}
				receiveThread.kill();
				notInitFlag = false;
			}
		} catch (Exception ex) {
			CategoryLog.connectionLogger.error(ex.getMessage(),ex);
		}

	}

	/**
	 * 根据设置的参数初始化连接，可以是首次建立TCP连接，也可以是重连。
	 */
	protected synchronized void connect() {
		
		setAttributes(new Args(ChannelInterfaceUtil.getArgMap(channelID)));
		
		//每次重新获取连接参数
		notInitFlag = true;
		//进行网络层连接时，应用层连接肯定是不可用的
		health =false;
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
		
		SGIPBindMessage request = null;
		try {
			request = new SGIPBindMessage(1,source_addr,
					shared_secret);
			request.setSequenceId(proxy.getSequence());
			ConnectionTask task = new ConnectionTask(request.getSequenceId(), request);
			taskMap.put(request.getSequenceId(),task);
			timer.schedule(task, readTimeout);
			send(request);
		} catch (Exception e) {
			CategoryLog.connectionLogger.error(e.getMessage(), e);
			close(false);
		}
	}

	/**
	 * 取得协议编码器，由具体协议的子类实现。
	 * 
	 * @param out
	 *            编码后数据的输出流。
	 */
	protected abstract PWriter getWriter(OutputStream out);

	/**
	 * 取得协议解码器，由具体协议的子类实现。
	 * 
	 * @param in
	 *            解码器读取数据的输入流。
	 */
	protected abstract PReader getReader(InputStream in);
	
	public void onReceive(SGIPMessage message) {
		int sequence = message.getSequenceId();
		int command = message.getCommandId();
		CategoryLog.connectionLogger.info("接收:CommandId={},sequence={},localPort={}",Integer.toHexString(command),sequence,localPort);
		//Submit_Rep_Command_Id、Connect_Rep_Command_Id、Active_Test_Rep_Command_Id只需要接收

		if(command == SGIPConstant.Submit_Rep_Command_Id ){
			this.sendSubmitRespQueue.add(message);
		}else if(command == SGIPConstant.Bind_Rep_Command_Id ){
			this.connectionSubmitRespQueue.add(message);
		}else{	//需要写响应数据
			SGIPMessage responseMessage = null;
			//状态报告
			if(command == SGIPConstant.Deliver_Command_Id){
				responseMessage = proxy.onDeliver((SGIPDeliverMessage)message);
			}else if(command == SGIPConstant.Report_Command_Id){
				responseMessage = proxy.onReport((SGIPReportMessage)message);
			}else if(command == SGIPConstant.UserReport_Command_Id){
				responseMessage = proxy.onUserReport((SGIPUserReportMessage)message);
			}
			if(responseMessage != null){
				send(responseMessage);
			}
			
		}
		
	}
	
	class SubmitRespWorker extends Thread {

		@Override
		public void run() {
			CategoryLog.connectionLogger.info("启动");
			while (true) {
				try {
					SGIPMessage message = connectionSubmitRespQueue.take();
					CategoryLog.connectionLogger.info("响应:CommandId={},sequence={},localPort={}",Integer.toHexString(message.getCommandId()),message.getSequenceId(),localPort);
					ConnectionTask task = (ConnectionTask)taskMap.remove(message.getSequenceId());
					if(task != null ){
						task.cancel();
						if(message instanceof SGIPBindRepMessage){
							SGIPBindRepMessage rsp = (SGIPBindRepMessage) message;
							CategoryLog.connectionLogger.info("连接响应[" + host + ":" + port+"("+source_addr+")"+ "]["+channelID+"-"+index+"]" + rsp);
							if(rsp.getResult() == 0){
								health = true;
								resetTimes();
							}
						} 
					}

				} catch (Exception e) {
					CategoryLog.connectionLogger.error(e.getMessage(), e);
				}
			}

		}

	}

}
