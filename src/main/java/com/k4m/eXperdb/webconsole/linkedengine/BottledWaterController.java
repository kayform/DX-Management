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
	 * 
	 * 연계엔진관리 > BottledWater 서버 목록 조회 페이지에서 데이터 없는 형태의 HTML을 생성하고 데이터는 같은 이름의 메소드에서 json 데이터 형태로 반환함
	 * 
	 * 
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
	 * 연계엔진관리 > BottledWater 서버 목록 조회 페이지에서 데이터 없는 형태의 HTML을 생성하고 데이터는 같은 이름의 메소드에서 json 데이터 형태로 반환함
	 * 
	 * searchServerType 파라미터는 현재 사용하지 않으나 향후 요구사항 변경을 대비하여 존치시킴 remarked by manimany
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
	 * Database 목록 조회 - 데이터 반환
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
	 * 연계 테이블 목록 조회 - 데이터 반환
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
	public List<Map<String, Object>> tableListData(Model model, HttpSession session, HttpServletRequest request, 
			@RequestParam(value = "systemName", defaultValue = "") String systemName,
			@RequestParam(value = "databaseName", defaultValue = "") String databaseName,
			@RequestParam(value = "connectName", defaultValue = "") String connectName,
			@RequestParam(value = "currentPage", defaultValue = "1") int currentPage) throws Exception {
		
		List<Map<String, Object>> tableList = null;		
		

		HashMap<String, String> param = new HashMap<String, String>();
		param.put("systemName", systemName);
		param.put("databaseName", databaseName);
		param.put("connectName", connectName);
		param.put("PAGE_SIZE", Integer.toString(Globals.PAGING_COUNT_PER_LIST));
		param.put("CURRENT_PAGE", Integer.toString(currentPage));

		try{

			int totalCount = bottledWaterService.selectTableListTotalCount(param); // 데이터 전체 건수 조회
			tableList = bottledWaterService.selectTableList(param);
			
		}catch (Exception e){
			Globals.logger.error(e.getMessage(), e);
		}
		return tableList;
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
