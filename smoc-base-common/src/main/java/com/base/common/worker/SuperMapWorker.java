package com.base.common.worker;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * @desc
 * 加载Map缓存数据超类
 */

public abstract class SuperMapWorker<T,M> extends SuperWorker{
	
	/**
	 * 数据集合
	 */
	protected Map<T,M> superMap = new HashMap<T,M>();
	
	/**
	 * 添加数据
	 * @param t
	 * @param m
	 */
	protected void add(T t,M m){
		superMap.put(t,m);
	}
	
	/**
	 * 获取数据
	 * @param t
	 * @return
	 */
	protected M get(T t){
		return superMap.get(t);
	}
	
	protected Set<T> keySet(){
		return superMap.keySet();
	}
	
	/**
	 * 获取集合大小
	 * @return
	 */
	protected int size(){
		return superMap.size();
	}
	
	protected M remove(T t){
		return superMap.remove(t);
	}
}


