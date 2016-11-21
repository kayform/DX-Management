package com.k4m.eXperdb.webconsole.dbmsman;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PgmonDAO {
	/**
	 * Session 
	 */
	@Autowired 
	private SqlSession sqlSession;
	
	/*
	public int selectPgSvrList(HashMap<String, String> param) {
		int insertRow = 0;
		sqlSession.insert("pgmon-mapper.insertPgSvrInfo", param);
		insertRow = sqlSession.insert("user-mapper.insertUser", param);
		return insertRow;
	}
	*/
	
	public int insertPgmonTbHchkInfo(Map<String, Object> param) throws Exception{
		int resultcnt = 0;
		
		resultcnt = sqlSession.insert("pgmon-mapper.insertPgmonTbHchkInfo", param);
		return resultcnt;
	}
	
	public int deletePgmonTbHchkInfo(Map<String, Object> param) throws Exception{
		int resultcnt = 0;
		
		resultcnt = sqlSession.delete("pgmon-mapper.deletePgmonTbHchkInfo", param);
		return resultcnt;
	}
	
	public int insertPgSvrInfo(Map<String, Object> param) throws Exception{
		int resultcnt = 0;
		
		resultcnt = sqlSession.insert("pgmon-mapper.insertPgSvrInfo", param);
		return resultcnt;
	}
	
	public int updatePgSvrInfo(Map<String, Object> param) throws Exception{
		int resultcnt = 0;
		
		resultcnt = sqlSession.update("pgmon-mapper.updatePgSvrInfo", param);
		return resultcnt;
	}
	
	public int deletePgSvrInfo(Map<String, Object> param) throws Exception{
		int resultcnt = 0;
		
		resultcnt = sqlSession.delete("pgmon-mapper.deletePgSvrInfo", param);
		return resultcnt;
	}
	
	public int selectGenerateInstanceId(Map<String, Object> param)  throws Exception{
		int instance_id = 0;
		instance_id = sqlSession.selectOne("pgmon-mapper.selectGenerateInstanceId", param);
		return instance_id;
	}
}
