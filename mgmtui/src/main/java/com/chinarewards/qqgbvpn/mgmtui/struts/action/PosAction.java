package com.chinarewards.qqgbvpn.mgmtui.struts.action;

import java.util.ArrayList;
import java.util.List;

import com.chinarewards.qqgbvpn.mgmtui.logic.exception.LogicException;
import com.chinarewards.qqgbvpn.mgmtui.logic.pos.PosLogic;
import com.chinarewards.qqgbvpn.mgmtui.model.pos.PosSearchVO;
import com.chinarewards.qqgbvpn.mgmtui.model.pos.PosVO;
import com.chinarewards.qqgbvpn.mgmtui.struts.BaseAction;
import com.chinarewards.qqgbvpn.mgmtui.util.Tools;
import com.google.inject.Inject;

/**
 * pos manager action
 * 
 * @author huangwei
 *
 */
public class PosAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6936323925311944355L;
	
	private final  PosLogic posLogic;
	
	
	private String id;
	
	private PosVO posVO;
	
	@Inject
	public PosAction(PosLogic posLogic){
		this.posLogic = posLogic;
	}
	
	public String detail(){
		log.debug("posAction call detail");
		try {
			posVO = posLogic.getPosById(id);
		} catch (LogicException e) {
			log.error("getPosById fail",e);
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String editPos(){
		log.debug("posAction call editPos");
		PosVO posVO = new PosVO();
		try {
			if(Tools.isEmptyString(id)){
				posLogic.savePos(posVO);
			}else{
				posLogic.updatePos(posVO);
			}
		} catch (LogicException e) {
			log.error("editPos fail",e);
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String delPos(){
		log.debug("posAction call delPos");
		try {
			posLogic.deletePosById(id);
		} catch (LogicException e) {
			log.error("deletePosById fail",e);
			return ERROR;
		}
		return SUCCESS;
	}
	
	@Override
	public String execute(){
		log.debug("posAction call list");
		PosSearchVO posSearchVO = new PosSearchVO();
		posLogic.queryPos(posSearchVO, null);
		return SUCCESS;
	}

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

	public PosLogic getPosLogic() {
		return posLogic;
	}
	
	//---------------------------------------------------//
	

}
