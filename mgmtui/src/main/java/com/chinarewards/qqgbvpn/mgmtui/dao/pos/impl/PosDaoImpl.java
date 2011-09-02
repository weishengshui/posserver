package com.chinarewards.qqgbvpn.mgmtui.dao.pos.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.domain.Pos;
import com.chinarewards.qqgbvpn.mgmtui.dao.pos.PosDao;
import com.chinarewards.qqgbvpn.mgmtui.logic.exception.LogicException;
import com.chinarewards.qqgbvpn.mgmtui.model.pos.PosSearchVO;
import com.chinarewards.qqgbvpn.mgmtui.model.pos.PosVO;
import com.chinarewards.qqgbvpn.mgmtui.model.util.PaginationTools;
import com.chinarewards.qqgbvpn.mgmtui.util.Tools;
import com.google.inject.Inject;

/**
 * pos dao
 * 
 * @author huangwei
 * 
 */
public class PosDaoImpl implements PosDao {

	Logger log = LoggerFactory.getLogger(PosDaoImpl.class);

	@Inject
	EntityManager em;

	public EntityManager getEm() {
		return em;
	}

	@Override
	public void deletePosById(String id) throws LogicException {
		log.trace("calling deletePosById start and params is {}", id);
		if (Tools.isEmptyString(id)) {
			throw new LogicException("id is null");
		}
		Pos pos = getEm().find(Pos.class, id);
		if(pos != null){
			getEm().remove(pos);
		}
		log.trace("calling deletePosById end ");
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageInfo<PosVO> queryPos(PosSearchVO posSearchVO,
			PaginationTools paginationTools) {
		log
				.trace(
						"calling queryPos start and params is  posSearchVO:({}),paginationTools:({})",
						new Object[] { Tools.objToString(posSearchVO),
								Tools.objToString(paginationTools) });
		int recordCount = this.countPos(posSearchVO);
		PageInfo<PosVO> pageinfo = new PageInfo<PosVO>();
		pageinfo.setRecordCount(recordCount);

		StringBuffer hql = new StringBuffer();
		hql.append("SELECT p FROM Pos p WHERE 1=1 ");

		Query query = getEm().createQuery(hql.toString());
		if (paginationTools != null) {
			query = query.setFirstResult(paginationTools.getStartIndex())
					.setMaxResults(paginationTools.getCountOnEachPage());
		}
		List<Pos> posList = query.getResultList();
		List<PosVO> posVOList = new ArrayList<PosVO>();
		for (Pos pos : posList) {
			PosVO posVO = new PosVO();
			posVO.setDstatus(pos.getDstatus());
			posVO.setId(pos.getId());
			posVO.setIstatus(pos.getIstatus());
			posVO.setModel(pos.getModel());
			posVO.setOstatus(pos.getOstatus());
			posVO.setPosId(pos.getPosId());
			posVO.setSecret(pos.getSecret());
			posVO.setSimPhoneNo(pos.getSimPhoneNo());
			posVO.setSn(pos.getSn());
			posVOList.add(posVO);
		}
		pageinfo.setItems(posVOList);
		log.trace("calling queryPos end and result is :({})", Tools
				.objToString(pageinfo));
		return pageinfo;
	}

	private int countPos(PosSearchVO posSearchVO) {
		log.trace("calling countPos start and params is  posSearchVO:({})",
				Tools.objToString(posSearchVO));
		StringBuffer hql = new StringBuffer();
		hql.append("SELECT count(p.id) FROM Pos p WHERE 1=1 ");

		int count = ((Long) getEm().createQuery(hql.toString())
				.getSingleResult()).intValue();
		log.trace("calling queryPos end and result is :({})", count);
		return count;
	}

	@Override
	public PosVO savePos(PosVO posVO) throws LogicException {
		log.trace("calling savePos start and params is {}", Tools
				.objToString(posVO));
		if (Tools.isEmptyString(posVO)) {
			throw new LogicException("posVO is null");
		}
		if (Tools.isEmptyString(posVO.getPosId())) {
			throw new LogicException("posId is null");
		}
		if (posIdIsExits(posVO.getPosId())) {
			throw new LogicException("posId is exits");
		}
		Pos pos = new Pos();
		pos.setDstatus(posVO.getDstatus());
		pos.setIstatus(posVO.getIstatus());
		pos.setModel(posVO.getModel());
		pos.setOstatus(posVO.getOstatus());
		pos.setPosId(posVO.getPosId());
		pos.setSecret(posVO.getSecret());
		pos.setSimPhoneNo(posVO.getSimPhoneNo());
		pos.setSn(posVO.getSn());
		getEm().persist(pos);
		posVO.setId(pos.getId());
		log.trace("calling savePos end and result is :({})", Tools
				.objToString(posVO));
		return posVO;
	}

	private boolean posIdIsExits(String posId) {
		long count = (Long) getEm().createQuery(
				"select count(p.id) from Pos p where p.posId = :posId")
				.setParameter("posId", posId).getSingleResult();
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void updatePos(PosVO posVO) throws LogicException {
		log.trace("calling updatePos start and params is {}", Tools
				.objToString(posVO));
		if (Tools.isEmptyString(posVO)) {
			throw new LogicException("posVO is null");
		}
		if (Tools.isEmptyString(posVO.getId())) {
			throw new LogicException("id is null");
		}

		if (Tools.isEmptyString(posVO.getPosId())) {
			throw new LogicException("posId is null");
		}

		long count = (Long) getEm()
				.createQuery(
						"select count(p.id) from Pos p where p.posId = :posId and p.id != :id")
				.setParameter("posId", posVO.getPosId()).setParameter("id",
						posVO.getId()).getSingleResult();
		if (count > 0) {
			throw new LogicException("posId is exits");
		}

		Pos pos = getEm().find(Pos.class, posVO.getId());
		if (pos == null) {
			throw new LogicException("pos is not exits,id is:" + posVO.getId());
		}
		pos.setDstatus(posVO.getDstatus());
		pos.setIstatus(posVO.getIstatus());
		pos.setModel(posVO.getModel());
		pos.setOstatus(posVO.getOstatus());
		pos.setPosId(posVO.getPosId());
		pos.setSecret(posVO.getSecret());
		pos.setSimPhoneNo(posVO.getSimPhoneNo());
		pos.setSn(posVO.getSn());
		getEm().merge(pos);
		log.trace("calling savePos end ");
	}

	@Override
	public PosVO getPosById(String id) throws LogicException {
		log.trace("calling getPosById start and params is {}", id);
		if (Tools.isEmptyString(id)) {
			throw new LogicException("id is null");
		}
		Pos pos = getEm().find(Pos.class, id);
		if (pos != null) {
			PosVO posVO = new PosVO();
			posVO.setDstatus(pos.getDstatus());
			posVO.setId(pos.getId());
			posVO.setIstatus(pos.getIstatus());
			posVO.setModel(pos.getModel());
			posVO.setOstatus(pos.getOstatus());
			posVO.setPosId(pos.getPosId());
			posVO.setSecret(pos.getSecret());
			posVO.setSimPhoneNo(pos.getSimPhoneNo());
			posVO.setSn(pos.getSn());
			return posVO;
		} else {
			return null;
		}

	}

}
