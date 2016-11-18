package com.k4m.eXperdb.webconsole.dbmsman;

import java.util.HashMap;

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
	
	public int selectPgSvrList(HashMap<String, String> param) {
		int insertRow = 0;
		sqlSession.insert("pgmon-mapper.insertPgSvrInfo", param);
		insertRow = sqlSession.insert("user-mapper.insertUser", param);
		return insertRow;
	}
	
	public int insertPgSvrInfo(HashMap<String, String> param) {
		int insertRow = 0;
		sqlSession.insert("pgmon-mapper.insertPgSvrInfo", param);
		insertRow = sqlSession.insert("user-mapper.insertUser", param);
		return insertRow;
	}
}
