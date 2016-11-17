package com.k4m.eXperdb.webconsole.linkedengine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class KafkaConnectServiceImpl implements KafkaConnectService {
	
	@Autowired
	private KafkaConnectDAO kafkaConnectDAO;
	
	

	@Override
	public List<Map<String, Object>> selectKafkaConnectServerList(HashMap<String, String> param) throws Exception {		
		List<Map<String, Object>> serverList = kafkaConnectDAO.selectKafkaConnectServerList(param);		
		return serverList;
	}



}
