package com.k4m.eXperdb.webconsole.dbmsman;

import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.k4m.eXperdb.webconsole.common.Globals;
import com.k4m.eXperdb.webconsole.common.SHA256;
//import com.k4m.eXperdb.webconsole.common.StrUtil;
import com.k4m.eXperdb.webconsole.common.pgHbaConfigLine;
import com.k4m.eXperdb.webconsole.util.DateUtils;




















import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;


@Controller
public class PgmanController {

	@Autowired
	private PgmanService pgmanService;
	
	
    @RequestMapping(value="/header.html")
    public ModelAndView getHeader(HttpServletResponse response) throws IOException{
        return new ModelAndView("header");
    }

    @RequestMapping(value="/footer.html")
    public ModelAndView getFooter(HttpServletResponse response) throws IOException{
        return new ModelAndView("footer");
    }
	
//	HashMap<Integer, Object> hba = new HashMap<Integer, Object>();
	HashMap<Integer, pgHbaConfigLine> hba = new LinkedHashMap<Integer, pgHbaConfigLine>();
	  /**
		 * DB에 저장되어 있는 사용자 리스트 조회
		 * @param model
		 * @param session
		 * @param request
		 * @param auth_dv
		 * @param use_yn
		 * @param user_nm
		 * @return
		 * @throws Exception
		 */
		@RequestMapping(value = "/acl")
		public ModelAndView acl(Model model, HttpSession session, HttpServletRequest request, 
				@RequestParam(value = "searchAuthDivision", defaultValue = "") String searchAuthDivision, 
				@RequestParam(value = "searchUseYn", defaultValue = "") String searchUseYn,
				@RequestParam(value = "searchUserName", defaultValue = "") String searchUserName) throws Exception {
			List<Map<String, Object>> serverList = null;		
			HashMap<String, String> param = new HashMap<String, String>();
			
			param.put("auth_dv", (searchAuthDivision == null || "".equals(searchAuthDivision)) ? "%" : "%" + searchAuthDivision + "%");
			param.put("use_yn", (searchUseYn == null || "".equals(searchUseYn)) ? "%" : "%" + searchUseYn + "%");
			param.put("user_nm", (searchUserName == null || "".equals(searchUserName)) ? "%" : "%" + searchUserName + "%");
			
			try{
				serverList = pgmanService.selectServerList(param);		
			}catch (Exception e){
				Globals.logger.error(e.getMessage(), e);
			}
			ModelAndView mav = new ModelAndView();
			mav.addObject("serverList", serverList);
			mav.addObject("searchAuthDivision", searchAuthDivision);
			mav.addObject("searchUseYn", searchUseYn);
			mav.addObject("searchUserName", searchUserName);
			
			mav.addObject("serverId", session.getAttribute("serverId"));
			mav.addObject("userAuth", session.getAttribute("userAuth"));

			mav.setViewName("acl");
			return mav;
		}		

	/**
	 * 사용자 ID와 Mode(CRU)를 입력받아 입력받은 사용자에 대한 정보를 리턴
	 * 페이지에서 해당 Mode에 따라 각각에 맞는 화면을 출력
	 * @param model
	 * @param session
	 * @param request
	 * @param user_id
	 * @param mode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/aclForm")
	public ModelAndView aclForm(Model model, HttpSession session, HttpServletRequest request, 
			@RequestParam(value = "mode", defaultValue = "") String mode,		
			@RequestParam(value = "seq", defaultValue = "") String seq) throws Exception {		
		ModelAndView mav = new ModelAndView();
		pgHbaConfigLine config = null;
		Map<String, Object> aclInfo = new HashMap<String, Object>();
		
		if (mode.equals(Globals.MODE_DATA_UPDATE)) {
			for(int j = 0; j < hba.size(); j++){
				config = hba.get(j);
				if(config.getItemNumber() == Integer.parseInt(seq)){
					break;
				}
			}
			aclInfo.put("Seq", seq);
			aclInfo.put("Set", config.isComment() ? 0 : 1);
			aclInfo.put("Type", config.getConnectType());
			aclInfo.put("Database", config.getDatabase());
			aclInfo.put("User", config.getUser());
			aclInfo.put("Ip", config.getIpaddress());
			aclInfo.put("Method", config.getMethod());
			aclInfo.put("Option", config.getOption());
			aclInfo.put("Changed", "");
			mav.addObject("aclInfo", aclInfo);
		}
		mav.addObject("mode", mode);
		mav.setViewName("aclForm");
		return mav;
		
/*		
		ModelAndView mav = new ModelAndView();
		Map<String, Object> userInfo = new HashMap<String, Object>();
		if (!(userId == null || userId.equals(""))) {			
			HashMap<String, String> param = new HashMap<String, String>();
			param.put("user_id", userId);
			param.put("mode", mode);
			userInfo = settingsService.selectUser(param);
			mav.addObject("userInfo", userInfo);
		} 
		mav.addObject("mode", mode);
		mav.setViewName("userForm");
		return mav;*/
	}
		
	/**
	 * 사용자정보와 Mode(CUD)를 입력받아 사용자 정보를 DB에 저장, 수정, 삭제
	 * 
	 * @param model
	 * @param session
	 * @param request
	 * @param mode
	 * @param user_id
	 * @param user_nm
	 * @param user_pw1
	 * @param user_pw2
	 * @param jgd
	 * @param auth_dv
	 * @param blg
	 * @param dept
	 * @param hpnm_no
	 * @param cg_biz_def
	 * @param user_expd
	 * @param pwd_use_term
	 * @param use_yn
	 * @param sngl_athr_yn
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("null")
	@RequestMapping(value = "/aclSearch")
	@ResponseBody
	public  List<Map> aclSearch(Model model, HttpSession session, HttpServletRequest request, 
			@RequestParam(value = "serverId", defaultValue = "") String serverId ) throws Exception {		
		ModelAndView mav = new ModelAndView();
		
		HashMap<String, String> param = new HashMap<String, String>();
		
		int rowCount = 0;

		//		param.put("user_id", serverId);
				
		String url = "jdbc:postgresql://192.168.10.70:5432/postgres";
	    String usr = "postgres";  
	    String pwd = "robin";
	    String [] testArray = null ;
	    
	    List<Map> array = new ArrayList<Map>();
	    
	    Class.forName("org.postgresql.Driver");
	    
	    // -- 1
	    Connection conn = null;
	    try {
	    	conn = DriverManager.getConnection(url, usr, pwd);
		    System.out.println(conn);
	    } catch(Exception e) {
		    System.out.println(conn);
	    }
		
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("SELECT * from pg_catalog.pg_read_file('pg_hba.conf')");
		
		while (rs.next()) {
		    testArray = rs.getString(1).split("\n");
		}
		
	    hba.clear();
		for(int i = 0; i < testArray.length ; i++){
		    pgHbaConfigLine config = new pgHbaConfigLine(testArray[i]);
			Map<String, String> aclLine = new LinkedHashMap<String, String>();
			if(config.isValid() || (!config.isValid() && !config.isComment()) && !(config.getText()).isEmpty()){
				config.setItemNumber(rowCount);
				aclLine.put("Seq", String.valueOf(rowCount++));
				aclLine.put("Set", config.isComment() ? "" : "1");
				aclLine.put("Type", config.getConnectType());
				aclLine.put("Database", config.getDatabase());
				aclLine.put("User", config.getUser());
				aclLine.put("Ip", config.getIpaddress());
				aclLine.put("Method", config.getMethod());
				aclLine.put("Option", config.getOption());
				aclLine.put("Changed", "");
				array.add(aclLine);
			}
			hba.put(i, config);
		}
		
		rs.close();
		st.close();
	    conn.close();
//		mav.addObject("aclLine", aclLine);
//		mav.setViewName("acl");

		return array;
	}
	
	@RequestMapping(value = "/aclProcess")	
	@ResponseBody
	public ModelAndView aclProcess(Model model, HttpSession session, HttpServletRequest request, 
			@RequestParam(value = "serverId", defaultValue = "") String serverId,
			@RequestParam(value = "aclArray", defaultValue = "") String aclArray ) throws Exception {
//			@RequestBody List<Map<String, Object>> list) throws Exception {
		ModelAndView mav = new ModelAndView();
		int rowCount = 0;
		
        try {        	 
            JSONParser jsonParser = new JSONParser();
             
            JSONObject jsonObject = (JSONObject) jsonParser.parse(aclArray);
            JSONArray aclInfoArray = (JSONArray) jsonObject.get("myrows");
 
            for(int i=0; i<aclInfoArray.size(); i++){
                JSONObject aclObject = (JSONObject) aclInfoArray.get(i);
                boolean found = false;
				if((aclObject.get("Changed")).equals("1")){
					int seqNumber = Integer.parseInt((String)aclObject.get("Seq"));
					pgHbaConfigLine config = null;
					for(int j = 0; j < hba.size(); j++){
						config = hba.get(j);
						if(config.getItemNumber() == seqNumber){
							found = true;
							break;
						}
					}

					if(!found){
					    config = new pgHbaConfigLine(null);					    
					}
					
					config.setChanged(true);
					if((aclObject.get("Set")).equals("1"))
						config.setComment(false);
					else
						config.setComment(true);
					config.setConnectType((String)aclObject.get("Type"));
					config.setDatabase((String)aclObject.get("Database"));
					config.setUser((String)aclObject.get("User"));
					config.setIpaddress((String)aclObject.get("Ip"));
					config.setMethod((String)aclObject.get("Method"));
					config.setOption((String)aclObject.get("Option"));
					
					if(!found){
						hba.put(hba.size(), config);
					}
				}
            }
        }catch (ParseException e) {            
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
            
		String buffer = "";
		for(int i =0; i < hba.size(); i++){
			pgHbaConfigLine config = (pgHbaConfigLine) hba.get(i);
			buffer += config.getText() + "\n";
		}
		
		String url = "jdbc:postgresql://192.168.10.70:5432/postgres";
	    String usr = "postgres";  
	    String pwd = "robin";
	    String query1 = "SELECT pg_file_unlink('pg_hba.conf.bak');";
	    String query2 = "SELECT pg_file_write('pg_hba.conf.tmp', '" + buffer + "', false);";
	    String query3 = "SELECT pg_file_rename('pg_hba.conf.tmp', 'pg_hba.conf', 'pg_hba.conf.bak');";
	    
	    Connection conn = null;
	    try {
	    	conn = DriverManager.getConnection(url, usr, pwd);
		    System.out.println(conn);
	    } catch(Exception e) {
		    System.out.println(conn);
	    }
		
	    try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query1);
			conn.setAutoCommit(false); 
			rs = st.executeQuery(query2);
			rs = st.executeQuery(query3);
			conn.commit();       
			rs.close();
			st.close();			
	    } catch (Exception e) {
		    System.out.println("error");	    
	    }
			
//		while (rs.next()) {
//		    testArray = rs.getString(1).split("\n");
//		}
	    
	    conn.close();
		mav.setViewName("acl");
		return mav;
	}
	
///*	@RequestMapping(value = "/monthlystatistic")
//	public ModelAndView monthlystatistic(Model model, HttpSession session, HttpServletRequest request) throws Exception {
//		ModelAndView mav = new ModelAndView();
//		mav.setViewName("monthlystatistic");
//		
//		String year = request.getParameter("searchYear");
//		String sysNm = request.getParameter("searchSysNm");
//		
//		List<Map<String,Object>> prsnCdList = pgmanService.selectPrsnCdList();
//		
//		List<Map<String,Object>> dbmsList = pgmanService.selectDBMSList();
//
//		if(year == null || "".equals(year)) {
//			year = new SimpleDateFormat("yyyy").format(new Date());
//		}
//		
//		Calendar cal = Calendar.getInstance();
//		cal.add(Calendar.YEAR, -10);
//		int firstYear = cal.get(Calendar.YEAR);
//		List<Integer> yearList = new ArrayList<Integer>();
//		for(int y=firstYear;y<=firstYear+10;y++) {
//			yearList.add(y);
//		}
//		
//		if((sysNm == null || "".equals(sysNm)) && dbmsList.size() > 0) {
//			sysNm = dbmsList.get(0).get("sys_nm").toString();
//		}
//		
//		List<Map<String,Object>> statisticsList = pgmanService.selectMonthlyStatistics(year, sysNm);
//		
//		List<Map<String,Object>> statisticsTableList = pgmanService.makeMonthlyStatisticsList(prsnCdList, statisticsList);
//		
//		mav.addObject("searchYear", year);
//		mav.addObject("searchSysNm", sysNm);
//		mav.addObject("prsnCdList", prsnCdList);
//		mav.addObject("dbmsList", dbmsList);
//		mav.addObject("statisticsList", statisticsList);
//		mav.addObject("statisticsTableList", statisticsTableList);
//		mav.addObject("yearList", yearList);
//		
//		return mav;
//	}
//
//	@RequestMapping(value = "/yearlystatistic")
//	public ModelAndView yearlystatistic(Model model, HttpSession session, HttpServletRequest request) throws Exception {
//		ModelAndView mav = new ModelAndView();
//		mav.setViewName("yearlystatistic");
//
//		String sysNm = request.getParameter("searchSysNm");
//		
//		List<Map<String,Object>> prsnCdList = pgmanService.selectPrsnCdList();
//		
//		List<Map<String,Object>> dbmsList = pgmanService.selectDBMSList();
//
//		if((sysNm == null || "".equals(sysNm)) && dbmsList.size() > 0) {
//			sysNm = dbmsList.get(0).get("sys_nm").toString(); 
//		}
//		
//		List<Map<String,Object>> statisticsList = pgmanService.selectYearlyStatistics(sysNm);
//		
//		List<Map<String,Object>> statisticsTableList = pgmanService.makeYearlyStatisticsList(prsnCdList, statisticsList);
//
//		
//		Calendar cal = Calendar.getInstance();
//		cal.add(Calendar.YEAR, -10);
//		int firstYear = cal.get(Calendar.YEAR);
//		List<Map<String,Object>> yearList = new ArrayList<Map<String,Object>>();
//		for(int y=firstYear;y<=firstYear+12;y++) {
//			Map<String,Object> row = new HashMap<String,Object>();
//			int cnt = 0;
//			row.put("yyyy", y);
//			for(Map<String,Object> sel : statisticsTableList) {
//				//yearList.add(y);
//				if(((java.math.BigDecimal)sel.get("number")).intValue() == y) {
//					row.put("col_tot_rslt_cnt", sel.get("col_tot_rslt_cnt"));
//					cnt++;
//				}
//			}
//			if(cnt == 0) {
//				row.put("col_tot_rslt_cnt", 0);
//			}
//			yearList.add(row);
//		}
//		
//		mav.addObject("searchSysNm", sysNm);
//		mav.addObject("prsnCdList", prsnCdList);
//		mav.addObject("dbmsList", dbmsList);
//		mav.addObject("statisticsList", statisticsList);
//		mav.addObject("statisticsTableList", statisticsTableList);
//		mav.addObject("yearList", yearList);
//		
//		return mav;
//	}
//	
//	public void statisticsRegist() throws Exception {
//		pgmanService.statisticsRegist();
//	}
//	
//	
//	@RequestMapping(value = "/monthlystatistic/download")
//	public ModelAndView downloadMonthlystatistic(Model model, HttpSession session, HttpServletRequest request, 
//			@RequestParam(value = "searchYear", defaultValue = "") String year,
//			@RequestParam(value = "searchSysNm", defaultValue = "") String sysNm,
//			@RequestParam(value = "password", defaultValue = "") String password
//		) throws Exception {
//		ModelAndView mav = new ModelAndView();
//		mav.setViewName("monthlystatistic");
//		
//		List<Map<String,Object>> prsnCdList = pgmanService.selectPrsnCdList();
//		
//		List<Map<String,Object>> dbmsList = pgmanService.selectDBMSList();
//
//		if(year == null || "".equals(year)) {
//			year = new SimpleDateFormat("yyyy").format(new Date());
//		}
//		
//		Calendar cal = Calendar.getInstance();
//		cal.add(Calendar.YEAR, -10);
//		int firstYear = cal.get(Calendar.YEAR);
//		List<Integer> yearList = new ArrayList<Integer>();
//		for(int y=firstYear;y<=firstYear+10;y++) {
//			yearList.add(y);
//		}
//		
//		if((sysNm == null || "".equals(sysNm)) && dbmsList.size() > 0) {
//			sysNm = dbmsList.get(0).get("sys_nm").toString();
//		}
//		
//		List<Map<String,Object>> statisticsList = pgmanService.selectMonthlyStatistics(year, sysNm);
//		
//		List<Map<String,Object>> statisticsTableList = pgmanService.makeMonthlyStatisticsList(prsnCdList, statisticsList);
//		
//		Map<String, Object> resultMap = new HashMap<String, Object>();
//		resultMap.put("systemName", sysNm);
//		resultMap.put("list", statisticsTableList);
//		
//		String fileName = "개인정보점검결과 월별통계내역(" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + ")";
//		
//		Map<String, Object> item = new HashMap<String, Object>();
//		item.put("file", "monthlystatistic");
//		item.put("fileName", fileName);
//		item.put("content", resultMap);
//		item.put("password", password);	
//		
//			
//		return new ModelAndView("excelDownload", "item", item);
//	}
//	
//	@RequestMapping(value = "/yearlystatistic/download")
//	public ModelAndView downloadYearlystatistic(Model model, HttpSession session, HttpServletRequest request,
//			@RequestParam(value = "searchSysNm", defaultValue = "") String sysNm,
//			@RequestParam(value = "password", defaultValue = "") String password
//		) throws Exception {
//		ModelAndView mav = new ModelAndView();
//		mav.setViewName("yearlystatistic");
//
//		List<Map<String,Object>> prsnCdList = pgmanService.selectPrsnCdList();
//		
//		List<Map<String,Object>> dbmsList = pgmanService.selectDBMSList();
//
//		if((sysNm == null || "".equals(sysNm)) && dbmsList.size() > 0) {
//			sysNm = dbmsList.get(0).get("sys_nm").toString(); 
//		}
//		
//		List<Map<String,Object>> statisticsList = pgmanService.selectYearlyStatistics(sysNm);
//		
//		List<Map<String,Object>> statisticsTableList = pgmanService.makeYearlyStatisticsList(prsnCdList, statisticsList);
//
//		Map<String, Object> resultMap = new HashMap<String, Object>();
//		resultMap.put("systemName", sysNm);
//		resultMap.put("list", statisticsTableList);
//		
//		String fileName = "개인정보점검결과 연별통계내역(" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + ")";
//		
//		Map<String, Object> item = new HashMap<String, Object>();
//		item.put("file", "yearlystatistic");
//		item.put("fileName", fileName);
//		item.put("content", resultMap);
//		item.put("password", password);	
//		
//		return new ModelAndView("excelDownload", "item", item);
//	}*/


}
