package com.k4m.eXperdb.webconsole.dashboard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.k4m.eXperdb.webconsole.common.Globals;

@Controller
public class DashboardController {
	
	@Autowired
	DashboardService dashboardService;
	
	@RequestMapping(value = "/dashboard")
	public ModelAndView securitysetting(Model model, HttpSession session, HttpServletRequest request) throws Exception {
		ModelAndView mav = new ModelAndView();
		//PropertieManager propertie = PropertieManager.getInstance();
		//String refresh = propertie.getValue(Globals.DASHBOARD_REFRESH_INTERVAL);
		String refresh = "10";
		mav.addObject("refreshTime", refresh);
		mav.setViewName("dashboard");
		return mav;
	}
	
	@RequestMapping(value = "/dashboardTargetDBMS")
	@ResponseBody
	public Map<String, Object> dashboardTargetDBMS(Model model, HttpSession session, HttpServletRequest request) throws Exception {
		Map<String, Object> response = new HashMap<String, Object>();
		int cnt = dashboardService.dashboardTargetDBMS();
		
		response.put("cnt", cnt);
		response.put("result", "SUCCESS");
		response.put("msg", "");
		return response;
	}
	
	@RequestMapping(value = "/dashboardCheckReservation")
	@ResponseBody
	public Map<String, Object> dashboardCheckReservation(Model model, HttpSession session, HttpServletRequest request) throws Exception {
		Map<String, Object> response = new HashMap<String, Object>();
		int cnt = dashboardService.dashboardCheckReservation();
		
		response.put("cnt", cnt);
		response.put("result", "SUCCESS");
		response.put("msg", "");
		return response;
	}
	
	@RequestMapping(value = "/dashboardRunning")
	@ResponseBody
	public Map<String, Object> dashboardRunning(Model model, HttpSession session, HttpServletRequest request) throws Exception {
		Map<String, Object> response = new HashMap<String, Object>();
		int cnt = dashboardService.dashboardRunning();
		
		response.put("cnt", cnt);
		response.put("result", "SUCCESS");
		response.put("msg", "");
		return response;
	}
	
	@RequestMapping(value = "/dashboardTargetList")
	@ResponseBody
	public Map<String, Object> dashboardTargetList(Model model, HttpSession session, HttpServletRequest request) throws Exception {
		request.getParameter("");
		Map<String, Object> response = new HashMap<String, Object>();
		List<Map<String, Object>> listTop = dashboardService.dashboardTargetList();
		
		response.put("list", listTop);
		response.put("result", "SUCCESS");
		response.put("msg", "");
		return response;
	}
	
	@RequestMapping(value = "/dashboardOrderedChart")
	@ResponseBody
	public Map<String, Object> dashboardOrderedChart(Model model, HttpSession session, HttpServletRequest request) throws Exception {
		request.getParameter("");
		Map<String, Object> response = new HashMap<String, Object>();
		List<Map<String, Object>> listTop = dashboardService.dashboardTargetList();
		List<Map<String, Object>> listTwo = dashboardService.dashboardTargetListTwo();
		
		response.put("list", listTop);
		response.put("listChart", listTwo);
		response.put("result", "SUCCESS");
		response.put("msg", "");
		return response;
	}
}
