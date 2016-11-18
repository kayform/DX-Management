package com.k4m.eXperdb.webconsole.test;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bouncycastle.util.encoders.Base64;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.k4m.eXperdb.webconsole.common.Globals;
import com.k4m.eXperdb.webconsole.linkedengine.RequestRestAPI;

public class ClouderaRestTemplateTest {

	
	
	/*
	 * 
HttpClientErrorException: 401 Full authentication is required to access this resource







	 * 
	 * 
	 */
	
	


	
	
	
	
	@Test
	public void clouderaServiceListTestsss() {

		
		List<Map<String, Object>> serviceList = new ArrayList<Map<String, Object>>();		
		Map<String, Object> serviceInfo = null;

		ResponseEntity<String> responseEntity = null;
		RequestRestAPI requestRestAPI  = new RequestRestAPI();

		String ip  ="58.229.253.137";
		int port = 7180;
		String url = "http://58.229.253.137:7180/api/v9/clusters/cluster/services";
		
		
		UriComponents uriComponents =
		        UriComponentsBuilder.newInstance()
		            .scheme("http").host(ip).port(port).path("/api/v9/clusters/cluster/services").build()
		            .encode();

		URI uri = uriComponents.toUri();
		System.out.println("=========="+uri.toString());
		

            


          //  headers = new HttpEntity<>(data, headers);        
		
		
		
		try {
			System.out.println(url.toString()+" REST API 호출");

			//데이터베이스에서 조회된 값으로 REST URL을 만들고, 해당 URL로 조회 요청을 하고 커넥터 목록을 반환받는다.
			responseEntity = requestRestAPI.requestRestAPI(uri);
			String jsonString = responseEntity.getBody();
			
			if(responseEntity!= null && responseEntity.getStatusCode().value() == 200 ){
				JSONParser jsonParser = new JSONParser();
				JSONArray objects =(JSONArray) jsonParser.parse(jsonString);
				
				for(int i = 0 ; i < objects.size() ; i++){
					System.out.println(url+" 의 "+i+"번째 커넥터명 : "+objects.get(i).toString());
				}
			}
			else{
			}			

			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	
		
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
		

		String user ="admin";
		String password = "admin";

		String plainCreds = user + ":" + password;

		byte[] plainCredsBytes = plainCreds.getBytes();

		byte[] base64Creds= Base64.encode(plainCredsBytes);

		System.out.println("base64Creds===="+new String(base64Creds));
		
		String base64StringEncode = new String(base64Creds);
		
		byte[] decodedByte = Base64.decode(base64StringEncode);
		
		System.out.println("==="+new String(decodedByte)+"===");
		
		
		

		requestHeaders.add("Authorization", "Basic " + base64Creds);
//		requestHeaders.add("Authorization", "Basic admin:admin");

	       
/*	   
	       printf Aladdin:OpenSesame | base64
	       .. yields a string 'QWxhZGRpbjpPcGVuU2VzYW1l' that is used like so:

	       Authorization: Basic QWxhZGRpbjpPcGVuU2VzYW1l

*/
	       
	       
		RestTemplate restTemplate = new RestTemplate();
	       
		HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);
	       
	       
		// Add the String message converter
		restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter() );
//		ResponseEntity<String> responseEntity =null;
		
		try {
			responseEntity = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, String.class );
		}catch (Exception e) {
			throw e;
		}
		Globals.logger.debug(uri+" REST API 응답 상태갑 = "+responseEntity.getStatusCode().toString());
		Globals.logger.debug(uri+" REST API 응답 헤더 = "+responseEntity.getHeaders().toString());
		Globals.logger.debug(uri+" REST API 응답 결과 = "+ responseEntity.getBody().toString());

		
		
			
	}
	
	
	
	

	
}
