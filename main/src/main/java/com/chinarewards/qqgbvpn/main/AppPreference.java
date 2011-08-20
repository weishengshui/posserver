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

	private String weiboUsername;

	private String weiboPassword;

	private String sinaProvinceId;

	private String sinaCityId;

	private String dbType;

	private String db;

	private String dbUsername;

	private String dbPassword;

	private String[] microblogComments;

	private Integer replyPerLoop;

	private Integer pausePerReply;

	private String commentFile;

	/**
	 * @return pause-per-reply. If this value is <code>null</code>, no limit
	 *         will be applied.
	 */
	public Integer getPausePerReply() {
		return pausePerReply;
	}

	/**
	 * @param pausePerReply
	 *            the pausePerReply to set
	 */
	public void setPausePerReply(Integer pausePerReply) {
		this.pausePerReply = pausePerReply;
	}

	/**
	 * Returns reply per loop. If this value is <code>null</code>, no limit will
	 * be applied.
	 * 
	 * @return
	 */
	public Integer getReplyPerLoop() {
		return replyPerLoop;
	}

	/**
	 * @param replyPerLoop
	 *            replyPerLoop to set
	 */
	public void setReplyPerLoop(Integer replyPerLoop) {
		this.replyPerLoop = replyPerLoop;
	}

	/**
	 * Returns the text used for replying microblog messages.
	 */
	public String[] getMicroblogComments() {
		return microblogComments;
	}

	/**
	 * @param microblogComments
	 *            the microblogComments to set
	 */
	public void setMicroblogComments(String[] microblogComments) {
		this.microblogComments = microblogComments;
	}

	/**
	 * @return the weiboUsername
	 */
	public String getWeiboUsername() {
		return weiboUsername;
	}

	/**
	 * @param weiboUsername
	 *            the weiboUsername to set
	 */
	public void setWeiboUsername(String weiboUsername) {
		this.weiboUsername = weiboUsername;
	}

	/**
	 * @return the weiboPassword
	 */
	public String getWeiboPassword() {
		return weiboPassword;
	}

	/**
	 * @param weiboPassword
	 *            the weiboPassword to set
	 */
	public void setWeiboPassword(String weiboPassword) {
		this.weiboPassword = weiboPassword;
	}

	/**
	 * @return the sinaProvinceId
	 */
	public String getSinaProvinceId() {
		return sinaProvinceId;
	}

	/**
	 * @param sinaProvinceId
	 *            the sinaProvinceId to set
	 */
	public void setSinaProvinceId(String sinaProvinceId) {
		this.sinaProvinceId = sinaProvinceId;
	}

	/**
	 * @return the sinaCityId
	 */
	public String getSinaCityId() {
		return sinaCityId;
	}

	/**
	 * @param sinaCityId
	 *            the sinaCityId to set
	 */
	public void setSinaCityId(String sinaCityId) {
		this.sinaCityId = sinaCityId;
	}

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

	/**
	 * @return the commentFile
	 */
	public String getCommentFile() {
		return commentFile;
	}

	/**
	 * @param commentFile
	 *            the commentFile to set
	 */
	public void setCommentFile(String commentFile) {
		this.commentFile = commentFile;
	}

}
