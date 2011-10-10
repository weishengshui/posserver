package com.chinarewards.qqgbvpn.mgmtui.vo;

import java.io.Serializable;

public class FinanceReportVO implements Serializable {

	private static final long serialVersionUID = -8807160572059457815L;
	
	public static final int BASE_AMOUNT = 120;
	
	public static final int BASE_LINE = 1;
	
	public static final double UNIT_PRICE = 0.6;
	
	private String reportMonth;
	
	private String agentName;
	
	private String posId;
	
	private Integer baseAmount;
	
	private Long actuallyValCount;
	
	private Long beyondValCount;
	
	private Double unitPrice;
	
	private Double beyondAmount;
	
	private Double amount;
	
	public FinanceReportVO(){}

	public FinanceReportVO(String reportMonth, String agentName, String posId,
			Integer baseAmount, Long actuallyValCount,
			Long beyondValCount, Double unitPrice,
			Double beyondAmount, Double amount) {
		super();
		this.reportMonth = reportMonth;
		this.agentName = agentName;
		this.posId = posId;
		this.baseAmount = baseAmount;
		this.actuallyValCount = actuallyValCount;
		this.beyondValCount = beyondValCount;
		this.unitPrice = unitPrice;
		this.beyondAmount = beyondAmount;
		this.amount = amount;
	}
	
	public String getReportMonth() {
		return reportMonth;
	}

	public void setReportMonth(String reportMonth) {
		this.reportMonth = reportMonth;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getPosId() {
		return posId;
	}

	public void setPosId(String posId) {
		this.posId = posId;
	}

	public Integer getBaseAmount() {
		return baseAmount;
	}

	public void setBaseAmount(Integer baseAmount) {
		this.baseAmount = baseAmount;
	}

	public Long getActuallyValCount() {
		return actuallyValCount;
	}

	public void setActuallyValCount(Long actuallyValCount) {
		this.actuallyValCount = actuallyValCount;
	}

	public Long getBeyondValCount() {
		return beyondValCount;
	}

	public void setBeyondValCount(Long beyondValCount) {
		this.beyondValCount = beyondValCount;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Double getBeyondAmount() {
		return beyondAmount;
	}

	public void setBeyondAmount(Double beyondAmount) {
		this.beyondAmount = beyondAmount;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

}
