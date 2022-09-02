package com.base.common.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.base.common.manager.AlarmManager;
import com.base.common.vo.AlarmMessage;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.base.common.log.CategoryLog;
import com.base.common.manager.ResourceManager;

public class LavenderDBSingleton {
	
	private static final String JDBC_DRIVERNAME = ResourceManager.getInstance().getValue("db.driver");
	private static final String JDBC_URL = ResourceManager.getInstance().getValue("db.url");
	//获取解密数据
	private static final String JDBC_USER = ResourceManager.getInstance().getDecryptionValue("db.user");
	//获取解密数据
	private static final String JDBC_PWD = ResourceManager.getInstance().getDecryptionValue("db.password");
	
	private static final String minEvictableIdleTimeMillis = ResourceManager.getInstance().getValue("db.minEvictableIdleTimeMillis");
	private static final String testWhileIdle = ResourceManager.getInstance().getValue("db.testWhileIdle");
	private static final String removeAbandoned = ResourceManager.getInstance().getValue("db.removeAbandoned");
	private static final String removeAbandonedTimeout = ResourceManager.getInstance().getValue("db.removeAbandonedTimeout");
	private static final String logAbandoned = ResourceManager.getInstance().getValue("db.logAbandoned");
	private static final String maxPoolPreparedStatementPerConnectionSize = ResourceManager.getInstance().getValue("db.maxPoolPreparedStatementPerConnectionSize");
	
	public DataSource dataSource;
	
	public static LavenderDBSingleton m_singleton = new LavenderDBSingleton();
	
	private Connection conn = null;

	private LavenderDBSingleton() {
		
		String minConnection = ResourceManager.getInstance().getValue("db.min.connection");
		String maxConnection = ResourceManager.getInstance().getValue("db.max.connection");
		String timeout = ResourceManager.getInstance().getValue("db.timeout");
		String leaseTime = ResourceManager.getInstance().getValue("db.leaseTime");
		
		Map<String,String> properties = new HashMap<String,String>();
		

		properties.put("driverClassName", JDBC_DRIVERNAME);
		properties.put("url", JDBC_URL);
		properties.put("username", JDBC_USER);
		properties.put("password", JDBC_PWD);
		
		//初始化时建立物理连接的个数
		if(StringUtils.isNotEmpty(minConnection)){
			properties.put("initialSize", minConnection);
		}
		//最大连接池数量
		if(StringUtils.isNotEmpty(maxConnection)){
			properties.put("maxActive", maxConnection);
		}
		//最小连接池数量
		if(StringUtils.isNotEmpty(minConnection)){
			properties.put("minIdle", minConnection);
		}
		
		//获取连接时最大等待时间，单位毫秒。
		if(StringUtils.isNotEmpty(timeout)){
			properties.put("maxWait", timeout);
		}
		
		//1) Destroy线程会检测连接的间隔时间 2) testWhileIdle的判断依据，详细看testWhileIdle属性的说明
		if(StringUtils.isNotEmpty(leaseTime)){
			properties.put("timeBetweenEvictionRunsMillis",leaseTime);
		}
		
		//连接保持空闲而不被驱逐的最长存活时间
		if(StringUtils.isNotEmpty(minEvictableIdleTimeMillis)){
			properties.put("minEvictableIdleTimeMillis", minEvictableIdleTimeMillis);
		}
		
		//申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。
		if(StringUtils.isNotEmpty(testWhileIdle)){
			properties.put("testWhileIdle", testWhileIdle);
		}

		//开启强制回收连接功能,强制回收连接要求程序在get到连接后的N秒内必须close连接，否则druid会强制中止该连接，不管该连接中是否有事务在运行
		if(StringUtils.isNotEmpty(removeAbandoned)){
			properties.put("removeAbandoned", removeAbandoned);
		}
		
		//当程序get到一个连接后,在 N 秒内必须 Close,否则连接池也会弄死它, 需要考虑一个合理的值，避免业务跑不完
		if(StringUtils.isNotEmpty(removeAbandonedTimeout)){
			properties.put("removeAbandonedTimeout", removeAbandonedTimeout);
		}
		
		//当连接池强制中止程序连接后，是否将stack trace记录到日志中
		if(StringUtils.isNotEmpty(logAbandoned)){
			properties.put("logAbandoned", logAbandoned);
		}

		//以下两个参数影响性能, 不建议使用
		//程序获取连接池中的连接时进行检测
		properties.put("testOnBorrow", "false");
		//程序归还连接时，检测连接是否仍有效
		properties.put("testOnReturn", "false");
		
		//是否缓存preparedStatement
		properties.put("poolPreparedStatements", "true");
		//每个连接最多缓存多少个SQL
		if(StringUtils.isNotEmpty(maxPoolPreparedStatementPerConnectionSize)){
			properties.put("maxPoolPreparedStatementPerConnectionSize", maxPoolPreparedStatementPerConnectionSize);
		}else{
			properties.put("maxPoolPreparedStatementPerConnectionSize", "20");
		}
		
		
		properties.put("validationQuery", "SELECT 'x' FROM DUAL");
		
		CategoryLog.commonLogger.debug("{}",properties);
		
		try {
			dataSource = DruidDataSourceFactory.createDataSource(properties);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * getInstance() returns the class, instantiating it if there is not yet an
	 * instance in the VM.
	 */
	public static LavenderDBSingleton getInstance() {
		return (m_singleton);
	}

	/*
	 * calls getConnection() on the broker class
	 */
	public synchronized Connection getConnection() {
		try {
			if (dataSource == null) {
				throw new Exception("Can't get Connection broker!");
			}
			return (dataSource.getConnection());
		} catch (Exception e) {
			AlarmManager.getInstance().process(new AlarmMessage(AlarmMessage.AlarmKey.Database, e.getMessage()));
			e.printStackTrace();
			return conn;
		}
	}

	public void closeAll(ResultSet rs, PreparedStatement pstmt, Connection conn) {
		try {

			if (null != rs) {
				rs.close();
			}
			if (null != pstmt) {
				pstmt.close();
			}
			if (null != conn) {
				conn.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
}
