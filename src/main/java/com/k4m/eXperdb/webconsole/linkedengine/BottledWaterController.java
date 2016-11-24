package com.k4m.eXperdb.webconsole.linkedengine;


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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;








import com.k4m.eXperdb.webconsole.common.Globals;


@Controller
public class BottledWaterController {

	@Autowired
	private BottledWaterService bottledWaterService;

	
	/**
	 * 연계엔진관리 > BottledWater 서버 목록 조회 - 페이지 뷰만 반환
	 * @param model
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/bottledwater")   
	public ModelAndView server(Model model, HttpSession session, HttpServletRequest request) throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("bottledWater");
		return mav;
	}
	
	/**
	 * 
	 * 연계엔진관리 > BottledWater 서버 목록 조회 - JSON 형태 데이터 반환
	 * 
	 * @param model 
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/bottledwaterData")  
	@ResponseBody
	public List<Map<String, Object>> serverList(Model model, HttpSession session, HttpServletRequest request) throws Exception {
		List<Map<String, Object>> serverList = null;		
		HashMap<String, String> param = new HashMap<String, String>();
		try{
			serverList = bottledWaterService.selectServerList(param);		
		}catch (Exception e){
			Globals.logger.error(e.getMessage(), e);
		}
		return serverList;
	}
	
	/**
	 * Database 목록 조회 - 페이지 뷰만 반환
	 * @param model
	 * @param session
	 * @param request
	 * @param searchSysNm
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/databaseList")   
	public ModelAndView databaseList(Model model, HttpSession session, HttpServletRequest request,
			@RequestParam(value = "searchSystemName", defaultValue = "") String searchSystemName) throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.addObject("searchSystemName", searchSystemName);
		mav.setViewName("databaseList");
		return mav;
	}
	
	
	/**
	 * Database 목록 조회 - JSON 형태 데이터 반환
	 * @param model
	 * @param session
	 * @param request
	 * @param searchSysNm
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/databaseListData")   
	@ResponseBody
	public List<Map<String, Object>> databaseListData(Model model, HttpSession session, HttpServletRequest request, 
			@RequestParam(value = "searchSystemName", defaultValue = "") String searchSystemName) throws Exception {
		
		List<Map<String, Object>> databaseList = null;		
		try{
			databaseList = bottledWaterService.selectDatabaseList(searchSystemName);
		}catch (Exception e){
			Globals.logger.error(e.getMessage(), e);
		}
		return databaseList;
	}
	
	/**
	 * 연계 테이블 목록 조회 - 페이지 뷰만 반환
	 * @param model
	 * @param session
	 * @param request
	 * @param systemName
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/tableList")   
	public ModelAndView tableList(Model model, HttpSession session, HttpServletRequest request, 
			@RequestParam(value = "systemName", defaultValue = "") String systemName,
			@RequestParam(value = "databaseName", defaultValue = "") String databaseName,
			@RequestParam(value = "connectName", defaultValue = "") String connectName) throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.addObject("systemName", systemName);
		mav.addObject("databaseName", databaseName);
		mav.addObject("connectName", connectName);
		mav.setViewName("tableList");
		return mav;
	}
	
	
	/**
	 * 연계 테이블 목록 조회 - JSON 형태 데이터 반환
	 * @param model
	 * @param session
	 * @param request
	 * @param systemName
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/tableListData")   
	@ResponseBody
	public Map<String, Object> tableListData(Model model, HttpSession session, HttpServletRequest request, 
			@RequestParam(value = "systemName", defaultValue = "") String systemName,
			@RequestParam(value = "databaseName", defaultValue = "") String databaseName,
			@RequestParam(value = "connectName", defaultValue = "") String connectName, 
			@RequestParam(value = "draw", defaultValue = "1") int draw,
			@RequestParam(value = "start", defaultValue = "1") int start,
			@RequestParam(value = "length", defaultValue = "1") int length) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();

		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("systemName", systemName);
		param.put("databaseName", databaseName);
		param.put("connectName", connectName);
		param.put("draw", draw);
		param.put("start", start);
		param.put("length", length);

		try{
			resultMap = bottledWaterService.selectTableList(param);
			
		}catch (Exception e){
			Globals.logger.error(e.getMessage(), e);
		}
		return resultMap;
	}
	
	/**
	 * 프로세스 재기동 또는 중지
	 * @param model
	 * @param session
	 * @param request
	 * @param systemName
	 * @param databaseName
	 * @param command
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/runProcess")   
	@ResponseBody
	public Map<String, Object> runProcess(Model model, HttpSession session, HttpServletRequest request,
			@RequestParam(value = "systemName", defaultValue = "") String systemName,
			@RequestParam(value = "databaseName", defaultValue = "") String databaseName,
			@RequestParam(value = "command", defaultValue = "") String command) throws Exception {
		
		Map<String, String> param = new HashMap<String, String>();
		Map<String, Object> resMap  = new HashMap<String, Object>();

		param.put("systemName", systemName);
		param.put("databaseName", databaseName);
		param.put("command", command);
	
		try{
			resMap = bottledWaterService.runProcess(param);
		}catch (Exception e){
			Globals.logger.error(e.getMessage(), e);
		}
		
		Globals.logger.debug("파라미터(systemName="+systemName+", databaseName="+databaseName+", command="+command+")에 대한 결과 메시지 ="+resMap.get("msg"));
		
		return resMap;
	}
	
	

}
