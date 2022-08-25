package com.protocol.access.client;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.common.manager.BusinessDataManager;
import com.base.common.manager.ResourceManager;
import com.protocol.access.manager.ReportTimerTaskWorkerManager;
import com.protocol.access.sgip.SgipConstant;
import com.protocol.access.sgip.pdu.Connect;
import com.protocol.access.sgip.pdu.ConnectResp;
import com.protocol.access.sgip.pdu.Deliver;
import com.protocol.access.sgip.pdu.DeliverResp;
import com.protocol.access.sgip.pdu.Report;
import com.protocol.access.sgip.pdu.ReportResp;
import com.protocol.access.sgip.pdu.Request;
import com.protocol.access.sgip.pdu.SgipPDU;
import com.protocol.access.sgip.pdu.Terminate;
import com.protocol.access.sgip.pdu.TerminateResp;
import com.protocol.access.sgip.sms.ByteBuffer;
import com.protocol.access.util.ConnectionArgCache;
import com.protocol.access.util.SequenceUtil;
import com.protocol.access.util.TypeConvert;


public class Client extends Thread{
	private static final Logger logger = LoggerFactory
			.getLogger(Client.class);
	
	protected Timer timer;
	
	protected Map<String, TimerTask> taskMap = new HashMap<String, TimerTask>();
	protected BlockingQueue<SgipPDU> connectionSubmitRespQueue = new LinkedBlockingQueue<SgipPDU>();
	
	private ClientPool clientPool;
	
	private JobList jobList; 
	
	private boolean runFlag = true;
	
	private String username;
	
	Socket socket;
	DataInputStream in;
	OutputStream out;
	
	private ReaderWorker readerThread;
	
	/**
	 * 连接状�??
	 */
	private boolean health;
	
	private String host;
	private int port;
	private String loginName;
	private String loginPassword;
	
	public Client(String client,JobList jobList,ClientPool clientPool) {
		timer = new Timer("Timer-" + client);
		this.username = client;
		this.jobList = jobList;
		this.clientPool = clientPool;
		SubmitRespWorker submitRespWorker = new SubmitRespWorker();
		submitRespWorker.start();
		bind();
	}
	
	
	public void release(){
		if(readerThread != null){
			readerThread.setFlag(false);
		}
		health = false;
		runFlag = false;
		close();
		removePool();
	}
	
	@Override
	public void run() {
		while(runFlag){
			//连接
			if(health){
				long start = System.currentTimeMillis();
				Request submit = jobList.getJob();
				logger.info("获取数据,队列={},data={}",username,Hex.encodeHexString(submit.getData().getBuffer()));
				send(submit);
				int interval = ResourceManager.getInstance().getIntValue("sgip.client.push.interval");
				if(submit instanceof Terminate){
					if(readerThread != null){
						readerThread.setFlag(false);
					}
					//发起断链后暂停时�?
					interval = ResourceManager.getInstance().getIntValue("sgip.client.terminate.interval");
					try {
						Thread.sleep(interval);
					} catch (Exception e) {
						logger.error(e.getMessage(),e);
					}
					release();
					return;
				}
				checkSendTimesInSecond(start,interval);
			}else{
				try {
					Thread.sleep(100);
				} catch (Exception e) {
					logger.error(e.getMessage(),e);
				}
			}
		}
	}
	
	private void checkSendTimesInSecond(long start,long interval) {
		long l = System.currentTimeMillis() - start;
		if (l < interval) {
			try {
				Thread.sleep(interval - l);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	private void removePool(){
		if(clientPool != null){
			clientPool.freeClient(this);
		}
		
	}
	
	/**
	 * 当连接成功后�?始处理信�?
	 */
	private void active(){
		health = true;
		start();
	}


	public void bind(){
		Map<String,String> argMap = ConnectionArgCache.getArgMap(username);
		logger.info("连接参数{}",argMap);
		if(argMap != null){
			host = argMap.get("host");
			port = Integer.parseInt(argMap.get("port"));
			loginName = argMap.get("loginName");
			loginPassword = argMap.get("loginPassword");
			try {
				ExecutorService exec = Executors.newFixedThreadPool(2);
				socket = new Socket(host, port);
				int readTimeout = ResourceManager.getInstance().getIntValue("sgip.client.read.timeout");
				socket.setSoTimeout(readTimeout);
				in = new DataInputStream(socket.getInputStream());
				out = socket.getOutputStream();
				readerThread = new ReaderWorker(in,connectionSubmitRespQueue);
				exec.execute(readerThread);
				logger.info("网络连接成功,连接参数{}",argMap);
				Connect connect = new Connect();
				connect.setLoginName(loginName);
				connect.setLoginPassword(loginPassword);
				connect.setLoginType((byte)2);
				connect.setSequenceId(SequenceUtil.getSequence());
				
				String key = Hex.encodeHexString(connect.getSequenceNumber());
				ConnectionTask task = new ConnectionTask(key,connect);
				taskMap.put(key,task);
				logger.info("sequence={},task={}",key,task);
				//建立客户端连接等待连接响应时间
				long taskScheduleInterval = ResourceManager.getInstance().getIntValue("sgip.client.task.delay");
				timer.schedule(task, taskScheduleInterval);
				send(connect);
			} catch (IOException e) {
				logger.error(e.getMessage(),e);
				logger.info("网络连接失败,连接参数{}",argMap);
				release();
			}
		}else{
			removePool();
		}
	}
	
	public void close(){
		try {
			if(out != null){
				out.close();
			}
			if(in != null){
				in.close();
			}
			if(socket != null){
				socket.close();
			}

		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	public boolean send(Request request){
		boolean result = true;
		try {	
			if(request instanceof Report){
				logger.info("command={},sequence={},mobile={},submitSequenceNum={}",
						request.getCommandId(),Hex.encodeHexString(request.getSequenceNumber())
								,((Report)request).getUserNumber(),Hex.encodeHexString(((Report)request).getSubmitSequenceNum()));
				com.protocol.access.vo.Report reportVo = ((Report)request).getReport();
				
				//获取最大推送次数
				int maxPushTimes = BusinessDataManager.getInstance().getAccountReportRepeatPushTimes(reportVo.getAccountId());
				//当小于推送次数则增加定时任务保证成功接收
				if(reportVo.getReportPushTimes() < maxPushTimes) {
					ReportTimerTaskWorkerManager.getInstance().addReportTimeoutTask(reportVo,Hex.encodeHexString(request.getSequenceNumber()));
				}
			}else if(request instanceof Deliver){
				logger.info("command={},sequence={},mobile={},spNumber={},TpPid={},TpUdhi={}",
						request.getCommandId(),Hex.encodeHexString(request.getSequenceNumber())
						,((Deliver)request).getUserNumber(),((Deliver)request).getSpNumber(),((Deliver)request).getTpPid(),((Deliver)request).getTpUdhi());
			}else{
				logger.info("command={},sequence={}",
						request.getCommandId(),Hex.encodeHexString(request.getSequenceNumber()));
			}
			out.write(request.getData().getBuffer());
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			health = false;
			close();
			removePool();
		}
		return result;
	}
	
	class SubmitRespWorker extends Thread {

		@Override
		public void run() {
			logger.info(this.getName() + "启动");
			while (true) {
				try {
					SgipPDU message = connectionSubmitRespQueue.take();
					String key = Hex.encodeHexString(message.getSequenceNumber());
					logger.info("sequence="+key+"收到submit响应,command="+Integer.toHexString(message.getCommandId()));
					ConnectionTask task = (ConnectionTask)taskMap.remove(key);
					logger.info("sequence={},task={}",key,task);
					if(task != null ){
						task.cancel();
						if(message instanceof ConnectResp){
							ConnectResp rsp = (ConnectResp) message;
							logger.info("连接响应[" + host + ":" + port+"("+loginName+")"+ "]" + rsp.dump());
							//连接成功将数据添加池
							if(rsp.getResult() == 0){
								active();
							}
						} 
					}

				} catch (Exception e) {
					logger.error("Worker error " + e.getMessage(), e);
				}
			}

		}

	}
	
	//触发task则代表在规定时间内没有响应的sequenceid
	protected class ConnectionTask extends TimerTask {

		private String key;
		private long time;
		private Request message;

		public ConnectionTask(String key,Request message) {
			this.key = key;
			this.message = message;
			time = System.currentTimeMillis();
		}

		@Override
		public void run() {
			try {
				taskMap.remove(key);
				logger.warn("删除task=" + key+",command="+Integer.toHexString(message.getCommandId()));
				release();
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
			}
		}
		
		public long getTime() {
			return time;
		}

	}
	
	class ReaderWorker implements Runnable{	
		private DataInputStream in;
		private BlockingQueue<SgipPDU> connectionSubmitRespQueue;
		public  ReaderWorker(DataInputStream in,BlockingQueue<SgipPDU> connectionSubmitRespQueue){
			this.in=in;
			this.connectionSubmitRespQueue = connectionSubmitRespQueue;
		}
		private boolean flag = true;
		public void run(){
			try{
				while(flag){
			        int total_Length = in.readInt();
			        logger.info("total_Length={}",total_Length);
			        if(total_Length > 1024*4){
			        	throw new IOException("too large");
			        }
			        int command_Id = in.readInt();
			        
			        logger.info("command_Id={}",Integer.toHexString(command_Id));
			        byte buf[] = new byte[total_Length - 8];
			        in.readFully(buf);
			        byte[] fullBuf = new byte[total_Length];
			        byte[] temp = TypeConvert.int2byte(total_Length);
			        System.arraycopy(temp, 0, fullBuf, 0, temp.length);
			        temp = TypeConvert.int2byte(command_Id);
			        System.arraycopy(temp, 0, fullBuf, 4, temp.length);
			        
			        System.arraycopy(buf, 0, fullBuf, 8, buf.length);
			        
			        logger.info("读取的完整流："+Hex.encodeHexString(fullBuf));
			        if(command_Id == SgipConstant.SGIP_BIND_RESP){
			        	ConnectResp resp = new ConnectResp();
			        	resp.setData(new ByteBuffer(fullBuf));
			        	logger.info("command={},sequence={}",resp.getCommandId(),Hex.encodeHexString(resp.getSequenceNumber()));
			        	connectionSubmitRespQueue.add(resp);
			        }else if(command_Id == SgipConstant.SGIP_DELIVER_RESP){
			        	DeliverResp resp = new DeliverResp();
			        	resp.setData(new ByteBuffer(fullBuf));
			        	logger.info("command={},sequence={}",resp.getCommandId(),Hex.encodeHexString(resp.getSequenceNumber()));
			        }else if(command_Id == SgipConstant.SGIP_REPORT_RESP){
			        	ReportResp resp = new ReportResp();
			        	resp.setData(new ByteBuffer(fullBuf));
			        	logger.info("command={},sequence={}",resp.getCommandId(),Hex.encodeHexString(resp.getSequenceNumber()));
			        	ReportTimerTaskWorkerManager.getInstance().removeReportTaskByResponse(Hex.encodeHexString(resp.getSequenceNumber()));
			        }else if(command_Id == SgipConstant.SGIP_UNBIND_RESP){
			        	TerminateResp resp = new TerminateResp();
			        	resp.setData(new ByteBuffer(fullBuf));
			        	logger.info("command={},sequence={}",resp.getCommandId(),Hex.encodeHexString(resp.getSequenceNumber()));
			        }
				}

			}catch(Exception e){
				logger.error(e.getMessage(),e);
				release();
			}
				
		}
		
		public void setFlag(boolean flag) {
			this.flag = flag;
		}	
		
	}
}