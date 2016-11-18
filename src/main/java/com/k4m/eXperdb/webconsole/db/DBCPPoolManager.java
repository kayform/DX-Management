package com.k4m.eXperdb.webconsole.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.dbcp2.PoolingDriver;
import org.apache.commons.pool2.impl.GenericObjectPool;

import com.k4m.eXperdb.webconsole.common.Globals;

/*
 * DB 커넥션풀 관리 클래스
 */

public class DBCPPoolManager {
    public class DB_TYPE
    {
    	public static final String ORA = "ORA";
    	public static final String POG = "POG";
    	public static final String POG_REP = "POG_REP";
	    public static final String MSS = "MSS";
	    public static final String TBR = "TBR";
	    public static final String DB2 = "DB2";
	    public static final String ASE = "ASE";
	    public static final String MYSQL = "MYSQL";
	    public static final String IQ = "IQ";
    }
    
	private DBCPPoolManager(){}
	public static ConcurrentHashMap<String, ConfigInfo> ConnInfoList = new ConcurrentHashMap<String, ConfigInfo>();
	public static void setupDriver(ConfigInfo configInfo, String poolName, int maxActive) throws Exception {
		Globals.logger.info("************************************************************");
		Globals.logger.info("DBCPPool을 생성합니다. ["+poolName+"]");

		Connection conn = null;
		// JDBC 클래스 로딩
		try {
			String driver = "";
	        // DB URI
	        String connectURI = "";
			
	        Properties props = new Properties();
	        
    		switch (configInfo.DB_TYPE) {
				case DB_TYPE.ORA :
					driver = "oracle.jdbc.driver.OracleDriver";
					connectURI = "jdbc:oracle:thin:@"+configInfo.SERVERIP+":"+configInfo.PORT+"/"+configInfo.DBNAME;
					break;
				case DB_TYPE.POG :
					driver = "org.postgresql.Driver" ;
					connectURI = "jdbc:postgresql://"+configInfo.SERVERIP+":"+configInfo.PORT+"/"+configInfo.DBNAME;
					break;
				case DB_TYPE.ASE :
					driver = "com.sybase.jdbc4.jdbc.SybDriver" ;
					connectURI = "jdbc:sybase:Tds:"+configInfo.SERVERIP+":"+configInfo.PORT+"/"+configInfo.DBNAME;
					break;
				case DB_TYPE.TBR :
					driver = "com.tmax.tibero.jdbc.TbDriver";
					connectURI = "jdbc:tibero:thin:@"+configInfo.SERVERIP+":"+configInfo.PORT+":"+configInfo.DBNAME;
					break;
				case DB_TYPE.MSS :
					driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver" ;
					connectURI = "jdbc:sqlserver://"+configInfo.SERVERIP+":"+configInfo.PORT+";databaseName="+configInfo.DBNAME;
					break;
				case DB_TYPE.DB2 :
					driver = "com.ibm.db2.jcc.DB2Driver" ;
					connectURI = "jdbc:db2://"+configInfo.SERVERIP+":"+configInfo.PORT+"/"+configInfo.DBNAME;
					System.setProperty("db2.jcc.charsetDecoderEncoder", "3");
					break;
    		}

			Class.forName(driver);
			
			//DB 연결대기 시간
			DriverManager.setLoginTimeout(5);
			
	        // ID and Password
	        props.put("user", configInfo.USERID);
	        props.put("password", configInfo.DB_PW);
	        
	        if (configInfo.CHARSET != null){
	        	props.put("charset", configInfo.CHARSET);
	        }
	        
	        Globals.logger.info("PROPERTY : charset=" + configInfo.CHARSET);
	        
	        // 풀이 커넥션을 생성하는데 사용하는 DriverManagerConnectionFactory를 생성
	        ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(connectURI, props);
	        
	        // ConnectionFactory의 래퍼 클래스인 PoolableConnectionFactory를 생성
	        PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, null);	        
	        
	        // 커넥션 풀로 사용할 commons-collections의 genericOjbectPool을 생성 
	        GenericObjectPool<PoolableConnection> connectionPool = new GenericObjectPool<PoolableConnection>(poolableConnectionFactory);
	        
	        // Pool에서 Connection을 받아와 DB에 Query문을 날리기 전에
	        // 해당 Connection이 Active한지 Check하고 
	        // Active하지 않으면 해당 Connection을 다시 생성합니다
	        connectionPool.setTestOnBorrow(true);
	        connectionPool.setTestOnReturn(true);
	        connectionPool.setTestWhileIdle(true);
	        connectionPool.setMinIdle(maxActive);	
	        connectionPool.setMaxTotal(maxActive);		        
	        connectionPool.setMaxWaitMillis(300000);  //사용할 커넥션이 없을 때 무한 대기
	        connectionPool.setMinEvictableIdleTimeMillis(30 * 1000);
	        connectionPool.setTimeBetweenEvictionRunsMillis(30 * 1000);
	        
	        poolableConnectionFactory.setPool(connectionPool);	        
	        
            //PoolingDriver 자신을 로딩
            Class.forName("org.apache.commons.dbcp2.PoolingDriver");
            PoolingDriver pDriver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");
            PoolingDataSource<PoolableConnection> dataSource=  new PoolingDataSource<PoolableConnection> (connectionPool);
            dataSource.setAccessToUnderlyingConnectionAllowed(true);
            
            //Pool 등록
            pDriver.registerPool(poolName, connectionPool);
            
            ConnInfoList.put(poolName, configInfo);
            
            conn = getConnection(poolName);
            conn.setAutoCommit(false);
            configInfo.DB_VER = conn.getMetaData().getDatabaseMajorVersion() + "." + conn.getMetaData().getDatabaseMinorVersion();
            configInfo.ORG_SCHEMA_NM= conn.getMetaData().getUserName();
		} catch (Exception e) {
			shutdownDriver(poolName);
			throw e;			
		}finally{
			if (conn != null){
				conn.close();
			}
		}
		
		Globals.logger.info("DBCPPool 생성 완료 하였습니다. ["+poolName+"]");
		Globals.logger.info("************************************************************");		
	}
	
	/*
	 * 풀명에 해당한는 풀 및 DB정보 close
	 */
	public static void shutdownDriver(String poolName) throws Exception {
		PoolingDriver driver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");
		driver.closePool(poolName);
		
		if (ConnInfoList.containsKey(poolName)) {
			ConnInfoList.remove(poolName);
		}		
	}
	
	/*
	 * 커넥션풀 모두 닫기 및 ConfigInfo 삭제 
	 */
	public static void shutdownAllDriver() {
		try {
			PoolingDriver driver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");
			for (String poolName : driver.getPoolNames()) {
				driver.closePool(poolName);
				
				if (ConnInfoList.containsKey(poolName)) {
					ConnInfoList.remove(poolName);
				}	
			}
		}catch(Exception e){
			Globals.logger.info(e.getMessage());
		}
	}
	
	/*
	 * connection get
	 */
    public static Connection getConnection(String poolName) throws Exception {
    	Connection conn = DriverManager.getConnection("jdbc:apache:commons:dbcp:" + poolName);

    	conn.setAutoCommit(false);
    	
    	return conn;
    }
    
    /*
     * 풀에 해당하는 DB정보 (ConfigInfo) 추출
     */
    public static ConfigInfo getConfigInfo(String poolName) throws Exception {
    	if (ConnInfoList.containsKey(poolName)){
    		return ConnInfoList.get(poolName);
    	}else{
    		return null;
    	}
    }
    
    /*
     * 풀에 해당하는 ConnectionString
     */
    public static String getConnectionString(String poolName) throws Exception {
    	if (ConnInfoList.containsKey(poolName)){
    		ConfigInfo configInfo = ConnInfoList.get(poolName);
    		String connectURI = "jdbc:sqlserver://"+configInfo.SERVERIP+":"+configInfo.PORT+";databaseName="+configInfo.DBNAME+";user=" + configInfo.USERID + ";password=" + configInfo.DB_PW;
    		return connectURI;
    	}else{
    		return null;
    	}
    }
    
    /*
     * 풀이 존재하는지 확인
     */
    public static boolean ContaintPool(String poolName) throws Exception {
    	if (ConnInfoList.containsKey(poolName)){
    		return true;    		
    	}else{
    		return false;
    	}
    }
    
    public static String[] GetPoolNameList() throws Exception {
    	PoolingDriver driver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");
    	return driver.getPoolNames();
    }
    
    public static int getPoolCount() {
    	return ConnInfoList.size(); 
    }
}
