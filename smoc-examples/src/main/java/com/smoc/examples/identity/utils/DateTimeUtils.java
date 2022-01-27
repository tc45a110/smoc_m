package com.smoc.examples.identity.utils;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * 日期工具类
 * 
 * @author wujihuio
 */
public class DateTimeUtils {

	// 默认日期格式
	public static final String DATE_DEFAULT_FORMAT = "yyyy-MM-dd";

	// 默认时间格式
	public static final String DATETIME_DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static final String DATE_STRING_FORMAT = "yyyyMMdd";
	
	public static final String TIME_DEFAULT_FORMAT = "HH:mm:ss";
	
	public static final String MONTH_DEFAULT_FORMAT = "yyyyMMddHHmmss";

	// 日期格式化
	private static DateFormat dateFormat = null;

	// 时间格式化
	private static DateFormat dateTimeFormat = null;

	private static DateFormat timeFormat = null;
	
	private static DateFormat dateStringFormat = null;
	
	private static DateFormat dateMonthFormat = null;

	private static Calendar gregorianCalendar = null;

	static {
		dateFormat = new SimpleDateFormat(DATE_DEFAULT_FORMAT);
		dateTimeFormat = new SimpleDateFormat(DATETIME_DEFAULT_FORMAT);
		dateStringFormat = new SimpleDateFormat(DATE_STRING_FORMAT);
		timeFormat = new SimpleDateFormat(TIME_DEFAULT_FORMAT);
		gregorianCalendar = new GregorianCalendar();
		dateMonthFormat = new SimpleDateFormat(MONTH_DEFAULT_FORMAT);
	}
	
	/**
	 * 日期格式化yyyyyMMdd
	 * 
	 * @param
	 * @return
	 */
	public static String currentDate(Date d1) {  
        return dateStringFormat.format(d1);  
    }
	
	
	/**
	 * 日期格式化yyMMddHHmmss
	 * 
	 * @param
	 * @return
	 */
	public static String currentDateFomat(Date d) {  
        return dateMonthFormat.format(d);  
    }

	/**
	 * 日期格式化yyyy-MM-dd
	 * 
	 * @param date
	 * @return
	 */
	public static Date formatDate(String date, String format) {
		try {
			return new SimpleDateFormat(format).parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 时间格式化成字符串
	 * 
	 * @param date
	 *            Date
	 * @param pattern
	 *            StrUtils.DATE_TIME_PATTERN || StrUtils.DATE_PATTERN，
	 *            如果为空，则为yyyy-MM-dd
	 * @return
	 * @throws ParseException
	 */
	public static String dateFormat(Date date, String pattern) throws ParseException {
		if (null == pattern || "".equals(pattern)) {
			pattern = DateTimeUtils.DATE_DEFAULT_FORMAT;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	/**
	 * 字符串解析成时间对象
	 * 
	 * @param dateTimeString
	 *            String
	 * @param pattern
	 *            StrUtils.DATE_TIME_PATTERN ||
	 *            StrUtils.DATE_PATTERN，如果为空，则为yyyy-MM-dd
	 * @return
	 * @throws ParseException
	 */
	public static Date dateParse(String dateTimeString, String pattern) throws ParseException {
		if (null == pattern || "".equals(pattern)) {
			pattern = DateTimeUtils.DATE_DEFAULT_FORMAT;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.parse(dateTimeString);
	}

	/**
	 * 日期格式化yyyy-MM-dd
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateFormat(Date date) {
		return dateFormat.format(date);
	}
	
	/**
	 * 日期格式化yyyy-MM-dd hh:mm:ss
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateTimeFormat(Date date) {
		return dateTimeFormat.format(date);
	}

	/**
	 * 时间格式化
	 * 
	 * @param date
	 * @return HH:mm:ss
	 */
	public static String getTimeFormat(Date date) {
		return timeFormat.format(date);
	}

	/**
	 * 日期格式化
	 * 
	 * @param date
	 * @param
	 * @return
	 */
	public static String getDateFormat(Date date, String formatStr) {
		if (!(null == formatStr || "".equals(formatStr))) {
			return new SimpleDateFormat(formatStr).format(date);
		}
		return null;
	}

	/**
	 * 日期格式化
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDateFormat(String date) {
		DateFormat dateFormat = new SimpleDateFormat(DATE_DEFAULT_FORMAT);
		try {
			return dateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 时间格式化
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDateTimeFormat(String date) {
		try {
			return dateTimeFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取当前日期(yyyy-MM-dd)
	 * 
	 * @param
	 * @return
	 */
	public static Date getNowDate() {
		return DateTimeUtils.getDateFormat(dateFormat.format(new Date()));
	}

	/**
	 * 获取当前日期(yyyy-MM-dd HH:mm:ss)
	 * 
	 * @param
	 * @return
	 */
	public static Date getNowDateTime() {
		return DateTimeUtils.getDateTimeFormat(dateTimeFormat.format(new Date()));
	}

	/**
	 * 获取当前日期星期一日期
	 * 
	 * @return date
	 */
	public static Date getFirstDayOfWeek() {
		gregorianCalendar.setFirstDayOfWeek(Calendar.MONDAY);
		gregorianCalendar.setTime(new Date());
		gregorianCalendar.set(Calendar.DAY_OF_WEEK, gregorianCalendar.getFirstDayOfWeek()); // Monday
		return gregorianCalendar.getTime();
	}

	/**
	 * 获取当前日期星期日日期
	 * 
	 * @return date
	 */
	public static Date getLastDayOfWeek() {
		gregorianCalendar.setFirstDayOfWeek(Calendar.MONDAY);
		gregorianCalendar.setTime(new Date());
		gregorianCalendar.set(Calendar.DAY_OF_WEEK, gregorianCalendar.getFirstDayOfWeek() + 6); // Monday
		return gregorianCalendar.getTime();
	}

	/**
	 * 获取日期星期一日期
	 * 
	 * @param
	 * @return date
	 */
	public static Date getFirstDayOfWeek(Date date) {
		if (date == null) {
			return null;
		}
		gregorianCalendar.setFirstDayOfWeek(Calendar.MONDAY);
		gregorianCalendar.setTime(date);
		gregorianCalendar.set(Calendar.DAY_OF_WEEK, gregorianCalendar.getFirstDayOfWeek()); // Monday
		return gregorianCalendar.getTime();
	}

	/**
	 * 获取日期星期一日期
	 * 
	 * @param
	 * @return date
	 */
	public static Date getLastDayOfWeek(Date date) {
		if (date == null) {
			return null;
		}
		gregorianCalendar.setFirstDayOfWeek(Calendar.MONDAY);
		gregorianCalendar.setTime(date);
		gregorianCalendar.set(Calendar.DAY_OF_WEEK, gregorianCalendar.getFirstDayOfWeek() + 6); // Monday
		return gregorianCalendar.getTime();
	}

	/**
	 * 获取当前月的第一天
	 * 
	 * @return date
	 */
	public static Date getFirstDayOfMonth() {
		gregorianCalendar.setTime(new Date());
		gregorianCalendar.set(Calendar.DAY_OF_MONTH, 1);
		return gregorianCalendar.getTime();
	}

	/**
	 * 获取当前月的最后一天
	 * 
	 * @return
	 */
	public static Date getLastDayOfMonth() {
		gregorianCalendar.setTime(new Date());
		gregorianCalendar.set(Calendar.DAY_OF_MONTH, 1);
		gregorianCalendar.add(Calendar.MONTH, 1);
		gregorianCalendar.add(Calendar.DAY_OF_MONTH, -1);
		return gregorianCalendar.getTime();
	}

	/**
	 * 获取指定月的第一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstDayOfMonth(Date date) {
		gregorianCalendar.setTime(date);
		gregorianCalendar.set(Calendar.DAY_OF_MONTH, 1);
		return gregorianCalendar.getTime();
	}

	/**
	 * 获取指定月的最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfMonth(Date date) {
		gregorianCalendar.setTime(date);
		gregorianCalendar.set(Calendar.DAY_OF_MONTH, 1);
		gregorianCalendar.add(Calendar.MONTH, 1);
		gregorianCalendar.add(Calendar.DAY_OF_MONTH, -1);
		return gregorianCalendar.getTime();
	}

	/**
	 * 获取日期前一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDayBefore(Date date) {
		gregorianCalendar.setTime(date);
		int day = gregorianCalendar.get(Calendar.DATE);
		gregorianCalendar.set(Calendar.DATE, day - 1);
		return gregorianCalendar.getTime();
	}

	/**
	 * 获取日期前5天
	 *
	 * @param date
	 * @return
	 */
	public static Date getFiveDayBefore(Date date) {
		gregorianCalendar.setTime(date);
		int day = gregorianCalendar.get(Calendar.DATE);
		gregorianCalendar.set(Calendar.DATE, day - 5);
		return gregorianCalendar.getTime();
	}

	/**
	 * 获取日期后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDayAfter(Date date) {
		gregorianCalendar.setTime(date);
		int day = gregorianCalendar.get(Calendar.DATE);
		gregorianCalendar.set(Calendar.DATE, day + 1);
		return gregorianCalendar.getTime();
	}

	/**
	 * 获取当前年
	 * 
	 * @return
	 */
	public static int getNowYear() {
		Calendar d = Calendar.getInstance();
		return d.get(Calendar.YEAR);
	}

	/**
	 * 获取当前月份
	 * 
	 * @return
	 */
	public static int getNowMonth() {
		Calendar d = Calendar.getInstance();
		return d.get(Calendar.MONTH) + 1;
	}

	/**
	 * 获取当月天数
	 * 
	 * @return
	 */
	public static int getNowMonthDay() {
		Calendar d = Calendar.getInstance();
		return d.getActualMaximum(Calendar.DATE);
	}

	/**
	 * 获取时间段的每一天
	 * 
	 * @param startDate
	 * @param endDate
	 * @return 日期列表
	 */
	public static List<Date> getEveryDay(Date startDate, Date endDate) {
		if (startDate == null || endDate == null) {
			return null;
		}
		// 格式化日期(yy-MM-dd)
		startDate = DateTimeUtils.getDateFormat(DateTimeUtils.getDateFormat(startDate));
		endDate = DateTimeUtils.getDateFormat(DateTimeUtils.getDateFormat(endDate));
		List<Date> dates = new ArrayList<Date>();
		gregorianCalendar.setTime(startDate);
		dates.add(gregorianCalendar.getTime());
		while (gregorianCalendar.getTime().compareTo(endDate) < 0) {
			// 加1天
			gregorianCalendar.add(Calendar.DAY_OF_MONTH, 1);
			dates.add(gregorianCalendar.getTime());
		}
		return dates;
	}

	/**
	 * 获取提前多少个月
	 * 
	 * @param monty
	 * @return
	 */
	public static Date getFirstMonth(int monty) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -monty);
		return c.getTime();
	}

	/****
	 * 传入具体日期 ，返回具体日期减一个月。
	 * 
	 * @param date
	 *            日期(2015-05-05)
	 * @throws ParseException
	 */
	public static String subMonth(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dt = sdf.parse(date);
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(dt);

		rightNow.add(Calendar.MONTH, -1);
		Date dt1 = rightNow.getTime();
		String reStr = sdf.format(dt1);

		return reStr;
	}

	/***
	 * 日期天数加减
	 * 
	 */
	public static String checkOption(String _date, Integer _dd) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cl = Calendar.getInstance();
		Date date = null;

		try {
			date = (Date) sdf.parse(_date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		cl.setTime(date);
		cl.add(Calendar.DAY_OF_MONTH, _dd);
		date = cl.getTime();
		return sdf.format(date);
	}

	/**
	 * 时间加减小时
	 * 
	 * @param startDate
	 *            要处理的时间，Null则为当前时间
	 * @param hours
	 *            加减的小时
	 * @return Date
	 */
	public static Date dateAddHours(Date startDate, int hours) {
		if (startDate == null) {
			startDate = new Date();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(startDate);
		c.set(Calendar.HOUR, c.get(Calendar.HOUR) + hours);
		return c.getTime();
	}

	/**
	 * 时间加减分钟
	 * 
	 * @param startDate
	 *            要处理的时间，Null则为当前时间
	 * @param minutes
	 *            加减的分钟
	 * @return
	 */
	public static Date dateAddMinutes(Date startDate, int minutes) {
		if (startDate == null) {
			startDate = new Date();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(startDate);
		c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + minutes);
		return c.getTime();
	}

	/**
	 * 时间加减秒数
	 * 
	 * @param startDate
	 *            要处理的时间，Null则为当前时间
	 * @param
	 *           seconds
	 * @return
	 */
	public static Date dateAddSeconds(Date startDate, int seconds) {
		if (startDate == null) {
			startDate = new Date();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(startDate);
		c.set(Calendar.SECOND, c.get(Calendar.SECOND) + seconds);
		return c.getTime();
	}

	/**
	 * 时间加减天数
	 * 
	 * @param startDate
	 *            要处理的时间，Null则为当前时间
	 * @param days
	 *            加减的天数
	 * @return Date
	 */
	public static Date dateAddDays(Date startDate, int days) {
		if (startDate == null) {
			startDate = new Date();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(startDate);
		c.set(Calendar.DATE, c.get(Calendar.DATE) + days);
		return c.getTime();
	}

	/**
	 * 时间月数加减
	 * 
	 * @param startDate
	 *            要处理的时间，Null则为当前时间
	 * @param months
	 *            加减的月数
	 * @return Date
	 */
	public static Date dateAddMonths(Date startDate, int months) {
		if (startDate == null) {
			startDate = new Date();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(startDate);
		c.add(Calendar.MONTH,months);
		return c.getTime();
	}

	/**
	 * 时间加减年数
	 * 
	 * @param startDate
	 *            要处理的时间，Null则为当前时间
	 * @param years
	 *            加减的年数
	 * @return Date
	 */
	public static Date dateAddYears(Date startDate, int years) {
		if (startDate == null) {
			startDate = new Date();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(startDate);
		c.add(Calendar.YEAR, years);
		return c.getTime();
	}

	/**
	 * 时间比较（如果myDate>compareDate返回1，<返回-1，相等返回0）
	 * 
	 * @param myDate
	 *            时间
	 * @param compareDate
	 *            要比较的时间
	 * @return int
	 */
	public static int dateCompare(Date myDate, Date compareDate) {
		Calendar myCal = Calendar.getInstance();
		Calendar compareCal = Calendar.getInstance();
		myCal.setTime(myDate);
		compareCal.setTime(compareDate);
		return myCal.compareTo(compareCal);
	}

	/**
	 * 获取两个日期（不含时分秒）相差的天数，不包含今天
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws ParseException
	 */
	public static int dateBetween(Date startDate, Date endDate) throws ParseException {
		Date dateStart = dateParse(dateFormat(startDate, DATE_DEFAULT_FORMAT), DATE_DEFAULT_FORMAT);
		Date dateEnd = dateParse(dateFormat(endDate, DATE_DEFAULT_FORMAT), DATE_DEFAULT_FORMAT);
		return (int) ((dateEnd.getTime() - dateStart.getTime()) / 1000 / 60 / 60 / 24);
	}

	/**
	 * 获取两个日期（不含时分秒）相差的天数，包含今天
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws ParseException
	 */
	public static int dateBetweenIncludeToday(Date startDate, Date endDate) throws ParseException {
		return dateBetween(startDate, endDate) + 1;
	}
	
	/**
	 * 查询当前月份和向后推6个月
	 * 
	 * @return
	 */
	public static String[] getMonthAndLast(int a) {

		// 查询月业绩流水和年华金额
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM");
		// 当前月份
		Date nowDate = DateTimeUtils.getNowDate();
		// 当前月份向后推5个月
		String[] dateArray = new String[a];
		dateArray[0] = sdf.format(nowDate);
		int z = 1;
		for (int i = 1; i < 6; i++) {
			Date subtract = DateTimeUtils.dateAddMonths(nowDate, -i);
			dateArray[z++] = sdf.format(subtract);
		}

		return dateArray;
	}
	
	/**
	 * 查询当前月份和向后推4个月
	 * 
	 * @return
	 */
	public static String[] getMonth(int a) {

		// 查询月业绩流水和年华金额
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		// 当前月份
		Date nowDate;
		try {
			nowDate = sdf.parse(sdf.format(new Date()));
			// 当前月份向后推6个月
			String[] dateArray = new String[a];
			dateArray[0] = sdf.format(nowDate);
			int z = 1;
			for (int i = 1; i < a; i++) {
				Date subtract = DateTimeUtils.dateAddMonths(nowDate, -i);
				dateArray[z++] = sdf.format(subtract);
			}

			return dateArray;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 查询当前月份和前5个月
	 * 
	 * @return
	 */
	public static String[] getNowAndFrontAndBackMonth(int a) {

		// 查询月业绩流水和年华金额
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
		// 当前月份
		Date nowDate = DateTimeUtils.getNowDate();
		// 当前前后三个月
		String[] dateArray = new String[a];

		int z = 0;
		// 前5个月
		for (int i = a-1; i > 0; i--) {
			Date subtract = DateTimeUtils.dateAddMonths(nowDate, -i);
			dateArray[z] =getLastDayOfMonthByJava8(subtract);;
			z++;
		}

		dateArray[z] = sdf.format(nowDate);
		/*
		 * z++; for (int i=0;i<6;) { Date subtract =
		 * DateTimeUtils.dateAddMonths(nowDate, i);
		 * dateArray[z]=sdf.format(subtract); z++; i++; }
		 */

		return dateArray;
	}
	
	/** 
     * java8(别的版本获取2月有bug) 获取某月最后一天 
     * @return 
     * @throws ParseException 
     */  
    public static String getLastDayOfMonthByJava8(Date date){  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());;  
        LocalDateTime endOfDay = localDateTime.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);  
        Date dates = Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());  
        return sdf.format(dates);  
    }
	
	public static long daylargeAndSmall(Date d1,Date d2){
    	long days =0;
    	try   {   
	    	long l1 = d1.getTime();
	    	long l2 = d2.getTime();
	    	if(l1<=l2){
	    		days=-1;
	    	}
    	 }   
        catch(Exception ex) {   
            
        }   
    	return days;
    }
	
	/**
	 * 查询当前月份和后几个月
	 * 
	 * @return
	 */
	public static String[] getNowAndLastMonth(int a) {

		// 查询月业绩流水和年华金额
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		// 当前月份
		Date nowDate = null;
		try {
			nowDate = sdf.parse(sdf.format(new Date()));
			String[] dateArray = new String[a];
			int z = 0;
			for (int i = 0; i < a;) {
				Date subtract = DateTimeUtils.dateAddMonths(nowDate, i);
				dateArray[z] = sdf.format(subtract);
				z++;
				i++;
			}
			return dateArray;
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 获取上个月
	 * @return
	 */
	public static String getBeforMonth(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date); // 设置为当前时间
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1); // 设置为上一个月
		date = calendar.getTime();
		String accDate = format.format(date);
		return accDate;
	}

	/**
	 * 获取当前月
	 * @return
	 */
	public static String getMonth(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date); // 设置为当前时间
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
		date = calendar.getTime();
		String accDate = format.format(date);
		return accDate;
	}

	/**
	 * 获取某年第一天日期
	 * @param year 年份
	 * @return Date
	 */
	public static Date getYearFirst(int year){
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		Date currYearFirst = calendar.getTime();
		return currYearFirst;
	}

	/**
	 * 获取某年最后一天日期
	 * @param year 年份
	 * @return Date
	 */
	public static Date getYearLast(int year){
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.roll(Calendar.DAY_OF_YEAR, -1);
		Date currYearLast = calendar.getTime();
		return currYearLast;
	}

	public static int compare_date(String DATE1, String DATE2) {
		DateFormat df = new SimpleDateFormat("yyyy-MM");
		try {
			Date dt1 = df.parse(DATE1);
			Date dt2 = df.parse(DATE2);
			if (dt1.getTime() > dt2.getTime()) {
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

	/**
	 * 时间日期转换
	 * @param strDate 字符串yyyyMMddHHmmss
	 * @return 字符串yyyy-MM-dd HH:mm:ss
	 */
	public static String strToDateLong(String strDate) {
		Date date = new Date();
		try {
			date = new SimpleDateFormat("yyyyMMddHHmmss").parse(strDate);//先按照原格式转换为时间
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);//再将时间转换为对应格式字符串
		return str;
	}
}