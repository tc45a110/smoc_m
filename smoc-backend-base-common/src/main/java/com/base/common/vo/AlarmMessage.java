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
		AccountReportQueue,//账号状态报告队列
		BusinessStatistics,//日志统计服务异常
		ProtocolAccess,//接入协议层异常
		ProtocolProxy,//代理协议层异常
		Redis,//redis异常
		Database,//数据库异常
		InsideFilter,//内部过滤异常
		ExternalBlacklistFilter,//外部过滤异常
		AccountSuccessRate,//账号成功率
		AccountDelayRate,//账号延迟率
		AccountUnknownRate,//账号未知率
		ChannelSuccessRate,//通道成功率
		ChannelDelayRate,//通道延迟率
		ChannelUnknownRate,//通道未知率
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


