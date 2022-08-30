/**
 * @desc
 * 
 */
package com.inse.manager;

import com.base.common.worker.SuperMapWorker;
import com.inse.server.handler.CallbackHTTPServer;

import java.util.HashSet;
import java.util.Set;

public class CallbackHTTPServerManager extends SuperMapWorker<String, CallbackHTTPServer> {

	private static CallbackHTTPServerManager manager = new CallbackHTTPServerManager();

	private CallbackHTTPServerManager() {
		this.start();
	}

	public static CallbackHTTPServerManager getInstance() {
		return manager;
	}

	public void doRun() throws Exception {
		sleep(INTERVAL);
	}

	public void maintain(String channelID,CallbackHTTPServer callbackHTTPServer){
		add(channelID,callbackHTTPServer);
	}

	/**
	 * 退出线程
	 */
	public void exit() {
		super.exit();
		Set<String> channelIDSet = new HashSet<String>(keySet());
		for (String channelID : channelIDSet) {
			exit(channelID);
		}
	}

	/**
	 * 将某个通道的线程停止
	 * 
	 * @param channelID
	 */
	public void exit(String channelID) {
		CallbackHTTPServer callbackHTTPServer = remove(channelID);
		if (callbackHTTPServer != null) {
			callbackHTTPServer.exit();
		}
	}

}
