/**
 * @desc
 * 
 */
package com.business.mo.server;

import com.base.common.constant.FixedConstant;
import com.base.common.manager.ExtendParameterManager;
import com.base.common.manager.ResourceManager;
import com.business.mo.worker.MainWorker;

public class MOServer {

	MainWorker mainWorker;
	
	public static void main(String[] args) {
		try {
			MOServer proxyServer = new MOServer();
			proxyServer.startup();
			
			EchoServer echoServer = new EchoServer(proxyServer);
			echoServer.startup();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("服务启动失败");
			System.exit(0);
		}
	}
	
	/**
	 * 启动服务
	 */
	private void startup(){

		System.out.println("服务正在启动");
		//初始化文件配置数据
		ResourceManager.getInstance();
		//初始化数据库配置数据
		ExtendParameterManager.getInstance();	
		
		mainWorker = new MainWorker();
		mainWorker.setName("MainWorker");
		mainWorker.start();
		System.out.println("服务已启动");

	}
	
	/**
	 * 停止服务
	 */
	public void stop(){
		mainWorker.exit();
		try {
			Thread.sleep(FixedConstant.COMMON_INTERVAL_TIME);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("服务已停止");
		System.exit(0);
	}

}


