package com.chinarewards.qqgbvpn.mgmtui.struts.action;

import java.util.List;

import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.domain.status.PosDeliveryStatus;
import com.chinarewards.qqgbvpn.domain.status.PosInitializationStatus;
import com.chinarewards.qqgbvpn.domain.status.PosOperationStatus;
import com.chinarewards.qqgbvpn.mgmtui.logic.exception.ParamsException;
import com.chinarewards.qqgbvpn.mgmtui.logic.exception.PosIdIsExitsException;
import com.chinarewards.qqgbvpn.mgmtui.logic.exception.SimPhoneNoIsExitsException;
import com.chinarewards.qqgbvpn.mgmtui.logic.pos.PosLogic;
import com.chinarewards.qqgbvpn.mgmtui.model.agent.AgentVO;
import com.chinarewards.qqgbvpn.mgmtui.model.pos.PosSearchVO;
import com.chinarewards.qqgbvpn.mgmtui.model.pos.PosVO;
import com.chinarewards.qqgbvpn.mgmtui.model.util.PaginationTools;
import com.chinarewards.qqgbvpn.mgmtui.struts.BasePagingAction;
import com.chinarewards.qqgbvpn.mgmtui.util.Tools;

/**
 * pos manager action
 * 
 * @author huangwei
 *
 */
public class PosAction extends BasePagingAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6936323925311944355L;
	
	private String id;
	
	private PosVO posVO;

	private PosLogic posLogic;
	
	private List<PosVO> posVOList;
	
	private String msg;
	
	private PosSearchVO posSearchVO;
	
	private String posId;

	private String model;

	/**
	 * Serial number.
	 */
	private String sn;

	private String simPhoneNo;

	private String dstatus;

	private String istatus;

	private String ostatus;

	// POS 内置唯一标识。6位字符
	private String secret;
	
	private PageInfo<PosVO> pageInfo;
	
	private static final int SIZE = 10;

	public String detail(){
		log.debug("posAction call detail");
		if(!Tools.isEmptyString(id)){
			try {
				posVO = getPosLogic().getPosById(id);
			} catch (ParamsException e) {
				log.error("getPosById fail",e);
				return ERROR;
			}catch (Exception e) {
				log.error("getPosById fail",e);
				return ERROR;
			}
		}else{
			posVO = new PosVO();
			//默认可运营
			posVO.setOstatus(PosOperationStatus.ALLOWED.toString());
			//默认没有初始化
			posVO.setIstatus(PosInitializationStatus.UNINITED.toString());
			//默认已回收
			posVO.setDstatus(PosDeliveryStatus.RETURNED.toString());
		}
		return SUCCESS;
	}
	
	public String editPos(){
		log.debug("posAction call editPos");
		try {
			posVO.setDstatus(dstatus);
			posVO.setIstatus(istatus);
			if(Tools.isEmptyString(posVO.getId())){
				getPosLogic().savePos(posVO);
			}else{
				getPosLogic().updatePos(posVO);
			}
		} catch (ParamsException e) {
			log.error("editPos fail",e);
			return INPUT;
		}catch (PosIdIsExitsException e) {
			msg = "POS机编号已存在";
			log.error("editPos fail",e);
			return INPUT;
		}catch (SimPhoneNoIsExitsException e) {
			msg = "电机号码已存在";
			log.error("editPos fail",e);
			return INPUT;
		}catch (Exception e) {
			log.error("editPos fail",e);
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String delPos(){
		log.debug("posAction call delPos");
		try {
			getPosLogic().deletePosById(id);
		} catch (ParamsException e) {
			log.error("deletePosById fail",e);
			return ERROR;
		}catch (Exception e) {
			log.error("delPos fail",e);
			return ERROR;
		}
		return SUCCESS;
	}
	
	@Override
	public String execute(){
		if (pageInfo == null) {
			pageInfo = new PageInfo<PosVO>();
			pageInfo.setPageId(1);
			pageInfo.setPageSize(SIZE);
		}
		
		if(Tools.isEmptyString(posSearchVO)){
			posSearchVO = new PosSearchVO();
		}  
		posSearchVO.setDstatus(dstatus);
		posSearchVO.setIstatus(istatus);
		posSearchVO.setModel(model);
		posSearchVO.setOstatus(ostatus);
		posSearchVO.setPosId(posId);
		posSearchVO.setSecret(secret);
		posSearchVO.setSimPhoneNo(simPhoneNo);
		posSearchVO.setSn(sn);
		
		posSearchVO.setSize(pageInfo.getPageSize());
		posSearchVO.setPage(pageInfo.getPageId());
		log.debug("posAction call list=======:"+Tools.objToString(posSearchVO));
		
		try{
			pageInfo = getPosLogic().queryPos(posSearchVO);
		}catch(Exception e){
			log.error("list fail",e);
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}
	
	//---------------------------------------------------//

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public PosVO getPosVO() {
		return posVO;
	}

	public void setPosVO(PosVO posVO) {
		this.posVO = posVO;
	}

	private PosLogic getPosLogic() {
		posLogic = super.getInstance(PosLogic.class);
		return posLogic;
	}

	public List<PosVO> getPosVOList() {
		return posVOList;
	}

	public void setPosVOList(List<PosVO> posVOList) {
		this.posVOList = posVOList;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public PosSearchVO getPosSearchVO() {
		return posSearchVO;
	}

	public void setPosSearchVO(PosSearchVO posSearchVO) {
		this.posSearchVO = posSearchVO;
	}

	public String getPosId() {
		return posId;
	}

	public void setPosId(String posId) {
		this.posId = posId;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getSimPhoneNo() {
		return simPhoneNo;
	}

	public void setSimPhoneNo(String simPhoneNo) {
		this.simPhoneNo = simPhoneNo;
	}

	public String getDstatus() {
		return dstatus;
	}

	public void setDstatus(String dstatus) {
		this.dstatus = dstatus;
	}

	public String getIstatus() {
		return istatus;
	}

	public void setIstatus(String istatus) {
		this.istatus = istatus;
	}

	public String getOstatus() {
		return ostatus;
	}

	public void setOstatus(String ostatus) {
		this.ostatus = ostatus;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}
	
	public PageInfo<PosVO> getPageInfo() {
		return pageInfo;
	}

	public void setPageInfo(PageInfo<PosVO> pageInfo) {
		this.pageInfo = pageInfo;
	}

}
