package com.chinarewards.qqgbpvn.testing.prepare.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataBase {
	
	private static final Logger logger = LoggerFactory.getLogger(DataBase.class);
	
	private static DataBase dataBase;
	
	public static final int ORACLE = 0;
	public static final int MYSQL = 1;
	
	private static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
	private static final String ORACLE_DRIVER = "oracle.jdbc.driver.OracleDriver";
	
	private final String url;			//连接地址
	private final String username;		//用户名
	private final String password;		//密码
	
	private Connection con;
	private Statement stmt;
	
	private static final int MAX_BATCH = 50;
	private static int existingBatch = 0;
	
	private DataBase(String url, String username, String password){
		this.url = url;
		this.username = username;
		this.password = password;
	}
	
	public static final synchronized DataBase getInstance(int db, String url, 
			String username, String password){
		if(dataBase == null){
			dataBase = new DataBase(url, username, password);
		}
		
		//load database driver class
		dataBase.loadDriver(db);
		
		//build a db connection, build a db statement, TODO build a db resultSet.
		dataBase.newConnection();
		return dataBase;
	}
	
	/**
	 * description：load database driver
	 * @param db
	 * @time 2011-9-27   上午11:36:03
	 * @author Seek
	 */
	private void loadDriver(int db){
		try {
			String driver = null;
			switch(db){
				case ORACLE:
					driver = ORACLE_DRIVER;
				case MYSQL:
					driver = MYSQL_DRIVER;
			}
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * description：build all database config
	 * @time 2011-9-27   上午10:35:43
	 * @author Seek
	 */
	public void newConnection() {
		try {
			//关闭
			if(this.con != null && this.stmt != null){
				this.close();
			}
			
			this.con = DriverManager.getConnection(url, username, password);
			con.setAutoCommit(false);
			
			this.stmt = this.con.createStatement();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * description：excute the sql
	 * @param sql
	 * @throws Exception
	 * @time 2011-9-27   下午02:18:12
	 * @author Seek
	 */
	public void excuteSql(String sql) throws Exception {
		try {
			logger.debug("excute sql:"+sql);
			stmt.executeQuery(sql);
		} catch (SQLException e) {
			throw new Exception(e.getMessage(), e);
		}
	}
	
	/**
	 * description：add a batch sql to Statement
	 * @param sql
	 * @throws Exception
	 * @time 2011-9-27   上午11:15:42
	 * @author Seek
	 */
	public void addBatch(String sql) throws Exception {
		try {
			logger.debug("addBatch sql:"+sql);
			stmt.addBatch(sql);
			existingBatch++;
			if(existingBatch >= MAX_BATCH){
				existingBatch = 0;
				stmt.executeBatch();
			}
		} catch (Throwable e) {
			throw new Exception(e.getMessage(), e);
		}
	}
	
	/**
	 * description：commit the connection
	 * @time 2011-9-27   上午11:20:02
	 * @author Seek
	 */
	public void commit(){
		try {
			stmt.executeBatch();
			con.commit();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * description：rollback the connection
	 * @time 2011-9-27   上午11:19:51
	 * @author Seek
	 */
	public void rollBack(){
		try {
			con.rollback();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * description：close the connection
	 * @time 2011-9-27   上午11:14:36
	 * @author Seek
	 */
	public void close() {
		try{
			this.stmt.close();
			this.con.close();
			
			this.stmt = null;
			this.con = null;
		}catch(SQLException e){
			logger.error(e.getMessage(), e);
		}
	}
	
}
