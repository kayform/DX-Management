/*
 * K4M, License, Version 1.1
 *
 * Copyright (c) 2000 K4M.  All rights  reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are prohibited.
 */
package com.k4m.eXperdb.webconsole.util;

/**
 * See \readme for info.
 * 
 * @author Jun Ho Park (jhpark@k4m.com)
 */

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	private static long prevTime = -1;

	public static Date Str2Date(String str) {
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy/MM/dd,HH:mm:ss").parse(str);
		} catch (Exception e) {
		}
		return date;
	}

	public static Date Str2Date(String str, String pattern) {
		Date date = null;
		try {
			date = new SimpleDateFormat(pattern).parse(str);
		} catch (Exception e) {
		}
		return date;
	}

	public static String Date2Str(Date date) {
		String str = null;
		try {
			str = new SimpleDateFormat("yyyy/MM/dd,HH:mm:ss").format(date);
		} catch (Exception e) {
		}
		return str;
	}

	public static String Date2Str(Date date, String pattern) {
		String str = null;
		try {
			str = new SimpleDateFormat(pattern).format(date);
		} catch (Exception e) {
		}
		return str;
	}

	public static String Date2Str(long date, String pattern) {
		String str = null;
		try {
			str = (String) new SimpleDateFormat(pattern).format(new Date(date));
		} catch (Throwable e) {
		}
		return str;
	}

	public static String Str2Str(String str, String srcPattern, String targetPattern) {
		Date date = null;
		try {

			if (str == null || "".equalsIgnoreCase(str)) {
				return "";
			}

			date = new SimpleDateFormat(srcPattern).parse(str);
		} catch (Exception e) {
		}
		return Date2Str(date.getTime(), targetPattern);
	}

	// 유니크한 현재 시간 값을 반환한다.
	public static synchronized long getUniqueTime() {
		long newTime = -1;
		while (true) {
			newTime = System.currentTimeMillis();
			if (newTime != prevTime) {
				break;
			}
			try {
				Thread.sleep(1);
			} catch (Exception e) {
			}
		}
		prevTime = newTime;
		return newTime;
	}

	/**
	 * format 타입의 현재시간 생성
	 * 
	 * @param format
	 * @return
	 */
	public static String getNowDate(String format) {
		return new SimpleDateFormat(format).format(new Date());
	}

	/**
	 * 현재 시간에 대한 년월일시분초 형태의 스트링 반환
	 */
	public static String getYearMonthDateHourMinSec() {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	}

	/**
	 * 지정된 시간에 대한 년월일시분초 형태의 스트링 반환
	 */
	public static String getYearMonthDateHourMinSec(long date) {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date(date));
	}

	/**
	 * 현재 시간에 대한 년월일 형태의 스트링 반환
	 */
	public static String getYearMonthDate() {
		return new SimpleDateFormat("yyyyMMdd").format(new Date());
	}

	/**
	 * 지정된 시간에 대한 년월일 형태의 스트링 반환
	 */
	public static String getYearMonthDate(long date) {
		return new SimpleDateFormat("yyyyMMdd").format(new Date(date));
	}
	/**
	 * 한자리 날짜인 경우 앞에 0을 생성해서 반환
	 * @param Date
	 * @return
	 */
	public static String dateCheck(String Date) {
		String str= "";
		if(Date.length() == 1) {
			if(Date.equals("*")) {
				str = Date;
			} else {
				str = "0" + Date;
			}
		} else {
			str = Date;
		}
		return str;
	}
}