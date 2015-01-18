package com.sf.module.pcss.datafbk.DIYTemplet.biz.reports.utils;

import java.math.BigDecimal;

import com.sf.module.pcss.commons.utils.DateUtils;

/**
 * 累加计时器
 * @author 449632
 * 2014-9-30
 */
public class CumulativeClock extends ReportClock {

	private String outputStr;
	public CumulativeClock(){
		super();
	}
	
	public CumulativeClock(String msg, int timeType){
		super(msg, timeType);
	}
	
	public void cumulate(String msg){
		this.cumulate(msg, false);
	}
	
	/**
	 * 累加计时记录，isEnd = true 时输出总记录
	 * @param msg
	 * @param isEnd
	 */
	public void cumulate(String msg, boolean isEnd){
		last = cur;
		cur = DateUtils.now();
		outputStr += msg + ": " + getIntervalTime() ;
		if(isEnd){
			log(outputStr);
			outputStr = "";
		}
	}
	
	/**
	 * 获得开始到结束的总时间
	 * @return
	 */
	public BigDecimal getWholeInterval(){
		return DateUtils.getDiffMillisecond(first, cur);
	}
}
