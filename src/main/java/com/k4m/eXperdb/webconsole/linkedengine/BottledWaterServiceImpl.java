package com.k4m.eXperdb.webconsole.linkedengine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class BottledWaterServiceImpl implements BottledWaterService {
	
	@Autowired
	private BottledWaterDAO bottledWaterDAO;
	
	

	@Override
	public List<Map<String, Object>> selectServerList(HashMap<String, String> param) throws Exception {		
		List<Map<String, Object>> serverList = bottledWaterDAO.selectServerList(param);		
		return serverList;
	}	

}
