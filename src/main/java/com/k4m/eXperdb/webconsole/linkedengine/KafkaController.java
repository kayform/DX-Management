package com.k4m.eXperdb.webconsole.linkedengine;

import java.net.InetSocketAddress;
import java.net.Socket;
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
import org.springframework.web.servlet.ModelAndView;

import com.k4m.eXperdb.webconsole.common.Globals;


@Controller
public class KafkaController {

	@Autowired
	private KafkaService kafkaService;

	
	/**
	 * 연계엔진관리 > kafka 서버 목록 조회
	 * @param model
	 * @param session
	 * @param request
	 * @param searchSystemName
	 * @param searchType
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/kafka")   
	public ModelAndView server(Model model, HttpSession session, HttpServletRequest request,
			@RequestParam(value = "searchSystemName", defaultValue = "") String searchSystemName,
			@RequestParam(value = "searchType", defaultValue = "") String searchType) throws Exception {

		List<Map<String, Object>> serverListTemp = null;		
		List<Map<String, Object>> serverList = new ArrayList<Map<String, Object>>();;		
		HashMap<String, String> param = new HashMap<String, String>();
	
		//TODO type을 배열로 사용하도록 변경 필요
		param.put("sys_nm", (searchSystemName == null || "".equals(searchSystemName)) ? "%" : "%" + searchSystemName + "%");
		param.put("type", (searchType == null || "".equals(searchType)) ? "%" : "%" + searchType + "%");
		
		int portScanResult = 0;
		
		try{
			serverListTemp = kafkaService.selectKafkaServerList(param);	
			for(Map<String, Object> serverInfo : serverListTemp){
				portScanResult = portScan((String)serverInfo.get("ip"),Integer.parseInt((String) serverInfo.get("port")));
				serverInfo.put("status", portScanResult);
				serverList.add(serverInfo);
			}
			
			
		}catch (Exception e){
			Globals.logger.error(e.getMessage(), e);
		}
		ModelAndView mav = new ModelAndView();
		mav.addObject("serverList", serverList);
		mav.addObject("searchSystemName", searchSystemName);
		mav.addObject("searchType", searchType);

		mav.setViewName("kafka");
		
		
		return mav;
	}
	
	/**
	 * 파라미터로 전달 받은 아이피와 포트에 대해 포트가 열려 있는지 closed이면 0, open이면 1을 반환
	 * 타임아웃 시간은 1000밀리세컨즈로 되어 있음
	 * @param ip
	 * @param port
	 * @return
	 */
	private int portScan(String ip, int port) {

		int timeOut = 1000;
		int portScanResult = 0;
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(ip, port), timeOut);
            portScanResult = 1;
            socket.close();
        } catch (Exception ex) {
        	portScanResult = 0;
        	Globals.logger.debug(ex.getMessage());
        }
        
        Globals.logger.debug("아이피:"+ip + ", 포트:" + port +"는 "+ portScanResult +"상태입니다.(0:closed, 1:opened)");
        return portScanResult;
	}

	
}
