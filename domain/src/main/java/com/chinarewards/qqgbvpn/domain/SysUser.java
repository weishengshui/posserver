package com.chinarewards.qqgbvpn.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SysUser {

	@Id
	String username;

	String password;

}
