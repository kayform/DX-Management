package com.k4m.eXperdb.webconsole.util;

public class ThreadManager {
	public static Thread[] getThreadList() {
		int i = Thread.activeCount() * 2;
		Thread athread[] = new Thread[i];
		int j = Thread.enumerate(athread);
		Thread athread1[] = new Thread[j];
		System.arraycopy(athread, 0, athread1, 0, j);
		return athread1;
	}

	public static int getThreadCount() {
		return getThreadList().length;
	}
}
