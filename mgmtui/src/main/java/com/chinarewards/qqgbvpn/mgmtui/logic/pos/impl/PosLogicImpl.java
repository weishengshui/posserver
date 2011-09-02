package com.chinarewards.qqgbvpn.mgmtui.logic.pos.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.mgmtui.dao.pos.PosDao;
import com.chinarewards.qqgbvpn.mgmtui.logic.exception.LogicException;
import com.chinarewards.qqgbvpn.mgmtui.logic.pos.PosLogic;
import com.chinarewards.qqgbvpn.mgmtui.model.pos.PosSearchVO;
import com.chinarewards.qqgbvpn.mgmtui.model.pos.PosVO;
import com.chinarewards.qqgbvpn.mgmtui.model.util.PaginationTools;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class PosLogicImpl implements PosLogic {
	
	Logger logger = LoggerFactory.getLogger(getClass());

	@Inject
	Provider<PosDao> posDao;
	@Override
	public void deletePosById(String id) throws LogicException{
		posDao.get().deletePosById(id);
	}

	@Override
	public PageInfo<PosVO> queryPos(PosSearchVO posSearchVO,
			PaginationTools paginationTools) {
		return posDao.get().queryPos(posSearchVO, paginationTools);
	}

	@Override
	public PosVO savePos(PosVO posVO) throws LogicException{
		return posDao.get().savePos(posVO);
	}

	@Override
	public void updatePos(PosVO posVO) throws LogicException{
		posDao.get().updatePos(posVO);
	}

	@Override
	public PosVO getPosById(String id) throws LogicException {
		return posDao.get().getPosById(id);
	}

	

}
