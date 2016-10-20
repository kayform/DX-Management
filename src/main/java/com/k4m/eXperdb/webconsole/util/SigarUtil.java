package com.k4m.eXperdb.webconsole.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarLoader;
import org.hyperic.sigar.Swap;

import com.k4m.eXperdb.webconsole.common.Globals;

public class SigarUtil {
	//private static Logger logger = Log.getLogger(Consts.SYSTEM_LOG_CATEGORY);

	public SigarUtil() throws Exception {
		try {
			SigarLoader loader = new SigarLoader(Sigar.class);
			if (SigarLoader.IS_WIN32 && System.getProperty("os.version").equals("4.0")) {
				String lib = loader.findJarPath("pdh.dll") + File.separator + "pdh.dll";
				System.load(lib);
			}
			loader.load();
		} catch (Throwable e) {
			// sigar library
			//logger.info("dynamic sigar library loading...");
			dynamicLibraryLoading();
		}
		sigar = new Sigar();
		try {
			if (cpuArray == null) {
				cpuArray = sigar.getCpuInfoList();
			}
		} catch (SigarException e) {
			throw new Exception(e.getMessage());
		}
	}

	private Sigar sigar;

	private CpuInfo[] cpuArray = null;

	/**
	 * Sigar library를 동적으로 로딩하도록 한다.
	 */
	private static void dynamicLibraryLoading() {
		try {
			String fildClass = "sigar-amd64-winnt.dll";
			URL url = Thread.currentThread().getContextClassLoader().getResource(fildClass);
			if (url == null) {
				url = Thread.currentThread().getContextClassLoader().getResource("/" + fildClass);
			}

			String java_io_tmpdir = System.getProperty(Globals.HOME_DIR);
			String sigar_tmpdir = new File(java_io_tmpdir).getAbsolutePath() + File.separator + "shlib" + File.separator + "sigar_shlib";

			File sigar_tmpdirFile = new File(sigar_tmpdir);
			if (!sigar_tmpdirFile.exists()) {
				sigar_tmpdirFile.mkdirs();
			}

			String path = url.getPath();
			if (path.indexOf("!") > -1) {

				//logger.info("dynamic sigar library loading...");

				File jarFile = new File(path.substring(5, path.indexOf("!")));

				java.util.jar.JarFile jar = null;
				try {
					jar = new JarFile(jarFile.getAbsoluteFile());
				} catch (Exception e) {
					jar = new JarFile(URLDecoder.decode(jarFile.getAbsolutePath(), "UTF-8"));
					//logger.info(jar.getName());
				}

				Enumeration<JarEntry> entries = jar.entries();
				JarEntry entry = null;
				while (entries.hasMoreElements()) {
					entry = entries.nextElement();
					if (!entry.isDirectory()) {
						String name = entry.getName();
						name = name.substring(name.lastIndexOf("/") + 1);
						//if (logger.isDebugEnabled()) {
						//logger.debug("entry.getName() : " + name);
						//logger.debug("entry.getSize() : " + entry.getSize());
						//}
						writeFile(new File(sigar_tmpdir + File.separator + name), jar.getInputStream(entry));
					}
				}

				String java_library_path = System.getProperty("java.library.path") + File.pathSeparator + sigar_tmpdir;
				System.setProperty("java.library.path", java_library_path);
			}

		} catch (Exception e) {
			Globals.logger.error(e.getMessage(), e);
			//logger.error("Sigar shlib module write error.", e);
		}
	}

	/**
	 * CPU count
	 * 
	 * @return
	 */
	public int availableProcessors() {
		if (sigar != null) {
			if (cpuArray != null) {
				return cpuArray.length;
			} else {
				return Runtime.getRuntime().availableProcessors();
			}
		} else {
			return Runtime.getRuntime().availableProcessors();
		}
	}

	/**
	 * index 번호의 CPU 모니터링 정보
	 * 
	 * @param index
	 * @return
	 * @throws Exception
	 */
	public CPU getCPUTotal() throws Exception {
		CPU cpu = new CPU();
		try {
			CpuPerc cpuPers = sigar.getCpuPerc();
			cpu.setIndex(-1);
			cpu.setUser(cpuPers.getUser());
			cpu.setSys(cpuPers.getSys());
			cpu.setWait(cpuPers.getWait());
			cpu.setNice(cpuPers.getNice());
			cpu.setIdle(cpuPers.getIdle());
			cpu.setTotal(cpuPers.getCombined());
		} catch (SigarException e) {
			throw new Exception(e.getMessage());
		}

		return cpu;
	}

	/**
	 * index 번호의 CPU 모니터링 정보
	 * 
	 * @param index
	 * @return
	 * @throws Exception
	 */
	public CPU getCPU(int index) throws Exception {
		CPU cpu = new CPU();
		try {
			CpuPerc[] cpuPersArray = sigar.getCpuPercList();
			cpu.setIndex(index);
			cpu.setUser(cpuPersArray[index].getUser());
			cpu.setSys(cpuPersArray[index].getSys());
			cpu.setWait(cpuPersArray[index].getWait());
			cpu.setNice(cpuPersArray[index].getNice());
			cpu.setIdle(cpuPersArray[index].getIdle());
			cpu.setTotal(cpuPersArray[index].getCombined());
		} catch (SigarException e) {
			throw new Exception(e.getMessage());
		}

		return null;
	}

	/**
	 * CPU가 여러개일 때 모니터링 정보
	 * 
	 * @return
	 * @throws Exception
	 */
	public CPU[] getCPUAll() throws Exception {
		CPU[] cpuArray = null;
		try {
			CpuPerc[] cpuPersArray = sigar.getCpuPercList();
			cpuArray = new CPU[cpuPersArray.length];
			for (int index = 0; index < cpuPersArray.length; index++) {
				cpuArray[index] = new CPU();
				cpuArray[index].setIndex(index);
				cpuArray[index].setUser(cpuPersArray[index].getUser());
				cpuArray[index].setSys(cpuPersArray[index].getSys());
				cpuArray[index].setWait(cpuPersArray[index].getWait());
				cpuArray[index].setNice(cpuPersArray[index].getNice());
				cpuArray[index].setIdle(cpuPersArray[index].getIdle());
				cpuArray[index].setTotal(cpuPersArray[index].getCombined());
			}
		} catch (SigarException e) {
			throw new Exception(e.getMessage());
		}

		return cpuArray;
	}

	/**
	 * 메모리 정보
	 * 
	 * @return
	 * @throws Exception
	 */
	public Memory getMemory() throws Exception {
		try {
			Mem mem = sigar.getMem();
			Swap swap = sigar.getSwap();
			Memory memory = new Memory();

			// physical memory
			memory.setUsed(new Long(mem.getUsed() / 1024));
			memory.setFree(new Long(mem.getFree() / 1024));
			memory.setActualUsed(new Long(mem.getActualUsed() / 1024));
			memory.setActualFree(new Long(mem.getActualFree() / 1024));
			memory.setTotal(new Long(mem.getTotal() / 1024));

			// swap memory
			memory.setSwapUsed(new Long(swap.getUsed() / 1024));
			memory.setSwapFree(new Long(swap.getFree() / 1024));
			memory.setSwapTotal(new Long(swap.getTotal() / 1024));

			return memory;
		} catch (SigarException e) {
			throw new Exception(e.getMessage());
		}
	}

	public static void writeFile(File file, InputStream is) {

		FileOutputStream fos = null;
		try {
			byte[] bytes = new byte[1024];
			int len = 0;
			fos = new FileOutputStream(file);
			while ((len = is.read(bytes)) != -1) {
				fos.write(bytes, 0, len);
			}
			fos.flush();
		} catch (Exception e) {
			Globals.logger.error(e.getMessage(), e);
			//logger.error("Sigar shlib module write error.", e);
		} finally {
			try {
				if (fos != null)
					fos.close();
			} catch (Exception e) {
				Globals.logger.error(e.getMessage(), e);
				//logger.error("Sigar shlib module write error.", e);
			}
		}
	}

	/*
	 * Sigar를 통해 디스크를 구할수 없음
	 * JDK 1.5에서는 사용량 구할 수 없음
	 * JDK 1.6에서는 사용량 구하는데 시간이 너무 많이 걸림
	 */
	//	public void disk() {
	//		try {
	//			FileSystem[] disks = sigar.getFileSystemList();
	//			for (FileSystem disk : disks) {
	//				System.out.print("DISK-" + disk.getDirName());
	//				DirUsage usage = sigar.getDirUsage(disk.getDirName());
	//				long usageValue = usage.getDiskUsage();
	//				long totalValue = usage.getTotal();
	//				System.out.print("\tFree-" + (totalValue - usageValue));
	//				System.out.print("\tUsage-" + (usageValue));
	//				System.out.print("\tTotal-" + (totalValue));
	//				System.out.println();
	//			}
	//			//uasge.get
	//		} catch (SigarException e) {
	//			e.printStackTrace();
	//		}
	//	}

	/**
	 * CPU 정보를 담기 위한 VO 클래스
	 * 
	 */
	public class CPU {

		private int index;
		private double user = -1d;
		private double sys = -1d;
		private double wait = -1d;
		private double nice = -1d;
		private double idle = -1d;
		private double total = -1d;

		public double getUser() {
			return user;
		}

		public void setUser(double user) {
			this.user = user;
		}

		public double getSys() {
			return sys;
		}

		public void setSys(double sys) {
			this.sys = sys;
		}

		public double getWait() {
			return wait;
		}

		public void setWait(double wait) {
			this.wait = wait;
		}

		public double getNice() {
			return nice;
		}

		public void setNice(double nice) {
			this.nice = nice;
		}

		public double getIdle() {
			return idle;
		}

		public void setIdle(double idle) {
			this.idle = idle;
		}

		public double getTotal() {
			return total;
		}

		public void setTotal(double total) {
			this.total = total;
		}

		public String getUserFormat() {
			if (user >= 0D) {
				return CpuPerc.format(user);
			} else {
				return "NaN";
			}
		}

		public String getSysFormat() {
			if (sys >= 0D) {
				return CpuPerc.format(sys);
			} else {
				return "NaN";
			}
		}

		public String getWaitFormat() {
			if (wait >= 0D) {
				return CpuPerc.format(wait);
			} else {
				return "NaN";
			}
		}

		public String getNiceFormat() {
			if (nice >= 0D) {
				return CpuPerc.format(nice);
			} else {
				return "NaN";
			}
		}

		public String getIdleFormat() {
			if (idle >= 0D) {
				return CpuPerc.format(idle);
			} else {
				return "NaN";
			}
		}

		public String getTotalFormat() {
			if (total >= 0D) {
				return CpuPerc.format(total);
			} else {
				return "NaN";
			}
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}
	}

	/**
	 * 메모리 정보를 담기위한 VO 클래스
	 * 
	 */
	public class Memory {
		private long used;
		private long free;
		private long actualUsed;
		private long actualFree;
		private long total;
		private long swapUsed;
		private long swapFree;
		private long swapTotal;

		public long getUsed() {
			return used;
		}

		public void setUsed(long used) {
			this.used = used;
		}

		public long getFree() {
			return free;
		}

		public void setFree(long free) {
			this.free = free;
		}

		public long getActualUsed() {
			return actualUsed;
		}

		public void setActualUsed(long actualUsed) {
			this.actualUsed = actualUsed;
		}

		public long getActualFree() {
			return actualFree;
		}

		public void setActualFree(long actualFree) {
			this.actualFree = actualFree;
		}

		public long getTotal() {
			return total;
		}

		public void setTotal(long total) {
			this.total = total;
		}

		public long getSwapUsed() {
			return swapUsed;
		}

		public void setSwapUsed(long swapUsed) {
			this.swapUsed = swapUsed;
		}

		public long getSwapFree() {
			return swapFree;
		}

		public void setSwapFree(long swapFree) {
			this.swapFree = swapFree;
		}

		public long getSwapTotal() {
			return swapTotal;
		}

		public void setSwapTotal(long swapTotal) {
			this.swapTotal = swapTotal;
		}
	}

	public class Disk {

	}
}
