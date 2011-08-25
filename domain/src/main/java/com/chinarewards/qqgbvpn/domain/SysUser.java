package com.chinarewards.qqgbvpn.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Stores system user information.
 * 
 * @author kmtong
 * @since 0.1.0
 */
@Entity
public class SysUser {

	@Id
	String username;

	String password;

}
