package com.k4m.eXperdb.webconsole.linkedengine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface KafkaConnectService {
	List<Map<String, Object>> selectKafkaConnectServerList(HashMap<String, String> param) throws Exception; // 서버 목록 조회	

	String createKafkaConnect(HashMap<String, String> param) throws Exception; // 카프카 커넥션 생성;

	String deleteKafkaConnect(HashMap<String, String> param) throws Exception; // 카프카 커넥션 삭제;
	
	String updateKafkaConnect(HashMap<String, String> param) throws Exception; // 카프카 커넥션 수정;


}
