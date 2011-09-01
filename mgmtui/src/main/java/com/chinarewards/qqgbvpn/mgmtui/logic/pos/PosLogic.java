package com.chinarewards.qqgbvpn.mgmtui.logic.pos;

import java.util.List;

import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.mgmtui.logic.exception.LogicException;
import com.chinarewards.qqgbvpn.mgmtui.model.pos.PosSearchVO;
import com.chinarewards.qqgbvpn.mgmtui.model.pos.PosVO;
import com.chinarewards.qqgbvpn.mgmtui.model.util.PaginationTools;

/**
 * pos service
 * 
 * @author huangwei
 *
 */
public interface PosLogic {

	public PosVO getPosById(String id)throws LogicException;
	
	/**
	 * 保存Pos
	 * 
	 * @param name
	 * @return
	 */
	public PosVO savePos(PosVO posVO) throws LogicException;
	
	/**
	 * 删除Pos
	 * 
	 * @param id
	 */
	public void deletePosById(String id)throws LogicException;
	
	/**
	 * 更新Pos
	 * 
	 * @param posVO
	 */
	public void updatePos(PosVO posVO)throws LogicException; 
	
	/**
	 * 查询Pos
	 * 
	 * @param posSearchVO
	 * @param paginationTools
	 * @return
	 */
	public PageInfo<PosVO> queryPos(PosSearchVO posSearchVO,PaginationTools paginationTools);
	
}
