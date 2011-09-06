/**
 * 
 */
package com.chinarewards.qqgbvpn.mgmtui.dao;

import java.util.List;

import com.chinarewards.qqgbvpn.domain.DeliveryNote;
import com.chinarewards.qqgbvpn.domain.DeliveryNoteDetail;

/**
 * @author cream
 * 
 */
public interface DeliveryDetailDao {

	/**
	 * Returns detail list for note.
	 */
	public List<DeliveryNoteDetail> fetchDeliveryNoteDetailByNOte(
			DeliveryNote note);
}
