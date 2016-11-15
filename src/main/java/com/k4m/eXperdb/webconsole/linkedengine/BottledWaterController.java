package com.k4m.eXperdb.webconsole.linkedengine;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.dxmig.db.DBCPPoolManager;
import com.k4m.eXperdb.webconsole.common.Globals;


@Controller
public class BottledWaterController {

	@Autowired
	private BottledWaterService bottledWaterService;

	
	/**
	 * 
	 * 연계엔진관리 > BottledWater 서버 목록 조회
	 * 
	 * searchServerType 파라미터는 현재 사용하지 않으나 향후 요구사항 변경을 대비하여 존치시킴 remarked by manimany
	 * 
	 * @param model 
	 * @param session
	 * @param request
	 * @param searchServerType
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/bottledwater")   
	public ModelAndView server(Model model, HttpSession session, HttpServletRequest request, 
			@RequestParam(value = "searchServerType", defaultValue = "") String searchServerType) throws Exception {
		List<Map<String, Object>> serverList = null;		
		HashMap<String, String> param = new HashMap<String, String>();
	
		//param.put("type", (searchServerType == null || "".equals(searchServerType)) ? "%" : "%" + searchServerType + "%");
		
		try{
			serverList = bottledWaterService.selectServerList(param);		
		}catch (Exception e){
			Globals.logger.error(e.getMessage(), e);
		}
		ModelAndView mav = new ModelAndView();
		mav.addObject("serverList", serverList);
		//mav.addObject("searchServerType", searchServerType);

		mav.setViewName("bottledWater");
		
		return mav;
	}
	
	/**
	 * Database 목록 조회
	 * @param model
	 * @param session
	 * @param request
	 * @param searchSysNm
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/databaseList")   
	public ModelAndView databaseList(Model model, HttpSession session, HttpServletRequest request, 
			@RequestParam(value = "searchSysNm", defaultValue = "") String searchSysNm) throws Exception {
		
		List<Map<String, Object>> databaseList = null;		
		HashMap<String, String> param = new HashMap<String, String>();
	
		try{
			databaseList = selectDatabaseList(searchSysNm);
		}catch (Exception e){
			Globals.logger.error(e.getMessage(), e);
		}
		ModelAndView mav = new ModelAndView();
		mav.addObject("databaseList", databaseList);
		mav.addObject("searchSysNm", searchSysNm);
		mav.setViewName("databaseList");
		return mav;
	}
	
	
	/**
	 * 데이터베이스 연계 정보 조회
	 * @param param
	 * @return
	 * @throws Exception
	 */
	private List<Map<String, Object>> selectDatabaseList(String searchSysNm) throws Exception {
		
		List<Map<String, Object>> databaseList = new ArrayList<Map<String, Object>>();
		Map<String, Object> databaseInfo = null;

	    //전체 database 명 조회 쿼리
	    String databaseListQuery = "SELECT datname FROM pg_database where datistemplate not in ('t') order by datname";
	    
	    Connection conn = null;
		Statement listST = null;
		ResultSet listRS = null;

		Statement extensionCheckST = null;
		ResultSet extensionCheckRS = null;
		String extensionCheckQuery = null;

		Statement st = null;
		ResultSet rs = null;
		String databaseQuery = null;

		
	    try {
	    	conn = DBCPPoolManager.getConnection(searchSysNm);

	    	listST = conn.createStatement();
			listRS = listST.executeQuery(databaseListQuery);

			String dbName = null;
			
			//database 명 조회
			while (listRS.next()) {
				dbName = listRS.getString(1); 
				Globals.logger.debug(" database명  "+dbName+" 에 대한 연계정보 조회" );

				extensionCheckQuery = "select * from dblink('dbname="+ dbName +"', 'select count(*) from pg_extension where extname in (''bottledwater'', ''bwcontrol'')') as t1(count bigint)";
				extensionCheckST = conn.createStatement();
				extensionCheckRS = extensionCheckST.executeQuery(extensionCheckQuery);

				//익스텐션 확장자가 설치되어 있다면...
				if(extensionCheckRS.next() && extensionCheckRS.getInt(1) == 2) { 
					databaseQuery = "select database_name, pg_get_status_ingest, connect_name" 
									+" from dblink('dbname="+dbName+"'," 
									+" 'select t2.database_name, pg_get_status_ingest ,  t2.connect_name"
									+" from pg_get_status_ingest() pg_get_status_ingest left outer join"
									+" (select database_name, connect_name"
									+" from kafka_con_config) t2 on 1=1')"
									+" as t1(database_name varchar(100), pg_get_status_ingest varchar(100), connect_name  varchar(100))";
					
					//Globals.logger.debug("database 연계상태 조회 쿼리 = "+databaseQuery);

					st = conn.createStatement();
					rs =  st.executeQuery(databaseQuery);
					databaseInfo = new HashMap<String, Object>();
					//kafka_con_config 테이블에 데이터베이스명이 없는 경우가 있어 dbName 으로 데이터베이스명 셋팅
					while (rs.next()) {
						databaseInfo.put("database_name", dbName);
						databaseInfo.put("pg_get_status_ingest", rs.getString(2));
						databaseInfo.put("connect_name", rs.getString(3));
					}
					databaseList.add(databaseInfo);

				} else{
					Globals.logger.debug("database "+dbName+"에는 확장 extension이 설치되어 있지 않습니다.");
				}
			}
			
		} catch(SQLException e){
			Globals.logger.error("SQL 에러코드 ("+e.getSQLState()+") 에러가 발생했습니다.");
			Globals.logger.error(e.getMessage(), e);
	    } catch (Exception e) {
	    	Globals.logger.error(e.getMessage(), e);
	    } finally{
	    	if(extensionCheckRS !=  null) extensionCheckRS.close();
	    	if(extensionCheckST !=  null) extensionCheckST.close();
	    	if(rs !=  null) rs.close();
	    	if(st !=  null) st.close();
	    	if(listRS !=  null) listRS.close();
	    	if(listST !=  null) listST.close();
	    	if(conn !=  null) conn.close();
	    }
		Globals.logger.debug("database 연계정보 조회 갯수 = "+databaseList.size());
		return databaseList;
	}	
	
	
	

}
