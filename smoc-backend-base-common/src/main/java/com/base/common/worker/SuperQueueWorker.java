package com.base.common.worker;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


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
	
	/**
	 * 获取集合大小
	 * @return
	 */
	protected int size(){
		return superQueue.size();
	}
}


