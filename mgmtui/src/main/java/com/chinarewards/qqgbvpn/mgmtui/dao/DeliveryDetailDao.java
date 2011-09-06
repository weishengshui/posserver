/**
 * 
 */
package com.chinarewards.qqgbvpn.mgmtui.dao;

import java.util.List;

import com.chinarewards.qqgbvpn.mgmtui.exception.PosNotExistException;
import com.chinarewards.qqgbvpn.mgmtui.model.delivery.DeliveryNoteDetailVO;
import com.chinarewards.qqgbvpn.mgmtui.model.pos.PosVO;

/**
 * @author cream
 * 
 */
public interface DeliveryDetailDao {

	/**
	 * Returns detail list for note.
	 */
	public List<DeliveryNoteDetailVO> fetchDetailListByNoteId(String noteId);

	/**
	 * Returns POS by detail ID.
	 * 
	 * @param detailId
	 * @return
	 */
	public PosVO fetchPosByDetailId(String detailId);

	/**
	 * Return POS list by detail IDs.
	 * 
	 * @param detailIds
	 * @return
	 */
	public List<PosVO> fetchPosByDetailIds(List<String> detailIds);

	/**
	 * Return POS list by note ID.
	 * 
	 * @param noteId
	 * @return
	 */
	public List<PosVO> fetchPosByNoteId(String noteId);

	/**
	 * Return detail by pos ID.
	 * 
	 * @param posId
	 * @return
	 */
	DeliveryNoteDetailVO fetchByPosId(String posId);

	/**
	 * Create by note and pos.
	 * 
	 * @param noteId
	 * @param posId
	 * @return
	 */
	DeliveryNoteDetailVO create(String noteId, String posId)
			throws PosNotExistException;

	/**
	 * Delete detail.
	 * 
	 * @param detailId
	 */
	void delete(String detailId);
}
