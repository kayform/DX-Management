package com.k4m.eXperdb.webconsole.common;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataHistoryServiceImpl implements DataHistoryService {


	@Autowired 
	private DataHistoryDAO dataHistoryDAO;
	
	@Override
	public void add(String menu_id, String cud_mode, String user_id, String ip, String data_id, String file_name, byte[] data_contents) {
		
		try {
			
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("menu_id", menu_id);
			param.put("cud_mode", cud_mode);
			param.put("user_id", user_id);
			param.put("ip", ip);
			param.put("data_id", data_id);
			param.put("file_name", file_name);
			param.put("data_contents", data_contents);
			
			dataHistoryDAO.insertDataHistory(param);
			
		} catch(Exception e) {
			Logger.getRootLogger().warn("데이터 히스토리 정보 등록 오류", e);
		}
	}
	

}
