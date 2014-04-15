package com.purbon.pdfwriter.chart.Impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.RegularTimePeriod;

class ChartUtils {

	private static Logger log = Logger.getLogger(ChartUtils.class.getName());

	private Pattern datePattern;
	private Pattern hourPattern;
	
	public ChartUtils() {
		datePattern  = Pattern.compile("^(\\d{1,2}\\/\\d{1,2}\\/\\d{4})$");
		hourPattern = Pattern.compile("^(\\d{1,2}:\\d{2})$");
	}
		
	public RegularTimePeriod parseTimeString(String timeString) {
		RegularTimePeriod time = null;
		if (isDate(timeString)) {
  			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
  			try {
				Date myDate = sdf.parse(timeString);
				time = new Day(myDate);
			} catch (ParseException e) {
				log.info(timeString, e);
			}
		} else if (isHour(timeString)) {
			Date myDate = new Date();
			int myHour = Integer.valueOf(timeString.split(":")[0]);
			time = new Hour(myHour, new Day(myDate));
		} else {
			log.info(timeString);
			throw new IllegalArgumentException();
		}
		return time;
	}
	
	private boolean isDate(String input) {
		   return isA(datePattern, input);
		}
		
		private boolean isHour(String input) {
			   return isA(hourPattern, input);
		}
		
		private boolean isA(Pattern pattern, String input) {
		   Matcher m = pattern.matcher(input);
		   return m.find();
		} 
}
