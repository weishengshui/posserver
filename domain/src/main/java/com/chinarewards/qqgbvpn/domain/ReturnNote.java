package com.chinarewards.qqgbvpn.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import com.chinarewards.qqgbvpn.domain.status.ReturnNoteStatus;

/**
 * 
 * 
 * @author kmtong
 * @since 0.1.0
 */
@Entity
public class ReturnNote {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	String id;

	String rnNumber;

	@ManyToOne
	Agent agent;

	@Enumerated(EnumType.STRING)
	ReturnNoteStatus status;

	Date createDate;
	Date confirmDate;
	Date printDate;

	/**
	 * Copy of the agent name (for redundancy)
	 */
	String agentName;

}
