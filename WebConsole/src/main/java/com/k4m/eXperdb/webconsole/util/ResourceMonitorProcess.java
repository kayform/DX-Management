package com.k4m.eXperdb.webconsole.util;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.k4m.eXperdb.webconsole.common.Globals;
import com.k4m.eXperdb.webconsole.util.DiskUtil.Disk;
import com.k4m.eXperdb.webconsole.util.SigarUtil.CPU;

public class ResourceMonitorProcess {
	/** map key */
	public static final String KEY_CHANNEL_ID = "ChannelId";
	public static final String KEY_CHECK_BEG_TIME = "CheckBeginTime";
	public static final String KEY_CHANNEL_TYPE = "ChannelType";
	public static final String KEY_CPU_CURRENT = "CpuCurrent";
	public static final String KEY_DISK_MAX = "DiskMax";
	public static final String KEY_DISK_CURRENT = "DiskCurrent";
	public static final String KEY_MEMORY_MAX = "MemoryMax";
	public static final String KEY_MEMORY_CURRENT = "MemoryCurrent";
	public static final String KEY_MEMORY_ALLOC = "MemoryAlloc";
	public static final String KEY_THREAD_CURRENT = "ThreadCurrent";
	public static final String KEY_SOCKET_ACTIVE = "SocketActive";
	public static final String KEY_SOCKET_IDLE = "SocketIdle";
	public static final String KEY_MP_ACTIVE = "MPActive";
	public static final String KEY_MP_IDLE = "MPIdle";

	public static final long CPU_CHECK_TH = 5000; // CPU 체크 Threshold

	/** logger */

	/** single ton instance */
	private static ResourceMonitorProcess instance;

	/** schedule interval */
	private Timer monitorTimer = new Timer(false);

	/** Monitor Task */
	private TimerTask monitorTimerTask = new MonitorTimerTask();

	/** sigar */
	private SigarUtil sigarUtil;

	/** disk ( jdk 1.6 이상일 경우에만 가능 ) */
	private DiskUtil diskUtil = new DiskUtil();

	/** 모니터링 task 시작 여부 */
	private boolean start = false;

	/** 모니터링 task 시작 여부 */

	/** 모니터링 values */
	private long checkBeginTime;
	private long cpuCurrent;
	private long diskMax;
	private long diskCurrent;
	private long memoryMax;
	private long memoryCurrent;
	private long memoryAlloc;
	private long threadCurrent;
	private long socketActive;
	private long socketIdle;
	private long mpActive;
	private long mpIdle;

	private long cpuCheckTime = 0;

	private long checkInterval = 60000; // 시스템 리스소 모니터링 쓰레드의 체크 주기(단위:1/1000초)

	/**
	 * 인스턴스 생성
	 * 
	 * @return
	 */
	public synchronized static ResourceMonitorProcess getInstance() {
		if (instance == null) {
			instance = new ResourceMonitorProcess();
		}
		return instance;
	}

	/**
	 * 외부에서 인스턴스 생성 금지.
	 */
	private ResourceMonitorProcess() {
		try {
			sigarUtil = new SigarUtil();
		} catch (Throwable t) {
		}

		try {
		} catch (Exception e) {
		}
	}

	public void setCheckInterval(String checkInterval) {
		try {
			long newCheckInterval = Long.parseLong(checkInterval);
			if (this.checkInterval != newCheckInterval) {
				this.checkInterval = newCheckInterval;
				ResourceMonitorProcess.this.reset();
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 모니터링 Task 종료
	 */
	public static void stop() {
		if(instance != null) {
			try {
				instance.monitorTimerTask.cancel();
				instance.monitorTimer.cancel();
			} finally {
				instance.start = false;
			}
		}
	}

	/**
	 * 모니터링 Task 시작
	 */
	public void start() {
		try {
			monitorTimerTask = new MonitorTimerTask();
			monitorTimer = new Timer("eCross_SRM", false);
			monitorTimer.schedule(monitorTimerTask, checkInterval, checkInterval);
		} finally {
			start = true;
		}
	}

	/**
	 * 모니터링 Task 시작 여부.
	 * 
	 * @return
	 */
	public boolean isStart() {
		return start;
	}

	/**
	 * 모니터링 정보를 가진 HashMap을 반환한다.
	 * 
	 * @return Map<String, String>
	 */
	public Map<String, String> getMonitorMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put(KEY_CHECK_BEG_TIME, new Long(checkBeginTime).toString());
		map.put(KEY_CHANNEL_TYPE, ""); // 설정하지 않음. 설정이 필요하다면, getMonitorMap을 호출한 곳에서 설정.
		map.put(KEY_DISK_MAX, new Long(diskMax).toString());
		map.put(KEY_DISK_CURRENT, new Long(diskCurrent).toString());
		map.put(KEY_CPU_CURRENT, new Long(cpuCurrent).toString());
		map.put(KEY_MEMORY_MAX, new Long(memoryMax).toString());
		map.put(KEY_MEMORY_CURRENT, new Long(memoryCurrent).toString());
		map.put(KEY_MEMORY_ALLOC, new Long(memoryAlloc).toString());
		map.put(KEY_THREAD_CURRENT, new Long(threadCurrent).toString());
		map.put(KEY_SOCKET_ACTIVE, new Long(socketActive).toString());
		map.put(KEY_SOCKET_IDLE, new Long(socketIdle).toString());
		map.put(KEY_MP_ACTIVE, new Long(mpActive).toString());
		map.put(KEY_MP_IDLE, new Long(mpIdle).toString());
		return map;
	}

	/**
	 * CPU 사용률을 반환한다.
	 * 
	 * 이전에 체크한 시간과 현재시간을 비교하여 CPU_CHECK_TH 보다 크면 체크를 수행한 결과를 반환하고, 그렇지 않으면 이전에 체크한 결과를 반환한다.
	 * 반환되는 값은 Percentage(%)를 나타내는 값이다.
	 * 
	 * @return long
	 */
	public long getCpu() {
		if ((System.currentTimeMillis() - cpuCheckTime) >= CPU_CHECK_TH) {
			setCPU();
		}
		return (cpuCurrent / 100);
	}
	
	public Map<String, String> getRealTimeResource() {
		Map<String, String> resource = new HashMap<String, String>();
		setCPU();
		setMemory();
		setDisk();
		
		resource.put("CpuCurrent", new Long(cpuCurrent).toString());
		resource.put("CPUPercent", new Long(getCpu()).toString());
		resource.put("MemoryMax", new Long(memoryMax).toString());
		resource.put("MemoryCurrent", new Long(memoryCurrent).toString());
		resource.put("MemoryAlloc", new Long(memoryAlloc).toString());
		resource.put("MemoryPercent", String.format("%.2f", (double)memoryCurrent/memoryAlloc * 100));
		resource.put("DiskMax", new Long(diskMax).toString());
		resource.put("DiskCurrent", new Long(diskCurrent).toString());
		resource.put("DiskPercent", String.format("%.2f", (double)diskCurrent/diskMax * 100));
		
		return resource;
	}

	/**
	 * 메모리 모니터링 정보 설정
	 */
	protected void setMemory() {
		try {
			//Memory memory = sigarUtil.getMemory();
			//memoryMax = Runtime.getRuntime().maxMemory();
			//memoryCurrent = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

			MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
			MemoryUsage memoryUsage = memoryBean.getHeapMemoryUsage();
			memoryMax = memoryUsage.getMax();
			memoryCurrent = memoryUsage.getUsed();
			memoryAlloc = Runtime.getRuntime().totalMemory();

			// if (logger.isDebugEnabled()) {
			// 	logger.debug("MEMORY MAX[" + memoryMax + "] CURRENT[" + memoryCurrent + "]");
			// }
		} catch (Throwable t) {
		}
	}

	/**
	 * 쓰레도 모니터링 정보 설정
	 */
	protected void setThread() {
		try {
			ThreadMXBean threadbean = ManagementFactory.getThreadMXBean();
			threadCurrent = threadbean.getThreadCount();

			// if (logger.isDebugEnabled()) {
			// 	logger.debug("THREAD MAX[" + threadMax + "] CURRENT[" + threadCurrent + "]");
			// }
		} catch (Throwable t) {
		}
	}

	/**
	 * CPU 모니터링 정보 설정
	 */
	protected synchronized void setCPU() {
		try {
			if ((System.currentTimeMillis() - cpuCheckTime) >= CPU_CHECK_TH) {
				CPU cpu = sigarUtil.getCPUTotal();
				cpuCurrent = (long) (cpu.getTotal() * 10000); // 10000을 곱한 값으로 설정한다.
				cpuCheckTime = System.currentTimeMillis();
			}

			// if (logger.isDebugEnabled()) {
			// logger.debug("CPU CURRENT[" + cpuCurrent + "][" + cpu.getTotal() + "]" + cpu.getTotalFormat());
			// }
		} catch (Throwable t) {
		}
	}

	/**
	 * 디스크 모니터링 정보 설정
	 */
	protected void setDisk() {
		try {
			String home = new File(Globals.HOME_DIR).getAbsolutePath();
			Disk[] disks = diskUtil.getDisks();
			for (Disk disk : disks) {
				if (home.startsWith(disk.getPath())) {
					diskMax = disk.getTotal();
					diskCurrent = disk.getUsed();
					// if (logger.isDebugEnabled()) {
					// 	logger.debug("DISK MAX[" + diskMax + "] CURRENT[" + diskCurrent + "]");
					// }
				}
			}
		} catch (Throwable t) {
		}
	}

	

	protected void reset() {
		stop();
		start();
	}

	/** Monitor Task */
	private class MonitorTimerTask extends TimerTask {
		@Override
		public void run() {
			try {
				checkBeginTime = System.currentTimeMillis();
				setMemory();
				setThread();
				setCPU();
				setDisk();

			} catch (Throwable t) {
			}
		}
	}
}
