/**
 * 
 */
package com.chinarewards.qqgbvpn.mgmtui.logic.pos;

import java.util.List;

import com.chinarewards.qqgbvpn.domain.DeliveryNote;
import com.chinarewards.qqgbvpn.domain.DeliveryNoteDetail;
import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.mgmtui.model.agent.AgentVO;
import com.chinarewards.qqgbvpn.mgmtui.model.pos.PosVO;
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
	public List<DeliveryNote> fetchAllDelivery();

	/**
	 * Get delivery note list.
	 * 
	 * @param pageInfo
	 * @return
	 */
	public PageInfo<DeliveryNote> fetchDeliveryList(PaginationTools pagination);

	/**
	 * Fetch delivery note detail list.
	 * 
	 * @param note
	 * @return
	 */
	public List<DeliveryNoteDetail> fetchDeliveryNoteDetailList(
			DeliveryNote note);

	/**
	 * 派送 POS 机给第三方。<br/>
	 * Check status:
	 * <ol>
	 * <li>Delivery note status : {@code DeliveryNoteStatus#DRAFT}</li>
	 * <li>POS status : {@code PosInitializationStatus#INITED}</li>
	 * </ol>
	 * 
	 * @param note
	 *            {@code null} means new delivery note. Generate new serial
	 *            number for it.
	 * @param agent
	 *            第三方
	 * @param posList
	 *            POS 机列表
	 * @return returns the POS with wrong status. NOT initialized, with
	 *         PosAssignment, delivered
	 */
	public List<PosVO> delivery(DeliveryNote note, AgentVO agent,
			List<PosVO> posList);

	/**
	 * Delivery note status must be {@code DeliveryNoteStatus#DRAFT}. And this
	 * method will change it to {@code DeliveryNoteStatus#CONFIRMED}.
	 * <p>
	 * Generate confirmed serial number for this delivery note.
	 * 
	 * @param note
	 */
	public void confirmDelivery(DeliveryNote note);

	/**
	 * Delivery note status should not be {@code DeliveryNoteStatus#DRAFT}. And
	 * this method will change it to {@code DeliveryNoteStatus#PRINTED}.
	 * 
	 * @param note
	 */
	public void printDelivery(DeliveryNote note);
}
