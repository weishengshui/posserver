package com.chinarewards.qqgbvpn.mgmtui.logic.pos.impl;

import java.util.List;

import javax.persistence.OptimisticLockException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.mgmtui.dao.pos.PosDao;
import com.chinarewards.qqgbvpn.mgmtui.exception.PosNotExistException;
import com.chinarewards.qqgbvpn.mgmtui.logic.exception.ParamsException;
import com.chinarewards.qqgbvpn.mgmtui.logic.exception.PosIdIsExitsException;
import com.chinarewards.qqgbvpn.mgmtui.logic.exception.SimPhoneNoIsExitsException;
import com.chinarewards.qqgbvpn.mgmtui.logic.pos.PosLogic;
import com.chinarewards.qqgbvpn.mgmtui.model.pos.PosSearchVO;
import com.chinarewards.qqgbvpn.mgmtui.model.pos.PosVO;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;

public class PosLogicImpl implements PosLogic {
	
	Logger logger = LoggerFactory.getLogger(getClass());

	@Inject
	Provider<PosDao> posDao;
	
	@Transactional
	@Override
	public void deletePosById(String id) throws ParamsException, OptimisticLockException{
		
		// TODO
		
		posDao.get().deletePosById(id);
	}

	@Override
	public PageInfo<PosVO> queryPos(PosSearchVO posSearchVO) {
		return posDao.get().queryPos(posSearchVO);
	}

	@Override
	public List<PosVO> queryPosByAgentId(String agentId) {
		return posDao.get().queryPosByAgentId(agentId);
	}

	@Transactional
	@Override
	public PosVO savePos(PosVO posVO) throws PosIdIsExitsException,ParamsException,SimPhoneNoIsExitsException, OptimisticLockException{
		return posDao.get().savePos(posVO);
	}

	@Transactional
	@Override
	public void updatePos(PosVO posVO) throws PosIdIsExitsException,ParamsException,SimPhoneNoIsExitsException, OptimisticLockException{
		posDao.get().updatePos(posVO);
	}

	@Transactional
	@Override
	public PosVO getPosById(String id) throws ParamsException {
		return posDao.get().getPosById(id);
	}

	@Transactional
	@Override
	public void updatePosStatusToWorking(List<String> posIds) throws OptimisticLockException{
		posDao.get().updatePosStatusToWorking(posIds);
	}

	@Transactional
	@Override
	public void createPosAssignment(String agentId, List<String> posIds) throws PosNotExistException {
		posDao.get().createPosAssignment(agentId, posIds);
	}

	@Override
	public PosVO getPosByPosNum(String posNum) throws ParamsException,PosNotExistException {
		return posDao.get().getPosByPosNum(posNum);
	}

}
