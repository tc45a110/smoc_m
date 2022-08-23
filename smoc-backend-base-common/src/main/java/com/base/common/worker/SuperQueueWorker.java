package com.base.common.worker;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.base.common.constant.FixedConstant;


/**
 * @desc
 * 处理队列线程超类
 */

public abstract class SuperQueueWorker<T> extends SuperWorker{
	
	/**
	 * 数据队列
	 */
	protected BlockingQueue<T> superQueue = new LinkedBlockingQueue<T>();
	
	public void add(T t){
		superQueue.add(t);
	}
	
	public T take() throws InterruptedException{
		return superQueue.take();
	}
	
	public T poll() throws InterruptedException{
		return superQueue.poll(FixedConstant.COMMON_POLL_INTERVAL_TIME,TimeUnit.SECONDS);
	}
	
	/**
	 * 获取集合大小
	 * @return
	 */
	protected int size(){
		return superQueue.size();
	}
}


