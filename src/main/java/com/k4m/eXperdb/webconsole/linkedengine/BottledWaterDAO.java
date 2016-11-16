package com.k4m.eXperdb.webconsole.linkedengine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BottledWaterDAO {
	
	/**
	 * Session 
	 */
	@Autowired 
	private SqlSession sqlSession;
	
	/**
	 * BottledWater 서버 목록 조회
	 * 
	 * 파라미터는 현재 사용하지 않으나 향후 요구사항 변경을 대비하여 존치시킴 remarked by manimany
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> selectServerList(HashMap<String, String> param) {
		List<Map<String,Object>> list = sqlSession.selectList("linkedengine-mapper.selectServerList", param);
		return list;
	}

}
