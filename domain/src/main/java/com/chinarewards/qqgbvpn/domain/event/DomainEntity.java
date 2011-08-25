package com.chinarewards.qqgbvpn.domain.event;

/**
 * 
 * @author kmtong
 * @since 0.1.0
 */
public enum DomainEntity {

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
	 * FIXME Javadoc??
	 */
	VALIDATION

}
