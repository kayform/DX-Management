package com.k4m.eXperdb.webconsole.dbmsman;

import java.util.Map;

public interface PgmonService {
	public int insertPgSvrInfo(Map<String, Object> param) throws Exception;
	public int selectGenerateInstanceId(Map<String, Object> param) throws Exception;
	public int insertPgmonTbHchkInfo(Map<String, Object> param) throws Exception;
	public int deletePgmonTbHchkInfo(Map<String, Object> param) throws Exception;
	public int updatePgSvrInfo(Map<String, Object> param) throws Exception;
	public int deletePgSvrInfo(Map<String, Object> param) throws Exception;
}
