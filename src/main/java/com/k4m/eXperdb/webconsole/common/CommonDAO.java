package com.k4m.eXperdb.webconsole.common;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CommonDAO {
	
	/**
	 * Session 
	 */
	@Autowired 
	private SqlSession sqlSession;
	
	public List<Map<String, Object>> selectSystemCode(Map<String, Object> param) {
		return sqlSession.selectList("common-mapper.selectSystemCode", param); 
	}
	
	public List<Map<String, Object>> selectAllMenuList(Map<String, Object> param) {
		return sqlSession.selectList("common-mapper.selectAllMenuList", param); 
	}
}
