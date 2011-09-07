/**
 * 
 */
package com.chinarewards.qqgbvpn.mgmtui.logic.pos;

import java.util.List;

import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.mgmtui.exception.DeliveryNoteWithNoDetailException;
import com.chinarewards.qqgbvpn.mgmtui.exception.PosNotExistException;
import com.chinarewards.qqgbvpn.mgmtui.model.delivery.DeliveryNoteDetailVO;
import com.chinarewards.qqgbvpn.mgmtui.model.delivery.DeliveryNoteVO;
import com.chinarewards.qqgbvpn.mgmtui.model.util.PaginationTools;

/**
 * @author cream
 * @since 1.0.0 2011-09-05
 */
public interface DeliveryLogic {

	/**
	 * Returns all delivery notes.
	 * 
	 * @return
	 */
	public List<DeliveryNoteVO> fetchAllDelivery();

	/**
	 * Returns delivery note.
	 * 
	 * @param noteId
	 * @return
	 */
	public DeliveryNoteVO fetchById(String noteId);

	/**
	 * Get delivery note list.
	 * 
	 * @param pageInfo
	 * @return
	 */
	public PageInfo<DeliveryNoteVO> fetchDeliveryList(PaginationTools pagination);

	/**
	 * Fetch delivery note detail list.
	 * 
	 * @param deliveryId
	 * @return
	 */
	public List<DeliveryNoteDetailVO> fetchDetailListByNoteId(String deliveryId);

	/**
	 * Create an empty delivery note. Generate a serial number for it.
	 * 
	 * @return
	 */
	public DeliveryNoteVO createDeliveryNote();

	/**
	 * Delete delivery note. It will delete delivery detail first.
	 * <p>
	 * Only DeliveryNoteStatus#DRAFT could be delete.
	 * 
	 * @param noteId
	 */
	public void deleteDeliveryNote(String noteId);

	/**
	 * Add or modify agent. Agent could be null.
	 * <p>
	 * <strong>CAREFUL:</strong>Delivery note status must be
	 * {@code DeliveryNoteStatus#DRAFT}
	 * 
	 * @param deliveryNoteId
	 * @param agentId
	 * @return
	 */
	public DeliveryNoteVO associateAgent(String deliveryNoteId, String agentId);

	/**
	 * Append POS to delivery. It will create delivery note detail.
	 * <p>
	 * <strong>CAREFUL:</strong>
	 * <ul>
	 * <li>Delivery note status must be {@code DeliveryNoteStatus#DRAFT}</li>
	 * <li>Pos.dstatus must be {@code PosDeliveryStatus#DELIVERED}</li>
	 * <li>There is no record for PosAssignment.</li>
	 * <ul>
	 * 
	 * @param deliveryNoteId
	 * @param posId
	 * @return
	 * @throws PosNotExistException
	 *             POS not existed.
	 */
	public DeliveryNoteDetailVO appendPosToNote(String deliveryNoteId,
			String posId) throws PosNotExistException;

	/**
	 * Delete delivery note detail from note.
	 * <p>
	 * <strong>CAREFUL:</strong>Delivery note status must be
	 * {@code DeliveryNoteStatus#DRAFT}
	 * 
	 * @param deliveryNoteId
	 * @param detailId
	 */
	public void deletePosFromNote(String deliveryNoteId, String detailId);

	/**
	 * 派送 POS 机给第三方。<br/>
	 * Check status:
	 * <ol>
	 * <li>Delivery note status : {@code DeliveryNoteStatus#DRAFT}</li>
	 * <li>POS status : {@code PosInitializationStatus#INITED}</li>
	 * </ol>
	 * 
	 * @param deliveryNoteId
	 *            should not be empty.
	 * @return returns the POS with wrong status. NOT initialized, with
	 *         PosAssignment, delivered
	 * @throws DeliveryNoteWithNoDetailException
	 *             when delivery not with no details.
	 */
	public List<String> delivery(String deliveryNoteId)
			throws DeliveryNoteWithNoDetailException;

	/**
	 * Delivery note status must be {@code DeliveryNoteStatus#DRAFT}. And this
	 * method will change it to {@code DeliveryNoteStatus#CONFIRMED}.
	 * <p>
	 * Generate confirmed serial number for this delivery note.
	 * <p>
	 * All the POS will be set as DELIVERED and ALLOWED.
	 * 
	 * @param deliveryNoteId
	 */
	public void confirmDelivery(String deliveryNoteId);

	/**
	 * Delivery note status should not be {@code DeliveryNoteStatus#DRAFT}. And
	 * this method will change it to {@code DeliveryNoteStatus#PRINTED}.
	 * 
	 * @param deliveryNoteId
	 */
	public void printDelivery(String deliveryNoteId);
}
