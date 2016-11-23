package com.k4m.eXperdb.webconsole.dbmsman;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.k4m.eXperdb.webconsole.common.CommonService;
import com.k4m.eXperdb.webconsole.common.DataHistoryService;
import com.k4m.eXperdb.webconsole.common.Globals;
import com.k4m.eXperdb.webconsole.db.DBCPPoolManager;
import com.k4m.eXperdb.webconsole.settings.SettingsService;

@Controller
public class PgmonController {
	@Autowired 
	private PooledDataSource dataSource;
	@Autowired
	private SettingsService settingsService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private CommonService pgmonService;
	
	private Map<String, Object> getServerList(String sys_nm, String ip, int draw, int start, int length) {
		List<Map<String, Object>> serverList = new ArrayList<Map<String, Object>>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try{
			
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("sys_nm", (sys_nm == null || sys_nm.equals("")) ? "%" : "%" + sys_nm + "%");
			param.put("type", "POSTGRESQL");			
			param.put("ip", (ip == null || ip.equals("")) ? "%" : "%" + ip + "%");
			param.put("start", start);
			param.put("end", start+length);
			
			int totalCount = settingsService.selectSERVERTotalCount(param); // 데이터 전체 건수 조회
			serverList = settingsService.selectSERVER(param); // 데이터 리스트 조회
			
			for(Map<String, Object> map : serverList) {
				String sys_nm_map = (String) map.get("sys_nm");
				Connection conn = null;
				
				try{
					if (DBCPPoolManager.ContaintPool(sys_nm_map)){
						conn = DBCPPoolManager.getConnection(sys_nm_map);
					
						if (!conn.isClosed()) {
							map.put("status", "Running");
						}else{
							map.put("status", "Stopped");
						}
					}else{
						map.put("status", "DB Password is invalid.");
					}								
				}catch(Exception e){
					Globals.logger.error(e.getMessage(), e);
				}finally{
					if (conn != null) {
						conn.close();
					}
				}				
			}
			
			resultMap.put("draw", draw);
			//resultMap.put("recordsTotal", totalCount);
			//resultMap.put("recordsFiltered", totalCount);
			resultMap.put("iTotalRecords", totalCount);
			resultMap.put("iTotalDisplayRecords", totalCount);
			resultMap.put("aaData", serverList);
			resultMap.put("iDisplayLength", length);
		}catch(Exception e){
			Globals.logger.error(e.getMessage(), e);
		}
		return resultMap;
	}
	
	@RequestMapping(value = "/pgmonitoring")
	public ModelAndView dbms(Model model, HttpSession session, HttpServletRequest request, @RequestParam(value = "searchSysNm", defaultValue = "") String sys_nm,
			@RequestParam(value = "searchIp", defaultValue = "") String ip, 
			@RequestParam(value = "draw", defaultValue = "1") int draw,
			@RequestParam(value = "start", defaultValue = "1") int start,
			@RequestParam(value = "length", defaultValue = "1") int length
			) throws Exception {
		ModelAndView mav = new ModelAndView();
		try {		
			//mav.addObject("serverList", getServerList(sys_nm, ip, draw, start, length));
			mav.setViewName("pgmonitoring");
		} catch (Exception e) {
			Globals.logger.error(e.getMessage(), e);
			throw e;
		}
		return mav;
	}
	
	@RequestMapping(value = "/pgmonList")
	@ResponseBody
	public Map<String, Object> getServerList(Model model, HttpSession session, HttpServletRequest request, @RequestParam(value = "searchSysNm", defaultValue = "") String sys_nm,
			@RequestParam(value = "searchIp", defaultValue = "") String ip, 
			@RequestParam(value = "draw", defaultValue = "1") int draw,
			@RequestParam(value = "start", defaultValue = "1") int start,
			@RequestParam(value = "length", defaultValue = "1") int length) throws Exception {
			
		return getServerList(sys_nm, ip, draw, start, length); // 데이터 리스트 조회
	}
}
