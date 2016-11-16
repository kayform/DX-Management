package com.k4m.eXperdb.webconsole.linkedengine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class KafkaServiceImpl implements KafkaService {
	
	@Autowired
	private KafkaDAO kafkaDAO;
	
	

	@Override
	public List<Map<String, Object>> selectKafkaServerList(HashMap<String, String> param) throws Exception {		
		List<Map<String, Object>> serverList = kafkaDAO.selectKafkaServerList(param);		
		return serverList;
	}



}
