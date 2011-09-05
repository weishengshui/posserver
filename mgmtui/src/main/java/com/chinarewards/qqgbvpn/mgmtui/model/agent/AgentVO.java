package com.chinarewards.qqgbvpn.mgmtui.model.agent;

/**
 * description：agent VO
 * @copyright binfen.cc
 * @projectName mgmtui
 * @time 2011-9-5   上午11:36:17
 * @author Seek
 */
public class AgentVO implements java.io.Serializable {
	
	private static final long serialVersionUID = 1842235357239629433L;

	String id;

	String name;

	String email;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
