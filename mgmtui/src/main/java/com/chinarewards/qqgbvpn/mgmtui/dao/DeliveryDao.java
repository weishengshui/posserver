/**
 * 
 */
package com.chinarewards.qqgbvpn.mgmtui.dao;

import java.util.List;

import com.chinarewards.qqgbvpn.domain.DeliveryNote;

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
	public List<DeliveryNote> fetchAllDelivery();

	/**
	 * Returns delivery note list.
	 * 
	 * @param pageInfo
	 * @return
	 */
	public List<DeliveryNote> fetchDeliveryList(int start, int limit);

	/**
	 * Counts the number of delivery note.
	 * 
	 * @return
	 */
	public long countDelivertList();
}
