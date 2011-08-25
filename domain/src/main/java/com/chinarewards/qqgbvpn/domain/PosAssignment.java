package com.chinarewards.qqgbvpn.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

/**
 * Represents the current assignment of POS to whatever Agent.
 * <p>
 * 
 * This entity should be created by management UI, and used by POS server.
 * 
 * @author kmtong
 * @since 0.1.0
 */
@Entity
public class PosAssignment {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	String id;

	@ManyToOne
	Pos pos;

	@ManyToOne
	Agent agent;

}
