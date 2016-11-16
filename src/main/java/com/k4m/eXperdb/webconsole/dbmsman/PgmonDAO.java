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
	
	public int insertPgSvrInfo(HashMap<String, String> param) {
		int insertRow = 0;
		sqlSession.delete("user-mapper.deleteUserAuthAll", param);
		//사용자 등록시 무조건적으로 대쉬보드메뉴는 등록시킴
		param.put("menu_id", "DSB000");
		sqlSession.insert("user-mapper.insertUserAuth", param);
		insertRow = sqlSession.insert("user-mapper.insertUser", param);
		return insertRow;
	}
}
