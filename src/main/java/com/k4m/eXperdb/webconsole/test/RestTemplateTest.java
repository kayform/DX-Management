package com.k4m.eXperdb.webconsole.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.k4m.eXperdb.webconsole.common.Globals;

public class RestTemplateTest {

	@Test
	public void test() {

		//커넥트 명 반환
		//String url = makeRestURL("58.229.253.142", 8083, "", "");
		//String[] results = null;

		//해당 커넥터의 상태값 반환
		//String url = makeRestURL("58.229.253.142", 8083, "work1", "status");
		
		//해당 커넥터의 설정 값 반환
		String url = makeRestURL("58.229.253.142", 8083, "work1", "config");

		String results = null;		
		
		try {
			results = requestRestAPI(url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	
			
	}
	
	private String requestRestAPI(String url) throws Exception {

		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);
		
		RestTemplate restTemplate = new RestTemplate();
		
		// Add the String message converter
		restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter() );
		String results = null;
		
		try {
			ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class );
			results = responseEntity.getBody();
		} catch (Exception e) {
			System.out.println(url+" REST API 접속에서 예외가 발생했습니다.");
			throw new Exception("REST API 접속에서 예외가 발생했습니다.", e);
		}
		System.out.println(url+" REST API 호출 결과 = "+ results.toString());
		return results;

	}
	
	private String makeRestURL(String ip, int port, String connectorName, String job){
		String returnURL = null;

		
		if(connectorName != null && connectorName.length() > 0 && job != null && job.length() > 0){
			returnURL =  "http://"+ip+":"+port+"/connectors/"+connectorName+"/"+job;
		}
		else {
			returnURL =  "http://"+ip+":"+port+"/connectors";	
		}
		
		System.out.println("REST API URL = "+returnURL);
		return returnURL;
	}

	
}
