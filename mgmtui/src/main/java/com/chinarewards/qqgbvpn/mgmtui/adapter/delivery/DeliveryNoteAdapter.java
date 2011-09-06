package com.chinarewards.qqgbvpn.mgmtui.adapter.delivery;

import java.util.LinkedList;
import java.util.List;

import com.chinarewards.qqgbvpn.domain.Agent;
import com.chinarewards.qqgbvpn.domain.DeliveryNote;
import com.chinarewards.qqgbvpn.domain.status.DeliveryNoteStatus;
import com.chinarewards.qqgbvpn.mgmtui.model.agent.AgentVO;
import com.chinarewards.qqgbvpn.mgmtui.model.delivery.DeliveryNoteVO;

/**
 * Delivery note Adapter
 * 
 * @author Cream
 * 
 */
public class DeliveryNoteAdapter {

	public DeliveryNoteVO convertToVO(DeliveryNote entity) {
		if (entity == null) {
			return null;
		}
		DeliveryNoteVO vo = new DeliveryNoteVO();

		Agent agent = entity.getAgent();
		AgentVO agentVo = null;
		if (agent != null) {
			agentVo = new AgentVO();
			agentVo.setEmail(agent.getEmail());
			agentVo.setId(agent.getId());
			agentVo.setName(agent.getName());
		}

		vo.setAgent(agentVo);
		vo.setAgentName(entity.getAgentName());
		vo.setConfirmDate(entity.getConfirmDate());
		vo.setCreateDate(entity.getCreateDate());
		vo.setDnNumber(entity.getDnNumber());
		vo.setId(entity.getId());
		vo.setPrintDate(entity.getPrintDate());
		vo.setStatus(entity.getStatus().toString());

		return vo;

	}

	public List<DeliveryNoteVO> convertToVO(List<DeliveryNote> entityList) {
		if (entityList == null) {
			return null;
		}

		List<DeliveryNoteVO> resultList = new LinkedList<DeliveryNoteVO>();
		for (DeliveryNote entity : entityList) {
			resultList.add(this.convertToVO(entity));
		}

		return resultList;
	}

	public DeliveryNote convertToEntity(DeliveryNoteVO vo) {
		if (vo == null) {
			return null;
		}

		DeliveryNote entity = new DeliveryNote();

		AgentVO agentVo = vo.getAgent();
		Agent agent = null;
		if (agentVo != null) {
			agent = new Agent();
			agent.setEmail(agentVo.getEmail());
			agent.setId(agentVo.getId());
			agent.setName(agentVo.getName());
		}

		entity.setAgent(agent);
		entity.setAgentName(vo.getAgentName());
		entity.setConfirmDate(vo.getConfirmDate());
		entity.setCreateDate(vo.getCreateDate());
		entity.setDnNumber(vo.getDnNumber());
		entity.setId(vo.getId());
		entity.setPrintDate(vo.getPrintDate());
		entity.setStatus(DeliveryNoteStatus.valueOf(vo.getStatus()));

		return entity;
	}

	public List<DeliveryNote> convertToEntity(List<DeliveryNoteVO> voList) {
		if (voList == null) {
			return null;
		}

		List<DeliveryNote> resultList = new LinkedList<DeliveryNote>();
		for (DeliveryNoteVO vo : voList) {
			resultList.add(this.convertToEntity(vo));
		}
		return resultList;
	}
}
