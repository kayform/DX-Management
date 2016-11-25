package com.k4m.eXperdb.webconsole.linkedengine;

import java.net.ConnectException;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.k4m.eXperdb.webconsole.common.Globals;
import com.k4m.eXperdb.webconsole.util.SecureManager;


@Controller
public class ClouderaController {

	@Autowired
	private ClouderaService clouderaService;

	
	
	
	/**
	 * 연계엔진관리 > Cloudera 서버 목록 조회 
	 * @param model
	 * @param session
	 * @param request
	 * @param searchSystemName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/cloudera")   
	public ModelAndView server(Model model, HttpSession session, HttpServletRequest request,
			@RequestParam(value = "searchSystemName", defaultValue = "") String searchSystemName) throws Exception {

		HashMap<String, String> param = new HashMap<String, String>();
		param.put("sys_nm", (searchSystemName == null || "".equals(searchSystemName)) ? "%" : "%" + searchSystemName + "%");

		List<Map<String, Object>> serverListTemp = null;		
		List<Map<String, Object>> serverList = new ArrayList<Map<String, Object>>();;		
		Map<String, String> clouderaServiceSummary  = null;

		
		try {
			//검색 조회 조건서버 이름에 해당하는 서버 리스트 조회
			serverListTemp = clouderaService.selectClouderaServerList(param);	
			
			for(Map<String, Object> serverInfo : serverListTemp){
				//데이터베이스에서 조회된 값으로 REST URL을 만들고, 해당 URL로 서비스 요약정보 조회 요청을 하고 결과값을 반환 받는다.
				clouderaServiceSummary = clouderaServiceSummary((String)serverInfo.get("ip"),(String) serverInfo.get("port"), (String)serverInfo.get("user_id"),SecureManager.decrypt(((String)serverInfo.get("user_pw")))); 
				serverInfo.put("services", clouderaServiceSummary.get("services"));
				serverInfo.put("servicesCount", clouderaServiceSummary.get("servicesCount"));		
				serverList.add(serverInfo);
			}
			Globals.logger.debug("조회된 클라우데라 서버 리스트 갯수 ="+serverList.size());
		} catch (Exception e) {
			throw e;
		} 
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("serverList", serverList);
		mav.addObject("searchSystemName", searchSystemName);
		mav.setViewName("cloudera");
		
		return mav;
	}

	/**
	 * 연계엔진관리 > cloudera 서비스 목록 조회
	 * @param model
	 * @param session
	 * @param request
	 * @param ip
	 * @param port
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/clouderaServicesListForm")   
	public ModelAndView kafkaConnectorConfigList(Model model, HttpSession session, HttpServletRequest request,
			@RequestParam(value = "searchSystemName", defaultValue = "") String searchSystemName) throws Exception {

		HashMap<String, String> param = new HashMap<String, String>();
		param.put("sys_nm", (searchSystemName == null || "".equals(searchSystemName)) ? "" :  searchSystemName );

		Map<String, Object> serverTemp = null;		
		List<Map<String, Object>> servicesList = new ArrayList<Map<String, Object>>();;		
		Map<String, String> clouderaServiceSummary  = null;

		
		try {
			serverTemp = clouderaService.selectClouderaServer(param);	

				
			//데이터베이스에서 조회된 값으로 REST URL을 만들고, 해당 URL로 조회 요청을 하고 결과값을 반환 받는다.
			servicesList = getClouderaServicesList((String)serverTemp.get("ip"),(String) serverTemp.get("port"), (String)serverTemp.get("user_id"), SecureManager.decrypt(((String)serverTemp.get("user_pw"))));
			
			
		} catch (Exception e) {
			throw e;
		} 
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("servicesList", servicesList);
		mav.setViewName("clouderaServicesListForm");
		
		return mav;
	}





	
	/**
	 * 입력받은 아이피, 포트, 사용자명, 패스워드에 대한 REST 응답을 리스트(JSONArray 타입)으로 반환
	 * getClouderaServicesList() 메소드를 호출하여 서비스리스틀 반환받고 화면 표시에 필요한 데이터 추출
	 * 
	 * @param ip
	 * @param port
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 */
	private  Map<String, String>  clouderaServiceSummary(String ip, String port, String username, String password) throws Exception {
		Map<String, String> clouderaServiceSummary =  new HashMap<String, String>();
		String services = "";
		int serviceCount = 0;
		List<Map<String, Object>> clouderaJsonObjectList = new ArrayList<Map<String, Object>>();
		
		try {
			clouderaJsonObjectList = getClouderaServicesList(ip, port, username, password);
			if(clouderaJsonObjectList != null && clouderaJsonObjectList.size()>0){
				serviceCount = clouderaJsonObjectList.size();
				
				StringBuffer sb = new StringBuffer();
				boolean isFirst = true;
				for (Map<String, Object> tempMap : clouderaJsonObjectList) {
					if(isFirst){
						sb.append(tempMap.get("type"));
						isFirst = false;
					}
					else {
						sb.append(", "+tempMap.get("type"));
					}
				}
				services = sb.toString();
				Globals.logger.debug("조회된 서비스 항목 = "+services);
				clouderaServiceSummary.put("services", services);
				clouderaServiceSummary.put("servicesCount", Integer.toString(serviceCount));
			}
			else{
				new Exception("클라우데라 서비스가 없습니다.");
			}
		} catch (HttpClientErrorException e) {
			Globals.logger.info(" REST API 접속에서 예외가 발생했습니다.");
			Globals.logger.info(e);
		}catch (ResourceAccessException e) {
			Globals.logger.info(" REST API 접속에서 예외가 발생했습니다.");
			Globals.logger.info(e);
		} catch (ConnectException e) {
			Globals.logger.info(" REST API 접속에서 예외가 발생했습니다.");
			Globals.logger.info(e);
		} catch (Exception e) {
			Globals.logger.error(e);
			throw e;
		}
		
		return clouderaServiceSummary ;
	}
	
	
	
	
	/**
	 * 입력받은 아이피, 포트, 사용자명, 패스워드에 대한 서비스리스트를 REST 응답(JSONArray 타입)으로 반환
	 * 
	 * 전체문서 : http://www.cloudera.com/documentation/enterprise/5-3-x/topics/cm_intro_api.html
	 * REST API 리턴타입 형태 : http://cloudera.github.io/cm_api/apidocs/v9/tutorial.html?_ga=1.224500954.1373049084.1480049343
	 * 서비스 리스트 문서 : http://cloudera.github.io/cm_api/apidocs/v9/path__clusters_-clusterName-_services.html
	 * 
	 * @param ip
	 * @param port
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 */
	private  List<Map<String, Object>>  getClouderaServicesList(String ip, String port, String username, String password) throws Exception {
		JSONArray clouderaJsonObjectArray = null;
		JSONObject jsonObjectMap = null;

		String url = "http://"+ip+":"+port+"/api/v9/clusters/cluster/services";
		String authString = username + ":" + password;
		//Globals.logger.debug("Auth string: " + authString);
		String authStringEnc = org.apache.commons.codec.binary.Base64.encodeBase64String(authString.getBytes());
		//Globals.logger.debug("Base64 encoded auth string: " + authStringEnc);		

		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
		requestHeaders.add("Authorization", "Basic " + authStringEnc);
	       
		RestTemplate restTemplate = new RestTemplate();
	       
		HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);
		ResponseEntity<String> responseEntity = null;
	       
	       
		// Add the String message converter
		restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter() );
		
		try {
			//해당 URL에 접속
			responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class );
			String jsonString = responseEntity.getBody();
			Globals.logger.debug(url+ "  =========== 호출결과 시작 ===============" );
			//Globals.logger.debug(jsonString);
			Globals.logger.debug(url+ "  =========== 호출결과 끝   ===============" );
			
			if(responseEntity!= null && responseEntity.getStatusCode().value() == 200 ){
				JSONParser jsonParser = new JSONParser();
				jsonObjectMap =(JSONObject) jsonParser.parse(jsonString);
				clouderaJsonObjectArray = (JSONArray) jsonObjectMap.get("items");
				Globals.logger.debug(url+ " 등록 서비스 갯수 = "+clouderaJsonObjectArray.size());				
			}
			else{
				//응답 코드가 200이 아닌 경우 에러 처리
				Globals.logger.debug(url+" REST API 접속에서 예외가 발생했습니다. 에러코드 : "+responseEntity.getStatusCode().value());
				throw new Exception(url+" REST API 접속에서 예외가 발생했습니다. 응답 코드가 적절하지 않습니다." );
			}
		} catch (HttpClientErrorException e) {
			Globals.logger.info(url+" REST API 접속에서 예외가 발생했습니다.");
			Globals.logger.info(e);
		}catch (ResourceAccessException e) {
			Globals.logger.info(url+" REST API 접속에서 예외가 발생했습니다.");
			Globals.logger.info(e);
		} catch (ConnectException e) {
			Globals.logger.info(url+" REST API 접속에서 예외가 발생했습니다.");
			Globals.logger.info(e);
		} catch (Exception e) {
			Globals.logger.error(e);
			throw e;
		}
		
		return clouderaJsonObjectArray;
	}
	
	
	
}
