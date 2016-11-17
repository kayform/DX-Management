package com.k4m.eXperdb.webconsole.linkedengine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.servlet.ModelAndView;

import com.k4m.eXperdb.webconsole.common.Globals;


@Controller
public class KafkaConnectController {

	@Autowired
	private KafkaConnectService kafkaConnectService;

	
	
	
	/**
	 * 연계엔진관리 > kafka connect 서버 목록 조회
	 * @param model
	 * @param session
	 * @param request
	 * @param searchSystemName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/kafkaconnect")   
	public ModelAndView server(Model model, HttpSession session, HttpServletRequest request,
			@RequestParam(value = "searchSystemName", defaultValue = "") String searchSystemName) throws Exception {

		List<Map<String, Object>> serverListTemp = null;		
		List<Map<String, Object>> serverList = new ArrayList<Map<String, Object>>();;		
		HashMap<String, String> param = new HashMap<String, String>();

		param.put("sys_nm", (searchSystemName == null || "".equals(searchSystemName)) ? "%" : "%" + searchSystemName + "%");
		
		//해당 아이피,포트의 커넥터 갯수
		int countConnector = 0;
		
		try {
			serverListTemp = kafkaConnectService.selectKafkaConnectServerList(param);	
			for(Map<String, Object> serverInfo : serverListTemp){
				
				//데이터베이스에서 조회된 값으로 REST URL을 만들고, 해당 URL로 조회 요청을 하고 결과값을 반환 받는다.
				countConnector = countConnectors((String)serverInfo.get("ip"),Integer.parseInt((String) serverInfo.get("port")));
				serverInfo.put("count_connect", countConnector);
				serverList.add(serverInfo);
			}
			
		} catch (Exception e) {
			throw e;
		} 
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("serverList", serverList);
		mav.addObject("searchSystemName", searchSystemName);
		mav.setViewName("kafkaconnect");
		
		return mav;
	}

	/**
	 * 연계엔진관리 > kafka connect 서버의 커넥터 목록 조회
	 * @param model
	 * @param session
	 * @param request
	 * @param ip
	 * @param port
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/kafkaConnectorConfigListForm")   
	public ModelAndView kafkaConnectorConfigList(Model model, HttpSession session, HttpServletRequest request,
			@RequestParam(value = "ip", defaultValue = "") String ip,
			@RequestParam(value = "port", defaultValue = "") String port) throws Exception {

		List<Map<String, Object>> configList = new ArrayList<Map<String, Object>>();		

		ResponseEntity<String> responseEntity = null;
		RequestRestAPI requestRestAPI  = new RequestRestAPI();

		String configJob = "config";
		String statusJob = "status";
		int portInt = Integer.parseInt(port);

		String url = null;
		try {
			
			
			url = requestRestAPI.makeRestURL(ip, portInt, "", "");
			Globals.logger.debug(url+" REST API 호출");

			//데이터베이스에서 조회된 값으로 REST URL을 만들고, 해당 URL로 조회 요청을 하고 커넥터 목록을 반환받는다.
			responseEntity = requestRestAPI.requestRestAPI(url);
			String jsonString = responseEntity.getBody();
			
			if(responseEntity!= null && responseEntity.getStatusCode().value() == 200 ){
				Map<String, Object> connectorConfig = null;
				Map<String, Object> connectorStatus = null;

				JSONParser jsonParser = new JSONParser();
				JSONArray objects =(JSONArray) jsonParser.parse(jsonString);
				
				//반환받은 커넥터명에 해당하는 config 및 status 정보를 조회하고 리스트에 담는다.
				for(int i = 0 ; i < objects.size() ; i++){
					connectorConfig = new HashMap<String, Object>();
					connectorStatus = new HashMap<String, Object>();

					//서버 해당 커넥터의 설정 정보 조회
					connectorConfig = getKafkaConnectorConfigorStatus(ip, portInt, objects.get(i).toString(), configJob);
					//서버 해당 커넥터의 상태 정보 조회
					connectorStatus = getKafkaConnectorConfigorStatus(ip, portInt, objects.get(i).toString(), statusJob);
					
					//서버 해당 커넥터의 상태 정보를 페이지 정보 조회를 위해 설정정보 맵에 추가
					connectorConfig.put("status", ((JSONObject)connectorStatus.get("connector")).get("state"));
					
					configList.add(connectorConfig);
				}
			}
			else{
				//응답 코드가 200이 아닌 경우 에러 처리
				Globals.logger.error(url+" REST API 접속에서 예외가 발생했습니다.");
				throw new Exception(url+" REST API 접속에서 예외가 발생했습니다. 응답 코드가 적절하지 않습니다.");
			}			
			
		} catch (Exception e) {
			throw e;
		} 
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("configList", configList);
		mav.setViewName("kafkaConnectorConfigListForm");
		
		return mav;
	}

	/**
	 * 연계엔진관리 > kafka connect 서버의 connector config  조회
	 * @param model
	 * @param session
	 * @param request
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/kafkaConnectorConfigForm")   
	public ModelAndView kafkaConnectorConfigList(Model model, HttpSession session, HttpServletRequest request,
			@RequestParam Map<String, String> paramMap ) throws Exception {
		
		Iterator<?> iter=paramMap.entrySet().iterator();
		while(iter.hasNext()){
			Map.Entry entry=(Map.Entry)iter.next();
			System.out.println(" 커넥터 설정 정보 키:"+String.valueOf(entry.getKey())+", 값:"+ String.valueOf(entry.getValue()));
		}
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("kafkaConnectorConfig", paramMap);
		mav.setViewName("kafkaConnectorConfigForm");
		
		return mav;
	}


	/**
	 * 입력받은 아이피, 포트, 커넥터명, 잡에 대한 REST 응답을 맵(JSONObject 타입)으로 반환
	 * @param ip
	 * @param port
	 * @param connectorName
	 * @param job
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> getKafkaConnectorConfigorStatus(String ip, int port, String connectorName, String job) throws Exception {
		
		JSONObject configJsonObjectMap = null;
		String jsonString = null;
		String url = null;
		
		ResponseEntity<String> responseEntity = null;
		RequestRestAPI requestRestAPI  = new RequestRestAPI();
		
		try {
			url = requestRestAPI.makeRestURL(ip, port, connectorName, job);
			Globals.logger.debug(url+" REST API 호출");

			//데이터베이스에서 조회된 값으로 REST URL을 만들고, 해당 URL로 조회 요청을 하고 결과값을 반환 받는다.
			responseEntity = requestRestAPI.requestRestAPI(url);
			jsonString = responseEntity.getBody();
			Globals.logger.debug("호출결과 =="+jsonString);
			
			if(responseEntity!= null && responseEntity.getStatusCode().value() == 200 ){

				JSONParser jsonParser = new JSONParser();

				configJsonObjectMap =(JSONObject) jsonParser.parse(jsonString);

				//Globals.logger.debug(configJsonObjectMap.getClass().getName());
				
				Iterator<?> iter=configJsonObjectMap.entrySet().iterator();
				while(iter.hasNext()){
					Map.Entry entry=(Map.Entry)iter.next();
					Globals.logger.debug(connectorName+" 커넥터 설정 정보 키:"+String.valueOf(entry.getKey())+", 값:"+ String.valueOf(entry.getValue()));
				}
			}
			else{
				//응답 코드가 200이 아닌 경우 에러 처리
				Globals.logger.error(url+" REST API 접속에서 예외가 발생했습니다.");
				throw new Exception(url+" REST API 접속에서 예외가 발생했습니다. 응답 코드가 적절하지 않습니다.");
			}

			
		} catch (HttpClientErrorException e) {
			Globals.logger.info(url+" REST API 접속에서 예외가 발생했습니다.");
			Globals.logger.info(e);
			throw e;
		}catch (ResourceAccessException e) {
			Globals.logger.info(url+" REST API 접속에서 예외가 발생했습니다.");
			Globals.logger.info(e);
			throw e;
		} catch (Exception e) {
			throw e;
		}
		
		return configJsonObjectMap;
	}
	
	
	/**
	 * 입입력받은 아이피, 포트에 등록된 커넥터 갯수 반환
	 * @param ip
	 * @param port
	 * @return
	 * @throws Exception
	 */
	private int countConnectors(String ip, int port) throws Exception {

		int intReturn = 0;
		String jsonString = null;
		String url = null;

		ResponseEntity<String> responseEntity = null;
		RequestRestAPI requestRestAPI  = new RequestRestAPI();
		
		try {
			url = requestRestAPI.makeRestURL(ip, port, "", "");
			Globals.logger.debug(url+" REST API 호출");

			//데이터베이스에서 조회된 값으로 REST URL을 만들고, 해당 URL로 조회 요청을 하고 결과값을 반환 받는다.
			responseEntity =requestRestAPI.requestRestAPI(url);
			jsonString = responseEntity.getBody();
			
			
			if(responseEntity!= null && responseEntity.getStatusCode().value() == 200 ){
				JSONParser jsonParser = new JSONParser();

				JSONArray objects =(JSONArray) jsonParser.parse(jsonString);
				intReturn= objects.size();
				
				for(int i = 0 ; i < objects.size() ; i++){
					Globals.logger.debug(url+" 의 "+i+"번째 커넥터명 : "+objects.get(i).toString());
				}
			}
			else{
				intReturn = 0;
			}

			
		} catch (IllegalArgumentException e) {
			//접속 예외가 발생하면 커넥터의 갯수가 0으로 반환. 예를 들어 포트 번호가 111111 등으로 범위를 벗어나면 예외 발생
			intReturn = 0;
			Globals.logger.info(url+" REST API 접속에서 예외가 발생했습니다.");
			Globals.logger.info(e);
		} catch (HttpClientErrorException e) {
			//접속 예외가 발생하면 커넥터의 갯수가 0으로 반환
			intReturn = 0;
			Globals.logger.info(url+" REST API 접속에서 예외가 발생했습니다.");
			Globals.logger.info(e);
		}catch (ResourceAccessException e) {
			//접속 예외가 발생하면 커넥터의 갯수가 0으로 반환
			intReturn = 0;
			Globals.logger.info(url+" REST API 접속에서 예외가 발생했습니다.");
			Globals.logger.info(e);
		} catch (Exception e) {
			//TODO 정상적인 예외 조사 및 추가 필요 maybe... remarked by kth
			throw e;
		}
		return intReturn;
	}
	

	
	
}
