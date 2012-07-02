package com.chinarewards.qqadidas.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import com.chinarewards.qqadidas.domain.status.GiftStatus;
import com.chinarewards.qqadidas.domain.status.PrivilegeStatus;

@Entity
public class QQActivityMember {
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;
	@Column(unique = true, nullable = false)
	private String cdkey;
	@Enumerated(EnumType.STRING)
	private GiftStatus giftStatus;
	@Enumerated(EnumType.STRING)
	private PrivilegeStatus privilegeStatus;
	private String importGroupNo;
	private Date importTime;
	private Date createdAt;
	private Date lastModifiedAt;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCdkey() {
		return cdkey;
	}

	public void setCdkey(String cdkey) {
		this.cdkey = cdkey;
	}

	public GiftStatus getGiftStatus() {
		return giftStatus;
	}

	public void setGiftStatus(GiftStatus giftStatus) {
		this.giftStatus = giftStatus;
	}

	public PrivilegeStatus getPrivilegeStatus() {
		return privilegeStatus;
	}

	public void setPrivilegeStatus(PrivilegeStatus privilegeStatus) {
		this.privilegeStatus = privilegeStatus;
	}

	public String getImportGroupNo() {
		return importGroupNo;
	}

	public void setImportGroupNo(String importGroupNo) {
		this.importGroupNo = importGroupNo;
	}

	public Date getImportTime() {
		return importTime;
	}

	public void setImportTime(Date importTime) {
		this.importTime = importTime;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getLastModifiedAt() {
		return lastModifiedAt;
	}

	public void setLastModifiedAt(Date lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

	@Override
	public String toString() {
		return "QQActivityMember [id=" + id + ", cdkey=" + cdkey
				+ ", giftStatus=" + giftStatus + ", privilegeStatus="
				+ privilegeStatus + ", importGroupNo=" + importGroupNo
				+ ", importTime=" + importTime + ", createdAt=" + createdAt
				+ ", lastModifiedAt=" + lastModifiedAt + "]";
	}

}
