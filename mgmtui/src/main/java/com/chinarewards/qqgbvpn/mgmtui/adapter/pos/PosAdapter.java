package com.chinarewards.qqgbvpn.mgmtui.adapter.pos;

import com.chinarewards.qqgbvpn.domain.Pos;
import com.chinarewards.qqgbvpn.domain.status.PosDeliveryStatus;
import com.chinarewards.qqgbvpn.domain.status.PosInitializationStatus;
import com.chinarewards.qqgbvpn.domain.status.PosOperationStatus;
import com.chinarewards.qqgbvpn.mgmtui.model.pos.PosVO;
import com.chinarewards.qqgbvpn.mgmtui.util.Tools;

/**
 * Pos Adapter
 * 
 * @author huangwei
 *
 */
public class PosAdapter {

	public PosVO convertToPosVO(Pos pos){
		if (pos == null) {
			return null;
		}
		PosVO posVO = new PosVO();
		posVO.setDstatus(pos.getDstatus()==null?"":pos.getDstatus().toString());
		posVO.setId(pos.getId());
		posVO.setIstatus(pos.getIstatus()==null?"":pos.getIstatus().toString());
		posVO.setModel(pos.getModel());
		posVO.setOstatus(pos.getOstatus()==null?"":pos.getOstatus().toString());
		posVO.setPosId(pos.getPosId());
		posVO.setSecret(pos.getSecret());
		posVO.setSimPhoneNo(pos.getSimPhoneNo());
		posVO.setSn(pos.getSn());
		return posVO;
		
	}
	
	public Pos convertToPosVO(PosVO posVO){
		if (posVO == null) {
			return null;
		}
		Pos pos = new Pos();
		pos.setId(posVO.getId());
		pos.setDstatus(Tools.isEmptyString(posVO.getDstatus())?null:PosDeliveryStatus.valueOf(posVO.getDstatus()));
		pos.setIstatus(Tools.isEmptyString(posVO.getIstatus())?null:PosInitializationStatus.valueOf(posVO.getIstatus()));
		pos.setModel(posVO.getModel());
		pos.setOstatus(Tools.isEmptyString(posVO.getOstatus())?null:PosOperationStatus.valueOf(posVO.getOstatus()));
		pos.setPosId(posVO.getPosId());
		pos.setSecret(posVO.getSecret());
		pos.setSimPhoneNo(posVO.getSimPhoneNo());
		pos.setSn(posVO.getSn());
		return pos;
	}
}
