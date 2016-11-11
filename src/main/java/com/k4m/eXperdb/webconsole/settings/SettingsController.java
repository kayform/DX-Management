package com.k4m.eXperdb.webconsole.settings;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.dxmig.db.DBCPPoolManager;
import com.dxmig.db.datastructure.ConfigInfo;
import com.dxmig.svr.socket.client.ClientAdapter;
import com.k4m.eXperdb.webconsole.common.CommonService;
import com.k4m.eXperdb.webconsole.common.DataHistoryService;
import com.k4m.eXperdb.webconsole.common.Globals;
import com.k4m.eXperdb.webconsole.common.SHA256;
import com.k4m.eXperdb.webconsole.common.StrUtil;
import com.k4m.eXperdb.webconsole.security.CustomUserDetails;
import com.k4m.eXperdb.webconsole.util.DateUtils;
import com.k4m.eXperdb.webconsole.util.SecureManager;

@Controller
public class SettingsController {
	@Autowired 
	private PooledDataSource dataSource;
	@Autowired
	private SettingsService settingsService;
	@Autowired
	private DataHistoryService dataHistoryService;
	@Autowired
	private CommonService commonService;
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
	@RequestMapping(value = "/userForm")
	public ModelAndView userForm(Model model, HttpSession session, HttpServletRequest request, 
			@RequestParam(value = "userId", defaultValue = "") String userId,
			@RequestParam(value = "mode", defaultValue = "") String mode) throws Exception {		
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
		return mav;
	}

	/**
	 * 사용자정보와 Mode(CUD)를 입력받아 사용자 정보를 DB에 저장, 수정, 삭제
	 * 
	 * data history 기능 사용자 요구사항에 의거 제거 edited by manimany  
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
	@RequestMapping(value = "/userProcess")
	@ResponseBody
	public Map<String, Object> userProcess(Model model, HttpSession session, HttpServletRequest request, 
			@RequestParam(value = "mode", defaultValue = "") String mode,
			@RequestParam(value = "userId", defaultValue = "") String userId,
			@RequestParam(value = "userName", defaultValue = "") String userName,
			@RequestParam(value = "password1", defaultValue = "") String password1,
			@RequestParam(value = "password2", defaultValue = "") String password2,
			@RequestParam(value = "jgd", defaultValue = "") String jgd,
			@RequestParam(value = "authDivision", defaultValue = "") String authDivision,
			@RequestParam(value = "blg", defaultValue = "") String blg,
			@RequestParam(value = "department", defaultValue = "") String department,
			@RequestParam(value = "phoneNo", defaultValue = "") String phoneNo,
			@RequestParam(value = "cg_biz_def", defaultValue = "") String cg_biz_def,
			@RequestParam(value = "userExpired", defaultValue = "") String userExpired,
			@RequestParam(value = "passwordUseTerm", defaultValue = "") String passwordUseTerm,
			@RequestParam(value = "useYn", defaultValue = "") String useYn,
			@RequestParam(value = "sngl_athr_yn", defaultValue = "") String sngl_athr_yn,
			@RequestParam(value = "pg_mon_client_path", defaultValue = "") String pg_mon_client_path,
			@RequestParam(value = "enc_mng_path", defaultValue = "") String enc_mng_path) throws Exception {
		/*
		ModelAndView mav = new ModelAndView();
		
		HashMap<String, String> param = new HashMap<String, String>();
		
		param.put("mode", mode);
		param.put("user_id", userId);
		param.put("user_nm", userName);
		
		param.put("jgd", jgd);
		param.put("auth_dv", authDivision);
		param.put("blg", blg);
		param.put("dept", department);
		param.put("hpnm_no", phoneNo);
		param.put("cg_biz_def", cg_biz_def);
		param.put("user_expd", DateUtils.Str2Str(userExpired, "yyyy-mm-dd", "yyyymmdd"));
		param.put("pwd_use_term", passwordUseTerm);
		param.put("use_yn", useYn);
		param.put("sngl_athr_yn", sngl_athr_yn);
		param.put("pg_mon_client_path", StrUtil.hasValue(pg_mon_client_path));
		param.put("enc_mng_path", StrUtil.hasValue(enc_mng_path));
		
		int rowCount = 0;
		
		if (mode.equals(Globals.MODE_DATA_INSERT)) {
			param.put("user_pw", SHA256.SHA256(password1));
			rowCount = settingsService.insertUser(param);
		} else if (mode.equals(Globals.MODE_DATA_UPDATE)) {
			rowCount = settingsService.updateUser(param);
		} else if (mode.equals(Globals.MODE_DATA_DELETE)) {
			rowCount = settingsService.deleteUser(param);
		}
		
		mav.setViewName("user");
		return mav;
		*/
		
		Map<String, Object> resMap  = new HashMap<String, Object>();
		
		String rtn = "";
		try{
			HashMap<String, String> param = new HashMap<String, String>();
			
			param.put("mode", mode);
			param.put("user_id", userId);
			param.put("user_nm", userName);
			
			param.put("jgd", jgd);
			param.put("auth_dv", authDivision);
			param.put("blg", blg);
			param.put("dept", department);
			param.put("hpnm_no", phoneNo);
			param.put("cg_biz_def", cg_biz_def);
			param.put("user_expd", DateUtils.Str2Str(userExpired, "yyyy-mm-dd", "yyyymmdd"));
			param.put("pwd_use_term", passwordUseTerm);
			param.put("use_yn", useYn);
			param.put("sngl_athr_yn", sngl_athr_yn);
			param.put("pg_mon_client_path", StrUtil.hasValue(pg_mon_client_path));
			param.put("enc_mng_path", StrUtil.hasValue(enc_mng_path));
			
			int rowCount = 0;
			
			if (mode.equals(Globals.MODE_DATA_INSERT)) {
				param.put("user_pw", SHA256.SHA256(password1));
				rowCount = settingsService.insertUser(param);
				rtn = "유저가 추가되었습니다.";
			} else if (mode.equals(Globals.MODE_DATA_UPDATE)) {
				rowCount = settingsService.updateUser(param);
				rtn = "유저정보가 수정되었습니다.";
			} else if (mode.equals(Globals.MODE_DATA_DELETE)) {
				rowCount = settingsService.deleteUser(param);
				rtn = "유저가 삭제되었습니다.";
			}
			
			if (rowCount == 0) {
				resMap.put("msg", "암호를 수정할 유저정보를 찾을 수 없습니다.");
				resMap.put("result", "FAIL");
				return resMap;
			}
		}catch(Exception e){
			Globals.logger.error(e.getMessage(), e);
			
			rtn = e.getMessage();
			resMap.put("msg", rtn);
			resMap.put("result", "FAIL");
			throw new Exception(e.getMessage(), e);
		}				
		
		resMap.put("msg", rtn);
		resMap.put("result", "SUCCESS");
		
		return resMap;
	}
	
	@RequestMapping(value = "/userPasswordForm")
	public ModelAndView userPasswordForm(Model model, HttpSession session, HttpServletRequest request, 
			@RequestParam(value = "userId", defaultValue = "") String userId,
			@RequestParam(value = "mode", defaultValue = "") String mode) throws Exception {		
		ModelAndView mav = new ModelAndView();
		Map<String, Object> userInfo = new HashMap<String, Object>();
		if (!(userId == null || "".equals(userId))) {			
			HashMap<String, String> param = new HashMap<String, String>();
			param.put("user_id", userId);
			param.put("mode", mode);
			userInfo = settingsService.selectUser(param);
			mav.addObject("userInfo", userInfo);
		} 
		mav.addObject("mode", mode);
		mav.setViewName("userPasswordForm");
		return mav;
	}
	
	@RequestMapping(value = "/userPasswordProcess")
	@ResponseBody
	public Map<String, Object> userPasswordProcess(Model model, HttpSession session, HttpServletRequest request, 
			@RequestParam(value = "mode", defaultValue = "") String mode,
			@RequestParam(value = "userId", defaultValue = "") String userId,
			@RequestParam(value = "password1", defaultValue = "") String password1,
			@RequestParam(value = "password2", defaultValue = "") String password2) throws Exception {		
		//ModelAndView mav = new ModelAndView();
		
		HashMap<String, String> param = new HashMap<String, String>();
		Map<String, Object> resMap  = new HashMap<String, Object>();
		String rtn = "암호가 수정되었습니다.";
		try{
			param.put("mode", mode);
			param.put("user_id", userId);
			param.put("user_pw", SHA256.SHA256(password1));		
			
			int rowCount = 0;
			
			rowCount = settingsService.updateUserPassword(param);
			
			if (rowCount == 0) {
				resMap.put("msg", "암호를 수정할 유저정보를 찾을 수 없습니다.");
				resMap.put("result", "FAIL");
				return resMap;
			}
		}catch(Exception e){
			Globals.logger.error(e.getMessage(), e);
			
			rtn = e.getMessage();
			resMap.put("msg", rtn);
			resMap.put("result", "FAIL");
			throw new Exception(e.getMessage(), e);
		}				
		
		resMap.put("msg", rtn);
		resMap.put("result", "SUCCESS");
		//dataHistoryService.add("userPassword", mode, (String)session.getAttribute("userId"), request.getRemoteAddr(), userId, null, new JSONObject(param).toJSONString().getBytes("UTF-8"));
		
		//mav.setViewName("user");
		//return mav;
		return resMap;
	}
	
	/**
	 * 서버관리 > 사용자관리 메뉴에서 사용하는 사용자 목록 조회
	 * @param model
	 * @param session
	 * @param request
	 * @param searchAuthDivision
	 * @param searchUseYn
	 * @param searchUserName
	 * @return
	 * @throws Exception
	 * @author manimany
	 */
	//TODO searchAuthDivision 및 searchUseYn 사용여부 확인 및 제거할지 결정 !! user-mapper.xml에는 조건값에 포함되어 있음. remarked by manimany
	@RequestMapping(value = "/user")   
	public ModelAndView user(Model model, HttpSession session, HttpServletRequest request, 
			@RequestParam(value = "searchAuthDivision", defaultValue = "") String searchAuthDivision, 
			@RequestParam(value = "searchUseYn", defaultValue = "") String searchUseYn,
			@RequestParam(value = "searchUserName", defaultValue = "") String searchUserName) throws Exception {
		List<Map<String, Object>> userList = null;		
		HashMap<String, String> param = new HashMap<String, String>();
	
		param.put("auth_dv", (searchAuthDivision == null || "".equals(searchAuthDivision)) ? "%" : "%" + searchAuthDivision + "%");
		param.put("use_yn", (searchUseYn == null || "".equals(searchUseYn)) ? "%" : "%" + searchUseYn + "%");
		param.put("user_nm", (searchUserName == null || "".equals(searchUserName)) ? "%" : "%" + searchUserName + "%");
		
		
		try{
			userList = settingsService.selectUserList(param);		
		}catch (Exception e){
			Globals.logger.error(e.getMessage(), e);
		}
		ModelAndView mav = new ModelAndView();
		mav.addObject("userList", userList);
		mav.addObject("searchAuthDivision", searchAuthDivision);
		mav.addObject("searchUseYn", searchUseYn);
		mav.addObject("searchUserName", searchUserName);
		
		//mav.addObject("serverId", session.getAttribute("serverId"));
		//mav.addObject("userAuth", session.getAttribute("userAuth"));

		mav.setViewName("user");
		return mav;
	}
	
	@RequestMapping(value = "/server")
	public ModelAndView dbms(Model model, HttpSession session, HttpServletRequest request, @RequestParam(value = "searchSystemName", defaultValue = "") String searchSystemName,
			@RequestParam(value = "type", defaultValue = "") String type, 
			@RequestParam(value = "searchIp", defaultValue = "") String searchIp, @RequestParam(value = "currentPage", defaultValue = "1") int currentPage) throws Exception {
		ModelAndView mav = new ModelAndView();
		try {
			// 리스트 네이게이션 개수
			int countPerPage = 5;
			// 리스트 개수
			int countPerList = 10;
			
			if (currentPage < 1) {
				currentPage = 1;
			}
			
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("searchSystemName", (searchSystemName == null || searchSystemName.equals("")) ? "%" : "%" + searchSystemName + "%");
			param.put("type", (type == null || type.equals("")) ? "%" : "%" + type + "%");			
			param.put("searchIp", (searchIp == null || searchIp.equals("")) ? "%" : "%" + searchIp + "%");
			
			Map<String , Object> requestMap = new HashMap<String, Object>();
			requestMap.put("SEARCH_PARAM", param);
			requestMap.put("PAGE_SIZE", Integer.toString(countPerList));
			requestMap.put("CURRENT_PAGE", Integer.toString(currentPage));
			
			int totalCount = settingsService.selectSERVERTotalCount(param); // 데이터 전체 건수 조회
			List<Map<String, Object>> list = settingsService.selectSERVER(requestMap); // 데이터 리스트 조회

			List<Map<String,Object>> serverTypeList = commonService.selectSystemCode("R0001");
			mav.addObject("serverTypeList", serverTypeList);
			/*
			PagingUtil pageUtil = new PagingUtil(currentPage, countPerPage, totalCount, countPerList);
			String page = pageUtil.getPageForKor("form01", "/server");
			String pageNavigator = pageUtil.getPageNavigator();
			
			mav.addObject("page" , page);
			mav.addObject("pageNavigator" , pageNavigator);
			mav.addObject("currentPage", currentPage);
			mav.addObject("searchSystemName", searchSystemName);
			mav.addObject("searchIp", searchIp);
			*/
			
			mav.addObject("list", list);
			mav.setViewName("server");
		} catch (Exception e) {
			Globals.logger.error(e.getMessage(), e);
			throw e;
		}
		return mav;
	}
	
	/**
	 * 사용자 등록시 입력한 사용자 ID에 대해 중복값을 체크
	 * @param model
	 * @param session
	 * @param request
	 * @param user_id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/userDuplicateCheack")
	public @ResponseBody String userDuplicateCheack(Model model, HttpSession session, HttpServletRequest request, 
			@RequestParam(value = "userId", defaultValue = "") String userId) throws Exception {		
		
		Map<String, Object> userInfo = new HashMap<String, Object>();
		String isDuplicate = "Y";
		if (!(userId == null || "".equals(userId))) {			
			HashMap<String, String> param = new HashMap<String, String>();
			param.put("mode", "I");
			param.put("user_id", userId);
			userInfo = settingsService.selectUser(param);
			
			String tmp = (String) userInfo.get("user_id");
			
			if (userId.equals(tmp)) {
				isDuplicate = "Y";
			} else {
				isDuplicate = "N";
			}
		}
		
		return isDuplicate;
	}
	
	@RequestMapping(value = "/serverForm")
	public ModelAndView dbmsForm(Model model, HttpSession session, HttpServletRequest request,
			@RequestParam(value = "mode", defaultValue = "") String mode,
			@RequestParam(value = "sys_nm", defaultValue = "") String systemName,
			@RequestParam(value = "type", defaultValue = "") String type,
			@RequestParam(value = "ip", defaultValue = "") String ip) throws Exception {
		ModelAndView mav = new ModelAndView();
		Map<String, Object> param = new HashMap<String, Object>();

		List<Map<String,Object>> serverTypeList = commonService.selectSystemCode("R0001");
		mav.addObject("serverTypeList", serverTypeList);
		
		if(!Globals.MODE_DATA_INSERT.equals(mode)) {
			param = new HashMap<String, Object>();
			param.put("searchSystemName", systemName);
			param.put("type", type);
			param.put("ip", ip);
			
			List<Map<String, Object>> serverInfoList = settingsService.selectSERVERDetail(param); // 데이터 전체 건수 조회
			
			if(serverInfoList.size() > 0) {
				mav.addObject("serverInfo", serverInfoList);
				/*
				mav.addObject("systemName", list.get(0).get("sys_nm").toString());
				mav.addObject("type", list.get(0).get("type").toString());
				mav.addObject("databaseName", list.get(0).get("db_nm").toString());
				mav.addObject("ip", list.get(0).get("ip").toString());
				mav.addObject("port", list.get(0).get("port").toString());
				mav.addObject("user_id",list.get(0).get("user_id").toString());
				mav.addObject("user_pw",list.get(0).get("user_pw").toString());
				mav.addObject("lt_wk_dtti",list.get(0).get("lt_wk_dtti").toString());
				mav.addObject("lt_wk_prsn",list.get(0).get("lt_wk_prsn").toString());
				*/
			}
		}
		
		mav.addObject("mode", mode);
		mav.setViewName("serverForm");
		return mav;
	}

	@RequestMapping(value = "/serverConnCheck")
	@ResponseBody
	public Map<String, Object> serverConnCheck(Model model, HttpSession session, HttpServletRequest request, 
			@RequestParam(value = "mode", defaultValue = "") String mode,
			@RequestParam(value = "sys_nm", defaultValue = "") String sys_nm,
			@RequestParam(value = "type", defaultValue = "") String type,
			@RequestParam(value = "db_nm", defaultValue = "") String db_nm,
			@RequestParam(value = "ip", defaultValue = "") String ip,
			@RequestParam(value = "port", defaultValue = "") String port,
			@RequestParam(value = "user_id", defaultValue = "") String user_id,
			@RequestParam(value = "user_pw", defaultValue = "") String user_pw) throws Exception {
		Map<String, Object> resMap = new HashMap<String, Object>();
		ConfigInfo configInfo = new ConfigInfo();
   		configInfo.SERVERIP = ip;
        configInfo.USERID = user_id;
        configInfo.DB_PW = user_pw;
        configInfo.PORT = port;
        configInfo.DBNAME = db_nm;
        configInfo.SCHEMA_NAME = user_id;
        configInfo.DB_TYPE = "POG";	            

		String msg = "";
		try {
			DBCPPoolManager.setupDriver(configInfo, sys_nm, 1);
		} catch (Exception e) {
			Globals.logger.error(e.getMessage(), e);
			
			msg = e.getMessage();
			resMap.put("msg", msg);
			resMap.put("result", "FAIL");
			throw new Exception(e.getMessage(), e);
		}finally{
    		DBCPPoolManager.shutdownDriver(sys_nm);
		}

		msg  = "접속이 성공했습니다.";

		ByteArrayOutputStream requestOutputStream = new ByteArrayOutputStream();
		requestOutputStream.write(msg.getBytes("UTF-8"));
		msg = requestOutputStream.toString("UTF-8");

		resMap.put("result", "SUCCESS");
		resMap.put("msg", msg);
		
		return resMap;
	}
	
	@RequestMapping(value = "/serverProcess")
	@ResponseBody
	public Map<String, Object> serverProcess(Model model, HttpSession session, HttpServletRequest request, 
			@RequestParam(value = "mode", defaultValue = "") String mode,
			@RequestParam(value = "sys_nm", defaultValue = "") String sys_nm,
			@RequestParam(value = "type", defaultValue = "") String type,
			@RequestParam(value = "db_nm", defaultValue = "") String db_nm,
			@RequestParam(value = "ip", defaultValue = "") String ip,
			@RequestParam(value = "port", defaultValue = "") String port,
			@RequestParam(value = "user_id", defaultValue = "") String user_id,
			@RequestParam(value = "user_pw", defaultValue = "") String user_pw) throws Exception {
		HashMap<String , String> param = null;
		Map<String, Object> resMap = new HashMap<String, Object>();
		try {
			sys_nm = StrUtil.hasValue(sys_nm);
			type = StrUtil.hasValue(type);
			db_nm = StrUtil.hasValue(db_nm);
			ip = StrUtil.hasValue(ip);
			port = StrUtil.hasValue(port);
			user_id = StrUtil.hasValue(user_id);
			user_pw = StrUtil.hasValue(user_pw);
			
			int rowCount = 0;
			String msg = "";
			
			if(Globals.MODE_DATA_DELETE.equals(mode)) {
				param = new HashMap<String, String>();
				param.put("sys_nm", sys_nm);
				msg = "서버정보가 삭제되었습니다.";
				rowCount = settingsService.deleteSERVER(param);				
			} else {
				param = new HashMap<String, String>();
				param.put("sys_nm", sys_nm);
				param.put("type", type);
				param.put("db_nm", db_nm);
				param.put("ip", ip);
				param.put("port", port);
				param.put("user_id", user_id);
				
				switch(type){
				case "POSTGRESQL":
		    		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		    		Map<String, Object> tempMap  = new HashMap<String, Object>();
					ConfigInfo configInfo = new ConfigInfo();
		       		configInfo.SERVERIP = ip;
		            configInfo.USERID = user_id;
		            configInfo.DB_PW = user_pw;
		            configInfo.PORT = port;
		            configInfo.DBNAME = db_nm;
		            configInfo.SCHEMA_NAME = user_id;
		            configInfo.DB_TYPE = "POG";	   
		    		String rtn = "";
		    		try {
		    			DBCPPoolManager.setupDriver(configInfo, sys_nm, 1);
		    		} catch (Exception e) {
		    			Globals.logger.error(e.getMessage(), e);
		    			
		    			rtn = e.getMessage();
		    			resMap.put("msg", rtn);
						resMap.put("result", "FAIL");
		    			throw new Exception(e.getMessage(), e);
		    		}finally{
		        		DBCPPoolManager.shutdownDriver(sys_nm);
		    		}
					break;
				case "KAFKA":
					break;
				case "KAFKA-CONNECT":
					break;
				case "KAFKA-SCHEMA-REGISTRY":
					break;
				case "CLOUDERA-MANAGER":
					break;
				}
				String databasePw = "";
				try {
					//db_pw = SecureManager.encrypt(req.getParameter("databasePassword"));
					databasePw = SecureManager.encrypt(user_pw);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Globals.logger.error(e.getMessage(), e);
				}
				param.put("user_pw", databasePw);
				

				CustomUserDetails userDetails = (CustomUserDetails) session.getAttribute("userLoginInfo");
				param.put("lt_wk_prsn", userDetails.getUserid());

				if(Globals.MODE_DATA_INSERT.equals(mode)) {
					rowCount = settingsService.insertSERVER(param);
					msg = "서버정보가 추가되었습니다.";
					//dataHistoryService.add("dbms", mode, (String)session.getAttribute("userId"), request.getRemoteAddr(), systemName, null, new JSONObject(param).toJSONString().getBytes("UTF-8"));
				} else if(Globals.MODE_DATA_UPDATE.equals(mode)) {
					rowCount = settingsService.updateSERVER(param);
					msg = "서버정보가 수정되었습니다.";
					//dataHistoryService.add("dbms", mode, (String)session.getAttribute("userId"), request.getRemoteAddr(), systemName, null, new JSONObject(param).toJSONString().getBytes("UTF-8"));
				}
			}
			
			if (rowCount == 0) {
				resMap.put("msg", "수정/삭제할 서버정보를 찾을 수 없습니다.");
				resMap.put("result", "FAIL");
				return resMap;
			}
			
			resMap.put("result", "SUCCESS");
			resMap.put("msg", msg);
		} catch (Exception e) {
			Globals.logger.error(e.getMessage(), e);
			resMap.put("result", "ERROR");
			resMap.put("msg", e.getMessage());
		}
		return resMap;
	}
	
	@RequestMapping(value = "/systemNameCheck")
	@ResponseBody
	public Map<String, Object> dbmsSystemNameCheck(Model model, HttpSession session, HttpServletRequest request, 
			@RequestParam(value = "sys_nm", defaultValue = "") String sys_nm,
			@RequestParam(value = "ip", defaultValue = "") String ip,
			@RequestParam(value = "port", defaultValue = "") String port) throws Exception {
		HashMap<String , String> param = null;
		Map<String, Object> resMap = new HashMap<String, Object>();
		try {
			param = new HashMap<String, String>();
			param.put("sys_nm", sys_nm);
			param.put("ip", ip);
			param.put("port", port);
			int cnt = settingsService.selectSERVERDupCheck(param);
			if(cnt > 0) {
				resMap.put("isDuplicate", true);
			} else {
				resMap.put("isDuplicate", false);
			}
			resMap.put("result", "SUCCESS");
			resMap.put("msg", "");
		} catch (Exception e) {
			Globals.logger.error(e.getMessage(), e);
			resMap.put("isDuplicate", true);
			resMap.put("result", "ERROR");
			resMap.put("msg", e.getMessage());
		}
		return resMap;
	}
	
	/**
	 * 사용자 ID를 입력받아 해당 사용자가 접근가능한 메뉴리스트를 반환
	 * @param model
	 * @param session
	 * @param request
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/userAuthForm")
	public ModelAndView userAuthForm(Model model, HttpSession session, HttpServletRequest request, 
			@RequestParam(value = "userId", defaultValue = "") String userId) throws Exception {		
		ModelAndView mav = new ModelAndView();
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("user_id", userId);
		List<Map<String, Object>> menuList = settingsService.selectUserAuth(param);
		mav.addObject("menuList", menuList);
		mav.addObject("userId", userId);
		mav.setViewName("userAuthForm");
		return mav;
	}
	
	
	/**
	 * 사용자에 대한 메뉴권한 처리
	 * @param model
	 * @param session
	 * @param request
	 * @param mode
	 * @param userId
	 * @param menuId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/userAuthProcess")
	@ResponseBody
	public HashMap<String, Object> userAuthProcess(Model model, HttpSession session, HttpServletRequest request, 
			@RequestParam(value = "mode", defaultValue = "") String[] mode,
			@RequestParam(value = "userId", defaultValue = "") String[] userId,
			@RequestParam(value = "menuId", defaultValue = "") String[] menuId) throws Exception {	
		HashMap<String, Object> responseMap = new HashMap<String, Object>();
		try {

//			HashMap<String, String> param = new HashMap<String, String>();
//			param.put("mode", mode);
//			param.put("user_id", userId);
//			param.put("menu_id", menuId);
			
			@SuppressWarnings("unused")
			int rowCount = 0;

			String[] modeArr = request.getParameterValues("mode");
			String[] userIdArr = request.getParameterValues("userId");
			String[] menuIdArr = request.getParameterValues("menuId");
		    
			if(modeArr != null) {
				List<HashMap<String, String>> paramList = new ArrayList<HashMap<String, String>>();
				for(int i=0;i<modeArr.length;i++) {
					HashMap<String, String> param = new HashMap<String, String>();
					param.put("mode", modeArr[i]);
					param.put("user_id", userIdArr[i]);
					param.put("menu_id", menuIdArr[i]);
					paramList.add(param);
				}
	
				rowCount = settingsService.updateUserAuthList(paramList);
				
				dataHistoryService.add("userAuth", "I", (String)session.getAttribute("userId"), request.getRemoteAddr(), userIdArr[0], null, JSONArray.toJSONString(paramList).getBytes("UTF-8"));
				
//				if (mode.equals(Globals.MODE_DATA_INSERT)) {
//					rowCount = settingsService.insertUserAuth(param);
//				} else if (mode.equals(Globals.MODE_DATA_DELETE)) {
//					rowCount = settingsService.deleteUserAuth(param);
//				}
				
				responseMap.put("resultMessage", "SUCCESS");
			} else {
				throw new Exception("잘못된 요청입니다.");
			}
		} catch (Exception e) {
			if (!(e.getMessage().indexOf("duplicate key value violates unique constraint") > -1)) {
				Globals.logger.error(e.getMessage(), e);
				responseMap.put("resultMessage", e.getMessage());
			}			
		}
		return responseMap;
	}
}
