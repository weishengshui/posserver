package com.chinarewards.qqgbvpn.mgmtui.adapter.pos;

import com.chinarewards.qqgbvpn.domain.Pos;
import com.chinarewards.qqgbvpn.mgmtui.model.pos.PosVO;

/**
 * Pos Adapter
 * 
 * @author huangwei
 *
 */
public class PosAdapter {

	public PosVO convertToPosVO(Pos pos){
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
}
