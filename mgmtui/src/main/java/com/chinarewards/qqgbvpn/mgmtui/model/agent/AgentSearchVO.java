package com.chinarewards.qqgbvpn.mgmtui.model.agent;

/**
 * description：search VO
 * @copyright binfen.cc
 * @projectName mgmtui
 * @time 2011-9-5   上午11:40:52
 * @author Seek
 */
public class AgentSearchVO implements java.io.Serializable{
	
	private static final long serialVersionUID = 1384924338373414853L;
	
	/**
	 * 分页参数
	 * page 页号
	 * size 页面内容条数
	 */
	private Integer page;
	private Integer size;
	
	private String agentName;
	
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	
	@Override
	public String toString() {
		return "AgentSearchVO [page=" + page + ", size=" + size
				+ ", agentName=" + agentName + "]";
	}

}
