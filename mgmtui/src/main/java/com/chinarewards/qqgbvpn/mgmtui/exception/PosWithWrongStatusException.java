/**
 * description：
 * @copyright binfen.cc
 * @projectName mgmtui
 * @time 2011-9-7   下午06:28:20
 * @author Seek
 */
package com.chinarewards.qqgbvpn.mgmtui.exception;

/**
 * description：Pos with wrong status.
 * 
 * @copyright binfen.cc
 * @projectName mgmtui
 * @time 2011-9-7 下午06:28:20
 * @author Seek
 */
public class PosWithWrongStatusException extends Exception {

	private static final long serialVersionUID = -3821601313180634376L;
	
	private String errorContent;	//错误内容

	public String getErrorContent() {
		return errorContent;
	}

	public void setErrorContent(String errorContent) {
		this.errorContent = errorContent;
	}

	public PosWithWrongStatusException() {
	}

	public PosWithWrongStatusException(String message) {
		super(message);
	}

	public PosWithWrongStatusException(Throwable cause) {
		super(cause);
	}

	public PosWithWrongStatusException(String message, Throwable cause) {
		super(message, cause);
	}

}
