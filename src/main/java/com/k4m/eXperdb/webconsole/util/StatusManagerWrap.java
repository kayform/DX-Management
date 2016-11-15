package com.k4m.eXperdb.webconsole.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StatusManagerWrap {
	private static StatusManagerWrap instance;
	
	public synchronized static StatusManagerWrap getInstance() {
		if (instance == null) {
			instance = new StatusManagerWrap();
		}
		return instance;
	}

	private StatusManagerWrap() {
	}

	public Map<String, String> getWebConsoleStatus() {
		Map<String, String> statusMap = new HashMap<String, String>();

		ResourceMonitorProcess resourceMonitorProcess = ResourceMonitorProcess.getInstance();
		Map<String, String> monitorMap = resourceMonitorProcess.getMonitorMap();
		statusMap = resourceMonitorProcess.getRealTimeResource();
		long memTotal = Long.parseLong(monitorMap.get(ResourceMonitorProcess.KEY_MEMORY_MAX));
		if (memTotal < 1) {
			memTotal = 1l;
		}
		long memUsed = Long.parseLong(monitorMap.get(ResourceMonitorProcess.KEY_MEMORY_CURRENT));
		long memAvail = memTotal - memUsed;
		double memUsedPer = (memUsed * 10000 / memTotal) / 100.0;
		double memAvailPer = (memAvail * 10000 / memTotal) / 100.0;
		
		// Caculate the 'Uptime'
		Date srvStartTime = SystemStaticInfo.getStartedTime();
		
		Date now = new Date();
		long gap = now.getTime() - srvStartTime.getTime();
		long gapDay, gapHour, gapMin, gapSec;

		// Get days of 'Uptime'
		if ((gap % (1000 * 60 * 60 * 24)) >= 1)
			gapDay = gap / (1000 * 60 * 60 * 24);
		else
			gapDay = 0;
		gap = (int) gap % (1000 * 60 * 60 * 24);

		// Get hours of 'Uptime'
		if ((gap % (1000 * 60 * 60)) >= 1)
			gapHour = gap / (1000 * 60 * 60);
		else
			gapHour = 0;
		gap = (int) gap % (1000 * 60 * 60);

		// Get minutes of 'Uptime'
		if ((gap % (1000 * 60)) >= 1)
			gapMin = (long) Math.floor(gap / (1000 * 60));
		else
			gapMin = 0;
		gap = (int) gap % (1000 * 60);

		// Get seconds of 'Uptime'
		gapSec = (long) Math.floor(gap / 1000);

		String upTime = gapDay + " 일 " + (gapHour < 10 ? "0" + gapHour : "" + gapHour) + "시간 " + (gapMin < 10 ? "0" + gapMin : "" + gapMin) + "분 " + (gapSec < 10 ? "0" + gapSec : "" + gapSec) + "초 경과";
		
		statusMap.put("startTime", DateUtils.Date2Str(srvStartTime));
		statusMap.put("upTime", upTime);
		statusMap.put("productCategory", "D-Specto");
		statusMap.put("version", "1.2");
		statusMap.put("patchNumber", "1.0");
		statusMap.put("totalMemory", String.valueOf(memTotal));
		statusMap.put("usedMemory", String.valueOf(memUsed));
		statusMap.put("availableMemory", String.valueOf(memAvail));
		statusMap.put("usedMemoryPercent", String.valueOf(memUsedPer));
		statusMap.put("availableMemoryPercent", String.valueOf(memAvailPer));
		statusMap.put("thread", String.valueOf(ThreadManager.getThreadCount()));

		//short dbmsCount = LicenseProcess.getInstance().getDbmsCount();;
		short dbmsCount = 0;
		statusMap.put("dbmsCount", String.valueOf(dbmsCount));
		
		return statusMap;
	}
}

