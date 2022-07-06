/**
 * @desc
 * 
 */
package com.business.access.util;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.base.common.constant.FixedConstant;
import com.base.common.vo.BusinessRouteValue;

public class ThreadPoolExecutorFactory {
	static ThreadPoolExecutor threadPoolExecutor = null;
	
	static{
		//创建一个线程池
		threadPoolExecutor = new ThreadPoolExecutor(FixedConstant.CPU_NUMBER * 4, Integer.MAX_VALUE,
				100000L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
		threadPoolExecutor.prestartAllCoreThreads();
	}
	
	public static Future<BusinessRouteValue>  process(Callable<BusinessRouteValue> task){
		return threadPoolExecutor.submit(task);
	}

}


