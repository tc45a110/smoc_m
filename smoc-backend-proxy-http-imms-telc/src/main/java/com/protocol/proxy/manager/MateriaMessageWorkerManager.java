/**
 * @desc
 * 
 */
package com.protocol.proxy.manager;
import com.base.common.worker.SuperMapWorker;
import com.protocol.proxy.worker.MaterialMessageWorker;

import java.util.HashSet;
import java.util.Set;

public class MateriaMessageWorkerManager extends SuperMapWorker<String, MaterialMessageWorker> {

	private static MateriaMessageWorkerManager manager = new MateriaMessageWorkerManager();

	private MateriaMessageWorkerManager() {
		this.setName("MaterialMessageWorkerManager");
		this.start();
	}

	public static MateriaMessageWorkerManager getInstance() {
		return manager;
	}

	public void doRun() throws Exception {
		sleep(INTERVAL);
	}

	public void maintain(String channelID, MaterialMessageWorker materiaMessageWorker){
		add(channelID,materiaMessageWorker);
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
		MaterialMessageWorker materiaMessageWorker = remove(channelID);
		if (materiaMessageWorker != null) {
			materiaMessageWorker.exit();
		}
	}

}
