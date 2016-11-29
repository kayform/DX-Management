package com.k4m.eXperdb.webconsole.linkedengine;


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
	 * 
	 * 파라미터 draw, start, length 는 데이터테이블에서 자동으로 넘오오는 변수
	 * 리턴 타입 객체 안에는 draw,recordsTotal, recordsFiltered 변수를 셋팅해서 넘겨주면 데이터테이블에서 내부적으로 사용
	 *  
	 * https://datatables.net/manual/server-side 참조
	 * 
	 * @see BottledWaterDAO
	 * 
	 * @param model
	 * @param session
	 * @param request
	 * @param systemName 시스템이름(서버명)
	 * @param databaseName 데이터베이스명
	 * @param connectName  연결명
	 * @param draw 데이터테이블에서 내부적으로 쓰는 변수
	 * @param start 페이지 시작 위치
	 * @param length 한 페이지에서 보여줄 레코드 수
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
	 * 연계 테이블 등록을 위한 목록 조회 - 페이지 뷰만 반환
	 * @param model
	 * @param session
	 * @param request
	 * @param systemName
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/tableRegistrationList")   
	public ModelAndView tableRegistrationList(Model model, HttpSession session, HttpServletRequest request, 
			@RequestParam(value = "systemName", defaultValue = "") String systemName,
			@RequestParam(value = "databaseName", defaultValue = "") String databaseName,
			@RequestParam(value = "schemaName", defaultValue = "") String schemaName, 
			@RequestParam(value = "connectName", defaultValue = "") String connectName, 
			@RequestParam(value = "draw", defaultValue = "1") int draw,
			@RequestParam(value = "start", defaultValue = "1") int start,
			@RequestParam(value = "length", defaultValue = "1") int length) throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.addObject("systemName", systemName);
		mav.addObject("databaseName", databaseName);
		mav.addObject("schemaName", schemaName);
		mav.addObject("connectName", connectName);
		mav.setViewName("tableRegistrationList");
		return mav;
	}
		
	
	
	/**
	 * 연계 테이블 등록을 위한 목록 조회 - JSON 형태 데이터 반환
	 * 
	 * 파라미터 draw, start, length 는 데이터테이블에서 자동으로 넘오오는 변수
	 * 리턴 타입 객체 안에는 draw,recordsTotal, recordsFiltered 변수를 셋팅해서 넘겨주면 데이터테이블에서 내부적으로 사용
	 *  
	 * https://datatables.net/manual/server-side 참조
	 * 
	 * @see BottledWaterDAO
	 * 
	 * @param model
	 * @param session
	 * @param request
	 * @param systemName 시스템이름(서버명)
	 * @param databaseName 데이터베이스명
	 * @param connectName  연결명
	 * @param draw 데이터테이블에서 내부적으로 쓰는 변수
	 * @param start 페이지 시작 위치
	 * @param length 한 페이지에서 보여줄 레코드 수
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/tableRegistrationListData")   
	@ResponseBody
	public Map<String, Object> tableRegistrationListData(Model model, HttpSession session, HttpServletRequest request, 
			@RequestParam(value = "systemName", defaultValue = "") String systemName,
			@RequestParam(value = "databaseName", defaultValue = "") String databaseName,
			@RequestParam(value = "schemaName", defaultValue = "") String schemaName, 
			@RequestParam(value = "connectName", defaultValue = "") String connectName, 
			@RequestParam(value = "draw", defaultValue = "1") int draw,
			@RequestParam(value = "start", defaultValue = "1") int start,
			@RequestParam(value = "length", defaultValue = "1") int length) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();

		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("systemName", systemName);
		param.put("databaseName", databaseName);
		param.put("schemaName", schemaName);
		param.put("connectName", connectName);
		param.put("draw", draw);
		param.put("start", start);
		param.put("length", length);

		try{
			resultMap = bottledWaterService.selectTableRegistrationListData(param);
			
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
	
	/**
	 * 연계 테이블 등록 및 삭제 처리
	 * @param model
	 * @param session
	 * @param request
	 * @param mode
	 * @param userId
	 * @param menuId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/addTableProcess")
	@ResponseBody
	public HashMap<String, Object> addTableProcess(Model model, HttpSession session, HttpServletRequest request, 
			@RequestParam(value = "mode", defaultValue = "") String[] mode,
			@RequestParam(value = "systemName", defaultValue = "") String[] systemName,
			@RequestParam(value = "databaseName", defaultValue = "") String[] databaseName,
			@RequestParam(value = "schemaName", defaultValue = "") String[] schemaName,
			@RequestParam(value = "tableName", defaultValue = "") String[] tableName) throws Exception {	
		HashMap<String, Object> responseMap = new HashMap<String, Object>();
		try {

			int rowCount = 0;

			String[] modeArr = request.getParameterValues("mode");
			String[] systemNameArr = request.getParameterValues("systemName");
			String[] databaseNameArr = request.getParameterValues("databaseName");
			String[] schemaNameArr = request.getParameterValues("schemaName");
			String[] tableNameArr = request.getParameterValues("tableName");
		    
			if(modeArr != null) {
				List<HashMap<String, String>> paramList = new ArrayList<HashMap<String, String>>();
				for(int i=0;i<modeArr.length;i++) {
					HashMap<String, String> param = new HashMap<String, String>();
					param.put("mode", modeArr[i]);
					param.put("systemName", systemNameArr[i]);
					param.put("databaseName", databaseNameArr[i]);
					param.put("schemaName", schemaNameArr[i]);
					param.put("tableName", tableNameArr[i]);
					paramList.add(param);
				}
	
				rowCount = bottledWaterService.updateLinkedTableList(paramList);
				responseMap.put("result", "SUCCESS");
				responseMap.put("msg", "연계 테이블 등록이 완료되었습니다.");
			} else {
				throw new Exception("잘못된 요청입니다.");
			}
		} catch (Exception e) {
				Globals.logger.error(e.getMessage(), e);
				responseMap.put("result", "ERROR");
				responseMap.put("msg", e.getMessage());
		}
		return responseMap;
	}
	
	

}
