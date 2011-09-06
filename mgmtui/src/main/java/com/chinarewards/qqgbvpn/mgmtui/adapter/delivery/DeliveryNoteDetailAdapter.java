package com.chinarewards.qqgbvpn.mgmtui.adapter.delivery;

import java.util.LinkedList;
import java.util.List;

import com.chinarewards.qqgbvpn.domain.DeliveryNoteDetail;
import com.chinarewards.qqgbvpn.mgmtui.model.delivery.DeliveryNoteDetailVO;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Delivery note Adapter
 * 
 * @author Cream
 * 
 */
public class DeliveryNoteDetailAdapter {

	@Inject
	Provider<DeliveryNoteAdapter> dnAdapter;

	public DeliveryNoteDetailVO convertToVO(DeliveryNoteDetail entity) {
		if (entity == null) {
			return null;
		}

		DeliveryNoteDetailVO vo = new DeliveryNoteDetailVO();

		vo.setDn(dnAdapter.get().convertToVO(entity.getDn()));
		vo.setId(entity.getId());
		vo.setModel(entity.getModel());
		vo.setPosId(entity.getPosId());
		vo.setSimPhoneNo(entity.getSimPhoneNo());
		vo.setSn(entity.getSn());

		return vo;

	}

	public List<DeliveryNoteDetailVO> convertToVO(
			List<DeliveryNoteDetail> entityList) {
		if (entityList == null) {
			return null;
		}

		List<DeliveryNoteDetailVO> resultList = new LinkedList<DeliveryNoteDetailVO>();
		for (DeliveryNoteDetail entity : entityList) {
			resultList.add(this.convertToVO(entity));
		}
		return resultList;
	}

	public DeliveryNoteDetail convertToEntity(DeliveryNoteDetailVO vo) {
		if (vo == null) {
			return null;
		}

		DeliveryNoteDetail entity = new DeliveryNoteDetail();

		entity.setDn(dnAdapter.get().convertToEntity(vo.getDn()));
		entity.setId(vo.getId());
		entity.setModel(vo.getModel());
		entity.setPosId(vo.getPosId());
		entity.setSimPhoneNo(vo.getSimPhoneNo());
		entity.setSn(vo.getSn());

		return entity;
	}

	public List<DeliveryNoteDetail> convertToEntity(
			List<DeliveryNoteDetailVO> voList) {
		if (voList == null) {
			return null;
		}
		List<DeliveryNoteDetail> resultList = new LinkedList<DeliveryNoteDetail>();
		for (DeliveryNoteDetailVO vo : voList) {
			resultList.add(this.convertToEntity(vo));
		}
		return resultList;
	}
}
