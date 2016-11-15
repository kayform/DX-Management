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
	
	public String getQuery(String queryId , Object sqlParam){
		/*
		BoundSql boundSql = sqlSession.getConfiguration().getMappedStatement(queryId).getSqlSource().getBoundSql(sqlParam);
		String query1 = boundSql.getSql();
		Object paramObj = boundSql.getParameterObject();
		
		if(paramObj != null){              // 파라미터가 아무것도 없을 경우
			List<ParameterMapping> paramMapping = boundSql.getParameterMappings();
			for(ParameterMapping mapping : paramMapping){
				String propValue = mapping.getProperty();       
				query1=query1.replaceFirst("\\?", "#{"+propValue+"}");
			}
		}
		return query1; */
		return sqlSession.getConfiguration().getMappedStatement(queryId).getSqlSource().getBoundSql(sqlParam).getSql();
	}
}
