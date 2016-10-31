package com.k4m.eXperdb.webconsole.dbmsman;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PgmanDAO {

	/**
	 * Session 
	 */
	@Autowired 
	private SqlSession sqlSession;
	
	public List<Map<String,Object>> selectPrsnCdList(Map<String,Object> parameter) throws Exception {
		return sqlSession.selectList("stat-mapper.selectPrsnCdList", parameter);
	}
	
	public List<Map<String,Object>> selectScheduleList(Map<String,Object> parameter) throws Exception {
		return sqlSession.selectList("stat-mapper.selectScheduleList", parameter);
	}
	
	public List<Map<String,Object>> selectDBMSList(Map<String,Object> parameter) throws Exception {
		return sqlSession.selectList("stat-mapper.selectDBMSList", parameter);
	}
	
	public List<Map<String,Object>> selectMonthlyStatistics(Map<String,Object> parameter) throws Exception {
		return sqlSession.selectList("stat-mapper.selectMonthlyStatistics", parameter);
	}

	public List<Map<String, Object>> selectYearlyStatistics(Map<String, Object> parameter) throws Exception {
		return sqlSession.selectList("stat-mapper.selectYearlyStatistics", parameter);
	}

	public int statisticsDelete(Map<String,Object> parameter) throws Exception {
		return sqlSession.update("stat-mapper.statisticsDelete", parameter);
	}
	public int statisticsRegist(Map<String,Object> parameter) throws Exception {
		return sqlSession.update("stat-mapper.statisticsRegist", parameter);
	}
	
	public List<Map<String, Object>> selectServer(HashMap<String, String> param) {
		String mode = (String) param.get("mode");
		List<Map<String,Object>> list = null;
		if (mode.equals("V")) {
			list = sqlSession.selectList("pgman-mapper.selectServerView", param);
		} else {
			list = sqlSession.selectList("pgman-mapper.selectServer", param);
		}
		
		return list;
	}
	
	public List<Map<String,Object>> selectServerList(HashMap<String, String> param) {
		List<Map<String,Object>> list = sqlSession.selectList("pgman-mapper.selectServerList", param);
		return list;
	}
}
