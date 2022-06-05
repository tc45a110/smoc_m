/**
 * @desc
 * 
 */
package com.base.common.vo;

public class AlarmValue {
	
	/**
	 * 告警级别
	 */
	public static enum AlarmLevel{
		//低、中、高
		LOW,MIDDLE,HIGH
	}
	
	/**
	 * 告警分类
	 */
	public static enum AlarmClassification{
		OPERATOR
	}
	
	/**
	 * 告警信息
	 */
	private String alarmInfo;
	
	/**
	 * 告警手机号
	 */
	private String alramPhoneNumber;
	
	private AlarmLevel alarmLevel;
	
	private AlarmClassification alarmClassification;

	public AlarmValue(String alarmInfo, String alramPhoneNumber,
			AlarmLevel alarmLevel, AlarmClassification alarmClassification) {
		super();
		this.alarmInfo = alarmInfo;
		this.alramPhoneNumber = alramPhoneNumber;
		this.alarmLevel = alarmLevel;
		this.alarmClassification = alarmClassification;
	}

	public String getAlarmInfo() {
		return alarmInfo;
	}

	public String getAlramPhoneNumber() {
		return alramPhoneNumber;
	}

	public AlarmLevel getAlarmLevel() {
		return alarmLevel;
	}

	public AlarmClassification getAlarmClassification() {
		return alarmClassification;
	}
	
	
}


