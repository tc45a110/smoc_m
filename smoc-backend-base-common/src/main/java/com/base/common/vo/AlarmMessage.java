/**
 * @desc
 */
package com.base.common.vo;

public class AlarmMessage {
	/**
	 * 告警点
	 */
	public static enum AlarmKey{
		ChannelWorker,//通道缓存队列
		ExternalBlacklistFilterWorker,//外部黑名单过滤缓存队列
	}
	
	/**
	 * 告警key
	 */
	private AlarmKey alarmKey;
	
	/**
	 * 告警值
	 */
	private String alarmValue;

	public AlarmMessage(AlarmKey alarmKey, String alarmValue) {
		super();
		this.alarmKey = alarmKey;
		this.alarmValue = alarmValue;
	}

	public AlarmKey getAlarmKey() {
		return alarmKey;
	}

	public String getAlarmValue() {
		return alarmValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((alarmKey == null) ? 0 : alarmKey.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AlarmMessage other = (AlarmMessage) obj;
		if (alarmKey != other.alarmKey)
			return false;
		return true;
	}

	
}


