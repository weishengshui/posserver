package com.chinarewards.qqgbvpn.main.protocol.cmd;

public class SearchResponseDetail {
	
	private String grouponId;
	
	private String grouponName;
	
	private String mercName;
	
	private String listName;
	
	private String detailName;

	@Override
	public String toString() {
		return " [grouponId=" + grouponId + ", grouponName=" + grouponName
				+ ", mercName=" + mercName + ", listName=" + listName
				+ ", detailName=" + detailName + "]";
	}

	//--------------------------------------------------//
	public String getGrouponId() {
		return grouponId;
	}

	public void setGrouponId(String grouponId) {
		this.grouponId = grouponId;
	}

	public String getGrouponName() {
		return grouponName;
	}

	public void setGrouponName(String grouponName) {
		this.grouponName = grouponName;
	}

	public String getMercName() {
		return mercName;
	}

	public void setMercName(String mercName) {
		this.mercName = mercName;
	}

	public String getListName() {
		return listName;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}

	public String getDetailName() {
		return detailName;
	}

	public void setDetailName(String detailName) {
		this.detailName = detailName;
	}
	


}
