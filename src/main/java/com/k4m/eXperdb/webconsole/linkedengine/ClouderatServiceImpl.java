package com.k4m.eXperdb.webconsole.linkedengine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ClouderatServiceImpl implements ClouderaService {
	
	@Autowired
	private ClouderaDAO clouderaDAO;
	
	

	@Override
	public List<Map<String, Object>> selectClouderaServerList(HashMap<String, String> param) throws Exception {		
		List<Map<String, Object>> serverList = clouderaDAO.selectClouderaServerList(param);		
		return serverList;
	}


	@Override
	public Map<String, Object> selectClouderaServer(HashMap<String, String> param) throws Exception {		
		Map<String, Object> server = clouderaDAO.selectClouderaServer(param);		
		return server;
	}



}
