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
	 * <p>
	 * 
	 * Expected domain: SysUser
	 * <p>
	 * 
	 * The following information should he logged:
	 * <ol>
	 * <li>Username (SysUser.username)</li>
	 * <li>User ID (SysUser.id)</li>
	 * <li>IP address (ip)</li>
	 * </ol>
	 */
	USER_LOGGED_IN,

	/**
	 * User has added a POS machine.
	 * <p>
	 * 
	 * Expected domain: POS.
	 * <p>
	 * 
	 * Expected entity ID: Pos.getId()
	 * <p>
	 * 
	 * The following information should he logged:
	 * <ol>
	 * <li>Complete POS information (entity POS)</li>
	 * <li>Username of the SysUser who performed this action.</li>
	 * </ol>
	 */
	USER_ADDED_POS,

	/**
	 * User has edited a POS machine.
	 * <p>
	 * 
	 * The following should he logged:
	 * <ol>
	 * <li>Complete POS information (entity POS)</li>
	 * <li>Username of the SysUser who performed this action.</li>
	 * </ol>
	 */
	USER_EDITED_POS,

	/**
	 * User has removed a POS machine.
	 * <p>
	 * 
	 * Expected domain: POS.
	 * <p>
	 * 
	 * The following should he logged:
	 * <ol>
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
	 * User has updated the information of an agent.
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
	USER_UPDATED_AGENT,

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
	
	
	USER_ADDED_RNOTE, 
	
	USER_ADDED_RNOTE_DTL, 
	
	/**
	 * User confirmed a return note.
	 * <p>
	 * 
	 */
	USER_CONFIRMED_RNOTE,
	
	USER_ADDED_RNOTE_INVITATION,

	// ---------- POS Server Related -------------
	/**
	 * Event: A product search action has been performed.
	 * <p>
	 * 
	 * Expected domain: POS.
	 * <p>
	 * 
	 * The following should he logged:
	 * <ol>
	 * <li>POS ID (pos.posId)</li>
	 * </ol>
	 */
	POS_INIT_REQ,

	/**
	 * Event: POS initialization successful.
	 * <p>
	 * 
	 * Expected domain: POS.
	 * <p>
	 * 
	 * Expected entity ID: Pos.getId()
	 * <p>
	 */
	POS_INIT_OK,

	/**
	 * Event: POS initialization failed.
	 * <p>
	 * 
	 * Expected domain: POS.
	 * <p>
	 * 
	 * Expected entity ID: Pos.getId()
	 * <p>
	 */
	POS_INIT_FAILED,

	/**
	 * Event: POS initialization failed.
	 * <p>
	 * 
	 * Expected domain: POS.
	 * <p>
	 * 
	 * Expected entity ID: Pos.getId()
	 * <p>
	 */
	POS_LOGGED_IN,

	/**
	 * Event: POS initialization failed.
	 * <p>
	 * 
	 * Expected domain: POS.
	 * <p>
	 * 
	 * Expected entity ID: Pos.getId()
	 * <p>
	 */
	POS_LOGGED_FAILED, //

	/**
	 * Event: A product search action has been performed.
	 * <p>
	 * 
	 * Expected domain: POS.
	 * <p>
	 * 
	 * The following should he logged:
	 * <ol>
	 * <li>Search criteria</li>
	 * <li>Search result</li>
	 * </ol>
	 */
	POS_PRODUCT_SEARCH,

	/**
	 * A 'groupon' has been validated and is successful (business-wise).
	 * <p>
	 * 
	 * Expected domain: Validation
	 * <p>
	 * 
	 * The following should he logged:
	 * <ol>
	 * <li>Complete validation information (see entity bean Validation)</li>
	 * </ol>
	 */
	POS_ORDER_VALIDATED_OK,

	/**
	 * A 'groupon' has been validated and failed.
	 * <p>
	 * 
	 * Expected domain: Validation
	 * <p>
	 * 
	 * The following should he logged:
	 * <ol>
	 * <li>Complete validation information (see entity bean Validation)</li>
	 * </ol>
	 */
	POS_ORDER_VALIDATED_FAILED,

	/**
	 * Unbind pos assignment success
	 */
	POS_UNBIND_SUCCESS,

	/**
	 * Unbind pos asssignment failed.
	 * <p>
	 * 
	 * Expected domain: POS
	 * <p>
	 * 
	 * Expected domain: POS
	 * <p>
	 * 
	 * The following should he logged:
	 * <ol>
	 * </ol>
	 */
	POS_UNBIND_FAILED,

	GROUPON_CACHE_INIT,

	GROUPON_CACHE_SEARCH,

	GROUPON_CACHE_DELETE,
	
	HUAXIA_REDEEM_QUERY,
	
	HUAXIA_REDEEM_VALIDATE_OK,
	
	HUAXIA_REDEEM_VALIDATE_FAILED,
	
	HUAXIA_REDEEM_ACK_OK,
	
	HUAXIA_REDEEM_ACK_FAILED,
	
	/**
	 * create FinanceReportHistory
	 */
	FINANCE_REPORT_HISTORY_CREATE,
	
	/**
	 * modify FinanceReportHistory
	 */
	FINANCE_REPORT_HISTORY_MODIFY,

	
	
	/**
	 * Indicates a QQ Meishi Q-Mi transaction request is successfully handled by
	 * the server. This <b>does not</b> mean that.
	 * 
	 * This event is emitted when the server processed a QQ Meishi Q-Mi Rewards
	 * transaction.
	 * 
	 * Attributes included:
	 * <ul>
	 * <li>posId: The POS ID (e.g. REWARDS-0001)</li>
	 * <li>qqwsapi: contains the original content of the QQMeishi web service.
	 * <ul>
	 * <li>content: The raw (original) content returned from the web service.
	 * This is a place holder, it should holds the actual content of the values
	 * returned from the QQ Meishi web service.</li>
	 * </ul>
	 * </li>
	 * </ul>
	 * 
	 * @see QQMEISHI_QMI_XACTION_FAILED
	 */
	QQMEISHI_QMI_XACTION_OK,
	
	/**
	 * Indicates a QQ Meishi Q-Mi transaction requested failed to be handled
	 * by the server. The meaning of failure does <b>not</b> refer to the 
	 * business meaning.
	 * 
	 * @see QQMEISHI_QMI_XACTION_FAILED
	 */
	QQMEISHI_QMI_XACTION_FAILED,
	
	/**
	 * QQ Adidas
	 */
	QQADIDAS_RECEIVE_GIFT_OK,
	QQADIDAS_RECEIVE_GIFT_FAILED,
	QQADIDAS_PRIVILEGE_EXCHANGE_OK,
	QQADIDAS_PRIVILEGE_EXCHANGE_FAILED,
	QQADIDAS_WEIXIN_SIGNIN_OK,
	QQADIDAS_WEIXIN_SIGNIN_FAILED,
	QQADIDAS_QQACTIVITYHISTORY_CREATE
}
