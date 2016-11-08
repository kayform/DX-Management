package com.k4m.eXperdb.webconsole.settings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.k4m.eXperdb.webconsole.common.DataHistoryService;
import com.k4m.eXperdb.webconsole.common.Globals;
import com.k4m.eXperdb.webconsole.common.SHA256;
import com.k4m.eXperdb.webconsole.util.DateUtils;

@Controller
public class SettingsController {
	@Autowired 
	private PooledDataSource dataSource;
	@Autowired
	private SettingsService settingsService;
	@Autowired
	private DataHistoryService dataHistoryService;
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
	public ModelAndView userProcess(Model model, HttpSession session, HttpServletRequest request, 
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
		param.put("pg_mon_client_path", pg_mon_client_path);
		param.put("enc_mng_path", enc_mng_path);
		
		int rowCount = 0;
		
		if (mode.equals(Globals.MODE_DATA_INSERT)) {
			param.put("user_pw", SHA256.SHA256(password1));
			rowCount = settingsService.insertUser(param);
			dataHistoryService.add("user", mode, (String)session.getAttribute("userId"), request.getRemoteAddr(), userId, null, new JSONObject(param).toJSONString().getBytes("UTF-8"));
		} else if (mode.equals(Globals.MODE_DATA_UPDATE)) {
			rowCount = settingsService.updateUser(param);
			dataHistoryService.add("user", mode, (String)session.getAttribute("userId"), request.getRemoteAddr(), userId, null, new JSONObject(param).toJSONString().getBytes("UTF-8"));
		} else if (mode.equals(Globals.MODE_DATA_DELETE)) {
			rowCount = settingsService.deleteUser(param);
			dataHistoryService.add("user", mode, (String)session.getAttribute("userId"), request.getRemoteAddr(), userId, null, new JSONObject(param).toJSONString().getBytes("UTF-8"));
		}
		
		mav.setViewName("user");
		return mav;
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
	public ModelAndView userPasswordProcess(Model model, HttpSession session, HttpServletRequest request, 
			@RequestParam(value = "mode", defaultValue = "") String mode,
			@RequestParam(value = "userId", defaultValue = "") String userId,
			@RequestParam(value = "password1", defaultValue = "") String password1,
			@RequestParam(value = "password2", defaultValue = "") String password2) throws Exception {		
		ModelAndView mav = new ModelAndView();
		
		HashMap<String, String> param = new HashMap<String, String>();
		
		param.put("mode", mode);
		param.put("user_id", userId);
		param.put("user_pw", SHA256.SHA256(password1));		
		
		int rowCount = 0;
		
		rowCount = settingsService.updateUserPassword(param);
		
		dataHistoryService.add("userPassword", mode, (String)session.getAttribute("userId"), request.getRemoteAddr(), userId, null, new JSONObject(param).toJSONString().getBytes("UTF-8"));
		
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
	//TODO searchAuthDivision 사용여부 확인 및 제거할지 결정 !! user-mapper.xml에는 조건값에 포함되어 있음. remarked by manimany
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
		
		mav.addObject("serverId", session.getAttribute("serverId"));
		mav.addObject("userAuth", session.getAttribute("userAuth"));

		mav.setViewName("user");
		return mav;
	}
}
