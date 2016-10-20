package com.k4m.eXperdb.webconsole.settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class SettingsDAO {
	
	/**
	 * Session 
	 */
	@Autowired 
	private SqlSession sqlSession;
	
	public List<Map<String,Object>> selectUserList(HashMap<String, String> param) {
		List<Map<String,Object>> list = sqlSession.selectList("user-mapper.selectUserList", param);
		return list;
	}

	public List<Map<String, Object>> selectUser(HashMap<String, String> param) {
		String mode = (String) param.get("mode");
		List<Map<String,Object>> list = null;
		if (mode.equals("V")) {
			list = sqlSession.selectList("user-mapper.selectUserView", param);
		} else {
			list = sqlSession.selectList("user-mapper.selectUser", param);
		}
		
		return list;
	}
	
	public int deleteUser(HashMap<String, String> param) {
		int deleteRow = 0;
		deleteRow = sqlSession.delete("user-mapper.deleteUser", param);
		//사용자 삭제시 해당 사용자의 메뉴권한도 삭제
		sqlSession.delete("user-mapper.deleteUserAuthAll", param);
		return deleteRow;
	}
	
	public int insertUser(HashMap<String, String> param) {
		int insertRow = 0;
		sqlSession.delete("user-mapper.deleteUserAuthAll", param);
		//사용자 등록시 무조건적으로 대쉬보드메뉴는 등록시킴
		param.put("menu_id", "DSB000");
		sqlSession.insert("user-mapper.insertUserAuth", param);
		insertRow = sqlSession.insert("user-mapper.insertUser", param);
		return insertRow;
	}
	
	public int updateUser(HashMap<String, String> param) {
		int updateRow = 0;
		updateRow = sqlSession.update("user-mapper.updateUser", param);
		return updateRow;
	}
	
	public int updateUserPassword(HashMap<String, String> param) {
		int updateRow = 0;
		updateRow = sqlSession.update("user-mapper.updateUserPassword", param);
		return updateRow;
	}
	
	public List<Map<String, Object>> selectUserAuth(HashMap<String, String> param) {
		List<Map<String,Object>> list = sqlSession.selectList("user-mapper.selectUserAuth", param);
		return list;
	}
	
	public int insertUserAuth(HashMap<String, String> param) {	
		int insertRow = 0;		
		sqlSession.insert("user-mapper.insertUserAuth", param);
		//메뉴권한 부여시 부모(상위메뉴)권한도 부여
		//sqlSession.insert("user-mapper.insertUserAuthParent", param);
		return insertRow;
	}
	
	public int deleteUserAuth(HashMap<String, String> param) {		
		return sqlSession.delete("user-mapper.deleteUserAuth", param);		
	}
	
	public int deleteUserAuthAll(HashMap<String, String> param) {		
		return sqlSession.delete("user-mapper.deleteUserAuthAll", param);		
	}
	
	public List<Map<String, Object>> selectSERVER(Map<String, Object> param) {
		return sqlSession.selectList("settings-mapper.selectSERVER", param);
	}

	public int selectSERVERTotalCount(Map<String, Object> param) {
		return sqlSession.selectOne("settings-mapper.selectSERVERTotalCount", param); 
	}

	public List<Map<String, Object>> selectReadonlyCode() {
		return sqlSession.selectList("settings-mapper.selectReadonlyCode"); 
	}

	public int insertSERVER(HashMap<String, String> param) {
		return sqlSession.insert("settings-mapper.insertSERVER", param);
	}

	public int deleteSERVER(HashMap<String, String> param) {
		return sqlSession.delete("settings-mapper.deleteSERVER", param);
	}

	public List<Map<String, Object>> selectSERVERDetail(Map<String, Object> param) {
		return sqlSession.selectList("settings-mapper.selectSERVERDetail", param);
	}

	public int updateSERVER(HashMap<String, String> param) {
		return sqlSession.delete("settings-mapper.updateSERVER", param);
	}
	
	public int addPropertie(Map<String, String> param) {
		return sqlSession.insert("settings-mapper.addPropertie", param);
	}
	
	public int updatePropertie(Map<String, String> param) {
		return sqlSession.update("settings-mapper.updatePropertie", param);
	}
	
	public int deletePropertie(Map<String, String> param) {
		return sqlSession.delete("settings-mapper.deletePropertie", param);
	}
	
	public List<Map<String, String>> selectAllPropertie() {
		return sqlSession.selectList("settings-mapper.selectAllPropertieList");
	}

	public int selectSERVERDupCheck(HashMap<String, String> param) {
		return sqlSession.selectOne("settings-mapper.selectSERVERDupCheck", param);
	}

	public int selectSERVERPolicy(HashMap<String, String> param) {
		return sqlSession.selectOne("settings-mapper.selectSERVERPolicy", param);
	}

	public int getLoginHistoryTotalCount(Map<String, Object> param) {
		return sqlSession.selectOne("settings-mapper.selectLoginHistoryTotalCount", param); 
	}

	public List<Map<String, Object>> getLoginHistoryList(Map<String, Object> param) {
		return sqlSession.selectList("settings-mapper.selectLoginHistory", param);
	}

	public int getSettingsHistoryTotalCount(Map<String, Object> param) {
		return sqlSession.selectOne("settings-mapper.selectSettingsHistoryTotalCount", param); 
	}

	public List<Map<String, Object>> getSettingsHistoryList(Map<String, Object> param) {
		return sqlSession.selectList("settings-mapper.selectSettingsHistory", param);
	}

	public List<Map<String, Object>> getSettingsHistoryDetail(Map<String, Object> param) {
		return sqlSession.selectList("settings-mapper.selectSettingsHistoryDetail", param);
	}
}
