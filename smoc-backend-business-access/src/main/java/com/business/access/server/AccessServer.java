/**
 * @desc
 * 
 */
package com.business.access.server;


import com.base.common.constant.FixedConstant;
import com.base.common.log.AccessBusinessLogManager;
import com.base.common.manager.AccountChannelManager;
import com.base.common.manager.ExtendParameterManager;
import com.base.common.manager.ResourceManager;
import com.business.access.manager.BusinessWorkerManager;
import com.business.access.manager.ContentRouteManager;
import com.business.access.manager.EnterpriseFlagManager;
import com.business.access.manager.ExternalBlacklistFilterPersistenceWorkerManager;
import com.business.access.manager.ExternalBlacklistFilterWorkerManager;
import com.business.access.manager.InsideFilterWorkerManager;
import com.business.access.manager.ReportPullWorkerManager;
import com.business.access.worker.MainWorker;

public class AccessServer {
	
	MainWorker mainWorker;
	
	public static void main(String[] args) {
		try {
			AccessServer accessServer = new AccessServer();
			accessServer.startup();
			
			EchoServer echoServer = new EchoServer(accessServer);
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
		long startTime = System.currentTimeMillis();
		//初始化文件配置数据
		ResourceManager.getInstance();
		//初始化数据库配置数据
		ExtendParameterManager.getInstance();		
		//启动日志服务
		AccessBusinessLogManager.getInstance();
		//维护临时表
		EnterpriseFlagManager.getInstance();
		//外部黑名单的过滤服务
		ExternalBlacklistFilterWorkerManager.getInstance();
		//内部过滤服务
		InsideFilterWorkerManager.getInstance();
		//启动状态报告拉取服务
		ReportPullWorkerManager.getInstance();
		//账号通道对应关系服务
		AccountChannelManager.getInstance();
		//内容路由加载
		ContentRouteManager.getInstance();
		//业务线程启动
		BusinessWorkerManager.getInstance();
		
		mainWorker = new MainWorker();
		mainWorker.setName("MainWorker");
		mainWorker.start();
		long interval = System.currentTimeMillis() - startTime;
		System.out.println("服务已启动,耗时"+interval+"毫秒");

	}
	
	/**
	 * 停止服务
	 */
	public void stop(){
		try {
			mainWorker.exit();
			ExternalBlacklistFilterPersistenceWorkerManager.getInstance().exit();
			//增加缓存的判断，当缓存数据为0时，则可以安心退出
			Thread.sleep(FixedConstant.COMMON_INTERVAL_TIME*3);
			System.out.println("业务数据缓存队列数量:"+BusinessWorkerManager.getInstance().getSize());
			System.out.println("内部过滤缓存队列数量:"+InsideFilterWorkerManager.getInstance().getSize());
			System.out.println("外部过滤缓存队列数量:"+ExternalBlacklistFilterWorkerManager.getInstance().getSize());
			System.out.println("外部过滤持久化队列数量:"+ExternalBlacklistFilterPersistenceWorkerManager.getInstance().getSize());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("服务已停止");
		System.exit(0);
	}
	
}


