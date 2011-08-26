package com.chinarewards.qqgbvpn.domain.event;

/**
 * 
 * @author kmtong
 * @since 0.1.0
 */
public enum DomainEntity {

	/**
	 * Related to system user (SysUser). The entity ID should be user's
	 * <code>Pos.getUsername()</code>, <b>not</b> the UUID.
	 */
	USER,

	/**
	 * Related to POS machine. The entity ID should be the POS machine's ID
	 * <code>Pos.getPosId()</code>, <b>not</b> the UUID.
	 */
	POS,

	/**
	 * An agent. The entity ID should be the UUID of the agent.
	 */
	AGENT,

	/**
	 * Delivery note. The entity ID should be <code>DeliveryNote.getId()</code>.
	 */
	DELIVERY_NOTE,

	/**
	 * Return note. The entity ID should be <code>ReturnNote.getId()</code>.
	 */
	RETURN_NOTE,

	/**
	 * Validation. The entity ID should be <code>Validation.getId</code>
	 */
	VALIDATION,
	
	/**
	 * Unbind pos assignment. The entity ID should be <code>PosAssignment.getId</code>
	 */
	UNBIND_POS_ASSIGNMENT,
	
	/**
	 * Related to group buying search product information. (this just is value object,not entity)The entity ID should be the POS machine's ID
	 * <code>Pos.getPosId()</code>, <b>not</b> the UUID.
	 */
	GROUPON_INFORMATION

}
