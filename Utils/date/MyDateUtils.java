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
 * ʱ�䴦����
 * need: commons-logging-x.x.jar��joda-time-x.x.jar
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
	 * �����������Ƚ�������������ķ��� ,������
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
	 * �Ƚ������������������ ,������
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
	 * �Ƚ���������֮�������������һ���ȡ0
	 * date2-date1
	 */
	public static long getDiffYears(Date date1, Date date2) {
		long diffMins = getDiffMins(date1, date2);
		long diffYears = diffMins / (60 * 24 * 365);
		return diffYears;
	}
	
	/**
	 * �Ƚ������������������,���뾫ȷ��С�������λ
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
		value = value.setScale(2, BigDecimal.ROUND_HALF_UP); // ����С�������λ
		return value;
	}
	
	/**
	 * ����С�����Ƚ������������������
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
	 * �����ڸ�ʽ����һ�ַ���
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
	 * String ת��ΪDate
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
	 * ��������Ƿ�ָ����ʽ
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
	 * �з�yyyy-mm-dd HH:mm:ss ��ʽ���ж�ʱ���ʽverifyFmtʱ���õ�
	 * j = 0 i= 0 �õ�yyyy
	 * j = 0 i= 1�õ��·�
	 * j = 0 i= 2 �õ�����
	 * j = 1 i= 0 �õ�hh
	 * j = 1 i= 1�õ�mm
	 * j = 1 i= 2 �õ�ss
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
	 * �õ���ǰʱ��
	 * @return Date
	 */
	public static Date now() {
		return DateTime.now().toDate();
	}
	
	/**
	 * �õ���ǰʱ��
	 * @return DateTime
	 */
	public static DateTime nowDt() {
		return DateTime.now();
	}

	/**
	 * �õ���ǰ�·ݵĵ�һ��
	 */
	public static Date get1stDayOfThisMonth() {
		DateTime dt=nowDt();
		return new DateTime(dt.getYear(),dt.getMonthOfYear(),1, 0, 0, 0).toDate();
	}

	/**
	 * ����һ��ʱ��Σ�ʱ��ֻ��ָ��һ����λ�����ֱ���������
	 * <pre>
	 * parsePeriod(" ") -> null
	 * parsePeriod(null) -> null
	 * parsePeriod("2m") -> 2 minutes //ע��m��M������
	 * parsePeriod("3M") -> 3 months //ע��m��M������
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
	 * �˷������ص��ǵ��յ�23:59:59 ���벻Ҫ��ʹ�ô˷�����Ϊ��ѯ�ı߼�������
	 * ��ʹ��{@link #getTimeAtStartOfDay}����{@link #getTimeAtStartOfNextDay}
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
	 * ȡ��ǰ���ڵĿ�ʼʱ�䡣һ��ÿ���Ǵ�00:00:00��ʼ��
	 * ����ĳЩʵ����ʱ�ƵĹ��һ����ʱ��ʵ�еĵ��죬ʱ����ǰ��1Сʱ��11:59:59ֱ�ӵ��ڶ���01:00:00����
	 * 
	 * @param date
	 * @return null ���date==null
	 */
	public static Date getTimeAtStartOfDay(Date date) {
		Date result = null;
		if (date != null) {
			result = new DateTime(date).withTimeAtStartOfDay().toDate();
		}
		return result;
	}
	
	/**
	 * ȡ����һ�����ڵĿ�ʼʱ�䡣
	 * @param date
	 * @return null ���date==null
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
	 * ����ָ�����ڰ������ӣ�����٣�������ڡ�<br/>
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
	 * ����ָ�����ڰ������ӣ�����٣�������ڡ�<br/>
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
	 * ���ӷ�����
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
	 * ����ָ�����ڰ������ӣ�����٣�������ڡ�<br/>
	 * <ul>
	 * <li>ע�⣺���ӣ����٣���ʱ������Ӧȡ����Ӧ���ڡ��롰����������ڡ�֮�����Сֵ���ο��������ӡ�</li>
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
	 * �Ƚ��������ڵĴ�С
	 * 
	 * @param date1
	 * @param date2
	 * @return date1С��date2 ����-1��date1����date2����1����ȷ���0
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
	 * ȡ����� 0ʱ0��0��0����
	 * @return
	 */
	public static Date getStartOfCurrentDay() {
		return DateTime.now().withHourOfDay(0).withMinuteOfHour(0)
				.withSecondOfMinute(0).withMillisOfSecond(0).toDate();
	}
	
	/**
	 * DateתΪָ����ʽ��Date
	 * @param date
	 * @return
	 */
	public static Date dateToFmtDate(Date date, String format) {
		return parseDate(getDateStr(date, format), format);
	}
	
	/**
	 * ��java.util.Date ת��Ϊ Timestamp
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
	 * Object ת��Ϊ Date 
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
	 * objectת��ʱ���ʽ�ַ���
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
	 * objectת��Ϊʱ���ʽyyyy-MM-dd HH:mm:ss���ַ��� 
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
