package com.sf.module.pcss.datafbk.DIYTemplet.biz.reports.utils;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.lf5.LogLevel;

import com.sf.module.pcss.commons.utils.DateUtils;

/**
 * 计时器，用于分析报表性能
 * 
 * @author 449632 2014-9-30
 */
public class ReportClock {

	private static final Log log = LogFactory.getLog(ReportClock.class);
	
	protected Date first = null;
	protected Date last = null;
	protected Date cur = null;
	/**时间类型 -- 毫秒**/
	public static final int TIME_TYPE_MILLISECOND = 0;
	/**时间类型 -- 秒**/
	public static final int TIME_TYPE_SECONDS = 1;	
	/**时间类型 -- 分钟**/
	public static final int TIME_TYPE_MINUTES = 2;
	protected int timeType;							//时间类型
	protected LogLevel logLvl = LogLevel.INFO; 		// 默认log级别

	/**
	 * 默认
	 */
	public ReportClock() {
		this("Clock start at: " + DateUtils.now(), TIME_TYPE_SECONDS);
	}

	/**
	 * 
	 * @param msg
	 * @param timeType
	 */
	public ReportClock(String msg, int timeType) {
		last = DateUtils.now();
		cur  = last;
		first = last;//初始化第一次
		this.timeType = timeType;
		log(msg);
	}
	
	/**
	 * 计时方法
	 * @param msg
	 */
	public void clock(String msg){
		last = cur;
		cur = DateUtils.now();
		log(msg + ": " + getIntervalTime());
	}
	
	/**
	 * 计算时间间隔
	 * 
	 * @return
	 */
	protected String getIntervalTime() {
		String tmStr = "";
		switch (timeType) {
		case 0: // 毫秒
			tmStr = (last.getTime() - cur.getTime()) + "毫秒";
			break;
		case 1: // 秒
			tmStr = DateUtils.getDiffMillisecond(last, cur) + "秒";
			break;
		case 2: // 分钟
			tmStr = DateUtils.getDiffMins(last, cur) + "分钟";
			break;
		}
		return tmStr;
	}

	/**
	 * 设置log级别
	 * @param logLvl
	 */
	public void setLogLvl(LogLevel logLvl) {
		this.logLvl = logLvl;
	}
	
	/**
	 * 输出日志
	 * @param msg
	 */
	protected void log(String msg){
		if(logLvl == LogLevel.INFO){
			log.info(msg);
		}else if(logLvl == LogLevel.DEBUG){
			log.debug(msg);
		}else if(logLvl == LogLevel.ERROR){
			log.error(msg);
		}
	}

}
