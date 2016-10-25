package com.k4m.eXperdb.webconsole.dbmsman;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface PgmanService {

	void statisticsRegist() throws Exception;

	List<Map<String, Object>> selectScheduleList() throws Exception;

	List<Map<String, Object>> selectDBMSList() throws Exception;

	List<Map<String, Object>> selectMonthlyStatistics(String year, String sysNm)
			throws Exception;

	List<Map<String, Object>> selectPrsnCdList() throws Exception;

	List<Map<String, Object>> makeMonthlyStatisticsList(
			List<Map<String, Object>> prsnCdList,
			List<Map<String, Object>> statisticsList) throws Exception;

	List<Map<String, Object>> selectYearlyStatistics(String sysNm) throws Exception;

	List<Map<String, Object>> makeYearlyStatisticsList(
			List<Map<String, Object>> prsnCdList,
			List<Map<String, Object>> statisticsList) throws Exception;


	Map<String, Object> selectServer(HashMap<String, String> param) throws Exception; //서버 정보 조회
	
	List<Map<String, Object>> selectServerList(HashMap<String, String> param) throws Exception; // 사용자 목록 조회
}
