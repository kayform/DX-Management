package com.k4m.eXperdb.webconsole.dbmsman;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PgmonServiceImpl implements PgmonService{
	@Autowired 
	private PgmonDAO pgmonDAO;
	
	public int insertPgSvrInfo(Map<String, Object> param) throws Exception{
		return pgmonDAO.insertPgSvrInfo(param);
	}
	
	public int selectGenerateInstanceId(Map<String, Object> param) throws Exception{
		return pgmonDAO.selectGenerateInstanceId(param);
	}
	
	public int insertPgmonTbHchkInfo(Map<String, Object> param) throws Exception{
		return pgmonDAO.insertPgmonTbHchkInfo(param);
	}
	
	public int deletePgmonTbHchkInfo(Map<String, Object> param) throws Exception{
		return pgmonDAO.deletePgmonTbHchkInfo(param);
	}
	
	public int updatePgSvrInfo(Map<String, Object> param) throws Exception{
		return pgmonDAO.updatePgSvrInfo(param);
	}
	
	public int deletePgSvrInfo(Map<String, Object> param) throws Exception{
		return pgmonDAO.deletePgSvrInfo(param);
	}
}
