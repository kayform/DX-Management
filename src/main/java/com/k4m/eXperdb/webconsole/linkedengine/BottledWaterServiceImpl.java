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
		return bottledWaterDAO.selectServerList(param);
	}


	@Override
	public List<Map<String, Object>> selectDatabaseList(String searchSysNm) throws Exception {
		return bottledWaterDAO.selectDatabaseList(searchSysNm);		
	}


	@Override
	public Map<String, Object> runProcess(Map<String, String> param)  throws Exception{
		return bottledWaterDAO.runProcess(param);
	}


	@Override
	public Map<String, Object> selectTableList(HashMap<String, Object> param) throws Exception {
		return bottledWaterDAO.selectTableList(param);		
	}	

}
