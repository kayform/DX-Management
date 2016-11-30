package com.k4m.eXperdb.webconsole.linkedengine;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.k4m.eXperdb.webconsole.common.Globals;
import com.k4m.eXperdb.webconsole.db.DBCPPoolManager;

@Repository
public class KafkaConnectDAO {
	
	/**
	 * Session 
	 */
	@Autowired 
	private SqlSession sqlSession;
	
	/**
	 * Kafka 서버 목록 조회
	 * 
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> selectKafkaConnectServerList(HashMap<String, String> param) {
		List<Map<String,Object>> list = sqlSession.selectList("linkedengine-mapper.selectKafkaConnectServerList", param);
		return list;
	}

	/**
	 * systemName, databaseName, connectName 파라미터에 해당하는 kafka connection 생성
	 * @param param
	 * @return
	 * @throws Exception 
	 */
	public String createKafkaConnect(HashMap<String, String> param) throws Exception {
		String systemName = param.get("systemName");
		String databaseName = param.get("databaseName");
		String connectName = param.get("connectName");
		
	    Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		String result = null;
		String query =  "\n select result from dblink('dbname="+ databaseName +"', 'select pg_create_kafka_connect(''"+ connectName +"'')') as t1(result varchar(100)) ";
		
		Globals.logger.debug("kafka connect 생성 = "+query);
	
		try {
	
	    	conn = DBCPPoolManager.getConnection(systemName);
	    	st = conn.createStatement();
			rs = st.executeQuery(query);
				
			while (rs.next()) {
				result = rs.getString("result");
				Globals.logger.debug("kafka connect 생성 결과 값 = "+result);
			}
			
		} catch(SQLException e){
			Globals.logger.error("SQL 에러코드 ("+e.getSQLState()+") 에러가 발생했습니다.");
			Globals.logger.error(e.getMessage(), e);
	    	throw e;
	    } catch (Exception e) {
	    	Globals.logger.error(e.getMessage(), e);
	    	throw e;
	    } finally{
	    	try {
				if(rs !=  null) rs.close();
				if(st !=  null) st.close();
				if(conn !=  null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    }
		return result;
	}
	/**
	 * systemName, databaseName, connectName 파라미터에 해당하는 kafka connection 삭제
	 * @param param
	 * @return
	 * @throws Exception 
	 */
	public String deleteKafkaConnect(HashMap<String, String> param) throws Exception {
		String systemName = param.get("systemName");
		String databaseName = param.get("databaseName");
		String connectName = param.get("connectName");
		
	    Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		String result = null;
		String query =  "\n select result from dblink('dbname="+ databaseName +"', 'select pg_delete_kafka_connect(''"+ connectName +"'')') as t1(result varchar(100)) ";
		
		Globals.logger.debug("kafka connect 삭제 = "+query);
	
		try {
	
	    	conn = DBCPPoolManager.getConnection(systemName);
	    	st = conn.createStatement();
			rs = st.executeQuery(query);
				
			while (rs.next()) {
				result = rs.getString("result");
				Globals.logger.debug("kafka connect 삭제 결과 값 = "+result);
			}
			
		} catch(SQLException e){
			Globals.logger.error("SQL 에러코드 ("+e.getSQLState()+") 에러가 발생했습니다.");
			Globals.logger.error(e.getMessage(), e);
	    	throw e;
	    } catch (Exception e) {
	    	Globals.logger.error(e.getMessage(), e);
	    	throw e;
	    } finally{
	    	try {
				if(rs !=  null) rs.close();
				if(st !=  null) st.close();
				if(conn !=  null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    }
		return result;
	}

}
