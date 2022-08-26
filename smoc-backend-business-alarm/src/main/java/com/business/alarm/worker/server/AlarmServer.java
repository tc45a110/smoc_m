/**
 * @desc
 * 
 */
package com.business.alarm.worker.server;
import com.base.common.manager.ExtendParameterManager;
import com.base.common.manager.ResourceManager;
import com.business.alarm.worker.QuartzContainer;


public class AlarmServer {	
	public static void main(String[] args) {
		try {
			AlarmServer proxyServer = new AlarmServer();
			proxyServer.startup();			
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
		//启动监控
		QuartzContainer.dispatcherTask();
		
		
		System.out.println("服务已启动");

	}
	
	
}


