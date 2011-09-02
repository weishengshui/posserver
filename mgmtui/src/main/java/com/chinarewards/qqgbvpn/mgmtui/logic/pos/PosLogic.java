package com.chinarewards.qqgbvpn.mgmtui.logic.pos;

import java.util.List;

import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.mgmtui.logic.exception.ParamsException;
import com.chinarewards.qqgbvpn.mgmtui.logic.exception.PosIdIsExitsException;
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

	public PosVO getPosById(String id)throws ParamsException;
	
	/**
	 * 保存Pos
	 * 
	 * @param name
	 * @return
	 */
	public PosVO savePos(PosVO posVO) throws PosIdIsExitsException,ParamsException;
	
	/**
	 * 删除Pos
	 * 
	 * @param id
	 */
	public void deletePosById(String id)throws ParamsException;
	
	/**
	 * 更新Pos
	 * 
	 * @param posVO
	 */
	public void updatePos(PosVO posVO)throws PosIdIsExitsException,ParamsException; 
	
	/**
	 * 查询Pos
	 * 
	 * @param posSearchVO
	 * @param paginationTools
	 * @return
	 */
	public PageInfo<PosVO> queryPos(PosSearchVO posSearchVO,PaginationTools paginationTools);
	
}
