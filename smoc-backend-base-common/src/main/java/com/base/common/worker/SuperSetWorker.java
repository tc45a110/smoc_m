package com.base.common.worker;

import java.util.HashSet;
import java.util.Set;


/**
 * @desc
 * 加载Map缓存数据超类
 */

public abstract class SuperSetWorker<T> extends SuperWorker{
	
	/**
	 * 数据集合
	 */
	protected Set<T> superSet = new HashSet<T>();
	
	/**
	 * 添加数据
	 * @param t
	 * @param m
	 */
	protected void add(T t){
		superSet.add(t);
	}
	
	/**
	 * 是否包含该数据
	 * @param t
	 * @return
	 */
	protected boolean contains(T t){
		return superSet.contains(t);
	}
	
	/**
	 * 获取集合大小
	 * @return
	 */
	protected int size(){
		return superSet.size();
	}
	
}


