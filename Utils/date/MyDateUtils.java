package moulce.test.ve;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.Period;
/**
 * 时间处理工具
 * need: commons-logging-x.x.jar、joda-time-x.x.jar
 * @author sugengbin
 *
 */
public class MyDateUtils {
	
	private static final Log log = LogFactory.getLog(MyDateUtils.class);

	public static final String FORMAT_PATTERN_YMDHMS = "yyyy-MM-dd HH:mm:ss";
	public static final String FORMAT_PATTERN_YMDHMSSSS = "yyyy-MM-dd HH:mm:ss.SSS";
	public static final String FORMAT_PATTERN_YMDHM = "yyyy-MM-dd HH:mm";
	public static final String FORMAT_PATTERN_YMD = "yyyy-MM-dd";
	public static final String FORMAT_PATTERN_HM = "HH:mm";
	public static final String FORMAT_PATTERN_MS = "mm:ss";
	public static final String FORMAT_PATTERN_YYYYMMDD = "yyyyMMdd";
	public static final String FORMAT_PATTERN_YMDHMSSSSS = "yyyyMMddHHmmssSSS";
	
	/**
	 * 忽略秒数，比较两个日期相隔的分钟 ,有正负
	 * 
	 * @param date1
	 * @param date2
	 * @return date2-date1
	 */
	public static long getDiffMins(Date date1, Date date2) {
		checkNotNull(date1);
		checkNotNull(date2);
		long m1 = date1.getTime() / 60000;
		long m2 = date2.getTime() / 60000;
		return (m2 - m1);
	}
	
	/**
	 * 比较两个日期相隔的天数 ,有正负
	 * 
	 * @param date1
	 * @param date2
	 * @return date2-date1
	 */
	public static long getDiffDays(Date date1, Date date2) {
		checkNotNull(date1);
		checkNotNull(date2);
		long m1 = date1.getTime();
		long m2 = date2.getTime();
		return (m2 - m1) / (60000 * 60 * 24);
	}
	
	/**
	 * 比较两个日期之间的年数，不到一年的取0
	 * date2-date1
	 */
	public static long getDiffYears(Date date1, Date date2) {
		long diffMins = getDiffMins(date1, date2);
		long diffYears = diffMins / (60 * 24 * 365);
		return diffYears;
	}
	
	/**
	 * 比较两个日期相隔的秒数,毫秒精确到小数点后两位
	 * 
	 * @param startDate
	 * @param endDate
	 * @return endDate-startDate
	 */
	public static BigDecimal getDiffMillisecond(Date startDate, Date endDate) {
		checkNotNull(startDate);
		checkNotNull(endDate);
		long m = startDate.getTime();
		long m2 = endDate.getTime();
		long diff = m2 - m;
		BigDecimal value = new BigDecimal(diff / 1000.00);
		value = value.setScale(2, BigDecimal.ROUND_HALF_UP); // 保留小数点后两位
		return value;
	}
	
	/**
	 * 忽略小数，比较两个日期相隔的秒数
	 * 
	 * @param date1
	 * @param date2
	 * @return date2-date1
	 */
	public static long getDiffScondsNoAbs(Date date1, Date date2) {
		checkNotNull(date1);
		checkNotNull(date2);
		long m1 = date1.getTime() / 1000;
		long m2 = date2.getTime() / 1000;
		return (long) (m2 - m1);
	}

	/**
	 * 把日期格式化成一字符串
	 * 
	 * @param date
	 * @param fmt
	 * @return
	 */
	public static String getDateStr(Date date, String fmt) {
		String val = StringUtils.EMPTY;
		SimpleDateFormat sdf = new SimpleDateFormat(fmt);
		try {
			if (date != null)
				val = sdf.format(date);
		} catch (Exception ex) {
			log.error(ex);
		}
		return val;
	}

	/**
	 * String 转化为Date
	 * @param strDate
	 * @param fmt
	 * @return
	 */
	public static Date parseDate(String strDate, String fmt) {
		Date dt = null;
		if (strDate != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(fmt);
			try {
				dt = sdf.parse(strDate);
			} catch (ParseException ex) {
				log.error(ex);
			}
		}
		return dt;
	}
	
	/**
	 * 检查日期是否指定格式
	 * @param strDate
	 * @param fmt
	 * @return
	 */
	public static boolean verifyFmt(String strDate, String fmt){
		boolean success = true;
		try{
			if(strDate != null){
				SimpleDateFormat sdf = new SimpleDateFormat(fmt);
				Date paseDate = sdf.parse(strDate);
				String fmtStr = sdf.format(paseDate);
				if(splitYMDHMSDate(fmtStr,0,0) != splitYMDHMSDate(strDate,0,0) ||
						splitYMDHMSDate(fmtStr,2,0) != splitYMDHMSDate(strDate,2,0) ||
						splitYMDHMSDate(fmtStr,1,1) != splitYMDHMSDate(strDate,1,1))
					success = false; 
			}
		}catch (java.text.ParseException e) {
			success = false;
		}
		return success;
	}
	
	/**
	 * 切分yyyy-mm-dd HH:mm:ss 格式，判断时间格式verifyFmt时候用到
	 * j = 0 i= 0 得到yyyy
	 * j = 0 i= 1得到月份
	 * j = 0 i= 2 得到日期
	 * j = 1 i= 0 得到hh
	 * j = 1 i= 1得到mm
	 * j = 1 i= 2 得到ss
	 * @param s
	 * @param i
	 * @return  int 
	 */
	private static int splitYMDHMSDate(String s, int i, int j){
		int num = -1;
		String[] ss = {};
		try {
			String[] strHms = StringUtils.split(s, " ");
			if(j == 0){
				ss = StringUtils.split(strHms[0], "-");
				num = Integer.parseInt(StringUtils.trimToEmpty(ss[i]));
			}
			if(j == 1){
				ss = StringUtils.split(strHms[1], ":");
				num = Integer.parseInt(StringUtils.trimToEmpty(ss[i]));
			}
		} catch (Exception e) {
			return -1;
		}
		return num;
	}

	/**
	 * 得到当前时间
	 * @return Date
	 */
	public static Date now() {
		return DateTime.now().toDate();
	}
	
	/**
	 * 得到当前时间
	 * @return DateTime
	 */
	public static DateTime nowDt() {
		return DateTime.now();
	}

	/**
	 * 得到当前月份的第一天
	 */
	public static Date get1stDayOfThisMonth() {
		DateTime dt=nowDt();
		return new DateTime(dt.getYear(),dt.getMonthOfYear(),1, 0, 0, 0).toDate();
	}

	/**
	 * 解析一个时间段，时间只能指定一个单位，数字必须是整数
	 * <pre>
	 * parsePeriod(" ") -> null
	 * parsePeriod(null) -> null
	 * parsePeriod("2m") -> 2 minutes //注意m与M的区别
	 * parsePeriod("3M") -> 3 months //注意m与M的区别
	 * parsePeriod("1d") -> 1 day
	 * parsePeriod("1D") -> 1 day
	 * parsePeriod("67Y") -> 67 years
	 * parsePeriod("67y") -> 67 years
	 * parsePeriod("1s") -> 1 second
	 * parsePeriod("1h") -> 1 hour
	 * 
	 * </pre>
	 * @param periodStr
	 * @return
	 */
	public static Period parsePeriod(String periodStr){
		Period result=null;
		if(StringUtils.isNotBlank(periodStr)){
			String pStr=StringUtils.trim(periodStr);
			String num=StringUtils.substring(pStr, 0, pStr.length()-1);
			Integer value=null;
			try{
				value=Integer.valueOf(num);
			}catch(NumberFormatException e){
				log.error(e);
			}
			if(value!=null){
				String timeUnit=StringUtils.substring(pStr, pStr.length()-1, pStr.length());
				
				if("S".equalsIgnoreCase(timeUnit)){
					result=Period.seconds(value);
				}else if("m".equals(timeUnit)){
					result=Period.minutes(value);
				}else if("H".equalsIgnoreCase(timeUnit)){
					result=Period.hours(value);
				}else if("D".equalsIgnoreCase(timeUnit)){
					result=Period.days(value);
				}else if("M".equals(timeUnit)){
					result=Period.months(value);
				}else if("Y".equalsIgnoreCase(timeUnit)){
					result=Period.years(value);
				}
			}
		}
		return result;
	}
	
	/**
	 * 此方法返回的是当日的23:59:59 ，请不要再使用此方法作为查询的边际条件。
	 * 请使用{@link #getTimeAtStartOfDay}或者{@link #getTimeAtStartOfNextDay}
	 * @param date
	 * @return
	 */
	@Deprecated
	public static Date getEndDate(Date date) {
		if (date == null){
			return null;
		}
		String maxDate = getDateStr(date, "yyyy-MM-dd");
		Timestamp endDate = Timestamp.valueOf(maxDate + " 23:59:59");
		return new Date(endDate.getTime());
	}
	
	/**
	 * 取当前日期的开始时间。一般每天是从00:00:00开始。
	 * 但是某些实行夏时制的国家会从夏时制实行的当天，时钟往前拨1小时（11:59:59直接到第二天01:00:00）。
	 * 
	 * @param date
	 * @return null 如果date==null
	 */
	public static Date getTimeAtStartOfDay(Date date) {
		Date result = null;
		if (date != null) {
			result = new DateTime(date).withTimeAtStartOfDay().toDate();
		}
		return result;
	}
	
	/**
	 * 取往后一天日期的开始时间。
	 * @param date
	 * @return null 如果date==null
	 * @see {@link #getTimeAtStartOfDay(Date)}
	 */
	public static Date getTimeAtStartOfNextDay(Date date) {
		Date result = null;
		if (date != null) {
			result = new DateTime(date).plusDays(1).withTimeAtStartOfDay()
					.toDate();
		}
		return result;
	}
	
	/**
	 * 计算指定日期按日增加（或减少）后的日期。<br/>
	 * <pre>
	 * DateUtils.plusDays(Date("2016-02-29"),1) --> 2016-03-01
	 * DateUtils.plusDays(Date("2015-03-01"),-1) --> 2015-02-28
	 * 
	 * </pre>
	 * @param date
	 * @param days
	 * @return
	 */
	public static Date plusDays(Date date, int days) {
		checkNotNull(date);
		DateTime dt = new DateTime(date.getTime()).plusDays(days);
		return dt.toDate();
	}
	
	/**
	 * 计算指定日期按日增加（或减少）后的日期。<br/>
	 * <pre>
	 * DateUtils.plusDays(Date("2016-02-29"),1) --> 2016-03-01
	 * DateUtils.plusDays(Date("2015-03-01"),-1) --> 2015-02-28
	 * 
	 * </pre>
	 * @param date
	 * @param days
	 * @return
	 */
	public static Date plusHours(Date date, int hours) {
		checkNotNull(date);
		DateTime dt = new DateTime(date.getTime()).plusHours(hours);
		return dt.toDate();
	}
	
	/**
	 * 
	 * 增加分钟数
	 * 
	 * @param date
	 * @param minutes
	 * @return
	 */
	public static Date plusMinutes(Date date, int minutes) {
		checkNotNull(date);
		DateTime dt = new DateTime(date.getTime()).plusMinutes(minutes);
		return dt.toDate();
	}
	
	/**
	 * 计算指定日期按月增加（或减少）后的日期。<br/>
	 * <ul>
	 * <li>注意：增加（减少）的时候，日期应取“对应日期”与“当月最大日期”之间的最小值，参考以下例子。</li>
	 * </ul>
	 * <pre>
	 * DateUtils.plusMonths(Date("2016-02-29"),-12) --> 2015-02-28
	 * DateUtils.plusMonths(Date("2016-03-31"),-1) --> 2016-02-29
	 * DateUtils.plusMonths(Date("2016-02-29"),1) --> 2016-03-29
	 * DateUtils.plusMonths(Date("2015-02-28"),12) --> 2016-02-28
	 * DateUtils.plusMonths(Date("2016-03-31"),0) --> 2016-03-31
	 * 
	 * </pre>
	 * 
	 * @param date
	 * @param month
	 * @return
	 */
	public static Date plusMonths(Date date, int month) {
		checkNotNull(date);
		DateTime dt = new DateTime(date.getTime()).plusMonths(month);
		return dt.toDate();
	}
	
	/**
	 * 比较两个日期的大小
	 * 
	 * @param date1
	 * @param date2
	 * @return date1小于date2 返回-1，date1大于date2返回1，相等返回0
	 */
	public static int compareDate(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		// different date might have different offset
		cal1.setTime(date1);
		long ldate1 = date1.getTime() + cal1.get(Calendar.ZONE_OFFSET)
				+ cal1.get(Calendar.DST_OFFSET);

		cal2.setTime(date2);
		long ldate2 = date2.getTime() + cal2.get(Calendar.ZONE_OFFSET)
				+ cal2.get(Calendar.DST_OFFSET);

		long dateDiff = ldate1 - ldate2;
		return dateDiff > 0 ? 1 : (dateDiff == 0 ? 0 : -1);
	}
	
	/**
	 * 取今天的 0时0分0秒0毫秒
	 * @return
	 */
	public static Date getStartOfCurrentDay() {
		return DateTime.now().withHourOfDay(0).withMinuteOfHour(0)
				.withSecondOfMinute(0).withMillisOfSecond(0).toDate();
	}
	
	/**
	 * Date转为指定格式的Date
	 * @param date
	 * @return
	 */
	public static Date dateToFmtDate(Date date, String format) {
		return parseDate(getDateStr(date, format), format);
	}
	
	/**
	 * 将java.util.Date 转换为 Timestamp
	 * @param dt
	 * @return
	 */
	public static Timestamp dateToTimestamp(Date dt) {
		Timestamp result = null;
		if (dt != null) {
			result = new Timestamp(dt.getTime());
		}
		return result;
	}
	
	/**
	 * Object 转化为 Date 
	 * o = null return null 
	 * o!= null return parseDate(o.toString(),fmt)
	 */
	public static Date objectToDate(Object o, String fmt) {
		if (o == null)
			return null;
		else {
			return parseDate(o.toString(), fmt);
		}
	}
	
	/**
	 * object转化时间格式字符串
	 * @param value
	 * @return
	 */
	public static String objectToDateStr(Object value, String format) {
		String transStr = StringUtils.EMPTY;
		try {
			if (value instanceof Date) {
				Date v = (Date) value;
				transStr = getDateStr(v, format);
			}
		} catch (Exception e) {
			log.error(e);
		}
		return transStr;
	}
	
	/**
	 * object转化为时间格式yyyy-MM-dd HH:mm:ss的字符串 
	 * @param value
	 * @return
	 */
	public static String objectToDateStr(Object value) {
		return objectToDateStr(value, FORMAT_PATTERN_YMDHMS);
	}

	private static void checkNotNull(Object obj){
		if(obj==null){
			throw new IllegalArgumentException("Argument can't be null!");
		}
	}
}
