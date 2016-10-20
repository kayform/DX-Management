package com.k4m.eXperdb.webconsole.common;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DataHistoryDAO {
	
	@Autowired 
	private SqlSession sqlSession;
	
	public int insertDataHistory(Map<String, Object> param) {
		return sqlSession.insert("datahistory-mapper.insertDataHistory", param); 
	}
}
