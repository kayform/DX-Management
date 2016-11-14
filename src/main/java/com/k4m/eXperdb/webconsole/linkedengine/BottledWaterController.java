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
		
		Globals.logger.debug("============== in the databaseList function ");
		
		
		List<Map<String, Object>> databaseList = null;		
		HashMap<String, String> param = new HashMap<String, String>();
	
		param.put("searchSysNm", (searchSysNm == null || "".equals(searchSysNm)) ? "%" : "%" + searchSysNm + "%");
		
		try{
			
			//TODO 데이터소스풀이 생성되면 해당 데이터소스 및 서비스 풀 사용하게 구현 변경해야함. 현재는 임시 메소드 에서 반환하는 값 사용 remarked by manimany
//			databaseList = bottledWaterService.selectServerList(param);		
			databaseList = selectDatabaseList(searchSysNm);
		}catch (Exception e){
			Globals.logger.error(e.getMessage(), e);
		}
		ModelAndView mav = new ModelAndView();
		mav.addObject("databaseList", databaseList);
		//mav.addObject("searchServerType", searchServerType);
		
		mav.setViewName("databaseList");
		return mav;
	}
	
	/**
	 * 아이피/아이디/패스워드를 이용한 다이나믹 데이터 소스 구현 이전까지 임시 사용
	 * @param param
	 * @return
	 * @throws Exception
	 */
	private List<Map<String, Object>> selectDatabaseList(String searchSysNm) throws Exception {
		
		Globals.logger.debug("============== in the databaseList function searchSysNm==="+searchSysNm);

		
		List<Map<String, Object>> databaseList = new ArrayList<Map<String, Object>>();
		Map<String, Object> databaseInfo = null;

//		databaseList.clear();
		
		String url = "jdbc:postgresql://58.229.240.89:5432/mobiusdb";
	    String usr = "mobius";  
	    String pwd = "mobius";
	    
	    //전체 database 조회 쿼리
	    String databaseListQuery = "SELECT datname FROM pg_database where datistemplate not in ('t') order by datname";
	    
	    Connection conn = null;
	    try {
//	    	conn = DriverManager.getConnection(url, usr, pwd);
	    	conn = DBCPPoolManager.getConnection(searchSysNm);
	    } catch(Exception e) {
	    	Globals.logger.error(e.getMessage(), e);
	    }
		
	    try {
			Statement listST = conn.createStatement();
			ResultSet listRS = listST.executeQuery(databaseListQuery);

			Statement st = conn.createStatement();
			ResultSet rs = null;
			String databaseQuery = null;
			String dbName = null;

			while (listRS.next()) {
				Globals.logger.debug("============ listRS.getString(1) 데이터베이스명 ======="+listRS.getString(1));
				dbName = listRS.getString(1);

				databaseQuery = "select database_name, pg_get_status_ingest, connect_name , count "
								+" from dblink('dbname="+listRS.getString(1) +"'," 
								+" 'select a.database_name, b.pg_get_status_ingest, a.connect_name , ex.count"
								+" from kafka_con_config  as a," 
								+" (select pg_get_status_ingest()) as b,"
								+" (select count(*) from pg_extension where extname in (''bottledwater'', ''bwcontrol'')) as ex')" 
								+" as t1(database_name varchar(100), pg_get_status_ingest varchar(100), connect_name  varchar(100), count bigint)";
				try{
					rs =  st.executeQuery(databaseQuery);
					databaseInfo = new HashMap<String, Object>();
					while (rs.next()) {
						Globals.logger.debug("============ rs.getString(1) 데이터베이스명 ======="+rs.getString(1));
						Globals.logger.debug("============ rs.getString(2) 연계상태 ======="+rs.getString(2));
						Globals.logger.debug("============ rs.getString(3) 커넥션 이름 ======="+rs.getString(3));
						Globals.logger.debug("============ rs.getString(4) 익스텐션 숫자 ======="+rs.getString(4));
						databaseInfo.put("database_name", rs.getString(1));
						databaseInfo.put("pg_get_status_ingest", rs.getString(2));
						databaseInfo.put("connect_name", rs.getString(3));
						databaseInfo.put("count", rs.getString(4));
					}
					databaseList.add(databaseInfo);
					
				} catch(SQLException e){
					Globals.logger.debug("============ e.getSQLState() ======="+e.getSQLState()+"==========");
					
					//테이블이 없을 경우 42P01 반환, function이 없을 경우 42883 반환
					if(e.getSQLState().equals("42P01") || e.getSQLState().equals("42883")) {
						Globals.logger.info(dbName+"은 extension이 설치되지 않았습니다.");
						rs.close();
						st.close();
					}
					else {
						rs.close();
						st.close();
						throw e;
					}
				}
			}
			
			listRS.close();
			listST.close();			
	    } catch (Exception e) {
	    	Globals.logger.error(e.getMessage(), e);
	    }
	    
		Globals.logger.debug("============ databaseList.size() ======="+databaseList.size()+"==========");
		
		
		return databaseList;
	}	
	
	
	

}
