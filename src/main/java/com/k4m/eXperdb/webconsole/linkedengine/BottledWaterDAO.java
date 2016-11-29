package com.k4m.eXperdb.webconsole.linkedengine;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.k4m.eXperdb.webconsole.common.Globals;
import com.k4m.eXperdb.webconsole.db.DBCPPoolManager;

@Repository
public class BottledWaterDAO {
	
	/**
	 * Session 
	 */
	@Autowired 
	private SqlSession sqlSession;
	
	/**
	 * BottledWater 서버 목록 조회
	 * 
	 * 파라미터는 현재 사용하지 않으나 향후 요구사항 변경을 대비하여 존치시킴 remarked by manimany
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> selectServerList(HashMap<String, String> param) {
		List<Map<String,Object>> list = sqlSession.selectList("linkedengine-mapper.selectServerList", param);
		return list;
	}


	/**
	 * 데이터베이스 연계 정보 조회
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectDatabaseList(String searchSysNm) throws Exception {
		
		List<Map<String, Object>> databaseList = new ArrayList<Map<String, Object>>();
		Map<String, Object> databaseInfo = null;
	
		Globals.logger.debug(" searchSysNm명  "+searchSysNm+" 에 대한 연계정보 조회" );

		//전체 database 명 조회 쿼리
	    String databaseListQuery = "\n SELECT datname FROM pg_database where datistemplate not in ('t') order by datname";
	    
	    Connection conn = null;
		Statement listST = null;
		ResultSet listRS = null;
	
		Statement extensionCheckST = null;
		ResultSet extensionCheckRS = null;
		String extensionCheckQuery = null;
	
		Statement st = null;
		ResultSet rs = null;
		String databaseQuery = null;
	
		
	    try {
	    	conn = DBCPPoolManager.getConnection(searchSysNm);
	
	    	listST = conn.createStatement();
			listRS = listST.executeQuery(databaseListQuery);
	
			String dbName = null;
			
			//database 명 조회
			while (listRS.next()) {
				dbName = listRS.getString(1);  
				Globals.logger.debug(" database명  "+dbName+" 에 대한 연계정보 조회" );
	
				extensionCheckQuery = "select * from dblink('dbname="+ dbName +"', 'select count(*) from pg_extension where extname in (''bottledwater'', ''bwcontrol'')') as t1(count bigint)";
				extensionCheckST = conn.createStatement();
				extensionCheckRS = extensionCheckST.executeQuery(extensionCheckQuery);
	
				//익스텐션 확장자가 설치되어 있다면...
				if(extensionCheckRS.next() && extensionCheckRS.getInt(1) == 2) { 
					databaseQuery = "select database_name, pg_get_status_ingest, connect_name" 
									+"\n from dblink('dbname="+dbName+"'," 
									+"\n 'select t2.database_name, pg_get_status_ingest ,  t2.connect_name"
									+"\n from pg_get_status_ingest() pg_get_status_ingest left outer join"
									+"\n (select database_name, connect_name"
									+"\n from kafka_con_config) t2 on 1=1')"
									+"\n as t1(database_name varchar(100), pg_get_status_ingest varchar(100), connect_name  varchar(100))";
					
					//Globals.logger.debug("database 연계상태 조회 쿼리 = "+databaseQuery);
	
					st = conn.createStatement();
					rs =  st.executeQuery(databaseQuery);
					databaseInfo = new HashMap<String, Object>();
					//kafka_con_config 테이블에 데이터베이스명이 없는 경우가 있어 dbName 으로 데이터베이스명 셋팅
					while (rs.next()) {
						databaseInfo.put("database_name", dbName);
						databaseInfo.put("pg_get_status_ingest", rs.getString(2));
						databaseInfo.put("connect_name", rs.getString(3));
					}
					databaseList.add(databaseInfo);
	
				} else{
					Globals.logger.debug("database "+dbName+"에는 확장 extension이 설치되어 있지 않습니다.");
				}
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
				if(extensionCheckRS !=  null) extensionCheckRS.close();
				if(extensionCheckST !=  null) extensionCheckST.close();
				if(rs !=  null) rs.close();
				if(st !=  null) st.close();
				if(listRS !=  null) listRS.close();
				if(listST !=  null) listST.close();
				if(conn !=  null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    }
		Globals.logger.debug("database 연계정보 조회 갯수 = "+databaseList.size());
		return databaseList;
	}

	/**
	 * 데이터베이스 연계 테이블 목록 조회
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> selectTableList(HashMap<String, Object> param) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> tableList = new ArrayList<Map<String, Object>>();
		Map<String, Object> tableInfo = null;
		
		String dbname = (String) param.get("databaseName");
		//datatable에서 start는 0부터 시작하고 row_no은 1부터 시작함. 따라서 1을 더해 줌. 
		int start  = (int) param.get("start")+1;
		int end = start + (int) param.get("length")-1;
		int recordsTotal = 0;
		
	    Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
	
		String tableQuery = "\n select row_no, table_schema, table_name"
				+ " \n    		from ("
				+ " \n \t 			select  row_no, table_schema, table_name"
				+ " \n \t 			from dblink('dbname="+ dbname +"', 'select row_number() over (order by table_name) as row_no, table_schema, table_name"
				+ " \n \t\t 																from tbl_mapps') as t1(row_no bigint, table_schema varchar(100), table_name varchar(100))"
				+ " \n \t 		) t2"
				+ " \n 			where t2.row_no between "+ start +" and "+ end ;
		
		Globals.logger.debug("연계테이블 목록 조회문 = "+tableQuery);
	
	    try {
	    	recordsTotal = selectTableListTotalCount(param);
	    	
	    	conn = DBCPPoolManager.getConnection((String) param.get("systemName"));
	    	st = conn.createStatement();
			rs = st.executeQuery(tableQuery);
				
			tableInfo = new HashMap<String, Object>();
	
			//database 명 조회
			while (rs.next()) {
				tableInfo = new HashMap<String, Object>();
				tableInfo.put("table_schema", rs.getString("table_schema"));
				tableInfo.put("table_name", rs.getString("table_name"));
				tableList.add(tableInfo);
			}
			
			
		} catch(SQLException e){
			Globals.logger.error("SQL 에러코드 ("+e.getSQLState()+") 에러가 발생했습니다.");
			Globals.logger.error(e.getMessage(), e);
	    } catch (Exception e) {
	    	Globals.logger.error(e.getMessage(), e);
	    } finally{
	    	if(rs !=  null)
				try {
					rs.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
	    	if(st !=  null)
				try {
					st.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
	    	if(conn !=  null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
	    }
		Globals.logger.debug("database 연계 테이블 갯수 = "+tableList.size());
		
		resultMap.put("draw", param.get("draw"));
		resultMap.put("recordsTotal", recordsTotal);
		resultMap.put("recordsFiltered", recordsTotal);
		resultMap.put("data", tableList);
		return resultMap;
	}
	
	
	
	/**
	 * 파라미터 조회조건에 해당하는 연계엔진의 재기동 또는 중지
	 * @param param
	 * @return
	 */
	public Map<String, Object> runProcess(Map<String, String> param){
		Map<String, Object> resMap  = new HashMap<String, Object>();
	
	    
	    Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
	
		//해당 시스템, 데이터베이스의 프로세스를 재기동 또는 중단 쿼리
	    String commandQuery = null;
		String returnMessage = "";
	
		
		if(param.get("command") != null && param.get("command").equals("RUN") ){
		    //전체 database 명 조회 쿼리
		    commandQuery = "select * from dblink('dbname="+ param.get("databaseName") +"', 'select pg_resume_ingest()') as t1(result varchar(100))";
		    returnMessage = "프로세스 기동을 성공했습니다.";
			
		}
		else if(param.get("command") != null && param.get("command").equals("STOP") ){
		    //전체 database 명 조회 쿼리
		    commandQuery = "select * from dblink('dbname="+ param.get("databaseName") +"', 'select pg_suspend_ingest()') as t1(result varchar(100))";
		    returnMessage = "프로세스 중지를 성공했습니다.";
		}
		else{
			//Globals.logger.error("param.get(command)==="+param.get("command")); 
		}
		
		
	    try {
	    	conn = DBCPPoolManager.getConnection(param.get("systemName"));
	    	st = conn.createStatement();
			rs = st.executeQuery(commandQuery);
			if(rs.next() && rs.getString(1).equals("Success(0)")){
				
			}
			else{
				returnMessage = "프로세스 기동 또는 중지가 실패했습니다. 결과값 = "+rs.getString(1);
				resMap.put("result", "FAIL");
			}
	
	    } catch(SQLException e){
			Globals.logger.error("SQL 에러코드 ("+e.getSQLState()+") 에러가 발생했습니다.");
			Globals.logger.error(e.getMessage(), e);
			returnMessage = e.getMessage();
			resMap.put("msg", returnMessage);
			resMap.put("result", "FAIL");			
	    } catch (Exception e) {
	    	Globals.logger.error(e.getMessage(), e);
	    	returnMessage = e.getMessage();
			resMap.put("msg", returnMessage);
			resMap.put("result", "FAIL");
	    } finally{
	    	try {
				if(rs !=  null) rs.close();
				if(st !=  null) st.close();
				if(conn !=  null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    }
	    
	
		resMap.put("msg", returnMessage);
		resMap.put("result", "SUCCESS");
		return resMap;
	}


	/**
	 * 연계 테이블 등록을 위한 목록 조회 - JSON 형태 데이터 반환
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> selectTableRegistrationListData(HashMap<String, Object> param) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> tableList = new ArrayList<Map<String, Object>>();
		Map<String, Object> tableInfo = null; 
		
		String systemName = (String) param.get("systemName");
		String databaseName = (String) param.get("databaseName");
		
		//datatable에서 start는 0부터 시작하고 row_no은 1부터 시작함. 따라서 1을 더해 줌. 
		int start  = (int) param.get("start")+ 1; 
		int end = start + (int) param.get("length")-1;
		int recordsTotal = 0;
		
	    Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
	
		String tableQuery =   "\n select row_no, database_name, table_schema, table_name, is_checked "
							+ "\n from dblink('dbname="+ databaseName +"',"
							+ "\n           '( "
							+ "\n             select row_number() over (order by a.table_schema, a.table_name) as row_no, a.table_catalog as database_name, a.table_schema, a.table_name,"
							+ "\n 		             (select count(*) "
							+ "\n		              from tbl_mapps b "
							+ "\n		              where b.database_name = a.table_catalog "
							+ "\n		              and b.table_schema = a.table_schema  "
							+ "\n		              and b.table_name = a.table_name "
							+ "\n		             ) as is_checked "
							+ "\n	          from information_schema.tables a "
							+ "\n	          where table_type =  ''BASE TABLE'' "
							+ "\n	          and table_schema not in (''pg_catalog'', ''information_schema'') "
							+ "\n	          and table_name not in (''tbl_mapps'',''tbl_mapps_hist'', ''kafka_con_config'') "
							+ "\n	          order by a.table_schema, a.table_name, is_checked "
							+ "\n	         )' "
							+ "\n	         ) "
							+ "\n as t1(row_no bigint, database_name varchar(100), table_schema varchar(100), table_name varchar(100),is_checked bigint) "
							+ "\n where t1.row_no between "+ start +" and "+ end +" "; 
		
		Globals.logger.debug("연계 테이블 등록을 위한 목록 조회문 = "+tableQuery);
	
	    try {
	    	recordsTotal = selectTableRegistrationListTotalCount(param);
	    	
	    	conn = DBCPPoolManager.getConnection(systemName);
	    	st = conn.createStatement();
			rs = st.executeQuery(tableQuery);
				
			tableInfo = new HashMap<String, Object>();
	
			//database 명 조회
			while (rs.next()) {
				tableInfo = new HashMap<String, Object>();
				tableInfo.put("table_schema", rs.getString("table_schema"));
				tableInfo.put("table_name", rs.getString("table_name"));
				tableInfo.put("is_checked", rs.getString("is_checked"));
				tableList.add(tableInfo);
			}
		} catch(SQLException e){
			Globals.logger.error("SQL 에러코드 ("+e.getSQLState()+") 에러가 발생했습니다.");
			Globals.logger.error(e.getMessage(), e);
	    } catch (Exception e) {
	    	Globals.logger.error(e.getMessage(), e);
	    } finally{
	    	if(rs !=  null)
				try {
					rs.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
	    	if(st !=  null)
				try {
					st.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
	    	if(conn !=  null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
	    }
		Globals.logger.debug("연계 테이블 등록을 위한 목록 조회 테이블 갯수 = "+tableList.size());
		
		resultMap.put("draw", param.get("draw"));
		resultMap.put("recordsTotal", recordsTotal);
		resultMap.put("recordsFiltered", recordsTotal);
		resultMap.put("data", tableList);
		return resultMap;
	}

	/**
	 * 데이터베이스 연계 테이블 목록 조회 총 건수
	 * @param param
	 * @return
	 * @throws Exception
	 */
	private int selectTableListTotalCount(HashMap<String, Object> param){
		
	    Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		int totalCount = 0;
	
		String tableCountQuery = "\n select count(*) as TOTAL_COUNT "
								+"\n from dblink('dbname="+ param.get("databaseName") +"', 'select table_schema, table_name "
								+"\n\t from tbl_mapps') as t1(table_schema varchar(100), table_name varchar(100))";
	
		Globals.logger.debug("연계테이블 총 건수 조회문 = "+tableCountQuery);
	
		try {
	
	    	conn = DBCPPoolManager.getConnection((String) param.get("systemName"));
	    	st = conn.createStatement();
			rs = st.executeQuery(tableCountQuery);
				
			//database 명 조회
			while (rs.next()) {
				totalCount = rs.getInt("TOTAL_COUNT");
				Globals.logger.debug("연계테이블 총 건수 = "+totalCount);
			}
	
			
			
		} catch(SQLException e){
			Globals.logger.error("SQL 에러코드 ("+e.getSQLState()+") 에러가 발생했습니다.");
			Globals.logger.error(e.getMessage(), e);
	    } catch (Exception e) {
	    	Globals.logger.error(e.getMessage(), e);
	    } finally{
	    	try {
				if(rs !=  null) rs.close();
				if(st !=  null) st.close();
				if(conn !=  null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    }
		return totalCount;
	}


	/**
	 * 연계 테이블 등록을 위한 목록 조회 - 총 건수 반환
	 * @param param
	 * @return
	 * @throws Exception
	 */
	private int selectTableRegistrationListTotalCount(HashMap<String, Object> param){
		
	    Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		int totalCount = 0;
		String tableCountQuery =  "\n select TOTAL_COUNT "
								+ "\n from dblink('dbname="+ param.get("databaseName") +"', " 
								+ "\n            '( "
								+ "\n              select count(a.*) as TOTAL_COUNT "
								+ "\n              from information_schema.tables a "
								+ "\n              where table_type =  ''BASE TABLE'' "
								+ "\n              and table_schema not in (''pg_catalog'', ''information_schema'') "
								+ "\n              and table_name not in (''tbl_mapps'',''tbl_mapps_hist'', ''kafka_con_config'') "
								+ "\n            )' "
								+ "\n           ) "
								+ "\n as t1(TOTAL_COUNT bigint)";
		
		Globals.logger.debug("연계 테이블 등록을 위한 목록 총 건수 조회문 = "+tableCountQuery);
	
		try {
	
	    	conn = DBCPPoolManager.getConnection((String) param.get("systemName"));
	    	st = conn.createStatement();
			rs = st.executeQuery(tableCountQuery);
				
			//database 명 조회
			while (rs.next()) {
				totalCount = rs.getInt("TOTAL_COUNT");
				Globals.logger.debug("연계 테이블 등록을 위한 목록 총 건수 = "+totalCount);
			}
			
		} catch(SQLException e){
			Globals.logger.error("SQL 에러코드 ("+e.getSQLState()+") 에러가 발생했습니다.");
			Globals.logger.error(e.getMessage(), e);
	    } catch (Exception e) {
	    	Globals.logger.error(e.getMessage(), e);
	    } finally{
	    	try {
				if(rs !=  null) rs.close();
				if(st !=  null) st.close();
				if(conn !=  null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    }
		return totalCount;
	}


	public int updateLinkedTableList(List<HashMap<String, String>> paramList) throws Exception {
		int count = 0;
		for(HashMap<String,String> param : paramList) {
			if(Globals.MODE_DATA_INSERT.equals(param.get("mode"))) {
				count += insertLinkedTable(param);
			} else if(Globals.MODE_DATA_DELETE.equals(param.get("mode"))) {
				count += deleteLinkedTable(param);
			}
		}
		return count;
	}


	private int insertLinkedTable(HashMap<String, String> param) throws Exception {
		String systemName = param.get("systemName");
		String databaseName = param.get("databaseName");
		String schemaName = param.get("schemaName");
		String tableName = param.get("tableName");
		
	    Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		int count = 0;
		String query =  "\n select result from dblink('dbname="+ databaseName +"', 'select pg_add_ingest_table(''"+ schemaName +"'', ''"+ tableName +"'', 1, 1, '''')') as t1(result varchar(100)) ";
		
		Globals.logger.debug("연계 테이블 등록문 = "+query);
	
		try {
	
	    	conn = DBCPPoolManager.getConnection(systemName);
	    	st = conn.createStatement();
			rs = st.executeQuery(query);
				
			//결과값이 "Success..."로 시작하면 1을 리턴, 이외에는 0을 리턴한다.
			String result = null;
			while (rs.next()) {
				result = rs.getString("result");
				Globals.logger.debug("연계 테이블 등록 결과 값 = "+result);
				if(result.startsWith("Success")){
					count = 1;
				}
				else{
					count = 0;
				}
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
		return count;
	}


	private int deleteLinkedTable(HashMap<String, String> param) throws Exception {
		String systemName = param.get("systemName");
		String databaseName = param.get("databaseName");
		String schemaName = param.get("schemaName");
		String tableName = param.get("tableName");
		
	    Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		int count = 0;
		String query =  "\n select result from dblink('dbname="+ databaseName +"', 'select pg_del_ingest_table(''"+ schemaName +"'', ''"+ tableName +"'')') as t1(result varchar(100)) ";
		
		Globals.logger.debug("연계 테이블 삭제문 = "+query);
	
		try {
	
	    	conn = DBCPPoolManager.getConnection(systemName);
	    	st = conn.createStatement();
			rs = st.executeQuery(query);
				
			//결과값이 "Success..."로 시작하면 1을 리턴, 이외에는 0을 리턴한다.
			String result = null;
			while (rs.next()) {
				result = rs.getString("result");
				Globals.logger.debug("연계 테이블 삭제 결과 값 = "+result);
				if(result.startsWith("Success")){
					count = 1;
				}
				else{
					count = 0;
				}
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
		return count;
	}

}
