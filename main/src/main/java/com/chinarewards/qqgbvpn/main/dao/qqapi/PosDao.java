/**
 * 
 */
package com.chinarewards.qqgbvpn.main.dao.qqapi;

import java.util.List;

import com.chinarewards.qqgbvpn.domain.Pos;
import com.chinarewards.qqgbvpn.domain.status.PosDeliveryStatus;
import com.chinarewards.qqgbvpn.domain.status.PosInitializationStatus;
import com.chinarewards.qqgbvpn.domain.status.PosOperationStatus;

/**
 * DAO for Pos entity.
 * 
 * @author cream
 * @since 1.0.0 2011-08-26
 */
public interface PosDao {

	public List<Pos> fetchAll();

	public Pos fetchPos(String posId, PosDeliveryStatus dstatus,
			PosInitializationStatus istatus, PosOperationStatus ostatus);

	public Pos createPos(Pos pos);

}
