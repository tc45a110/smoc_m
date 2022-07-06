/**
 * @desc
 * 
 */
package com.protocol.proxy.server;

import com.base.common.constant.FixedConstant;
import com.base.common.manager.ChannelInfoManager;
import com.base.common.manager.ExtendParameterManager;
import com.base.common.manager.ResourceManager;
import com.protocol.proxy.manager.SubmitPullWorkerManager;

public class ProxyServer {
	
	public static void main(String[] args) {
		try {
			ProxyServer proxyServer = new ProxyServer();
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
		//通道信息
		ChannelInfoManager.getInstance();
		//通道拉取服务
		SubmitPullWorkerManager.getInstance();
		System.out.println("服务已启动");

	}
	
	/**
	 * 停止服务
	 */
	public void stop(){
		try {
			SubmitPullWorkerManager.getInstance().exit();
			Thread.sleep(FixedConstant.COMMON_INTERVAL_TIME);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("服务已停止");
		System.exit(0);
	}

}


