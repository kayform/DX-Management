package com.k4m.eXperdb.webconsole.linkedengine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface KafkaConnectService {
	List<Map<String, Object>> selectKafkaConnectServerList(HashMap<String, String> param) throws Exception; // 서버 목록 조회	


}
