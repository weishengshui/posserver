/**
 * 
 */
package com.chinarewards.qqgbvpn.mgmtui.dao;

import java.util.List;

import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.mgmtui.model.delivery.DeliveryNoteVO;
import com.chinarewards.qqgbvpn.mgmtui.model.delivery.DeliverySearchVO;

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
	 * Search delivery. It will order by create date.
	 * 
	 * @param criteria
	 * @return
	 */
	public PageInfo<DeliveryNoteVO> fetchDeliverys(DeliverySearchVO criteria);

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
	public DeliveryNoteVO create(DeliveryNoteVO vo);

	/**
	 * Call em.merge() to save it.
	 * 
	 * @param vo
	 * @return
	 */
	public DeliveryNoteVO merge(DeliveryNoteVO vo);

	/**
	 * Delete delivery note by ID. It will delete delivery note detail which
	 * belongs to this note.
	 * 
	 * @param id
	 */
	public void deleteById(String id);
}
