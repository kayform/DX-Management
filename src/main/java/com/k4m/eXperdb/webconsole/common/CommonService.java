package com.k4m.eXperdb.webconsole.common;

import java.util.List;
import java.util.Map;

public interface CommonService {
	List<Map<String,Object>> selectSystemCode (String SystemGroupCode); // 시스템 구분 코드 조회
	List<Map<String,Object>> selectAllMenuList ();
	String getQuery(String queryId , Object sqlParam);
}
