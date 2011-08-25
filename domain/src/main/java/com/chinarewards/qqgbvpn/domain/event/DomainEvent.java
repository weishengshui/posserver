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
	 */
	USER_ADDED_POS,

	/**
	 * User has added a POS machine.
	 */
	USER_REMOVED_POS, //
	USER_ADDED_AGENT, USER_REMOVED_AGENT, //
	USER_ADDED_DNOTE, USER_ADDED_DNOTE_DTL, USER_REMOVED_DNOTE_DTL, USER_REMOVED_DNOTE, //
	USER_CONFIRMED_DNOTE, USER_PRINTED_DNOTE, //
	USER_ADDED_RNOTE, USER_ADDED_RNOTE_DTL,

	// ---------- POS Server Related -------------
	POS_INIT_REQ, POS_INIT_OK, POS_INIT_FAILED, POS_LOGGED_IN, //
	POS_PRODUCT_SEARCH, POS_ORDER_VALIDATED_OK, POS_ORDER_VALIDATED_FAILED
}
