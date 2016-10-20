package com.k4m.eXperdb.webconsole.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.TimeZone;

/**
 * 정적인 서버 정보를 제공한다.
 * 
 * @author mskim
 */
public class SystemStaticInfo {
	private static Date startedTime = null;

	private static InetAddress inetAddr = null;

	private static int cpuCoreCount = 0;

	private static TimeZone timeZone = null;

	private static Object inetAddrMutex = new Object();

	private static Object cpuCoreCountMutex = new Object();

	private static Object timeZoneMutex = new Object();

	static {
		startedTime = new Date();
		try {
			getInetAddress();
		} catch (UnknownHostException e) {
		}
		getCpuCoreCount();
	}

	public static Date getStartedTime() {
		return startedTime;
	}

	public static InetAddress getInetAddress() throws UnknownHostException {
		if (inetAddr != null) {
			return inetAddr;
		}
		synchronized (inetAddrMutex) {
			inetAddr = InetAddress.getLocalHost();
		}
		return inetAddr;
	}

	public static int getCpuCoreCount() {
		if (cpuCoreCount != 0) {
			return cpuCoreCount;
		}
		synchronized (cpuCoreCountMutex) {
			cpuCoreCount = Runtime.getRuntime().availableProcessors();			
		}
		return cpuCoreCount;
	}

	public static TimeZone getTimeZone() {
		if (timeZone != null) {
			return timeZone;
		}
		synchronized (timeZoneMutex) {
			timeZone = TimeZone.getDefault();
		}
		return timeZone;
	}
}
