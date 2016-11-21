package com.k4m.eXperdb.webconsole.linkedengine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ClouderaService {
	List<Map<String, Object>> selectClouderaServerList(HashMap<String, String> param) throws Exception; // 서버 목록 조회	

	Map<String, Object> selectClouderaServer(HashMap<String, String> param) throws Exception; // 서버 조회	

}
