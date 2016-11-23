package com.k4m.eXperdb.webconsole.linkedengine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface BottledWaterService {
	List<Map<String, Object>> selectServerList(HashMap<String, String> param) throws Exception; // 서버 목록 조회	

	List<Map<String, Object>> selectDatabaseList(String searchSysNm) throws Exception;

	Map<String, Object> runProcess(Map<String, String> param) throws Exception;;

	int selectTableListTotalCount(HashMap<String, String> param) throws Exception;;

	List<Map<String, Object>> selectTableList(HashMap<String, String> param) throws Exception;;


}
