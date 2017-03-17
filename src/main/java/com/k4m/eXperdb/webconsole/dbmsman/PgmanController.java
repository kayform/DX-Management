package com.k4m.eXperdb.webconsole.dbmsman;

import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import com.k4m.eXperdb.webconsole.db.DBCPPoolManager;
import com.k4m.eXperdb.webconsole.util.DateUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
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
	 * DB에 저장되어 있는 Postgres server 리스트 조회
	 * @param model
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/acl")
	public ModelAndView acl(Model model, HttpSession session, HttpServletRequest request) throws Exception {
		List<Map<String, Object>> serverList = null;		
		HashMap<String, String> param = new HashMap<String, String>();
					
		try{
			serverList = pgmanService.selectServerList(param);		
		}catch (Exception e){
			Globals.logger.error(e.getMessage(), e);
		}
		ModelAndView mav = new ModelAndView();
		mav.addObject("serverList", serverList);

		mav.setViewName("acl");
		return mav;
	}	
		
	/**
	 * ACL seq Mode(CRU)를 입력받아 입력받은 ACL 정보를 리턴
	 * 페이지에서 해당 Mode에 따라 각각에 맞는 화면을 출력
	 * @param model
	 * @param session
	 * @param request
	 * @param mode
	 * @param seq
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/aclForm")
	public ModelAndView aclForm(Model model, HttpSession session, HttpServletRequest request, 
			@RequestParam(value = "mode", defaultValue = "") String mode,		
			@RequestParam(value = "seq", defaultValue = "") String seq,
			@RequestParam(value = "set", defaultValue = "") String set,
			@RequestParam(value = "type", defaultValue = "") String type,
			@RequestParam(value = "database", defaultValue = "") String database,
			@RequestParam(value = "user", defaultValue = "") String user,
			@RequestParam(value = "ip", defaultValue = "") String ip,
			@RequestParam(value = "method", defaultValue = "") String method,
			@RequestParam(value = "option", defaultValue = "") String option) throws Exception {		
		ModelAndView mav = new ModelAndView();
		Map<String, Object> aclInfo = new HashMap<String, Object>();
		
		if (mode.equals(Globals.MODE_DATA_UPDATE)) {
			aclInfo.put("Seq", seq);
			aclInfo.put("Set", set);
			aclInfo.put("Type", type);
			aclInfo.put("Database", database);
			aclInfo.put("User", user);
			aclInfo.put("Ip", ip);
			aclInfo.put("Method", method);
			aclInfo.put("Option", option);
			aclInfo.put("Changed", "");
			mav.addObject("aclInfo", aclInfo);
		}
		mav.addObject("mode", mode);
		mav.setViewName("aclForm");
		return mav;
	}
		
	/**
	 * DBMS명을 입력받아 ACL 정보를 조회
	 * 
	 * @param model
	 * @param session
	 * @param request
	 * @param serverId
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
		Statement st = null;
		ResultSet rs = null;
		int rowCount = 0;

		//		param.put("user_id", serverId);
				
		//String url = "jdbc:postgresql://192.168.10.70:5432/postgres";
	  //  String usr = "postgres";  
	  //  String pwd = "robin";
	  String [] testArray = null ;
	    
	    List<Map> array = new ArrayList<Map>();
		if(serverId == "") return array;
	    
	    Class.forName("org.postgresql.Driver");
	    
	    // -- 1
	    Connection conn = null;

		try {
			conn = DBCPPoolManager.getConnection(serverId);
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * from pg_catalog.pg_read_file('pg_hba.conf')");
			
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
		} catch(SQLException e){
			Globals.logger.error("SQL 에러코드 ("+e.getSQLState()+") 에러가 발생했습니다.");
			Globals.logger.error(e.getMessage(), e);
	    } finally{
	    	if(rs !=  null) rs.close();
	    	if(st !=  null) st.close();
	    	if(conn !=  null) conn.close();
	    }

		return array;
	}
	
	/**
	 * ACL 저장
	 * 
	 * @param model
	 * @param session
	 * @param request
	 * @param serverId
	 * @param aclArray
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/aclProcess")	
	@ResponseBody
	public Map<String, Object> aclProcess(Model model, HttpSession session, HttpServletRequest request, 
			@RequestParam(value = "serverId", defaultValue = "") String serverId,
			@RequestParam(value = "aclArray", defaultValue = "") String aclArray ) throws Exception {
		ModelAndView mav = new ModelAndView();
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		Map<String, Object> resMap = new HashMap<String, Object>();
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
										
			        Iterator<Integer> keys = hba.keySet().iterator();
			        while( keys.hasNext() ){
			            Integer key = keys.next();
			            config = hba.get(key);
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
            
			String buffer = "";
	        Iterator<Integer> keys = hba.keySet().iterator();
	        while( keys.hasNext() ){
	            Integer key = keys.next();
	            pgHbaConfigLine config = hba.get(key);
	            if(config != null)
					buffer += config.getText() + "\n";
	        }
	        buffer = buffer.replaceAll("'", "''");
			//String url = "jdbc:postgresql://192.168.10.70:5432/postgres";
		  //  String usr = "postgres";  
		  //  String pwd = "robin";
		    String query1 = "SELECT pg_file_unlink('pg_hba.conf.bak');";
		    String query2 = "SELECT pg_file_write('pg_hba.conf.tmp', '" + buffer + "', false);";
		    String query3 = "SELECT pg_file_rename('pg_hba.conf.tmp', 'pg_hba.conf', 'pg_hba.conf.bak');";
		    String query4 = "SELECT pg_reload_conf();";

//	    	conn = DriverManager.getConnection(url, usr, pwd);
	    	conn = DBCPPoolManager.getConnection(serverId);
		
			st = conn.createStatement();
			rs = st.executeQuery(query1);
			conn.setAutoCommit(false); 
			rs = st.executeQuery(query2);
			rs = st.executeQuery(query3);
			rs = st.executeQuery(query4);
			conn.commit();       
			resMap.put("result", "SUCCESS");
			resMap.put("msg", "저장되었습니다.");
	    } catch (Exception e) {
			Globals.logger.error(e.getMessage(), e);
			resMap.put("result", "FAIL");
			resMap.put("msg", "저장에 실패하였습니다.");
	    } finally{
	    	if(rs !=  null) rs.close();
	    	if(st !=  null) st.close();
	    	if(conn !=  null) conn.close();
	    }
	    
        return resMap;
	}
	
	/**
	 * ACL list 삭제
	 * @param model
	 * @param session
	 * @param request
	 * @param seq
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/aclDelete")

	public ModelAndView aclDelete(Model model, HttpSession session, HttpServletRequest request, 
			@RequestParam(value = "seq", defaultValue = "") String seq) throws Exception {		
		ModelAndView mav = new ModelAndView();
		pgHbaConfigLine config = null;
		
        try {   
	        Iterator<Integer> keys = hba.keySet().iterator();
	        while( keys.hasNext() ){
	            Integer key = keys.next();
	            config = hba.get(key);
				if(config.getItemNumber() == Integer.parseInt(seq)){
					hba.remove(key);
					break;
				}
	        }	    
        } catch (Exception e) {
			Globals.logger.error(e.getMessage(), e);			
	    }

		mav.setViewName("acl");
		return mav;
	}
}
