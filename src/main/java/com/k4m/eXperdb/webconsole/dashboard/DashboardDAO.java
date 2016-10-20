package com.k4m.eXperdb.webconsole.dashboard;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DashboardDAO {
	/**
	 * Session 
	 */
	@Autowired 
	private SqlSession sqlSession;

	public int dashboardTargetDBMS() {
		return sqlSession.selectOne("dashboard-mapper.selectTargetDBMS");
	}

	public int dashboardCheckReservation() {
		return sqlSession.selectOne("dashboard-mapper.selectCheckReservation");
	}

	public int dashboardRunning() {
		return sqlSession.selectOne("dashboard-mapper.selectRunning");
	}
	
	public List<Map<String, Object>> dashboardTargetList() {
		return sqlSession.selectList("dashboard-mapper.selectTargetList");
	}

	public List<Map<String, Object>> dashboardTargetListTwo() {
		return sqlSession.selectList("dashboard-mapper.selectTargetListTwo");
	}
}
