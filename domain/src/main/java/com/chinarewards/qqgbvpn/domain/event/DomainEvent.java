package com.chinarewards.qqgbvpn.domain.event;

/**
 * Defines a list of well-known domain events.
 * 
 * @author kmtong
 * @since 0.1.0
 */
public enum DomainEvent {

	// -------- UI Related -------------

	/**
	 * User has logged in to the management UI.
	 */
	USER_LOGGED_IN,

	/**
	 * User has added a POS machine.
	 * <p>
	 * 
	 * The following should he logged:
	 * <ol>
	 * <li>Complete POS information (entity POS)</li>
	 * <li>Username of the SysUser who performed this action.</li>
	 * </ol>
	 */
	USER_ADDED_POS,

	/**
	 * User has added a POS machine.
	 * <p>
	 * 
	 * Expected domain: POS.
	 * <p>
	 * 
	 * The following should he logged:
	 * <ol>
	 * <li>Complete POS information (entity POS)</li>
	 * <li>Username of the SysUser who performed this action.</li>
	 * </ol>
	 */
	USER_REMOVED_POS, //

	/**
	 * User has added a Agent.
	 * <p>
	 * 
	 * Expected domain: Agent.
	 * <p>
	 * 
	 * The following should he logged:
	 * <ol>
	 * <li>Complete Agent information (entity Agent)</li>
	 * <li>Username of the SysUser who performed this action.</li>
	 * </ol>
	 */
	USER_ADDED_AGENT,

	/**
	 * User has removed an Agent.
	 * <p>
	 * 
	 * Expected domain: Agent.
	 * <p>
	 * 
	 * The following should he logged:
	 * <ol>
	 * <li>Complete Agent information (entity Agent)</li>
	 * <li>Username of the SysUser who performed this action.</li>
	 * </ol>
	 */
	USER_REMOVED_AGENT, //
	
	/**
	 * User has added a delivery note.
	 * <p>
	 * 
	 * Expected domain: Delivery Note.
	 * <p>
	 * 
	 * The following should he logged:
	 * <ol>
	 * <li>Complete delivery note.</li>
	 * <li>Child delivery details (if any).</li>
	 * <li>Username of the SysUser who performed this action.</li>
	 * </ol>
	 */
	USER_ADDED_DNOTE, 
	
	/**
	 * User has added a delivery note.
	 * <p>
	 * 
	 * Expected domain: Delivery Note.
	 * <p>
	 * 
	 * The following should he logged:
	 * <ol>
	 * <li>Delivery note ID (UUID).</li>
	 * <li>Complete delivery note detail.</li>
	 * <li>Username of the SysUser who performed this action.</li>
	 * </ol>
	 */
	USER_ADDED_DNOTE_DTL, 
	
	USER_REMOVED_DNOTE_DTL, USER_REMOVED_DNOTE, //
	USER_CONFIRMED_DNOTE, USER_PRINTED_DNOTE, //
	USER_ADDED_RNOTE, USER_ADDED_RNOTE_DTL,

	// ---------- POS Server Related -------------
	POS_INIT_REQ, POS_INIT_OK, POS_INIT_FAILED, POS_LOGGED_IN, //
	POS_PRODUCT_SEARCH, POS_ORDER_VALIDATED_OK, POS_ORDER_VALIDATED_FAILED
}
