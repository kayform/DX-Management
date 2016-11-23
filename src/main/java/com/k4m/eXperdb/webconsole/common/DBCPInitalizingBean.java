package com.k4m.eXperdb.webconsole.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.k4m.eXperdb.webconsole.db.ConfigInfo;
import com.k4m.eXperdb.webconsole.db.DBCPPoolManager;
import com.k4m.eXperdb.webconsole.settings.SettingsService;
import com.k4m.eXperdb.webconsole.util.SecureManager;

public class DBCPInitalizingBean implements InitializingBean {
	@Autowired SettingsService settingsService;

	@Override
	public void afterPropertiesSet() throws Exception {
		try {			
			/*
			 * 서버정보의 PostgreSQL 정보를 읽어 Connection Pool 생성
			 */
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("sys_nm", "%");
			param.put("type", "POSTGRESQL");			
			param.put("ip", "%");
			
			/*
			int totalCount = settingsService.selectSERVERTotalCount(param); // 데이터 전체 건수 조회

			Map<String , Object> requestMap = new HashMap<String, Object>();
			requestMap.put("SEARCH_PARAM", param);
			requestMap.put("PAGE_SIZE", Integer.toString(1));
			requestMap.put("CURRENT_PAGE", Integer.toString(totalCount));
			*/
			
			List<Map<String, Object>> serverList = settingsService.selectSERVER(param); // 데이터 리스트 조회
			
    		Map<String, Object> tempMap  = new HashMap<String, Object>();
    		
    		for (Map<String, Object> map : serverList){
    			ConfigInfo configInfo = new ConfigInfo();
           		configInfo.SERVERIP = (String)map.get("ip");
                configInfo.USERID = (String)map.get("user_id");
                configInfo.DB_PW = (String)map.get("user_pw");;
                configInfo.DB_PW = SecureManager.decrypt(configInfo.DB_PW);
                configInfo.PORT = (String)map.get("port");;
                configInfo.DBNAME = (String)map.get("db_nm");;
                configInfo.SCHEMA_NAME = (String)map.get("user_id");;
                configInfo.DB_TYPE = "POG";	   
                
                String sys_nm = (String)map.get("sys_nm");
                
                try{
                	DBCPPoolManager.setupDriver(configInfo, sys_nm, 5);
                }catch (Exception e) {
                	DBCPPoolManager.shutdownDriver(sys_nm);
        			Globals.logger.error(e.getMessage(), e);
        		}                
    		}
		} catch (Exception e) {
			Globals.logger.error(e.getMessage(), e);
		}
	}
}
