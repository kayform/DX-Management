package com.k4m.eXperdb.webconsole.linkedengine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ClouderaDAO {
	
	/**
	 * Session 
	 */
	@Autowired 
	private SqlSession sqlSession;
	
	/**
	 * Cloudera 서버 조회
	 * 
	 * @param param
	 * @return
	 */
	public Map<String,Object> selectClouderaServer(HashMap<String, String> param) {
		Map<String,Object> map = sqlSession.selectOne("linkedengine-mapper.selectClouderaServer", param);
		return map;
	}

	/**
	 * Cloudera 서버 목록 조회
	 * 
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> selectClouderaServerList(HashMap<String, String> param) {
		List<Map<String,Object>> list = sqlSession.selectList("linkedengine-mapper.selectClouderaServerList", param);
		return list;
	}

}
