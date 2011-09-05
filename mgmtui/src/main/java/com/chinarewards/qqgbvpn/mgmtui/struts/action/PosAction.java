package com.chinarewards.qqgbvpn.mgmtui.struts.action;

import java.util.List;

import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.mgmtui.logic.exception.ParamsException;
import com.chinarewards.qqgbvpn.mgmtui.logic.exception.PosIdIsExitsException;
import com.chinarewards.qqgbvpn.mgmtui.logic.pos.PosLogic;
import com.chinarewards.qqgbvpn.mgmtui.model.pos.PosSearchVO;
import com.chinarewards.qqgbvpn.mgmtui.model.pos.PosVO;
import com.chinarewards.qqgbvpn.mgmtui.struts.BaseAction;
import com.chinarewards.qqgbvpn.mgmtui.util.Tools;

/**
 * pos manager action
 * 
 * @author huangwei
 *
 */
public class PosAction extends BaseAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6936323925311944355L;
	
	private String id;
	
	private PosVO posVO;

	private PosLogic posLogic;
	
	private List<PosVO> posVOList;
	
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
		}
		return SUCCESS;
	}
	
	public String editPos(){
		log.debug("posAction call editPos");
		try {
			if(Tools.isEmptyString(posVO.getId())){
				getPosLogic().savePos(posVO);
			}else{
				getPosLogic().updatePos(posVO);
			}
		} catch (ParamsException e) {
			log.error("editPos fail",e);
			return INPUT;
		}catch (PosIdIsExitsException e) {
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
		log.debug("posAction call list");
		PosSearchVO posSearchVO = new PosSearchVO();
		try{
			PageInfo<PosVO> pageInfo =getPosLogic().queryPos(posSearchVO, null);
			posVOList = pageInfo.getItems();
		}catch(Exception e){
			log.error("list fail",e);
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
		posLogic = getInjector().getInstance(PosLogic.class);
		return posLogic;
	}

	public List<PosVO> getPosVOList() {
		return posVOList;
	}

	public void setPosVOList(List<PosVO> posVOList) {
		this.posVOList = posVOList;
	}
	
	

}
