package com.chinarewards.qqgbvpn.mgmtui.logic.pos;

import java.util.List;

import javax.persistence.OptimisticLockException;

import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.domain.status.PosDeliveryStatus;
import com.chinarewards.qqgbvpn.domain.status.PosOperationStatus;
import com.chinarewards.qqgbvpn.mgmtui.exception.PosNotExistException;
import com.chinarewards.qqgbvpn.mgmtui.logic.exception.ParamsException;
import com.chinarewards.qqgbvpn.mgmtui.logic.exception.PosIdIsExitsException;
import com.chinarewards.qqgbvpn.mgmtui.logic.exception.SimPhoneNoIsExitsException;
import com.chinarewards.qqgbvpn.mgmtui.model.pos.PosSearchVO;
import com.chinarewards.qqgbvpn.mgmtui.model.pos.PosVO;

/**
 * pos service
 * 
 * @author huangwei
 *
 */
public interface PosLogic {

	/**
	 * 
	 * @param id
	 * @return
	 * @throws ParamsException
	 */
	public PosVO getPosById(String id) throws ParamsException;
	
	/**
	 * description：根据POS编号查找POS
	 * @return
	 * @throws ParamsException
	 * @time 2011-9-13   下午04:16:14
	 * @author Seek
	 */
	public PosVO getPosByPosNum(String posNum) throws ParamsException, PosNotExistException;
	
	/**
	 * 保存Pos
	 * 
	 * @param name
	 * @return
	 */
	public PosVO savePos(PosVO posVO) throws PosIdIsExitsException,ParamsException,SimPhoneNoIsExitsException,OptimisticLockException;
	
	/**
	 * 删除Pos
	 * 
	 * @param id
	 */
	public void deletePosById(String id)throws ParamsException, OptimisticLockException;
	
	/**
	 * 更新Pos
	 * 
	 * @param posVO
	 */
	public void updatePos(PosVO posVO)throws PosIdIsExitsException,ParamsException,SimPhoneNoIsExitsException, OptimisticLockException; 
	
	/**
	 * 查询Pos
	 * 
	 * @param posSearchVO
	 * @return
	 */
	public PageInfo<PosVO> queryPos(PosSearchVO posSearchVO);	
	
	/**
	 * 查询Pos
	 * 
	 * @param posSearchVO
	 * @return
	 */
	public List<PosVO> queryPosByAgentId(String agentId);    

	/**
	 * 批量更新 Pos 状态为 {@link PosDeliveryStatus#DELIVERED} 和 {@link PosOperationStatus#ALLOWED}
	 * @author cream
	 * @param posIds Pos.posId
	 */
	void updatePosStatusToWorking(List<String> posIds) throws OptimisticLockException;
	
	/**
	 * 创建 PosAssignment
	 * 
	 * @param agentId
	 * @param posIds - POS ID Pos.ID
	 * @throws PosNotExistException 
	 */
	void createPosAssignment(String agentId, List<String> posIds) throws PosNotExistException;
}
