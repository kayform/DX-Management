package com.k4m.eXperdb.webconsole.common;

import org.apache.log4j.Logger;



public class Globals {
	public static Logger logger = Logger.getLogger("k4m.zephyros.webconsole");

	// DATA 처리 모드
	public final static String MODE_DATA_INSERT = "I";
	public final static String MODE_DATA_UPDATE = "U";
	public final static String MODE_DATA_VIEW = "V";
	public final static String MODE_DATA_DELETE = "D";
	public final static String MODE_DATA_TEST = "T";
	public final static String MODE_DATA_RUN = "R";
	public final static String MODE_DATA_RUN_ONCE = "RO";
	public final static String MODE_DATA_CHANGE_ALL = "CA";
	public final static String MODE_DATA_CHANGE = "C";
	public final static String MODE_DATE_EXPORT = "E";
	public final static String MODE_DATE_RELOAD_CONF = "RC";
	public final static String MODE_DATE_SYSTEM_START = "ST";
	public final static String MODE_DATE_SYSTEM_STOP = "SP";
	public final static String MODE_DATE_LIBRARY_PATCH = "LP";
	
	public final static String DASHBOARD_REFRESH_INTERVAL = "dashboard.refresh.interval";
	
	//사용자 권한 (1=사용자, 2=관리자, 3=슈퍼관리자)
	public final static String USER_ROLE_USER = "1";
	public final static String USER_ROLE_ADMIN = "2";
	public final static String USER_ROLE_SUPER = "3";
	
	public final static String HOME_DIR = System.getProperty("dspecto.Home");
	
	
}
