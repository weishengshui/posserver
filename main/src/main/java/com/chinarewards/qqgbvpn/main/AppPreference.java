/**
 * 
 */
package com.chinarewards.qqgbvpn.main;

/**
 * Stores application-wide preference.
 * 
 * @author Cyril
 * @since 1.0.0
 */
public class AppPreference {

	private String dbType;

	private String db;

	private String dbUsername;

	private String dbPassword;

	/**
	 * @return the dbType
	 */
	public String getDbType() {
		return dbType;
	}

	/**
	 * @param dbType
	 *            the dbType to set
	 */
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	/**
	 * @return the dbUsername
	 */
	public String getDbUsername() {
		return dbUsername;
	}

	/**
	 * @param dbUsername
	 *            the dbUsername to set
	 */
	public void setDbUsername(String dbUsername) {
		this.dbUsername = dbUsername;
	}

	/**
	 * @return the dbPassword
	 */
	public String getDbPassword() {
		return dbPassword;
	}

	/**
	 * @param dbPassword
	 *            the dbPassword to set
	 */
	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	/**
	 * Returns the name of the database to use.
	 * 
	 * @return the db
	 */
	public String getDb() {
		return db;
	}

	/**
	 * @param db
	 *            the db to set
	 */
	public void setDb(String db) {
		this.db = db;
	}

}
