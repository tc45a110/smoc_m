package com.protocol.access.sgip;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.InetSocketAddress;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.management.MBeanServer;
import javax.management.ObjectName;


import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.executor.OrderedThreadPoolExecutor;
import org.apache.mina.integration.jmx.IoServiceMBean;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.common.manager.ExtendParameterManager;
import com.base.common.manager.ResourceManager;
import com.protocol.access.manager.AuthCheckerManager;
import com.protocol.access.manager.AuthSubmitMessageManager;
import com.protocol.access.manager.MOManager;
import com.protocol.access.manager.ReportManager;
import com.protocol.access.manager.ReportPullManager;
import com.protocol.access.manager.SessionManager;


/**
 * TODO : Add documentation
 * 
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 * 
 */
public class MinaSgip extends IoHandlerAdapter {
	private static final Logger logger = LoggerFactory
			.getLogger(MinaSgip.class);
	
	//服务监听端口
	private static int PORT = 7892;
	private static final int BUFFER_SIZE = 8192;

	public static final String OPEN = "open";

	public SocketAcceptor acceptor;
	public SocketConnector connector;
	
	private static final ThreadFactory THREAD_FACTORY = new ThreadFactory() {
		public Thread newThread(final Runnable r) {
			return new Thread(null, r, "MinaThread", 64 * 1024);
		}
	};

	private OrderedThreadPoolExecutor executor;

	public static AtomicInteger sent = new AtomicInteger(0);

	public MinaSgip() throws IOException {
		
		if(ResourceManager.getInstance().getIntValue("receive.port") > 0){
			PORT = ResourceManager.getInstance().getIntValue("receive.port");
		}
		
		executor = new OrderedThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),128, 60, TimeUnit.SECONDS, THREAD_FACTORY);
		//executor = new OrderedThreadPoolExecutor();
		acceptor = new NioSocketAcceptor(Runtime.getRuntime().availableProcessors() + 1);
		acceptor.setReuseAddress(true);
		acceptor.getSessionConfig().setReceiveBufferSize(BUFFER_SIZE);
		//acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
		acceptor.getFilterChain().addLast("threadPool", new ExecutorFilter(executor));
		//acceptor.getFilterChain().addLast("threadPool",new ExecutorFilter(Executors.newCachedThreadPool()));
		acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new SgipProtocolCodecFactory()));

		MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
		IoServiceMBean acceptorMBean = new IoServiceMBean(acceptor);
		ObjectName acceptorName;
		try {
			acceptorName = new ObjectName(acceptor.getClass().getPackage()
					.getName()
					+ ":type=acceptor,name="
					+ acceptor.getClass().getSimpleName());
			mBeanServer.registerMBean(acceptorMBean, acceptorName);

			ResourceManager.getInstance();
			ExtendParameterManager.getInstance();
			AuthCheckerManager.getInstance();
			SessionManager.getInstance();
			AuthSubmitMessageManager.getInstance();
			MOManager.getInstance();
			ReportManager.getIntance();
			ReportPullManager.getIntance();

		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}

	}

	public synchronized void start() throws Exception {
		final InetSocketAddress socketAddress = new InetSocketAddress(
				"0.0.0.0", PORT);
		acceptor.setHandler(new SgipIoHandler());
		acceptor.bind(socketAddress);
		logger.info("MinaCmpp启动端口{}" ,PORT);
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) {
		if (!(cause instanceof IOException)) {
			logger.error("Exception: ", cause);
		} else {
			logger.info("I/O error: " + cause.getMessage());
		}
		session.closeNow();
	}

	public static void main(String[] args) throws Exception {
		new MinaSgip().start();
	}
}
