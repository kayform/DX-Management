package com.k4m.eXperdb.webconsole.settings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface SettingsService {
	List<Map<String, Object>> getWebConsoleSystemProperties() throws Exception; //웹콘솔 시스템환경변수 조회
	
	List<Map<String, Object>> selectUserList(HashMap<String, String> param) throws Exception; // 사용자 목록 조회	

	Map<String, Object> selectUser(HashMap<String, String> param) throws Exception; //사용자 정보 조회
	
	int deleteUser(HashMap<String, String> param) throws Exception; // 사용자 삭제
	
	int insertUser(HashMap<String, String> param) throws Exception; // 사용자 등록
	
	int updateUser(HashMap<String, String> param) throws Exception; // 사용자 정보 수정
	
	int updateUserPassword(HashMap<String, String> param) throws Exception; // 사용자 비밀번호 수정
	
	List<Map<String, Object>> selectUserAuth(HashMap<String, String> param) throws Exception; // 사용자 메뉴권한 조회
	
	int insertUserAuth(HashMap<String, String> param) throws Exception; // 사용자 메뉴권한 부여

	int updateUserAuthList(List<HashMap<String, String>> paramList) throws Exception;
	
	int deleteUserAuth(HashMap<String, String> param) throws Exception; // 사용자 메뉴권한 삭제
	
	List<Map<String, Object>> getWebConsoleLoglist() throws Exception;
	
	List<Map<String, Object>> getServerLoglist(String dirName) throws Exception;

	Map<String, Object> getLogContent() throws Exception; // 
	
	Map<String, String> getWebConsoleStatus() throws Exception; //웹콘솔 시스템 상태조회
	
	List<Map<String,Object>> selectSERVER (Map<String, Object> param); // SERVER 관리 조회 리스트

	int selectSERVERTotalCount(Map<String, Object> param); // SERVER 전체 건수 검색
	
	List<Map<String,Object>> selectReadonlyCode (); // 데이터 쓰기 권한 조회

	int insertSERVER(HashMap<String, String> param); // SERVER 등록

	int deleteSERVER(HashMap<String, String> param); // SERVER 삭제

	Map<String, Object> selectSERVERDetail(Map<String, Object> param); // SERVER 상제 조회

	int updateSERVER(HashMap<String, String> param); // SERVER 수정
	
	int selectSERVERDupCheck(HashMap<String, String> param);
		
	List<Map<String, Object>> getWebConsoleThreadList() throws Exception;
}
