package com.k4m.eXperdb.webconsole.settings;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.k4m.eXperdb.webconsole.common.Globals;
import com.k4m.eXperdb.webconsole.util.DateUtils;
import com.k4m.eXperdb.webconsole.util.StatusManagerWrap;
import com.k4m.eXperdb.webconsole.util.ThreadManager;

@Service
public class SettingsServiceImpl implements SettingsService {	
	@Autowired
	private SettingsDAO settingsDAO;
	
	@Override
	public List<Map<String, Object>> getWebConsoleSystemProperties() throws Exception {
		
		List<Map<String, Object>> systemPropertyList = new ArrayList<Map<String, Object>>();

		Properties systemProperties = System.getProperties();

		// property name을 기준으로 정렬
		Vector<String> propertyNameVector = new Vector<String>();

		for (Enumeration<?> enumVar = systemProperties.propertyNames(); enumVar.hasMoreElements();) {
			propertyNameVector.add((String) enumVar.nextElement());
		}

		Collections.sort(propertyNameVector, new Comparator<Object>() {
			public int compare(Object a, Object b) {
				return (((String) a).toLowerCase()).compareToIgnoreCase(((String) b).toLowerCase());
			}
		});

		int propertiesLength = propertyNameVector.size();

		for (int i = 0; i < propertiesLength; i++) {
			Map<String, Object> item = new HashMap<String, Object>();

			String key = (String) propertyNameVector.get(i);//em.nextElement();
			String value = "";

			if (key.endsWith(".path")) {
				StringBuffer dirpath = new StringBuffer(System.getProperty(key));
				StringBuffer newpath = new StringBuffer();
				for (int j = 0; j < dirpath.length(); j++) {
					if (dirpath.charAt(j) == ';') {
						newpath.append(dirpath.charAt(j)).append(" ");
					} else {
						newpath.append(dirpath.charAt(j));
					}
				}
				value = newpath.toString();
			} else {
				value = System.getProperty(key);
			}
			item.put(key, value);
			systemPropertyList.add(item);
		}
		return systemPropertyList;
	}

	@Override
	public List<Map<String, Object>> selectUserList(HashMap<String, String> param) throws Exception {		
		List<Map<String, Object>> userList = settingsDAO.selectUserList(param);		
		return userList;
	}

	@Override
	public Map<String, Object> selectUser(HashMap<String, String> param) throws Exception {		
		List<Map<String,Object>> list = settingsDAO.selectUser(param);
		Map<String, Object> user = new HashMap<String, Object>();
		if (list.size() > 0) {
			user = list.get(0);
		} 		
		return user;
	}
	
	@Override
	public int deleteUser(HashMap<String, String> param) throws Exception {
		return settingsDAO.deleteUser(param);			
	}
	
	@Override
	public int insertUser(HashMap<String, String> param) throws Exception {		
		return settingsDAO.insertUser(param);	
	}
	
	@Override
	public int updateUser(HashMap<String, String> param) throws Exception {
		return settingsDAO.updateUser(param);			
	}
	
	@Override
	public int updateUserPassword(HashMap<String, String> param) throws Exception {
		return settingsDAO.updateUserPassword(param);	
	}
	
	@Override
	public List<Map<String, Object>> selectUserAuth(HashMap<String, String> param) throws Exception {
		return settingsDAO.selectUserAuth(param);
	}
	
	@Override
	public int insertUserAuth(HashMap<String, String> param) throws Exception {			
		return settingsDAO.insertUserAuth(param);
	}
	
	@Override
	public int updateUserAuthList(List<HashMap<String, String>> paramList) throws Exception {
		int count = 0;
		for(HashMap<String,String> param : paramList) {
			if(Globals.MODE_DATA_INSERT.equals(param.get("mode"))) {
				settingsDAO.deleteUserAuth(param);
				count += settingsDAO.insertUserAuth(param);
			} else if(Globals.MODE_DATA_DELETE.equals(param.get("mode"))) {
				count += settingsDAO.deleteUserAuth(param);
			}
		}
		return count;
	}
	
	@Override
	public int deleteUserAuth(HashMap<String, String> param) throws Exception {			
		return settingsDAO.deleteUserAuth(param);
	}

	@Override
	public List<Map<String, Object>> getWebConsoleLoglist() throws Exception {		
		List<Map<String, Object>> logFileList = new ArrayList<Map<String, Object>>();
		File[] logFiles = new File(Globals.HOME_DIR + File.separatorChar + "logs").listFiles();
		if(logFiles != null) {
			for (File file : logFiles) {
				if (file.getName().contains("webconsole.log") || file.getName().contains("access_log") || file.getName().contains("catalina.")) {
					Map<String, Object> logFile = new HashMap<String, Object>();
					logFile.put("fileName", file.getName());
					logFile.put("fileSize", file.length());
					logFile.put("fileLast", DateUtils.Date2Str(file.lastModified(), "yyyy-MM-dd HH:mm:ss"));
					logFileList.add(logFile);
				}
			}
		}

		Collections.sort(logFileList, new Comparator<Map<String, Object>>() {
			// 내림차순으로 정렬한다.
			public int compare(Map<String, Object> a, Map<String, Object> b) {
				int result = a.get("fileLast").toString().compareTo(b.get("fileLast").toString());
				return -result;
			}
		});

		return logFileList;
	}
	
	@Override
	public List<Map<String, Object>> getServerLoglist(String dirName) throws Exception {		
		List<Map<String, Object>> logFileList = new ArrayList<Map<String, Object>>();
		File[] logFiles = new File(Globals.HOME_DIR + File.separatorChar + "logs" + File.separator + dirName).listFiles();
		if(logFiles != null) {
			for (File file : logFiles) {
				Map<String, Object> logFile = new HashMap<String, Object>();
				logFile.put("fileName", file.getName());
				logFile.put("fileSize", file.length());
				logFile.put("fileLast", DateUtils.Date2Str(file.lastModified(), "yyyy-MM-dd HH:mm:ss"));
				logFileList.add(logFile);
			}
		}
		
		Collections.sort(logFileList, new Comparator<Map<String, Object>>() {
			// 내림차순으로 정렬한다.
			public int compare(Map<String, Object> a, Map<String, Object> b) {
				int result = 0;
				if (!"webconsole.log".equals(a.get("fileName").toString())) {
					result = a.get("fileLast").toString().compareTo(b.get("fileLast").toString());
				}
				return -result;
			}
		});
		
		return logFileList;
	}

	@Override
	public Map<String, Object> getLogContent() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getWebConsoleStatus() throws Exception {		
		Map<String, String> statusMap = (Map<String, String>) StatusManagerWrap.getInstance().getWebConsoleStatus();		
		return statusMap;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String,Object>> selectSERVER(Map<String, Object> param) {
		/*
		int pageSize = Integer.parseInt((String) param.get("PAGE_SIZE"));
		int currentPage = Integer.parseInt((String) param.get("CURRENT_PAGE"));
		
		int start = (currentPage * pageSize) - (pageSize - 1);
		int end = (currentPage * pageSize);
		
		searchParams.put("start", start);
		searchParams.put("end", end);
		*/
		return settingsDAO.selectSERVER(param);
	}

	@Override
	public int selectSERVERTotalCount(Map<String, Object> param) {
		return settingsDAO.selectSERVERTotalCount(param);
	}

	@Override
	public List<Map<String, Object>> selectReadonlyCode() {
		return settingsDAO.selectReadonlyCode();
	}
	
	@Override
	public int insertSERVER(HashMap<String, String> param) {
		return settingsDAO.insertSERVER(param);
	}

	@Override
	public int deleteSERVER(HashMap<String, String> param) {
		return settingsDAO.deleteSERVER(param);
	}

	@Override
	public List<Map<String, Object>> selectSERVERDetail(Map<String, Object> param) {
		return settingsDAO.selectSERVERDetail(param);
	}

	@Override
	public int updateSERVER(HashMap<String, String> param) {
		return settingsDAO.updateSERVER(param);
	}

	@Override
	public int selectSERVERDupCheck(HashMap<String, String> param) {
		return settingsDAO.selectSERVERDupCheck(param);
	}
	
	/**
	 * 목록 조회
	 * 
	 * @param reqBody
	 * @param resBody
	 * @throws Exception
	 */
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map<String, Object>> getWebConsoleThreadList() throws Exception {
		List<Map<String, Object>> threadList = new ArrayList<Map<String, Object>>();
		try {

			Thread[] threads = ThreadManager.getThreadList();
			Vector threadVector = new Vector();
			int threadListLength = threads.length;

			for (int i = 0; i < threadListLength; i++) {
				threadVector.add(threads[i]);
			}

			Collections.sort(threadVector, new Comparator() {
				public int compare(Object a, Object b) {
					return (((Thread) a).getName()).compareToIgnoreCase(((Thread) b).getName());
				}
			});

			for (int i = 0; i < threadListLength; i++) {
				Thread thread = (Thread) threadVector.get(i);
				Map<String, Object> threadItem = new HashMap<String, Object>();
				threadItem.put("threadName", thread.getName());//이름
				threadItem.put("priority", thread.getPriority() + "");//우선순위
				threadItem.put("activeStatus", thread.isAlive() + "");//활동상태
				threadItem.put("isDemon", thread.isDaemon() + "");//데몬
				threadItem.put("stopStatus", thread.isInterrupted() + "");//중단상태
				threadList.add(threadItem);
			}			
		} catch (Exception e) {
			throw new Exception("Thread 관리자에서 오류가 발생하였습니다.");
		}
		return threadList;
	}

	@Override
	public int getLoginHistoryTotalCount(Map<String, Object> param) {
		return settingsDAO.getLoginHistoryTotalCount(param);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getLoginHistoryList(Map<String, Object> param) {
		Map<String , Object> searchParams = (Map<String , Object>) param.get("SEARCH_PARAM");
		
		int pageSize = Integer.parseInt((String) param.get("PAGE_SIZE"));
		int currentPage = Integer.parseInt((String) param.get("CURRENT_PAGE"));
		
		int start = (currentPage * pageSize) - (pageSize - 1);
		int end = (currentPage * pageSize);
		
		searchParams.put("start", start);
		searchParams.put("end", end);
		
		return settingsDAO.getLoginHistoryList(searchParams);
	}

	@Override
	public int getSettingsHistoryTotalCount(Map<String, Object> param) {
		return settingsDAO.getSettingsHistoryTotalCount(param);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getSettingsHistoryList(Map<String, Object> param) {
		Map<String , Object> searchParams = (Map<String , Object>) param.get("SEARCH_PARAM");
		
		int pageSize = Integer.parseInt((String) param.get("PAGE_SIZE"));
		int currentPage = Integer.parseInt((String) param.get("CURRENT_PAGE"));
		
		int start = (currentPage * pageSize) - (pageSize - 1);
		int end = (currentPage * pageSize);
		
		searchParams.put("start", start);
		searchParams.put("end", end);
		
		return settingsDAO.getSettingsHistoryList(searchParams);
	}

	@Override
	public List<Map<String, Object>> getSettingsHistoryDetail(Map<String, Object> param) {
		return settingsDAO.getSettingsHistoryDetail(param);
	}	

	
}
