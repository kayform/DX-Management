package com.k4m.eXperdb.webconsole.linkedengine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface KafkaService {
	List<Map<String, Object>> selectKafkaServerList(HashMap<String, String> param) throws Exception; // 서버 목록 조회	


}
