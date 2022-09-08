package com.base.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateUtil {

	//年月日
	public static final String DATE_FORMAT_COMPACT_DAY = "yyyyMMdd";
	//年月日时
	public static final String DATE_FORMAT_COMPACT_HOUR = "yyyyMMddHH";
	//年月日时分
	public static final String DATE_FORMAT_COMPACT_MINUTE = "yyyyMMddHHmm";
	//年月日时分秒
	public static final String DATE_FORMAT_COMPACT_SECOND = "yyyyMMddHHmmss";
	//年月日时分秒毫秒
	public static final String DATE_FORMAT_COMPACT_MILLI = "yyyyMMddHHmmssSSS";
	
	//年月
	public static final String DATE_YEAR_MONTH = "yyyyMM";
	//时分
	public static final String DATE_HOUR_MINUTE = "HHmm";
	
	//cmpp时间格式
	public static final String DATE_FORMAT_COMPACT_CMPP = "yyMMddHHmm";

	//标准时间格式 天
	public static final String DATE_FORMAT_COMPACT_STANDARD_DAY = "yyyy-MM-dd";
	//标准时间格式 小时
	public static final String DATE_FORMAT_COMPACT_STANDARD_HOUR = "yyyy-MM-dd HH";
	//标准时间格式 分钟
	public static final String DATE_FORMAT_COMPACT_STANDARD_MINUTE = "yyyy-MM-dd HH:mm";
	//标准时间格式 秒
	public static final String DATE_FORMAT_COMPACT_STANDARD_SECONDE = "yyyy-MM-dd HH:mm:ss";
	//标准时间格式 毫秒
	public static final String DATE_FORMAT_COMPACT_STANDARD_MILLI = "yyyy-MM-dd HH:mm:ss SSS";

	public static final long SECOND_MILLI_SECONDE_NUMBER = 1000;
	
	public static final long MINUTE_MILLI_SECONDE_NUMBER = 60 * 1000;
	
	public static final long HOUR_MILLI_SECONDE_NUMBER = 60 * 60 * 1000;
	
	public static final long DAY_MILLI_SECONDE_NUMBER = 24 * 60 * 60 * 1000;
	
	/**
	 * 得到当前日期
	 * 
	 * @param format 日期格式
	 * @return String 当前日期 format日期格式
	 * @author kevin
	 */
	public static String getCurDateTime(String format) {
		try {
			Calendar now = Calendar.getInstance(TimeZone.getDefault());
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			sdf.setTimeZone(TimeZone.getDefault());
			return (sdf.format(now.getTime()));
		} catch (Exception e) {
			return getCurDateTime(); // 如果无法转化，则返回默认格式的时间。
		}
	}
	
	/**
	 * 得到当前日期
	 */
	public static String getCurDateTime() {
		Calendar now = Calendar.getInstance(TimeZone.getDefault());
		DateFormat sdf = new SimpleDateFormat(DATE_FORMAT_COMPACT_STANDARD_SECONDE);
		sdf.setTimeZone(TimeZone.getDefault());
		return (sdf.format(now.getTime()));
	}
	
	/**
	 * 获取两个时间段的间隔时间，单位：秒
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static int getIntervalTime(String startTime,String endTime) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_COMPACT_STANDARD_MILLI);
			long start = sdf.parse(startTime).getTime();
			long end = sdf.parse(endTime).getTime();
			int intervalTime = (int)(end -start)/1000;
			return intervalTime;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获取两个时间段的间隔时间，单位：秒
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static int getIntervalTime(String startTime,String endTime,String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			long start = sdf.parse(startTime).getTime();
			long end = sdf.parse(endTime).getTime();
			int intervalTime = (int)(end -start)/1000;
			return intervalTime;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获取两个时间段的间隔时间，单位：分钟
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static int getMinuteIntervalTime(Date startTime,Date endTime) {
		try {
			long start = startTime.getTime();
			long end = endTime.getTime();
			double interval = (double) (end - start) / (1000d * 60d);
			int intervalTime = (int)Math.round(interval);
			return intervalTime;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 *   字符串解析成时间类型
	 * @param time
	 * @param format
	 * @return
	 */
	public static Date parseDate(String time,String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 	离当前时间的间隔时间
	 * @param startTime
	 * @param format
	 * @return
	 */
	public static int getNowIntervalTime(String startTime,String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			long start = sdf.parse(startTime).getTime();
			long end = new Date().getTime();
			int intervalTime = (int)(end -start)/1000;
			return intervalTime;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * 获取N天之后的数据  -:前 	 +:后
	 * @param day
	 * @param format
	 * @return
	 */
	public static String getAfterDayDateTime(int day, String format) {
		long lTime = new Date().getTime() + day * DAY_MILLI_SECONDE_NUMBER;
		Calendar calendar = new GregorianCalendar();
		java.util.Date date_now = new java.util.Date(lTime);
		calendar.setTime(date_now);
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		java.util.Date date = calendar.getTime();
		return sdf.format(date);
	}
	
	/**
	 * 获取N分钟之后的数据  -:前 	 +:后
	 * @param minute
	 * @param format
	 * @return
	 */
	public static String getAfterMinuteDateTime(int minute, String format) {
		long lTime = new Date().getTime() + minute * MINUTE_MILLI_SECONDE_NUMBER;
		Calendar calendar = new GregorianCalendar();
		java.util.Date date_now = new java.util.Date(lTime);
		calendar.setTime(date_now);
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		java.util.Date date = calendar.getTime();
		return sdf.format(date);
	}
	
	/**
	 * 获取N分钟之后的数据  -:前 	 +:后
	 * @param minute
	 * @param format
	 * @return
	 */
	public static String getAfterMinuteDateTime(int minute, String format,Date date) {
		long lTime = date.getTime() + minute * MINUTE_MILLI_SECONDE_NUMBER;
		Calendar calendar = new GregorianCalendar();
		Date date_now = new Date(lTime);
		calendar.setTime(date_now);
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(calendar.getTime());
	}
	
	/**
	 * 	时间格式化
	 * @param date
	 * @param format
	 * @return
	 */
	public static String format(Date date,String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
}
