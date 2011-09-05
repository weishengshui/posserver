package com.chinarewards.qqgbvpn.mgmtui.struts.action;

import java.util.List;

import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.mgmtui.logic.exception.ParamsException;
import com.chinarewards.qqgbvpn.mgmtui.logic.exception.PosIdIsExitsException;
import com.chinarewards.qqgbvpn.mgmtui.logic.pos.PosLogic;
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
			msg = "PosId已存在";
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
		return SUCCESS;
	}
	
	
	public String listCenter(){
		log.debug("posAction call listCenter");
		PosSearchVO posSearchVO = new PosSearchVO();
		PaginationTools paginationTools = new PaginationTools(page, countEachPage);
		try{
			PageInfo<PosVO> pageInfo =getPosLogic().queryPos(posSearchVO, paginationTools);
			posVOList = pageInfo.getItems();
			this.insertPageList(pageInfo.getRecordCount(), page);
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

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	

}
