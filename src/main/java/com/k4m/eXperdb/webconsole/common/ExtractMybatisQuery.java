package com.k4m.eXperdb.webconsole.common;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ExtractMybatisQuery {
	@Autowired 
	private static SqlSession sqlSession;
	

}
