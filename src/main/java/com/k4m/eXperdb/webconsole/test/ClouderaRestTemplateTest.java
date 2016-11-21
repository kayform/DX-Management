package com.k4m.eXperdb.webconsole.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.apache.commons.codec.binary.Base64;

import com.k4m.eXperdb.webconsole.common.Globals;
import com.k4m.eXperdb.webconsole.linkedengine.RequestRestAPI;

public class ClouderaRestTemplateTest {


	
	@Test
	public void clouderaServiceSummaryTest() {
		
		String ip = "58.229.253.137";
		String port = "7180";
		String username = "admin";
//		String password = "admin";
		String password = null;
		String encodeedPassword = "86IIlz0+DRw=";

		//서버 해당 커넥터의 상태 정보 조회
		try {
			Map<String, String> clouderaServiceSummary = clouderaServiceSummary(ip, port, username, password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	//@Test
	public void clouderaServiceListTest() {
		
		String ip = "58.229.253.137";
		String port = "7180";
		String username = "admin";
		String password = "admin";

		//서버 해당 커넥터의 상태 정보 조회
		try {
			List<Map<String, Object>> clouderaJsonObjectList = getClouderaServicesList(ip, port, username, password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 입력받은 아이피, 포트, 사용자명, 패스워드에 대한 REST 응답을 리스트(JSONArray 타입)으로 반환
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
		
		try {
			List<Map<String, Object>> clouderaJsonObjectList = getClouderaServicesList(ip, port, username, password);
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
			System.out.println("조회된 서비스 항목 = "+services);
			clouderaServiceSummary.put("services", services);
			clouderaServiceSummary.put("servicesCount", Integer.toString(serviceCount));
		} catch (Exception e) {
			throw e;
		}
		
		return clouderaServiceSummary ;
	}
	
	
	
	
	/**
	 * 입력받은 아이피, 포트, 사용자명, 패스워드에 대한 REST 응답을 리스트(JSONArray 타입)으로 반환
	 * @param ip
	 * @param port
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 */
	private  List<Map<String, Object>>  getClouderaServicesList(String ip, String port, String username, String password) throws Exception {
		
		JSONObject jsonObjectMap = null;
		JSONArray clouderaJsonObjectArray = null;

		String url = "http://"+ip+":"+port+"/api/v9/clusters/cluster/services";
		String authString = username + ":" + password;
		
		System.out.println("Auth string: " + authString);
		String authStringEnc = org.apache.commons.codec.binary.Base64.encodeBase64String(authString.getBytes());
		System.out.println("Base64 encoded auth string: " + authStringEnc);		
		

		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
		requestHeaders.add("Authorization", "Basic " + authStringEnc);
	       
	       
		RestTemplate restTemplate = new RestTemplate();
	       
		HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);
		ResponseEntity<String> responseEntity = null;
	       
	       
		// Add the String message converter
		restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter() );
		
		try {
			responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class );
			String jsonString = responseEntity.getBody();
			System.out.println(url+ "  =========== 호출결과 시작 ===============" );
			System.out.println(jsonString);
			System.out.println(url+ "  =========== 호출결과 끝   ===============" );
			
			if(responseEntity!= null && responseEntity.getStatusCode().value() == 200 ){

				JSONParser jsonParser = new JSONParser();

				jsonObjectMap =(JSONObject) jsonParser.parse(jsonString);
				clouderaJsonObjectArray = (JSONArray) jsonObjectMap.get("items");
				System.out.println(url+ " 등록 서비스 갯수 = "+clouderaJsonObjectArray.size());				
			}
			else{
				//응답 코드가 200이 아닌 경우 에러 처리
				Globals.logger.info(url+" REST API 접속에서 예외가 발생했습니다. 에러코드 : "+responseEntity.getStatusCode().value());
				throw new Exception(url+" REST API 접속에서 예외가 발생했습니다. 응답 코드가 적절하지 않습니다." );
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
		
		return clouderaJsonObjectArray;
	}
	
}
