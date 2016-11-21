package com.k4m.eXperdb.webconsole.settings;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.session.SqlSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.k4m.eXperdb.webconsole.common.CommonDAO;
import com.k4m.eXperdb.webconsole.common.CommonService;
import com.k4m.eXperdb.webconsole.common.CommonServiceImpl;
import com.k4m.eXperdb.webconsole.common.DataHistoryService;
import com.k4m.eXperdb.webconsole.common.ExtractMybatisQuery;
import com.k4m.eXperdb.webconsole.common.Globals;
import com.k4m.eXperdb.webconsole.common.SHA256;
import com.k4m.eXperdb.webconsole.common.StrUtil;
import com.k4m.eXperdb.webconsole.common.TransactionManager;
import com.k4m.eXperdb.webconsole.db.ConfigInfo;
import com.k4m.eXperdb.webconsole.db.DBCPPoolManager;
import com.k4m.eXperdb.webconsole.db.DataAdapter;
import com.k4m.eXperdb.webconsole.db.DataTable;
import com.k4m.eXperdb.webconsole.dbmsman.PgmonService;
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
	@Autowired
	private PgmonService pgmonService;

	@Autowired 
	private PlatformTransactionManager platTransactionManager; 

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
	@RequestMapping(value = "/userDetail")
	@ResponseBody
	public Map<String, Object> userDetail(Model model, HttpSession session, HttpServletRequest request, 
			@RequestParam(value = "userId", defaultValue = "") String userId,
			@RequestParam(value = "mode", defaultValue = "") String mode) throws Exception {		
		Map<String, Object> userInfo = new HashMap<String, Object>();
		if (!(userId == null || userId.equals(""))) {			
			HashMap<String, String> param = new HashMap<String, String>();
			param.put("user_id", userId);
			param.put("mode", "V");
			userInfo = settingsService.selectUser(param);
		} 
		return userInfo;
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
		param.put("user_id", "%");
		
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
	@RequestMapping(value = "/userList")   
	@ResponseBody
	public List<Map<String, Object>> user(Model model, HttpSession session, HttpServletRequest request, 
			@RequestParam(value = "searchAuthDivision", defaultValue = "") String searchAuthDivision, 
			@RequestParam(value = "searchUseYn", defaultValue = "") String searchUseYn,			
			@RequestParam(value = "searchUserId", defaultValue = "") String searchUserId,
			@RequestParam(value = "ownUserId", defaultValue = "") String ownUserId,
			@RequestParam(value = "searchUserName", defaultValue = "") String searchUserName) throws Exception {
		List<Map<String, Object>> userList = null;		
		HashMap<String, String> param = new HashMap<String, String>();
	
		param.put("auth_dv", (searchAuthDivision == null || "".equals(searchAuthDivision)) ? "%" : "%" + searchAuthDivision + "%");
		param.put("use_yn", (searchUseYn == null || "".equals(searchUseYn)) ? "%" : "%" + searchUseYn + "%");
		param.put("user_nm", (searchUserName == null || "".equals(searchUserName)) ? "%" : "%" + searchUserName + "%");
		param.put("user_id", (searchUserId == null || "".equals(searchUserId)) ? "%" : "%" + searchUserId + "%");
		param.put("ownUserId", ownUserId);
		
		try{
			userList = settingsService.selectUserList(param);		
		}catch (Exception e){
			Globals.logger.error(e.getMessage(), e);
		}

		return userList;
	}
	
	@RequestMapping(value = "/server")
	public ModelAndView dbms(Model model, HttpSession session, HttpServletRequest request, @RequestParam(value = "searchSysNm", defaultValue = "") String sys_nm,
			@RequestParam(value = "searchType", defaultValue = "") String type, 
			@RequestParam(value = "searchIp", defaultValue = "") String ip, @RequestParam(value = "currentPage", defaultValue = "1") int currentPage) throws Exception {
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
			param.put("sys_nm", (sys_nm == null || sys_nm.equals("")) ? "%" : "%" + sys_nm + "%");
			param.put("type", (type == null || type.equals("")) ? "%" : "%" + type + "%");			
			param.put("ip", (ip == null || ip.equals("")) ? "%" : "%" + ip + "%");
			/*
			Map<String , Object> requestMap = new HashMap<String, Object>();
			requestMap.put("SEARCH_PARAM", param);
			requestMap.put("PAGE_SIZE", Integer.toString(countPerList));
			requestMap.put("CURRENT_PAGE", Integer.toString(currentPage));
			
			int totalCount = settingsService.selectSERVERTotalCount(param); // 데이터 전체 건수 조회
			*/
//			List<Map<String, Object>> serverList = settingsService.selectSERVER(requestMap); // 데이터 리스트 조회
			List<Map<String, Object>> serverList = settingsService.selectSERVER(param); // 데이터 리스트 조회

			List<Map<String,Object>> serverTypeList = commonService.selectSystemCode("R0001");
			Map<String, Object> totalParamMap = new HashMap<String, Object>();
			totalParamMap.put("sys_mnt_cd", "%");
			totalParamMap.put("sys_mnt_cd_nm", "전체");
			serverTypeList.add(0, totalParamMap);
			
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
			
			mav.addObject("serverList", serverList);
			mav.setViewName("server");
		} catch (Exception e) {
			Globals.logger.error(e.getMessage(), e);
			throw e;
		}
		return mav;
	}
	/**
	 * 서버관리 : 서버리스트 return
	 * @param model
	 * @param session
	 * @param request
	 * @param sys_nm
	 * @param type
	 * @param ip
	 * @param currentPage
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/serverList")
	@ResponseBody
	public List<Map<String, Object>> getServerList(Model model, HttpSession session, HttpServletRequest request, @RequestParam(value = "searchSysNm", defaultValue = "") String sys_nm,
	//public String getServerList(Model model, HttpSession session, HttpServletRequest request, @RequestParam(value = "searchSysNm", defaultValue = "") String sys_nm,
			@RequestParam(value = "searchType", defaultValue = "") String type, 
			@RequestParam(value = "searchIp", defaultValue = "") String ip, @RequestParam(value = "currentPage", defaultValue = "1") int currentPage) throws Exception {
		List<Map<String, Object>> serverList = new ArrayList<Map<String, Object>>();
		try {
			// 리스트 네이게이션 개수
			int countPerPage = 5;
			// 리스트 개수
			int countPerList = 10;
			
			if (currentPage < 1) {
				currentPage = 1;
			}
			
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("sys_nm", (sys_nm == null || sys_nm.equals("")) ? "%" : "%" + sys_nm + "%");
			param.put("type", (type == null || type.equals("")) ? "%" : "%" + type + "%");			
			param.put("ip", (ip == null || ip.equals("")) ? "%" : "%" + ip + "%");
			
			serverList = settingsService.selectSERVER(param); // 데이터 리스트 조회
			/*
			for(Map<String, Object> map : serverList) {
				map.put("mng", "<button id=\"viewBtn\" style=\"margin:0;height:20px;width:50px;\" class=\"button\" onclick=\"javascript:showServerForm('V', '${item.sys_nm}');\"><span class=\"mif-search\"></span></button>");
			}
			*/
		} catch (Exception e) {
			Globals.logger.error(e.getMessage(), e);
			throw e;
		}
		//return listmap_to_json_string(serverList);
		return serverList;
	}
	
	public String listmap_to_json_string(List<Map<String, Object>> list)
	{       
	    JSONArray json_arr=new JSONArray();
	    
	    for (Map<String, Object> map : list) {
	        JSONObject json_obj=new JSONObject();
	        for (Map.Entry<String, Object> entry : map.entrySet()) {
	            String key = entry.getKey();
	            Object value = entry.getValue();
	            try {
	                json_obj.put(key,value);
	            } catch (Exception e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }                           
	        }
	        json_arr.add(json_obj);
	    }
	    return json_arr.toString();
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
			@RequestParam(value = "sys_nm", defaultValue = "") String sys_nm,
			@RequestParam(value = "type", defaultValue = "") String type,
			@RequestParam(value = "ip", defaultValue = "") String ip) throws Exception {
		ModelAndView mav = new ModelAndView();
		Map<String, Object> param = new HashMap<String, Object>();

		List<Map<String,Object>> serverTypeList = commonService.selectSystemCode("R0001");
		mav.addObject("serverTypeList", serverTypeList);
		
		if(!Globals.MODE_DATA_INSERT.equals(mode)) {
			param = new HashMap<String, Object>();
			param.put("sys_nm", sys_nm);
			
			Map<String, Object> serverInfoList = settingsService.selectSERVERDetail(param); // 데이터 전체 건수 조회
			
			if(serverInfoList.size() > 0) {
				mav.addObject("serverInfoList", serverInfoList);
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
	
	public static String getQuery(SqlSession sqlSession, String queryId , Object sqlParam){
		BoundSql boundSql = sqlSession.getConfiguration().getMappedStatement(queryId).getSqlSource().getBoundSql(sqlParam);
		String query1 = boundSql.getSql();
		Object paramObj = boundSql.getParameterObject();
		
		if(paramObj != null){              // 파라미터가 아무것도 없을 경우
			List<ParameterMapping> paramMapping = boundSql.getParameterMappings();
			for(ParameterMapping mapping : paramMapping){
				String propValue = mapping.getProperty();       
				query1=query1.replaceFirst("\\?", "#{"+propValue+"}");
			}
		}
		return query1; 
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
		Map<String, Object> param2 = new HashMap<String, Object>();
		Map<String, Object> resMap = new HashMap<String, Object>();
		
		TransactionManager transactionManager = new TransactionManager(this.platTransactionManager);
		int maxActiveCnt = 5;
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
									
			transactionManager.begin();
			
			if(Globals.MODE_DATA_DELETE.equals(mode)) {
				param = new HashMap<String, String>();
				param.put("sys_nm", sys_nm);
				msg = "서버정보가 삭제되었습니다.";
				rowCount = settingsService.deleteSERVER(param);
				
				int uptPgSvrRowCnt = 0;
				param2.put("conn_name", sys_nm);
				uptPgSvrRowCnt = pgmonService.deletePgmonTbHchkInfo(param2);
				uptPgSvrRowCnt = pgmonService.deletePgSvrInfo(param2);
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
		    		
					ConfigInfo configInfo = new ConfigInfo();
		       		configInfo.SERVERIP = ip;
		            configInfo.USERID = user_id;		            
		            configInfo.PORT = port;
		            configInfo.DBNAME = db_nm;
		            configInfo.SCHEMA_NAME = user_id;
		            configInfo.DB_TYPE = "POG";	  
		            
		    		Map<String, Object> tempMap  = new HashMap<String, Object>();
		    		
		    		if(Globals.MODE_DATA_UPDATE.equals(mode)) {
			    		tempMap.put("sys_nm", sys_nm);					
						Map<String, Object> serverInfoMap = settingsService.selectSERVERDetail(tempMap); // 데이터 전체 건수 조회
						tempMap.clear();
									            
			            if (serverInfoMap.get("user_pw").toString().equals(user_pw)){
			            	user_pw = SecureManager.decrypt(user_pw);
			            }
		    		}
		            
		            configInfo.DB_PW = user_pw;
		            
		    		String rtn = "";
		    		
		    		ConfigInfo bkConfigInfo = null;
		    		try {
		    			if (DBCPPoolManager.ContaintPool(sys_nm)) {
		    				bkConfigInfo = DBCPPoolManager.getConfigInfo(sys_nm);
		    				DBCPPoolManager.shutdownDriver(sys_nm);
		    			}
		    			
		    			DBCPPoolManager.setupDriver(configInfo, sys_nm, maxActiveCnt);		    			
		    			
		    			tempMap.put("usename", user_id);
		    			String sql = commonService.getQuery("pginfo-mapper.selectUserSuperPrivs", null);
		    			
		    			DataAdapter da = DataAdapter.getInstance(sys_nm);
		    			List<Object> binds = new ArrayList<Object>();
		    			binds.add(user_id);
		    			DataTable dt = da.Fill(sql, binds);
		    			
		    			if (dt.getRows().size() == 1) {
		    				boolean usesuper = (boolean)dt.getRows().get(0).get("USESUPER");
		    				if ( usesuper == false) {
		    					throw new Exception("입력된 유저는 슈퍼유저가 아닙니다. 슈퍼유저만 등록가능합니다.");
		    				}
		    			}else{
		    				throw new Exception("입력된 유저의 슈퍼권한정보 추출이 실패하였습니다.");
		    			}
		    			
		    		} catch (Exception e) {		    			
		    			Globals.logger.error(e.getMessage(), e);
		    			
		    			rtn = "DB 접속테스트가 아래의 이유로 실패했습니다.\n" + e.getMessage();
		    			resMap.put("msg", rtn);
						resMap.put("result", "ERROR");
						
						if (DBCPPoolManager.ContaintPool(sys_nm)){
							DBCPPoolManager.shutdownDriver(sys_nm);
							
							if (bkConfigInfo != null) {
								DBCPPoolManager.setupDriver(bkConfigInfo, sys_nm, maxActiveCnt);
							}
						}
						
		    			throw new Exception(e.getMessage(), e);
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
				
				String userPw = "";
				try {
					//db_pw = SecureManager.encrypt(req.getParameter("databasePassword"));
					userPw = SecureManager.encrypt(user_pw);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Globals.logger.error(e.getMessage(), e);
				}
				param.put("user_pw", userPw);
				

				CustomUserDetails userDetails = (CustomUserDetails) session.getAttribute("userLoginInfo");
				param.put("lt_wk_prsn", userDetails.getUserid());			
				
				if(Globals.MODE_DATA_INSERT.equals(mode)) {
					rowCount = settingsService.insertSERVER(param);
					
					param2.clear();
					param2.put("sequence_name", "pgmon.instance_id");
					int instance_id = pgmonService.selectGenerateInstanceId(param2);
					
					param2.clear();
					param2.put("instance_id", instance_id);
					param2.put("server_ip", ip);
					param2.put("service_port", port);
					param2.put("dbms_type", "PostgreUnicodeX64");
					param2.put("conn_user_id", user_id);
					param2.put("conn_user_pwd", user_pw);
					param2.put("collect_yn", "Y");
					param2.put("collect_period_sec", 3);
					param2.put("conn_db_name", db_nm);
					param2.put("conn_name", sys_nm);
					
					int insPgSvrRowCnt = 0;
					insPgSvrRowCnt = pgmonService.insertPgSvrInfo(param2);
					
					param2.clear();
					param2.put("instance_id", instance_id);
					insPgSvrRowCnt = pgmonService.insertPgmonTbHchkInfo(param2);
					msg = "서버정보가 추가되었습니다.";
					//dataHistoryService.add("dbms", mode, (String)session.getAttribute("userId"), request.getRemoteAddr(), systemName, null, new JSONObject(param).toJSONString().getBytes("UTF-8"));
				} else if(Globals.MODE_DATA_UPDATE.equals(mode)) {
					rowCount = settingsService.updateSERVER(param);
					
					param2.clear();
					param2.put("server_ip", ip);
					param2.put("service_port", port);
					param2.put("conn_user_id", user_id);
					param2.put("conn_user_pwd", user_pw);
					param2.put("conn_db_name", db_nm);
					param2.put("conn_name", sys_nm);
					
					int uptPgSvrRowCnt = 0;
					uptPgSvrRowCnt = pgmonService.updatePgSvrInfo(param2);
					msg = "서버정보가 추가되었습니다.";
					
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
			
			transactionManager.commit();
		} catch (Exception e) {
			Globals.logger.error(e.getMessage(), e);
			resMap.put("result", "ERROR");
			
			if (resMap.get("msg") == null) {
				resMap.put("msg", e.getMessage());
			}
			
			transactionManager.rollback();
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
				
				responseMap.put("result", "SUCCESS");
				responseMap.put("msg", "메뉴권한 등록이 완료되었습니다.");
			} else {
				throw new Exception("잘못된 요청입니다.");
			}
		} catch (Exception e) {
			if (!(e.getMessage().indexOf("duplicate key value violates unique constraint") > -1)) {
				Globals.logger.error(e.getMessage(), e);
				responseMap.put("result", "ERROR");
				responseMap.put("msg", e.getMessage());
			}			
		}
		return responseMap;
	}
}
