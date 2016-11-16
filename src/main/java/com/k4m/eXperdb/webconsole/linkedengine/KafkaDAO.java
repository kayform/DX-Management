package com.k4m.eXperdb.webconsole.linkedengine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class KafkaDAO {
	
	/**
	 * Session 
	 */
	@Autowired 
	private SqlSession sqlSession;
	
	/**
	 * Kafka 서버 목록 조회
	 * 
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> selectKafkaServerList(HashMap<String, String> param) {
		List<Map<String,Object>> list = sqlSession.selectList("linkedengine-mapper.selectKafkaServerList", param);
		return list;
	}

}
