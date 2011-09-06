/**
 * 
 */
package com.chinarewards.qqgbvpn.mgmtui.dao;

import java.util.List;

import com.chinarewards.qqgbvpn.mgmtui.model.delivery.DeliveryNoteVO;

/**
 * @author cream
 * 
 */
public interface DeliveryDao {

	/**
	 * Returns all delivery notes.
	 * 
	 * @return
	 */
	public List<DeliveryNoteVO> fetchAllDelivery();

	/**
	 * Returns delivery note list.
	 * 
	 * @param pageInfo
	 * @return
	 */
	public List<DeliveryNoteVO> fetchDeliveryList(int start, int limit);

	/**
	 * Counts the number of delivery note.
	 * 
	 * @return
	 */
	public long countDelivertList();

	/**
	 * Returns delivery note match giving ID.
	 * 
	 * @param id
	 * @return the found entity instance or null if the entity does not exist
	 */
	public DeliveryNoteVO fetchDeliveryById(String id);

	/**
	 * Call em.persist() to save it.
	 * 
	 * @param vo
	 * @return
	 */
	public DeliveryNoteVO save(DeliveryNoteVO vo);
}
