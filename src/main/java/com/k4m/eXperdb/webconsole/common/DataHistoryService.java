package com.k4m.eXperdb.webconsole.common;


public interface DataHistoryService {
	public void add(String menuId, String cudMode, String userId, String ip, String dataId, String fileName, byte[] dataContents);
}
