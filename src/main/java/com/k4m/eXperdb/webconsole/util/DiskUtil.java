package com.k4m.eXperdb.webconsole.util;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.k4m.eXperdb.webconsole.common.Globals;

public class DiskUtil {


	private static final String usableSpaceMethodName = "getUsableSpace";
	private static final String freeSpaceMethodName = "getFreeSpace";
	private static final String totalSpaceMethodName = "getTotalSpace";

	private String homeDir = Globals.HOME_DIR;//
	private File[] roots;
	private String[] rootPaths = new String[0];
	private String[] rootNames = new String[0];

	private Method usableSpaceMethod;
	private Method freeSpaceMethod;
	private Method totalSpaceMethod;

	//private SigarUtil sigarUtil;

	public DiskUtil() {
		load();
	}

	public void setHomePath(String homeDir) {
		this.homeDir = homeDir;
		load();
	}

	private void load() {
		File home = new File(homeDir);
		roots = new File[] { home };
		if (home != null) {
			rootPaths = new String[1];
			rootNames = new String[1];
			for (int i = 0; i < 1; i++) {
				rootPaths[i] = home.getAbsolutePath();
				rootNames[i] = home.getName();
			}
		}

			// JAVA 1.6 이상일 경우 java.lang.File을 이용
			try {
				usableSpaceMethod = File.class.getDeclaredMethod(usableSpaceMethodName);
				freeSpaceMethod = File.class.getDeclaredMethod(freeSpaceMethodName);
				totalSpaceMethod = File.class.getDeclaredMethod(totalSpaceMethodName);
			} catch (SecurityException e) {
			} catch (NoSuchMethodException e) {
			}
		
	}

	/**
	 * root 디렉토리 개수
	 * 
	 * @return
	 */
	public int rootCount() {
		if (roots != null) {
			return roots.length;
		} else {
			return 0;
		}
	}

	/**
	 * root path 목록
	 * 
	 * @return
	 */
	public String[] getRootPaths() {
		return rootPaths;
	}

	/**
	 * root name 목록
	 * 
	 * @return
	 */
	public String[] getRootNames() {
		return rootPaths;
	}

	/**
	 * 디스크 사용량
	 * 
	 * @param index
	 * @return
	 */
	public Disk getDisk(int index) {
		load();
		Disk disk = new Disk();
		disk.setFile(roots[index]);
			long total = getTotalSpace(roots[index]);
			long free = getFreeSpace(roots[index]);
			disk.setUsed(total - free);
			disk.setTotal(total);
		
		return disk;
	}

	/**
	 * 디스크 사용량
	 * 
	 * @param index
	 * @return
	 */
	public Disk[] getDisks() {
		load();
		Disk[] disks = new Disk[roots.length];
		for (int index = 0; index < roots.length; index++) {
			disks[index] = new Disk();
			disks[index].setFile(roots[index]);
				disks[index].setUsed(getUsableSpace(roots[index]));
				// java 1.6의 File.getFreeSpace() 메소드를 통해 구해지는 값에 오류가 있음.
				long total = getTotalSpace(roots[index]);
				long free = getFreeSpace(roots[index]);
				disks[index].setUsed(total - free);
				disks[index].setTotal(total);
		}
		return disks;
	}

	public class Disk {
		private File file;
		private long used = -1L;
		// java 1.6의 File.getFreeSpace() 메소드를 통해 구해지는 값에 오류가 있음.
		//private long free = -1L;
		private long total = -1L;

		public long getUsed() {
			return used;
		}

		public void setUsed(long used) {
			this.used = used;
		}

		//		public long getFree() {
		//			return free;
		//		}
		//
		//		public void setFree(long free) {
		//			this.free = free;
		//		}

		public long getTotal() {
			return total;
		}

		public void setTotal(long total) {
			this.total = total;
		}

		public File getFile() {
			return file;
		}

		public void setFile(File file) {
			this.file = file;
		}

		public String getName() {
			return file.getName();
		}

		public String getPath() {
			return file.getAbsolutePath();
		}
	}

	/**
	 * used disk size(1.6에서만 지원)
	 * 
	 * @param file
	 * @return
	 */
	private long getUsableSpace(File file) {
		if (usableSpaceMethod != null) {
			Long usable = -1L;
			try {
				usable = (Long) usableSpaceMethod.invoke(file);
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			} catch (InvocationTargetException e) {
			}
			return usable;
		} else {
			return -1L;
		}
	}

	/**
	 * free disk size(1.6에서만 지원)
	 * java 1.6의 File.getFreeSpace() 메소드를 통해 구해지는 값에 오류가 있음.(TODO)
	 * @param file
	 * @return
	 */
	private long getFreeSpace(File file) {
		if (freeSpaceMethod != null) {
			Long usable = -1L;
			try {
				usable = (Long) freeSpaceMethod.invoke(file);
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			} catch (InvocationTargetException e) {
			}
			return usable;
		} else {
			return -1L;
		}
	}

	/**
	 * total disk size
	 * 
	 * @param file
	 * @return
	 */
	private long getTotalSpace(File file) {
		if (totalSpaceMethod != null) {
			Long usable = -1L;
			try {
				usable = (Long) totalSpaceMethod.invoke(file);
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			} catch (InvocationTargetException e) {
			}
			return usable;
		} else {
			return -1L;
		}
	}
}
