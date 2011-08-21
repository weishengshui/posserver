package com.chinarewards.qqgbvpn.qqapi.vo;

public class GroupBuyingSearchListVO {

	/**
	 * 团购ID
	 */
	private String grouponId;
	
	/**
	 * 团购名称-小票上显示
	 */
	private String grouponName;
	
	/**
	 * 商家名称-小票上显示
	 */
	private String mercName;
	
	/**
	 * 列表上的名称，包含换行符号
	 */
	private String listName;
	
	/**
	 * 验证界面上的名称
	 */
	private String detailName;

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
