package com.k4m.eXperdb.webconsole.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import com.k4m.eXperdb.webconsole.linkedengine.RequestRestAPI;

public class RestTemplateTest {

	@Test
	public void kafkaConnectorConfigListTest() {

		
		List<Map<String, Object>> configList = new ArrayList<Map<String, Object>>();		
		Map<String, Object> connectorConfig = null;
		Map<String, Object> connectorStatus = null;

		ResponseEntity<String> responseEntity = null;
		RequestRestAPI requestRestAPI  = new RequestRestAPI();

		String ip  ="58.229.253.142";
		int port = 8083;
		String configJob = "config";
		String statusJob = "status";

		String url = null;
		try {
			
			
			url = requestRestAPI.makeRestURL(ip, port, "", "");
			System.out.println(url+" REST API 호출");

			//데이터베이스에서 조회된 값으로 REST URL을 만들고, 해당 URL로 조회 요청을 하고 커넥터 목록을 반환받는다.
			responseEntity = requestRestAPI.requestRestAPI(url);
			String jsonString = responseEntity.getBody();
			
			if(responseEntity!= null && responseEntity.getStatusCode().value() == 200 ){
				JSONParser jsonParser = new JSONParser();
				JSONArray objects =(JSONArray) jsonParser.parse(jsonString);
				
				for(int i = 0 ; i < objects.size() ; i++){
					connectorConfig = new HashMap<String, Object>();
					connectorStatus = new HashMap<String, Object>();
					System.out.println(url+" 의 "+i+"번째 커넥터명 : "+objects.get(i).toString());
					connectorConfig = getKafkaConnectorConfig(ip, port, objects.get(i).toString(), configJob);
					connectorStatus = getKafkaConnectorConfig(ip, port, objects.get(i).toString(), statusJob);
					
					System.out.println("=========="+((JSONObject)connectorStatus.get("connector")).get("state"));
					
					connectorConfig.put("status", ((JSONObject)connectorStatus.get("connector")).get("state"));
					
					configList.add(connectorConfig);
				}
			}
			else{
			}			

			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	
			
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
	private Map<String, Object> getKafkaConnectorConfig(String ip, int port, String connectorName, String job) throws Exception {
		
		JSONObject configJsonObjectMap = null;
		String jsonString = null;
		String url = null;
		
		ResponseEntity<String> responseEntity = null;
		RequestRestAPI requestRestAPI  = new RequestRestAPI();
		
		try {
			url = requestRestAPI.makeRestURL(ip, port, connectorName, job);
			System.out.println(url+" REST API 호출");

			//데이터베이스에서 조회된 값으로 REST URL을 만들고, 해당 URL로 조회 요청을 하고 결과값을 반환 받는다.
			responseEntity = requestRestAPI.requestRestAPI(url);
			jsonString = responseEntity.getBody();
			System.out.println("호출결과 =="+jsonString);
			
			if(responseEntity!= null && responseEntity.getStatusCode().value() == 200 ){

				JSONParser jsonParser = new JSONParser();

				configJsonObjectMap =(JSONObject) jsonParser.parse(jsonString);

				//System.out.println(configJsonObjectMap.getClass().getName());
				
				Iterator<?> iter=configJsonObjectMap.entrySet().iterator();
				while(iter.hasNext()){
					Map.Entry entry=(Map.Entry)iter.next();
					System.out.println(connectorName+" 커넥터 설정 정보 키:"+String.valueOf(entry.getKey())+", 값:"+ String.valueOf(entry.getValue()));
				}
			}
			else{
				//응답 코드가 200이 아닌 경우 에러 처리
				System.out.println(url+" REST API 접속에서 예외가 발생했습니다.");
				throw new Exception(url+" REST API 접속에서 예외가 발생했습니다. 응답 코드가 적절하지 않습니다.");
			}

			
		} catch (HttpClientErrorException e) {
			System.out.println(url+" REST API 접속에서 예외가 발생했습니다.");
			System.out.println(e);
			throw e;
		}catch (ResourceAccessException e) {
			System.out.println(url+" REST API 접속에서 예외가 발생했습니다.");
			System.out.println(e);
			throw e;
		} catch (Exception e) {
			throw e;
		}
		
		return configJsonObjectMap;
	}
	
	
	
	
	/**
	 * 입력받은 아이피, 포트에 등록된 커넥터 갯수 반환
	 * @param ip
	 * @param port
	 * @param connectorName
	 * @param job
	 * @return
	 * @throws Exception
	 */
	private int countConnector(String ip, int port) throws Exception {

		int intReturn = 0;
		String jsonString = null;
		String url = null;

		ResponseEntity<String> responseEntity = null;
		RequestRestAPI requestRestAPI  = new RequestRestAPI();
		
		try {
			url = requestRestAPI.makeRestURL(ip, port, "", "");
			System.out.println(url+" REST API 호출");

			//데이터베이스에서 조회된 값으로 REST URL을 만들고, 해당 URL로 조회 요청을 하고 결과값을 반환 받는다.
			responseEntity = requestRestAPI.requestRestAPI(url);
			jsonString = responseEntity.getBody();
			
			
			if(responseEntity!= null && responseEntity.getStatusCode().value() == 200 ){
				JSONParser jsonParser = new JSONParser();

				JSONArray objects =(JSONArray) jsonParser.parse(jsonString);
				intReturn= objects.size();
				
				for(int i = 0 ; i < objects.size() ; i++){
					System.out.println(url+" 의 "+i+"번째 커넥터명 : "+objects.get(i).toString());
				}
			}
			else{
				intReturn = 0;
			}

			
		} catch (HttpClientErrorException e) {
			//접속 예외가 발생하면 커넥터의 갯수가 0으로 반환
			intReturn = 0;
			System.out.println(url+" REST API 접속에서 예외가 발생했습니다.");
			System.out.println(e);
		}catch (ResourceAccessException e) {
			//접속 예외가 발생하면 커넥터의 갯수가 0으로 반환
			intReturn = 0;
			System.out.println(url+" REST API 접속에서 예외가 발생했습니다.");
			System.out.println(e);
		} catch (Exception e) {
			throw e;
		}
		return intReturn;
	}

	

	
}
